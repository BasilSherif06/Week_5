package org.example;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Library library = new Library();
        loadSampleData(library);
        try {
            boolean running = true;
            while (running) {
                printMenu();
                int choice = readInt("Choose an option: ");
                System.out.println();
                running = handle(library, choice);
                System.out.println();
            }
        } catch (NoSuchElementException e) {
            System.out.println();
        }
        System.out.println("Goodbye!");
    }

    private static void loadSampleData(Library library) {
        library.registerItem(new Book(101, "The Alchemist", "Paulo Coelho", 208));
        library.registerItem(new Book(102, "Cairo Trilogy", "Naguib Mahfouz", 1313));
        library.registerItem(new Magazine(201, "National Geographic", 45));
        library.registerItem(new Magazine(202, "Science Today", 12));
        library.registerItem(new DVD(301, "Inception", 148));
        library.registerMember(new Member("Basil", 1, MembershipType.STANDARD));
        library.registerMember(new Member("Nour", 2, MembershipType.STUDENT));
        library.registerMember(new Member("Salma", 3, MembershipType.PREMIUM, 40.00));
    }

    private static void printMenu() {
        System.out.println("===== " + LibraryItem.LIBRARY_NAME + " =====");
        System.out.println("1. View catalogue");
        System.out.println("2. Register member");
        System.out.println("3. Borrow item");
        System.out.println("4. Return item");
        System.out.println("5. Renew loan");
        System.out.println("6. Search item by ID");
        System.out.println("7. View items by status");
        System.out.println("8. Pay outstanding fines");
        System.out.println("9. View all members");
        System.out.println("10. Library report");
        System.out.println("11. Mark item as lost");
        System.out.println("0. Exit");
    }

    private static boolean handle(Library library, int choice) {
        switch (choice) {
            case 1:
                library.listCatalogue();
                break;
            case 2: {
                String name = readText("Name: ");
                int id = readInt("Membership ID: ");
                MembershipType type = readMembershipType();
                if (library.registerMember(new Member(name, id, type))) {
                    System.out.println("Member registered.");
                }
                break;
            }
            case 3: {
                int itemId = readInt("Item catalogue ID: ");
                int memberId = readInt("Membership ID: ");
                library.lendItem(itemId, memberId);
                break;
            }
            case 4: {
                int itemId = readInt("Item catalogue ID: ");
                int days = readInt("Overdue days (0 if on time): ");
                library.returnItem(itemId, days);
                break;
            }
            case 5:
                library.renewItem(readInt("Item catalogue ID: "));
                break;
            case 6: {
                LibraryItem item = library.findItem(readInt("Item catalogue ID: "));
                if (item == null) {
                    System.out.println("Item not found.");
                } else {
                    item.display();
                }
                break;
            }
            case 7:
                library.listByStatus(readStatus());
                break;
            case 8: {
                int memberId = readInt("Membership ID: ");
                double amount = readDouble("Payment amount: ");
                library.payFines(memberId, amount);
                break;
            }
            case 9:
                library.listMembers();
                break;
            case 10:
                library.printReport();
                break;
            case 11:
                library.markItemLost(readInt("Item catalogue ID: "));
                break;
            case 0:
                return false;
            default:
                System.out.println("Invalid option. Please choose a number from 0 to 11.");
        }
        return true;
    }

    private static MembershipType readMembershipType() {
        while (true) {
            System.out.print("Category (STANDARD, STUDENT, PREMIUM): ");
            String line = scanner.nextLine().trim().toUpperCase();
            try {
                return MembershipType.valueOf(line);
            } catch (IllegalArgumentException e) {
                System.out.println("Please enter STANDARD, STUDENT or PREMIUM.");
            }
        }
    }

    private static ItemStatus readStatus() {
        while (true) {
            System.out.print("Status (AVAILABLE, ON_LOAN, LOST): ");
            String line = scanner.nextLine().trim().toUpperCase().replace(' ', '_');
            try {
                return ItemStatus.valueOf(line);
            } catch (IllegalArgumentException e) {
                System.out.println("Please enter AVAILABLE, ON_LOAN or LOST.");
            }
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(line);
                if (Double.isNaN(value) || Double.isInfinite(value)) {
                    System.out.println("Please enter a valid number.");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("This value must not be empty.");
        }
    }
}
