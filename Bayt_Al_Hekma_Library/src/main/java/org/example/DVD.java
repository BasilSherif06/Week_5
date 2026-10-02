package org.example;

public class DVD extends LibraryItem {
    private static final int LOAN_DAYS = 3;
    private static final double DAILY_FINE = 15.00;

    private final int runtimeMinutes;

    public DVD(int catalogueId, String title, int runtimeMinutes) {
        super(catalogueId, title);
        this.runtimeMinutes = runtimeMinutes;
    }

    public int getRuntimeMinutes() {
        return runtimeMinutes;
    }

    @Override
    public double calculateFine(int overdueDays) {
        if (overdueDays < 0) {
            throw new IllegalArgumentException("Overdue days cannot be negative.");
        }
        return DAILY_FINE * overdueDays;
    }

    @Override
    public int getLoanPeriodDays() {
        return LOAN_DAYS;
    }

    @Override
    public String getCategory() {
        return "DVD";
    }
}

