package com.tajir.taskly.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun NameInput(
    modifier: Modifier = Modifier,
    name: String?,
    onNameChanged: (name: String) -> Unit,
    label_res: Int
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = name ?: "",
        onValueChange = { name ->
            onNameChanged(name)
        },
        label = {
            Text(text = stringResource(
                    id = label_res)
            )
        },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
        )
    )
}
