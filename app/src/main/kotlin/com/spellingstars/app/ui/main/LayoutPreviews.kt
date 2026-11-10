package com.spellingstars.app.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.spellingstars.app.data.KidWordHelper
import com.spellingstars.app.ui.flashcards.FlashcardsScreen
import com.spellingstars.app.ui.quiz.PracticeQuizMode
import com.spellingstars.app.ui.quiz.PracticeQuizScreen
import com.spellingstars.app.ui.theme.SpellingStarsTheme
import com.spellingstars.app.ui.words.WordManagerScreen

@Preview(
  name = "Flashcards_Pixel10",
  widthDp = 411,
  heightDp = 890,
  fontScale = 1.15f,
  showBackground = true
)
@Composable
fun FlashcardsPreview() {
  val sampleList = KidWordHelper.starterWeeklyLists().first()
  SpellingStarsTheme {
    FlashcardsScreen(
      activeList = sampleList,
      onSpeakWord = { _, _ -> },
      onSpellOutWord = {},
      onSpeakSentence = { _, _ -> },
      onMarkWordStatus = { _, _, _ -> },
      onFinishDeck = {}
    )
  }
}

@Preview(
  name = "QuizMC_Pixel10",
  widthDp = 411,
  heightDp = 890,
  fontScale = 1.15f,
  showBackground = true
)
@Composable
fun QuizMultipleChoicePreview() {
  val sampleList = KidWordHelper.starterWeeklyLists().first()
  SpellingStarsTheme {
    PracticeQuizScreen(
      words = sampleList.words,
      initialMode = PracticeQuizMode.MULTIPLE_CHOICE,
      onSpeakWord = { _, _ -> },
      onSpeakWordWithSentence = { _, _, _ -> },
      onRecordWordResult = { _, _ -> },
      onQuizCompleted = { _, _, _ -> }
    )
  }
}

@Preview(
  name = "QuizType_Pixel10",
  widthDp = 411,
  heightDp = 890,
  fontScale = 1.15f,
  showBackground = true
)
@Composable
fun QuizListenTypePreview() {
  val sampleList = KidWordHelper.starterWeeklyLists().first()
  SpellingStarsTheme {
    PracticeQuizScreen(
      words = sampleList.words,
      initialMode = PracticeQuizMode.LISTEN_AND_TYPE,
      onSpeakWord = { _, _ -> },
      onSpeakWordWithSentence = { _, _, _ -> },
      onRecordWordResult = { _, _ -> },
      onQuizCompleted = { _, _, _ -> }
    )
  }
}

@Preview(
  name = "Words_Pixel10",
  widthDp = 411,
  heightDp = 890,
  fontScale = 1.15f,
  showBackground = true
)
@Composable
fun WordManagerPreview() {
  val lists = KidWordHelper.starterWeeklyLists()
  SpellingStarsTheme {
    WordManagerScreen(
      lists = lists,
      activeList = lists.first(),
      onSelectList = {},
      onCreateWeeklyList = { _, _, _ -> },
      onAddBulkWords = {},
      onAddWord = { _, _, _ -> },
      onUpdateWord = { _, _, _, _, _ -> },
      onDeleteWord = {},
      onSpeakWord = {}
    )
  }
}
