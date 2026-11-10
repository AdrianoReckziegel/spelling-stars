# Privacy Policy & Children's Data Safety — Spelling Stars

**Effective Date:** November 2026  
**Application Name:** Spelling Stars (`com.spellingstars.app`)  
**Target Audience:** Elementary school students (ages 6–10) and parents/guardians.

---

## 1. Summary: 100% Offline & Private by Design

**Spelling Stars** was built specifically for family use to help children study for weekly school spelling tests.
- **Zero Data Collection:** The app does **not** collect, transmit, store, or share any personal information, usage telemetry, audio recordings, or analytics on external servers.
- **Zero Internet Access Required:** The app does not request the `android.permission.INTERNET` permission in [`AndroidManifest.xml`](../app/src/main/AndroidManifest.xml). It cannot connect to the internet.
- **Zero Ads or In-App Purchases:** There are no third-party advertising SDKs, trackers, or external links.

---

## 2. How Data is Stored

All spelling lists, custom words, definitions, example sentences, mastery statuses, and earned study stars are stored **exclusively on your Android device** inside the application's private internal storage directory:
- **File Path:** `<App Internal Storage>/files/spelling_stars_state_v2.json`
- **Access Control:** Protected by Android's application sandbox; no other apps on the device can read or modify this file.
- **Deletion:** Uninstalling the app or clearing app storage in Android Settings permanently deletes all saved word lists and progress.

---

## 3. Text-to-Speech (TTS) Audio

Pronunciation, slow speech, and letter-by-letter dictation use Android's built-in on-device `android.speech.tts.TextToSpeech` engine (`Locale.US`). No voice audio is recorded from the microphone, and no text is sent to cloud speech servers by the application.

---

## 4. COPPA & Google Play Families Compliance

Because **Spelling Stars** operates completely offline, collects no personal identifiers, requires no user accounts, and includes no third-party SDKs, it complies with the Children's Online Privacy Protection Act (COPPA) and Google Play's Families Policy requirements.

