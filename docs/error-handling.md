# QuietLog — Error Handling Philosophy

## Goal

QuietLog should fail gracefully.

The app should remain:

- stable
- understandable
- predictable

when problems happen.

---

## Main Principle

Do not crash when a recoverable problem exists.

Prefer:

- safe fallbacks
- clear messages
- graceful recovery

---

## Important Error Cases

### Invalid Phone Number

If a phone number is invalid:

- show clear message
- prevent broken dialer action

Never auto-call.

---

### Missing Installed Game/App

If a linked app/game was removed:

- show message
- allow relinking or removal

Do not crash.

---

### Notification Permission Denied

If notifications are disabled:

- explain calmly
- keep reminder visible in-app

Avoid spammy permission requests.

---

### Failed Reminder Scheduling

If scheduling fails:

- retry safely when appropriate
- avoid duplicate notifications

---

## Main Rule

A utility app must feel trustworthy even when problems happen.
