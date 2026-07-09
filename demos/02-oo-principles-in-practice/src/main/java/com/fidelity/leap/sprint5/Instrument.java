package com.fidelity.leap.sprint5;

// An abstract class: it cannot be instantiated directly (new Instrument(...) is
// illegal), it exists only to be extended. calculateFee() has no body here - each
// concrete subclass MUST provide its own, since the fee structure genuinely differs
// per asset class. This is inheritance used correctly: every subclass really "is an"
// Instrument, and shares real, common state (ticker) and behaviour.
public abstract class Instrument {

    private final String ticker;

    protected Instrument(String ticker) {
        this.ticker = ticker;
    }

    public String getTicker() {
        return ticker;
    }

    // No implementation here - subclasses are required to provide one.
    public abstract double calculateFee(double tradeValue);
}
