package com.abhishek.smartexpensetracker.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.abhishek.smartexpensetracker.core.voice.VoiceManager
import com.abhishek.smartexpensetracker.ui.theme.AppSpacing
import kotlinx.coroutines.delay

@Composable
fun VoiceOverlayHost() {
    val status by VoiceManager.status.collectAsState(initial = VoiceManager.Status.Idle)
    val transcript by VoiceManager.recognizedText.collectAsState(initial = "")
    val context = LocalContext.current

    // Check if already granted
    val permissionGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.RECORD_AUDIO
    ) == PackageManager.PERMISSION_GRANTED

    // Launcher to request permission
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(
                context,
                "Microphone permission granted",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                context,
                "Microphone permission denied",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    LaunchedEffect(Unit) {
        if (!permissionGranted) {
            launcher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    Log.d("VoiceOverlayHost", "${status} : ${transcript} -> ${transcript.length}")
    AnimatedVisibility(
        visible = status != VoiceManager.Status.Idle || transcript.isNotBlank(),
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = Modifier.fillMaxSize()
    ) {
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .noRippleClickable { VoiceManager.stopListening() }
            )
            Column(
                modifier = Modifier
//                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .shadow(20.dp, MaterialTheme.shapes.extraLarge)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                            )
                        ),
                        shape = MaterialTheme.shapes.extraLarge
                    )
                    .padding(AppSpacing.lg)
            ) {
                // Title Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedTextStatus(status)
                    TextButton(onClick = { VoiceManager.stopListening() }) {
                        Text("Close", style = MaterialTheme.typography.labelLarge)
                    }
                }

                Spacer(Modifier.height(AppSpacing.md))

                // Mic Animation
                if (status == VoiceManager.Status.Listening) {
                    PulsingMic()
                    Spacer(Modifier.height(AppSpacing.md))
                }

                // Transcript
                AnimatedVisibility(
                    visible = transcript.isNotBlank(),
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Text(
                        text = transcript,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(0.2f)
                    .background(Color.Transparent)
                    .noRippleClickable { VoiceManager.stopListening() }
            )
        }
    }
}

@Composable
fun AnimatedTextStatus(status: VoiceManager.Status) {
    val baseText = when (status) {
        VoiceManager.Status.Speak -> "Speak please"
        VoiceManager.Status.Listening -> "Listening"
        VoiceManager.Status.Processing -> "Processing"
        VoiceManager.Status.Error -> "Error"
        else -> "Voice"
    }

    var dotCount by remember { mutableIntStateOf(0) }
    LaunchedEffect(baseText) {
        while (true) {
            delay(500)
            dotCount = (dotCount + 1) % 4
        }
    }

    Text(
        text = baseText + ".".repeat(dotCount),
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
fun PulsingMic() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAnim"
    )

    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.error.copy(alpha = 0.85f))
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = "Listening, tap Close to stop",
            tint = MaterialTheme.colorScheme.onError
        )
    }
}

@Preview
@Composable
private fun PreviewVoiceOverlayHost() {
    VoiceOverlayHost()
}
