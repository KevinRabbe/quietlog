# QuietLog — Codex Implementation Roadmap

This roadmap is written for a coding agent.

Goal:
Build QuietLog step by step without overengineering, feature creep, or unfinished large systems.

Core rule:
Complete one small stable step before starting the next one.

---

## Global Project Rules

Codex must follow these rules for every task:

- Keep the app offline-first.
- Do not add cloud sync.
- Do not add accounts.
- Do not add AI features.
- Do not add ads.
- Do not add analytics/tracking SDKs.
- Do not add subscriptions.
- Do not add social or multiplayer systems.
- Prefer official Android/Jetpack libraries.
- Keep dependencies minimal.
- Prefer readable code over clever abstractions.
- Do not introduce unnecessary design patterns.
- Every step must keep the project buildable.

---

## Target Package

```text
com.kevinrabbe.quietlog
```

---

## Target Tech Stack

- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose
- Room Database
- DataStore
- Coroutines
- Flow / StateFlow
- AlarmManager for exact reminders
- WorkManager only for non-exact background work or maintenance tasks

---

# Phase 1 — Android Foundation

## Task 1.1 — Initialize Android Project

Create the Android project foundation.

Requirements:
- Project name: QuietLog
- Package: `com.kevinrabbe.quietlog`
- Kotlin
- Jetpack Compose
- Material 3
- Min SDK: 26 or 27
- Target SDK: latest stable available in local Android Studio

Acceptance criteria:
- Gradle sync works.
- App builds.
- App launches.
- Home screen placeholder is visible.

Do not:
- Add Room yet.
- Add notification logic yet.
- Add complex architecture yet.

Suggested commit:

```text
chore: initialize Android project
```

---

## Task 1.2 — Add Base Package Structure

Create the base packages.

Required packages:

```text
core/
  database/
  notification/
  navigation/
  design/
  time/
  util/

data/
  local/
  mapper/
  repository/

domain/
  model/
  repository/
  usecase/

feature/
  home/
  reminders/
  shopping/
  games/
  settings/
```

Acceptance criteria:
- Packages exist.
- Project still builds.
- No unused complex code added.

Suggested commit:

```text
chore: add base package structure
```

---

## Task 1.3 — Add Basic Navigation

Add Navigation Compose and bottom navigation.

Screens:
- Home
- Reminders
- Shopping
- Games
- Settings

Acceptance criteria:
- User can switch between all screens.
- Each screen shows a simple placeholder title.
- Back behavior is stable.

Do not:
- Add real reminder logic yet.
- Add real shopping logic yet.
- Add real game event logic yet.

Suggested commit:

```text
feat: add basic app navigation
```

---

## Task 1.4 — Add QuietLog Theme Foundation

Add basic Material 3 theme.

Requirements:
- Dark theme first.
- Clean readable typography.
- Calm colors.
- No flashy animations.

Acceptance criteria:
- App uses the QuietLog theme.
- UI remains readable.
- Navigation still works.

Suggested commit:

```text
feat: add QuietLog theme foundation
```

---

# Phase 2 — Reminder Foundation

## Task 2.1 — Add Reminder Domain Model

Create the domain model for reminders.

Suggested fields:

```text
id
title
notes
dateTime
repeatRule
notificationMode
status
phoneNumber
createdAt
updatedAt
```

Acceptance criteria:
- Domain model exists.
- Enums or sealed classes exist for status, repeat rule, and notification mode.
- No database code yet if not needed.

Suggested commit:

```text
feat: add reminder domain model
```

---

## Task 2.2 — Add Room Database For Reminders

Add Room dependency and reminder persistence.

Required:
- ReminderEntity
- ReminderDao
- QuietLogDatabase
- Mapper between ReminderEntity and Reminder domain model

Acceptance criteria:
- Reminder can be inserted.
- Reminder can be queried as Flow.
- Project builds.

Do not:
- Add shopping tables yet.
- Add game event tables yet.

Suggested commit:

```text
feat: add reminder local database
```

---

## Task 2.3 — Add Reminder Repository

Create repository abstraction and implementation.

Required:
- `ReminderRepository` in domain layer
- `ReminderRepositoryImpl` in data layer

Required methods initially:
- observeReminders()
- getReminderById(id)
- createReminder(reminder)
- updateReminder(reminder)
- deleteReminder(id)

Acceptance criteria:
- ViewModels do not access DAO directly.
- Repository uses Room DAO internally.

Suggested commit:

```text
feat: add reminder repository
```

---

## Task 2.4 — Add Reminder Use Cases

Create small use cases.

Initial use cases:
- ObserveRemindersUseCase
- CreateReminderUseCase
- CompleteReminderUseCase
- DeleteReminderUseCase

Acceptance criteria:
- Use cases are small.
- No unnecessary abstraction.
- Reminder ViewModel can call use cases.

Suggested commit:

```text
feat: add reminder use cases
```

---

## Task 2.5 — Add Reminder UI State And ViewModel

Create Reminder screen logic.

Required:
- ReminderUiState
- ReminderEvent
- ReminderViewModel

Acceptance criteria:
- ViewModel exposes StateFlow.
- UI can observe reminder list.
- UI sends events to ViewModel.

Suggested commit:

```text
feat: add reminder view model state
```

---

## Task 2.6 — Add Basic Reminder Screen

Build simple reminder list and create form.

Required:
- reminder list
- empty state
- title input
- date/time input
- save button

Acceptance criteria:
- User can create a reminder.
- Reminder appears in the list.
- Reminder survives app restart.

Do not:
- Add recurring reminders yet.
- Add phone call support yet.
- Add advanced UI polish yet.

Suggested commit:

```text
feat: add basic reminder creation
```

---

# Phase 3 — Notification Prototype

