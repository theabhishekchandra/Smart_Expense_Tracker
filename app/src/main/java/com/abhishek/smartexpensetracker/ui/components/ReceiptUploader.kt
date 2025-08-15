package com.abhishek.smartexpensetracker.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun ReceiptUploader(uri: String?, onUpload: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Button(onClick = onUpload) { Text("Upload Receipt") }
        Spacer(modifier = Modifier.width(8.dp))
        AnimatedVisibility(visible = uri != null) {
            Image(painter = rememberAsyncImagePainter(uri), contentDescription = "receipt", modifier = Modifier.size(64.dp))
        }
    }
}