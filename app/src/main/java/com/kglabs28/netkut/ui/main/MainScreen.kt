package com.kglabs28.netkut.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.VpnService
import android.os.PowerManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kglabs28.netkut.ui.components.AppListAdaptive
import com.kglabs28.netkut.ui.components.BatteryOptimizationBanner
import com.kglabs28.netkut.ui.components.CategoryDropdown
import com.kglabs28.netkut.ui.components.SearchBar
import com.kglabs28.netkut.ui.components.SyncDialog
import com.kglabs28.netkut.ui.theme.AccentBlue
import com.kglabs28.netkut.ui.theme.Dimens
import com.kglabs28.netkut.ui.theme.DividerWhite
import com.kglabs28.netkut.ui.theme.GradientEnd
import com.kglabs28.netkut.ui.theme.GradientStart
import com.kglabs28.netkut.ui.theme.InputBackground
import com.kglabs28.netkut.ui.theme.Strings
import com.kglabs28.netkut.ui.theme.TextMutedBlue
import com.kglabs28.netkut.vpn.NetCutVpnService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    
    val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    val isIgnoringBatteryOptimizations = pm.isIgnoringBatteryOptimizations(context.packageName)
    var showBatteryBanner by remember { mutableStateOf(!isIgnoringBatteryOptimizations) }
    
    val vpnLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            startVpnService(context)
        }
    }

    val onToggleApp = { packageName: String, blocked: Boolean ->
        if (blocked) {
            val vpnIntent = VpnService.prepare(context)
            if (vpnIntent != null) {
                vpnLauncher.launch(vpnIntent)
            } else {
                startVpnService(context)
            }
        }
        viewModel.toggleAppBlocked(packageName, blocked)
    }

    if (uiState.showSyncDialog) {
        SyncDialog()
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
                        IconButton(onClick = {
                            coroutineScope.launch {
                                viewModel.setShowSyncDialog(true)
                                delay(2000)
                                viewModel.setShowSyncDialog(false)
                            }
                        }) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = Strings.Refresh)
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
                } else if (uiState.selectedTab == AppTab.SELECTED_APPS) {
                    if (uiState.apps.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Dimens.PaddingLarge, vertical = Dimens.PaddingTiny),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = Strings.appsSelectedCount(uiState.apps.size),
                                style = TextStyle(
                                    fontSize = Dimens.FontSizeRegular,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            )
                            TextButton(
                                onClick = { viewModel.clearAllBlockedApps() },
                                contentPadding = PaddingValues(Dimens.PaddingZero)
                            ) {
                                Text(Strings.ClearAll, color = AccentBlue, fontSize = Dimens.FontSizeRegular)
                            }
                        }
                    }
                }

                if (uiState.isLoading) {
                    Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    if (uiState.selectedTab == AppTab.SELECTED_APPS && uiState.apps.isEmpty()) {
                        Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    modifier = Modifier.size(Dimens.IconSizeLarge),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(Modifier.height(Dimens.SpacingLarge))
                                Text(
                                    Strings.NoAppsSelected,
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(Modifier.height(Dimens.SpacingLarge))
                                Button(onClick = { viewModel.setTab(AppTab.ALL_APPS) }) {
                                    Text(Strings.GoToAllApps)
                                }
                            }
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
        }
    }
}

private fun startVpnService(context: Context) {
    val intent = Intent(context, NetCutVpnService::class.java)
    ContextCompat.startForegroundService(context, intent)
}
