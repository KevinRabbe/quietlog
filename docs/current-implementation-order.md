# QuietLog — Current Implementation Order

This is the current recommended development sequence.

The goal is to improve QuietLog in the highest-value order without destabilizing the working MVP.

---

## 1. Home Dashboard

Issue:
`#25 Redesign Home screen as calm daily dashboard`

Goal:
Make Home useful as the central overview.

Core idea:

> Home = What matters next.

Includes:

- Today
- Next Up
- Active Shopping
- Next App Event
- Quick Actions
- Quiet Status

Reason:
The Home dashboard ties the whole app together and improves screenshots, first impressions, and daily usefulness.

---

## 2. Apps Tab Refactor

Goal:
Rename and reposition the current Game Events feature into a broader Apps tab.

Core idea:

> Apps = app-linked reminders and events.

Scope:

- Rename Games tab to Apps
- Keep installed app selection
- Keep manual event/reminder creation
- Open selected app from notification
- Open Android notification settings for selected app

Do not add:

- NotificationListenerService
- notification reading
- notification interception
- notification history
- app usage tracking

Reason:
This keeps the original game-event idea but makes it useful for any installed app without privacy-invasive behavior.

---

## 3. Shopping Categories

Issue:
`#23 Add shopping categories and organization tools`

Goal:
Make shopping lists easier to organize manually.

Core idea:

> Fast list. Organize it how you want.

Includes:

- custom categories
- category reorder
- collapse/expand categories
- favorite/default category
- move item between categories
- category counters
- clear completed

Do not add:

- barcode scanner
- AI category detection
- store aisle mapping
- online product database
- GPS store tracking

---

## 4. Repeatable Shopping Templates

Issue:
`#24 Add repeatable shopping list templates`

Goal:
Support repeated shopping routines.

Examples:

- Weekly groceries
- Monthly household
- Pet supplies
- Grandma shopping
- Party snacks

Includes:

- save list as template
- create fresh list from template
- generated items start unchecked
- categories preserved
- item order preserved

Reason:
This adds strong daily value without cloud, accounts, or smart grocery complexity.

---

## 5. Important Logs

Goal:
Add a dedicated feature for recurring life records and private important dates.

Core idea:

> Important Logs = private recurring personal records and reminders.

Examples:

- birthdays
- pet vaccines
- vet checkups
- TÜV
- insurance renewal
- finance deadlines
- contracts
- warranties
- home maintenance

Rules:

- local only
- manual entry
- no contact scraping
- no finance integrations
- no bank access
- no AI suggestions
- no cloud sync

Reason:
This fits QuietLog's identity strongly: private, local, useful reminders for important life events.

---

## 6. Android Notification Reliability QA

Issue:
`#5 Test Android notification reliability on real devices`

Goal:
Verify real notification behavior on actual devices.

Devices:

- Google Pixel 9 XL
- Samsung Galaxy A17 or similar Samsung A-series device

Focus:

- notification timing
- app closed behavior
- screen locked behavior
- reboot rescheduling
- permission denied/granted behavior
- overnight reminders
- no duplicate notifications

---

## 7. Battery Safety QA

Issue:
`#26 Test battery safety during idle and scheduled notifications`

Goal:
Verify QuietLog stays battery-friendly.

Core rule:

> If the user did not schedule anything, QuietLog should basically do nothing.

Do not fix reliability by adding:

- permanent background service
- constant polling
- GPS/store detection
- notification listener
- app usage tracking
- sync loops

---

## Current Rule

Do not add unrelated features while working through this order.

If a serious bug appears, fix the bug first.
Otherwise, follow this sequence.
