package com.kglabs28.netkut.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kglabs28.netkut.util.AppUtils

@Composable
fun SyncIntervalDropdown(
    selectedMinutes: Long,
    onIntervalSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = AppUtils.syncIntervals.map { item ->
        DropdownItem(
            id = item.minutes,
            label = item.label,
            icon = if (item.minutes <= 0) Icons.Default.Block else Icons.Default.Schedule
        )
    }

    AppDropdown(
        items = items,
        selectedId = selectedMinutes,
        onItemSelected = onIntervalSelected,
        modifier = modifier
    )
}
