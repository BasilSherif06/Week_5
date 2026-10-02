package org.example;

public class Library {
    public static final int ITEM_CAPACITY = 50;
    public static final int MEMBER_CAPACITY = 20;
    public static final int PROJECTION_DAYS = 5;

    private final LibraryItem[] catalogue = new LibraryItem[ITEM_CAPACITY];
    private int itemCount = 0;
    private final Member[] members = new Member[MEMBER_CAPACITY];
    private int memberCount = 0;

    public boolean registerItem(LibraryItem item) {
        if (item == null) {
            return false;
        }
        if (itemCount >= ITEM_CAPACITY) {
            System.out.println("The catalogue is full. Item rejected.");
            return false;
        }
        if (findItem(item.getCatalogueId()) != null) {
            System.out.println("An item with catalogue ID " + item.getCatalogueId() + " already exists. Item rejected.");
            return false;
        }
        catalogue[itemCount++] = item;
        LibraryItem.recordItemAdded();
        return true;
    }

    public boolean registerMember(Member member) {
        if (member == null) {
            return false;
        }
        if (member.getMembershipId() <= 0) {
            System.out.println("Membership ID must be a positive number. Member rejected.");
            return false;
        }
        if (memberCount >= MEMBER_CAPACITY) {
            System.out.println("The member register is full. Member rejected.");
            return false;
        }
        if (findMember(member.getMembershipId()) != null) {
            System.out.println("A member with ID " + member.getMembershipId() + " already exists. Member rejected.");
            return false;
        }
        members[memberCount++] = member;
        return true;
    }

    public LibraryItem findItem(int catalogueId) {
        for (int i = 0; i < itemCount; i++) {
            if (catalogue[i].getCatalogueId() == catalogueId) {
                return catalogue[i];
            }
        }
        return null;
    }

    public Member findMember(int membershipId) {
        for (int i = 0; i < memberCount; i++) {
            if (members[i].getMembershipId() == membershipId) {
                return members[i];
            }
        }
        return null;
    }

    public void listCatalogue() {
        if (itemCount == 0) {
            System.out.println("The catalogue is empty.");
            return;
        }
        for (int i = 0; i < itemCount; i++) {
            catalogue[i].display();
        }
    }

