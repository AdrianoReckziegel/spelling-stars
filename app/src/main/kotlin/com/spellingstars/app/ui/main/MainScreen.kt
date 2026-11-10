package com.spellingstars.app.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.spellingstars.app.data.MasteryStatus
import com.spellingstars.app.data.SpellingRepository
import com.spellingstars.app.ui.flashcards.FlashcardsScreen
import com.spellingstars.app.ui.quiz.PracticeQuizMode
import com.spellingstars.app.ui.quiz.PracticeQuizScreen
import com.spellingstars.app.ui.theme.OnSunshineYellow
import com.spellingstars.app.ui.theme.SpellingStarsTheme
import com.spellingstars.app.ui.theme.StarPurple
import com.spellingstars.app.ui.theme.StarPurpleContainer
import com.spellingstars.app.ui.theme.SunshineYellow
import com.spellingstars.app.ui.tts.SpellingSpeechManager
import com.spellingstars.app.ui.words.WordManagerScreen

enum class SpellingTab(val label: String, val icon: ImageVector) {
  FLASHCARDS("Cards", Icons.Default.Style),
  QUIZ("Quiz", Icons.Default.Quiz),
  WORDS("Words", Icons.Default.EditNote)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
  onItemClick: (NavKey) -> Unit = {},
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val repository = remember { SpellingRepository(context) }
  val speechManager = remember { SpellingSpeechManager(context) }

  DisposableEffect(Unit) {
    onDispose {
      speechManager.shutdown()
    }
  }

  val appData by repository.state.collectAsStateWithLifecycle()
  val activeList = appData.lists.find { it.id == appData.activeListId } ?: appData.lists.first()

  var selectedTab by rememberSaveable { mutableStateOf(SpellingTab.FLASHCARDS) }
  var requestedQuizMode by rememberSaveable { mutableStateOf(PracticeQuizMode.MULTIPLE_CHOICE) }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "🌟", fontSize = 22.sp, maxLines = 1)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Spelling Stars",
              fontWeight = FontWeight.ExtraBold,
              color = StarPurple,
              maxLines = 1,
              softWrap = false,
              overflow = TextOverflow.Ellipsis
            )
          }
        },
        actions = {
          Surface(
            color = SunshineYellow,
            shape = RoundedCornerShape(50),
            modifier = Modifier.padding(end = 12.dp)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Text(text = "⭐", fontSize = 14.sp, maxLines = 1)
              Text(
                text = "${appData.totalStars}",
                fontWeight = FontWeight.ExtraBold,
                color = OnSunshineYellow,
                fontSize = 13.sp,
                maxLines = 1,
                softWrap = false
              )
            }
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.background
        )
      )
    },
    bottomBar = {
      NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
      ) {
        SpellingTab.entries.forEach { tab ->
          val selected = selectedTab == tab
          NavigationBarItem(
            selected = selected,
            onClick = { selectedTab = tab },
            icon = {
              Icon(imageVector = tab.icon, contentDescription = tab.label)
            },
            label = {
              Text(
                text = tab.label,
                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium,
                fontSize = 12.sp,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = StarPurple,
              selectedTextColor = StarPurple,
              indicatorColor = StarPurpleContainer
            )
          )
        }
      }
    }
  ) { innerPadding ->
    Box(modifier = Modifier.padding(innerPadding)) {
      when (selectedTab) {
        SpellingTab.FLASHCARDS -> {
          FlashcardsScreen(
            activeList = activeList,
            onSpeakWord = { word, slow -> speechManager.speakWord(word, slow) },
            onSpellOutWord = { word -> speechManager.spellOutWord(word) },
            onSpeakSentence = { word, sentence ->
              speechManager.speakWordWithSentence(word, sentence, slow = false)
            },
            onMarkWordStatus = { wordId, isCorrect, explicitStatus ->
              repository.recordWordPractice(wordId, isCorrect, explicitStatus)
              if (explicitStatus == MasteryStatus.MASTERED) {
                speechManager.speakEncouragement("Great job!")
              }
            },
            onFinishDeck = {
              repository.awardBonusStars(5)
              speechManager.speakEncouragement("Yay! You finished your flashcards!")
            }
          )
        }

        SpellingTab.QUIZ -> {
          PracticeQuizScreen(
            words = activeList.words,
            initialMode = requestedQuizMode,
            onSpeakWord = { word, slow -> speechManager.speakWord(word, slow) },
            onSpeakWordWithSentence = { word, sentence, slow ->
              speechManager.speakWordWithSentence(word, sentence, slow)
            },
            onRecordWordResult = { wordId, isCorrect ->
              repository.recordWordPractice(wordId, isCorrect)
            },
            onQuizCompleted = { _, score, total ->
              repository.awardBonusStars(5)
              speechManager.speakEncouragement("Awesome work! You got $score out of $total!")
            }
          )
        }

        SpellingTab.WORDS -> {
          WordManagerScreen(
            lists = appData.lists,
            activeList = activeList,
            onSelectList = { repository.selectWeeklyList(it) },
            onCreateWeeklyList = { title, emoji, rawWords ->
              repository.createNewWeeklyList(title, emoji, rawWords)
            },
            onAddBulkWords = { rawWords ->
              repository.addBulkWordsToActiveList(rawWords)
            },
            onAddWord = { word, def, sent ->
              repository.addWordToActiveList(word, def, sent)
            },
            onUpdateWord = { wordId, word, def, sent, syl ->
              repository.updateWordDetails(wordId, word, def, sent, syl)
            },
            onDeleteWord = { wordId ->
              repository.deleteWordFromActiveList(wordId)
            },
            onSpeakWord = { word -> speechManager.speakWord(word, slow = false) }
          )
        }
      }
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
  SpellingStarsTheme {
    MainScreen()
  }
}
