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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WifiOff
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.ShoppingBag
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
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                placeholder = {
                    Text(
                        if (uiState.selectedTab == AppTab.SELECTED_APPS) "Search selected apps..." else "Search apps"
                    )
                },
                singleLine = true,
                textStyle = TextStyle(fontSize = 14.sp, color = Color.White),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF162339),
                    unfocusedContainerColor = Color(0xFF162339),
                    disabledContainerColor = Color(0xFF162339),
                    focusedBorderColor = Color(0xFF2575FC),
                    unfocusedBorderColor = Color(0xFF1C3D6A),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedPlaceholderColor = Color(0xFF8B9CB5),
                    unfocusedPlaceholderColor = Color(0xFF8B9CB5),
                    focusedLeadingIconColor = Color(0xFF8B9CB5),
                    unfocusedLeadingIconColor = Color(0xFF8B9CB5),
                    focusedTrailingIconColor = Color(0xFF8B9CB5),
                    unfocusedTrailingIconColor = Color(0xFF8B9CB5)
                ),
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
                        modifier = Modifier.weight(1.3f),
                        selectedCategory = uiState.selectedCategory,
                        onCategorySelected = { viewModel.setCategory(it) }
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            "Show system apps",
                            style = TextStyle(fontSize = 12.sp, color = Color(0xFF8B9CB5))
                        )
                        Spacer(Modifier.width(6.dp))
                        Switch(
                            checked = uiState.showSystemApps,
                            onCheckedChange = { viewModel.toggleSystemApps(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF3B95FF),
                                uncheckedThumbColor = Color(0xFF8B9CB5),
                                uncheckedTrackColor = Color(0xFF162339),
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
                            .padding(horizontal = 16.dp, vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val count = uiState.apps.size
                        Text(
                            text = "$count ${if (count == 1) "app" else "apps"} selected",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        )
                        TextButton(
                            onClick = { viewModel.clearAllBlockedApps() },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("Clear All", color = Color(0xFF3B95FF), fontSize = 14.sp)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    modifier: Modifier = Modifier,
    selectedCategory: Int?,
    onCategorySelected: (Int?) -> Unit
) {
    val categories = listOf(
        Triple(null, "All Categories", Icons.Default.Apps),
        Triple(0, "Games", Icons.Default.SportsEsports),
        Triple(4, "Social", Icons.Default.People),
        Triple(2, "Video", Icons.Default.Movie),
        Triple(1, "Audio", Icons.Default.MusicNote),
        Triple(7, "Product", Icons.Default.ShoppingBag)
    )

    var expanded by remember { mutableStateOf(false) }
    val selectedItem = categories.find { it.first == selectedCategory } ?: categories.first()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedItem.second,
            onValueChange = {},
            readOnly = true,
            textStyle = TextStyle(fontSize = 13.sp, color = Color.White),
            leadingIcon = {
                Icon(
                    imageVector = selectedItem.third,
                    contentDescription = null,
                    tint = Color(0xFF8B9CB5)
                )
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(),
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF162339),
                unfocusedContainerColor = Color(0xFF162339),
                disabledContainerColor = Color(0xFF162339),
                focusedBorderColor = Color(0xFF2575FC),
                unfocusedBorderColor = Color(0xFF1C3D6A),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedTrailingIconColor = Color(0xFF8B9CB5),
                unfocusedTrailingIconColor = Color(0xFF8B9CB5)
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color(0xFF162339)
        ) {
            categories.forEach { (categoryInt, label, icon) ->
                val isSelected = categoryInt == selectedCategory
                val itemTextColor = if (isSelected) Color(0xFF3B95FF) else Color.White
                val itemIconColor = if (isSelected) Color(0xFF3B95FF) else Color(0xFF8B9CB5)

                DropdownMenuItem(
                    text = {
                        Text(
                            text = label,
                            color = itemTextColor,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = itemIconColor
                        )
                    },
                    trailingIcon = {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = Color(0xFF3B95FF)
                            )
                        }
                    },
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
        columns = GridCells.Adaptive(minSize = 350.dp),
        state = gridState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
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
                Text(appState.appInfo.appName, style = MaterialTheme.typography.titleMedium, color = Color.White)
                Text(
                    appState.appInfo.packageName,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF8B9CB5),
                    maxLines = 1
                )
            }
            Switch(
                checked = appState.isBlocked,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF3B95FF),
                    uncheckedThumbColor = Color(0xFF8B9CB5),
                    uncheckedTrackColor = Color(0xFF0D182A),
                    uncheckedBorderColor = Color.Transparent
                )
            )
        }
    }
}

private fun startVpnService(context: Context) {
    val intent = Intent(context, NetCutVpnService::class.java)
    ContextCompat.startForegroundService(context, intent)
}
