package com.kglabs28.netkut.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.kglabs28.netkut.ui.theme.AccentBlue
import com.kglabs28.netkut.ui.theme.Dimens
import com.kglabs28.netkut.ui.theme.FocusedBorderColor
import com.kglabs28.netkut.ui.theme.InputBackground
import com.kglabs28.netkut.ui.theme.Strings
import com.kglabs28.netkut.ui.theme.TextMutedBlue
import com.kglabs28.netkut.ui.theme.UnfocusedBorderColor
import com.kglabs28.netkut.util.AppUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    modifier: Modifier = Modifier,
    selectedCategory: Int?,
    onCategorySelected: (Int?) -> Unit
) {
    val selectedItem = AppUtils.getCategoryItem(selectedCategory)
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .height(Dimens.DropdownHeight),
            shape = RoundedCornerShape(Dimens.RadiusLarge),
            color = InputBackground,
            border = BorderStroke(Dimens.DividerThickness, if (expanded) FocusedBorderColor else UnfocusedBorderColor)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Dimens.PaddingMedium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = selectedItem.icon,
                        contentDescription = null,
                        tint = TextMutedBlue,
                        modifier = Modifier.size(Dimens.RadiusMedium + Dimens.PaddingTiny)
                    )
                    Spacer(modifier = Modifier.width(Dimens.SpacingMedium))
                    Text(
                        text = selectedItem.label,
                        style = TextStyle(fontSize = Dimens.FontSizeMedium, color = Color.White),
                        maxLines = 1
                    )
                }
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
        }
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            shape = RoundedCornerShape(Dimens.RadiusMedium),
            containerColor = InputBackground
        ) {
            AppUtils.categories.forEach { item ->
                val isSelected = item.id == selectedCategory
                val itemTextColor = if (isSelected) AccentBlue else Color.White
                val itemIconColor = if (isSelected) AccentBlue else TextMutedBlue

                DropdownMenuItem(
                    text = {
                        Text(
                            text = item.label,
                            color = itemTextColor,
                            fontSize = Dimens.FontSizeMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            tint = itemIconColor
                        )
                    },
                    trailingIcon = {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = Strings.SelectedIndicator,
                                tint = AccentBlue
                            )
                        }
                    },
                    onClick = {
                        onCategorySelected(item.id)
                        expanded = false
                    }
                )
            }
        }
    }
}
