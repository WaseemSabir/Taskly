package com.tajir.taskly.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tajir.taskly.ui.screens.*

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalAnimationApi
@Composable
fun NavigationRoutes() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash_screen") {
        composable("splash_screen") {
            SplashScreen(navController = navController)
        }

        composable("login_screen") {
            AuthenticationScreen(navController = navController)
        }

        composable("home_screen") {
            HomeScreen(navController = navController)
        }

        composable("task_view") {
            TaskScreen(type="view", navController=navController)
        }
        
        composable("task_edit") {
            TaskScreen(type = "edit", navController=navController)
        }

        composable("history_screen") {
            HistoryScreen(navController = navController)
        }

        composable("insight_screen") {
            InsightsScreen(navController = navController)
        }

        composable("profile_screen") {
            ProfileScreen(navController = navController)
        }

        composable("search_screen") {
            SearchScreen(navController = navController)
        }
    }
}
