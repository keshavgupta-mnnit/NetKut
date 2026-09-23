package com.kglabs28.netkut.ui.screens.main

import android.app.Activity
import android.os.PowerManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kglabs28.netkut.ui.components.BatteryOptimizationBanner
import com.kglabs28.netkut.ui.components.SearchBar
import com.kglabs28.netkut.ui.components.SyncDialog
import com.kglabs28.netkut.ui.theme.AccentBlue
import com.kglabs28.netkut.ui.theme.Dimens
import com.kglabs28.netkut.ui.theme.DividerWhite
import com.kglabs28.netkut.ui.theme.GradientEnd
import com.kglabs28.netkut.ui.theme.GradientStart
import com.kglabs28.netkut.ui.theme.Strings
import com.kglabs28.netkut.ui.theme.TextMutedBlue
import com.kglabs28.netkut.util.VpnUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onSettingsClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    
    val isIgnoringBatteryOptimizations = VpnUtils.isIgnoringBatteryOptimizations(context)
    var showBatteryBanner by remember { mutableStateOf(!isIgnoringBatteryOptimizations) }
    
    val vpnLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            VpnUtils.startVpnService(context)
        }
    }

    val onToggleApp = { packageName: String, blocked: Boolean ->
        if (blocked) {
            val vpnIntent = VpnUtils.prepareVpnIntent(context)
            if (vpnIntent != null) {
                vpnLauncher.launch(vpnIntent)
            } else {
                VpnUtils.startVpnService(context)
            }
        }
        viewModel.toggleAppBlocked(packageName, blocked)
    }

    if (uiState.showSyncDialog) {
        SyncDialog(onDismiss = { viewModel.setShowSyncDialog(false) })
    }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(GradientStart, GradientEnd)
    )

    Box(modifier = Modifier.fillMaxSize().background(backgroundGradient)) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text(Strings.AppName) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White
                    ),
                    actions = {
                        IconButton(onClick = onSettingsClick) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = Strings.SettingsTitle,
                                tint = Color.White
                            )
                        }
                    }
                )
            },
            bottomBar = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = Dimens.DividerThickness,
                        color = DividerWhite
                    )
                    NavigationBar(
                        containerColor = Color.Transparent
                    ) {
                        NavigationBarItem(
                            selected = uiState.selectedTab == AppTab.SELECTED_APPS,
                            onClick = { viewModel.setTab(AppTab.SELECTED_APPS) },
                            icon = { Icon(Icons.Default.Security, contentDescription = Strings.SelectedApps) },
                            label = { Text(Strings.SelectedApps) },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.Transparent,
                                selectedIconColor = AccentBlue,
                                selectedTextColor = AccentBlue,
                                unselectedIconColor = TextMutedBlue,
                                unselectedTextColor = TextMutedBlue
                            )
                        )
                        NavigationBarItem(
                            selected = uiState.selectedTab == AppTab.ALL_APPS,
                            onClick = { viewModel.setTab(AppTab.ALL_APPS) },
                            icon = { Icon(Icons.Default.Apps, contentDescription = Strings.AllApps) },
                            label = { Text(Strings.AllApps) },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.Transparent,
                                selectedIconColor = AccentBlue,
                                selectedTextColor = AccentBlue,
                                unselectedIconColor = TextMutedBlue,
                                unselectedTextColor = TextMutedBlue
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                if (showBatteryBanner) {
                    BatteryOptimizationBanner(
                        context = context,
                        onDismiss = { showBatteryBanner = false }
                    )
                }

                SearchBar(
                    query = uiState.searchQuery,
                    selectedTab = uiState.selectedTab,
                    onQueryChange = { viewModel.updateSearchQuery(it) }
                )

                if (uiState.selectedTab == AppTab.ALL_APPS) {
                    AllAppsTabContent(
                        uiState = uiState,
                        viewModel = viewModel,
                        onToggleApp = onToggleApp,
                        modifier = Modifier.weight(1f)
                    )
                } else if (uiState.selectedTab == AppTab.SELECTED_APPS) {
                    SelectedAppsTabContent(
                        uiState = uiState,
                        viewModel = viewModel,
                        context = context,
                        vpnLauncher = vpnLauncher,
                        onToggleApp = onToggleApp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
