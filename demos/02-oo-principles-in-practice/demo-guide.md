# Demo: Module 2 — Object-Oriented Principles in Practice

**Duration:** 26 minutes
**Files:** `OODemo.java`, `Trade.java`, `ConcreteInheritanceProblem.java`, `Instrument.java`,
`BondInstrument.java`, `EquityInstrument.java`, `FundInstrument.java`, `Feeable.java`,
`AccountMaintenanceCharge.java`, `Holding.java`, `BadClientRegistry.java`

Every code example referenced in this guide is shown **before** it's discussed, in the same
order `OODemo.java` runs them — nothing here refers to code the room hasn't seen yet.

## Part 0: Class vs. object (4 min)

Show `Trade` (from Module 1) and the first lines of `OODemo.java`:

```java
Trade tradeOne = new Trade("T0001", "Alice Chen", "AAPL", 120, 185.32, "BUY");
Trade tradeTwo = new Trade("T0002", "Ben Whitfield", "MSFT", 60, 402.11, "BUY");
```

Narration: `Trade` (the `.java` file, the `class` keyword) is a **blueprint** — it has no data
of its own. Each `new Trade(...)` call builds a separate **object**: a real, independent thing in
memory, with its own copy of every field. Run the demo's own check:

```java
System.out.println("Same class? " + (tradeOne.getClass() == tradeTwo.getClass())); // true
System.out.println("Same object? " + (tradeOne == tradeTwo));                       // false
```

Narration: same *class*, two different *objects* — changing `tradeOne`'s quantity would never
affect `tradeTwo`. Python equivalent: `class Trade: ...` then `t1 = Trade(...)`, `t2 = Trade(...)`
— identical idea, Java just always requires the explicit `new`.

## Part 1: Inheriting from a concrete class — and its problem (5 min)

Show `ConcreteInstrument` in `ConcreteInheritanceProblem.java`: a normal, instantiable class with
a real, working `calculateFee()` that returns a 0.1% default. Show `EquityInstrumentV1 extends
ConcreteInstrument` — no override, and it happens to be correct, since 0.1% is right for an
equity.

Now show `BondInstrumentBuggy extends ConcreteInstrument` — also no override. Run the demo:

```
EquityInstrumentV1 fee (correct by coincidence): 10.0
BondInstrumentBuggy fee (SHOULD be a flat $5.00): 10.0  <- silently wrong
```

Narration: bonds should charge a flat $5.00, not a percentage — whoever wrote
`BondInstrumentBuggy` forgot to override `calculateFee()`, and the compiler said nothing, because
the inherited method is a real, callable one. This bug is invisible until someone notices the
number is wrong, possibly in production.

## Part 2: The fix — an abstract class (5 min)

Show `Instrument.java`: `public abstract class Instrument`, with `calculateFee()` having **no
body at all**. Narration: `abstract` means two things — the class itself can never be
instantiated directly (`new Instrument("AAPL")` is a compile error), and any subclass that
doesn't override `calculateFee()` **will not compile**. Show `BondInstrument.java` — the
corrected version — and narrate: the Step 1 bug is now a compiler error, not a silent mistake,
if you deleted this method the whole project would refuse to build.

Run the demo's output for both `EquityInstrument` and the corrected `BondInstrument` (5.0, not
10.0) to show the fix landing.

## Part 3: Interfaces — a capability, not a hierarchy (6 min)

Show `Feeable.java`: `public interface Feeable { double calculateFee(double tradeValue); }`.
Narration: an interface is a pure contract — a method signature, no body, no state at all, not
even a private field. `Instrument implements Feeable` (point back at `Instrument.java`'s class
declaration).

Now show `AccountMaintenanceCharge.java`: `implements Feeable`, but does **not** extend
`Instrument` at all — no ticker, no trade-related state, nothing in common with the `Instrument`
hierarchy. Narration: this is exactly the problem abstract classes alone can't solve. Forcing
`AccountMaintenanceCharge` into the `Instrument` hierarchy just to reuse `calculateFee()` would
be precisely the reuse-only inheritance mistake Part 6 warns about. A class can `extend` only one
other class, but can `implement` as many interfaces as it genuinely needs.

## Part 4: Polymorphism across the interface (3 min)

```java
List<Feeable> feeableThings = List.of(equity, bond, maintenanceCharge);
for (Feeable feeable : feeableThings) {
    totalFees += feeable.calculateFee(tradeValue);
}
```

Narration: this list holds genuinely unrelated classes — two `Instrument` subclasses and one
completely unrelated `AccountMaintenanceCharge` — united only by the one capability they share.
Polymorphism isn't limited to a class hierarchy; it works through an interface exactly the same
way.

## Part 5: Encapsulation as a design decision (4 min)

Show `Holding.java`. Narration: `quantity` is `private` for a specific reason — this class has an
invariant (quantity can never go negative) that needs protecting. Run the demo's valid `adjust()`
call, then the rejected one. `adjust()` is the *only* way to change `quantity`, and it enforces
the rule every time, in exactly one place — not because every caller remembers to check.

## Part 6: Recognising bad inheritance (3 min)

Show `BadClientRegistry extends ArrayList<String>`. Run the demo: adding `"C001"` twice succeeds
— `ArrayList` allows the duplicate, because `BadClientRegistry` never got the chance to define
its own rule. Narration: the fix is composition, not inheritance — the lab has you build it.

## Key message

Concrete inheritance works, but nothing stops a subclass from silently forgetting to override
something it should. Abstract classes turn that silent mistake into a compiler error. Interfaces
solve a different problem entirely: a capability shared across classes that aren't related by
inheritance at all. All three — concrete, abstract, and interface — are real tools, each suited to
a different situation, not a strict "always prefer the newest one" ladder.
