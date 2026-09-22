package com.kglabs28.netkut.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.kglabs28.netkut.ui.theme.Dimens
import com.kglabs28.netkut.ui.theme.Strings

@Composable
fun SyncDialog() {
    AlertDialog(
        onDismissRequest = { },
        confirmButton = { },
        title = { Text(Strings.SyncingTitle) },
        text = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingLarge)
            ) {
                CircularProgressIndicator()
                Text(Strings.SyncingMessage)
            }
        }
    )
}
