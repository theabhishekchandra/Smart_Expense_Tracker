package com.abhishek.smartexpensetracker.ui.screens.login

import android.util.Patterns
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhishek.smartexpensetracker.R
import com.abhishek.smartexpensetracker.core.navigation.NavManager
import com.abhishek.smartexpensetracker.core.navigation.ScreenRoutes
import com.abhishek.smartexpensetracker.ui.components.AppButton
import com.abhishek.smartexpensetracker.ui.components.DividerWithText
import com.abhishek.smartexpensetracker.ui.components.LabeledTextField
import com.abhishek.smartexpensetracker.ui.components.SocialLoginButton
import com.abhishek.smartexpensetracker.ui.components.SwitchAuthText
import com.abhishek.smartexpensetracker.ui.theme.AppSpacing
import com.abhishek.smartexpensetracker.ui.theme.heroGradientVertical

@Composable
fun SignupScreen(
    navManager: NavManager?
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var mobile by remember { mutableStateOf("") }
    var isValid by remember { mutableStateOf(true) }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    val isPasswordValid = password.length >= 6
    val isConfirmPasswordValid = confirmPassword == password && confirmPassword.isNotEmpty()

    Column(modifier = Modifier.fillMaxSize()) {
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
                    text = "Create your account",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "It only takes a minute to get started",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = AppSpacing.md)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.lg)
        ) {
            Spacer(modifier = Modifier.height(AppSpacing.md))

            // Name Field
            LabeledTextField(
                label = "Name",
                value = name,
                onValueChange = { name = it },
                placeholder = "Enter your name",
                imeAction = ImeAction.Next,
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
            )

            // Mobile Field
            LabeledTextField(
                label = "Mobile Number",
                value = mobile,
                onValueChange = {
                    if (it.length <= 10 && it.all(Char::isDigit)) {
                        mobile = it
                        isValid = it.matches(Regex("^[6-9][0-9]{9}$"))
                    }
                },
                placeholder = "Enter your mobile number",
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next,
                isError = mobile.isNotEmpty() && !isValid,
                errorText = "Invalid mobile number",
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) }
            )

            // Email Field
            LabeledTextField(
                label = "Email",
                value = email,
                onValueChange = {
                    email = it
                    isEmailValid = Patterns.EMAIL_ADDRESS.matcher(it).matches()
                },
                placeholder = "example@gmail.com",
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                isError = email.isNotEmpty() && !isEmailValid,
                errorText = "Invalid email address",
                leadingIcon = { Icon(Icons.Default.Mail, contentDescription = null) }
            )

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

            AppButton(
                text = "Signup",
                onClick = {
                    val formValid = name.isNotBlank() && mobile.isNotBlank() && isValid &&
                        email.isNotBlank() && isEmailValid && isPasswordValid && isConfirmPasswordValid
                    if (formValid) {
                        Toast.makeText(context, "Signup successful", Toast.LENGTH_SHORT).show()
                        navManager?.navigateToRoot(ScreenRoutes.Home.route)
                    } else {
                        Toast.makeText(context, "Please fix the errors", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            DividerWithText()

            SocialLoginButton(
                label = "Continue with Google",
                iconPainter = painterResource(id = R.drawable.ic_google),
                onClick = { /* Handle Google Login */ }
            )

            SocialLoginButton(
                label = "Continue with Facebook",
                iconPainter = painterResource(id = R.drawable.ic_facebook),
                onClick = { /* Handle Facebook Login */ }
            )

            SwitchAuthText(
                prompt = "Already have an account? ",
                actionText = "Login",
                onActionClick = {
                    navManager?.navigateToRoot(ScreenRoutes.ExpenseList.route)
                }
            )

            Spacer(modifier = Modifier.height(AppSpacing.xxl + AppSpacing.xl))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignupScreenPreview() {
//    SmartExpenseTrackerTheme {
//        SignupScreen(navManager = null)
//    }
}
