# Design Rationale — Zack Todd

## Decision 1: OrderProcessor vs separate OrderValidator + OrderExecutor

**What Module 3's worksheet said:**
A single `OrderProcessor` class coordinates validation, execution, and reporting.

**What Module 4's class diagram said:**
`OrderProcessor` depended on `OrderValidator` and `SettlementReport` as separate classes, but still did the execution logic itself.

**What Module 13 actually built:**
`OrderProcessingEngine` coordinates between `OrderValidator`, `HoldingUpdater`, and `SettlementReport` — execution logic lives in `HoldingUpdater`, not the engine itself.

**Survived unchanged / revised / dropped?**
Revised.

**What caused the change, and in which module did that happen?**
Module 7 (SRP) — the engine doing both orchestration and holding-update logic in the same place was a single-responsibility violation. Extracting `HoldingUpdater` gave holding mutation its own home, tested independently in Module 12.

---

## Decision 2: Fee as an attribute vs Fee as a class

**What Module 3's worksheet said:**
Fee is an attribute (a double) attached to a confirmed order — not a class of its own.

**What Module 4's class diagram said:**
Same — no `Fee` class, just a `fee` field on `Confirmation`.

**What Module 13 actually built:**
Fee calculation is entirely delegated to `Instrument.calculateFee()` — no `Confirmation` class either. The fee value flows directly into `SettlementReport.recordAccepted()`.

**Survived unchanged / revised / dropped?**
The "fee as attribute" decision survived. The `Confirmation` class was dropped — the settlement report records everything directly.

**What caused the change, and in which module did that happen?**
Module 9 (Clean Code / YAGNI) — `Confirmation` added a layer without adding behaviour. The settlement report already records everything needed; a `Confirmation` wrapper would have been a data class with no logic, which Module 9's checklist flags as a smell.

---

## Decision 3: Portfolio as its own class

**What Module 3's worksheet said:**
Portfolio should be its own class rather than a list on Client, because it can expose `totalValue()` and `wouldExceedRiskLimit()` cleanly.

**What Module 4's class diagram said:**
`Client *-- Portfolio`, `Portfolio *-- Holding` — composition, own class.

**What Module 13 actually built:**
`Portfolio` is its own class holding a `Map<String, Holding>` and a `totalValue` field, with `getHolding()` and `adjustTotalValue()`. The risk-limit check lives in `OrderValidator`, not `Portfolio`.

**Survived unchanged / revised / dropped?**
Mostly survived. The responsibility split changed — `Portfolio` doesn't do the risk-limit check itself; `OrderValidator` does it using `portfolio.getTotalValue()` and `client.getRiskLimit()`.

**What caused the change, and in which module did that happen?**
Module 8 (DIP) — `OrderValidator` taking primitive values (current portfolio value, risk limit) rather than a `Portfolio` object made it easier to test in isolation (Module 12) without constructing a whole object graph.

---

## Overall Reflection

- **Which single module changed the design most?** Module 7 (SOLID Part 1, SRP) — splitting responsibilities into focused classes rippled through everything that came after.
- **Would design differently?** Would not put `wouldExceedRiskLimit` on `Portfolio` at all from the start — that responsibility clearly belongs to `OrderValidator`.
- **Confident decision that turned out wrong?** The `Confirmation` class felt essential in Module 3. By Module 9 it was obviously unnecessary. Writing a test for it in Module 12 would have surfaced this earlier — there's nothing to test on a class that only holds data and has no behaviour.
