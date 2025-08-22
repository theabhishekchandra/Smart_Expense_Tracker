package com.abhishek.smartexpensetracker.ui.screens.login

import android.util.Patterns
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.abhishek.smartexpensetracker.R
import com.abhishek.smartexpensetracker.core.navigation.NavManager
import com.abhishek.smartexpensetracker.core.navigation.ScreenRoutes
import com.abhishek.smartexpensetracker.core.utils.Utils.Companion.isPhoneNumber
import com.abhishek.smartexpensetracker.ui.components.*
import com.abhishek.smartexpensetracker.ui.components.AppButton
import com.abhishek.smartexpensetracker.ui.screens.login.viewmodel.AuthViewModel
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
    val context = LocalContext.current

    LaunchedEffect(Unit/*viewModel?.errorMessage*/) {
        /*val message = viewModel?.errorMessage?.takeIf { it.isNotBlank() }
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }*/
    }

    Scaffold(
        topBar = {
            KagazMitraToolbar(
                title = "Login to your account",
                onBackClick = { navManager?.navigateBack() },
                onActionClick = null
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { innerPadding ->

        Surface(
            contentColor = MaterialTheme.colorScheme.onSurface,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
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
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Remember me")
                        }
                        Text(
                            text = "Forgot password?",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.labelSmall,
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
                            isError = otp.isNotEmpty() && otp.length < 6,
                            errorText = "Invalid OTP",
                            leadingIcon = { Icon(Icons.Default.Numbers, contentDescription = null) },
                            maxLength = 6,
                            filterOnlyDigits = true
                        )
                    }
                }

                AppButton(
                    text = "Login",
                    onClick = {
                        navManager?.navigateToRoot(ScreenRoutes.Home.route)
                        coroutineScope.launch {
                            if (isEmailLogin) {
                                if (!isEmailValid || !isPasswordValid) {
                                    snackBarHostState.showSnackbar("Please fix errors before logging in")
                                    return@launch
                                }
                                // TODO: viewModel?.submitEmailLogin(...)
                            } else {
                                /*if (!phoneNumber.isPhoneNumber() || otp.length < 6) {
                                    snackbarHostState.showSnackbar("Please enter valid phone and OTP")
                                    isOtpSent = false
                                    return@launch
                                }else{*/
//                                    viewModel?.loginWithPhoneNumber(
//                                        phone = phoneNumber,
//                                        activity = context as MainActivity
//                                    )
                                isOtpSent = true
                                // TODO: viewModel?.submitPhoneLogin(...)
//                                }
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
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
//    SmartExpenseTrackerTheme(darkTheme = false) {
//        LoginScreen(null, null)
//    }
}