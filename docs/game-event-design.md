# QuietLog — Game Event Design

## Goal

Game Events let users create manual reminders for installed games/apps.

The app must not guess event times.
The user must be in control of the event schedule.

---

## Core Use Cases

Examples:

- boss spawn at 20:00
- raid every Wednesday at 19:30
- daily reset at 02:00
- guild event on Saturday
- limited event ending tomorrow

---

## Required Creation Flow

When creating a game event, the user should be able to set:

1. Game/app
2. Event title
3. Event date
4. Event time
5. Reminder offset
6. Repeat rule
7. Notification mode

---

## Game/App Selection

User selects an installed app/game.

Stored values:

- app name
- package name

The package name is used later to open the app from a notification.

---

## Event Title

User enters a custom title.

Examples:

- World Boss
- Raid
- Daily Reset
- Clan War
- Event Shop Reset

---

## Event Date

User must be able to choose the event date.

Minimum options:

- today
- tomorrow
- custom date

---

## Event Time

User must be able to choose the event time manually.

Important rule:

QuietLog must not automatically use the current time as the event time.

That defeats the purpose of planning future boss spawns, raids, resets, or scheduled game events.

---

## Reminder Offset

User chooses when to be notified before the event.

Recommended options:

- at event time
- 5 minutes before
- 10 minutes before
- 15 minutes before
- 30 minutes before
- 1 hour before

The notification time is calculated from:

```text
event date/time - reminder offset
```

---

## Repeat Rule

Minimum repeat options:

- once
- daily
- weekly
- custom weekdays later

Do not add complex repeat behavior until basic once/daily/weekly behavior is reliable.

---

## Notification Actions

Game event notifications should support:

- Open Game
- Snooze
- Done

If the app/game is no longer installed, QuietLog should show a calm message and avoid crashing.

---

## Display Requirements

Game event cards should show:

- app/game name
- event title
- event date/time
- reminder offset
- repeat rule
- notification mode

The chosen time must be visible and must match what the user selected.

---

## Incorrect Behavior

The following behavior is wrong:

- automatically using current time as event time
- creating events without manual date/time selection
- hiding the event time from the user
- scheduling reminders based on creation time instead of chosen event time

---

## Acceptance Criteria

A correct Game Event implementation must satisfy:

- user can select game/app
- user can enter event title
- user can choose event date
- user can choose event time
- user can choose reminder offset
- user can choose repeat rule
- saved event displays chosen date/time
- notification scheduling uses chosen date/time
- app does not auto-use creation time as event time
- missing linked app is handled safely

---

## Main Rule

Game Events are manual planning tools.

The user chooses the schedule.
QuietLog only reminds them.