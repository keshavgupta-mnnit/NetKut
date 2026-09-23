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
    const val Start = "Start"
    const val Pause = "Pause"
    const val Sync = "Sync"
    const val StatusActive = "Status: Active"
    const val StatusPaused = "Status: Paused"
    const val StatusActiveWithoutSync = "Status: Active without automatic sync"
    
    // Onboarding Strings
    const val OnboardingWelcomeTitle = "Block Internet Access\n for Selected Apps"
    
    const val HowItWorksTitle = "How It Works"
    const val VpnServiceTitle = "VPN Service"
    const val VpnServiceDesc = "Creates a local VPN connection to block the internet for selected apps. This keeps your device secure and private."
    const val StartPauseTitle = "Start / Pause"
    const val StartPauseDesc = "Control blocking anytime using Start and Pause on your dashboard."
    const val SyncIntervalTitle = "Sync Every 2 Hours"
    const val SyncIntervalDesc = "Automatically syncs and reapplies your rules every 2 hours."
    
    const val VpnNoticeTitle = "VPN Icon in Status Bar"
    const val VpnNoticeDesc = "A status bar VPN icon is normal, safe, and indicates NetKut is active."
    const val ExpectedSafeTitle = "Expected & Safe"
    const val ExpectedSafeDesc = "Operates 100% on-device. Your data is private and never tracked."
    
    const val Next = "Next"
    const val OkGotIt = "Ok Got It"

    // Settings Strings
    const val SettingsTitle = "Settings"
    const val SyncIntervalSettingTitle = "Sync Interval"
    const val SyncIntervalSettingDesc = "Set how often to sync rules"
    const val HowItWorksLearnMore = "Learn how NetKut works"
    const val AboutUsTitle = "About Us"
    const val RateUsOnPlayStore = "Rate us on Play Store"
    const val AppTagline = "Block Internet. Stay Focused."
    const val AboutUsDesc = "NetKut helps you take control of your time by blocking internet access for selected apps. Build a healthier and more productive digital life."
    const val LoveOurApp = "Love our app?"
    const val VersionText = "Version 1.0.0"

    // Sync Interval Options
    const val SyncDisable = "Disable"
    const val SyncEvery1Hour = "Every 1 hour"
    const val SyncEvery2Hours = "Every 2 hours"
    const val SyncEvery3Hours = "Every 3 hours"
    const val SyncEvery4Hours = "Every 4 hours"
    const val SyncEvery6Hours = "Every 6 hours"
    const val SyncEvery8Hours = "Every 8 hours"
    const val SyncEvery12Hours = "Every 12 hours"

    fun appsSelectedCount(count: Int): String {
        return "$count ${if (count == 1) "app" else "apps"} selected"
    }

    fun statusActiveWithSync(intervalLabel: String): String {
        return "Status: Active with automatic sync ${intervalLabel.lowercase()}"
    }
}
