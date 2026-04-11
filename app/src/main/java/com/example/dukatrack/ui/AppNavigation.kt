package com.example.dukatrack.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dukatrack.data.AppDatabase
import com.example.dukatrack.ui.sales.SaleViewModel
import com.example.dukatrack.ui.sales.SaleViewModelFactory

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: screen_names.Dashboard

    // Get ViewModel
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val viewModel: SaleViewModel = viewModel(
        factory = SaleViewModelFactory(database.salesDao())
    )
    val state by viewModel.state.collectAsState()

    // Map routes to titles
    val title = when (currentRoute) {
        screen_names.Dashboard -> "Dashboard"
        screen_names.Products -> "Products"
        screen_names.NewSale -> "New Sale"
        screen_names.SalesHistory -> "Sales History"
        else -> "Dukatrack"
    }

    MainLayout(navController = navController, title = title) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = screen_names.Dashboard,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(screen_names.Dashboard) {
                DashboardScreen(
                    navController = navController,
                    state = state,
                    onEvent = viewModel::onEvent
                )
            }
            composable(screen_names.Products) {
                ProductsScreen(navController = navController)
            }
            composable(screen_names.NewSale) {
                NewSaleScreen(navController = navController)
            }
            composable(screen_names.SalesHistory) {
                SalesHistoryScreen(navController = navController)
            }
        }
    }
}
