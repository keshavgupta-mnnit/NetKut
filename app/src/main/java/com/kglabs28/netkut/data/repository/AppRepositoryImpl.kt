package com.kglabs28.netkut.data.repository

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import com.kglabs28.netkut.domain.model.AppInfo
import com.kglabs28.netkut.domain.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepositoryImpl(private val context: Context) : AppRepository {

    @Volatile
    private var cachedApps: List<AppInfo>? = null

    override suspend fun getInstalledApps(includeSystemApps: Boolean): List<AppInfo> = withContext(Dispatchers.IO) {
        val apps = cachedApps ?: fetchInstalledApps().also { cachedApps = it }
        
        if (includeSystemApps) {
            apps
        } else {
            apps.filter { !it.isSystemApp }
        }
    }

    private fun fetchInstalledApps(): List<AppInfo> {
        val packageManager = context.packageManager
        val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        
        return installedApps.map { appInfo ->
            val isSystem = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0 && (appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0
            val category = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                appInfo.category
            } else {
                -1
            }
            AppInfo(
                packageName = appInfo.packageName,
                appName = packageManager.getApplicationLabel(appInfo).toString(),
                icon = packageManager.getApplicationIcon(appInfo),
                isSystemApp = isSystem,
                category = category
            )
        }.sortedBy { it.appName.lowercase() }
    }
}
