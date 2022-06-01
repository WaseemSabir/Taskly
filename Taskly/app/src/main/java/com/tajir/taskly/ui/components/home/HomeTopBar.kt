package com.tajir.taskly.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Colors
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeTopBar(
    onSearchClick : () -> Unit,
    onRefreshClick : () -> Unit,
    onLogoutClick : () -> Unit,
    backgroundColor: Color
){
    Row(
        modifier = Modifier.fillMaxWidth().background(backgroundColor),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "My Tasks",
            modifier = Modifier.padding(16.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )

        Row(
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(
                onClick = onSearchClick,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Button",
                )
            }

            IconButton(
                onClick = onRefreshClick,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Refresh",
                )
            }

            IconButton(
                onClick = onLogoutClick,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Logout,
                    contentDescription = "Logout",
                )
            }
        }
    }
}