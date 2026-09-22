package com.kglabs28.netkut.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.VpnService
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import android.os.PowerManager
import android.provider.Settings
import android.net.Uri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kglabs28.netkut.vpn.NetCutVpnService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape

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
        AlertDialog(
            onDismissRequest = { },
            confirmButton = { },
            title = { Text("Syncing...") },
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator()
                    Text("Refreshing app list and settings")
                }
            }
        )
    }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0F1E36),
            Color(0xFF050B14)
        )
    )

    Box(modifier = Modifier.fillMaxSize().background(backgroundGradient)) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("NetKut") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    actions = {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                viewModel.setShowSyncDialog(true)
                                delay(2000)
                                viewModel.setShowSyncDialog(false)
                            }
                        }) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh")
                        }
                    }
                )
            },
            bottomBar = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = Color.White.copy(alpha = 0.1f)
                    )
                    NavigationBar(
                        containerColor = Color.Transparent
                    ) {
                        NavigationBarItem(
                            selected = uiState.selectedTab == AppTab.SELECTED_APPS,
                            onClick = { viewModel.setTab(AppTab.SELECTED_APPS) },
                            icon = { Icon(Icons.Default.Security, contentDescription = "Selected Apps") },
                            label = { Text("Selected Apps") },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.Transparent,
                                selectedIconColor = Color(0xFF3B95FF),
                                selectedTextColor = Color(0xFF3B95FF),
                                unselectedIconColor = Color(0xFF8B9CB5),
                                unselectedTextColor = Color(0xFF8B9CB5)
                            )
                        )
                        NavigationBarItem(
                            selected = uiState.selectedTab == AppTab.ALL_APPS,
                            onClick = { viewModel.setTab(AppTab.ALL_APPS) },
                            icon = { Icon(Icons.Default.Apps, contentDescription = "All Apps") },
                            label = { Text("All Apps") },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.Transparent,
                                selectedIconColor = Color(0xFF3B95FF),
                                selectedTextColor = Color(0xFF3B95FF),
                                unselectedIconColor = Color(0xFF8B9CB5),
                                unselectedTextColor = Color(0xFF8B9CB5)
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
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Battery optimization may stop the VPN from running in the background.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                                    data = Uri.parse("package:${context.packageName}")
                                }
                                context.startActivity(intent)
                                showBatteryBanner = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.onErrorContainer,
                                contentColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Text("Fix Now")
                        }
                    }
                }
            }

            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search apps") },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear search")
                        }
                    }
                }
            )

            if (uiState.selectedTab == AppTab.ALL_APPS) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CategoryDropdown(
                        modifier = Modifier.weight(1f),
                        selectedCategory = uiState.selectedCategory,
                        onCategorySelected = { viewModel.setCategory(it) }
                    )
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("System apps", style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.width(8.dp))
                        Switch(
                            checked = uiState.showSystemApps,
                            onCheckedChange = { viewModel.toggleSystemApps(it) }
                        )
                    }
                }
            } else if (uiState.selectedTab == AppTab.SELECTED_APPS) {
                if (uiState.apps.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { viewModel.clearAllBlockedApps() }) {
                            Text("Clear All")
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
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                "No apps selected yet",
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(16.dp))
                            Button(onClick = { viewModel.setTab(AppTab.ALL_APPS) }) {
                                Text("Go to All Apps")
                            }
                        }
                    }
                } else {
                    AppListAdaptive(
                        modifier = Modifier.weight(1f),
                        apps = uiState.apps,
                        searchQuery = uiState.searchQuery,
                        onToggle = onToggleApp
                    )
                }
            }
        }
    }
}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    modifier: Modifier = Modifier,
    selectedCategory: Int?,
    onCategorySelected: (Int?) -> Unit
) {
    val categories = listOf(
        null to "All",
        0 to "Games",
        4 to "Social",
        2 to "Video",
        1 to "Audio",
        7 to "Product"
    )

    var expanded by remember { mutableStateOf(false) }
    val selectedText = categories.find { it.first == selectedCategory }?.second ?: "All"

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(),
            singleLine = true,
            shape = RoundedCornerShape(24.dp)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { (categoryInt, label) ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = {
                        onCategorySelected(categoryInt)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun AppListAdaptive(
    modifier: Modifier = Modifier,
    apps: List<AppItemUiState>,
    searchQuery: String,
    onToggle: (String, Boolean) -> Unit
) {
    val gridState = rememberLazyGridState()
    
    LaunchedEffect(searchQuery) {
        if (searchQuery.isEmpty() && apps.isNotEmpty()) {
            gridState.animateScrollToItem(0)
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 350.dp),
        state = gridState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(apps, key = { it.appInfo.packageName }) { appState ->
            AppItemRow(
                appState = appState,
                onToggle = { blocked -> onToggle(appState.appInfo.packageName, blocked) }
            )
        }
    }
}

@Composable
fun AppItemRow(
    appState: AppItemUiState,
    onToggle: (Boolean) -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color(0xFF162339)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icon = appState.appInfo.icon
            if (icon != null) {
                Image(
                    bitmap = icon.toBitmap().asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(appState.appInfo.appName, style = MaterialTheme.typography.titleMedium)
                Text(
                    appState.appInfo.packageName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
            Switch(
                checked = appState.isBlocked,
                onCheckedChange = onToggle
            )
        }
    }
}

private fun startVpnService(context: Context) {
    val intent = Intent(context, NetCutVpnService::class.java)
    ContextCompat.startForegroundService(context, intent)
}
