package com.abhishek.smartexpensetracker.ui.screens.login

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhishek.smartexpensetracker.core.navigation.NavManager
import com.abhishek.smartexpensetracker.core.navigation.ScreenRoutes
import com.abhishek.smartexpensetracker.ui.components.AppButton
import com.abhishek.smartexpensetracker.ui.components.FinanceTopBar
import com.abhishek.smartexpensetracker.ui.components.LabeledTextField
import com.abhishek.smartexpensetracker.ui.components.SwitchAuthText

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
    var isValid by remember { mutableStateOf(true) }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            FinanceTopBar(
                title = "Reset Password",
                onBackClick = {
                    navManager?.navigateBack()
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                    isError = email.isNotEmpty() && !isEmailValid,
                    errorText = "Invalid email address",
                    leadingIcon = { Icon(Icons.Default.Mail, contentDescription = null)}
                )

                Spacer(modifier = Modifier.height(20.dp))
                // OTP
                if (isOTPSend == false) {
                    LabeledTextField(
                        label = "OTP",
                        value = otp,
                        onValueChange = {
                            if (it.length <= 6 && it.all { c -> c.isDigit() }) {
                                otp = it
                            }
                        },
                        placeholder = "Enter your OTP",
                        keyboardType = KeyboardType.Phone,
                        isError = otp.isNotEmpty() && !isOTPCorrect,
                        errorText = "Invalid OTP",
                        leadingIcon = { Icon(Icons.Default.Numbers, contentDescription = null)}
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    SwitchAuthText(
                        prompt = "Didn’t get OTP? ",
                        actionText = "Resend",
                        onActionClick = {
                            // 👉 Navigate to registration screen
                        },
                        arrangement = Arrangement.Start
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))

                //App Button
                AppButton(
                    text = "Submit",
                    onClick = {
                        navManager?.navigate(ScreenRoutes.Home.route)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = true
                )

                Spacer(modifier = Modifier.height(20.dp))

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
//    SmartExpenseTrackerTheme {
//        ResetPasswordScreen(
//            null
//        )
//    }
}
