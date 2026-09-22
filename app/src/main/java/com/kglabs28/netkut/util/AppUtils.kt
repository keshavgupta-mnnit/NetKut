package com.kglabs28.netkut.util

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
}
