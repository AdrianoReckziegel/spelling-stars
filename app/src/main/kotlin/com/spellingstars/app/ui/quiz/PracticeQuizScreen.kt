package com.spellingstars.app.ui.quiz

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spellingstars.app.data.DistractorGenerator
import com.spellingstars.app.data.SpellingWord
import com.spellingstars.app.ui.theme.CoralPink
import com.spellingstars.app.ui.theme.CoralPinkContainer
import com.spellingstars.app.ui.theme.MintGreen
import com.spellingstars.app.ui.theme.MintGreenSoft
import com.spellingstars.app.ui.theme.OnCoralPinkContainer
import com.spellingstars.app.ui.theme.OnMintGreen
import com.spellingstars.app.ui.theme.OnSkyBlueContainer
import com.spellingstars.app.ui.theme.OnSunshineYellow
import com.spellingstars.app.ui.theme.SkyBlueContainer
import com.spellingstars.app.ui.theme.StarPurple
import com.spellingstars.app.ui.theme.StarPurpleContainer
import com.spellingstars.app.ui.theme.SunshineYellow
import com.spellingstars.app.ui.theme.SunshineYellowSoft

enum class PracticeQuizMode(val title: String, val subtitle: String) {
  MULTIPLE_CHOICE("🎯 Pick Word", "Hear the word & choose from 3 look-alike spellings"),
  LISTEN_AND_TYPE("⌨️ Listen & Type", "Hear the word & type every letter yourself")
}

