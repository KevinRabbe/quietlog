# QuietLog — State Management

## Goal

State management should remain:

- predictable
- reactive
- easy to debug
- understandable

## Main Flow

Room Database
→ Repository
→ ViewModel
→ StateFlow
→ Compose UI

## Main Principle

Single source of truth.

Avoid duplicated state across multiple layers.

## UI Philosophy

Compose UI should:
- display state
- send events

Compose UI should not:
- contain business logic
- directly manipulate database
- schedule notifications

## ViewModel Philosophy

ViewModels should:
- coordinate actions
- expose StateFlow
- stay predictable

Avoid giant ViewModels.

## Main Rule

Predictable state creates predictable behavior.
