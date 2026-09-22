package com.kglabs28.netkut.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.kglabs28.netkut.ui.main.AppItemUiState
import com.kglabs28.netkut.ui.main.AppTab
import com.kglabs28.netkut.ui.theme.Dimens

@Composable
fun AppListAdaptive(
    modifier: Modifier = Modifier,
    apps: List<AppItemUiState>,
    searchQuery: String,
    selectedCategory: Int?,
    selectedTab: AppTab,
    onToggle: (String, Boolean) -> Unit
) {
    val gridState = rememberLazyGridState()
    
    LaunchedEffect(searchQuery, selectedCategory, selectedTab) {
        if (apps.isNotEmpty()) {
            gridState.animateScrollToItem(0)
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = Dimens.GridMinSize),
        state = gridState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = Dimens.PaddingLarge, vertical = Dimens.PaddingMedium),
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingGrid),
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingGrid)
    ) {
        items(apps, key = { it.appInfo.packageName }) { appState ->
            AppItemRow(
                appState = appState,
                onToggle = { blocked -> onToggle(appState.appInfo.packageName, blocked) }
            )
        }
    }
}
