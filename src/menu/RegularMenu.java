package menu;

import entity.Inventory;
import entity.Item;
import entity.User;

import java.util.List;
import java.util.Scanner;

public class RegularMenu {
    private final Inventory inventory;
    private final List<Item> items;
    private final List<User> users;
    private final Scanner scanner = new Scanner(System.in);
    private final MainActions mainActions;

    private final User user;

    public RegularMenu(Inventory inventory, MainActions mainActions, User user) {
        this.inventory = inventory;
        items = inventory.getItems();
        users = inventory.getUsers();
        this.mainActions = mainActions;
        this.user = user;
    }

    public void displayMenu() {
        A: while(true) {
            System.out.println("\nINVENTORY MANAGEMENT SYSTEM");
            System.out.println("1). Manage items");
            System.out.println("2). Profile");
            System.out.println("3). Logout");
            System.out.println("4). Exit");
            System.out.print("Choose an option below to continue: ");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> displayManageItemsMenu();
                case 2 -> displayProfileMenu();
                case 3 -> mainActions.onLogout();
                case 4 -> {
                    mainActions.onExit();
                    break A;
                }
                default -> System.out.println("Invalid choice\nPlease try again!");
            }
        }
    }

    private void displayManageItemsMenu() {
        A: while(true) {
            System.out.println("\nINVENTORY MANAGEMENT SYSTEM");
            System.out.println("1). Show all items");
            System.out.println("2). Back to Main Menu");
            System.out.print("Choose an option below to continue: ");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> handleShowAllItems();
                case 2 -> {
                    break A;
                }
                default -> System.out.println("Invalid choice\nPlease try again!");
            }
        }
    }

    private void handleShowAllItems() {
        System.out.println("ALL ITEMS");
        if (items.isEmpty()) {
            System.out.println("No items found");
        } else {
            items.forEach(Item::display);
        }
    }

    private void displayProfileMenu() {
        A: while(true) {
            System.out.println("\nINVENTORY MANAGEMENT SYSTEM");
            System.out.println("1). Show profile");
            System.out.println("2). Update profile");
            System.out.println("3). Delete account");
            System.out.println("4). Back to main menu");
            System.out.print("Choose an option below to continue: ");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> user.display();
                case 2 -> handleUpdateProfile();
                case 3 -> handleDeleteAccount();
                case 4 -> {
                    break A;
                }
                default -> System.out.println("Invalid choice\nPlease try again!");
            }
        }
    }

    private void handleUpdateProfile() {
        System.out.println("UPDATE PROFILE");
        System.out.println("Current details");
        System.out.println(user);
        System.out.println("Choose an attribute to update");
        System.out.println("1. Name");
        System.out.println("2. Password");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                System.out.print("Enter new name: ");
                String name = scanner.nextLine();
                user.setName(name);
            }
            case 2 -> {
                System.out.print("Enter new password: ");
                String password = scanner.nextLine();
                user.setPassword(password);
            }
        }

        int index = users.indexOf(user);
        inventory.updateUser(index, user);
        mainActions.saveChanges();
        System.out.println("Profile updated successfully");
    }

    private void handleDeleteAccount() {
        System.out.println("DELETE ACCOUNT");
        System.out.print("Are you sure you want to delete account? (y/n): ");
        String response = scanner.next();
        scanner.nextLine();

        switch (response.toLowerCase()) {
            case "y" -> {
                System.out.print("Type in your password to delete: ");
                String password = scanner.nextLine();
                if (!password.equals(user.getPassword())) {
                    System.out.println("Incorrect password");
                    handleDeleteAccount();
                } else {
                    int index = users.indexOf(user);
                    User deletedUser = inventory.deleteUser(index);
                    if (deletedUser != null) System.out.println("User deleted successfully");
                    mainActions.saveChanges();
                    mainActions.onLogout();
                }
            }
            case "n" -> System.out.println("Cancelling request...");
        }
    }

}
