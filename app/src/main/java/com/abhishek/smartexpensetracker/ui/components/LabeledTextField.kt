package com.abhishek.smartexpensetracker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import com.abhishek.smartexpensetracker.ui.theme.AppSpacing

@Composable
fun LabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    isError: Boolean = false,
    errorText: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    maxLength: Int = Int.MAX_VALUE, // Add maxLength
    filterOnlyDigits: Boolean = false // Optional digit-only filter
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = AppSpacing.xs)
        )
        OutlinedTextField(
            value = value,
            onValueChange = {
                var newValue = it
                if (filterOnlyDigits) newValue = newValue.filter { char -> char.isDigit() }
                if (newValue.length <= maxLength) onValueChange(newValue)
            },
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            singleLine = singleLine,
            isError = isError,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = leadingIcon,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            shape = MaterialTheme.shapes.small
        )
        if (isError && errorText.isNotEmpty()) {
            Text(
                text = errorText,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = AppSpacing.xs)
            )
        }
    }
}