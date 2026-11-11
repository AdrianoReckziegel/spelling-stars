package com.spellingstars.app.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spellingstars.app.R
import com.spellingstars.app.data.KidWordHelper
import com.spellingstars.app.ui.flashcards.FlashcardsScreen
import com.spellingstars.app.ui.quiz.PracticeQuizMode
import com.spellingstars.app.ui.quiz.PracticeQuizScreen
import com.spellingstars.app.ui.theme.SpellingStarsTheme
import com.spellingstars.app.ui.words.WordManagerScreen

@Preview(
  name = "AppIconPreview",
  widthDp = 160,
  heightDp = 160,
  showBackground = true
)
@Composable
fun AppIconPreview() {
  Box(
    modifier = Modifier
      .size(160.dp)
      .background(Color.Transparent),
    contentAlignment = Alignment.Center
  ) {
    Box(
      modifier = Modifier
        .size(136.dp)
        .clip(RoundedCornerShape(32.dp))
    ) {
      Image(
        painter = painterResource(id = R.drawable.ic_launcher_background),
        contentDescription = null,
        modifier = Modifier.size(136.dp)
      )
      Image(
        painter = painterResource(id = R.drawable.ic_launcher_foreground),
        contentDescription = "Spelling Stars App Icon",
        modifier = Modifier.size(136.dp)
      )
    }
  }
}

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