    public void listByStatus(ItemStatus status) {
        boolean found = false;
        for (int i = 0; i < itemCount; i++) {
            if (catalogue[i].getStatus() == status) {
                catalogue[i].display();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No items with status " + status + ".");
        }
    }

    public void listMembers() {
        if (memberCount == 0) {
            System.out.println("There are no registered members.");
            return;
        }
        for (int i = 0; i < memberCount; i++) {
            members[i].display();
        }
    }

    public int countItemsOnLoan() {
        int count = 0;
        for (int i = 0; i < itemCount; i++) {
            if (catalogue[i].getStatus() == ItemStatus.ON_LOAN) {
                count++;
            }
        }
        return count;
    }

    public double calculateLoanRate() {
        if (itemCount == 0) {
            return 0.0;
        }
        return countItemsOnLoan() * 100.0 / itemCount;
    }

    public double calculateTotalOutstanding() {
        double total = 0;
        for (int i = 0; i < memberCount; i++) {
            total += members[i].getBalanceOwed();
        }
        return total;
    }

    public double calculateProjectedFines(int overdueDays) {
        double total = 0;
        for (int i = 0; i < itemCount; i++) {
            if (catalogue[i].getStatus() == ItemStatus.ON_LOAN) {
                total += catalogue[i].calculateFine(overdueDays);
            }
        }
        return total;
    }

    public boolean lendItem(int itemId, int memberId) {
        LibraryItem item = findItem(itemId);
        if (item == null) {
            System.out.println("No item found with catalogue ID " + itemId + ".");
            return false;
        }
        Member member = findMember(memberId);
        if (member == null) {
            System.out.println("No member found with membership ID " + memberId + ".");
            return false;
        }
        if (item.getStatus() != ItemStatus.AVAILABLE) {
            System.out.println("This item is not available. Current status: " + item.getStatus() + ".");
            return false;
        }
        if (!member.canBorrow()) {
            System.out.println("This member cannot borrow: the limit is " + Member.MAX_ITEMS
                    + " items and the balance must not exceed "
                    + String.format("%.2f", Member.MAX_BALANCE_TO_BORROW) + " EGP.");
            return false;
        }
        item.lend(member.getMembershipId());
        member.recordBorrow();
        System.out.println(item.getTitle() + " has been lent to " + member.getName()
                + ". Loan period: " + item.getLoanPeriodDays() + " days.");
        return true;
    }

    public boolean returnItem(int itemId, int overdueDays) {
        LibraryItem item = findItem(itemId);
        if (item == null) {
            System.out.println("No item found with catalogue ID " + itemId + ".");
            return false;
        }
        if (item.getStatus() != ItemStatus.ON_LOAN) {
            System.out.println("This item is not on loan, so it cannot be returned.");
            return false;
        }
        if (overdueDays < 0) {
            System.out.println("Overdue days cannot be negative.");
            return false;
        }
        Member member = findMember(item.getBorrowerMemberId());
        double baseFine = item.calculateFine(overdueDays);
        if (member != null) {
            if (overdueDays > 0 && baseFine > 0) {
                double waiver = baseFine * member.getMembershipType().getWaiverRate();
                double afterWaiver = baseFine - waiver;
                double total = afterWaiver + LibraryItem.ADMIN_CHARGE;
                member.receiveFine(total);
                System.out.println("--- Fine breakdown ---");
                System.out.printf("Overdue days: %d%n", overdueDays);
                System.out.printf("Base fine: %.2f EGP%n", baseFine);
                System.out.printf("Waiver (%s, %.0f%%): -%.2f EGP%n", member.getMembershipType(),
                        member.getMembershipType().getWaiverRate() * 100, waiver);
                System.out.printf("Administrative charge: +%.2f EGP%n", LibraryItem.ADMIN_CHARGE);
                System.out.printf("Total charged: %.2f EGP%n", total);
                System.out.printf("New balance for %s: %.2f EGP%n", member.getName(), member.getBalanceOwed());
            } else {
                System.out.println("Returned on time. No fine and no administrative charge.");
            }
            member.recordReturn();
        } else {
            System.out.println("Borrower record not found. The item will be returned without a fine.");
        }
        item.returnItem();
        System.out.println(item.getTitle() + " is now AVAILABLE.");
        return true;
    }

    public boolean renewItem(int itemId) {
        LibraryItem item = findItem(itemId);
        if (item == null) {
            System.out.println("No item found with catalogue ID " + itemId + ".");
            return false;
        }
        if (!(item instanceof Renewable)) {
            System.out.println(item.getCategory() + " items cannot be renewed.");
            return false;
        }
        Renewable renewable = (Renewable) item;
        if (item.getStatus() != ItemStatus.ON_LOAN) {
            System.out.println("Only items that are on loan can be renewed.");
            return false;
        }
        if (item.getRenewalsUsed() >= renewable.getRenewalLimit()) {
            System.out.println("The renewal limit of " + renewable.getRenewalLimit()
                    + " has been reached for this " + item.getCategory() + ".");
            return false;
        }
        if (renewable.renew()) {
            System.out.println(item.getTitle() + " renewed. Renewals used: " + item.getRenewalsUsed()
                    + " of " + renewable.getRenewalLimit() + ".");
            return true;
        }
        System.out.println("The renewal could not be completed.");
        return false;
    }

    public boolean markItemLost(int itemId) {
        LibraryItem item = findItem(itemId);
        if (item == null) {
            System.out.println("No item found with catalogue ID " + itemId + ".");
            return false;
        }
        if (item.getStatus() == ItemStatus.ON_LOAN) {
            System.out.println("This item is currently on loan and cannot be marked as lost.");
            return false;
        }
        if (!item.markLost()) {
            System.out.println("Only AVAILABLE items can be marked as lost. Current status: " + item.getStatus() + ".");
            return false;
        }
        System.out.println(item.getTitle() + " has been marked as LOST.");
        return true;
    }

    public boolean payFines(int memberId, double amount) {
        Member member = findMember(memberId);
        if (member == null) {
            System.out.println("No member found with membership ID " + memberId + ".");
            return false;
        }
        if (amount <= 0) {
            System.out.println("The payment must be a positive amount.");
            return false;
        }
        if (!member.makePayment(amount)) {
            System.out.printf("The payment exceeds the balance owed (%.2f EGP).%n", member.getBalanceOwed());
            return false;
        }
        System.out.printf("Payment accepted. New balance for %s: %.2f EGP%n", member.getName(), member.getBalanceOwed());
        return true;
    }

    public void printReport() {
        System.out.println("===== " + LibraryItem.LIBRARY_NAME + " Report =====");
        System.out.println("Catalogue size: " + itemCount);
        System.out.println("Items ever added: " + LibraryItem.getTotalItemsAdded());
        System.out.println("Items on loan: " + countItemsOnLoan());
        System.out.printf("Loan rate: %.1f%%%n", calculateLoanRate());
        System.out.printf("Total outstanding fines: %.2f EGP%n", calculateTotalOutstanding());
        System.out.printf("Projected fines (%d days overdue, before waivers): %.2f EGP%n",
                PROJECTION_DAYS, calculateProjectedFines(PROJECTION_DAYS));
    }
}

