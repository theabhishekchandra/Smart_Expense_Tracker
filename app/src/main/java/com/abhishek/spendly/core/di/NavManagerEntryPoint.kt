package com.abhishek.spendly.core.di

import com.abhishek.spendly.core.navigation.NavManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

//Entry point interface to access NavManager inside Composables
@EntryPoint
@InstallIn(SingletonComponent::class)
interface NavManagerEntryPoint {
    fun navManager(): NavManager
}