# QuietLog — Agent Instructions

Work token-efficiently.

## Always Follow

- Work only on the assigned GitHub issue.
- Keep changes minimal and focused.
- Do not add unrequested features.
- Do not overengineer.
- Keep the app offline-first.
- No cloud sync.
- No accounts.
- No AI features.
- No ads.
- No analytics or tracking SDKs.
- No subscriptions.
- No social or multiplayer systems.

## Reference Docs

Use these only when needed:

- `docs/codex-roadmap.md`
- `docs/architecture-philosophy.md`
- `docs/feature-decision-rule.md`
- `docs/definition-of-done.md`

Do not restate project philosophy unless directly relevant.

## Architecture

Use:

- Kotlin
- Jetpack Compose
- Material 3
- MVVM
- Repository Pattern
- UseCases only when useful
- Room
- DataStore
- AlarmManager for exact reminders

Avoid:

- unnecessary abstraction
- giant ViewModels
- business logic inside Compose UI
- direct DAO access from ViewModels
- unnecessary dependencies

## Output Format

After work, respond only with:

1. Files changed
2. What was implemented
3. How to test
4. Next suggested issue
