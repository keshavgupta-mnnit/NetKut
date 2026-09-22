package com.kglabs28.netkut.vpn

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.ServiceInfo
import android.net.VpnService
import android.os.Build
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.kglabs28.netkut.MainActivity
import com.kglabs28.netkut.NetKutApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.FileInputStream
import java.util.concurrent.atomic.AtomicBoolean

class NetCutVpnService : VpnService() {

    companion object {
        private const val TAG = "NetCutVpnService"
        private const val NOTIFICATION_CHANNEL_ID = "netcut_vpn_channel"
        private const val NOTIFICATION_ID = 1
        const val ACTION_DISCONNECT = "com.kglabs28.netkut.DISCONNECT_VPN"
    }

    private var vpnInterface: ParcelFileDescriptor? = null
    private var vpnThread: Thread? = null
    private val isRunning = AtomicBoolean(false)
    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForegroundServiceWithNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_DISCONNECT) {
            stopSelf()
            return START_NOT_STICKY
        }
        
        startForegroundServiceWithNotification()

        val repository = (application as NetKutApplication).container.blocklistRepository
        
        serviceScope.launch {
            repository.blockedPackages.collectLatest { packages ->
                if (packages.isEmpty()) {
                    Log.d(TAG, "Blocklist is empty. Stopping VPN.")
                    stopVpn()
                    stopSelf()
                } else {
                    Log.d(TAG, "Blocklist updated: $packages")
                    restartVpn(packages)
                }
            }
        }
        
        return START_STICKY
    }

    private fun startForegroundServiceWithNotification() {
        val disconnectIntent = Intent(this, NetCutVpnService::class.java).apply {
            action = ACTION_DISCONNECT
        }
        val pendingDisconnectIntent = PendingIntent.getService(
            this, 0, disconnectIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val mainActivityIntent = Intent(this, MainActivity::class.java)
        val pendingMainActivityIntent = PendingIntent.getActivity(
            this, 0, mainActivityIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val notification: Notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("NetKut is active")
            .setContentText("Dropping traffic for blocked apps.")
            .setSmallIcon(android.R.drawable.ic_secure) // fallback to standard icon
            .setContentIntent(pendingMainActivityIntent)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Disable", pendingDisconnectIntent)
            .setOngoing(true)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            ServiceCompat.startForeground(
                this,
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "NetKut VPN Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    private fun restartVpn(blockedPackages: Set<String>) {
        val builder = Builder()
            .addAddress("10.0.0.2", 32)
            .addRoute("0.0.0.0", 0)
            .setSession("NetKut")
            .setBlocking(true)
        
        for (pkg in blockedPackages) {
            try {
                builder.addAllowedApplication(pkg)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to add package to VPN: $pkg", e)
            }
        }

        synchronized(this) {
            val oldInterface = vpnInterface
            try {
                val newInterface = builder.establish()
                if (newInterface == null) {
                    Log.e(TAG, "VPN is not prepared or was revoked.")
                    stopVpn()
                    stopSelf()
                    return
                }
                vpnInterface = newInterface
                Log.d(TAG, "VPN interface established")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to establish VPN interface", e)
                return
            }

            oldInterface?.close()

            if (!isRunning.get() || vpnThread == null || !vpnThread!!.isAlive) {
                startReadingFromVpn()
            }
        }
    }

    private fun startReadingFromVpn() {
        isRunning.set(true)
        vpnThread = Thread {
            val buffer = ByteArray(32767)
            var currentInterface = vpnInterface
            var inputStream: FileInputStream? = currentInterface?.fileDescriptor?.let { FileInputStream(it) }
            
            while (isRunning.get()) {
                try {
                    val latestInterface = vpnInterface
                    if (latestInterface != null && latestInterface != currentInterface) {
                        currentInterface = latestInterface
                        inputStream = FileInputStream(currentInterface.fileDescriptor)
                    }

                    if (inputStream != null && currentInterface != null) {
                        val length = inputStream.read(buffer)
                        if (length > 0) {
                            // Traffic is dropped by simply doing nothing with the read data
                        } else if (length < 0) {
                            // Interface closed
                            Thread.sleep(100)
                        }
                    } else {
                        Thread.sleep(1000)
                    }
                } catch (e: InterruptedException) {
                    break
                } catch (e: Exception) {
                    Log.e(TAG, "Error in VPN read loop", e)
                    try {
                        Thread.sleep(500)
                    } catch (ie: InterruptedException) {
                        break
                    }
                }
            }
        }.apply { start() }
    }

    private fun stopVpn() {
        isRunning.set(false)
        vpnThread?.interrupt()
        vpnThread = null
        synchronized(this) {
            try {
                vpnInterface?.close()
            } catch (e: Exception) {
                Log.e(TAG, "Error closing VPN interface", e)
            }
            vpnInterface = null
        }
    }

    override fun onDestroy() {
        stopVpn()
        serviceScope.cancel()
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        super.onDestroy()
    }
}