## Task 3.1 — Add Notification Permission Handling

Handle Android notification permission where required.

Acceptance criteria:
- App requests notification permission only when needed.
- If permission is denied, reminder still exists in app.
- App does not spam permission requests.

Suggested commit:

```text
feat: add notification permission handling
```

---

## Task 3.2 — Add NotificationFactory

Create centralized notification creation.

Required:
- basic reminder notification
- notification channel creation
- actions placeholder if needed

Acceptance criteria:
- Notification creation logic is not inside ViewModel.
- Notification title/body are clear and calm.

Suggested commit:

```text
feat: add notification factory
```

---

## Task 3.3 — Add NotificationScheduler

Create reminder scheduling.

Requirements:
- Use AlarmManager for exact reminder notifications.
- Schedule one-time reminders.
- Cancel scheduled reminder when deleted.

Acceptance criteria:
- A future reminder triggers notification.
- Deleted reminder does not trigger.
- Past reminder is not scheduled incorrectly.

Suggested commit:

```text
feat: add reminder notification scheduler
```

---

## Task 3.4 — Add Notification Actions

Add actions:
- Done
- Snooze
- Open app/reminder

Acceptance criteria:
- Done marks reminder completed.
- Snooze reschedules reminder.
- Open opens QuietLog.

Suggested commit:

```text
feat: add reminder notification actions
```

---

## Task 3.5 — Add Reboot Rescheduling

Add BootReceiver.

Requirement:
- Reschedule pending reminders after device reboot.

Acceptance criteria:
- Reboot receiver exists.
- Pending reminders can be rescheduled.
- No duplicate reminders are created.

Suggested commit:

```text
feat: reschedule reminders after reboot
```

---

# Phase 4 — Pixel Device Reliability Testing

This phase should be done on the Google Pixel 9 XL when available.

## Task 4.1 — Real Device Reminder Timing Test

Test cases:
- reminder in 1 minute
- reminder in 5 minutes
- reminder while app is closed
- multiple reminders
- snooze

Acceptance criteria:
- Notifications trigger close to expected time.
- No duplicate notifications.
- Snooze works.

---

## Task 4.2 — Reboot Test

Test:
- schedule reminder
- reboot Pixel
- wait for reminder

Acceptance criteria:
- Reminder still triggers.
- No duplicate notification appears.

---

## Task 4.3 — Battery Optimization Test

Test behavior under normal Pixel battery optimization.

Acceptance criteria:
- App remains battery-friendly.
- Reminder behavior is acceptable.
- If user guidance is needed, message must be calm and clear.

---

# Phase 5 — Call Reminder Support

## Task 5.1 — Add Phone Number Field

Add optional phone number to reminder creation/editing.

Acceptance criteria:
- Phone number can be saved.
- Phone number can be edited.
- Empty phone number is allowed.

Suggested commit:

```text
feat: add phone number to reminders
```

---

## Task 5.2 — Add IntentFactory For Dialer

Create IntentFactory.

Requirement:
- Open Android dialer with number pre-filled.
- Never call automatically.

Acceptance criteria:
- ACTION_DIAL is used.
- ACTION_CALL is not used.
- Invalid numbers are handled safely.

Suggested commit:

```text
feat: add dialer intent support
```

---

# Phase 6 — Shopping List Foundation

## Task 6.1 — Add Shopping Data Model

Entities:
- ShoppingList
- ShoppingItem

Acceptance criteria:
- Models exist.
- Room entities exist.
- DAO exists.

Suggested commit:

```text
feat: add shopping data model
```

---

## Task 6.2 — Add Basic Shopping UI

Requirements:
- create list
- add item
- check/uncheck item
- clear completed items

Acceptance criteria:
- Shopping list works offline.
- Items persist after restart.

Suggested commit:

```text
feat: add basic shopping lists
```

---

## Task 6.3 — Add Forgot Something Reminder

Logic:
- shopping mode active
- user checks at least one item
- unchecked items remain
- no item checked for configured delay
- send notification

Default delay:
5 minutes

Acceptance criteria:
- Reminder triggers only when unchecked items remain.
- Timer resets when another item is checked.
- Finish Shopping stops reminder.

Suggested commit:

```text
feat: add forgot something shopping reminder
```

---

# Phase 7 — Game Event Planner

## Task 7.1 — List Installed Apps

Requirement:
- allow user to select installed app/game

Acceptance criteria:
- Installed apps can be listed.
- Selected app package name is stored.

Suggested commit:

```text
feat: add installed app selection
```

---

## Task 7.2 — Add Game Event Model

Fields:
- id
- app name
- package name
- event title
- event time
- reminder offset
- repeat rule
- notification mode
- status

Acceptance criteria:
- Game event can be stored locally.

Suggested commit:

```text
feat: add game event data model
```

---

## Task 7.3 — Add Game Event Notifications

Requirement:
- schedule event reminder
- open selected app from notification

Acceptance criteria:
- Notification triggers.
- Open Game action launches selected app if installed.
- Missing app is handled safely.

Suggested commit:

```text
feat: add game event notifications
```

---

# Phase 8 — Polish And Hardening

## Task 8.1 — Add Settings

Settings:
- default notification mode
- default snooze duration
- shopping reminder delay
- theme setting

Acceptance criteria:
- Settings persist with DataStore.
- Defaults are sensible.

Suggested commit:

```text
feat: add basic settings
```

---

## Task 8.2 — Add Changelog Discipline

Requirement:
- every release has clear changelog text
- avoid vague lines like "general improvements"

Acceptance criteria:
- changelog file exists
- release notes are specific

Suggested commit:

```text
docs: add changelog
```

---

# Final Rule

If a step becomes too large, split it into smaller steps.

Never combine unrelated systems in one task.

QuietLog should grow through small reliable increments.
