package com.spellingstars.app.ui.words

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spellingstars.app.data.DistractorGenerator
import com.spellingstars.app.data.KidWordHelper
import com.spellingstars.app.data.SpellingWord
import com.spellingstars.app.data.WeeklySpellingList
import com.spellingstars.app.ui.flashcards.StatusBadge
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
import com.spellingstars.app.ui.theme.SunshineYellowSoft

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WordManagerScreen(
  lists: List<WeeklySpellingList>,
  activeList: WeeklySpellingList,
  onSelectList: (String) -> Unit,
  onCreateWeeklyList: (String, String, String) -> Unit,
  onAddBulkWords: (String) -> Unit,
  onAddWord: (String, String, String) -> Unit,
  onUpdateWord: (String, String, String, String, String) -> Unit,
  onDeleteWord: (String) -> Unit,
  onSpeakWord: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var showBulkAddDialog by rememberSaveable { mutableStateOf(false) }
  var showSingleAddDialog by rememberSaveable { mutableStateOf(false) }
  var showNewWeekDialog by rememberSaveable { mutableStateOf(false) }
  var editingWord by remember { mutableStateOf<SpellingWord?>(null) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    // Weekly List Switcher Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = "📚 Weekly Spelling Lists",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.ExtraBold,
          color = Color(0xFF1E1A2B)
        )
        Text(
          text = "Add all your weekly words at once!",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      FilledTonalButton(
        onClick = { showNewWeekDialog = true },
        shape = RoundedCornerShape(14.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        modifier = Modifier.defaultMinSize(minHeight = 42.dp),
        colors = ButtonDefaults.filledTonalButtonColors(
          containerColor = SkyBlueContainer,
          contentColor = OnSkyBlueContainer
        )
      ) {
        Icon(Icons.Default.LibraryAdd, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "New Week",
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          maxLines = 1,
          softWrap = false
        )
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // List Selector Chips
    FlowRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      lists.forEach { list ->
        val selected = list.id == activeList.id
        FilterChip(
          selected = selected,
          onClick = { onSelectList(list.id) },
          label = {
            Text(
              text = "${list.themeEmoji} ${list.title} (${list.words.size})",
              fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = StarPurple,
            selectedLabelColor = Color.White
          )
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Primary Bulk Add Words Button + Secondary Single Word Button
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Button(
        onClick = { showBulkAddDialog = true },
        modifier = Modifier
          .weight(1.1f)
          .defaultMinSize(minHeight = 48.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(containerColor = StarPurple)
      ) {
        Icon(Icons.Default.PlaylistAdd, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Add Words",
          fontWeight = FontWeight.ExtraBold,
          fontSize = 14.sp,
          textAlign = TextAlign.Center,
          maxLines = 1,
          softWrap = false
        )
      }

      OutlinedButton(
        onClick = { showSingleAddDialog = true },
        modifier = Modifier
          .weight(1f)
          .defaultMinSize(minHeight = 48.dp),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.5.dp, StarPurple)
      ) {
        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "Custom Word",
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          textAlign = TextAlign.Center,
          maxLines = 1,
          softWrap = false
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "✨ Prepared Flashcards (${activeList.words.size} words)",
      style = MaterialTheme.typography.titleSmall,
      fontWeight = FontWeight.ExtraBold,
      color = StarPurple
    )
    Text(
      text = "Each card automatically includes syllables, definitions, sentences & 2 look-alike quiz words!",
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(8.dp))

    activeList.words.forEach { word ->
      val previewDistractors = remember(word.word) {
        DistractorGenerator.generateDistractors(word.word, count = 2)
      }
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.5.dp, StarPurpleContainer)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            IconButton(
              onClick = { onSpeakWord(word.word) },
              modifier = Modifier
                .size(38.dp)
                .background(StarPurpleContainer, CircleShape)
            ) {
              Icon(
                Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = "Speak ${word.word}",
                tint = StarPurple
              )
            }

            Column(modifier = Modifier.weight(1f)) {
              FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
              ) {
                Text(
                  text = word.word,
                  fontSize = 19.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = Color(0xFF1E1A2B)
                )
                Surface(
                  color = SunshineYellowSoft,
                  shape = RoundedCornerShape(50)
                ) {
                  Text(
                    text = word.syllables,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSunshineYellow,
                    maxLines = 1,
                    softWrap = false
                  )
                }
                StatusBadge(status = word.masteryStatus)
              }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
              IconButton(
                onClick = { editingWord = word },
                modifier = Modifier.size(36.dp)
              ) {
                Icon(Icons.Default.Edit, contentDescription = "Edit word", tint = SkyBlue, modifier = Modifier.size(20.dp))
              }
              IconButton(
                onClick = { onDeleteWord(word.id) },
                modifier = Modifier.size(36.dp)
              ) {
                Icon(Icons.Default.DeleteOutline, contentDescription = "Remove word", tint = CoralPink, modifier = Modifier.size(20.dp))
              }
            }
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = "💡 ${word.definition}",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2B263B)
          )
          Text(
            text = "📖 \"${word.exampleSentence}\"",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(6.dp))

          Surface(
            color = CoralPinkContainer.copy(alpha = 0.45f),
            shape = RoundedCornerShape(10.dp)
          ) {
            Text(
              text = "🎯 Quiz Look-Alikes: ${previewDistractors.joinToString(", ")}",
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = OnCoralPinkContainer
            )
          }
        }
      }
    }
  }

  if (showBulkAddDialog) {
    BulkAddWordsDialog(
      listTitle = activeList.title,
      onDismiss = { showBulkAddDialog = false },
      onConfirm = { rawWords ->
        onAddBulkWords(rawWords)
        showBulkAddDialog = false
      }
    )
  }

  if (showSingleAddDialog) {
    AddWordDialog(
      onDismiss = { showSingleAddDialog = false },
      onConfirm = { w, def, sent ->
        onAddWord(w, def, sent)
        showSingleAddDialog = false
      }
    )
  }

  if (showNewWeekDialog) {
    CreateWeeklyListDialog(
      nextWeekNumber = lists.size + 1,
      onDismiss = { showNewWeekDialog = false },
      onConfirm = { title, emoji, wordsRaw ->
        onCreateWeeklyList(title, emoji, wordsRaw)
        showNewWeekDialog = false
      }
    )
  }

  editingWord?.let { wordToEdit ->
    EditWordDialog(
      word = wordToEdit,
      onDismiss = { editingWord = null },
      onConfirm = { updatedWord, updatedDef, updatedSent, updatedSyl ->
        onUpdateWord(wordToEdit.id, updatedWord, updatedDef, updatedSent, updatedSyl)
        editingWord = null
      }
    )
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BulkAddWordsDialog(
  listTitle: String,
  onDismiss: () -> Unit,
  onConfirm: (String) -> Unit
) {
  var rawWords by rememberSaveable { mutableStateOf("") }
  val parsedPreview = remember(rawWords) {
    KidWordHelper.parseBulkWords(rawWords)
  }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text("📋 Add All Words at Once", fontWeight = FontWeight.ExtraBold)
    },
    text = {
      Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Text(
          text = "Type or paste your daughter's words for \"$listTitle\" below — one per line, or separated by commas or spaces!",
          style = MaterialTheme.typography.bodySmall
        )
        OutlinedTextField(
          value = rawWords,
          onValueChange = { rawWords = it },
          label = { Text("Type or paste words here") },
          placeholder = { Text("rainbow\numbrella\nwhisper\npromise") },
          minLines = 5,
          modifier = Modifier.fillMaxWidth()
        )

        if (parsedPreview.isNotEmpty()) {
          Surface(
            color = MintGreenSoft,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, MintGreen),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(10.dp)) {
              Text(
                text = "✅ Ready to create ${parsedPreview.size} flashcards:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.ExtraBold,
                color = OnMintGreen
              )
              Spacer(modifier = Modifier.height(6.dp))
              FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                parsedPreview.forEach { w ->
                  Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(8.dp)
                  ) {
                    Text(
                      text = w,
                      modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Bold,
                      color = StarPurple
                    )
                  }
                }
              }
            }
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = { onConfirm(rawWords) },
        enabled = parsedPreview.isNotEmpty()
      ) {
        Text("Add ${parsedPreview.size} Words")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}

