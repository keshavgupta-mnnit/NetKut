package com.kglabs28.netkut.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.kglabs28.netkut.ui.main.AppTab
import com.kglabs28.netkut.ui.theme.Dimens
import com.kglabs28.netkut.ui.theme.FocusedBorderColor
import com.kglabs28.netkut.ui.theme.InputBackground
import com.kglabs28.netkut.ui.theme.Strings
import com.kglabs28.netkut.ui.theme.TextMutedBlue
import com.kglabs28.netkut.ui.theme.UnfocusedBorderColor

@Composable
fun SearchBar(
    query: String,
    selectedTab: AppTab,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.PaddingLarge, vertical = Dimens.PaddingSmall),
        placeholder = {
            Text(
                if (selectedTab == AppTab.SELECTED_APPS) Strings.SearchSelectedApps else Strings.SearchApps
            )
        },
        singleLine = true,
        textStyle = TextStyle(fontSize = Dimens.FontSizeRegular, color = Color.White),
        shape = RoundedCornerShape(Dimens.RadiusLarge),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = InputBackground,
            unfocusedContainerColor = InputBackground,
            disabledContainerColor = InputBackground,
            focusedBorderColor = FocusedBorderColor,
            unfocusedBorderColor = UnfocusedBorderColor,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedPlaceholderColor = TextMutedBlue,
            unfocusedPlaceholderColor = TextMutedBlue,
            focusedLeadingIconColor = TextMutedBlue,
            unfocusedLeadingIconColor = TextMutedBlue,
            focusedTrailingIconColor = TextMutedBlue,
            unfocusedTrailingIconColor = TextMutedBlue
        ),
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = Strings.SearchApps)
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = Strings.ClearSearch)
                }
            }
        }
    )
}
