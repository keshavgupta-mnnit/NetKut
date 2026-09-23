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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.kglabs28.netkut.ui.theme.AccentBlue
import com.kglabs28.netkut.ui.theme.ButtonGreen
import com.kglabs28.netkut.ui.theme.CardBackground
import com.kglabs28.netkut.ui.theme.Dimens
import com.kglabs28.netkut.ui.theme.FeatureSyncPurple
import com.kglabs28.netkut.ui.theme.PhoneBorderColor
import com.kglabs28.netkut.ui.theme.Strings
import com.kglabs28.netkut.ui.theme.TextMutedBlue

@Composable
fun HowItWorksOnboardingPage(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.PaddingExtraLarge),
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingLarge)
    ) {
        Spacer(modifier = Modifier.height(Dimens.SpacingSmall))

        Text(
            text = Strings.HowItWorksTitle,
            style = TextStyle(
                fontSize = Dimens.FontSizeLargeHeading,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )

        // Card 1: VPN Service
        FeatureCard(
            icon = Icons.Default.Security,
            iconBackgroundColor = ButtonGreen,
            title = Strings.VpnServiceTitle,
            description = Strings.VpnServiceDesc
        )

        // Card 2: Start / Pause
        FeatureCard(
            icon = Icons.Default.PlayArrow,
            iconBackgroundColor = AccentBlue,
            title = Strings.StartPauseTitle,
            description = Strings.StartPauseDesc
        )

        // Card 3: Sync Every 2 Hours
        FeatureCard(
            icon = Icons.Default.Refresh,
            iconBackgroundColor = FeatureSyncPurple,
            title = Strings.SyncIntervalTitle,
            description = Strings.SyncIntervalDesc
        )
    }
}

@Composable
private fun FeatureCard(
    icon: ImageVector,
    iconBackgroundColor: Color,
    title: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.RadiusMedium),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        border = BorderStroke(Dimens.DividerThickness, PhoneBorderColor)
    ) {
        Row(
            modifier = Modifier.padding(Dimens.PaddingLarge),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                modifier = Modifier.size(Dimens.FeatureCardIconSize),
                shape = CircleShape,
                color = iconBackgroundColor
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(Dimens.RadiusLarge)
                    )
                }
            }

            Spacer(modifier = Modifier.width(Dimens.SpacingLarge))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = Dimens.FontSizeCardTitle,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(Dimens.PaddingTiny))

                Text(
                    text = description,
                    style = TextStyle(
                        fontSize = Dimens.FontSizeMedium,
                        color = TextMutedBlue
                    )
                )
            }
        }
    }
}
