package com.abhishek.smartexpensetracker.core.datastore

enum class ThemeType(val value: String) {
    LIGHT("light"),
    DARK("dark");

//    SYSTEM("system"); // optional for auto-follow system

    companion object {
        fun fromValue(value: String): ThemeType {
            return entries.find { it.value == value } ?: LIGHT
        }
    }
}

// BusinessMode.kt
sealed class BusinessMode {
    object Personal : BusinessMode()
    object Business : BusinessMode()
}
