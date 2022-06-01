package com.tajir.taskly.ui.components.home

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable

@Composable
fun FloatingButtonHome(
    onClick : () -> Unit
){
    FloatingActionButton(
        onClick = onClick
    ) {
        Icon(Icons.Filled.Add, "Add Tasks")
    }
}