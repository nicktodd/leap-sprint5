# TDD Log — FeeBandClassifier

| Cycle | Test written (red) | Minimal change to go green | Refactor notes (if any) |
|---|---|---|---|
| 1 | `classify(1000)` returns `"STANDARD"` | Returned `"STANDARD"` unconditionally | None |
| 2 | `classify(60000)` returns `"INSTITUTIONAL"` | Added `if >= 50000 return "INSTITUTIONAL"` | None |
| 3 | `classify(10000)` returns `"PREMIUM"` | Added `else if >= 5000 return "PREMIUM"` | None |
| 4 | `classify(5000)` returns `"PREMIUM"` (lower boundary inclusive) | Boundary already handled by `>=` — test passed immediately | None |
| 5 | `classify(50000)` returns `"INSTITUTIONAL"` (lower boundary inclusive) | Boundary already handled by `>=` — test passed immediately | None |
| 6 (refactor only) | — | — | Extracted `5000` and `50000` as named constants `PREMIUM_THRESHOLD` and `INSTITUTIONAL_THRESHOLD` |

## Reflection

- At Cycle 1 returning `"STANDARD"` unconditionally felt like cheating — the urge was to write the full if/else immediately. The discipline was useful: it made Cycle 2 meaningful because the test actually forced a real decision.
- Cycles 4 and 5 (boundary tests) passed immediately because `>=` was already correct. That's not wasted effort — it confirmed the boundary behaviour without having to reason about it manually.
- If building again from scratch: would log each cycle in real time rather than reconstructing it — the reconstruction inevitably smooths over the moments where you second-guessed yourself.
