package com.tajir.taskly.ui.components.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun OutlinedInputField(
    value: String,
    modifier: Modifier,
    label: String,
    onChangedHandler: (newVal: String) -> Unit
) {
    OutlinedTextField(
        value = value,
        label = {
            Text(text = label)
        },
        onValueChange = { newVal ->
            onChangedHandler(newVal)
        },
        modifier = modifier.fillMaxWidth()
    )
}
