package org.example;

public class Member {
    public static final int MAX_ITEMS = 3;
    public static final double MAX_BALANCE_TO_BORROW = 100.00;

    private String name;
    private final int membershipId;
    private final MembershipType membershipType;
    private double balanceOwed;
    private int itemsHeld;

    public Member(String name, int membershipId, MembershipType membershipType) {
        this(name, membershipId, membershipType, 0.0);
    }

    public Member(String name, int membershipId, MembershipType membershipType, double balanceOwed) {
        if (balanceOwed < 0) {
            throw new IllegalArgumentException("Balance cannot be negative.");
        }
        this.name = name;
        this.membershipId = membershipId;
        this.membershipType = membershipType;
        this.balanceOwed = round(balanceOwed);
        this.itemsHeld = 0;
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMembershipId() {
        return membershipId;
    }

    public MembershipType getMembershipType() {
        return membershipType;
    }

    public double getBalanceOwed() {
        return balanceOwed;
    }

    public int getItemsHeld() {
        return itemsHeld;
    }

    public boolean receiveFine(double amount) {
        if (amount <= 0) {
            return false;
        }
        balanceOwed = round(balanceOwed + amount);
        return true;
    }

    public boolean makePayment(double amount) {
        if (amount <= 0 || round(amount) > balanceOwed) {
            return false;
        }
        balanceOwed = round(balanceOwed - amount);
        return true;
    }

    public boolean canBorrow() {
        return itemsHeld < MAX_ITEMS && balanceOwed <= MAX_BALANCE_TO_BORROW;
    }

    public void recordBorrow() {
        itemsHeld++;
    }

    public void recordReturn() {
        if (itemsHeld > 0) {
            itemsHeld--;
        }
    }

    public void display() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        return String.format("%s | ID: %d | Category: %s | Items held: %d | Balance owed: %.2f EGP",
                name, membershipId, membershipType, itemsHeld, balanceOwed);
    }
}

