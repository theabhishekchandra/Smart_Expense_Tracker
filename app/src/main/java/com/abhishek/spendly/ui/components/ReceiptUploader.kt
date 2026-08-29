package com.abhishek.spendly.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import com.abhishek.spendly.ui.theme.AppSpacing

@Composable
fun ReceiptUploader(
    uri: String?,
    onUpload: () -> Unit,
    onRemove: () -> Unit // Added remove callback
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Button(
            onClick = onUpload,
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Text("Upload Receipt", style = MaterialTheme.typography.labelLarge)
        }
        Spacer(modifier = Modifier.width(AppSpacing.sm))
        AnimatedVisibility(visible = uri != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Uploaded receipt preview",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(MaterialTheme.shapes.small)
                )
                Spacer(modifier = Modifier.width(AppSpacing.xs))
                IconButton(onClick = onRemove) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Remove Receipt")
                }
            }
        }
    }
}
