package com.tajir.taskly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.tajir.taskly.ui.theme.TasklyTheme
import com.tajir.taskly.navigation.NavigationRoutes
import com.tajir.taskly.viewModels.UserStateViewModel
import com.tajir.taskly.viewModels.UserState
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider

class MainActivity : ComponentActivity() {
    private val userState by viewModels<UserStateViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(UserState provides userState) {
                TasklyTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        NavigationRoutes()
                    }
                }
            }
        }
    }
}
