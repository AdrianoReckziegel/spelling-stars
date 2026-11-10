package com.spellingstars.app.data

import kotlin.random.Random

/**
 * Generates plausible, kid-appropriate spelling distractors (misspellings) for a target word.
 * Used in the "Listen & Pick" multiple-choice dictation quiz so that all choices look realistic
 * and test genuine spelling patterns (double letters, vowel swaps, silent letters, transpositions).
 */
object DistractorGenerator {

  private val phoneticReplacements = listOf(
    "ee" to "ea",
    "ea" to "ee",
    "ie" to "ei",
    "ei" to "ie",
    "ai" to "ay",
    "ay" to "ai",
    "oa" to "ow",
    "ow" to "oa",
    "ou" to "ow",
    "oo" to "u",
    "au" to "aw",
    "aw" to "au",
    "igh" to "ite",
    "ight" to "ite",
    "tion" to "shun",
    "sion" to "shun",
    "ph" to "f",
    "kn" to "n",
    "wr" to "r",
    "wh" to "w",
    "ck" to "k",
    "dg" to "j",
    "ge" to "j",
    "ce" to "se",
    "se" to "ce",
    "ci" to "si",
    "cy" to "sy",
    "le" to "el",
    "el" to "le",
    "al" to "el",
    "er" to "ur",
    "ur" to "er",
    "ir" to "er",
    "or" to "er",
    "ar" to "er"
  )

  private val doubleConsonants = listOf('b', 'c', 'd', 'f', 'g', 'l', 'm', 'n', 'p', 'r', 's', 't', 'z')
  private val vowels = listOf('a', 'e', 'i', 'o', 'u')

  /**
   * Returns a list of [count] unique distractors that do not match [targetWord] (case-insensitive).
   */
  fun generateDistractors(targetWord: String, count: Int = 2): List<String> {
    val clean = targetWord.trim()
    if (clean.length <= 1) {
      return listOf("${clean}e", "${clean}s").take(count)
    }

    val lower = clean.lowercase()
    val candidates = linkedSetOf<String>()

    // 1. Phonetic / orthographic pattern swaps (e.g., because -> becuase, friend -> freind)
    addCommonKidMisspellings(lower, candidates)

    for ((pattern, replacement) in phoneticReplacements) {
      val index = lower.indexOf(pattern)
      if (index >= 0) {
        val modified = lower.substring(0, index) + replacement + lower.substring(index + pattern.length)
        if (modified != lower && modified.length >= 2) {
          candidates.add(matchCase(clean, modified))
        }
      }
    }

    // 2. Deduplicate existing double letters (e.g., "balloon" -> "baloon", "happy" -> "hapy")
    for (i in 0 until lower.length - 1) {
      if (lower[i] == lower[i + 1]) {
        val single = lower.removeRange(i, i + 1)
        if (single != lower && single.length >= 2) {
          candidates.add(matchCase(clean, single))
        }
      }
    }

    // 3. Double a single internal consonant (e.g., "habit" -> "habbit", "planet" -> "plannet")
    for (i in 1 until lower.length - 1) {
      val ch = lower[i]
      if (ch in doubleConsonants && lower[i - 1] != ch && lower[i + 1] != ch) {
        val doubled = lower.substring(0, i + 1) + ch + lower.substring(i + 1)
        if (doubled != lower) {
          candidates.add(matchCase(clean, doubled))
        }
      }
    }

    // 4. Drop or add silent trailing 'e' (e.g., "believe" -> "believ", "planet" -> "planete")
    if (lower.endsWith("e") && !lower.endsWith("ee") && lower.length > 3) {
      candidates.add(matchCase(clean, lower.dropLast(1)))
    } else if (!lower.endsWith("e") && lower.last() in doubleConsonants) {
      candidates.add(matchCase(clean, "${lower}e"))
    }

    // 5. Transpose two adjacent internal characters (e.g., "because" -> "becuase", "friend" -> "freind")
    for (i in 1 until lower.length - 1) {
      if (lower[i] != lower[i + 1]) {
        val chars = lower.toCharArray()
        val tmp = chars[i]
        chars[i] = chars[i + 1]
        chars[i + 1] = tmp
        val swapped = String(chars)
        if (swapped != lower) {
          candidates.add(matchCase(clean, swapped))
        }
      }
    }

    // 6. Vowel substitution in the middle of the word
    for (i in 1 until lower.length - 1) {
      val ch = lower[i]
      if (ch in vowels) {
        val altVowel = when (ch) {
          'a' -> 'e'
          'e' -> 'i'
          'i' -> 'e'
          'o' -> 'u'
          'u' -> 'o'
          else -> 'a'
        }
        val replaced = lower.substring(0, i) + altVowel + lower.substring(i + 1)
        if (replaced != lower) {
          candidates.add(matchCase(clean, replaced))
        }
      }
    }

    // Fallback if word is very short
    if (candidates.size < count) {
      val chars = lower.toCharArray()
      if (chars.size >= 2) {
        val last = chars.size - 1
        val tmp = chars[last]
        chars[last] = chars[last - 1]
        chars[last - 1] = tmp
        val swapped = String(chars)
        if (swapped != lower) candidates.add(matchCase(clean, swapped))
      }
      candidates.add(matchCase(clean, "${lower}s"))
      candidates.add(matchCase(clean, "${lower}e"))
    }

    return candidates
      .filter { !it.equals(clean, ignoreCase = true) }
      .take(count)
  }

  /**
   * Builds a shuffled list of choices containing the [targetWord] and [distractorCount] distractors.
   */
  fun buildQuizChoices(targetWord: String, distractorCount: Int = 2, random: Random = Random.Default): List<String> {
    val distractors = generateDistractors(targetWord, distractorCount)
    return (listOf(targetWord.trim()) + distractors).shuffled(random)
  }

  private fun addCommonKidMisspellings(lower: String, out: MutableSet<String>) {
    val specificMap = mapOf(
      "because" to listOf("becuase", "becasue"),
      "friend" to listOf("freind", "frend"),
      "believe" to listOf("beleive", "belive"),
      "receive" to listOf("recieve", "receve"),
      "separate" to listOf("seperate", "seprate"),
      "definitely" to listOf("definately", "definitly"),
      "beautiful" to listOf("beutiful", "beautifull"),
      "different" to listOf("diffrent", "diferent"),
      "favorite" to listOf("favrite", "favourit"),
      "together" to listOf("togeather", "togather"),
      "surprise" to listOf("suprise", "surprize"),
      "tomorrow" to listOf("tommorow", "tomorow"),
      "library" to listOf("libary", "librery"),
      "calendar" to listOf("calender", "calandar"),
      "February" to listOf("Febuary", "Februry"),
      "wednesday" to listOf("wensday", "wednsday"),
      "adventure" to listOf("advenchure", "adventur"),
      "butterfly" to listOf("buterfly", "butterflie"),
      "mountain" to listOf("mountin", "mountan"),
      "laughter" to listOf("lafter", "laugter"),
      "knowledge" to listOf("knowlege", "noledge"),
      "treasure" to listOf("tresure", "treashure"),
      "astronaut" to listOf("astronot", "astranaut"),
      "dinosaur" to listOf("dinasaur", "dinosor"),
      "chocolate" to listOf("choclate", "chocolat")
    )
    specificMap[lower]?.forEach { out.add(it) }
  }

  private fun matchCase(original: String, generatedLower: String): String {
    if (original.isEmpty() || generatedLower.isEmpty()) return generatedLower
    return if (original[0].isUpperCase()) {
      generatedLower.replaceFirstChar { it.uppercase() }
    } else {
      generatedLower
    }
  }
}

