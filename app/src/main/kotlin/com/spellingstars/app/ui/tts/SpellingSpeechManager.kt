package com.spellingstars.app.ui.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

/**
 * Friendly Text-to-Speech manager tailored for elementary school spelling practice.
 * Supports normal speed, slow tortoise speed (0.6f), and letter-by-letter spelling dictation.
 */
class SpellingSpeechManager(context: Context) {

  private var tts: TextToSpeech? = null
  private var isReady = false

  init {
    tts = TextToSpeech(context.applicationContext) { status ->
      if (status == TextToSpeech.SUCCESS) {
        val result = tts?.setLanguage(Locale.US)
        isReady = result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED
        tts?.setPitch(1.08f) // Slightly warm, upbeat voice pitch for kids
      }
    }
  }

  fun speakWord(word: String, slow: Boolean = false) {
    if (!isReady) return
    tts?.setSpeechRate(if (slow) 0.58f else 0.92f)
    tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null, "word_${System.currentTimeMillis()}")
  }

  fun speakWordWithSentence(word: String, sentence: String, slow: Boolean = false) {
    if (!isReady) return
    tts?.setSpeechRate(if (slow) 0.62f else 0.92f)
    val text = if (sentence.isNotBlank()) {
      "$word. ... $sentence ... Again, the word is $word."
    } else {
      word
    }
    tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "dictation_${System.currentTimeMillis()}")
  }

  fun spellOutWord(word: String) {
    if (!isReady) return
    val letters = word.trim().toCharArray().joinToString(", ") { it.toString() }
    tts?.setSpeechRate(0.78f)
    tts?.speak("$word. $letters. $word!", TextToSpeech.QUEUE_FLUSH, null, "spell_${System.currentTimeMillis()}")
  }

  fun speakEncouragement(message: String) {
    if (!isReady) return
    tts?.setSpeechRate(0.95f)
    tts?.speak(message, TextToSpeech.QUEUE_FLUSH, null, "cheer_${System.currentTimeMillis()}")
  }

  fun shutdown() {
    tts?.stop()
    tts?.shutdown()
    tts = null
  }
}

