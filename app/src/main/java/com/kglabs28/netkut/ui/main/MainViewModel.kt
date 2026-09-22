package com.kglabs28.netkut.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kglabs28.netkut.domain.model.AppInfo
import com.kglabs28.netkut.domain.repository.AppRepository
import com.kglabs28.netkut.domain.repository.BlocklistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab {
    SELECTED_APPS,
    ALL_APPS
}

data class AppItemUiState(
    val appInfo: AppInfo,
    val isBlocked: Boolean
)

data class MainUiState(
    val apps: List<AppItemUiState> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = true,
    val selectedTab: AppTab = AppTab.SELECTED_APPS,
    val showSystemApps: Boolean = false,
    val selectedCategory: Int? = null,
    val showSyncDialog: Boolean = false
)

class MainViewModel(
    private val appRepository: AppRepository,
    private val blocklistRepository: BlocklistRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _installedApps = MutableStateFlow<List<AppInfo>>(emptyList())
    private val _isLoading = MutableStateFlow(true)
    private val _selectedTab = MutableStateFlow(AppTab.SELECTED_APPS)
    private val _showSystemApps = MutableStateFlow(false)
    private val _selectedCategory = MutableStateFlow<Int?>(null)
    private val _showSyncDialog = MutableStateFlow(false)

    val uiState: StateFlow<MainUiState> = combine(
        _installedApps,
        _searchQuery,
        blocklistRepository.blockedPackages,
        _isLoading,
        combine(
            _selectedTab,
            _showSystemApps,
            _selectedCategory,
            _showSyncDialog
        ) { tab, showSystemApps, selectedCategory, showSyncDialog ->
            FilterState(tab, showSystemApps, selectedCategory, showSyncDialog)
        }
    ) { apps, searchQuery, blockedPackages, isLoading, filterState ->
        val filteredApps = apps.filter { app ->
            val matchSystem = if (filterState.showSystemApps) true else !app.isSystemApp
            val matchSearch = searchQuery.isBlank() || app.appName.contains(searchQuery, ignoreCase = true)
            val matchCategory = filterState.selectedCategory == null || app.category == filterState.selectedCategory
            val matchTab = if (filterState.tab == AppTab.SELECTED_APPS) blockedPackages.contains(app.packageName) else true
            
            matchSystem && matchSearch && matchCategory && matchTab
        }.map { app ->
            AppItemUiState(
                appInfo = app,
                isBlocked = blockedPackages.contains(app.packageName)
            )
        }
        
        MainUiState(
            apps = filteredApps,
            searchQuery = searchQuery,
            isLoading = isLoading,
            selectedTab = filterState.tab,
            showSystemApps = filterState.showSystemApps,
            selectedCategory = filterState.selectedCategory,
            showSyncDialog = filterState.showSyncDialog
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainUiState(isLoading = true)
    )

    init {
        loadApps()
    }

    private fun loadApps() {
        viewModelScope.launch {
            _isLoading.value = true
            // Load all apps, filtering will be done in the combine flow
            _installedApps.value = appRepository.getInstalledApps(includeSystemApps = true)
            _isLoading.value = false
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setTab(tab: AppTab) {
        _selectedTab.value = tab
    }

    fun toggleSystemApps(show: Boolean) {
        _showSystemApps.value = show
    }

    fun setCategory(category: Int?) {
        _selectedCategory.value = category
    }

    fun setShowSyncDialog(show: Boolean) {
        _showSyncDialog.value = show
    }

    fun toggleAppBlocked(packageName: String, blocked: Boolean) {
        viewModelScope.launch {
            if (blocked) {
                blocklistRepository.addBlockedPackage(packageName)
            } else {
                blocklistRepository.removeBlockedPackage(packageName)
            }
        }
    }

    fun clearAllBlockedApps() {
        viewModelScope.launch {
            blocklistRepository.setBlockedPackages(emptySet())
        }
    }

    companion object {
        fun provideFactory(
            appRepository: AppRepository,
            blocklistRepository: BlocklistRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(appRepository, blocklistRepository) as T
            }
        }
    }
}

private data class FilterState(
    val tab: AppTab,
    val showSystemApps: Boolean,
    val selectedCategory: Int?,
    val showSyncDialog: Boolean
)
