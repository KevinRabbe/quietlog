# QuietLog — Local Data Model

## Core Rule

All data is stored locally on the device.

No cloud.
No accounts.
No external sync by default.

---

## Main Entities

### Reminder

Fields:

- id
- title
- notes
- date
- time
- repeat rule
- notification mode
- status
- created at
- updated at

Optional:
- phone number

---

### Shopping List

Fields:

- id
- name
- status
- created at
- updated at

---

### Shopping Item

Fields:

- id
- list id
- name
- quantity
- checked status
- checked at
- created at
- updated at

---

### Game Event

Fields:

- id
- game/app name
- package name
- event title
- event time
- reminder offset
- repeat rule
- notification mode
- status
- created at
- updated at

---

### App Settings

Fields:

- default notification mode
- default snooze duration
- shopping reminder delay
- theme setting
- first launch completed
