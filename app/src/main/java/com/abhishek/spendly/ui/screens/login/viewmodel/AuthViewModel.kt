package com.abhishek.spendly.ui.screens.login.viewmodel

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.spendly.core.sharepref.IPreferenceStorage
import com.abhishek.spendly.data.repository.IExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: IExpenseRepository
) : ViewModel() {

    fun get(){
        viewModelScope.launch {
            repo.getAll()
        }
    }
    /*{
    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    private var phone by mutableStateOf("")
    private var deviceId by mutableStateOf("")
    private var fcmToken by mutableStateOf("")

    // Firebase Auth
    private val TAG = "AuthViewModel"
    private var storedVerificationId by mutableStateOf<String?>(null)
    private var auth by mutableStateOf<FirebaseAuth?>(null)
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onErrorInCall("Exception handled: ${throwable.localizedMessage}")
    }
    private fun onErrorInCall(message: String) {
        errorMessage = message
    }

    init {
        auth = FirebaseAuth.getInstance()
        auth?.useAppLanguage()
    }

    // Step 1-> Enter Phone Number Send Otp Generate request.
    fun sendVerificationCodeToPhoneNumber(phoneNumber: String, activity: Activity) {
        val options = PhoneAuthOptions.newBuilder(auth!!)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    // Step 2 -> Generate Otp Request Verification Request and Verification ID.
    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(TAG, "onVerificationCompleted:$credential")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                // reCAPTCHA verification attempted with null Activity
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            resendToken = token
        }

        override fun onCodeAutoRetrievalTimeOut(p0: String) {
            super.onCodeAutoRetrievalTimeOut(p0)
            Log.d(TAG, "onCodeAutoRetrievalTimeOut:$p0")
        }
    }

    // Step 3-> Enter Otp For Verification
    fun verifyPhoneNumberCode(code : String) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    // Step 4 -> Monitor Response for Login.
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth!!.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }

    fun submitLogin(phone: String, deviceId: String, fcmToken: String){
        this.phone = phone
        this.deviceId = deviceId
        this.fcmToken = fcmToken
    }
    fun loginWithPhoneNumber(phone : String, activity: Activity){
        val number = "+91$phone"
        val options = PhoneAuthOptions.newBuilder(auth!!)
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun signOut() {
        auth!!.signOut()
//        _authenticationState.value = false
//        _otpSendState.value = false
        isLoading = false
    }
}*/
}
