package com.kglabs28.netkut.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.kglabs28.netkut.ui.components.AppListAdaptive
import com.kglabs28.netkut.ui.components.CategoryDropdown
import com.kglabs28.netkut.ui.theme.AccentBlue
import com.kglabs28.netkut.ui.theme.Dimens
import com.kglabs28.netkut.ui.theme.InputBackground
import com.kglabs28.netkut.ui.theme.Strings
import com.kglabs28.netkut.ui.theme.TextMutedBlue

@Composable
fun AllAppsTabContent(
    uiState: MainUiState,
    viewModel: MainViewModel,
    onToggleApp: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.PaddingLarge),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CategoryDropdown(
                modifier = Modifier.weight(1.3f),
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = { viewModel.setCategory(it) }
            )
            
            Spacer(modifier = Modifier.width(Dimens.SpacingMedium))
            
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    Strings.ShowSystemApps,
                    style = TextStyle(fontSize = Dimens.FontSizeSmall, color = TextMutedBlue)
                )
                Spacer(Modifier.width(Dimens.SpacingSmall))
                Switch(
                    checked = uiState.showSystemApps,
                    onCheckedChange = { viewModel.toggleSystemApps(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = AccentBlue,
                        uncheckedThumbColor = TextMutedBlue,
                        uncheckedTrackColor = InputBackground,
                        uncheckedBorderColor = Color.Transparent
                    )
                )
            }
        }

        if (uiState.isLoading) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            AppListAdaptive(
                modifier = Modifier.weight(1f),
                apps = uiState.apps,
                searchQuery = uiState.searchQuery,
                selectedCategory = uiState.selectedCategory,
                selectedTab = uiState.selectedTab,
                onToggle = onToggleApp
            )
        }
    }
}
