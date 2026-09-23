package com.kglabs28.netkut.util

object AppConstants {
    const val PREFS_NAME = "netkut_prefs"
    const val KEY_ONBOARDING_COMPLETED = "has_completed_onboarding"
    const val KEY_SYNC_INTERVAL_MINUTES = "sync_interval_minutes"
    const val KEY_VPN_ACTIVE = "key_vpn_active"
    
    const val WORK_MANAGER_VPN_SYNC_NAME = "VpnSyncWorker"
    const val DEFAULT_SYNC_INTERVAL_MINUTES = 120L
    const val MIN_WORK_MANAGER_INTERVAL_MINUTES = 15L
}
