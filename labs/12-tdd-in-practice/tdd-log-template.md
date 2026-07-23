# TDD Log — HoldingUpdater

| Cycle | Test written (red) | Minimal change to go green | Refactor notes (if any) |
|---|---|---|---|
| 1 | Buy of 10 on Holding(100) → quantity 110 | Called `holding.adjust(quantity)` unconditionally | None |
| 2 | Sell of 10 on Holding(100) → quantity 90 | Added `if (isBuy)` branch, else `holding.adjust(-quantity)` | None |
| 3 | Buy of -5 throws `IllegalArgumentException` | Added guard clause: `if (quantity <= 0) throw` | None |
| 4 | Sell of 200 on Holding(100) throws `IllegalArgumentException` | Already passed — `Holding.adjust()` enforces the non-negative invariant | None |
| 5 (refactor only) | — | — | Organised tests into `@Nested WhenBuying` / `WhenSelling` groups with `@DisplayName` |

## Reflection

- Cycle 4 passed immediately. That tells you `HoldingUpdater` correctly delegates to `Holding` rather than re-implementing the check — the test confirmed the delegation is working without any extra code.
- Not duplication — two classes correctly guarding their own boundary. `Holding` guards its invariant (quantity never negative). `HoldingUpdater` guards its precondition (the quantity argument must be positive). Different concerns at different levels.
- Compared to `FeeBandClassifier`: slightly harder discipline because the requirement ("let Holding handle it") feels like doing nothing, which runs against the instinct to add a guard. `FeeBandClassifier` was purer — every cycle added something visible. Here Cycle 4 taught restraint instead.
