# 🌟 Spelling Stars — Weekly Spelling Flashcards & Audio Dictation App

<p align="center">
  <img src="docs/screenshots/app_icon.png" alt="Spelling Stars App Icon" width="140" />
</p>

**Spelling Stars** is a native Android application built with **Kotlin** and **Jetpack Compose (Material 3)** for elementary school students (ages 6–10) and their parents. It turns the weekly school spelling list into interactive 3D flip flashcards, Text-to-Speech (TTS) audio dictation, and look-alike spelling quizzes in seconds.

---

## 📱 App Screens

| 🃏 3D Flip Flashcards (`Cards`) | 🎯 Look-Alike Quiz (`Pick Word`) | ⌨️ Dictation Quiz (`Listen & Type`) | 📚 Bulk Word Manager (`Words`) |
| :---: | :---: | :---: | :---: |
| ![Flashcards Screen](docs/screenshots/01_flashcards_screen.png) | ![Pick Word Quiz Screen](docs/screenshots/02_quiz_pick_word.png) | ![Listen and Type Quiz Screen](docs/screenshots/03_quiz_listen_type.png) | ![Word Manager Screen](docs/screenshots/04_word_list_manager.png) |
| Interactive 3D flip card with syllable chunks, vowel/consonant color tiles, and **Say It / 🐢 Slow / A-B-C** audio. | Speaks the word aloud (with unlimited replays) and offers **2 realistic look-alike misspellings** alongside the real word. | Dictation practice with auto-correct disabled, optional first/last letter hints, and letter-by-letter feedback. | Paste or type **all weekly words at once** to auto-generate flashcards, definitions, sentences, and quiz look-alikes. |

---

## ✨ Key Features

- **⚡ Instant Weekly Preparation (Add All Words at Once):**
  - Tap **`+ New Week`** or **`Add Words`** in the **Words** tab and paste or type all of your child's spelling words in one go (separated by new lines, commas, or spaces).
  - Automatically generates **syllable breakdowns** (e.g., `be • cause`), **kid-friendly definitions**, **example sentences**, and **2 plausible look-alike quiz distractors** for every word.
- **🃏 Interactive 3D Flip Flashcards:**
  - **Front:** Large typography, syllable chunk pill, color-coded letter tiles (pink vowels & blue consonants to build visual memory), and three speech buttons:
    - **`🔊 Say It`** — Normal-speed pronunciation.
    - **`🐢 Slow`** — Slow-motion pronunciation (`0.58x` speed).
    - **`🗣️ A-B-C`** — Says the word, spells every letter out loud (`b, e, c, a, u, s, e`), and repeats the word.
  - **Back:** 3D Y-axis flip animation revealing the word's meaning, an example sentence, and a **`🔊 Read Word & Sentence`** button.
  - **Self-Check & Filtering:** Kids can filter by **All Words**, **Learning**, or **Mastered ⭐**, and earn **Stars (⭐)** as they master each card.
- **🎯 "Pick Word" Multiple-Choice Dictation Quiz:**
  - Automatically speaks the target word and provides **`🔊 Play Word`**, **`🐢 Slow`**, and **`📖 Sentence`** buttons for unlimited replays, plus a blanked-out context sentence clue (`"We wore our rain boots _______ there were big puddles outside."`).
  - Uses [`DistractorGenerator`](app/src/main/kotlin/com/spellingstars/app/data/DistractorGenerator.kt) to create **2 realistic, kid-plausible misspellings** (e.g., `because` → `becuase`, `becasue`; `butterfly` → `buterfly`, `butterflie`) so the child practices spotting genuine spelling traps.
- **⌨️ "Listen & Type" Spelling Test Mode:**
  - Lets the child type the word from audio dictation with keyboard auto-correct disabled, an optional first/last letter hint (`b _ _ _ _ _ e`), and a letter-by-letter green/pink comparison grid.
- **🔒 100% Offline & Kid-Safe:**
  - Zero internet permissions, zero ads, zero tracking, and local JSON persistence (`spelling_stars_state_v2.json`).
- **📐 Responsive Across Devices & Large Font Scales:**
  - Designed and verified for **Google Pixel 7** and **Google Pixel 10** screen sizes, including Android Accessibility Font Scales up to **`1.3x` (130%)**.

---

## 📖 Documentation Index

Full project and user documentation is included in the [`docs/`](docs/) directory:

| Document | Description |
| :--- | :--- |
| [**📘 Parent & Kid User Guide (`docs/USER_GUIDE.md`)**](docs/USER_GUIDE.md) | Step-by-step guide for parents preparing weekly lists in bulk and kids using flashcards & quizzes. |
| [**🏗️ Architecture & Technical Guide (`docs/ARCHITECTURE.md`)**](docs/ARCHITECTURE.md) | Project structure, Mermaid architecture diagram, look-alike word generation algorithm, and responsive UI patterns. |
| [**🔒 Privacy Policy & Data Safety (`docs/PRIVACY_POLICY.md`)**](docs/PRIVACY_POLICY.md) | Offline data storage details, permissions overview, and COPPA / Families compliance statement. |
| [**🤝 Contributing & Development (`CONTRIBUTING.md`)**](CONTRIBUTING.md) | Instructions for adding built-in vocabulary words, running unit tests, and rendering Compose previews. |
| [**📋 Changelog (`CHANGELOG.md`)**](CHANGELOG.md) | Version history and release notes. |

---

## 🚀 Getting Started (Build & Install)

### Prerequisites
- **JDK 17+**
- **Android SDK** (`compileSdk 36`, `minSdk 24`)

### 1. Run Unit Tests & Build the Debug APK
```powershell
cd C:\Users\adria\.gemini\antigravity\scratch\spelling-stars
.\gradlew.bat testDebugUnitTest assembleDebug
```
The compiled APK is generated at:
`app/build/outputs/apk/debug/app-debug.apk`

### 2. Install on a Google Pixel via Wireless Debugging
1. On your Pixel, go to **Settings → System → Developer options → Wireless debugging** and note the `IP:Port`.
2. Connect and install via `adb`:
```powershell
adb connect <PIXEL_IP>:<PORT>
adb -s <PIXEL_IP>:<PORT> install -r app\build\outputs\apk\debug\app-debug.apk
adb -s <PIXEL_IP>:<PORT> shell am start -n com.spellingstars.app/.MainActivity
```

### 3. Render Multi-Device Compose Previews (No Device Required)
Using the Android CLI preview renderer:
```powershell
android preview render --daemon -o build/previews "gradle://.?:app@debug" `
  --fqn "com.spellingstars.app.ui.main.LayoutPreviewsKt.FlashcardsPreview" `
  --fqn "com.spellingstars.app.ui.main.LayoutPreviewsKt.QuizMultipleChoicePreview" `
  --fqn "com.spellingstars.app.ui.main.LayoutPreviewsKt.QuizListenTypePreview" `
  --fqn "com.spellingstars.app.ui.main.LayoutPreviewsKt.WordManagerPreview"
```

