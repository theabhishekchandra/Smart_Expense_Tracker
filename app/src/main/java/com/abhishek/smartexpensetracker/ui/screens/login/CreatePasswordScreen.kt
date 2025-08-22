package com.abhishek.smartexpensetracker.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.abhishek.smartexpensetracker.core.navigation.NavManager
import com.abhishek.smartexpensetracker.core.navigation.ScreenRoutes
import com.abhishek.smartexpensetracker.ui.components.AppButton
import com.abhishek.smartexpensetracker.ui.components.KagazMitraToolbar
import com.abhishek.smartexpensetracker.ui.components.LabeledTextField
import kotlinx.coroutines.launch

@Composable
fun CreatePasswordScreen(
    navManager: NavManager?
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    val isPasswordValid = password.length >= 6
    val isConfirmPasswordValid = confirmPassword == password && confirmPassword.isNotEmpty()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            KagazMitraToolbar(
                title = "Create New Password",
                onBackClick = {
                    navManager?.navigateBack()
                },
                onActionClick = null
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
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                // Password Field
                LabeledTextField(
                    label = "Password",
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Enter your password",
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle password"
                            )
                        }
                    },
                    isError = password.isNotEmpty() && !isPasswordValid,
                    errorText = "Password must be at least 6 characters",
                    leadingIcon = { Icon(Icons.Default.Password, contentDescription = null)}
                        )

                Spacer(modifier = Modifier.height(20.dp))

                // Confirm Password Field
                LabeledTextField(
                    label = "Confirm Password",
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = "Re-enter your password",
                    visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                            Icon(
                                imageVector = if (showConfirmPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle password"
                            )
                        }
                    },
                    isError = confirmPassword.isNotEmpty() && !isConfirmPasswordValid,
                    errorText = "Passwords do not match",
                    leadingIcon = { Icon(Icons.Default.Password, contentDescription = null)}
                )

                Spacer(modifier = Modifier.height(24.dp))

                AppButton(
                    text = "Create Password",
                    onClick = {
                        if (!isPasswordValid || !isConfirmPasswordValid) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Please fix the errors before proceeding")
                            }
                            return@AppButton
                        }
                        navManager?.navigateToRoot(ScreenRoutes.Login.route)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

//@Preview
//@Composable
//private fun CreatePasswordScreenPreview() {
//    SmartExpenseTrackerTheme {
//        CreatePasswordScreen(navManager = null)
//    }
//
//}

