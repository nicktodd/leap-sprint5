package com.fidelity.leap.sprint5;

public class BondInstrument extends Instrument {

    private static final double FLAT_FEE = 5.00;

    public BondInstrument(String ticker) {
        super(ticker);
    }

    @Override
    public double calculateFee(double tradeValue) {
        return FLAT_FEE;
    }
}
