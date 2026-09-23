package com.kglabs28.netkut.util

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.kglabs28.netkut.worker.VpnSyncWorker
import java.util.concurrent.TimeUnit

object WorkManagerUtils {
    fun updatePeriodicSync(context: Context, minutes: Long) {
        if (minutes <= 0 || !PreferenceUtils.getBoolean(context, AppConstants.KEY_VPN_ACTIVE, false)) {
            cancelPeriodicSync(context)
        } else {
            val interval = maxOf(AppConstants.MIN_WORK_MANAGER_INTERVAL_MINUTES, minutes)
            val syncRequest = PeriodicWorkRequestBuilder<VpnSyncWorker>(interval, TimeUnit.MINUTES).build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                AppConstants.WORK_MANAGER_VPN_SYNC_NAME,
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                syncRequest
            )
        }
    }

    fun cancelPeriodicSync(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(AppConstants.WORK_MANAGER_VPN_SYNC_NAME)
    }
}
