# OOAD Worksheet — Order Processing & Settlement Engine

Pair names: Zack Todd

## Step 1-2: Candidate nouns and verbs (brain-dump, no filtering)

Nouns: client, portfolio, holding, order, instrument, risk limit, fee, confirmation, settlement report, quantity, price, advisor, batch, reason, validation rule, report format

Verbs: submit, validate, reject, execute, calculate, update, record, produce, accept, extend, add

## Step 3: Class or attribute?

| Candidate | Class or attribute? | Reasoning |
|---|---|---|
| Client | Class | Has identity, holds a portfolio, has a risk limit — behaviour and state of its own |
| Portfolio | Class | Has its own behaviour (holds multiple holdings, computes total value) — not just a field on Client |
| Holding | Class | Represents quantity of a specific instrument; has adjust() behaviour — not just a number |
| Order | Class | Has identity, state (pending/accepted/rejected), and travels through the system |
| Instrument | Class | Has its own fee rules (differ by type) — abstract class with subclasses |
| Risk limit | Attribute | A single numeric threshold stored on Client — no behaviour of its own |
| Fee | Attribute | A calculated double attached to a confirmed Order — not an independent entity |
| Confirmation | Class | Records the outcome of a successful order execution — has its own state |
| Settlement report | Class | Aggregates all confirmations and rejections for the day — has produce() behaviour |

## Step 4: Responsibilities (verb -> owning class)

| Responsibility (verb) | Owning class | Why this class, not another? |
|---|---|---|
| Validate an order | OrderValidator | Keeps validation rules separate from Order itself — easy to add new rules (req 7) |
| Reject an order | Order | The order knows its own state; rejection records the reason on the order |
| Calculate a fee | Instrument (subclass) | Fee rules differ by instrument type — polymorphism handles new types without changing other logic |
| Execute an order | OrderProcessor | Coordinates fee calculation, holding update, and confirmation — separate from validation |
| Update a holding | Holding | Holding owns its own quantity — adjust() encapsulates the state change |
| Produce the settlement report | SettlementReport | Knows how to format and output its own contents |

## Step 5: Relationships (sketch informally)

- Client **has one** Portfolio
- Portfolio **has many** Holdings (one per instrument)
- Holding **refers to** one Instrument
- Order **refers to** Client, Instrument, quantity, price, side
- OrderValidator **depends on** Order and Portfolio (needs portfolio to check sell/buy limits)
- OrderProcessor **depends on** OrderValidator, Holding, Instrument (for fee), SettlementReport
- SettlementReport **has many** Order outcomes (accepted + rejected)

## The one genuinely ambiguous call we made, and why

**Is Portfolio its own class, or just a collection living on Client?**

We made it its own class. A `Client` could just have a `List<Holding>` and a `totalValue()` method, which would be simpler. But requirement 7 says new report formats and validation rules should be addable without rewriting existing logic — if Portfolio is its own class, it can expose `totalValue()`, `getHolding(instrument)`, and `wouldExceedRiskLimit(order)` as a clean interface, rather than OrderValidator reaching inside Client to do arithmetic. The extra class is worth it for that boundary.
