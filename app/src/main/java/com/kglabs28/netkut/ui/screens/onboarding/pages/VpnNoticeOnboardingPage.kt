package com.kglabs28.netkut.ui.screens.onboarding.pages

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.kglabs28.netkut.ui.theme.AccentBlue
import com.kglabs28.netkut.ui.theme.CardBackground
import com.kglabs28.netkut.ui.theme.Dimens
import com.kglabs28.netkut.ui.theme.IconBackgroundBlue
import com.kglabs28.netkut.ui.theme.PhoneBorderBackground
import com.kglabs28.netkut.ui.theme.PhoneBorderColor
import com.kglabs28.netkut.ui.theme.Strings
import com.kglabs28.netkut.ui.theme.TextMutedBlue

@Composable
fun VpnNoticeOnboardingPage(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.PaddingExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Graphic Placeholder: Status bar illustration
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.OnboardingStatusGraphicHeight),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(Dimens.OnboardingGraphicHeight - Dimens.IconSizeLarge),
                shape = RoundedCornerShape(topStart = Dimens.RadiusHuge, topEnd = Dimens.RadiusHuge),
                color = PhoneBorderBackground,
                border = BorderStroke(Dimens.PaddingTiny, PhoneBorderColor)
            ) {
                Column(
                    modifier = Modifier.padding(Dimens.PaddingLarge),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Status bar row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "9:41",
                            style = TextStyle(
                                fontSize = Dimens.FontSizeSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(Dimens.PaddingTiny),
                                color = AccentBlue,
                                modifier = Modifier.size(Dimens.PaddingLarge)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Key,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(Dimens.SpacingGrid)
                                    )
                                }
                            }
                            Text(
                                "VPN",
                                style = TextStyle(
                                    fontSize = Dimens.FontSizeSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentBlue
                                )
                            )
                        }
                    }
                }
            }
        }

        // Title and Description
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = Dimens.SpacingSmall)
        ) {
            Text(
                text = Strings.VpnNoticeTitle,
                style = TextStyle(
                    fontSize = Dimens.FontSizeHeading,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))

            Text(
                text = Strings.VpnNoticeDesc,
                style = TextStyle(
                    fontSize = Dimens.FontSizeRegular,
                    color = TextMutedBlue,
                    textAlign = TextAlign.Center
                )
            )
        }

        // Safety Info Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimens.PaddingLarge),
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
                    modifier = Modifier.size(Dimens.FeatureCardIconSize),
                    shape = RoundedCornerShape(Dimens.PaddingMedium),
                    color = IconBackgroundBlue,
                    border = BorderStroke(Dimens.DividerThickness, AccentBlue)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = AccentBlue,
                            modifier = Modifier.size(Dimens.RadiusLarge)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(Dimens.SpacingLarge))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = Strings.ExpectedSafeTitle,
                        style = TextStyle(
                            fontSize = Dimens.FontSizeTitle,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(Dimens.PaddingTiny))

                    Text(
                        text = Strings.ExpectedSafeDesc,
                        style = TextStyle(
                            fontSize = Dimens.FontSizeMedium,
                            color = TextMutedBlue
                        )
                    )
                }
            }
        }
    }
}
