package com.kglabs28.netkut.domain.repository

import com.kglabs28.netkut.domain.model.AppInfo

interface AppRepository {
    suspend fun getInstalledApps(includeSystemApps: Boolean = false): List<AppInfo>
}
