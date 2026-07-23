package com.fidelity.leap.sprint5;

public class AnnualServiceFee implements Feeable {

    @Override
    public double calculateFee(double tradeValue) {
        return 30.00;
    }
}
