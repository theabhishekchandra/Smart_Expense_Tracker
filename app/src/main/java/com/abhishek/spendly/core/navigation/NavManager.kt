package com.abhishek.spendly.core.navigation

import androidx.navigation.NavController

// This Interface that defines navigation actions abstractly(fixed action allowed)
interface NavManager {
    fun setNavController(controller: NavController)

    // Basic Navigation
    fun navigate(route: String)
    fun navigateAndPopUpTo(route: String, popUpTo: String)
    fun navigateToRoot(route: String)
    fun navigateBack()

    // Optional NavOptions or Launch Modes
    fun navigateSingleTop(route: String)
    fun navigationForBottomBar(route: String)


    // Add You can pass data as Bundle or Map
    // fun navigateWithArgs(route: String, args: Bundle)
    fun navigateWithArgs(route: String, args: Map<String, String>)
    // For getting results from previous destination
    fun navigateForResult(route: String, resultKey: String)

    // Set a result for a previous screen to collect
    fun setResult(resultKey: String, result: Any)

    // Check current destination route
    fun getCurrentRoute(): String?

    // Navigate only if not already at the destination
    fun navigateIfNotCurrent(route: String)
}
