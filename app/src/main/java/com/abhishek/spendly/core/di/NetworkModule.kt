package com.abhishek.spendly.core.di

import com.abhishek.spendly.core.ktor.KtorClientFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideKtorClientFactory(): KtorClientFactory = KtorClientFactory()

    @Provides
    @Singleton
    fun provideHttpClient(factory: KtorClientFactory): HttpClient = factory.createHttpClient()

//    @Provides
//    @Singleton
//    fun provideLoginService(client: HttpClient): LoginService = LoginServiceImpl(client)
}
