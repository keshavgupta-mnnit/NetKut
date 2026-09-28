package com.kglabs28.netkut

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.kglabs28.netkut.data.repository.AppRepositoryImpl
import com.kglabs28.netkut.data.repository.BlocklistRepositoryImpl
import com.kglabs28.netkut.domain.repository.AppRepository
import com.kglabs28.netkut.domain.repository.BlocklistRepository
import com.kglabs28.netkut.util.ShortcutUtils
import com.kglabs28.netkut.worker.VpnSyncWorker
import java.util.concurrent.TimeUnit

interface AppContainer {
    val appRepository: AppRepository
    val blocklistRepository: BlocklistRepository
}

class DefaultAppContainer(private val application: Application) : AppContainer {
    override val appRepository: AppRepository by lazy {
        AppRepositoryImpl(application)
    }
    
    override val blocklistRepository: BlocklistRepository by lazy {
        BlocklistRepositoryImpl(application)
    }
}

class NetKutApplication : Application() {
    lateinit var container: AppContainer
        private set
    
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
        
        setupWorkManager()
        ShortcutUtils.setupAppShortcuts(this)
    }

    private fun setupWorkManager() {
        val syncRequest = PeriodicWorkRequestBuilder<VpnSyncWorker>(15, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "VpnSyncWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )
    }
}
