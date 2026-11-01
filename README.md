# SANKALP – Discipline & Habit Tracker

> *"Discipline builds destiny. What you conquer in private defines who you become in public."*

**SANKALP** is a native Android application engineered for individuals committed to forging unwavering personal discipline, building positive habits, maintaining daily consistency, and mastering focus. Designed with a military-inspired aesthetic, the app provides a focused, tactical environment free of distractions, operating 100% offline with local Room database persistence.

---

## 📌 Project Overview & Purpose

Modern habit trackers are often cluttered with social feeds, complex subscriptions, or unnecessary gamification. **SANKALP** takes a disciplined, tactical approach:
- Treats daily habits as critical missions.
- Visualizes unbroken streaks and operational readiness.
- Provides deep concentration tools via a built-in Tactical Focus Timer.
- Ensures total privacy and reliability by running locally and offline.

---

## ⚔️ Key Features

### 1. Mission Briefing (Welcome Onboarding)
- Military insignia crest and core doctrine presentation.
- Personalized callsign and rank entry (*Commander / Soldier*).
- One-tap seamless entry to the HQ Dashboard.

### 2. HQ Dashboard
- Real-time operational overview with today's formatted date and personalized greeting.
- Dual progress metrics: dynamic circular percentage gauge and linear progress bar.
- Daily habit completion counter (*completed vs. total*).
- Unbroken active streak counter with flame indicator.
- Interactive today's habit checklist for swift execution and completion toggling.
- Daily rotating motivational quote with one-tap favorite bookmarking.

### 3. Habit Arsenal (Habit Management)
- **Full CRUD Support**: Create, inspect, edit, and delete habits.
- **Categorization**: Tactical categories including *Discipline*, *Physical*, *Mind*, *Study*, *Nutrition*, and *Routine*.
- **Flexible Scheduling**: Supports *Daily*, *Weekdays*, and *Weekends*.
- **Pre-configured Military Standards**: Ships with foundational habits:
  - Wake Up Early (05:00 AM)
  - Physical Drill / Exercise (06:30 AM)
  - Hydration (3 Liters Water)
  - Read 30 Minutes
  - Deep Study / Skill Mastery
  - Mind Silence / Meditation
- **Objective Dossier**: Inspection dialog displaying current streak, longest streak, and lifetime completion count.

### 4. Streak & Consistency Tracking
- Automatic calculation of consecutive completion days.
- Distinguishes active days from missed days without corrupting historical logs.
- Calculates both global active streak and per-habit records.

### 5. Progress & Telemetry
- **Weekly Analytics**:
  - Interactive 7-day bar chart showing daily completion percentages.
  - Identification of the best-performing day of the week.
  - 7-day average execution percentage and total weekly completions counter.
  - Detailed daily mission breakdown list for the past 7 days.
- **Monthly Calendar**:
  - Full-month grid navigation (previous and next month switching).
  - Status color dots highlighting flawless (gold) and partial (military green) execution.
  - Date drill-down: select any past date to inspect or toggle completed habits.

### 6. Tactical Focus Timer
- Interval presets: **25 Minutes (Tactical Focus)** and **45 Minutes (Deep Drill)**.
- **Custom Duration**: Flexible duration selection (1 to 180 minutes) with custom mission tags.
- Full state controls: *Commence Drill (Start)*, *Hold (Pause)*, *Resume*, and *Abort (Reset)*.
- High-priority completion notification and haptic vibration sequence upon session completion.
- Focus session history log and lifetime focus minutes tracker.

### 7. Motivational Doctrine & Quotes
- Curated collection of original military, philosophical, and stoic quotes.
- Automatic daily rotation based on day of the year.
- Bookmark and view saved favorite quotes in a dedicated dialog.

### 8. Reminders & Local Notifications
- Configurable per-habit reminder times with local `AlarmManager` and `BroadcastReceiver`.
- Scheduled background sync checks via `WorkManager`.
- Android 13+ `POST_NOTIFICATIONS` runtime permission handling.

### 9. Settings & Protocols
- Callsign and commander name customization.
- Daily mission notification preferences toggle.
- Saved favorite quotes manager.
- Permanent data purge and reset option with confirmation safeguard and default re-seeding.

---

## 🛠️ Technologies & Architecture

