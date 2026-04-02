package com.example.dukatrack.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun AppNavigation(){
    val navController  = rememberNavController()
    NavHost(
        navController=navController,
        startDestination = screen_names.Dashboard
    ){
        composable(screen_names.Dashboard){
            DashboardScreen(
                navController = navController
            )

        }
        composable(screen_names.Products) {
            ProductsScreen(
                navController = navController
            )
        }
        composable ( screen_names.NewSale ){
            NewSaleScreen(
                navController = navController
            )
        }
    }


}