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
import com.example.dukatrack.ui.products.ProductViewModel
import com.example.dukatrack.ui.products.ProductViewModelFactory
import com.example.dukatrack.ui.sales.DashboardViewModel
import com.example.dukatrack.ui.sales.DashboardViewModelFactory
import com.example.dukatrack.ui.sales.NewSaleViewModel
import com.example.dukatrack.ui.sales.NewSaleViewModelFactory
import com.example.dukatrack.ui.sales.SalesHistoryViewModel
import com.example.dukatrack.ui.sales.SalesHistoryViewModelFactory

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: screen_names.Dashboard

    // Get ViewModel
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val viewModel: DashboardViewModel = viewModel(
        factory = DashboardViewModelFactory(database.salesDao())
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
                val productViewModel: ProductViewModel = viewModel(
                    factory = ProductViewModelFactory(database.productDao())
                )
                ProductsScreen(navController = navController, viewModel = productViewModel)
            }
            composable(screen_names.NewSale) {
                val newSaleViewModel: NewSaleViewModel = viewModel(
                    factory = NewSaleViewModelFactory(database.productDao(), database.salesDao(), database.customerDao())
                )
                NewSaleScreen(navController = navController, viewModel = newSaleViewModel)
            }
            composable(screen_names.SalesHistory) {
                val salesHistoryViewModel: SalesHistoryViewModel = viewModel(
                    factory = SalesHistoryViewModelFactory(database.salesDao())
                )
                SalesHistoryScreen(navController = navController, viewModel = salesHistoryViewModel)
            }
        }
    }
}
