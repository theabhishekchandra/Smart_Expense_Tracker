package com.abhishek.spendly.core.voice

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

object VoiceManager {

    enum class Status {
        Idle,       // Nothing happening
        Listening,  // Actively listening
        Speak,      // Prompting user to speak
        Processing, // Processing results
        Error
    }

    private lateinit var appContext: Context
    private var speechRecognizer: SpeechRecognizer? = null

    private val _status = MutableStateFlow(Status.Idle)
    val status: StateFlow<Status> = _status.asStateFlow()

    private val _recognizedText = MutableStateFlow("")
    val recognizedText: StateFlow<String> = _recognizedText.asStateFlow()

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun toggleListening() {
        if (_status.value == Status.Listening || _status.value == Status.Speak) {
            stopListening()
        } else {
            startListening()
        }
    }

    fun startListening() {
        if (!::appContext.isInitialized) return
        if (!SpeechRecognizer.isRecognitionAvailable(appContext)) {
            _recognizedText.value = "Speech not available"
            _status.value = Status.Error
            return
        }

        _recognizedText.value = ""
        _status.value = Status.Speak

        // ✅ Only destroy if previous one exists
        speechRecognizer?.destroy()

        // ✅ Explicitly bind to Google recognizer if available
        val component = ComponentName(
            "com.google.android.googlequicksearchbox",
            "com.google.android.voicesearch.serviceapi.GoogleRecognitionService"
        )
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(appContext, component)

        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                _status.value = Status.Listening
            }

            override fun onBeginningOfSpeech() {
                _status.value = Status.Listening
            }

            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {
                _status.value = Status.Processing
            }
            override fun onEvent(eventType: Int, params: Bundle?) {}

            override fun onPartialResults(partialResults: Bundle?) {
                val partial = partialResults
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.joinToString(" ") ?: return
                _recognizedText.value = partial
            }

            override fun onResults(results: Bundle?) {
                val text = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.joinToString(" ")
                if (!text.isNullOrBlank()) _recognizedText.value = text
                _status.value = Status.Idle
            }

            override fun onError(error: Int) {
                _recognizedText.value = "Error code: $error"
                _status.value = Status.Error
            }
        })

        // ✅ Build intent
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, appContext.packageName)
        }

        // ✅ Start listening only after recognizer initialized
        speechRecognizer?.startListening(intent)
    }

    fun stopListening() {
        try {
            speechRecognizer?.stopListening()
            speechRecognizer?.cancel()
            speechRecognizer?.destroy()
        } catch (_: Exception) {
        }
        speechRecognizer = null
        _status.value = Status.Idle
    }
}
