package com.kglabs28.netkut

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kglabs28.netkut.ui.screens.main.MainScreen
import com.kglabs28.netkut.ui.screens.main.MainViewModel
import com.kglabs28.netkut.ui.screens.onboarding.OnboardingScreen
import com.kglabs28.netkut.ui.theme.NetKutTheme
import com.kglabs28.netkut.util.AppUtils

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
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
                var showOnboarding by remember {
                    mutableStateOf(!AppUtils.getOnboardingCompleted(this))
                }

                if (showOnboarding) {
                    OnboardingScreen(
                        onComplete = {
                            AppUtils.setOnboardingCompleted(this, true)
                            showOnboarding = false
                        }
                    )
                } else {
                    MainScreen(viewModel = viewModel)
                }
            }
        }
    }
}
