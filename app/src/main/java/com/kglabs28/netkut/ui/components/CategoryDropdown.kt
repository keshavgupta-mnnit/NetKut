package com.kglabs28.netkut.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kglabs28.netkut.util.AppUtils

@Composable
fun CategoryDropdown(
    modifier: Modifier = Modifier,
    selectedCategory: Int?,
    onCategorySelected: (Int?) -> Unit
) {
    val items = AppUtils.categories.map {
        DropdownItem(it.id, it.label, it.icon)
    }

    AppDropdown(
        items = items,
        selectedId = selectedCategory,
        onItemSelected = onCategorySelected,
        modifier = modifier
    )
}
