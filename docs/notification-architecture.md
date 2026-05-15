# QuietLog — Notification Architecture

## Goal

Notifications must remain:

- reliable
- predictable
- lightweight
- user-controlled

This is one of the most important systems in QuietLog.

## Notification Flow

Reminder/Game Event Created
→ stored in Room Database
→ scheduling logic runs
→ Android notification scheduled
→ user receives notification
→ user action handled

## Main Components

- NotificationScheduler
- NotificationFactory
- NotificationActionHandler
- BootReceiver
- TimeChangeReceiver

## NotificationScheduler

Responsible for:

- scheduling reminders
- rescheduling recurring reminders
- canceling notifications
- handling snooze logic

## NotificationFactory

Responsible for building notification objects.

Creates:

- reminder notifications
- shopping notifications
- game event notifications
- call reminder notifications

## NotificationActionHandler

Handles:

- Done
- Snooze
- Open
- Open Game
- Open Dialer
- Finish Shopping

## BootReceiver

Purpose:
Reschedule notifications after reboot.

## TimeChangeReceiver

Handles:

- timezone changes
- manual clock changes

## Reliability Priority

Notifications must survive:

- device reboot
- app updates
- app restarts
- Android background restrictions

## Philosophy

Notifications should feel:

- useful
- calm
- intentional
- actionable
