# Module 11 Demo Guide — JUnit

Open `JUnitFeaturesDemoTest.java` and run it first, then walk through it top to bottom.

```bash
mvn test
```

Expect: 14 tests run, 1 skipped, 0 failures. Point out the skipped count before diving in — it's
deliberate, not a problem (see `@Disabled` at the bottom).

## `@BeforeEach` — Fresh State, Every Test

Every lab this sprint has created its test objects inline, inside each test method. `setUp()`
here does it once, and JUnit re-runs it before *every single test method* — each test gets its
own fresh `Holding`, with zero risk of one test's leftover state leaking into another. Ask: what
would happen if `holding` were created once, outside any method, and shared across tests? (Answer:
tests could pass or fail depending on execution order — exactly the kind of flaky, order-dependent
test suite `@BeforeEach` prevents.)

## `assertThrows` — the Formal Way

Point out `adjustBelowZeroThrows()`. Every previous lab that tested for an exception did it with a
manual `try { ... fail(...); } catch (X e) { ... }`, or relied on the test framework failing loudly
if an exception propagated up uncaught. `assertThrows` is shorter, and — this is the important
part — it **fails the test if no exception is thrown at all**, which is easy to get wrong with a
hand-rolled `try/catch` (an empty catch block with nothing after it will silently let the test
pass even if the code never threw).

## `assertAll` — Grouped Assertions

Run `bondReportsTickerAndFeeTogether()` and deliberately break it (e.g., temporarily change
`BondInstrument`'s flat fee) to show **both** failures reported together, not just the first one
JUnit happens to reach. Contrast with what a chain of three separate `assertEquals` calls would
show: only the first failure, hiding the second until the first is fixed and the test is re-run.

## `@ParameterizedTest` — One Test Body, Many Inputs

`bondFeeIsAlwaysFive` and `equityFeeIsOnePercentOfTradeValue` replace what would otherwise be
four or five near-identical copy-pasted test methods. Point out the difference between
`@ValueSource` (one input per run) and `@CsvSource` (an input *and* its expected output, paired).
Ask: when would you reach for one over the other? (`@ValueSource` when the assertion logic is the
same for every input; `@CsvSource` when each input has a different expected result.)

## `@Nested` — Structure the Test Report Itself

`WhenHoldingIsAtZero` groups two tests under a name that reads like a sentence:
"JUnitFeaturesDemoTest > when the holding is at exactly zero > any negative adjustment throws."
This is documentation, generated for free from the test structure — nobody has to keep a separate
spec document in sync with what's actually tested.

## `@Disabled` — Skipping With Intent on Record

The last test is deliberately skipped, with a reason. Contrast with commenting a test out or
deleting it: both lose the fact that the test was *intended* to exist, and why it currently
doesn't run. `@Disabled` keeps that information visible in the test report itself.

## Points to Make Explicitly

- **None of this changes what you were already doing conceptually** — it's the same "write a
  test, make an assertion" pattern from every earlier module, with better tools for organising
  and expressing it.
- **`@DisplayName` matters more than it looks like it should.** A test report full of
  `bondFeeIsAlwaysFive` is fine; a test report full of "a bond's fee is always exactly $5,
  regardless of trade value" is something a non-technical stakeholder could actually read.

## Transition to the Lab

Learners write a comprehensive test suite from scratch for a given, already-implemented class
(`RiskLimitChecker`) — using `@BeforeEach`, `@Nested`, `@ParameterizedTest`, `assertThrows`, and
`assertAll` deliberately, not just `@Test` and `assertEquals` as in earlier modules.
