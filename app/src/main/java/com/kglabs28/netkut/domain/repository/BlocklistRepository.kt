package com.kglabs28.netkut.domain.repository

import kotlinx.coroutines.flow.Flow

interface BlocklistRepository {
    val blockedPackages: Flow<Set<String>>
    
    suspend fun setBlockedPackages(packages: Set<String>)
    suspend fun addBlockedPackage(packageName: String)
    suspend fun removeBlockedPackage(packageName: String)
}
