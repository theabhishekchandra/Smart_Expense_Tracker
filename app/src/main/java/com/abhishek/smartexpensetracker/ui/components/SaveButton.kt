package com.abhishek.smartexpensetracker.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale

@Composable
fun SaveButton(showAnim: Boolean, onClick: () -> Unit) {
    val scale by animateFloatAsState(if (showAnim) 1.2f else 1f, label = "scaleAnim")
    FloatingActionButton(onClick = onClick, modifier = Modifier.scale(scale)) { Text("Save") }
}