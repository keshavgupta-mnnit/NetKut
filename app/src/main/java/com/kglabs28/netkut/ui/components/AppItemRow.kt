package com.kglabs28.netkut.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import com.kglabs28.netkut.ui.main.AppItemUiState
import com.kglabs28.netkut.ui.theme.AccentBlue
import com.kglabs28.netkut.ui.theme.CardBackground
import com.kglabs28.netkut.ui.theme.Dimens
import com.kglabs28.netkut.ui.theme.TextMutedBlue
import com.kglabs28.netkut.ui.theme.UncheckedTrackColor

@Composable
fun AppItemRow(
    appState: AppItemUiState,
    onToggle: (Boolean) -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.RadiusMedium),
        colors = CardDefaults.elevatedCardColors(
            containerColor = CardBackground
        )
    ) {
        Row(
            modifier = Modifier.padding(Dimens.PaddingLarge),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icon = appState.appInfo.icon
            if (icon != null) {
                Image(
                    bitmap = icon.toBitmap().asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(Dimens.IconSizeMedium)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    modifier = Modifier.size(Dimens.IconSizeMedium)
                )
            }
            Spacer(modifier = Modifier.width(Dimens.SpacingLarge))
            Column(modifier = Modifier.weight(1f)) {
                Text(appState.appInfo.appName, style = MaterialTheme.typography.titleMedium, color = Color.White)
                Text(
                    appState.appInfo.packageName,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMutedBlue,
                    maxLines = 1
                )
            }
            Switch(
                checked = appState.isBlocked,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = AccentBlue,
                    uncheckedThumbColor = TextMutedBlue,
                    uncheckedTrackColor = UncheckedTrackColor,
                    uncheckedBorderColor = Color.Transparent
                )
            )
        }
    }
}
