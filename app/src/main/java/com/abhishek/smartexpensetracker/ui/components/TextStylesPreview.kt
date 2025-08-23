package com.abhishek.smartexpensetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhishek.smartexpensetracker.ui.theme.SmartExpenseTrackerTheme


@Composable
fun TextStylesPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
//        PreviewText("Header24Center", AppTextStyles.Header24Center)
//        PreviewText("Body12Center", AppTextStyles.Body12Center)
//        PreviewText("Body16Right", AppTextStyles.Body16Right)
//        PreviewText("Body16Center", AppTextStyles.Body16Center)
//        PreviewText("ToolbarTitle", AppTextStyles.ToolbarTitle)
//        PreviewText("Label12", AppTextStyles.Label12)
//        PreviewText("Caption8", AppTextStyles.Caption8)
//        PreviewText("Caption8Line14", AppTextStyles.Caption8Line14)
//        PreviewText("Small12Line26", AppTextStyles.Small12Line26)
//        PreviewText("Body16Line26", AppTextStyles.Body16Line26)
//        PreviewText("Caption8Line16", AppTextStyles.Caption8Line16)
//        PreviewText("Title20Medium", AppTextStyles.Title20Medium)
    }
}

@Composable
fun PreviewText(label: String, style: androidx.compose.ui.text.TextStyle) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(12.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEFEFEF))
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Sample Text",
                style = style,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextStylesPreview_Light() {

    SmartExpenseTrackerTheme(
        true,
        false
    ){
        TextStylesPreview()
    }
}

@Preview(showBackground = true)
@Composable
fun TextStylesPreview_Dark() {
    SmartExpenseTrackerTheme(
        true,
        false
    ){
        TextStylesPreview()
    }
}