- **Language**: [Kotlin](https://kotlinlang.org/) (100%)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material Design 3 (M3)
- **Architecture**: Model-View-ViewModel (MVVM) with Repository Pattern
- **Local Persistence**: [Room Database](https://developer.android.com/training/data-storage/room) with SQLite & KSP
- **Asynchronous & Reactive**: Kotlin Coroutines & `StateFlow` / `Flow`
- **Navigation**: [Jetpack Navigation Compose](https://developer.android.com/guide/navigation)
- **Background & Scheduling**: [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager) and `AlarmManager`
- **Testing**: [Robolectric](https://robolectric.org/) and JUnit4 for local JVM unit testing

---

## 🎨 Design & Color Palette

SANKALP utilizes a high-contrast, distraction-free military aesthetic with subtle tactile borders and dark surfaces:

| Element | Color Name | Hex Code | Preview |
| :--- | :--- | :--- | :--- |
| **Primary Background** | Charcoal Black | `#101210` | ![#101210](https://placehold.co/15x15/101210/101210.png) |
| **Secondary Background / Cards** | Dark Olive | `#20251C` | ![#20251C](https://placehold.co/15x15/20251C/20251C.png) |
| **Primary Accent** | Muted Military Green | `#78865A` | ![#78865A](https://placehold.co/15x15/78865A/78865A.png) |
| **Active Accent** | Military Green Bright | `#8FA85F` | ![#8FA85F](https://placehold.co/15x15/8FA85F/8FA85F.png) |
| **Secondary Accent** | Gold / Brass | `#C5A45B` | ![#C5A45B](https://placehold.co/15x15/C5A45B/C5A45B.png) |
| **Primary Text** | Off-White | `#F1F1E9` | ![#F1F1E9](https://placehold.co/15x15/F1F1E9/F1F1E9.png) |
| **Secondary Text** | Muted Grey | `#A4A69C` | ![#A4A69C](https://placehold.co/15x15/A4A69C/A4A69C.png) |
| **Card Borders** | Olive Border | `#2E3529` | ![#2E3529](https://placehold.co/15x15/2E3529/2E3529.png) |

---

## 🚀 Installation & Setup Instructions

### Prerequisites
- Android Studio Ladybug (or newer)
- Android SDK 36 (Minimum SDK: 26)
- JDK 17 or JDK 21

### Setup Steps
1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/sankalp-habit-tracker.git
   cd sankalp-habit-tracker
   ```
2. **Open in Android Studio**:
   - Launch Android Studio.
   - Select **Open** and choose the project root folder.
3. **Gradle Sync**:
   - Allow Android Studio to sync Gradle dependencies automatically.
   - Ensure the KSP and Compose compiler plugins resolve without issues.

---

## 📱 How to Run the Application

### Via Android Studio
1. Connect a physical Android device (Android 8.0+ / API 26+) with USB debugging enabled, or start an Android Virtual Device (AVD).
2. Select the `app` run configuration in the top toolbar.
3. Click the **Run** button (or press `Shift + F10`).

### Via Command Line
To build the debug APK:
```bash
gradle assembleDebug
```
The APK will be generated at:
```
app/build/outputs/apk/debug/app-debug.apk
```

To run local unit and Robolectric tests:
```bash
gradle :app:testDebugUnitTest
```

---

## 🚦 Project Status

**Status**: `Under Development` 🛠️

Core MVP functionality—including offline Room database operations, habit checklist management, streak tracking, weekly/monthly analytics, focus timer with vibration and notifications, and settings—is fully functional and verified.

---

## 🔮 Future Improvements (Planned Features)

The following features are planned for upcoming releases:
- [ ] **Data Export & Import**: Backup habit history and focus logs to JSON / CSV.
- [ ] **Home Screen App Widget**: Quick-glance habit checklist and streak widget.
- [ ] **Sound Packs**: Optional military bugle / bell chimes for focus session completion.
- [ ] **Biometric Lock**: Optional fingerprint/face unlock to secure personal habit logs.
- [ ] **Wear OS Companion**: Quick habit checking directly from a smartwatch.

---

## 👨‍💻 Developer Information

- **Project Name**: SANKALP – Discipline & Habit Tracker
- **Application ID**: `com.aistudio.sankalp.htrk`
- **Architecture**: Clean Architecture / MVVM (Kotlin + Jetpack Compose)
- **License**: Proprietary / Educational
