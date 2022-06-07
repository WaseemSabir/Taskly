package com.tajir.taskly.ui.components

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.tajir.taskly.navigation.NavigationItem

@Composable
fun BottomNavigationBar(
    navController: NavController,
    modifier: Modifier,
    navBackground : Color
) {
    val navItems = listOf(
        NavigationItem.Home,
        NavigationItem.History,
        NavigationItem.Insights,
        NavigationItem.Profile
    )

    BottomNavigation(
        backgroundColor = navBackground
    ) {
        navItems.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(text = item.title) },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.White,
                alwaysShowLabel = true,
                selected = false,
                onClick = {
                    navController.navigate(item.route)
                }
            )
        }
    }
}
