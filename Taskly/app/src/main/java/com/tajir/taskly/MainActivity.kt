package com.tajir.taskly

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.tajir.taskly.ui.theme.TasklyTheme
import com.tajir.taskly.navigation.NavigationRoutes
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.CompositionLocalProvider
import com.tajir.taskly.viewModels.*

class MainActivity : ComponentActivity() {
    private val userState by viewModels<UserStateViewModel>()
    private val authState by viewModels<AuthenticationViewModel>()
    private val taskState by viewModels<TaskStateViewModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(
                UserState provides userState,
                AuthState provides authState,
                TaskState provides taskState
            ) {
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
