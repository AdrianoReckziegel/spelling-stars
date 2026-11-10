package com.spellingstars.app.ui.flashcards

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spellingstars.app.data.MasteryStatus
import com.spellingstars.app.data.SpellingWord
import com.spellingstars.app.data.WeeklySpellingList
import com.spellingstars.app.ui.theme.CoralPink
import com.spellingstars.app.ui.theme.CoralPinkContainer
import com.spellingstars.app.ui.theme.MintGreen
import com.spellingstars.app.ui.theme.MintGreenSoft
import com.spellingstars.app.ui.theme.OnCoralPinkContainer
import com.spellingstars.app.ui.theme.OnMintGreen
import com.spellingstars.app.ui.theme.OnSkyBlueContainer
import com.spellingstars.app.ui.theme.OnSunshineYellow
import com.spellingstars.app.ui.theme.SkyBlue
import com.spellingstars.app.ui.theme.SkyBlueContainer
import com.spellingstars.app.ui.theme.StarPurple
import com.spellingstars.app.ui.theme.StarPurpleContainer
import com.spellingstars.app.ui.theme.SunshineYellow
import com.spellingstars.app.ui.theme.SunshineYellowSoft

enum class FlashcardFilter(val label: String) {
  ALL("All Words"),
  NEEDS_PRACTICE("Learning"),
  MASTERED("Mastered ⭐")
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlashcardsScreen(
  activeList: WeeklySpellingList,
  onSpeakWord: (String, Boolean) -> Unit,
  onSpellOutWord: (String) -> Unit,
  onSpeakSentence: (String, String) -> Unit,
  onMarkWordStatus: (String, Boolean, MasteryStatus) -> Unit,
  onFinishDeck: () -> Unit,
  modifier: Modifier = Modifier
) {
  val words = activeList.words
  var selectedFilter by rememberSaveable { mutableStateOf(FlashcardFilter.ALL) }

  val filteredWords = remember(words, selectedFilter) {
    when (selectedFilter) {
      FlashcardFilter.ALL -> words
      FlashcardFilter.NEEDS_PRACTICE -> {
        val unmastered = words.filter { it.masteryStatus != MasteryStatus.MASTERED }
        unmastered.ifEmpty { words }
      }
      FlashcardFilter.MASTERED -> {
        val mastered = words.filter { it.masteryStatus == MasteryStatus.MASTERED }
        mastered.ifEmpty { words }
      }
    }
  }

  var currentIndex by rememberSaveable(selectedFilter, activeList.id) { mutableIntStateOf(0) }
  var isFlipped by rememberSaveable(currentIndex, selectedFilter, activeList.id) { mutableStateOf(false) }

  val safeIndex = currentIndex.coerceIn(0, (filteredWords.size - 1).coerceAtLeast(0))
  val currentWord = filteredWords.getOrNull(safeIndex)

  LaunchedEffect(currentWord?.id) {
    isFlipped = false
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 16.dp, vertical = 10.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Active List Mastery Summary Header
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(20.dp),
      elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            Brush.horizontalGradient(
              colors = listOf(StarPurple, Color(0xFF9155FD))
            )
          )
          .padding(horizontal = 14.dp, vertical = 12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = activeList.themeEmoji, fontSize = 26.sp)
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = activeList.title,
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.ExtraBold,
              color = Color.White
            )
            Text(
              text = "${activeList.masteredCount} of ${activeList.words.size} words mastered",
              style = MaterialTheme.typography.labelSmall,
              color = Color.White.copy(alpha = 0.9f)
            )
          }
          Surface(
            color = SunshineYellow,
            shape = RoundedCornerShape(12.dp)
          ) {
            Text(
              text = "${activeList.masteredCount}/${activeList.words.size} ⭐",
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
              fontWeight = FontWeight.ExtraBold,
              color = OnSunshineYellow,
              fontSize = 12.sp,
              maxLines = 1,
              softWrap = false
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Filter Chips FlowRow
    FlowRow(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
      verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      FlashcardFilter.entries.forEach { filter ->
        val selected = selectedFilter == filter
        FilterChip(
          selected = selected,
          onClick = {
            selectedFilter = filter
            currentIndex = 0
            isFlipped = false
          },
          label = {
            Text(
              text = filter.label,
              fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
              maxLines = 1,
              softWrap = false
            )
          },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = StarPurple,
            selectedLabelColor = Color.White
          )
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    if (currentWord == null) {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 32.dp),
        shape = RoundedCornerShape(24.dp)
      ) {
        Column(
          modifier = Modifier.padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text("🎉 No flashcards in this list yet!", fontSize = 20.sp, fontWeight = FontWeight.Bold)
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            "Go to the Words tab to paste or type this week's spelling words all at once!",
            textAlign = TextAlign.Center
          )
        }
      }
      return@Column
    }

    // Card Progress Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Card ${safeIndex + 1} of ${filteredWords.size}",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = StarPurple
      )
      StatusBadge(status = currentWord.masteryStatus)
    }

    Spacer(modifier = Modifier.height(6.dp))

    LinearProgressIndicator(
      progress = { (safeIndex + 1).toFloat() / filteredWords.size.toFloat() },
      modifier = Modifier
        .fillMaxWidth()
        .height(10.dp)
        .clip(RoundedCornerShape(5.dp)),
      color = StarPurple,
      trackColor = StarPurpleContainer
    )

    Spacer(modifier = Modifier.height(12.dp))

    // 3D Interactive Flip Card
    FlipFlashcard(
      word = currentWord,
      cardNumber = safeIndex + 1,
      isFlipped = isFlipped,
      onFlip = { isFlipped = !isFlipped },
      onSpeakNormal = { onSpeakWord(currentWord.word, false) },
      onSpeakSlow = { onSpeakWord(currentWord.word, true) },
      onSpellOut = { onSpellOutWord(currentWord.word) },
      onSpeakSentence = { onSpeakSentence(currentWord.word, currentWord.exampleSentence) }
    )

    Spacer(modifier = Modifier.height(14.dp))

    // Self-Assessment Mastery Buttons
    Text(
      text = "How did you do with \"${currentWord.word}\"?",
      style = MaterialTheme.typography.bodyMedium,
      fontWeight = FontWeight.SemiBold,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(8.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      OutlinedButton(
        onClick = {
          onMarkWordStatus(currentWord.id, false, MasteryStatus.PRACTICING)
          if (safeIndex < filteredWords.size - 1) {
            currentIndex = safeIndex + 1
          } else {
            currentIndex = 0
            onFinishDeck()
          }
        },
        modifier = Modifier
          .weight(1f)
          .defaultMinSize(minHeight = 52.dp),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(2.dp, SunshineYellow),
        colors = ButtonDefaults.outlinedButtonColors(
          containerColor = SunshineYellowSoft,
          contentColor = OnSunshineYellow
        )
      ) {
        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Practice More",
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          textAlign = TextAlign.Center
        )
      }

      Button(
        onClick = {
          onMarkWordStatus(currentWord.id, true, MasteryStatus.MASTERED)
          if (safeIndex < filteredWords.size - 1) {
            currentIndex = safeIndex + 1
          } else {
            currentIndex = 0
            onFinishDeck()
          }
        },
        modifier = Modifier
          .weight(1f)
          .defaultMinSize(minHeight = 52.dp),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = MintGreen,
          contentColor = Color.White
        )
      ) {
        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "I Got It! ⭐",
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          textAlign = TextAlign.Center
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Previous / Flip / Next Navigation Controls
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      FilledTonalButton(
        onClick = { if (safeIndex > 0) currentIndex = safeIndex - 1 },
        enabled = safeIndex > 0,
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
        modifier = Modifier.defaultMinSize(minHeight = 46.dp)
      ) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous card", modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Prev", fontSize = 13.sp, maxLines = 1, softWrap = false)
      }

      FilledTonalButton(
        onClick = { isFlipped = !isFlipped },
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
        modifier = Modifier
          .weight(1f)
          .defaultMinSize(minHeight = 46.dp),
        colors = ButtonDefaults.filledTonalButtonColors(
          containerColor = SkyBlueContainer,
          contentColor = OnSkyBlueContainer
        )
      ) {
        Icon(Icons.Default.FlipCameraAndroid, contentDescription = "Flip Card", modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = if (isFlipped) "Show Word" else "Flip Card",
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          maxLines = 1,
          softWrap = false,
          overflow = TextOverflow.Ellipsis
        )
      }

      FilledTonalButton(
        onClick = { if (safeIndex < filteredWords.size - 1) currentIndex = safeIndex + 1 },
        enabled = safeIndex < filteredWords.size - 1,
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
        modifier = Modifier.defaultMinSize(minHeight = 46.dp)
      ) {
        Text("Next", fontSize = 13.sp, maxLines = 1, softWrap = false)
        Spacer(modifier = Modifier.width(4.dp))
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next card", modifier = Modifier.size(18.dp))
      }
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlipFlashcard(
  word: SpellingWord,
  cardNumber: Int,
  isFlipped: Boolean,
  onFlip: () -> Unit,
  onSpeakNormal: () -> Unit,
  onSpeakSlow: () -> Unit,
  onSpellOut: () -> Unit,
  onSpeakSentence: () -> Unit
) {
  val rotation by animateFloatAsState(
    targetValue = if (isFlipped) 180f else 0f,
    animationSpec = tween(durationMillis = 480, easing = FastOutSlowInEasing),
    label = "flashcard_3d_rotation"
  )
  val density = LocalDensity.current.density

  val adaptiveWordFontSize = when {
    word.word.length >= 11 -> 28.sp
    word.word.length >= 9 -> 32.sp
    else -> 36.sp
  }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .defaultMinSize(minHeight = 320.dp)
      .graphicsLayer {
        rotationY = rotation
        cameraDistance = 14f * density
      }
      .clickable { onFlip() },
    shape = RoundedCornerShape(28.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    border = BorderStroke(
      width = 3.dp,
      color = if (rotation <= 90f) StarPurple.copy(alpha = 0.35f) else SkyBlue.copy(alpha = 0.5f)
    )
  ) {
    if (rotation <= 90f) {
      // FRONT OF FLASHCARD
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .defaultMinSize(minHeight = 320.dp)
          .background(
            Brush.verticalGradient(
              colors = listOf(Color.White, StarPurpleContainer.copy(alpha = 0.55f))
            )
          )
          .padding(18.dp)
      ) {
        Column(
          modifier = Modifier.fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Surface(
              color = StarPurpleContainer,
              shape = RoundedCornerShape(12.dp)
            ) {
              Text(
                text = "🃏 Word #$cardNumber",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = StarPurple,
                maxLines = 1,
                softWrap = false
              )
            }
            Text(
              text = "Tap card to flip ↺",
              style = MaterialTheme.typography.labelMedium,
              color = StarPurple.copy(alpha = 0.75f),
              fontWeight = FontWeight.SemiBold,
              maxLines = 1,
              softWrap = false
            )
          }

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = word.word,
              fontSize = adaptiveWordFontSize,
              fontWeight = FontWeight.ExtraBold,
              color = Color(0xFF221547),
              textAlign = TextAlign.Center,
              letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            if (word.syllables.isNotBlank()) {
              Surface(
                color = SunshineYellowSoft,
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, SunshineYellow.copy(alpha = 0.5f))
              ) {
                Text(
                  text = word.syllables,
                  modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp),
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = OnSunshineYellow,
                  textAlign = TextAlign.Center
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Visual Letter Blocks
            FlowRow(
              horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
              verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
              val vowels = "aeiouAEIOU"
              word.word.forEach { ch ->
                val isVowel = ch in vowels
                Surface(
                  color = if (isVowel) CoralPinkContainer else SkyBlueContainer,
                  shape = RoundedCornerShape(8.dp),
                  border = BorderStroke(
                    1.dp,
                    if (isVowel) CoralPink.copy(alpha = 0.5f) else SkyBlue.copy(alpha = 0.5f)
                  )
                ) {
                  Text(
                    text = ch.toString(),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isVowel) OnCoralPinkContainer else OnSkyBlueContainer
                  )
                }
              }
            }
          }

          // Audio Pronunciation Bar on Front
          FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            FilledTonalButton(
              onClick = onSpeakNormal,
              shape = RoundedCornerShape(14.dp),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
              colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = StarPurple,
                contentColor = Color.White
              )
            ) {
              Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Say It", fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1, softWrap = false)
            }

            FilledTonalButton(
              onClick = onSpeakSlow,
              shape = RoundedCornerShape(14.dp),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
              colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = SkyBlueContainer,
                contentColor = OnSkyBlueContainer
              )
            ) {
              Text("🐢 Slow", fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1, softWrap = false)
            }

            FilledTonalButton(
              onClick = onSpellOut,
              shape = RoundedCornerShape(14.dp),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
              colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = SunshineYellowSoft,
                contentColor = OnSunshineYellow
              )
            ) {
              Icon(Icons.Default.RecordVoiceOver, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("A-B-C", fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1, softWrap = false)
            }
          }
        }
      }
    } else {
      // BACK OF FLASHCARD
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .defaultMinSize(minHeight = 320.dp)
          .graphicsLayer { rotationY = 180f }
          .background(
            Brush.verticalGradient(
              colors = listOf(Color.White, SkyBlueContainer.copy(alpha = 0.65f))
            )
          )
          .padding(18.dp)
      ) {
        Column(
          modifier = Modifier.fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Surface(
              color = SkyBlueContainer,
              shape = RoundedCornerShape(12.dp)
            ) {
              Text(
                text = "💡 Meaning & Sentence",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = OnSkyBlueContainer,
                maxLines = 1,
                softWrap = false
              )
            }
            Text(
              text = "Tap to flip ↺",
              style = MaterialTheme.typography.labelMedium,
              color = OnSkyBlueContainer.copy(alpha = 0.75f),
              fontWeight = FontWeight.SemiBold,
              maxLines = 1,
              softWrap = false
            )
          }

          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 4.dp)
          ) {
            Text(
              text = word.word,
              fontSize = 24.sp,
              fontWeight = FontWeight.ExtraBold,
              color = StarPurple,
              textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = word.definition,
              style = MaterialTheme.typography.bodyLarge,
              fontWeight = FontWeight.SemiBold,
              textAlign = TextAlign.Center,
              color = Color(0xFF1E1A2B)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
              color = Color.White.copy(alpha = 0.9f),
              shape = RoundedCornerShape(16.dp),
              border = BorderStroke(1.dp, SkyBlue.copy(alpha = 0.4f)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text(
                  text = "📖 Example Sentence",
                  style = MaterialTheme.typography.labelMedium,
                  fontWeight = FontWeight.Bold,
                  color = SkyBlue
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "\"${word.exampleSentence}\"",
                  style = MaterialTheme.typography.bodyMedium,
                  fontWeight = FontWeight.Medium,
                  textAlign = TextAlign.Center,
                  color = Color(0xFF2B263B)
                )
              }
            }
          }

          Button(
            onClick = onSpeakSentence,
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            modifier = Modifier.defaultMinSize(minHeight = 46.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = SkyBlue,
              contentColor = Color.White
            )
          ) {
            Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Read Word & Sentence",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              textAlign = TextAlign.Center
            )
          }
        }
      }
    }
  }
}

@Composable
fun StatusBadge(status: MasteryStatus) {
  val (bg, fg, text) = when (status) {
    MasteryStatus.MASTERED -> Triple(MintGreenSoft, OnMintGreen, "⭐ Mastered")
    MasteryStatus.PRACTICING -> Triple(SunshineYellowSoft, OnSunshineYellow, "🌱 Practicing")
    MasteryStatus.NEW -> Triple(StarPurpleContainer, StarPurple, "✨ New")
  }
  Surface(
    color = bg,
    shape = RoundedCornerShape(50)
  ) {
    Text(
      text = text,
      modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
      style = MaterialTheme.typography.labelMedium,
      fontWeight = FontWeight.Bold,
      color = fg,
      maxLines = 1,
      softWrap = false
    )
  }
}
