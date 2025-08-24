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

// BusinessMode
sealed class BusinessMode {
    object Personal : BusinessMode()
    object Business : BusinessMode()
}

// PremiumType

enum class PremiumType(val value: String) {
    BASIC("basic"),
    MONTHLY("monthly"),
    YEARLY("yearly");
}

sealed class PaymentMode {
    object Cash : PaymentMode()
    object Card : PaymentMode()
    object UPI : PaymentMode()
    object NetBanking : PaymentMode()
}
