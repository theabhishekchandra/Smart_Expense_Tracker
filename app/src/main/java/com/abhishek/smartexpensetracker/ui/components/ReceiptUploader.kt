package com.abhishek.smartexpensetracker.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close

@Composable
fun ReceiptUploader(
    uri: String?,
    onUpload: () -> Unit,
    onRemove: () -> Unit // Added remove callback
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Button(onClick = onUpload) { Text("Upload Receipt") }
        Spacer(modifier = Modifier.width(8.dp))
        AnimatedVisibility(visible = uri != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "receipt",
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(onClick = onRemove) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Remove Receipt")
                }
            }
        }
    }
}
