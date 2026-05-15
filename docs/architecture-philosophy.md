# QuietLog — Architecture Philosophy

## Goal

Build a codebase that stays:

- understandable
- maintainable
- reliable
- lightweight

for many years.

## Core Principle

Architecture exists to reduce complexity, not create it.

## Preferred Architecture

Use:

- MVVM
- Repository Pattern
- small focused UseCases
- Factory Pattern only where justified

Main flow:

UI
→ ViewModel
→ UseCase
→ Repository
→ Room Database

## Preferred Style

Prefer:

- readable naming
- predictable flow
- modular features
- small focused classes

Avoid:

- overengineering
- unnecessary abstraction
- giant inheritance structures
- patterns added only to look professional

## Engineering Priorities

1. Reliability
2. Simplicity
3. Maintainability
4. Performance
5. Extensibility
6. Cleverness

Clever code is only useful if future development stays understandable.
