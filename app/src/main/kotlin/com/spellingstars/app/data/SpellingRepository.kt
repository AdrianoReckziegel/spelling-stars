package com.spellingstars.app.data

import android.content.Context
import java.io.File
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class PersistedSpellingData(
  val activeListId: String,
  val totalStars: Int,
  val lists: List<WeeklySpellingList>
)

class SpellingRepository(private val context: Context? = null) {

  private val json = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
    encodeDefaults = true
  }

  private val storageFile: File?
    get() = context?.filesDir?.let { File(it, "spelling_stars_state_v2.json") }

  private val _state = MutableStateFlow(loadInitialState())
  val state: StateFlow<PersistedSpellingData> = _state.asStateFlow()

  private fun loadInitialState(): PersistedSpellingData {
    val file = storageFile
    if (file != null && file.exists()) {
      runCatching {
        val raw = file.readText()
        return json.decodeFromString<PersistedSpellingData>(raw)
      }
    }
    val starter = KidWordHelper.starterWeeklyLists()
    return PersistedSpellingData(
      activeListId = starter.first().id,
      totalStars = 15,
      lists = starter
    )
  }

  private fun saveState(data: PersistedSpellingData) {
    val file = storageFile ?: return
    runCatching {
      file.writeText(json.encodeToString(data))
    }
  }

  private fun updateAndPersist(transform: (PersistedSpellingData) -> PersistedSpellingData) {
    _state.update { current ->
      val next = transform(current)
      saveState(next)
      next
    }
  }

  fun selectWeeklyList(listId: String) {
    updateAndPersist { current ->
      current.copy(activeListId = listId)
    }
  }

  fun createNewWeeklyList(
    title: String,
    themeEmoji: String,
    rawWordsInput: String
  ): String {
    val parsedWords = KidWordHelper.parseBulkWords(rawWordsInput)

    val words = parsedWords.map { w ->
      val info = KidWordHelper.enrichWord(w)
      SpellingWord(
        id = UUID.randomUUID().toString(),
        word = w,
        definition = info.definition,
        exampleSentence = info.exampleSentence,
        syllables = info.syllables,
        masteryStatus = MasteryStatus.NEW
      )
    }

    val newListId = "week_${System.currentTimeMillis()}"
    val newList = WeeklySpellingList(
      id = newListId,
      title = title.ifBlank { "Weekly Spelling Words" },
      weekLabel = "${words.size} Spelling Words",
      themeEmoji = themeEmoji.ifBlank { "🌟" },
      words = words
    )

    updateAndPersist { current ->
      current.copy(
        activeListId = newListId,
        lists = listOf(newList) + current.lists
      )
    }
    return newListId
  }

  /**
   * Adds multiple words at once (from a newline, comma, or space-separated list)
   * to the currently active spelling list.
   */
  fun addBulkWordsToActiveList(rawWordsInput: String): Int {
    val parsedWords = KidWordHelper.parseBulkWords(rawWordsInput)
    if (parsedWords.isEmpty()) return 0

    var addedCount = 0
    updateAndPersist { current ->
      val updatedLists = current.lists.map { list ->
        if (list.id != current.activeListId) {
          list
        } else {
          val existingLower = list.words.map { it.word.lowercase() }.toSet()
          val newSpellingWords = parsedWords
            .filter { it.lowercase() !in existingLower }
            .map { clean ->
              val info = KidWordHelper.enrichWord(rawWord = clean)
              SpellingWord(
                id = UUID.randomUUID().toString(),
                word = clean,
                definition = info.definition,
                exampleSentence = info.exampleSentence,
                syllables = info.syllables,
                masteryStatus = MasteryStatus.NEW
              )
            }
          addedCount = newSpellingWords.size
          val combined = list.words + newSpellingWords
          list.copy(
            words = combined,
            weekLabel = "${combined.size} Spelling Words"
          )
        }
      }
      current.copy(lists = updatedLists)
    }
    return addedCount
  }

  fun addWordToActiveList(
    wordText: String,
    customDefinition: String = "",
    customSentence: String = ""
  ) {
    val clean = wordText.trim()
    if (clean.isEmpty()) return
    // If the user pasted multiple words into the single-word input without custom definition, handle them all!
    val multiCheck = KidWordHelper.parseBulkWords(clean)
    if (multiCheck.size > 1 && customDefinition.isBlank() && customSentence.isBlank()) {
      addBulkWordsToActiveList(clean)
      return
    }

    val info = KidWordHelper.enrichWord(
      rawWord = clean,
      customDefinition = customDefinition,
      customSentence = customSentence
    )

    updateAndPersist { current ->
      val updatedLists = current.lists.map { list ->
        if (list.id != current.activeListId) {
          list
        } else {
          val newWord = SpellingWord(
            id = UUID.randomUUID().toString(),
            word = clean,
            definition = info.definition,
            exampleSentence = info.exampleSentence,
            syllables = info.syllables,
            masteryStatus = MasteryStatus.NEW
          )
          val combined = list.words + newWord
          list.copy(
            words = combined,
            weekLabel = "${combined.size} Spelling Words"
          )
        }
      }
      current.copy(lists = updatedLists)
    }
  }

  fun updateWordDetails(
    wordId: String,
    newWordText: String,
    newDefinition: String,
    newSentence: String,
    newSyllables: String
  ) {
    val clean = newWordText.trim()
    if (clean.isEmpty()) return
    val info = KidWordHelper.enrichWord(
      rawWord = clean,
      customDefinition = newDefinition,
      customSentence = newSentence,
      customSyllables = newSyllables
    )

    updateAndPersist { current ->
      val updatedLists = current.lists.map { list ->
        if (list.id != current.activeListId) {
          list
        } else {
          list.copy(
            words = list.words.map { w ->
              if (w.id == wordId) {
                w.copy(
                  word = clean,
                  definition = info.definition,
                  exampleSentence = info.exampleSentence,
                  syllables = info.syllables
                )
              } else {
                w
              }
            }
          )
        }
      }
      current.copy(lists = updatedLists)
    }
  }

  fun deleteWordFromActiveList(wordId: String) {
    updateAndPersist { current ->
      val updatedLists = current.lists.map { list ->
        if (list.id != current.activeListId) {
          list
        } else {
          val remaining = list.words.filterNot { it.id == wordId }
          list.copy(
            words = remaining,
            weekLabel = "${remaining.size} Spelling Words"
          )
        }
      }
      current.copy(lists = updatedLists)
    }
  }

  fun recordWordPractice(wordId: String, isMasteredOrCorrect: Boolean, explicitStatus: MasteryStatus? = null) {
    updateAndPersist { current ->
      var starsEarned = 0
      val updatedLists = current.lists.map { list ->
        if (list.id != current.activeListId) {
          list
        } else {
          val updatedWords = list.words.map { word ->
            if (word.id != wordId) {
              word
            } else {
              val nextStreak = if (isMasteredOrCorrect) word.correctStreak + 1 else 0
              val nextStatus = explicitStatus ?: when {
                isMasteredOrCorrect && (nextStreak >= 2 || word.masteryStatus == MasteryStatus.MASTERED) ->
                  MasteryStatus.MASTERED
                isMasteredOrCorrect -> MasteryStatus.PRACTICING
                else -> MasteryStatus.PRACTICING
              }
              if (isMasteredOrCorrect) {
                starsEarned += if (nextStatus == MasteryStatus.MASTERED) 2 else 1
              }
              word.copy(
                masteryStatus = nextStatus,
                correctStreak = nextStreak,
                practiceCount = word.practiceCount + 1
              )
            }
          }
          list.copy(words = updatedWords)
        }
      }
      current.copy(
        totalStars = current.totalStars + starsEarned,
        lists = updatedLists
      )
    }
  }

  fun awardBonusStars(bonusStars: Int = 5) {
    updateAndPersist { current ->
      current.copy(totalStars = current.totalStars + bonusStars)
    }
  }
}
