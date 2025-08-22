package com.abhishek.smartexpensetracker.core.sharepref

interface IPreferenceStorage {
    var onBoardingCompleted : Boolean
    var isUserLoggedIn: Boolean
    var authToken: String?
    var userId: String?
    fun clearAll()
}

