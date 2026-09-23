package com.kglabs28.netkut.util

import com.kglabs28.netkut.domain.model.AppInfo
import com.kglabs28.netkut.ui.screens.main.AppItemUiState
import com.kglabs28.netkut.ui.screens.main.AppTab

object AppFilterUtils {
    fun filterApps(
        apps: List<AppInfo>,
        searchQuery: String,
        blockedPackages: Set<String>,
        showSystemApps: Boolean,
        selectedCategory: Int?,
        tab: AppTab
    ): List<AppItemUiState> {
        return apps.filter { app ->
            val matchSystem = if (showSystemApps) true else !app.isSystemApp
            val matchSearch = searchQuery.isBlank() || app.appName.contains(searchQuery, ignoreCase = true)
            val matchCategory = selectedCategory == null || app.category == selectedCategory
            val matchTab = if (tab == AppTab.SELECTED_APPS) blockedPackages.contains(app.packageName) else true
            
            matchSystem && matchSearch && matchCategory && matchTab
        }.map { app ->
            AppItemUiState(
                appInfo = app,
                isBlocked = blockedPackages.contains(app.packageName)
            )
        }
    }
}
