package com.tajir.taskly.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tajir.taskly.ui.screens.SplashScreen
import com.tajir.taskly.ui.screens.AuthenticationScreen

@Composable
fun NavigationRoutes() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash_screen") {
        composable("splash_screen") {
            SplashScreen(navController = navController)
        }

        composable("login_screen") {
            AuthenticationScreen()
        }
    }
}
