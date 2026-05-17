# QuietLog — Game Event Design

## Goal

Game Events let users create manual reminders for installed games/apps.

The app must not guess event times.
The user must be in control of the event schedule.

---

## Core Example

A correct Game Event can look like this:

```text
Game/App: World of Warcraft
Event title: WoW raid
Event time: 20:00
Reminder offset: 20 minutes before
Repeat rule: Friday
Notification mode: Device notification
```

QuietLog should then notify the user at 19:40 for a 20:00 Friday raid.

---

## Core Use Cases

Examples:

- boss spawn at 20:00
- raid every Friday at 20:00
- daily reset at 02:00
- guild event on Saturday
- limited event ending tomorrow

---

## Required Creation Flow

When creating a game event, the user must be able to set:

1. Selected installed app/game
2. Event title
3. Event time
4. Reminder offset
5. Repeat rule
6. Notification mode

Event date is only required for one-time events or custom schedules.
For weekly events, the repeat day plus event time is enough.

---

## Game/App Selection

User selects an installed app/game from the device.

Preferred approach:

- scan/list installed launchable apps
- user selects the game/app from the list
- store app name and package name

Stored values:

- app name
- package name

The package name is used later to open the app from a notification.

Possible later fallback only if useful:

- manually select/link an app path or launcher target if Android allows it safely
- manually enter app name without launch support

Do not add complex app scanning beyond what Android supports cleanly.

---

## Event Title

User enters a custom title.

Examples:

- WoW raid
- World Boss
- Daily Reset
- Clan War
- Event Shop Reset

---

## Event Time

User must be able to choose the event time manually.

Examples:

- 20:00
- 19:30
- 02:00

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
- 20 minutes before
- 30 minutes before
- 1 hour before

The notification time is calculated from:

```text
event date/time - reminder offset
```

Example:

```text
Event: Friday 20:00
Reminder offset: 20 minutes
Notification: Friday 19:40
```

---

## Repeat Rule

Minimum repeat options:

- once
- daily
- weekly
- selected weekday

Example:

```text
Repeat: Friday
Event time: 20:00
```

This means the event happens every Friday at 20:00.

Do not add complex repeat behavior until basic once/daily/weekly/weekday behavior is reliable.

---

## Notification Mode

User chooses how loud the event should be.

Options:

- Off
- In-app only
- Device notification
- Persistent notification

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
- event time
- reminder offset
- repeat rule
- notification mode

For one-time events, also show the date.

The chosen time must be visible and must match what the user selected.

---

## Incorrect Behavior

The following behavior is wrong:

- automatically using current time as event time
- creating events without manual time selection
- hiding the event time from the user
- scheduling reminders based on creation time instead of chosen event time
- forcing a date for simple weekly events when weekday + time is enough

---

## Acceptance Criteria

A correct Game Event implementation must satisfy:

- user can select game/app from device if possible
- user can enter event title
- user can choose event time manually
- user can choose reminder offset
- user can choose repeat rule such as Friday
- user can choose notification mode
- saved event displays chosen time
- saved event displays repeat rule
- notification scheduling uses chosen event time and reminder offset
- app does not auto-use creation time as event time
- missing linked app is handled safely

---

## Main Rule

Game Events are manual planning tools.

The user chooses the schedule.
QuietLog only reminds them.