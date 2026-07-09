package com.fidelity.leap.sprint5;

import java.util.List;

public class OODemo {

    public static void main(String[] args) {

        // --- Part 1: polymorphism and (good) inheritance ---
        // The variable's declared type is Instrument (the abstract base) - but the
        // actual object at runtime is an EquityInstrument or a FundInstrument.
        // Calling calculateFee() runs whichever subclass's version actually applies,
        // decided at runtime based on the real object, not the declared type. This
        // is polymorphism: one method call, different behaviour per concrete type.
        List<Instrument> instruments = List.of(
                new EquityInstrument("AAPL"),
                new FundInstrument("GLBEQ1")
        );

        double tradeValue = 10000.0;
        for (Instrument instrument : instruments) {
            double fee = instrument.calculateFee(tradeValue);
            System.out.println(instrument.getTicker() + ": fee = " + fee);
        }
        // Note: this loop has NO if/else checking "is this an Equity or a Fund?"
        // Adding a third Instrument subclass later requires zero changes here -
        // that's the payoff of designing the hierarchy this way.

        // --- Part 2: encapsulation as a design decision ---
        Holding holding = new Holding(100);
        holding.adjust(-30); // sell 30 units
        System.out.println("Holding quantity after sell: " + holding.getQuantity());

        try {
            holding.adjust(-1000); // would go negative
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }

        // --- Part 3: recognising bad inheritance ---
        // BadClientRegistry extends ArrayList<String> - narrate, don't run this
        // part live in a way that demonstrates "good" behaviour, since the whole
        // point is that it's a trap:
        BadClientRegistry registry = new BadClientRegistry();
        registry.add("C001");
        registry.add("C001"); // ArrayList happily allows a duplicate - the
                                // registry has no way to prevent this, because it
                                // never got the chance to define its own rules.
        System.out.println("Registry (should never have allowed a duplicate): " + registry);
    }
}
