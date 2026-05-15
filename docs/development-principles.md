# QuietLog — Development Principles

## Build Small, Finish Small

Prefer:

- small completed systems
- stable releases
- iterative improvements

Avoid giant unfinished systems.

---

## Reliability First

QuietLog is a utility app.

Users must trust:

- reminders
- notifications
- recurring events
- local data persistence

Reliability is more important than flashy features.

---

## Reduce User Friction

Good features often remove small daily annoyances.

Examples:

- open dialer from reminder
- open game from notification
- forgot shopping reminder
- in-app notification mode

---

## Keep Complexity Low

Before adding a feature, ask:

- Is this truly useful?
- Does this improve daily use enough?
- Is the maintenance cost worth it?

Avoid unnecessary complexity.

---

## Respect Device Resources

QuietLog should:

- use little battery
- use little storage
- avoid excessive background activity
- remain lightweight long-term

---

## Long-Term Maintainability

Code should remain understandable years later.

Prefer:

- readable code
- modular systems
- predictable behavior

Avoid:

- overengineered abstractions
- unreadable clever code
