# Demo: Module 2 — Object-Oriented Principles in Practice

**Duration:** 20 minutes
**Files:** `OODemo.java`, `Instrument.java`, `EquityInstrument.java`, `FundInstrument.java`,
`Holding.java`, `BadClientRegistry.java`

## Part 1: Polymorphism and inheritance, done well (7 min)

Show `Instrument.java`: an `abstract class` with a `ticker` field (real, shared state) and an
`abstract` method `calculateFee(double)` with no body. Narration: `abstract` means this class
can never be instantiated directly — it exists purely to be extended, and it *requires* every
subclass to supply its own `calculateFee`, since the fee genuinely differs per asset class.

Show `EquityInstrument` (percentage fee) and `FundInstrument` (flat fee) — two real, different
implementations of the same method signature.

Run the loop in `OODemo.java`:

```java
List<Instrument> instruments = List.of(new EquityInstrument("AAPL"), new FundInstrument("GLBEQ1"));
for (Instrument instrument : instruments) {
    double fee = instrument.calculateFee(tradeValue);
}
```

Narration: the loop variable's *declared* type is `Instrument`, but the *actual* object at
runtime is an `EquityInstrument` or a `FundInstrument`. Calling `calculateFee()` runs whichever
subclass's version genuinely applies — decided at runtime, based on the real object. That's
polymorphism. Point out what's *not* here: no `if (instrument instanceof EquityInstrument)`
anywhere. Adding a third `Instrument` subclass later needs zero changes to this loop — that's the
actual payoff of designing the hierarchy this way, not just a syntax curiosity.

## Part 2: Encapsulation as a design decision (5 min)

Show `Holding.java`. Narration: the `quantity` field is `private` for a specific reason, not out
of habit — this class has an invariant (quantity can never go negative) that needs protecting.
If `quantity` were a public field, *every* piece of code anywhere that touches a `Holding` would
individually have to remember that rule; one careless line, anywhere, breaks it silently.

Run the demo's `adjust()` calls — a valid sell, then an invalid one that would go negative.
Point out `adjust()` is the *only* way to change `quantity`, and it enforces the rule every
single time, in exactly one place.

Show the commented-out `BadHolding` example at the bottom of the file — a public field version —
and ask the room: what stops someone setting `quantity = -500` directly? Nothing. That's the
actual cost of skipping encapsulation, made concrete.

## Part 3: Recognising bad inheritance (6 min)

Show `BadClientRegistry extends ArrayList<String>`. Narration: this is a real, common mistake —
extending `ArrayList` gives you `add()`, `get()`, `size()`, iteration, "for free." But a client
registry is not an `ArrayList` — there's no genuine "is-a" relationship, only a desire to reuse
storage code.

Run the demo: add `"C001"` twice — `ArrayList` happily allows the duplicate, because
`BadClientRegistry` never got the chance to define its own rule against it. Every `ArrayList`
method (`remove(int)`, `set(int, T)`, `sort(...)`) is now part of this class's public interface
too, whether that was intended or not.

Narration: the fix is **composition, not inheritance** — a registry that *has* an internal list,
wrapped behind a small interface it fully controls, rather than *is* an `ArrayList` with every
`ArrayList` method along for the ride. The lab has you build this fix yourself.

## Key message

Inheritance and polymorphism are powerful when a genuine "is-a" relationship exists and subclasses
truly share behaviour worth centralising. Reaching for inheritance just to reuse code, without
that "is-a" relationship, creates a class with no control over its own interface. Encapsulation
isn't a style preference — it's how a class protects an invariant it's actually responsible for.
