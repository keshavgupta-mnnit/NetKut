package com.kglabs28.netkut.util

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.ui.graphics.vector.ImageVector
import com.kglabs28.netkut.ui.theme.Strings

data class CategoryItem(
    val id: Int?,
    val label: String,
    val icon: ImageVector
)

data class SyncIntervalItem(
    val minutes: Long,
    val label: String
)

object AppUtils {
    val categories: List<CategoryItem> = listOf(
        CategoryItem(null, Strings.AllCategories, Icons.Default.Apps),
        CategoryItem(0, Strings.CategoryGames, Icons.Default.SportsEsports),
        CategoryItem(4, Strings.CategorySocial, Icons.Default.People),
        CategoryItem(2, Strings.CategoryVideo, Icons.Default.Movie),
        CategoryItem(1, Strings.CategoryAudio, Icons.Default.MusicNote),
        CategoryItem(7, Strings.CategoryProduct, Icons.Default.ShoppingBag)
    )

    val syncIntervals: List<SyncIntervalItem> = listOf(
        SyncIntervalItem(0L, Strings.SyncDisable),
        SyncIntervalItem(60L, Strings.SyncEvery1Hour),
        SyncIntervalItem(120L, Strings.SyncEvery2Hours),
        SyncIntervalItem(180L, Strings.SyncEvery3Hours),
        SyncIntervalItem(240L, Strings.SyncEvery4Hours),
        SyncIntervalItem(360L, Strings.SyncEvery6Hours),
        SyncIntervalItem(480L, Strings.SyncEvery8Hours),
        SyncIntervalItem(720L, Strings.SyncEvery12Hours)
    )

    fun getCategoryItem(id: Int?): CategoryItem {
        return categories.find { it.id == id } ?: categories.first()
    }

    fun getSyncIntervalItem(minutes: Long): SyncIntervalItem {
        return syncIntervals.find { it.minutes == minutes } ?: syncIntervals[2] // Default: 2 hours (120m)
    }

    fun getOnboardingCompleted(context: Context): Boolean {
        return PreferenceUtils.getBoolean(context, AppConstants.KEY_ONBOARDING_COMPLETED, false)
    }

    fun setOnboardingCompleted(context: Context, completed: Boolean) {
        PreferenceUtils.setBoolean(context, AppConstants.KEY_ONBOARDING_COMPLETED, completed)
    }

    fun getSyncIntervalMinutes(context: Context): Long {
        return PreferenceUtils.getLong(context, AppConstants.KEY_SYNC_INTERVAL_MINUTES, AppConstants.DEFAULT_SYNC_INTERVAL_MINUTES)
    }

    fun setSyncIntervalMinutes(context: Context, minutes: Long) {
        PreferenceUtils.setLong(context, AppConstants.KEY_SYNC_INTERVAL_MINUTES, minutes)
        if (minutes <= 0) {
            setVpnActive(context, false)
        } else {
            WorkManagerUtils.updatePeriodicSync(context, minutes)
        }
    }

    fun getVpnActive(context: Context): Boolean {
        return PreferenceUtils.getBoolean(context, AppConstants.KEY_VPN_ACTIVE, false)
    }

    fun setVpnActive(context: Context, active: Boolean) {
        PreferenceUtils.setBoolean(context, AppConstants.KEY_VPN_ACTIVE, active)
        if (active) {
            val minutes = getSyncIntervalMinutes(context)
            if (minutes > 0) {
                WorkManagerUtils.updatePeriodicSync(context, minutes)
            }
        } else {
            WorkManagerUtils.cancelPeriodicSync(context)
            VpnUtils.stopVpnService(context)
        }
    }
}
