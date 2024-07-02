import entity.Inventory;
import entity.Item;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String FILENAME = "inventory.dat";
    private static final Inventory inventory = Inventory.loadFromFile(FILENAME);
    private static final List<Item> items = inventory.getItems();

    public static void main(String[] args) {
        displayMenu();
    }

    private static void displayMenu() {
        System.out.println("\nINVENTORY MANAGEMENT SYSTEM");
        System.out.println("1). Show all items");
        System.out.println("2). Add items");
        System.out.println("3). Update item");
        System.out.println("4). Delete item");
        System.out.println("5). Exit");
        System.out.print("Choose an option below to continue: ");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        handleMenuChoice(choice);
    }

    private static void handleMenuChoice(int choice) {
        switch (choice) {
            case 1 -> handleShowAllItems();
            case 2 -> handleAddItems();
            case 3 -> handleUpdateItem();
            case 4 -> handleDeleteItem();
            case 5 -> handleExit();
            default -> {
                System.out.println("Invalid choice\nPlease try again!");
                displayMenu();
            }
        }
    }

    private static void handleShowAllItems() {
        System.out.println("ALL ITEMS");
        if (items.isEmpty()) {
            System.out.println("No items found");
        } else {
            items.forEach(Item::display);
        }
        displayMenu();
    }

    private static void handleAddItems() {
        System.out.println("ADD ITEM");
        Scanner scanner = new Scanner(System.in);

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
        displayMenu();
    }

    private static void handleUpdateItem() {
        System.out.println("UPDATE ITEM");

        Scanner scanner = new Scanner(System.in);
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

        displayMenu();
    }

    private static void updateItem(Item item) {
        Scanner scanner = new Scanner(System.in);
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

        Scanner scanner = new Scanner(System.in);
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

        displayMenu();
    }

    private static void handleExit() {
        Inventory.saveToFile(inventory, FILENAME);
        System.exit(0);
    }

}