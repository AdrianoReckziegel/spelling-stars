package com.spellingstars.app.data

/**
 * Built-in kid-friendly word dictionary & automatic sentence/syllable generator.
 * When a parent adds words for a weekly test, this helper automatically provides a
 * syllable breakdown, kid-friendly definition, and example sentence for every word.
 */
object KidWordHelper {

  data class WordInfo(
    val syllables: String,
    val definition: String,
    val exampleSentence: String
  )

  private val builtInWords = mapOf(
    "adventure" to WordInfo(
      syllables = "ad • ven • ture",
      definition = "An exciting or unusual experience or journey.",
      exampleSentence = "Camping in the backyard was a grand adventure!"
    ),
    "because" to WordInfo(
      syllables = "be • cause",
      definition = "For the reason that; since.",
      exampleSentence = "We wore our rain boots because there were big puddles outside."
    ),
    "beautiful" to WordInfo(
      syllables = "beau • ti • ful",
      definition = "Very pleasing and lovely to look at or listen to.",
      exampleSentence = "We saw a beautiful rainbow after the summer storm."
    ),
    "believe" to WordInfo(
      syllables = "be • lieve",
      definition = "To feel sure that something is true or real.",
      exampleSentence = "I believe you can get every word right on your test!"
    ),
    "butterfly" to WordInfo(
      syllables = "but • ter • fly",
      definition = "An insect with colorful wings that flies among flowers.",
      exampleSentence = "A bright orange butterfly landed gently on the flower."
    ),
    "calendar" to WordInfo(
      syllables = "cal • en • dar",
      definition = "A chart showing the days, weeks, and months of the year.",
      exampleSentence = "She put a gold star sticker on her wall calendar."
    ),
    "chocolate" to WordInfo(
      syllables = "choc • o • late",
      definition = "A sweet treat made from roasted cocoa beans.",
      exampleSentence = "Warm chocolate chip cookies are my favorite afternoon snack."
    ),
    "curious" to WordInfo(
      syllables = "cu • ri • ous",
      definition = "Eager to learn, explore, or know about something.",
      exampleSentence = "The curious kitten peeked inside the paper bag."
    ),
    "different" to WordInfo(
      syllables = "dif • fer • ent",
      definition = "Not the same as another; unique.",
      exampleSentence = "Every snowflake has a different and special pattern."
    ),
    "dinosaur" to WordInfo(
      syllables = "di • no • saur",
      definition = "A prehistoric reptile that lived millions of years ago.",
      exampleSentence = "We saw a giant dinosaur skeleton at the science museum."
    ),
    "discover" to WordInfo(
      syllables = "dis • cov • er",
      definition = "To find something for the first time.",
      exampleSentence = "Scientists love to discover new stars and planets in space."
    ),
    "elephant" to WordInfo(
      syllables = "el • e • phant",
      definition = "A very large gray mammal with a long trunk and big ears.",
      exampleSentence = "The baby elephant sprayed cool water with its trunk."
    ),
    "favorite" to WordInfo(
      syllables = "fa • vor • ite",
      definition = "Liked more than all others of the same kind.",
      exampleSentence = "Art class is her favorite part of the school week."
    ),
    "friend" to WordInfo(
      syllables = "friend",
      definition = "Someone you know well, like, and enjoy spending time with.",
      exampleSentence = "A good friend shares a smile and helps you out."
    ),
    "galaxy" to WordInfo(
      syllables = "gal • ax • y",
      definition = "A huge system of billions of stars and planets in space.",
      exampleSentence = "Our solar system is part of the Milky Way galaxy."
    ),
    "gravity" to WordInfo(
      syllables = "grav • i • ty",
      definition = "The invisible force that pulls objects toward the Earth.",
      exampleSentence = "Without gravity, we would all float around like astronauts!"
    ),
    "imagine" to WordInfo(
      syllables = "im • ag • ine",
      definition = "To form a picture or idea of something in your mind.",
      exampleSentence = "Close your eyes and imagine flying on the back of a dragon."
    ),
    "knowledge" to WordInfo(
      syllables = "knowl • edge",
      definition = "Information, understanding, and skills you gain by learning.",
      exampleSentence = "Reading books is a fun way to grow your knowledge."
    ),
    "laughter" to WordInfo(
      syllables = "laugh • ter",
      definition = "The happy sound of laughing.",
      exampleSentence = "The puppy's silly tricks filled the room with laughter."
    ),
    "library" to WordInfo(
      syllables = "li • brar • y",
      definition = "A place with lots of books you can read and borrow.",
      exampleSentence = "We checked out three mystery books from the school library."
    ),
    "mountain" to WordInfo(
      syllables = "moun • tain",
      definition = "A very tall natural hill that rises high into the sky.",
      exampleSentence = "Snow covered the top of the tall mountain."
    ),
    "neighbor" to WordInfo(
      syllables = "neigh • bor",
      definition = "A person who lives next door or near your home.",
      exampleSentence = "Our kind neighbor waved hello from across the street."
    ),
    "promise" to WordInfo(
      syllables = "prom • ise",
      definition = "Saying for sure that you will do something.",
      exampleSentence = "I promise to practice my spelling flashcards after dinner."
    ),
    "rainbow" to WordInfo(
      syllables = "rain • bow",
      definition = "An arch of bright colors in the sky caused by sunshine and rain.",
      exampleSentence = "Can you count all seven colors in the rainbow?"
    ),
    "separate" to WordInfo(
      syllables = "sep • a • rate",
      definition = "To divide or keep apart in different groups.",
      exampleSentence = "Let's separate the puzzle pieces by their colors first."
    ),
    "surprise" to WordInfo(
      syllables = "sur • prise",
      definition = "Something unexpected that brings excitement or wonder.",
      exampleSentence = "We planned a fun surprise party for Grandma's birthday."
    ),
    "telescope" to WordInfo(
      syllables = "tel • e • scope",
      definition = "A tool with lenses that makes faraway stars and planets look closer.",
      exampleSentence = "Through the telescope, we could see the rings of Saturn!"
    ),
    "together" to WordInfo(
      syllables = "to • geth • er",
      definition = "With each other; in one group.",
      exampleSentence = "We built a giant sandcastle together at the beach."
    ),
    "tomorrow" to WordInfo(
      syllables = "to • mor • row",
      definition = "On the day after today.",
      exampleSentence = "Pack your backpack tonight so you are ready for school tomorrow."
    ),
    "treasure" to WordInfo(
      syllables = "treas • ure",
      definition = "Valuable things like gold, jewels, or something very special to you.",
      exampleSentence = "The pirates followed the map to find the hidden treasure chest."
    ),
    "umbrella" to WordInfo(
      syllables = "um • brel • la",
      definition = "A folding canopy you hold over your head to stay dry in the rain.",
      exampleSentence = "She opened her yellow polka-dot umbrella when the rain started."
    ),
    "vacation" to WordInfo(
      syllables = "va • ca • tion",
      definition = "A special time away from school or work to relax or travel.",
      exampleSentence = "Our family is going to the lake for summer vacation."
    ),
    "weather" to WordInfo(
      syllables = "weath • er",
      definition = "What the sky and air are like outside, such as sunny, rainy, or windy.",
      exampleSentence = "Sunny weather is perfect for riding bikes in the park."
    ),
    "whisper" to WordInfo(
      syllables = "whis • per",
      definition = "To speak very softly using your breath instead of a loud voice.",
      exampleSentence = "Please whisper in the library so others can read quietly."
    ),
    "wonderful" to WordInfo(
      syllables = "won • der • ful",
      definition = "Extremely good, marvelous, and delightful.",
      exampleSentence = "You did a wonderful job practicing your spelling words today!"
    )
  )