@Composable
private fun AddWordDialog(
  onDismiss: () -> Unit,
  onConfirm: (String, String, String) -> Unit
) {
  var wordText by rememberSaveable { mutableStateOf("") }
  var definition by rememberSaveable { mutableStateOf("") }
  var sentence by rememberSaveable { mutableStateOf("") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text("✨ Add Single Word", fontWeight = FontWeight.ExtraBold)
    },
    text = {
      Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Text(
          text = "Type a word and optional custom clue. Leave the definition or sentence blank to auto-fill kid-friendly ones!",
          style = MaterialTheme.typography.bodySmall
        )
        OutlinedTextField(
          value = wordText,
          onValueChange = { wordText = it },
          label = { Text("Spelling Word *") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = definition,
          onValueChange = { definition = it },
          label = { Text("Definition (Optional)") },
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = sentence,
          onValueChange = { sentence = it },
          label = { Text("Example Sentence (Optional)") },
          modifier = Modifier.fillMaxWidth()
        )
      }
    },
    confirmButton = {
      Button(
        onClick = { onConfirm(wordText, definition, sentence) },
        enabled = wordText.isNotBlank()
      ) {
        Text("Create Flashcard")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CreateWeeklyListDialog(
  nextWeekNumber: Int,
  onDismiss: () -> Unit,
  onConfirm: (String, String, String) -> Unit
) {
  var title by rememberSaveable { mutableStateOf("Week $nextWeekNumber Spelling Words") }
  var emoji by rememberSaveable { mutableStateOf("🌈") }
  var rawWords by rememberSaveable { mutableStateOf("") }

  val parsedPreview = remember(rawWords) {
    KidWordHelper.parseBulkWords(rawWords)
  }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text("📚 New Weekly Word List", fontWeight = FontWeight.ExtraBold)
    },
    text = {
      Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Text(
          text = "Enter a name for the week and type or paste all the spelling words at once (one per line, or separated by commas or spaces)!",
          style = MaterialTheme.typography.bodySmall
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = emoji,
            onValueChange = { emoji = it },
            label = { Text("Icon") },
            singleLine = true,
            modifier = Modifier.width(72.dp)
          )
          OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Week Title") },
            singleLine = true,
            modifier = Modifier.weight(1f)
          )
        }
        OutlinedTextField(
          value = rawWords,
          onValueChange = { rawWords = it },
          label = { Text("All Spelling Words") },
          placeholder = { Text("e.g.\nrainbow\numbrella\nwhisper\npromise") },
          minLines = 5,
          modifier = Modifier.fillMaxWidth()
        )

        if (parsedPreview.isNotEmpty()) {
          Surface(
            color = MintGreenSoft,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, MintGreen),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(10.dp)) {
              Text(
                text = "✅ ${parsedPreview.size} words ready:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.ExtraBold,
                color = OnMintGreen
              )
              Spacer(modifier = Modifier.height(4.dp))
              FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                parsedPreview.forEach { w ->
                  Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(8.dp)
                  ) {
                    Text(
                      text = w,
                      modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Bold,
                      color = StarPurple
                    )
                  }
                }
              }
            }
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = { onConfirm(title, emoji, rawWords) },
        enabled = parsedPreview.isNotEmpty()
      ) {
        Text("Create (${parsedPreview.size} Words)")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}

