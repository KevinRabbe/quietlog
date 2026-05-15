# QuietLog — Definition Of Done

## Goal

A feature is only considered done when it is:

- functional
- stable
- understandable
- maintainable

not simply working once.

---

## Required For Completion

Before closing a feature issue:

- app builds successfully
- no obvious crashes
- navigation still works
- feature works after app restart when relevant
- UI remains readable
- no major regressions introduced

---

## Notification Features

Notification-related features additionally require:

- notification triggers correctly
- no duplicate notifications
- reminder survives app restart when appropriate
- behavior tested on real device when possible

---

## UI Features

UI changes should:

- support multiple screen sizes
- remain readable
- use proper touch targets
- avoid clutter

---

## Code Quality

Feature code should:

- follow architecture rules
- avoid unnecessary complexity
- avoid giant classes/functions
- use readable naming

---

## Git Requirements

Before merge:

- commits are understandable
- commits are focused
- unrelated changes are separated

---

## Main Rule

Mostly working is not the same as done.
