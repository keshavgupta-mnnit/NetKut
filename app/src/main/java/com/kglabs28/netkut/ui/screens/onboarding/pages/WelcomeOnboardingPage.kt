package com.kglabs28.netkut.ui.screens.onboarding.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import com.kglabs28.netkut.ui.theme.AccentBlue
import com.kglabs28.netkut.ui.theme.BlockBadgeRed
import com.kglabs28.netkut.ui.theme.ButtonGreen
import com.kglabs28.netkut.ui.theme.ButtonOrange
import com.kglabs28.netkut.ui.theme.ButtonRed
import com.kglabs28.netkut.ui.theme.Dimens
import com.kglabs28.netkut.ui.theme.IconBackgroundBlue
import com.kglabs28.netkut.ui.theme.PhoneBorderBackground
import com.kglabs28.netkut.ui.theme.PhoneBorderColor
import com.kglabs28.netkut.ui.theme.Strings
import com.kglabs28.netkut.ui.theme.TextMutedBlue

@Composable
fun WelcomeOnboardingPage(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.PaddingScreen),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

        // Center Graphic Illustration
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.OnboardingGraphicHeight),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Globe / Network Icon on Left
                Surface(
                    modifier = Modifier.size(Dimens.IconSizeMedium),
                    shape = CircleShape,
                    color = IconBackgroundBlue
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = null,
                            tint = AccentBlue,
                            modifier = Modifier.size(Dimens.RadiusLarge)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(Dimens.SpacingMedium))

                // Phone with VPN Shield
                Surface(
                    modifier = Modifier
                        .width(Dimens.PhoneIllustrationWidth)
                        .height(Dimens.PhoneIllustrationHeight),
                    shape = RoundedCornerShape(Dimens.RadiusHuge),
                    color = PhoneBorderBackground,
                    border = BorderStroke(Dimens.PaddingTiny, PhoneBorderColor)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Surface(
                            modifier = Modifier
                                .width(Dimens.ShieldCardWidth)
                                .height(Dimens.ShieldCardHeight),
                            shape = RoundedCornerShape(Dimens.PaddingMedium),
                            color = IconBackgroundBlue,
                            border = BorderStroke(Dimens.DividerThickness, AccentBlue)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.Security,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(Dimens.SpacingHuge)
                                    )
                                    Text(
                                        "VPN",
                                        style = TextStyle(
                                            fontSize = Dimens.FontSizeSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(Dimens.SpacingLarge))

                // App Badges Column with Blocked Symbols
                Column(
                    verticalArrangement = Arrangement.spacedBy(Dimens.SpacingLarge),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AppBadgeWithBlocked(
                        icon = Icons.Default.SportsEsports,
                        color = ButtonGreen
                    )
                    AppBadgeWithBlocked(
                        icon = Icons.Default.Movie,
                        color = ButtonRed
                    )
                    AppBadgeWithBlocked(
                        icon = Icons.Default.Language,
                        color = ButtonOrange
                    )
                }
            }
        }

        // Title and Description
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = Dimens.SpacingHuge)
        ) {
            Text(
                text = Strings.OnboardingWelcomeTitle,
                style = TextStyle(
                    fontSize = Dimens.FontSizeLargeHeading,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingLarge))

            val annotatedDesc = buildAnnotatedString {
                append(Strings.OnboardingWelcomeDescPart1)
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = AccentBlue)) {
                    append(Strings.OnboardingWelcomeDescPart2)
                }
                append(Strings.OnboardingWelcomeDescPart3)
            }

            Text(
                text = annotatedDesc,
                style = TextStyle(
                    fontSize = Dimens.FontSizeRegular,
                    color = TextMutedBlue,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

@Composable
private fun AppBadgeWithBlocked(
    icon: ImageVector,
    color: Color
) {
    Box(modifier = Modifier.size(Dimens.IconSizeMedium)) {
        Surface(
            modifier = Modifier
                .size(Dimens.IconSizeBadge)
                .align(Alignment.TopStart),
            shape = RoundedCornerShape(Dimens.RadiusSmall),
            color = color
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(Dimens.PaddingExtraLarge)
                )
            }
        }
        
        Surface(
            modifier = Modifier
                .size(Dimens.IconSizeBadgeSmall)
                .align(Alignment.BottomEnd),
            shape = CircleShape,
            color = BlockBadgeRed,
            border = BorderStroke(Dimens.DividerThickness, Color.Black)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .width(Dimens.SpacingGrid)
                        .height(Dimens.PaddingTiny)
                        .background(Color.White)
                )
            }
        }
    }
}
