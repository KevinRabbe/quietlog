# QuietLog — Android Reliability Strategy

## Goal

QuietLog reminders should remain reliable across normal Android device behavior.

Especially:
- app restarts
- device reboot
- device sleep
- Android background restrictions
- battery optimization

## Core Systems

### AlarmManager

Use for:
- exact reminder timing
- important scheduled notifications

### WorkManager

Use for:
- maintenance tasks
- non-exact background work
- rescheduling support if appropriate

## Required Receivers

### BootReceiver

Purpose:
Reschedule reminders after reboot.

### TimeChangeReceiver

Purpose:
Handle timezone changes and manual clock changes.

## Battery Philosophy

Avoid:
- constant polling
- aggressive background services
- unnecessary wakeups

QuietLog should remain battery-friendly.

## Main Rule

Reminder reliability is more important than feature complexity.
