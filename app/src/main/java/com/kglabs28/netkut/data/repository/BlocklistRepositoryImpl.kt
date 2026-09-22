package com.kglabs28.netkut.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kglabs28.netkut.domain.repository.BlocklistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "blocklist_prefs")

class BlocklistRepositoryImpl(private val context: Context) : BlocklistRepository {
    
    private val BLOCKED_PACKAGES_KEY = stringSetPreferencesKey("blocked_packages")

    override val blockedPackages: Flow<Set<String>> = context.dataStore.data
        .map { preferences ->
            preferences[BLOCKED_PACKAGES_KEY] ?: emptySet()
        }

    override suspend fun setBlockedPackages(packages: Set<String>) {
        context.dataStore.edit { preferences ->
            preferences[BLOCKED_PACKAGES_KEY] = packages
        }
    }

    override suspend fun addBlockedPackage(packageName: String) {
        context.dataStore.edit { preferences ->
            val current = preferences[BLOCKED_PACKAGES_KEY] ?: emptySet()
            preferences[BLOCKED_PACKAGES_KEY] = current + packageName
        }
    }

    override suspend fun removeBlockedPackage(packageName: String) {
        context.dataStore.edit { preferences ->
            val current = preferences[BLOCKED_PACKAGES_KEY] ?: emptySet()
            preferences[BLOCKED_PACKAGES_KEY] = current - packageName
        }
    }
}
