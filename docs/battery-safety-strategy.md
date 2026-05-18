# QuietLog — Battery Safety Strategy

## Goal

QuietLog must be reliable without becoming a battery-draining app.

Core rule:

> If the user did not schedule anything, QuietLog should basically do nothing.

Battery trust is part of product trust.

---

## Hard Rules

Do not add:

- permanent background service
- constant polling
- GPS/store detection
- notification listener
- app usage tracking
- wake locks unless absolutely unavoidable
- sync loops
- analytics SDKs
- cloud retry jobs
- always-running timers

---

## Allowed Background Work

### AlarmManager

Use for:

- user-created reminders
- app-linked events
- important logs
- exact time notifications

Only schedule alarms when the user creates something that needs a reminder.

---

### BootReceiver

Use only to restore scheduled alarms after reboot.

Rules:

- reschedule only active/upcoming items
- avoid duplicates
- avoid heavy work
- use safe async handling

---

### WorkManager

Use only for rare non-urgent maintenance if needed.

Examples:

- cleanup
- non-exact maintenance
- future optional backup/export preparation

Do not use WorkManager as a polling loop.

---

## Not Allowed

QuietLog should not use:

- notification reading access
- NotificationListenerService
- foreground service for normal reminders
- background loops checking the clock
- location-based shopping detection
- automatic store detection
- app usage monitoring

---

## Device Testing

Test on:

- Google Pixel 9 XL
- Samsung Galaxy A17 or similar Samsung A-series device

Why:

- Pixel tests reference Android behavior
- Samsung tests manufacturer battery/background behavior

---

## Battery QA Checklist

Test:

- app closed
- screen locked
- device reboot
- overnight reminder
- multiple reminders
- multiple app events
- notification permission denied
- notification permission granted
- 24-hour idle battery behavior

---

## Acceptance Criteria

QuietLog should:

- not show abnormal battery usage during idle time
- not run unnecessary background work
- not use continuous services
- not drain battery when no reminders/events/logs are scheduled
- still deliver scheduled notifications reliably enough to trust

---

## Main Rule

Reliability must not be achieved through wasteful background behavior.

QuietLog should be quiet for the user and quiet for the battery.
