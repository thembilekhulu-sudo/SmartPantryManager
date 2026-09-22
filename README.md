# Smart Pantry Manager

A native Android app for tracking pantry inventory and suggesting recipes you can actually make with what you have on hand.

## Description

Smart Pantry Manager lets users add, edit, and delete pantry items (with quantities), and browse a library of recipes. Its core feature is a **strict-matching recipe suggestion engine**: a recipe is only suggested if *every* required ingredient is present in the pantry in sufficient quantity — no partial matches. Ingredient name comparison is normalized for case, whitespace, and basic singular/plural forms (e.g. "tomato" vs "tomatoes") so near-identical entries still match correctly.

### Key Features
- Add / edit / delete pantry items with input validation
- Browse suggested recipes based on strict ingredient-availability matching
- View full recipe details (ingredients + method steps)
- Settings screen for expiring-soon alerts and metric/imperial unit preference (persisted via SharedPreferences)

## Database Choice and Justification

**SQLite** (via `SQLiteOpenHelper`) was chosen over alternatives such as Room or a remote database for the following reasons:

- **Offline-first**: pantry tracking is a personal, local task with no inherent need for network connectivity or multi-device sync.
- **Lightweight and built-in**: SQLite ships with the Android platform, requiring no extra dependencies or build configuration overhead.
- **Relational structure fits the data**: the app's data — pantry items, recipes, and recipe ingredients — has natural relational structure (a recipe has many ingredients, and matching pantry items against recipe ingredients is essentially a join/lookup), which SQLite's table/query model handles directly.
- **Direct control for a learning exercise**: writing raw `SQLiteOpenHelper`/`ContentValues`/`Cursor` code (rather than an ORM like Room) demonstrates a clear understanding of the underlying CRUD and query mechanics, which suited the goals of this assignment.

### Schema Overview
- `pantry_items` — user's current pantry inventory (name, quantity, unit, etc.)
- `recipes` — recipe library (seeded with 16 recipes on first run)
- `recipe_ingredients` — join table linking recipes to their required ingredients and quantities

## Setup / Run Instructions

1. Clone the repository:
2. Open the project folder in **Android Studio** (Hedgehog or later recommended).
3. Let Gradle sync finish (it will download the dependencies listed in `app/build.gradle.kts`).
4. Run the app on an emulator or physical device:
    - Minimum SDK: 24 (Android 7.0)
    - Target/Compile SDK: 37
5. On first launch, the app automatically creates the SQLite database and seeds it with 16 recipes — no manual setup step is needed.
6. To reset the seeded data during testing, uninstall the app from the emulator/device (or clear its storage) and relaunch — `onCreate()` will reseed the database.

## GitHub Repository

[https://github.com/thembilekhulu-sudo/SmartPantryManager](https://github.com/thembilekhulu-sudo/SmartPantryManager)