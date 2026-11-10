# Changelog — Spelling Stars

All notable changes to **Spelling Stars** are documented in this file.

---

## [1.2.0] — 2026-11-07
### Added
- **Bulk Word Entry (`Add Words` & `New Week`):** Parents can now paste or type an entire weekly word list at once (separated by new lines, commas, semicolons, or spaces) with a live preview of detected word chips.
- **Active List Mastery Header on Flashcards:** Added a top summary banner on the **Cards** tab showing the active weekly list title, icon, and mastered count (`3/12 ⭐`).
- **Mastered Filter Chip:** Added `All Words`, `Learning`, and `Mastered ⭐` filter chips on the Flashcards screen.

### Removed
- **Monday–Friday Progression Tab:** Removed the daily schedule/weekday assignment feature to keep the app simple, direct, and focused on flashcards, quizzes, and word lists.

---

## [1.1.0] — 2026-11-07
### Fixed
- **Full Responsive Layout & `1.3x` Font Scale Overhaul (Pixel 10 & Pixel 7):**
  - Replaced fixed button and card heights with `defaultMinSize(minHeight = ...)` across all screens so enlarged text never clips vertically.
  - Converted audio control bars, filter chips, and word metadata tags to `FlowRow` so buttons wrap cleanly instead of squeezing into narrow vertical columns.
  - Replaced Material3 `SingleChoiceSegmentedButtonRow` in the Practice Quiz with an adaptive two-pill mode switcher.

---

## [1.0.0] — 2026-11-06
### Added
- Initial release of **Spelling Stars** (`com.spellingstars.app`) built with Kotlin & Jetpack Compose.
- Interactive 3D flip flashcards with syllable chunking, vowel/consonant color-coded tiles, and Android Text-to-Speech (`Say It`, `🐢 Slow`, `A-B-C`).
- **Pick Word** multiple-choice dictation quiz with [`DistractorGenerator`](app/src/main/kotlin/com/spellingstars/app/data/DistractorGenerator.kt) creating 2 realistic look-alike misspellings per word.
- **Listen & Type** dictation quiz with letter-by-letter visual feedback and optional first/last letter hints.
- Local JSON storage via [`SpellingRepository`](app/src/main/kotlin/com/spellingstars/app/data/SpellingRepository.kt).

