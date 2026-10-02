package org.example;

public abstract class LibraryItem {
    public static final String LIBRARY_NAME = "Bayt Al Hekma Library";
    public static final double ADMIN_CHARGE = 5.00;
    public static final int NO_BORROWER = 0;

    private static int totalItemsAdded = 0;

    private final int catalogueId;
    private final String title;
    private ItemStatus status;
    private int borrowerMemberId;
    private int renewalsUsed;

    protected LibraryItem(int catalogueId, String title) {
        this.catalogueId = catalogueId;
        this.title = title;
        this.status = ItemStatus.AVAILABLE;
        this.borrowerMemberId = NO_BORROWER;
        this.renewalsUsed = 0;
    }

    public static int getTotalItemsAdded() {
        return totalItemsAdded;
    }

    public static void recordItemAdded() {
        totalItemsAdded++;
    }

    public int getCatalogueId() {
        return catalogueId;
    }

    public String getTitle() {
        return title;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public int getBorrowerMemberId() {
        return borrowerMemberId;
    }

    public int getRenewalsUsed() {
        return renewalsUsed;
    }

    public abstract double calculateFine(int overdueDays);

    public abstract int getLoanPeriodDays();

    public abstract String getCategory();

    public boolean markLost() {
        if (status != ItemStatus.AVAILABLE) {
            return false;
        }
        status = ItemStatus.LOST;
        return true;
    }

    public boolean markFound() {
        if (status != ItemStatus.LOST) {
            return false;
        }
        status = ItemStatus.AVAILABLE;
        return true;
    }

    public boolean lend(int memberId) {
        if (status != ItemStatus.AVAILABLE) {
            return false;
        }
        status = ItemStatus.ON_LOAN;
        borrowerMemberId = memberId;
        renewalsUsed = 0;
        return true;
    }

    public final boolean returnItem() {
        if (status != ItemStatus.ON_LOAN) {
            return false;
        }
        status = ItemStatus.AVAILABLE;
        borrowerMemberId = NO_BORROWER;
        renewalsUsed = 0;
        return true;
    }

    protected void recordRenewal() {
        renewalsUsed++;
    }

    protected boolean tryRenew(int limit) {
        if (status != ItemStatus.ON_LOAN || renewalsUsed >= limit) {
            return false;
        }
        recordRenewal();
        return true;
    }

    public void display() {
        String borrower = borrowerMemberId == NO_BORROWER ? "-" : String.valueOf(borrowerMemberId);
        System.out.printf("ID: %d | %s | %s | Status: %s | Borrower: %s | Loan period: %d days | Fine/day: %.2f EGP%n",
                catalogueId, getCategory(), title, status, borrower, getLoanPeriodDays(), calculateFine(1));
    }
}

