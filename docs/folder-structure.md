# QuietLog — Folder Structure

## Goal

The folder structure should make it easy to find where each part of the app belongs.

QuietLog should avoid both extremes:

- one giant messy folder
- too many abstract architecture layers

## Recommended Package Structure

```text
com.kevinrabbe.quietlog

├── MainActivity.kt
├── QuietLogApp.kt
│
├── core
│   ├── database
│   ├── notification
│   ├── navigation
│   ├── time
│   ├── design
│   └── util
│
├── domain
│   ├── model
│   ├── repository
│   └── usecase
│
├── data
│   ├── local
│   ├── repository
│   └── mapper
│
└── feature
    ├── home
    ├── reminders
    ├── shopping
    ├── games
    └── settings
```

## Feature Folder Pattern

Each feature should follow a predictable structure.

Example:

```text
feature/reminders

├── ReminderScreen.kt
├── ReminderViewModel.kt
├── ReminderUiState.kt
├── ReminderEvent.kt
└── components
```

## Core Folder

Use `core` for shared technical systems.

Examples:

- database setup
- notification scheduling
- navigation routes
- shared UI theme
- time utilities

## Domain Folder

Use `domain` for app logic and app models.

Examples:

- Reminder
- ShoppingList
- GameEvent
- CreateReminderUseCase
- SnoozeReminderUseCase

## Data Folder

Use `data` for persistence and implementation details.

Examples:

- Room entities
- DAOs
- repository implementations
- mappers between database and domain models

## Rule

If a file belongs to only one screen, keep it inside that feature.

If a file is reused across multiple features, move it to `core`, `domain`, or `data`.
