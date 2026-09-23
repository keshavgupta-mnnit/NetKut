package com.kglabs28.netkut.util

import android.content.Context
import android.content.SharedPreferences
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

object AppUtils {
    private const val PREFS_NAME = "netkut_prefs"
    private const val KEY_ONBOARDING_COMPLETED = "has_completed_onboarding"

    val categories: List<CategoryItem> = listOf(
        CategoryItem(null, Strings.AllCategories, Icons.Default.Apps),
        CategoryItem(0, Strings.CategoryGames, Icons.Default.SportsEsports),
        CategoryItem(4, Strings.CategorySocial, Icons.Default.People),
        CategoryItem(2, Strings.CategoryVideo, Icons.Default.Movie),
        CategoryItem(1, Strings.CategoryAudio, Icons.Default.MusicNote),
        CategoryItem(7, Strings.CategoryProduct, Icons.Default.ShoppingBag)
    )

    fun getCategoryItem(id: Int?): CategoryItem {
        return categories.find { it.id == id } ?: categories.first()
    }

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getOnboardingCompleted(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }

    fun setOnboardingCompleted(context: Context, completed: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_ONBOARDING_COMPLETED, completed).apply()
    }
}
