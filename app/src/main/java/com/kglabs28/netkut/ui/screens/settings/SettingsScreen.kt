package com.kglabs28.netkut.ui.screens.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.kglabs28.netkut.R
import com.kglabs28.netkut.ui.components.AppDropdown
import com.kglabs28.netkut.ui.components.DropdownItem
import com.kglabs28.netkut.ui.theme.AccentBlue
import com.kglabs28.netkut.ui.theme.CardBackground
import com.kglabs28.netkut.ui.theme.Dimens
import com.kglabs28.netkut.ui.theme.GradientEnd
import com.kglabs28.netkut.ui.theme.GradientStart
import com.kglabs28.netkut.ui.theme.IconBackgroundBlue
import com.kglabs28.netkut.ui.theme.PhoneBorderColor
import com.kglabs28.netkut.ui.theme.Strings
import com.kglabs28.netkut.ui.theme.TextMutedBlue
import com.kglabs28.netkut.util.AppUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onHowItWorksClick: () -> Unit,
    onAboutUsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedMinutes by remember { mutableStateOf(AppUtils.getSyncIntervalMinutes(context)) }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(GradientStart, GradientEnd)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_titlebar),
                                contentDescription = Strings.AppName,
                                modifier = Modifier.size(Dimens.IconSizeBadgeSmall + Dimens.SpacingMedium)
                            )
                            Spacer(modifier = Modifier.width(Dimens.SpacingMedium))
                            Text(Strings.SettingsTitle)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = Strings.Cancel,
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = Dimens.PaddingScreen, vertical = Dimens.PaddingLarge),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Dimens.SpacingLarge),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Option 1: Sync Interval Card with Dropdown
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(Dimens.RadiusMedium),
                        colors = CardDefaults.cardColors(
                            containerColor = CardBackground
                        ),
                        border = BorderStroke(Dimens.DividerThickness, PhoneBorderColor)
                    ) {
                        Column(
                            modifier = Modifier.padding(Dimens.PaddingLarge),
                            verticalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Surface(
                                    modifier = Modifier.size(Dimens.PaddingExtraLarge * 2),
                                    shape = CircleShape,
                                    color = IconBackgroundBlue
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Schedule,
                                            contentDescription = null,
                                            tint = AccentBlue,
                                            modifier = Modifier.size(Dimens.PaddingExtraLarge)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(Dimens.PaddingMedium))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = Strings.SyncIntervalSettingTitle,
                                        style = TextStyle(
                                            fontSize = Dimens.FontSizeTitle,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(Dimens.PaddingTiny))

                                    Text(
                                        text = Strings.SyncIntervalSettingDesc,
                                        style = TextStyle(
                                            fontSize = Dimens.FontSizeMedium,
                                            color = TextMutedBlue
                                        )
                                    )
                                }

                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = TextMutedBlue,
                                    modifier = Modifier.size(Dimens.PaddingExtraLarge)
                                )
                            }

                            // Sync Interval Dropdown
                            val syncItems = AppUtils.syncIntervals.map { item ->
                                DropdownItem(
                                    id = item.minutes,
                                    label = item.label,
                                    icon = if (item.minutes <= 0) Icons.Default.Block else Icons.Default.Schedule
                                )
                            }

                            AppDropdown(
                                items = syncItems,
                                selectedId = selectedMinutes,
                                onItemSelected = { newMinutes ->
                                    selectedMinutes = newMinutes
                                    AppUtils.setSyncIntervalMinutes(context, newMinutes)
                                }
                            )
                        }
                    }

                    // Option 2: How It Works
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onHowItWorksClick() },
                        shape = RoundedCornerShape(Dimens.RadiusMedium),
                        colors = CardDefaults.cardColors(
                            containerColor = CardBackground
                        ),
                        border = BorderStroke(Dimens.DividerThickness, PhoneBorderColor)
                    ) {
                        Row(
                            modifier = Modifier.padding(Dimens.PaddingLarge),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier.size(Dimens.PaddingExtraLarge * 2),
                                shape = CircleShape,
                                color = IconBackgroundBlue
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Lightbulb,
                                        contentDescription = null,
                                        tint = AccentBlue,
                                        modifier = Modifier.size(Dimens.PaddingExtraLarge)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(Dimens.PaddingMedium))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = Strings.HowItWorksTitle,
                                    style = TextStyle(
                                        fontSize = Dimens.FontSizeTitle,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )

                                Spacer(modifier = Modifier.height(Dimens.PaddingTiny))

                                Text(
                                    text = Strings.HowItWorksLearnMore,
                                    style = TextStyle(
                                        fontSize = Dimens.FontSizeMedium,
                                        color = TextMutedBlue
                                    )
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = TextMutedBlue,
                                modifier = Modifier.size(Dimens.PaddingExtraLarge)
                            )
                        }
                    }

                    // Option 3: About Us
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAboutUsClick() },
                        shape = RoundedCornerShape(Dimens.RadiusMedium),
                        colors = CardDefaults.cardColors(
                            containerColor = CardBackground
                        ),
                        border = BorderStroke(Dimens.DividerThickness, PhoneBorderColor)
                    ) {
                        Row(
                            modifier = Modifier.padding(Dimens.PaddingLarge),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier.size(Dimens.PaddingExtraLarge * 2),
                                shape = CircleShape,
                                color = IconBackgroundBlue
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = AccentBlue,
                                        modifier = Modifier.size(Dimens.PaddingExtraLarge)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(Dimens.PaddingMedium))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = Strings.AboutUsTitle,
                                    style = TextStyle(
                                        fontSize = Dimens.FontSizeTitle,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )

                                Spacer(modifier = Modifier.height(Dimens.PaddingTiny))

                                Text(
                                    text = Strings.RateUsOnPlayStore,
                                    style = TextStyle(
                                        fontSize = Dimens.FontSizeMedium,
                                        color = TextMutedBlue
                                    )
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = TextMutedBlue,
                                modifier = Modifier.size(Dimens.PaddingExtraLarge)
                            )
                        }
                    }
                }

                // Version Text Footer
                Text(
                    text = Strings.VersionText,
                    style = TextStyle(
                        fontSize = Dimens.FontSizeMedium,
                        color = TextMutedBlue,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Dimens.PaddingMedium)
                )
            }
        }
    }
}
