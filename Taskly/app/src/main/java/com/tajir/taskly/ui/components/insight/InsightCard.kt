package com.tajir.taskly.ui.components.insight

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun InsightCard(
    modifier: Modifier,
    heading : String,
    subtext : String
) {
    Card(
        modifier = modifier,
        elevation = 10.dp,
        shape = RoundedCornerShape(20)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = heading,
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                fontWeight = FontWeight.W600
            )

            Text(
                text = subtext,
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(vertical = 10.dp)
            )
        }
    }
}
