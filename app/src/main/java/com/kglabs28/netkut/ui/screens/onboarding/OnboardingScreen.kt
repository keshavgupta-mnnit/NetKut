package com.kglabs28.netkut.ui.screens.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kglabs28.netkut.R
import com.kglabs28.netkut.ui.theme.AccentBlue
import com.kglabs28.netkut.ui.theme.BlockBadgeRed
import com.kglabs28.netkut.ui.theme.ButtonGreen
import com.kglabs28.netkut.ui.theme.ButtonOrange
import com.kglabs28.netkut.ui.theme.ButtonRed
import com.kglabs28.netkut.ui.theme.CardBackground
import com.kglabs28.netkut.ui.theme.Dimens
import com.kglabs28.netkut.ui.theme.FeatureSyncPurple
import com.kglabs28.netkut.ui.theme.GradientEnd
import com.kglabs28.netkut.ui.theme.GradientStart
import com.kglabs28.netkut.ui.theme.IconBackgroundBlue
import com.kglabs28.netkut.ui.theme.PhoneBorderBackground
import com.kglabs28.netkut.ui.theme.PhoneBorderColor
import com.kglabs28.netkut.ui.theme.Strings
import com.kglabs28.netkut.ui.theme.TextMutedBlue

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(GradientStart, GradientEnd)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        Scaffold(
            containerColor = Color.Transparent
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = Dimens.PaddingLarge, vertical = Dimens.PaddingMedium),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // 1. App Header: NetKut Icon + NetKut Name
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_titlebar),
                        contentDescription = Strings.AppName,
                        modifier = Modifier.size(Dimens.IconSizeSmall)
                    )
                    Spacer(modifier = Modifier.width(Dimens.SpacingSmall))
                    Text(
                        text = Strings.AppName,
                        style = TextStyle(
                            fontSize = Dimens.FontSizeHeading,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }

                // 3. Graphic Illustration: Phone & VPN Shield Diagram (Bigger, prominent graphic)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            color = IconBackgroundBlue
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = null,
                                    tint = AccentBlue,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Surface(
                            modifier = Modifier
                                .width(95.dp)
                                .height(140.dp),
                            shape = RoundedCornerShape(16.dp),
                            color = PhoneBorderBackground,
                            border = BorderStroke(1.5.dp, PhoneBorderColor)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Surface(
                                    modifier = Modifier
                                        .width(60.dp)
                                        .height(75.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    color = IconBackgroundBlue,
                                    border = BorderStroke(1.dp, AccentBlue)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(
                                                imageVector = Icons.Default.Security,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(26.dp)
                                            )
                                            Text(
                                                text = "VPN",
                                                style = TextStyle(
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CompactAppBadge(icon = Icons.Default.SportsEsports, color = ButtonGreen)
                            CompactAppBadge(icon = Icons.Default.Movie, color = ButtonRed)
                            CompactAppBadge(icon = Icons.Default.Language, color = ButtonOrange)
                        }
                    }
                }

                // 2. Heading: Block Internet Access for Selected Apps
                Text(
                    text = Strings.OnboardingWelcomeTitle,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentBlue,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                // 4. Heading: How It Works
                Text(
                    text = Strings.HowItWorksTitle,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )

                CompactFeatureCard(
                    icon = Icons.Default.Security,
                    iconBackgroundColor = IconBackgroundBlue,
                    title = Strings.VpnServiceTitle,
                    description = Strings.VpnServiceDesc
                )

                // 5. Feature Card 1: Start / Pause
                CompactFeatureCard(
                    icon = Icons.Default.PlayArrow,
                    iconBackgroundColor = AccentBlue,
                    title = Strings.StartPauseTitle,
                    description = Strings.StartPauseDesc
                )

                // 6. Feature Card 2: Sync Every 2 Hours
                CompactFeatureCard(
                    icon = Icons.Default.Refresh,
                    iconBackgroundColor = FeatureSyncPurple,
                    title = Strings.SyncIntervalTitle,
                    description = Strings.SyncIntervalDesc
                )

                // 7. Feature Card 3: VPN Icon in Status Bar
                CompactFeatureCard(
                    icon = Icons.Default.Key,
                    iconBackgroundColor = AccentBlue,
                    title = Strings.VpnNoticeTitle,
                    description = Strings.VpnNoticeDesc
                )

                // 8. Feature Card 4: Expected & Safe
                CompactFeatureCard(
                    icon = Icons.Default.Security,
                    iconBackgroundColor = IconBackgroundBlue,
                    title = Strings.ExpectedSafeTitle,
                    description = Strings.ExpectedSafeDesc
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 9. Button - Ok Got It
                Button(
                    onClick = onComplete,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.ButtonHeightCompact),
                    shape = RoundedCornerShape(Dimens.RadiusLarge),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = Strings.OkGotIt,
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun CompactFeatureCard(
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
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(38.dp),
                shape = CircleShape,
                color = iconBackgroundColor
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = description,
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = TextMutedBlue
                    ),
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
private fun CompactAppBadge(
    icon: ImageVector,
    color: Color
) {
    Box(modifier = Modifier.size(34.dp)) {
        Surface(
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.TopStart),
            shape = RoundedCornerShape(6.dp),
            color = color
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Surface(
            modifier = Modifier
                .size(14.dp)
                .align(Alignment.BottomEnd),
            shape = CircleShape,
            color = BlockBadgeRed,
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .width(8.dp)
                        .height(2.dp)
                        .background(Color.White)
                )
            }
        }
    }
}
