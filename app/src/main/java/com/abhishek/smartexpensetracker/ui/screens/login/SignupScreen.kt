package com.abhishek.smartexpensetracker.ui.screens.login

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.abhishek.smartexpensetracker.ui.components.KagazMitraToolbar
import com.abhishek.smartexpensetracker.ui.components.LabeledTextField
import com.abhishek.smartexpensetracker.ui.components.SocialLoginButton
import com.abhishek.smartexpensetracker.ui.components.SwitchAuthText

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

    Scaffold(
        topBar = {
            KagazMitraToolbar(
                title = "Signup to your account",
                onBackClick = {
                    navManager?.navigateBack()
                },
                onActionClick = null
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 18.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Name Field
            LabeledTextField(
                label = "Name",
                value = name,
                onValueChange = { name = it },
                placeholder = "Enter your name",
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
                leadingIcon = { Icon(Icons.Default.Password, contentDescription = null) }
            )

            AppButton(
                text = "Signup",
                onClick = {
                    // Add signup logic or form validation
                    if (true) {
//                    if (isValid && isEmailValid && isPasswordValid && isConfirmPasswordValid) {
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

            Spacer(modifier = Modifier.height(80.dp))
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
