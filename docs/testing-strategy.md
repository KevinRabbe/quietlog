# QuietLog — Testing Strategy

## Goal

QuietLog must be tested around the systems users trust most.

Priority:

- reminders
- notifications
- recurring events
- shopping list behavior
- local data safety

---

## Highest Priority Tests

### Reminder Scheduling

Test:

- one-time reminders
- future reminders
- overdue reminders
- snooze behavior
- cancel behavior

---

### Recurring Reminders

Test:

- daily repeat
- weekly repeat
- custom weekdays
- next occurrence calculation

---

### Shopping Reminder Logic

Test:

- forgot something reminder delay
- no reminder when all items are checked
- timer reset when another item is checked

---

### Game Event Notifications

Test:

- reminder offset
- repeat rules
- open game intent creation
- deleted app handling

---

## Main Rule

If a bug can cause a missed reminder, it is high priority.
