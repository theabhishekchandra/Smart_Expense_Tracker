package com.abhishek.spendly.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhishek.spendly.R
import com.abhishek.spendly.ui.theme.AppSpacing
import com.abhishek.spendly.ui.theme.SpendlyTheme


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
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = contentColor
        ),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline),
        shape = MaterialTheme.shapes.small

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
            Spacer(modifier = Modifier.width(AppSpacing.sm))

            Text(text = label, color = contentColor, style = MaterialTheme.typography.labelLarge)
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun SocialLoginButtonPreview() {
    SpendlyTheme(
        true,

    ){
        SocialLoginButton(
            label = "Login with Google",
            onClick = {},
            iconPainter = painterResource(id = R.drawable.ic_google),

            )
    }
}