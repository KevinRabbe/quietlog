# QuietLog — Feature Decision Rule

Every new feature must pass a value vs. complexity check.

## Rule

A feature is worth adding when it creates high user value with reasonable development effort.

Good feature:

```text
40% user-quality improvement
20% added complexity
→ likely worth building
```

Bad feature:

```text
5% user-quality improvement
30% added complexity
→ not worth building
```

## Evaluation Questions

Before adding a feature, ask:

- Does this make QuietLog more reliable?
- Does this make daily use easier?
- Does this reduce notification chaos?
- Does this protect user privacy?
- Does this fit offline-first?
- Does this add too much UI or technical complexity?

## Default Answer

If a feature is not clearly useful, keep it out.

QuietLog should stay simple, reliable, and calm.
