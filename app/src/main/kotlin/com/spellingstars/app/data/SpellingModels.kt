package com.spellingstars.app.data

import kotlinx.serialization.Serializable

@Serializable
enum class MasteryStatus {
  NEW,
  PRACTICING,
  MASTERED
}

@Serializable
data class SpellingWord(
  val id: String,
  val word: String,
  val definition: String = "",
  val exampleSentence: String = "",
  val syllables: String = "",
  val masteryStatus: MasteryStatus = MasteryStatus.NEW,
  val correctStreak: Int = 0,
  val practiceCount: Int = 0
) {
  /**
   * Returns the example sentence with the target word blanked out for quizzes,
   * or falls back to a default kid-friendly sentence if none was provided.
   */
  fun maskedExampleSentence(): String {
    val base = if (exampleSentence.isNotBlank()) {
      exampleSentence
    } else {
      "Can you spell the word $word?"
    }
    return base.replace(Regex(Regex.escape(word), RegexOption.IGNORE_CASE), "_______")
  }
}

@Serializable
data class WeeklySpellingList(
  val id: String,
  val title: String,
  val weekLabel: String,
  val themeEmoji: String = "🌟",
  val words: List<SpellingWord>
) {
  val masteredCount: Int
    get() = words.count { it.masteryStatus == MasteryStatus.MASTERED }

  val practicingCount: Int
    get() = words.count { it.masteryStatus == MasteryStatus.PRACTICING }

  val progressFraction: Float
    get() = if (words.isEmpty()) 0f else masteredCount.toFloat() / words.size.toFloat()
}
