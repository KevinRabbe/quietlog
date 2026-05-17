# QuietLog — Shopping Roadmap

## Goal

The Shopping feature should be fast, offline, simple, and useful in real stores.

Core rule:

> Shopping features must reduce friction. They must not make adding items slower.

---

## Philosophy

QuietLog Shopping should be:

- offline-first
- fast to use
- one-hand friendly
- privacy-friendly
- no account
- no cloud sync
- no store tracking
- no AI dependency

---

## What Shopping Should Solve

QuietLog Shopping should help users:

- quickly build shopping lists
- check items fast while shopping
- avoid forgetting remaining items
- reuse common lists
- optionally organize items by simple categories
- manually share a list without QuietLog servers

---

# Feature Priority

## Phase S1 — Shopping Stability Baseline

Goal:
Make sure the current shopping list behavior is stable before adding more features.

Checks:

- add item
- check item
- uncheck item
- delete item if supported
- clear completed if supported
- app restart persistence
- empty input handling
- no crashes

---

## Phase S2 — Fast Add Mode

Goal:
Make list creation faster.

Behavior:

- user types item name
- pressing enter/add saves the item
- input stays focused
- input clears after save
- empty input is ignored safely

Reason:
High value, low complexity.

---

## Phase S3 — Quantity Field

Goal:
Allow simple quantities without overcomplicating items.

Examples:

- Milk x2
- Cat food x12
- Pizza x3

Rules:

- quantity is optional
- default quantity is 1
- no price tracking
- no units system in V1
- no barcode scanning

---

## Phase S4 — Optional Categories

Goal:
Make shopping faster in-store by grouping items.

Default categories:

- Fruit & Vegetables
- Fridge
- Frozen
- Drinks
- Household
- Hygiene / Beauty
- Pets
- Other

Rules:

- category is optional
- item creation must not require choosing a category
- if no category is selected, use Other
- shopping mode groups items by category
- categories must not slow down fast item adding

Possible later:

- custom categories
- remember last category per item name

Not in V1:

- AI sorting
- supermarket aisle mapping
- store-specific layouts

---

## Phase S5 — Shopping Mode

Goal:
Create a focused in-store mode.

Behavior:

- user starts shopping mode for a list
- checked items become visually de-emphasized or move down
- unchecked items remain easy to see
- Finish Shopping button stops shopping mode
- forgot-something reminder can run only while shopping mode is active

---

## Phase S6 — Forgot Something Reminder

Goal:
Help users avoid leaving with unchecked items.

Logic:

- shopping mode is active
- user checked at least one item
- unchecked items remain
- no item was checked for configured delay
- send reminder

Default delay:
5 minutes

Notification text:

> Still items left on your list.

Actions:

- Open List
- Finish Shopping
- Ignore

Rules:

- no GPS
- no cashier detection
- no movement detection in V1
- no AI
- no store tracking

---

## Phase S7 — Reusable Lists / Duplicate List

Goal:
Make recurring shopping easier.

Examples:

- Weekly groceries
- Cat supplies
- Cleaning supplies
- Pizza night

Behavior:

- duplicate existing list
- copied items start unchecked
- quantities and categories are preserved

---

## Phase S8 — Manual Share As Text

Goal:
Allow easy sharing without QuietLog infrastructure.

Behavior:

- user taps Share
- QuietLog creates plain text list
- Android share sheet opens

Example output:

```text
Shopping list:
[ ] Milk x2 — Fridge
[ ] Cat food x12 — Pets
[ ] Trash bags — Household
```

Rules:

- no QuietLog servers
- no account
- no live sync
- internet is optional transport only

---

# Explicitly Not Planned For Shopping MVP

Do not add:

- cloud shared lists
- accounts
- live collaboration
- barcode scanning
- GPS store reminders
- cashier detection
- movement detection
- price comparison
- supermarket integrations
- AI categorization
- meal planning
- ads

---

# Recommended Implementation Order

1. Shopping stability baseline
2. Fast add mode
3. Quantity field
4. Optional categories
5. Shopping mode
6. Forgot-something reminder
7. Duplicate/reuse list
8. Manual share as text

Do not start later phases until earlier ones are stable.
