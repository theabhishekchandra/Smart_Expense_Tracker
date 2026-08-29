package com.abhishek.spendly.core.navigation


import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.navOptions
import javax.inject.Inject
import javax.inject.Singleton

// Implementation of NavManager that actually uses the NavController
@Singleton
class DefaultNavManager @Inject constructor() : NavManager {

    private var navController: NavController? = null

    override fun setNavController(controller: NavController) {
        this.navController = controller
    }

    override fun navigate(route: String) {
        try {
            navController?.navigate(route)
        } catch (e: Exception) {
            Log.e("NavManager", "Navigation failed for route: $route", e)
            Toast.makeText(navController?.context, "Navigation failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun navigateAndPopUpTo(route: String, popUpTo: String) {
        try {
            navController?.navigate(route) {
                popUpTo(popUpTo) { inclusive = true }
                launchSingleTop = true
            }
        } catch (e: Exception) {
            Log.e("NavManager", "Navigation failed for route: $route", e)
            Toast.makeText(navController?.context, "Navigation failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun navigateToRoot(route: String) {
        try {
            navController?.navigate(route) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        } catch (e: Exception) {
            Log.e("NavManager", "Navigation failed for route: $route", e)
            Toast.makeText(navController?.context, "Navigation failed", Toast.LENGTH_SHORT).show()
        }
    }
    override fun navigateBack() {
        try {
            val controller = navController ?: return

            if (controller.previousBackStackEntry != null) {
                controller.popBackStack()
            } else {
                Log.d("NavManager", "No backstack to pop")

                val context = controller.context
                val intent = Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_HOME)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)

//                Toast.makeText(context, "Exiting app", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            Log.e("NavManager", "Navigation failed", e)
            Toast.makeText(navController?.context, "Navigation failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun navigateSingleTop(route: String) {
        try {
            navController?.navigate(route, navOptions {
                launchSingleTop = true
            })
        } catch (e: Exception) {
            Log.e("NavManager", "Navigation failed for route: $route", e)
            Toast.makeText(navController?.context, "Navigation failed", Toast.LENGTH_SHORT).show()
        }
    }
    override fun navigationForBottomBar(route: String){
        try {
            val currentRoute = navController?.currentBackStackEntry?.destination?.route

            if (currentRoute == route) {
                // Already on this screen, do nothing ✅ avoids white flicker
                return
            }
            if (currentRoute == ScreenRoutes.Home.route) {
                // Normal navigation from Home
                navController?.navigate(route)
            } else {
                // From non-Home → clear stack back to Home, then go to destination
                navController?.navigate(route) {
                    popUpTo(ScreenRoutes.Home.route) {
                        inclusive = false
                    }
                    launchSingleTop = true
                }
            }

        } catch (e: Exception) {
            Log.e("NavManager", "Navigation failed for route: $route", e)
            Toast.makeText(navController?.context, "Navigation failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun navigateWithArgs(route: String, args: Map<String, String>) {
        try {
            val query = args.entries.joinToString("&") { "${it.key}=${it.value}" }
            val fullRoute = if (query.isNotEmpty()) "$route?$query" else route
            navController?.navigate(fullRoute)
        } catch (e: Exception) {
            Log.e("NavManager", "Navigation failed for route: $route", e)
            Toast.makeText(navController?.context, "Navigation failed", Toast.LENGTH_SHORT).show()
        }
    }



    override fun navigateForResult(route: String, resultKey: String) {
        try {// Example approach, typically handled via SavedStateHandle in ViewModel
            navController?.currentBackStackEntry?.savedStateHandle?.remove<Any>(resultKey)
            navController?.navigate(route)
        } catch (e: Exception) {
            Log.e("NavManager", "Navigation failed for route: $route", e)
            Toast.makeText(navController?.context, "Navigation failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun setResult(resultKey: String, result: Any) {
        try {
            navController?.previousBackStackEntry
                ?.savedStateHandle
                ?.set(resultKey, result)
        } catch (e: Exception) {
            Log.e("NavManager", "Navigation failed", e)
            Toast.makeText(navController?.context, "Navigation failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getCurrentRoute(): String? {
        return try {
            navController?.currentDestination?.route
        } catch (e: Exception) {
            Toast.makeText(navController?.context, "Navigation failed", Toast.LENGTH_SHORT).show()
            Log.e("NavManager", "Navigation failed ", e)
        } as String?
    }

    override fun navigateIfNotCurrent(route: String) {
        try {
            if (getCurrentRoute() != route) {
                navigate(route)
            }
        } catch (e: Exception) {
            Log.e("NavManager", "Navigation failed for route: $route", e)
            Toast.makeText(navController?.context, "Navigation failed", Toast.LENGTH_SHORT).show()
        }
    }
}
