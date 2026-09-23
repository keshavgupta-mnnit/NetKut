package com.kglabs28.netkut.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kglabs28.netkut.ui.theme.AccentBlue
import com.kglabs28.netkut.ui.theme.ButtonGreen
import com.kglabs28.netkut.ui.theme.ButtonOrange
import com.kglabs28.netkut.ui.theme.ButtonRed
import com.kglabs28.netkut.ui.theme.Dimens
import com.kglabs28.netkut.ui.theme.Strings

@Composable
fun SelectedAppsActionRow(
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onSyncClick: () -> Unit,
    onClearAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.PaddingLarge, vertical = Dimens.PaddingSmall),
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Start Button
        Button(
            onClick = onStartClick,
            modifier = Modifier
                .weight(1f)
                .height(Dimens.DropdownHeight),
            shape = RoundedCornerShape(Dimens.RadiusMedium),
            colors = ButtonDefaults.buttonColors(
                containerColor = ButtonGreen,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = Strings.Start,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = Strings.Start,
                    style = TextStyle(fontSize = Dimens.FontSizeMedium, fontWeight = FontWeight.SemiBold)
                )
            }
        }

        // Pause Button
        Button(
            onClick = onPauseClick,
            modifier = Modifier
                .weight(1f)
                .height(Dimens.DropdownHeight),
            shape = RoundedCornerShape(Dimens.RadiusMedium),
            colors = ButtonDefaults.buttonColors(
                containerColor = ButtonOrange,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Pause,
                    contentDescription = Strings.Pause,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = Strings.Pause,
                    style = TextStyle(fontSize = Dimens.FontSizeMedium, fontWeight = FontWeight.SemiBold)
                )
            }
        }

        // Sync Button
        Button(
            onClick = onSyncClick,
            modifier = Modifier
                .weight(1f)
                .height(Dimens.DropdownHeight),
            shape = RoundedCornerShape(Dimens.RadiusMedium),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentBlue,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = Strings.Sync,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = Strings.Sync,
                    style = TextStyle(fontSize = Dimens.FontSizeMedium, fontWeight = FontWeight.SemiBold)
                )
            }
        }

        // Clear All Button
        Button(
            onClick = onClearAllClick,
            modifier = Modifier
                .weight(1f)
                .height(Dimens.DropdownHeight),
            shape = RoundedCornerShape(Dimens.RadiusMedium),
            colors = ButtonDefaults.buttonColors(
                containerColor = ButtonRed,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = Strings.ClearAll,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = Strings.ClearAll,
                    style = TextStyle(fontSize = Dimens.FontSizeSmall, fontWeight = FontWeight.SemiBold),
                    maxLines = 1
                )
            }
        }
    }
}
