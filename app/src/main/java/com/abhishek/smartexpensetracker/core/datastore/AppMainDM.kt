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

    companion object {
        fun fromValue(value: String?): PremiumType = PremiumType.entries.find { it.value == value } ?: BASIC
    }
}

sealed class PaymentMode {
    object Cash : PaymentMode()
    object Card : PaymentMode()
    object UPI : PaymentMode()
    object NetBanking : PaymentMode()
}

enum class Language(val value: String) {
    HINDI("Hindi"),
    ENGLISH("English");

    companion object {
        fun fromValue(value: String?): Language = Language.entries.find { it.value == value } ?: ENGLISH
    }
}

enum class Currency(val value: String, val symbol: String) {
    RUPEE("Rupee", "₹"),
    DOLLAR("Dollar", "$"),
    POUND("Pound", "£"),
    YEN("Yen", "¥"),
    RUBLE("Ruble", "₽"),
    BITCOIN("Bitcoin", "₿"),
    EURO("Euro","€");
    companion object {
        fun fromValue(value: String?): Currency = Currency.entries.find { it.value == value } ?: RUPEE
        fun fromSymbol(symbol: String?): Currency = Currency.entries.find { it.symbol == symbol } ?: RUPEE
        fun getSymbol(currency: Currency): String = currency.symbol
    }
}

enum class ExportFormat(val value: String) {
    PDF("PDF"),
    CSV("CSV"),
    EXCEL("Excel");

    companion object {
        fun fromValue(value: String?): ExportFormat = ExportFormat.entries.find { it.value == value } ?: PDF
    }
}

enum class SyncWith(val value: String) {
    GOOGLE_DRIVE("Google Drive"),
    APP_DRIVE("App Drive"),
    ONE_DRIVE("OneDrive");

    companion object {
        fun fromValue(value: String?): SyncWith = SyncWith.entries.find { it.value == value } ?: GOOGLE_DRIVE
    }
}

enum class SyncFrequency(val value: String) {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly");

    companion object {
        fun fromValue(value: String?): SyncFrequency = SyncFrequency.entries.find { it.value == value } ?: DAILY
    }
}

