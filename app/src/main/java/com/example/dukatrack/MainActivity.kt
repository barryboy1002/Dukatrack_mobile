    package com.example.dukatrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.dukatrack.ui.AppNavigation
import com.example.dukatrack.ui.theme.DukatrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DukatrackTheme {
                AppNavigation()
            }
        }
    }
}
