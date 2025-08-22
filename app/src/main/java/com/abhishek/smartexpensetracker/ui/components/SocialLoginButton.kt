package com.abhishek.smartexpensetracker.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhishek.smartexpensetracker.R
import com.abhishek.smartexpensetracker.ui.theme.SmartExpenseTrackerTheme


@Composable
fun SocialLoginButton(
    label: String,
    onClick: () -> Unit,
    iconPainter: Painter? = null,
    iconVector: ImageVector? = null,
) {
    val contentColor = MaterialTheme.colorScheme.onSurface // adapts to dark/light mode

    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = contentColor
        )

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            if (iconPainter != null) {
                Image(
                    painter = iconPainter,
                    contentDescription = "$label icon",
                    modifier = Modifier.size(20.dp)
                )
            } else if (iconVector != null) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = "$label icon",
//                tint = contentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))

            Text(text = label, color = contentColor)
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun SocialLoginButtonPreview() {
    SmartExpenseTrackerTheme(darkTheme = true) {
        SocialLoginButton(
            label = "Login with Google",
            onClick = {},
            iconPainter = painterResource(id = R.drawable.ic_google),

            )
    }
}