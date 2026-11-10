package com.spellingstars.app.ui.main

import com.spellingstars.app.data.DistractorGenerator
import com.spellingstars.app.data.KidWordHelper
import com.spellingstars.app.data.MasteryStatus
import com.spellingstars.app.data.SpellingRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MainScreenViewModelTest {

  @Test
  fun distractorGenerator_createsTwoUniqueLookAlikeWords() {
    val targetWords = listOf(
      "because", "friend", "believe", "butterfly", "mountain",
      "adventure", "calendar", "planet", "happy", "cat"
    )
    for (target in targetWords) {
      val distractors = DistractorGenerator.generateDistractors(target, count = 2)
      assertEquals("Expected 2 distractors for $target", 2, distractors.size)
      assertEquals("Distractors should be unique for $target", 2, distractors.distinct().size)
      distractors.forEach { d ->
        assertFalse(
          "Distractor '$d' should not match target '$target'",
          d.equals(target, ignoreCase = true)
        )
      }

      val quizChoices = DistractorGenerator.buildQuizChoices(target, distractorCount = 2)
      assertEquals(3, quizChoices.size)
      assertTrue(quizChoices.any { it.equals(target, ignoreCase = true) })
    }
  }

  @Test
  fun kidWordHelper_parsesBulkWordsAndAutoGeneratesInfo() {
    val bulkParsed = KidWordHelper.parseBulkWords("rainbow, umbrella\nwhisper; promise   elephant")
    assertEquals(listOf("rainbow", "umbrella", "whisper", "promise", "elephant"), bulkParsed)

    val known = KidWordHelper.enrichWord("because")
    assertEquals("be • cause", known.syllables)
    assertTrue(known.definition.isNotBlank())
    assertTrue(known.exampleSentence.contains("because", ignoreCase = true))
  }

  @Test
  fun repository_addsBulkWordsAndTracksMasteryAndStars() {
    val repo = SpellingRepository(context = null)
    val initial = repo.state.value
    val initialActive = initial.lists.first { it.id == initial.activeListId }
    val initialWordCount = initialActive.words.size
    val initialStars = initial.totalStars

    val addedCount = repo.addBulkWordsToActiveList("astronaut\nvolcano, penguin")
    assertEquals(3, addedCount)

    val afterBulk = repo.state.value
    val updatedList = afterBulk.lists.first { it.id == afterBulk.activeListId }
    assertEquals(initialWordCount + 3, updatedList.words.size)

    val addedWord = updatedList.words.last()
    assertEquals("penguin", addedWord.word)

    repo.recordWordPractice(addedWord.id, isMasteredOrCorrect = true, explicitStatus = MasteryStatus.MASTERED)
    val afterMastery = repo.state.value
    val masteredWord = afterMastery.lists
      .first { it.id == afterMastery.activeListId }
      .words.first { it.id == addedWord.id }
    assertEquals(MasteryStatus.MASTERED, masteredWord.masteryStatus)
    assertTrue(afterMastery.totalStars > initialStars)
  }
}
