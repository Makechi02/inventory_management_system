package menu;

import entity.Inventory;
import entity.Item;
import entity.User;
import entity.UserType;

import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private final Inventory inventory;
    private final List<Item> items;
    private final List<User> users;
    private final Scanner scanner = new Scanner(System.in);
    private final MainActions mainActions;

    private final User user;

    public AdminMenu(Inventory inventory, MainActions mainActions, User user) {
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
            System.out.println("2). Manage users");
            System.out.println("3). Profile");
            System.out.println("4). Logout");
            System.out.println("5). Exit");
            System.out.print("Choose an option below to continue: ");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> displayManageItemsMenu();
                case 2 -> displayManageUsersMenu();
                case 3 -> displayProfileMenu();
                case 4 -> mainActions.onLogout();
                case 5 -> {
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
            System.out.println("2). Add item");
            System.out.println("3). Update item");
            System.out.println("4). Delete item");
            System.out.println("5). Back to Main Menu");
            System.out.print("Choose an option below to continue: ");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> handleShowAllItems();
                case 2 -> handleAddItem();
                case 3 -> handleUpdateItem();
                case 4 -> handleDeleteItem();
                case 5 -> {
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

    private void handleAddItem() {
        System.out.println("ADD ITEM");

        System.out.println("Enter item details: ");
        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Description: ");
        String description = scanner.nextLine();

        System.out.print("Quantity: ");
        int quantity = scanner.nextInt();

        System.out.print("Price: ");
        double price = scanner.nextDouble();

        Item item = new Item(name, description, quantity, price);
        inventory.addItem(item);
        mainActions.saveChanges();
        System.out.println("Item added successfully");
    }

    private void handleUpdateItem() {
        System.out.println("UPDATE ITEM");

        System.out.print("Enter name of item to update: ");
        String name = scanner.nextLine();
        int index = -1;

        for (Item item : items) {
            if (item.getName().equals(name)) {
                index = items.indexOf(item);
            }
        }

        if (index == -1) System.out.println("Item not found");
        else {
            Item item = inventory.getItem(index);
            updateItem(item);
            inventory.updateItem(index, item);
            mainActions.saveChanges();
            System.out.println("Item updated successfully");
        }
    }

    private void updateItem(Item item) {
        item.display();
        System.out.println("Choose an attribute to update:");
        System.out.println("1. Name");
        System.out.println("2. Description");
        System.out.println("3. Quantity");
        System.out.println("4. Price");
        System.out.println("5. Done");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                System.out.print("Enter new name: ");
                String newName = scanner.nextLine();
                item.setName(newName);
            }
            case 2 -> {
                System.out.print("Enter new description: ");
                String newDescription = scanner.nextLine();
                item.setDescription(newDescription);
            }
            case 3 -> {
                System.out.print("Enter new quantity: ");
                int newQuantity = scanner.nextInt();
                item.setQuantity(newQuantity);
            }
            case 4 -> {
                System.out.print("Enter new price: ");
                double newPrice = scanner.nextDouble();
                item.setPrice(newPrice);
            }
            case 5 -> {
                System.out.println();
                return;
            }
            default -> {
                System.out.println("Invalid choice\nPlease try again");
                updateItem(item);
            }
        }
        updateItem(item);
    }

    private void handleDeleteItem() {
        System.out.println("DELETE ITEM");
        System.out.print("Enter name of item to delete: ");
        String name = scanner.nextLine();

        int index = -1;
        for (Item item : items) {
            if (item.getName().equals(name)) {
                index = items.indexOf(item);
            }
        }

        if (index == -1) System.out.println("Item not found");
        else {
            Item item = inventory.deleteItem(index);
            mainActions.saveChanges();
            if (item != null) System.out.println("Item deleted successfully");
        }
    }

    private void displayManageUsersMenu() {
        A: while(true) {
            System.out.println("\nINVENTORY MANAGEMENT SYSTEM");
            System.out.println("1). Show all users");
            System.out.println("2). Add user");
            System.out.println("3). Back to main menu");
            System.out.print("Choose an option below to continue: ");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> handleShowAllUsers();
                case 2 -> handleAddUser();
                case 3 -> {
                    break A;
                }
                default -> System.out.println("Invalid choice\nPlease try again!");
            }
        }
    }

    private void handleShowAllUsers() {
        System.out.println("ALL USERS");
        if (users.isEmpty()) {
            System.out.println("No users found");
        } else {
            users.forEach(User::display);
        }
    }

    private void handleAddUser() {
        System.out.println("Enter user details to continue");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("User type: ");
        String userType = scanner.nextLine();

        User user = new User();
        user.setName(username);
        user.setPassword(password);
        user.setUserType(UserType.valueOf(userType));
        inventory.addUser(user);
        mainActions.saveChanges();
        System.out.println("User was added successfully");
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