@Composable
fun PracticeQuizScreen(
  words: List<SpellingWord>,
  initialMode: PracticeQuizMode = PracticeQuizMode.MULTIPLE_CHOICE,
  onSpeakWord: (String, Boolean) -> Unit,
  onSpeakWordWithSentence: (String, String, Boolean) -> Unit,
  onRecordWordResult: (String, Boolean) -> Unit,
  onQuizCompleted: (PracticeQuizMode, Int, Int) -> Unit,
  modifier: Modifier = Modifier
) {
  var quizMode by rememberSaveable(initialMode) { mutableStateOf(initialMode) }
  var currentIndex by rememberSaveable(quizMode) { mutableIntStateOf(0) }
  var correctCount by rememberSaveable(quizMode) { mutableIntStateOf(0) }
  var isFinished by rememberSaveable(quizMode) { mutableStateOf(false) }

  if (words.isEmpty()) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      Text("Add spelling words in the Word List tab to start the quiz!", fontSize = 18.sp)
    }
    return
  }

  val safeIndex = currentIndex.coerceIn(0, words.lastIndex)
  val currentWord = words[safeIndex]

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 16.dp, vertical = 12.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Adaptive 2-Button Mode Switcher (avoids Material SegmentedButton checkmark clipping)
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      PracticeQuizMode.entries.forEach { mode ->
        val selected = quizMode == mode
        Surface(
          modifier = Modifier
            .weight(1f)
            .defaultMinSize(minHeight = 46.dp)
            .clickable {
              quizMode = mode
              currentIndex = 0
              correctCount = 0
              isFinished = false
            },
          shape = RoundedCornerShape(16.dp),
          color = if (selected) StarPurple else Color.White,
          border = BorderStroke(
            width = if (selected) 0.dp else 1.5.dp,
            color = StarPurple.copy(alpha = 0.35f)
          ),
          shadowElevation = if (selected) 4.dp else 0.dp
        ) {
          Box(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = mode.title,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 13.sp,
              color = if (selected) Color.White else StarPurple,
              textAlign = TextAlign.Center,
              maxLines = 1,
              softWrap = false,
              overflow = TextOverflow.Ellipsis
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    if (isFinished) {
      QuizCelebrationCard(
        mode = quizMode,
        score = correctCount,
        total = words.size,
        onPlayAgain = {
          currentIndex = 0
          correctCount = 0
          isFinished = false
        },
        onSwitchMode = {
          quizMode = if (quizMode == PracticeQuizMode.MULTIPLE_CHOICE) {
            PracticeQuizMode.LISTEN_AND_TYPE
          } else {
            PracticeQuizMode.MULTIPLE_CHOICE
          }
          currentIndex = 0
          correctCount = 0
          isFinished = false
        }
      )
      return@Column
    }

    // Question Progress Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Word ${safeIndex + 1} of ${words.size}",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = StarPurple
      )
      Surface(
        color = SunshineYellowSoft,
        shape = RoundedCornerShape(50)
      ) {
        Text(
          text = "⭐ Score: $correctCount / ${words.size}",
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
          fontWeight = FontWeight.Bold,
          color = OnSunshineYellow,
          style = MaterialTheme.typography.labelLarge,
          maxLines = 1,
          softWrap = false
        )
      }
    }

    Spacer(modifier = Modifier.height(6.dp))

    LinearProgressIndicator(
      progress = { (safeIndex + 1).toFloat() / words.size.toFloat() },
      modifier = Modifier
        .fillMaxWidth()
        .height(10.dp)
        .clip(RoundedCornerShape(5.dp)),
      color = StarPurple,
      trackColor = StarPurpleContainer
    )

    Spacer(modifier = Modifier.height(12.dp))

    // Audio Dictation Prompt Card (Shared by both quiz modes; supports unlimited replays!)
    DictationSpeakerCard(
      word = currentWord,
      onPlayWord = { onSpeakWord(currentWord.word, false) },
      onPlaySlow = { onSpeakWord(currentWord.word, true) },
      onPlayWithSentence = {
        onSpeakWordWithSentence(currentWord.word, currentWord.exampleSentence, false)
      }
    )

    Spacer(modifier = Modifier.height(14.dp))

    when (quizMode) {
      PracticeQuizMode.MULTIPLE_CHOICE -> {
        MultipleChoiceQuestionSection(
          word = currentWord,
          onAnswered = { isCorrect ->
            onRecordWordResult(currentWord.id, isCorrect)
            if (isCorrect) correctCount++
          },
          onNextWord = {
            if (safeIndex < words.lastIndex) {
              currentIndex = safeIndex + 1
            } else {
              isFinished = true
              onQuizCompleted(quizMode, correctCount, words.size)
            }
          },
          isLastWord = safeIndex == words.lastIndex
        )
      }

      PracticeQuizMode.LISTEN_AND_TYPE -> {
        ListenAndTypeQuestionSection(
          word = currentWord,
          onAnswered = { isCorrect ->
            onRecordWordResult(currentWord.id, isCorrect)
            if (isCorrect) correctCount++
          },
          onNextWord = {
            if (safeIndex < words.lastIndex) {
              currentIndex = safeIndex + 1
            } else {
              isFinished = true
              onQuizCompleted(quizMode, correctCount, words.size)
            }
          },
          isLastWord = safeIndex == words.lastIndex
        )
      }
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DictationSpeakerCard(
  word: SpellingWord,
  onPlayWord: () -> Unit,
  onPlaySlow: () -> Unit,
  onPlayWithSentence: () -> Unit
) {
  LaunchedEffect(word.id) {
    onPlayWord()
  }

  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(24.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(2.dp, StarPurpleContainer)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "🔊 Listen to the Dictation",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.ExtraBold,
        color = StarPurple,
        textAlign = TextAlign.Center
      )
      Text(
        text = "Tap any speaker button as many times as you want!",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(12.dp))

      // FlowRow ensures Play Word, Slow, and Sentence buttons never squeeze or clip at 1.3x font scale!
      FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Button(
          onClick = onPlayWord,
          shape = RoundedCornerShape(16.dp),
          contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
          modifier = Modifier.defaultMinSize(minHeight = 46.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = StarPurple,
            contentColor = Color.White
          )
        ) {
          Icon(
            Icons.AutoMirrored.Filled.VolumeUp,
            contentDescription = "Hear Word",
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text("Play Word", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, maxLines = 1, softWrap = false)
        }

        FilledTonalButton(
          onClick = onPlaySlow,
          shape = RoundedCornerShape(16.dp),
          contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
          modifier = Modifier.defaultMinSize(minHeight = 46.dp),
          colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = SkyBlueContainer,
            contentColor = OnSkyBlueContainer
          )
        ) {
          Text("🐢 Slow", fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1, softWrap = false)
        }

        FilledTonalButton(
          onClick = onPlayWithSentence,
          shape = RoundedCornerShape(16.dp),
          contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
          modifier = Modifier.defaultMinSize(minHeight = 46.dp),
          colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = SunshineYellowSoft,
            contentColor = OnSunshineYellow
          )
        ) {
          Text("📖 Sentence", fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1, softWrap = false)
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Blanked-out context sentence clue
      Surface(
        color = StarPurpleContainer.copy(alpha = 0.45f),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(12.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "Clue: ${word.definition}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            color = Color(0xFF2B224C)
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "\"${word.maskedExampleSentence()}\"",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            color = StarPurple
          )
        }
      }
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MultipleChoiceQuestionSection(
  word: SpellingWord,
  onAnswered: (Boolean) -> Unit,
  onNextWord: () -> Unit,
  isLastWord: Boolean
) {
  val options = remember(word.id) {
    DistractorGenerator.buildQuizChoices(word.word, distractorCount = 2)
  }

  var selectedOption by remember(word.id) { mutableStateOf<String?>(null) }
  val hasAnswered = selectedOption != null
  val isCorrect = selectedOption?.equals(word.word, ignoreCase = true) == true

  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = "Which spelling is the RIGHT one?",
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.ExtraBold,
      color = Color(0xFF1E1A2B),
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(10.dp))

    options.forEachIndexed { idx, option ->
      val optionLabel = listOf("A", "B", "C").getOrElse(idx) { "${idx + 1}" }
      val isThisRealWord = option.equals(word.word, ignoreCase = true)
      val isThisSelected = selectedOption == option

      val (containerColor, borderColor, textColor) = when {
        !hasAnswered -> Triple(Color.White, StarPurple.copy(alpha = 0.35f), Color(0xFF1E1A2B))
        isThisRealWord -> Triple(MintGreenSoft, MintGreen, OnMintGreen)
        isThisSelected && !isThisRealWord -> Triple(CoralPinkContainer, CoralPink, OnCoralPinkContainer)
        else -> Triple(Color(0xFFF5F3FA), Color.LightGray, Color.Gray)
      }

      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 6.dp)
          .clickable(enabled = !hasAnswered) {
            selectedOption = option
            onAnswered(isThisRealWord)
          },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(if (hasAnswered && (isThisRealWord || isThisSelected)) 3.dp else 2.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (!hasAnswered) 3.dp else 0.dp)
      ) {
        FlowRow(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            Surface(
              color = if (hasAnswered && isThisRealWord) MintGreen else StarPurpleContainer,
              shape = CircleShape,
              modifier = Modifier.size(36.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Text(
                  text = optionLabel,
                  fontWeight = FontWeight.ExtraBold,
                  color = if (hasAnswered && isThisRealWord) Color.White else StarPurple
                )
              }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
              text = option,
              fontSize = 21.sp,
              fontWeight = FontWeight.ExtraBold,
              color = textColor,
              letterSpacing = 0.8.sp
            )
          }

          if (hasAnswered) {
            if (isThisRealWord) {
              Text("✅ Correct!", fontWeight = FontWeight.Bold, color = OnMintGreen, fontSize = 13.sp)
            } else if (isThisSelected) {
              Text("❌ Tricky twin!", fontWeight = FontWeight.Bold, color = OnCoralPinkContainer, fontSize = 13.sp)
            }
          }
        }
      }
    }

    AnimatedVisibility(visible = hasAnswered) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Surface(
          color = if (isCorrect) MintGreenSoft else SunshineYellowSoft,
          shape = RoundedCornerShape(18.dp),
          border = BorderStroke(1.5.dp, if (isCorrect) MintGreen else SunshineYellow),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = if (isCorrect) {
                "🌟 Awesome job! You spotted the right spelling: \"${word.word}\"!"
              } else {
                "💪 Nice try! That was a sneaky look-alike. The right spelling is \"${word.word}\" (${word.syllables})."
              },
              fontWeight = FontWeight.Bold,
              textAlign = TextAlign.Center,
              color = if (isCorrect) OnMintGreen else OnSunshineYellow
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
          onClick = onNextWord,
          modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 52.dp),
          contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
          shape = RoundedCornerShape(18.dp),
          colors = ButtonDefaults.buttonColors(containerColor = StarPurple)
        ) {
          Text(
            text = if (isLastWord) "See My Quiz Stars! 🏆" else "Next Word",
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold
          )
          Spacer(modifier = Modifier.width(8.dp))
          Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
        }
      }
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ListenAndTypeQuestionSection(
  word: SpellingWord,
  onAnswered: (Boolean) -> Unit,
  onNextWord: () -> Unit,
  isLastWord: Boolean
) {
  var typedText by remember(word.id) { mutableStateOf("") }
  var hasSubmitted by remember(word.id) { mutableStateOf(false) }
  var showHint by remember(word.id) { mutableStateOf(false) }

  val isCorrect = typedText.trim().equals(word.word.trim(), ignoreCase = true)

  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Type the word you heard (${word.word.length} letters):",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.weight(1f)
      )
      OutlinedButton(
        onClick = { showHint = !showHint },
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        modifier = Modifier.defaultMinSize(minHeight = 38.dp)
      ) {
        Icon(Icons.Default.Lightbulb, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = if (showHint) "Hide" else "Hint",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          maxLines = 1,
          softWrap = false
        )
      }
    }

    AnimatedVisibility(visible = showHint) {
      val hintPattern = remember(word.word) {
        word.word.mapIndexed { idx, c ->
          if (idx == 0 || idx == word.word.lastIndex) c.toString() else "_"
        }.joinToString(" ")
      }
      Surface(
        color = SunshineYellowSoft,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 6.dp)
      ) {
        Text(
          text = "💡 Starts with '${word.word.first()}' and ends with '${word.word.last()}':  $hintPattern",
          modifier = Modifier.padding(10.dp),
          fontWeight = FontWeight.Bold,
          color = OnSunshineYellow,
          textAlign = TextAlign.Center
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
      value = typedText,
      onValueChange = { if (!hasSubmitted) typedText = it },
      enabled = !hasSubmitted,
      placeholder = { Text("Type spelling here...") },
      singleLine = true,
      textStyle = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 1.2.sp
      ),
      shape = RoundedCornerShape(18.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = StarPurple,
        unfocusedBorderColor = StarPurple.copy(alpha = 0.4f),
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White
      ),
      keyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.None,
        autoCorrectEnabled = false,
        imeAction = ImeAction.Done
      ),
      keyboardActions = KeyboardActions(
        onDone = {
          if (typedText.isNotBlank() && !hasSubmitted) {
            hasSubmitted = true
            onAnswered(typedText.trim().equals(word.word.trim(), ignoreCase = true))
          }
        }
      ),
      modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(12.dp))

    if (!hasSubmitted) {
      Button(
        onClick = {
          hasSubmitted = true
          onAnswered(typedText.trim().equals(word.word.trim(), ignoreCase = true))
        },
        enabled = typedText.isNotBlank(),
        modifier = Modifier
          .fillMaxWidth()
          .defaultMinSize(minHeight = 52.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(containerColor = StarPurple)
      ) {
        Icon(Icons.Default.CheckCircle, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Check My Spelling!", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
      }
    } else {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
          containerColor = if (isCorrect) MintGreenSoft else CoralPinkContainer.copy(alpha = 0.6f)
        ),
        border = BorderStroke(2.dp, if (isCorrect) MintGreen else CoralPink)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = if (isCorrect) {
              "🎉 Spot On! You spelled \"${word.word}\" perfectly!"
            } else {
              "Almost! Let's compare letter by letter:"
            },
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,
            color = if (isCorrect) OnMintGreen else OnCoralPinkContainer,
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(10.dp))

          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            val trimmedInput = typedText.trim()
            word.word.forEachIndexed { idx, targetChar ->
              val userChar = trimmedInput.getOrNull(idx)
              val matches = userChar != null && userChar.equals(targetChar, ignoreCase = true)
              Surface(
                color = if (matches) MintGreen else CoralPink,
                shape = RoundedCornerShape(8.dp)
              ) {
                Text(
                  text = targetChar.toString(),
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                  color = Color.White,
                  fontWeight = FontWeight.ExtraBold,
                  fontSize = 17.sp
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      Button(
        onClick = onNextWord,
        modifier = Modifier
          .fillMaxWidth()
          .defaultMinSize(minHeight = 52.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(containerColor = StarPurple)
      ) {
        Text(
          text = if (isLastWord) "Finish Practice Quiz 🏆" else "Next Word",
          fontSize = 16.sp,
          fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
      }
    }
  }
}

@Composable
private fun QuizCelebrationCard(
  mode: PracticeQuizMode,
  score: Int,
  total: Int,
  onPlayAgain: () -> Unit,
  onSwitchMode: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 16.dp),
    shape = RoundedCornerShape(28.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    border = BorderStroke(3.dp, SunshineYellow)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(text = "🏆🌟🎉", fontSize = 40.sp)
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = "Amazing Effort!",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.ExtraBold,
        color = StarPurple,
        textAlign = TextAlign.Center
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = "You scored $score out of $total in ${mode.title}!",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
      )
      Spacer(modifier = Modifier.height(8.dp))
      Surface(
        color = SunshineYellowSoft,
        shape = RoundedCornerShape(16.dp)
      ) {
        Text(
          text = "+5 Bonus Weekly Study Stars Earned! ⭐",
          modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
          fontWeight = FontWeight.ExtraBold,
          color = OnSunshineYellow,
          textAlign = TextAlign.Center
        )
      }

      Spacer(modifier = Modifier.height(18.dp))

      Button(
        onClick = onPlayAgain,
        modifier = Modifier
          .fillMaxWidth()
          .defaultMinSize(minHeight = 50.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = StarPurple)
      ) {
        Icon(Icons.Default.Refresh, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Play Again", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
      }

      Spacer(modifier = Modifier.height(10.dp))

      FilledTonalButton(
        onClick = onSwitchMode,
        modifier = Modifier
          .fillMaxWidth()
          .defaultMinSize(minHeight = 50.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        shape = RoundedCornerShape(16.dp)
      ) {
        Text(
          text = if (mode == PracticeQuizMode.MULTIPLE_CHOICE) {
            "Try ⌨️ Listen & Type"
          } else {
            "Try 🎯 Pick Word"
          },
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          textAlign = TextAlign.Center
        )
      }
    }
  }
}
