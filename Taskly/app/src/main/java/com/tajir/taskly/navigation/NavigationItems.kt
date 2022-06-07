package com.tajir.taskly.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(var route: String, var icon: ImageVector, var title: String) {
    object Home : NavigationItem("home_screen", Icons.Default.Home, "Home")
    object History : NavigationItem("history_screen", Icons.Default.History, "History")
    object Insights : NavigationItem("insight_screen", Icons.Default.Insights, "Insights")
    object Profile : NavigationItem("profile_screen", Icons.Default.Person, "Profile")
    object Search : NavigationItem("search_screen", Icons.Default.Search, "Search")
}
