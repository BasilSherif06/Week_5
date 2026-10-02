package org.example;

public class Magazine extends LibraryItem implements Renewable {
    private static final int LOAN_DAYS = 7;
    private static final double DAILY_FINE = 3.00;
    private static final double MAX_FINE = 30.00;
    private static final int RENEWAL_LIMIT = 1;

    private final int issueNumber;

    public Magazine(int catalogueId, String title, int issueNumber) {
        super(catalogueId, title);
        this.issueNumber = issueNumber;
    }

    public int getIssueNumber() {
        return issueNumber;
    }

    @Override
    public double calculateFine(int overdueDays) {
        if (overdueDays < 0) {
            throw new IllegalArgumentException("Overdue days cannot be negative.");
        }
        return Math.min(DAILY_FINE * overdueDays, MAX_FINE);
    }

    @Override
    public int getLoanPeriodDays() {
        return LOAN_DAYS;
    }

    @Override
    public String getCategory() {
        return "Magazine";
    }

    @Override
    public boolean renew() {
        return tryRenew(RENEWAL_LIMIT);
    }

    @Override
    public int getRenewalLimit() {
        return RENEWAL_LIMIT;
    }
}

