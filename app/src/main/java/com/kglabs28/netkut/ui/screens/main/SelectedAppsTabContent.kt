package com.kglabs28.netkut.ui.screens.main

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kglabs28.netkut.ui.components.AppListAdaptive
import com.kglabs28.netkut.ui.components.EmptySelectedApps
import com.kglabs28.netkut.ui.components.SelectedAppsActionRow
import com.kglabs28.netkut.util.AppUtils
import com.kglabs28.netkut.util.VpnUtils

@Composable
fun SelectedAppsTabContent(
    uiState: MainUiState,
    viewModel: MainViewModel,
    context: Context,
    vpnLauncher: ActivityResultLauncher<Intent>,
    onToggleApp: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var isVpnActive by remember { mutableStateOf(AppUtils.getVpnActive(context)) }
    val syncIntervalMinutes = AppUtils.getSyncIntervalMinutes(context)

    Column(modifier = modifier.fillMaxSize()) {
        if (uiState.apps.isNotEmpty()) {
            SelectedAppsActionRow(
                isVpnActive = isVpnActive,
                syncIntervalMinutes = syncIntervalMinutes,
                onStartClick = {
                    AppUtils.setVpnActive(context, true)
                    isVpnActive = true
                    val vpnIntent = VpnUtils.prepareVpnIntent(context)
                    if (vpnIntent != null) {
                        vpnLauncher.launch(vpnIntent)
                    } else {
                        VpnUtils.startVpnService(context)
                    }
                },
                onPauseClick = {
                    AppUtils.setVpnActive(context, false)
                    isVpnActive = false
                },
                onSyncClick = {
                    viewModel.setShowSyncDialog(true)
                },
                onClearAllClick = {
                    viewModel.clearAllBlockedApps()
                }
            )
        }

        if (uiState.isLoading) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            if (uiState.apps.isEmpty()) {
                EmptySelectedApps(
                    onGoToAllApps = { viewModel.setTab(AppTab.ALL_APPS) },
                    modifier = Modifier.weight(1f)
                )
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
}
