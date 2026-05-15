# QuietLog — Design Patterns

## Goal

Use design patterns only when they make the code easier to understand, maintain, or extend.

Patterns should reduce complexity, not add ceremony.

## Recommended Patterns

### MVVM

Used for UI state handling.

Flow:

UI Screen
→ ViewModel
→ UseCase
→ Repository

## Repository Pattern

Used to separate app logic from data storage.

ViewModels and UseCases should not talk directly to DAOs.

## Use Case Pattern

Used for focused business actions.

Examples:

- CreateReminderUseCase
- SnoozeReminderUseCase
- CompleteReminderUseCase
- StartShoppingModeUseCase
- CreateGameEventUseCase

## Factory Pattern

Use only where object creation has variants or Android-specific details.

Good examples:

- NotificationFactory
- IntentFactory
- RepeatRuleFactory

Avoid factories for simple data objects unless creation becomes complex.

## Observer Pattern / Kotlin Flow

Use Flow for reactive local data updates.

Good for:

- reminder lists
- shopping list updates
- game event lists
- settings changes

## Avoid For Now

- Abstract Factory
- Service Locator
- giant inheritance trees
- unnecessary Singletons
- over-layered Clean Architecture

## Main Rule

If a pattern does not make the code clearer, do not use it.
