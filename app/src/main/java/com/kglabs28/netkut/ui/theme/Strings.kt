package com.kglabs28.netkut.ui.theme

object Strings {
    const val AppName = "NetKut"
    const val Refresh = "Refresh"
    const val SyncingTitle = "Syncing..."
    const val SyncingMessage = "Refreshing app list and settings"
    const val SelectedApps = "Selected Apps"
    const val AllApps = "All Apps"
    const val BatteryOptimizationWarning = "Battery optimization may stop the VPN from running in the background."
    const val FixNow = "Fix Now"
    const val SearchApps = "Search apps"
    const val SearchSelectedApps = "Search selected apps..."
    const val ClearSearch = "Clear search"
    const val ShowSystemApps = "Show system apps"
    const val ClearAll = "Clear All"
    const val NoAppsSelected = "No apps selected yet"
    const val GoToAllApps = "Go to All Apps"
    const val AllCategories = "All Categories"
    const val CategoryGames = "Games"
    const val CategorySocial = "Social"
    const val CategoryVideo = "Video"
    const val CategoryAudio = "Audio"
    const val CategoryProduct = "Product"
    const val SelectedIndicator = "Selected"
    const val Cancel = "Cancel"
    
    fun appsSelectedCount(count: Int): String {
        return "$count ${if (count == 1) "app" else "apps"} selected"
    }
}
