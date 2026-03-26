package com.example.dukatrack.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController


@Composable
fun ProductsScreen(navController: NavController) {
    // Implement the UI for the Products screen
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = "Products Screen")
    }
}

