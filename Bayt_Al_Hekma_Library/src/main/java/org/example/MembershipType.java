package org.example;

public enum MembershipType {
    STANDARD(0.00),
    STUDENT(0.25),
    PREMIUM(0.50);

    private final double waiverRate;

    MembershipType(double waiverRate) {
        this.waiverRate = waiverRate;
    }

    public double getWaiverRate() {
        return waiverRate;
    }
}

