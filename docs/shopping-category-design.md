# QuietLog — Shopping Category Design

## Goal

Shopping lists should stay fast, manual, and easy to organize.

Core philosophy:

> Fast list. Organize it how you want.

QuietLog should not become a smart grocery platform.

---

## Included Features

### 1. Custom Categories

Users can create their own shopping categories.

Examples:

- Food
- Drinks
- Frozen
- Hygiene
- Pets
- Household
- Grandma
- Kaufland
- Action
- Gaming Snacks

Categories are local only.

---

### 2. Category Reorder

Users can reorder categories manually.

Purpose:
Let users match their personal shopping route.

Example:

```text
1. Drinks
2. Food
3. Frozen
4. Hygiene
5. Household
```

---

### 3. Collapse / Expand Categories

Users can collapse or expand category sections.

Example:

```text
▼ Food
  □ Pizza
  □ Cheese

▶ Household
```

Collapsed state should persist locally.

---

### 4. Favorite / Default Category

Users can choose a favorite/default category.

When adding a new item, QuietLog should preselect the default category.

Purpose:
Fast repeated item entry.

---

### 5. Move Item Between Categories

Users can move an existing item to another category.

Simple options:

- item overflow menu
- long press later if useful
- dropdown in edit item dialog

Do not overcomplicate this.

---

### 6. Item Counters

Category headers should show progress.

Example:

```text
Food 2/5
```

Meaning:

```text
2 checked / 5 total
```

---

### 7. Clear Completed

Users can clear checked items from the shopping list.

This should work globally across all categories.

---

### 8. Duplicate Previous List

Users can duplicate an existing shopping list.

Expected behavior:

- duplicate list name may append "copy" or ask for name
- items are copied
- copied items start unchecked
- categories are preserved

Purpose:
Useful for repeated weekly shopping.

---

## Default Categories

On first install, QuietLog may create simple default categories:

- Food
- Drinks
- Frozen
- Hygiene
- Pets
- Household
- Other

Users can rename, reorder, collapse, or remove categories later.

---

## No / Probably Never List

Do not add:

- barcode scanner
- AI category detection
- store aisle mapping
- online product database
- shared accounts
- automatic store detection
- GPS store tracking
- smart grocery recommendations

Reason:

- too much complexity
- too much maintenance
- privacy risk
- not needed for QuietLog's core value

---

## Data Model Direction

### ShoppingCategory

Suggested fields:

```text
id
name
sortOrder
isCollapsed
isDefault
createdAt
updatedAt
```

### ShoppingItem

Suggested fields:

```text
id
listId
categoryId
name
quantity
isChecked
checkedAt
createdAt
updatedAt
```

---

## UI Direction

Example layout:

```text
[Add item...]
Default category: Food ▼

▼ Food 2/4
  □ Pizza
  □ Dino nuggets
  ☑ Cheese
  ☑ Milk

▼ Drinks 0/1
  □ Cola

▶ Household 0/3

[Clear completed]
```

---

## Main Rule

Shopping should stay fast and manual.

Do not make users fight the app.
Do not make QuietLog guess too much.
Let users organize their list their way.
