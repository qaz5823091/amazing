package com.cppdesigns.amazing_assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cppdesigns.amazing_assignment.screen.MainScreen
import com.cppdesigns.amazing_assignment.ui.theme.AmazingassignmentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AmazingassignmentTheme {
                MainScreen()
            }
        }
    }
}
