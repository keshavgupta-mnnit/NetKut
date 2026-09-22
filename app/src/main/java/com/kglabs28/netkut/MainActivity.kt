package com.kglabs28.netkut

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.kglabs28.netkut.ui.main.MainScreen
import com.kglabs28.netkut.ui.main.MainViewModel
import com.kglabs28.netkut.ui.theme.NetKutTheme

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
                MainScreen(viewModel = viewModel)
            }
        }
    }
}
