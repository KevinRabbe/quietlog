package com.kevinrabbe.quietlog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.kevinrabbe.quietlog.domain.model.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val settingsRepository = (application as QuietLogApplication).container.settingsRepository

        setContent {
            val theme by settingsRepository.theme.collectAsState(initial = AppTheme.SYSTEM)
            QuietLogApp(theme = theme)
        }
    }
}

