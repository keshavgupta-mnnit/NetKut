package com.kglabs28.netkut.vpn

import android.R
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
import com.kglabs28.netkut.util.AppUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NetCutVpnService : VpnService() {

    companion object {
        private const val TAG = "NetCutVpnService"
        private const val NOTIFICATION_CHANNEL_ID = "netcut_vpn_channel"
        private const val NOTIFICATION_ID = 1
        const val ACTION_DISCONNECT = "com.kglabs28.netkut.DISCONNECT_VPN"
    }

    private val vpnLock = Any()
    @Volatile
    private var isStopping = false
    private var vpnInterface: ParcelFileDescriptor? = null
    private var collectorJob: Job? = null
    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForegroundServiceWithNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_DISCONNECT || !AppUtils.getVpnActive(applicationContext)) {
            Log.d(TAG, "Disconnect requested or VPN inactive. Shutting down service.")
            shutdown()
            ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
            stopSelf()
            return START_NOT_STICKY
        }

        isStopping = false
        startForegroundServiceWithNotification()

        if (collectorJob == null || collectorJob?.isActive == false) {
            collectorJob = serviceScope.launch {
                val repository = (application as NetKutApplication).container.blocklistRepository
                repository.blockedPackages.collectLatest { packages ->
                    if (packages.isEmpty()) {
                        Log.d(TAG, "Blocklist is empty. Stopping VPN.")
                        shutdown()
                        ServiceCompat.stopForeground(this@NetCutVpnService, ServiceCompat.STOP_FOREGROUND_REMOVE)
                        stopSelf()
                    } else {
                        Log.d(TAG, "Blocklist updated: $packages")
                        applyVpn(packages)
                    }
                }
            }
        }

        return START_NOT_STICKY
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
            .setSmallIcon(R.drawable.ic_secure)
            .setContentIntent(pendingMainActivityIntent)
            .addAction(R.drawable.ic_menu_close_clear_cancel, "Disable", pendingDisconnectIntent)
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

    private fun applyVpn(blockedPackages: Set<String>) {
        synchronized(vpnLock) {
            if (isStopping) return

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

            val newInterface = try {
                builder.establish()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to establish VPN interface", e)
                null
            }

            if (newInterface == null) {
                Log.e(TAG, "VPN is not prepared or was revoked.")
                shutdown()
                stopSelf()
                return
            }

            val oldInterface = vpnInterface
            vpnInterface = newInterface

            try {
                oldInterface?.close()
            } catch (e: Exception) {
                Log.e(TAG, "Error closing old VPN interface", e)
            }
        }
    }

    private fun shutdown() {
        isStopping = true
        collectorJob?.cancel()
        collectorJob = null

        synchronized(vpnLock) {
            try {
                vpnInterface?.close()
            } catch (e: Exception) {
                Log.e(TAG, "Error closing VPN interface", e)
            }
            vpnInterface = null
        }
    }

    override fun onRevoke() {
        Log.d(TAG, "VPN permission revoked by system/user.")
        AppUtils.setVpnActive(applicationContext, false)
        shutdown()
        stopSelf()
        super.onRevoke()
    }

    override fun onDestroy() {
        shutdown()
        serviceScope.cancel()
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        super.onDestroy()
    }
}
