package com.kglabs28.netkut.worker

import android.content.Context
import android.content.Intent
import android.net.VpnService
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kglabs28.netkut.NetKutApplication
import com.kglabs28.netkut.vpn.NetCutVpnService
import kotlinx.coroutines.flow.first

class VpnSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val app = applicationContext as? NetKutApplication ?: return Result.failure()
        val blocklistRepository = app.container.blocklistRepository
        
        try {
            val blockedApps = blocklistRepository.blockedPackages.first()
            if (blockedApps.isNotEmpty()) {
                val vpnIntent = VpnService.prepare(applicationContext)
                if (vpnIntent == null) {
                    val intent = Intent(applicationContext, NetCutVpnService::class.java)
                    applicationContext.startService(intent)
                }
            } else {
                val intent = Intent(applicationContext, NetCutVpnService::class.java).apply {
                    action = NetCutVpnService.ACTION_DISCONNECT
                }
                applicationContext.startService(intent)
            }
            return Result.success()
        } catch (e: Exception) {
            return Result.retry()
        }
    }
}
