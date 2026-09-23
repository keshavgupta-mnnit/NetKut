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
    const val OnboardingWelcomeTitle = "Block Internet Access\nfor Selected Apps"
    const val OnboardingWelcomeDescPart1 = "NetKut creates a "
    const val OnboardingWelcomeDescPart2 = "VPN service to block"
    const val OnboardingWelcomeDescPart3 = " the internet for the apps you choose.\nDon't worry if you see a VPN icon in the status bar — it's normal!"
    
    const val HowItWorksTitle = "How It Works"
    const val VpnServiceTitle = "VPN Service"
    const val VpnServiceDesc = "Creates a local VPN connection to block the internet for selected apps. This keeps your device secure and private."
    const val StartPauseTitle = "Start / Pause"
    const val StartPauseDesc = "Start the service to block internet for selected apps. Pause it anytime to get back online."
    const val SyncIntervalTitle = "Sync Every 2 Hours"
    const val SyncIntervalDesc = "The app will automatically sync and reapply the rules every 2 hours. You can change this interval in settings."
    
    const val VpnNoticeTitle = "About VPN Icon in Status Bar"
    const val VpnNoticeDesc = "You might see a VPN icon in your status bar. This means NetKut is working as a VPN service to block the internet for selected apps."
    const val ExpectedSafeTitle = "This is expected and safe."
    const val ExpectedSafeDesc = "It doesn't mean your device is being tracked."
    
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
