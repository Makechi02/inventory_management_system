import entity.Inventory;
import entity.Item;
import entity.User;
import entity.UserType;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String FILENAME = "inventory.dat";
    private static final Inventory inventory = Inventory.loadFromFile(FILENAME);
    private static final List<Item> items = inventory.getItems();
    private static final List<User> users = inventory.getUsers();
    private static final Scanner scanner = new Scanner(System.in);

    private static User user;

    public static void main(String[] args) {
        displayAccountActions();
    }

    private static void displayAccountActions() {
        System.out.println("1. Login");
        System.out.println("2. Registration");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1 -> displayLoginMenu();
            case 2 -> displayRegistrationMenu();
        }
    }

    private static void displayLoginMenu() {
        System.out.println("Enter your credentials to login");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User authenticatedUser = authenticateUser(username, password);
        if (authenticatedUser == null) {
            System.out.println("Wrong credentials");
            return;
        }

        user = authenticatedUser;

        if (authenticatedUser.getUserType() == UserType.ADMIN) {
            displayAdminMenu();
        } else if (authenticatedUser.getUserType() == UserType.REGULAR) {
            displayRegularMenu();
        }
    }

    private static void displayRegistrationMenu() {
        System.out.println("Enter your details to continue");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = new User();
        user.setName(username);
        user.setPassword(password);
        user.setUserType(UserType.REGULAR);
        inventory.addUser(user);
        System.out.println("User was added successfully");
        displayLoginMenu();
    }

    private static User authenticateUser(String username, String password) {
        User foundUser = null;

        for (User user: users) {
            if (user.getName().equals(username) && user.getPassword().equals(password)) {
                foundUser = user;
            }
        }

        return foundUser;
    }

    private static void displayAdminMenu() {
        A: while(true) {
            System.out.println("\nINVENTORY MANAGEMENT SYSTEM");
            System.out.println("1). Show all items");
            System.out.println("2). Add items");
            System.out.println("3). Update item");
            System.out.println("4). Delete item");
            System.out.println("5). Update profile");
            System.out.println("6). Delete account");
            System.out.println("7). Exit");
            System.out.print("Choose an option below to continue: ");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> handleShowAllItems();
                case 2 -> handleAddItems();
                case 3 -> handleUpdateItem();
                case 4 -> handleDeleteItem();
                case 5 -> handleUpdateProfile();
                case 6 -> handleDeleteUser();
                case 7 -> {
                    handleExit();
                    break A;
                }
                default -> System.out.println("Invalid choice\nPlease try again!");
            }
        }
    }

    private static void displayRegularMenu() {
        A: while(true) {
            System.out.println("\nINVENTORY MANAGEMENT SYSTEM");
            System.out.println("1). Show all items");
            System.out.println("2). Update item");
            System.out.println("3). Update profile");
            System.out.println("4). Delete account");
            System.out.println("5). Exit");
            System.out.print("Choose an option below to continue: ");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> handleShowAllItems();
                case 2 -> handleUpdateItem();
                case 3 -> handleUpdateProfile();
                case 4 -> handleDeleteUser();
                case 5 -> {
                    handleExit();
                    break A;
                }
                default -> System.out.println("Invalid choice\nPlease try again!");
            }
        }
    }

    private static void handleDeleteUser() {
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
                    handleDeleteUser();
                } else {
                    int index = users.indexOf(user);
                    user = inventory.deleteUser(index);
                    if (user != null) System.out.println("User deleted successfully");
                    System.out.println();
                    Inventory.saveToFile(inventory, FILENAME);
                    displayAccountActions();
                }
            }
            case "n" -> {
                System.out.println("Cancelling request...");
            }
        }
    }

    private static void handleUpdateProfile() {
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
    }

    private static void handleShowAllItems() {
        System.out.println("ALL ITEMS");
        if (items.isEmpty()) {
            System.out.println("No items found");
        } else {
            items.forEach(Item::display);
        }
    }

    private static void handleAddItems() {
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
        System.out.println("Item added successfully");
    }

    private static void handleUpdateItem() {
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
        }
    }

    private static void updateItem(Item item) {
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

    private static void handleDeleteItem() {
        System.out.println("Delete Item");

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
            if (item != null) System.out.println("Item deleted successfully");
        }
    }

    private static void handleExit() {
        Inventory.saveToFile(inventory, FILENAME);
        scanner.close();
    }
}