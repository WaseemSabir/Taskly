package com.tajir.taskly.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors

@Composable
fun TasklyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        content = content
    )
}
