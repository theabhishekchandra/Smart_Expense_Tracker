package com.abhishek.smartexpensetracker.ui.screens.login

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
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.abhishek.smartexpensetracker.core.navigation.NavManager
import com.abhishek.smartexpensetracker.core.navigation.ScreenRoutes
import com.abhishek.smartexpensetracker.ui.components.AppButton
import com.abhishek.smartexpensetracker.ui.components.LabeledTextField
import com.abhishek.smartexpensetracker.ui.theme.AppSpacing
import com.abhishek.smartexpensetracker.ui.theme.heroGradientVertical
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
                        text = "Set a new password",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Choose a strong password you haven't used before",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(AppSpacing.md),
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(AppSpacing.md))

                // Password Field
                LabeledTextField(
                    label = "Password",
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Enter your password",
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (showPassword) "Hide password" else "Show password"
                            )
                        }
                    },
                    isError = password.isNotEmpty() && !isPasswordValid,
                    errorText = "Password must be at least 6 characters",
                    leadingIcon = { Icon(Icons.Default.Password, contentDescription = null) }
                )

                Spacer(modifier = Modifier.height(AppSpacing.lg))

                // Confirm Password Field
                LabeledTextField(
                    label = "Confirm Password",
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = "Re-enter your password",
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                            Icon(
                                imageVector = if (showConfirmPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (showConfirmPassword) "Hide password" else "Show password"
                            )
                        }
                    },
                    isError = confirmPassword.isNotEmpty() && !isConfirmPasswordValid,
                    errorText = "Passwords do not match",
                    leadingIcon = { Icon(Icons.Default.Password, contentDescription = null) }
                )

                Spacer(modifier = Modifier.height(AppSpacing.lg))

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
