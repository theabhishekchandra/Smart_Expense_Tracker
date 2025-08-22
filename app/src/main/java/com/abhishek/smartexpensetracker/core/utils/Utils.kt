package com.abhishek.smartexpensetracker.core.utils

class Utils {

    companion object {

        // Extension function on String to check if it's a valid 10-digit phone number
        fun String.isPhoneNumber(): Boolean {
            if (this.isBlank()) return false
            val phoneNumberRegex = Regex("^[6-9][0-9]{9}$")
            return phoneNumberRegex.matches(this)
        }

        // Extension function on String to check if it's a valid Email.
        fun String.isValidEmail(): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
        }

        // Function to validate and format a numeric string
        fun isValidatedForNumber(inputText: String): String {
            val input = inputText.trim()

            if (input.isBlank()) return "0"

            val sanitizedInput = if (input.startsWith("0") && !input.startsWith("0.") && input.length > 1) {
                input.replaceFirst("^0+(?!$)".toRegex(), "")
            } else {
                input
            }

            return try {
                val doubleValue = sanitizedInput.toDouble()
                if (sanitizedInput.contains(".") && sanitizedInput.length > sanitizedInput.indexOf(".") + 2) {
                    "%.2f".format(doubleValue)
                } else {
                    sanitizedInput
                }
            } catch (e: NumberFormatException) {
                "0"
            }
        }
    }
}