  fun enrichWord(
    rawWord: String,
    customDefinition: String = "",
    customSentence: String = "",
    customSyllables: String = ""
  ): WordInfo {
    val clean = rawWord.trim()
    val preset = builtInWords[clean.lowercase()]
    val syllables = when {
      customSyllables.isNotBlank() -> customSyllables.trim()
      preset != null -> preset.syllables
      else -> estimateSyllables(clean)
    }
    val definition = when {
      customDefinition.isNotBlank() -> customDefinition.trim()
      preset != null -> preset.definition
      else -> "A great vocabulary word to master for this week's spelling test! (${clean.length} letters)"
    }
    val sentence = when {
      customSentence.isNotBlank() -> customSentence.trim()
      preset != null -> preset.exampleSentence
      else -> "I practiced how to spell \"$clean\" on my flashcard today."
    }
    return WordInfo(syllables = syllables, definition = definition, exampleSentence = sentence)
  }

  /**
   * Parses a bulk string containing multiple words (separated by newlines, commas, semicolons, or spaces)
   * into clean, unique words.
   */
  fun parseBulkWords(rawInput: String): List<String> {
    return rawInput
      .split(Regex("[\\n,;]+"))
      .flatMap { line ->
        val trimmed = line.trim()
        // If someone typed space-separated single words on one line, split them cleanly
        if (trimmed.contains(" ")) {
          trimmed.split(Regex("\\s+"))
        } else {
          listOf(trimmed)
        }
      }
      .map { it.trim().trim('.', '!', '?', '"', '\'', '-', '•', '*') }
      .filter { it.isNotEmpty() && it.any { ch -> ch.isLetter() } }
      .distinctBy { it.lowercase() }
  }

