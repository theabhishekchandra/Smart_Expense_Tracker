package com.abhishek.smartexpensetracker.ui.screens.login

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhishek.smartexpensetracker.R
import com.abhishek.smartexpensetracker.core.navigation.NavManager
import com.abhishek.smartexpensetracker.core.navigation.ScreenRoutes
import com.abhishek.smartexpensetracker.core.utils.Utils.Companion.isPhoneNumber
import com.abhishek.smartexpensetracker.ui.components.*
import com.abhishek.smartexpensetracker.ui.screens.login.viewmodel.AuthViewModel
import com.abhishek.smartexpensetracker.ui.theme.AppSpacing
import com.abhishek.smartexpensetracker.ui.theme.heroGradientVertical
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navManager: NavManager?,
    viewModel: AuthViewModel?  = hiltViewModel()
) {
    var isEmailLogin by remember { mutableStateOf(true) }

    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var otp by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var isEmailValid by remember { mutableStateOf(true) }
    var isPasswordValid by remember { mutableStateOf(true) }
    var isRememberMeChecked by remember { mutableStateOf(false) }
    var isOtpSent by remember { mutableStateOf(false) }

    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Gradient hero header - replaces the generic FinanceTopBar, which is
            // meant for main-app screens (it defaults to showing search/filter/
            // notification/menu icons that make no sense pre-login).
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
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(AppSpacing.sm))
                    Text(
                        text = "Welcome back",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Login to continue managing your expenses",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(AppSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
            ) {
                if (isEmailLogin) {
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

                    LabeledTextField(
                        label = "Password",
                        value = password,
                        onValueChange = {
                            password = it
                            isPasswordValid = it.length >= 6
                        },
                        placeholder = "Enter your password",
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.clickable {
                                isRememberMeChecked = !isRememberMeChecked
                            },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isRememberMeChecked,
                                onCheckedChange = null
                            )
                            Spacer(modifier = Modifier.width(AppSpacing.sm))
                            Text(text = "Remember me", style = MaterialTheme.typography.bodyMedium)
                        }
                        Text(
                            text = "Forgot password?",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.clickable {
                                navManager?.navigate(ScreenRoutes.ResetPassword.route)
                            }
                        )
                    }
                } else {
                    LabeledTextField(
                        label = "Phone Number",
                        value = phoneNumber,
                        onValueChange = {
                            val filtered = it.filter { c -> c.isDigit() }
                            if (filtered.length <= 10) phoneNumber = filtered
                        },
                        placeholder = "Enter your phone number",
                        keyboardType = KeyboardType.Phone,
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                        singleLine = true,
                        isError = phoneNumber.isNotEmpty() && !phoneNumber.isPhoneNumber(),
                        errorText = "Invalid phone number",
                        maxLength = 10,
                        filterOnlyDigits = true
                    )
                    if (isOtpSent) {
                        LabeledTextField(
                            label = "OTP",
                            value = otp,
                            onValueChange = {
                                val filtered = it.filter { c -> c.isDigit() }
                                if (filtered.length <= 6) otp = filtered
                            },
                            placeholder = "######",
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done,
                            isError = otp.isNotEmpty() && otp.length < 6,
                            errorText = "Invalid OTP",
                            leadingIcon = { Icon(Icons.Default.Numbers, contentDescription = null) },
                            maxLength = 6,
                            filterOnlyDigits = true
                        )
                    }
                }

                AppButton(
                    text = if (!isEmailLogin && !isOtpSent) "Send OTP" else "Login",
                    onClick = {
                        coroutineScope.launch {
                            if (isEmailLogin) {
                                if (email.isBlank() || password.isBlank() || !isEmailValid || !isPasswordValid) {
                                    snackBarHostState.showSnackbar("Please fix errors before logging in")
                                    return@launch
                                }
                                // TODO: viewModel?.submitEmailLogin(...)
                                navManager?.navigateToRoot(ScreenRoutes.Home.route)
                            } else if (!isOtpSent) {
                                isOtpSent = true
                            } else {
                                if (otp.length < 6) {
                                    snackBarHostState.showSnackbar("Enter the 6-digit OTP")
                                    return@launch
                                }
                                // TODO: viewModel?.submitPhoneLogin(...)
                                navManager?.navigateToRoot(ScreenRoutes.Home.route)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                DividerWithText()

                SocialLoginButton(
                    label = if (!isEmailLogin) "Login with Email" else "Continue with Phone Number",
                    onClick = {
                        isEmailLogin = !isEmailLogin
                    },
                    iconVector = if ( !isEmailLogin ) Icons.Default.Email else Icons.Default.Phone,
                )

                SocialLoginButton(
                    label = "Continue with Google",
                    onClick = { /* TODO */ },
                    iconPainter = painterResource(id = R.drawable.ic_google)
                )

                SocialLoginButton(
                    label = "Continue with Facebook",
                    iconPainter = painterResource(id = R.drawable.ic_facebook),
                    onClick = { /* TODO */ }
                )

                SwitchAuthText(
                    prompt = "Don't have an account? ",
                    actionText = "Register",
                    onActionClick = {
                        navManager?.navigate(ScreenRoutes.SignUp.route)
                    }
                )
            }
        }

        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
//    SmartExpenseTrackerTheme(darkTheme = false) {
//        LoginScreen(null, null)
//    }
}
