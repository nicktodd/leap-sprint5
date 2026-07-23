# Peer Review — Mission Model (self-review)

Reviewer: Zack Todd  
Design reviewed: own mission-model.mmd + mission-order-sequence.mmd

---

## Written comments

**Comment 1 — OrderProcessor does too much**
OrderProcessor currently coordinates validation, fee calculation, holding updates, and settlement reporting. That's four distinct responsibilities. Consider extracting an `OrderExecutor` that handles just the execution step (fee + holding update), leaving OrderProcessor as a thin coordinator. This would make it easier to add new execution steps without touching validation logic.

**Comment 2 — Confirmation class is defined but never used in the sequence diagram**
The class diagram includes a `Confirmation` class, but the sequence diagram never shows it being created after a successful execution. Either the sequence diagram should show `OrderProcessor` creating a `Confirmation` and passing it to `SettlementReport`, or `Confirmation` should be removed from the class diagram if `Order` itself carries that information.

---

## Decision

**Request changes** — the two issues above are worth fixing before Module 12's build. The Confirmation gap in particular would cause confusion when implementing the settlement report, since it's unclear whether the report holds Orders or Confirmations.

---

## Response to imagined partner comment

A partner might note: "Why does Client call produce() on SettlementReport directly? The client shouldn't know about the report."  
Response: Fair point — in a real system a batch runner or scheduler would trigger the report, not the client. I put it on Client for simplicity in the sequence diagram, but it should be a separate actor (e.g. BatchRunner) in a more complete design.
