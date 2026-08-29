package com.abhishek.spendly.core.ktor

import android.content.Context.MODE_PRIVATE
//import com.abhishek.spendly.BuildConfig
//import com.abhishek.spendly.MainApp
import com.abhishek.spendly.core.sharepref.SharedIPreferenceStorage
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.headers
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KtorClientFactory @Inject constructor() {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        prettyPrint = true
    }

//    private fun prepareAppMainHeaders(): Map<String, String> = mutableMapOf<String, String>().apply {
//        put("Source", "android")
//        put("AndroidAppVersion", BuildConfig.VERSION_NAME)
//        put("AndroidAppFlavour", BuildConfig.FLAVOR)
//        put("AndroidAppBuildType", BuildConfig.BUILD_TYPE)
//        put("LoggedInUserUid", "uid") // Replace with actual logic if needed
//        MainApp.getContext()?.let {
//            val token = SharedIPreferenceStorage(
//                it.getSharedPreferences("kagaz_prefs", MODE_PRIVATE)
//            ).authToken
//            if (!token.isNullOrBlank()) {
//                put("token", token)
//            }
//        }
//    }

    fun createHttpClient(): HttpClient {
        return HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(json)
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        android.util.Log.d("KtorLogger", message)
                    }
                }
                level = LogLevel.BODY
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 60_000
                connectTimeoutMillis = 60_000
                socketTimeoutMillis = 60_000
            }

            defaultRequest {
//                url(BuildConfig.BASE_URL)
//
//                headers {
//                    prepareAppMainHeaders().forEach { (key, value) ->
//                        append(key, value)
//                    }
//                    contentType(ContentType.Application.Json)
//                    accept(ContentType.Application.Json)
//                }
            }

            HttpResponseValidator {
                handleResponseExceptionWithRequest { exception, _ ->
                    when (exception) {
                        is ServerResponseException -> {
                            if (exception.response.status.value in 500..599) {
                                throw Exception("Internal server error. Please try again.")
                            }
                        }
                        is ClientRequestException -> {
                            if (exception.response.status.value == 401) {
                                throw Exception("Unauthorized request.")
                            }
                        }
                        is UnknownHostException -> {
                            throw Exception("No internet connection.")
                        }
                        else -> throw exception
                    }
                }
            }
        }
    }
}
