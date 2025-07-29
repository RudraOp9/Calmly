package com.calmly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.calmly.di.appModule
import com.calmly.navigation.CalmlyNavigation
import com.calmly.presentation.components.TopBar
import com.calmly.presentation.screens.MainScreen
import com.calmly.presentation.theme.CalmlyTheme
import com.calmly.presentation.viewmodel.SoundsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplication
import org.koin.core.logger.Level

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            KoinApplication(
                application = {
                    androidLogger(Level.DEBUG)
                    androidContext(this@MainActivity)
                    modules(appModule)
                }
            ) {
                CalmlyTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MainScreen()
                       // CalmlyNavigation(this@MainActivity)
                    }
                }
            }
        }
    }
}