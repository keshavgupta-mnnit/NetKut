package com.kglabs28.netkut

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kglabs28.netkut.ui.screens.about.AboutUsScreen
import com.kglabs28.netkut.ui.screens.main.MainScreen
import com.kglabs28.netkut.ui.screens.main.MainViewModel
import com.kglabs28.netkut.ui.screens.onboarding.OnboardingScreen
import com.kglabs28.netkut.ui.screens.settings.SettingsScreen
import com.kglabs28.netkut.ui.theme.NetKutTheme
import com.kglabs28.netkut.util.AppUtils
import com.kglabs28.netkut.util.ShortcutUtils
import com.kglabs28.netkut.util.VpnUtils

enum class CurrentScreen {
    MAIN,
    SETTINGS,
    ABOUT_US,
    HOW_IT_WORKS_ONBOARDING
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleShortcutIntent(intent)
        
        val appContainer = (application as NetKutApplication).container
        
        val viewModel: MainViewModel by viewModels {
            MainViewModel.provideFactory(
                appRepository = appContainer.appRepository,
                blocklistRepository = appContainer.blocklistRepository
            )
        }

        enableEdgeToEdge()
        setContent {
            NetKutTheme {
                var showInitialOnboarding by remember {
                    mutableStateOf(!AppUtils.getOnboardingCompleted(this))
                }

                var currentScreen by remember { mutableStateOf(CurrentScreen.MAIN) }

                if (showInitialOnboarding) {
                    OnboardingScreen(
                        onComplete = {
                            AppUtils.setOnboardingCompleted(this, true)
                            showInitialOnboarding = false
                        }
                    )
                } else {
                    when (currentScreen) {
                        CurrentScreen.MAIN -> {
                            MainScreen(
                                viewModel = viewModel,
                                onSettingsClick = {
                                    currentScreen = CurrentScreen.SETTINGS
                                }
                            )
                        }

                        CurrentScreen.SETTINGS -> {
                            BackHandler {
                                currentScreen = CurrentScreen.MAIN
                            }
                            SettingsScreen(
                                onBackClick = {
                                    currentScreen = CurrentScreen.MAIN
                                },
                                onHowItWorksClick = {
                                    currentScreen = CurrentScreen.HOW_IT_WORKS_ONBOARDING
                                },
                                onAboutUsClick = {
                                    currentScreen = CurrentScreen.ABOUT_US
                                }
                            )
                        }

                        CurrentScreen.ABOUT_US -> {
                            BackHandler {
                                currentScreen = CurrentScreen.SETTINGS
                            }
                            AboutUsScreen(
                                onBackClick = {
                                    currentScreen = CurrentScreen.SETTINGS
                                }
                            )
                        }

                        CurrentScreen.HOW_IT_WORKS_ONBOARDING -> {
                            BackHandler {
                                currentScreen = CurrentScreen.SETTINGS
                            }
                            OnboardingScreen(
                                onComplete = {
                                    currentScreen = CurrentScreen.SETTINGS
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleShortcutIntent(intent)
    }

    private fun handleShortcutIntent(intent: Intent?) {
        when (intent?.action) {
            ShortcutUtils.ACTION_START_VPN -> {
                AppUtils.setVpnActive(this, true)
                val vpnIntent = VpnUtils.prepareVpnIntent(this)
                if (vpnIntent == null) {
                    VpnUtils.startVpnService(this)
                }
            }
            ShortcutUtils.ACTION_STOP_VPN, "com.kglabs28.netkut.ACTION_PAUSE_VPN" -> {
                AppUtils.setVpnActive(this, false)
            }
        }
    }
}
