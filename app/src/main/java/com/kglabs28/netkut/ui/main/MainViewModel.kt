package com.kglabs28.netkut.ui.main

import android.graphics.drawable.Drawable
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

data class AppItemUiState(
    val appInfo: AppInfo,
    val isBlocked: Boolean
)

data class MainUiState(
    val apps: List<AppItemUiState> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = true
)

class MainViewModel(
    private val appRepository: AppRepository,
    private val blocklistRepository: BlocklistRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _installedApps = MutableStateFlow<List<AppInfo>>(emptyList())
    private val _isLoading = MutableStateFlow(true)

    val uiState: StateFlow<MainUiState> = combine(
        _installedApps,
        _searchQuery,
        blocklistRepository.blockedPackages,
        _isLoading
    ) { apps, searchQuery, blockedPackages, isLoading ->
        val filteredApps = apps.filter { app ->
            !app.isSystemApp && (searchQuery.isBlank() || app.appName.contains(searchQuery, ignoreCase = true))
        }.map { app ->
            AppItemUiState(
                appInfo = app,
                isBlocked = blockedPackages.contains(app.packageName)
            )
        }
        MainUiState(
            apps = filteredApps,
            searchQuery = searchQuery,
            isLoading = isLoading
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

    fun toggleAppBlocked(packageName: String, blocked: Boolean) {
        viewModelScope.launch {
            if (blocked) {
                blocklistRepository.addBlockedPackage(packageName)
            } else {
                blocklistRepository.removeBlockedPackage(packageName)
            }
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
