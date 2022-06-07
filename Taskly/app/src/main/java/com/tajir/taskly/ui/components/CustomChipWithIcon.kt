package com.tajir.taskly.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun CustomChipWithIcon(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    icon : ImageVector,
    iconDesc : String = ""
) {
    Surface(
        color = when {
            selected -> MaterialTheme.colors.primary
            else -> Color.Transparent
        },
        contentColor = when {
            selected -> MaterialTheme.colors.onPrimary
            else -> Color.LightGray
        },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = when {
                selected -> MaterialTheme.colors.primary
                else -> Color.LightGray
            }
        ),
        modifier = modifier
    ) {
        // Inside a Row pack the Image and text together to
        // show inside the chip
        Row(modifier = Modifier) {
            Icon(icon,iconDesc, tint = Color.Black)
            Text(
                text = text,
                modifier = Modifier.padding(end = 8.dp, top = 8.dp, bottom = 8.dp)
            )
        }
    }
}