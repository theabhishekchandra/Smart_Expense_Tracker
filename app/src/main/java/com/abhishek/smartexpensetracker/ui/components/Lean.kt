package com.abhishek.smartexpensetracker.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// Use Modifier.composed for Custom Reusable Modifiers
fun Modifier.paddingIf(condition: Boolean, padding: Dp) = composed {
    if (condition) padding(padding) else this
}


/** Safe Form Binding with Lambda Delegation
 * onValueChange = { viewModel.username = it }  Bad Code.
 *
 * val onUsernameChange = viewModel::onUsernameChanged
 * TextField(value = ..., onValueChange = onUsernameChange) Good Code.
 * */

// Use ViewModelFactory for Parameterized ViewModels
class MyViewModel(private val userId: String) : ViewModel()

@Suppress("UNCHECKED_CAST")
class MyFactory(private val userId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        MyViewModel(userId) as T
}

// Create a Custom Scaffold or BaseScreen
@Composable
fun BaseScreen(
    isLoading: Boolean,
    snackbarHostState: SnackbarHostState,
    content: @Composable () -> Unit
) {
    Scaffold(

        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        Box(modifier = Modifier.padding(it)) {
            if (isLoading) CircularProgressIndicator()
            content()
        }
//        HomeScreen(
//            navManager = null
//        )
    }
}

@Preview
@Composable
private fun BasicScreenPreview() {
    BaseScreen(isLoading = true, snackbarHostState = remember { SnackbarHostState() }) {
        // Your content
    }
}

// Build Your Own Design System with Material 3
object AppTheme {
    val spacing = PaddingValues(16.dp)
    val radius = 12.dp
    val cardElevation = 4.dp
}
@Composable
fun Test (){
    Card(
        shape = RoundedCornerShape(AppTheme.radius),
        elevation = CardDefaults.cardElevation(AppTheme.cardElevation)
    ) {
        // Your content
    }
    /// Use Modifier.clickable with indication = null for Invisible Taps
    Modifier.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
//        onClick()

    }
}

// Composable Extension Functions for Cleaner Modifiers
fun Modifier.roundedBackground(): Modifier = this
    .background(Color.Gray, RoundedCornerShape(12.dp))
    .padding(8.dp)
// Use like this Text("Abhishek", Modifier.roundedBackground())

/** Use Crossfade for Switching Screens or States Smoothly
Crossfade(targetState = currentScreen) { screen ->
    when (screen) {
        Screen.Home -> HomeUI()
        Screen.Profile -> ProfileUI()
    }
}*/

/** Debounce User Input (Search Bar / OTP / Form Fields)*/


/** Composable Function Contracts for Stability
Use @Stable, @Immutable, @ComposableContract (upcoming) to prevent unnecessary recompositions in deeply nested Composables.
 */

@Immutable
data class UserDataTest(val name: String, val age: Int)



fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier =
    composed {
        clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) { onClick() }
    }