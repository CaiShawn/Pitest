# AliceRec

An Android movie tracker — an offline personal catalogue for discovering, saving, and tracking films. Powered by TMDb. Built with vibe coding.

## Tech Stack

| Layer | Library |
|-------|---------|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM |
| Database | Room (SQLite) |
| Network | Retrofit + OkHttp + Moshi |
| DI | Koin |
| Navigation | Jetpack Navigation Compose |
| Images | Coil |
| API | TMDb (The Movie Database) |

## Project Structure

```
app/src/main/java/com/caishawn/alicerec/
├── AliceRecApp.kt              # Application (Koin init)
├── MainActivity.kt             # Single Activity + bottom nav
├── di/
│   └── AppModule.kt            # Koin DI definitions
├── data/
│   ├── local/
│   │   ├── MovieEntity.kt      # Room entity
│   │   ├── MovieDao.kt         # Room DAO
│   │   └── AppDatabase.kt      # Room database
│   ├── remote/
│   │   ├── TmdbApi.kt          # Retrofit API interface
│   │   └── TmdbModels.kt       # API response models
│   └── repository/
│       └── MovieRepository.kt  # Data layer coordinator
├── domain/
│   └── model/
│       └── Movie.kt            # Domain model
└── ui/
    ├── navigation/
    │   └── NavGraph.kt         # Navigation routes
    ├── search/
    │   ├── SearchScreen.kt     # TMDb search UI
    │   └── SearchViewModel.kt
    ├── collection/
    │   ├── CollectionScreen.kt # Want-to-see / Watched lists
    │   └── CollectionViewModel.kt
    ├── detail/
    │   ├── DetailScreen.kt     # Movie detail page
    │   └── DetailViewModel.kt
    └── theme/
        └── Theme.kt            # Material 3 dynamic color
```

## Getting Started

1. **Get a TMDb API key** — Sign up at https://www.themoviedb.org/settings/api
2. **Set your key** — Open `local.properties` and replace `YOUR_TMDB_API_KEY_HERE`
3. **Open in Android Studio** — Open the root `build.gradle.kts`, sync Gradle
4. **Run** — Pick an emulator (API 26+), click Run

## Installed Skills

Skills are located in `.pi/skills/` and are auto-discovered by [pi](https://github.com/earendil-works/pi-coding-agent).

### Engineering Skills (User-invoked)

- `/ask-matt` — Route to the right skill for your situation
- `/grill-with-docs` — Grilling session that builds your project's domain model
- `/triage` — Move issues through a state machine of triage roles
- `/improve-codebase-architecture` — Scan codebase for deepening opportunities
- `/setup-matt-pocock-skills` — Configure this repo for the engineering skills
- `/to-spec` — Turn conversation into a spec
- `/to-tickets` — Break plans into tracer-bullet tickets
- `/implement` — Build work described by a spec/tickets
- `/wayfinder` — Plan large chunks of work as investigation tickets

### Engineering Skills (Model-invoked)

- `prototype` — Build throwaway prototypes
- `diagnosing-bugs` — Disciplined bug diagnosis loop
- `research` — Investigate against high-trust sources
- `tdd` — Test-driven development with red-green-refactor
- `domain-modeling` — Build and sharpen domain model
- `codebase-design` — Design deep modules
- `code-review` — Two-axis code review
- `resolving-merge-conflicts` — Resolve merge conflicts by intent

### Productivity Skills

- `/grill-me` — Get interviewed about a plan/design
- `/handoff` — Compact conversation into handoff document
- `/teach` — Teach skills over multiple sessions
- `/writing-great-skills` — Reference for writing skills

## Getting Started

Run `/setup-matt-pocock-skills` in pi to configure the skills for this project.
