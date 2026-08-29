package com.abhishek.spendly.ui.screens.login

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhishek.spendly.core.navigation.NavManager
import com.abhishek.spendly.core.navigation.ScreenRoutes
import com.abhishek.spendly.ui.components.AppButton
import com.abhishek.spendly.ui.components.LabeledTextField
import com.abhishek.spendly.ui.components.SwitchAuthText
import com.abhishek.spendly.ui.theme.AppSpacing
import com.abhishek.spendly.ui.theme.heroGradientVertical
import kotlinx.coroutines.launch

@Composable
fun ResetPasswordScreen(
    navManager: NavManager?
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }

    var isOTPCorrect by remember { mutableStateOf(false) }
    var otp by remember { mutableStateOf("") }
    var isOTPSend by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        heroGradientVertical(),
                        RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    )
                    .statusBarsPadding()
                    .padding(AppSpacing.lg)
            ) {
                Column {
                    IconButton(onClick = { navManager?.navigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.height(AppSpacing.sm))
                    Text(
                        text = "Forgot your password?",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Enter your email and we'll send you a code to reset it",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(AppSpacing.md),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(AppSpacing.md))

                // Email Field
                LabeledTextField(
                    label = "Email",
                    value = email,
                    onValueChange = {
                        email = it
                        isEmailValid = Patterns.EMAIL_ADDRESS.matcher(it).matches()
                    },
                    placeholder = "example12345@gmail.com",
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                    isError = email.isNotEmpty() && !isEmailValid,
                    errorText = "Invalid email address",
                    leadingIcon = { Icon(Icons.Default.Mail, contentDescription = null) }
                )

                Spacer(modifier = Modifier.height(AppSpacing.lg))
                // OTP
                if (!isOTPSend) {
                    LabeledTextField(
                        label = "OTP",
                        value = otp,
                        onValueChange = {
                            if (it.length <= 6 && it.all { c -> c.isDigit() }) {
                                otp = it
                                isOTPCorrect = it.length == 6
                            }
                        },
                        placeholder = "Enter your OTP",
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done,
                        isError = otp.isNotEmpty() && !isOTPCorrect,
                        errorText = "Invalid OTP",
                        leadingIcon = { Icon(Icons.Default.Numbers, contentDescription = null) }
                    )

                    Spacer(modifier = Modifier.height(AppSpacing.lg))

                    SwitchAuthText(
                        prompt = "Didn't get OTP? ",
                        actionText = "Resend",
                        onActionClick = {
                            // Navigate to registration screen
                        },
                        arrangement = Arrangement.Start
                    )
                }
                Spacer(modifier = Modifier.height(AppSpacing.lg))

                //App Button
                AppButton(
                    text = "Submit",
                    onClick = {
                        if (email.isBlank() || !isEmailValid || (!isOTPSend && !isOTPCorrect)) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Please enter a valid email and OTP")
                            }
                            return@AppButton
                        }
                        navManager?.navigate(ScreenRoutes.Home.route)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = true
                )

                Spacer(modifier = Modifier.height(AppSpacing.lg))

                SwitchAuthText(
                    prompt = "Remember password? ",
                    actionText = "Login",
                    onActionClick = {
                        navManager?.navigateToRoot(ScreenRoutes.Login.route)
                    }
                )
            }
        }

    }
}
@Preview(showBackground = true)
@Composable
fun ResetPasswordScreenPreview() {
//    SpendlyTheme {
//        ResetPasswordScreen(
//            null
//        )
//    }
}
