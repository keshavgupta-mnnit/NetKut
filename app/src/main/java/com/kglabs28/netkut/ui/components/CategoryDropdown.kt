package com.kglabs28.netkut.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    modifier: Modifier = Modifier,
    selectedCategory: Int?,
    onCategorySelected: (Int?) -> Unit
) {
    val categories = listOf(
        Triple(null, Strings.AllCategories, Icons.Default.Apps),
        Triple(0, Strings.CategoryGames, Icons.Default.SportsEsports),
        Triple(4, Strings.CategorySocial, Icons.Default.People),
        Triple(2, Strings.CategoryVideo, Icons.Default.Movie),
        Triple(1, Strings.CategoryAudio, Icons.Default.MusicNote),
        Triple(7, Strings.CategoryProduct, Icons.Default.ShoppingBag)
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
            textStyle = TextStyle(fontSize = Dimens.FontSizeMedium, color = Color.White),
            leadingIcon = {
                Icon(
                    imageVector = selectedItem.third,
                    contentDescription = null,
                    tint = TextMutedBlue
                )
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(),
            singleLine = true,
            shape = RoundedCornerShape(Dimens.RadiusLarge),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = InputBackground,
                unfocusedContainerColor = InputBackground,
                disabledContainerColor = InputBackground,
                focusedBorderColor = FocusedBorderColor,
                unfocusedBorderColor = UnfocusedBorderColor,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedTrailingIconColor = TextMutedBlue,
                unfocusedTrailingIconColor = TextMutedBlue
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            shape = RoundedCornerShape(Dimens.RadiusMedium),
            containerColor = InputBackground
        ) {
            categories.forEach { (categoryInt, label, icon) ->
                val isSelected = categoryInt == selectedCategory
                val itemTextColor = if (isSelected) AccentBlue else Color.White
                val itemIconColor = if (isSelected) AccentBlue else TextMutedBlue

                DropdownMenuItem(
                    text = {
                        Text(
                            text = label,
                            color = itemTextColor,
                            fontSize = Dimens.FontSizeMedium,
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
                                contentDescription = Strings.SelectedIndicator,
                                tint = AccentBlue
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