@Composable
private fun EditWordDialog(
  word: SpellingWord,
  onDismiss: () -> Unit,
  onConfirm: (String, String, String, String) -> Unit
) {
  var wordText by rememberSaveable(word.id) { mutableStateOf(word.word) }
  var syllables by rememberSaveable(word.id) { mutableStateOf(word.syllables) }
  var definition by rememberSaveable(word.id) { mutableStateOf(word.definition) }
  var sentence by rememberSaveable(word.id) { mutableStateOf(word.exampleSentence) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("✏️ Edit Flashcard", fontWeight = FontWeight.ExtraBold) },
    text = {
      Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedTextField(
          value = wordText,
          onValueChange = { wordText = it },
          label = { Text("Word") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = syllables,
          onValueChange = { syllables = it },
          label = { Text("Syllables (e.g. be • cause)") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = definition,
          onValueChange = { definition = it },
          label = { Text("Kid-Friendly Definition") },
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = sentence,
          onValueChange = { sentence = it },
          label = { Text("Example Sentence") },
          modifier = Modifier.fillMaxWidth()
        )
      }
    },
    confirmButton = {
      Button(
        onClick = { onConfirm(wordText, definition, sentence, syllables) },
        enabled = wordText.isNotBlank()
      ) {
        Text("Save")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}
