package org.example;

public class Book extends LibraryItem implements Renewable {
    private static final int LOAN_DAYS = 14;
    private static final double DAILY_FINE = 5.00;
    private static final int RENEWAL_LIMIT = 2;

    private final String author;
    private final int pageCount;

    public Book(int catalogueId, String title, String author, int pageCount) {
        super(catalogueId, title);
        this.author = author;
        this.pageCount = pageCount;
    }

    public String getAuthor() {
        return author;
    }

    public int getPageCount() {
        return pageCount;
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
        return "Book";
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

