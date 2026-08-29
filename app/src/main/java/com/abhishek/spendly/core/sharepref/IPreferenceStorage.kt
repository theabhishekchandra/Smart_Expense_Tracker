package com.abhishek.spendly.core.sharepref

interface IPreferenceStorage {
    var onBoardingCompleted : Boolean
    var isUserLoggedIn: Boolean
    var authToken: String?
    var userId: String?
    fun clearAll()
}