  private fun estimateSyllables(word: String): String {
    if (word.length <= 4) return word.lowercase()
    val vowels = "aeiouyAEIOUY"
    val parts = mutableListOf<String>()
    var current = StringBuilder()
    var i = 0
    while (i < word.length) {
      current.append(word[i])
      val isVowel = word[i] in vowels
      val hasNext = i + 2 < word.length
      if (isVowel && hasNext && word[i + 1] !in vowels && current.length >= 2 && word.length - i > 2) {
        if (i + 2 < word.length && word[i + 2] !in vowels && word.length - i > 3) {
          current.append(word[i + 1])
          i++
        }
        parts.add(current.toString())
        current = StringBuilder()
      }
      i++
    }
    if (current.isNotEmpty()) {
      if (parts.isNotEmpty() && current.length == 1) {
        val last = parts.removeAt(parts.lastIndex)
        parts.add(last + current.toString())
      } else {
        parts.add(current.toString())
      }
    }
    return parts.joinToString(" • ").lowercase()
  }

  fun starterWeeklyLists(): List<WeeklySpellingList> {
    val week1Words = listOf(
      "because", "friend", "believe", "beautiful", "adventure",
      "butterfly", "surprise", "different", "together", "tomorrow",
      "library", "favorite"
    ).mapIndexed { index, w ->
      val info = enrichWord(w)
      SpellingWord(
        id = "w1_$index",
        word = w,
        definition = info.definition,
        exampleSentence = info.exampleSentence,
        syllables = info.syllables,
        masteryStatus = when (index) {
          0, 1, 2 -> MasteryStatus.MASTERED
          3, 4 -> MasteryStatus.PRACTICING
          else -> MasteryStatus.NEW
        },
        correctStreak = if (index < 3) 3 else if (index < 5) 1 else 0,
        practiceCount = if (index < 5) 2 else 0
      )
    }

    val week2Words = listOf(
      "galaxy", "telescope", "gravity", "mountain", "dinosaur",
      "discover", "curious", "treasure", "knowledge", "weather"
    ).mapIndexed { index, w ->
      val info = enrichWord(w)
      SpellingWord(
        id = "w2_$index",
        word = w,
        definition = info.definition,
        exampleSentence = info.exampleSentence,
        syllables = info.syllables,
        masteryStatus = MasteryStatus.NEW
      )
    }

    return listOf(
      WeeklySpellingList(
        id = "week_1",
        title = "Week 1: Everyday Champions",
        weekLabel = "12 Spelling Words",
        themeEmoji = "🦄",
        words = week1Words
      ),
      WeeklySpellingList(
        id = "week_2",
        title = "Week 2: Science & Discovery",
        weekLabel = "10 Spelling Words",
        themeEmoji = "🚀",
        words = week2Words
      )
    )
  }
}
