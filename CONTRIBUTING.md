# Contributing & Development Guide — Spelling Stars

Thank you for helping improve **Spelling Stars**! This guide covers common development workflows, how to expand the built-in kid dictionary, and how to verify UI layouts across devices and font scales.

---

## 1. Expanding the Built-In Kid Dictionary

When a parent enters a word that isn't in the built-in preset map, [`KidWordHelper.kt`](app/src/main/kotlin/com/spellingstars/app/data/KidWordHelper.kt) automatically estimates its syllables and provides a friendly fallback clue. To add curated definitions and sentences for new grade-level words:

1. Open [`app/src/main/kotlin/com/spellingstars/app/data/KidWordHelper.kt`](app/src/main/kotlin/com/spellingstars/app/data/KidWordHelper.kt).
2. Add an entry to `builtInWords`:
   ```kotlin
   "astronaut" to WordInfo(
     syllables = "as • tro • naut",
     definition = "A person trained to travel and work in outer space.",
     exampleSentence = "The astronaut waved from inside the space station."
   )
   ```
3. If the word has a famous kid misspelling, you can also add it to `addCommonKidMisspellings` in [`DistractorGenerator.kt`](app/src/main/kotlin/com/spellingstars/app/data/DistractorGenerator.kt).

---

## 2. Running Tests

Run the JUnit 4 unit test suite in [`MainScreenViewModelTest.kt`](app/src/test/kotlin/com/spellingstars/app/ui/main/MainScreenViewModelTest.kt) to verify:
- Look-alike distractor generation (`distractorGenerator_createsTwoUniqueLookAlikeWords`)
- Bulk word list parsing (`kidWordHelper_parsesBulkWordsAndAutoGeneratesInfo`)
- Repository state persistence & star calculation (`repository_addsBulkWordsAndTracksMasteryAndStars`)

```powershell
.\gradlew.bat testDebugUnitTest
```

---

## 3. UI & Accessibility Layout Guidelines (Pixel 7 / Pixel 10)

To ensure every screen looks great even when Android's system font size is scaled up to **130% (`fontScale = 1.3f`)**:
- **Never use fixed `.height(...)` on buttons or text cards.** Always use `Modifier.defaultMinSize(minHeight = ...)` so multi-line or large-scale text can expand vertically without clipping.
- **Use `FlowRow` for horizontal button groups and filter chips.** This allows buttons (like `Play Word`, `🐢 Slow`, `📖 Sentence`) to wrap onto a second line on compact widths (`360dp`) instead of crushing text vertically.
- **Use `Modifier.weight(1f)` on text columns inside `Row`s** whenever a trailing action button or badge sits on the right side.
- Check your changes by rendering the `@Preview` functions in [`LayoutPreviews.kt`](app/src/main/kotlin/com/spellingstars/app/ui/main/LayoutPreviews.kt).

