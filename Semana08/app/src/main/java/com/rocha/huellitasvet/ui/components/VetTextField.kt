package com.rocha.huellitasvet.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun VetTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    esClave: Boolean = false,
    enabled: Boolean = true,
    error: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    leadingIcon: (@Composable () -> Unit)? = null
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = leadingIcon,
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp),
            enabled = enabled,
            singleLine = true,
            isError = error != null,
            visualTransformation = if (esClave) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (esClave) KeyboardType.Password else keyboardType
            ),
            shape = RoundedCornerShape(12.dp)
        )
        if (error != null) {
            Spacer(Modifier.height(2.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}