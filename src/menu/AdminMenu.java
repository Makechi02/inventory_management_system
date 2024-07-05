package menu;

import entity.*;
import service.category.CategoryService;
import service.category.CategoryServiceImpl;
import service.item.ItemService;
import service.item.ItemServiceImpl;
import service.user.UserService;
import service.user.UserServiceImpl;

import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private final UserService userService = new UserServiceImpl();
    private final ItemService itemService = new ItemServiceImpl();
    private final CategoryService categoryService = new CategoryServiceImpl();

    private final List<Item> items = itemService.getAllItems();
    private final Scanner scanner = new Scanner(System.in);
    private final MainActions mainActions;
    private final UserActions userActions;

    private final User user;

    public AdminMenu(MainActions mainActions, UserActions userActions, User user) {
        this.mainActions = mainActions;
        this.userActions = userActions;
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
            System.out.println("5). Manage categories");
            System.out.println("6). Back to Main Menu");
            System.out.print("Choose an option below to continue: ");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> handleShowAllItems();
                case 2 -> handleAddItem();
                case 3 -> handleUpdateItem();
                case 4 -> handleDeleteItem();
                case 5 -> displayManageCategoriesMenu();
                case 6 -> {
                    break A;
                }
                default -> System.out.println("Invalid choice\nPlease try again!");
            }
        }
    }

    private void displayManageCategoriesMenu() {
        A: while(true) {
            System.out.println("\nINVENTORY MANAGEMENT SYSTEM");
            System.out.println("1). Show all categories");
            System.out.println("2). Add category");
            System.out.println("3). Update category");
            System.out.println("4). Delete category");
            System.out.println("5). Back");
            System.out.print("Choose an option below to continue: ");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> handleShowAllCategories();
                case 2 -> handleAddCategory();
                case 3 -> handleUpdateCategory();
                case 4 -> handleDeleteCategory();
                case 5 -> {
                    break A;
                }
                default -> System.out.println("Invalid choice\nPlease try again!");
            }
        }
    }

    private void handleShowAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        System.out.println("ALL CATEGORIES");
        if (categories.isEmpty()) {
            System.out.println("No categories found");
        } else {
            categories.forEach(System.out::println);
        }
    }

    private void handleAddCategory() {
        System.out.println("ADD CATEGORY");

        System.out.print("Enter category name: ");
        String name = scanner.nextLine();

        Category category = new Category();
        category.setName(name);
        int result = categoryService.saveCategory(category);
        if (result > 0) System.out.println("Category added successfully");
        else System.out.println("An error occurred");
    }

    private void handleUpdateCategory() {
        System.out.println("UPDATE CATEGORY");

        System.out.print("Enter name of category to update: ");
        String name = scanner.nextLine();
        Category category = categoryService.getCategoryByName(name);

        System.out.print("Enter new name: ");
        String newName = scanner.nextLine();
        category.setName(newName);

        int result = categoryService.updateCategory(name, category);
        if (result > 0) System.out.println("Category updated successfully");
        else System.out.println("An error occurred");
    }

    private void handleDeleteCategory() {
        System.out.println("DELETE CATEGORY");
        System.out.print("Enter name of category to delete: ");
        String name = scanner.nextLine();

        int result = categoryService.deleteCategory(name);
        if (result > 0) System.out.println("Category deleted successfully");
        else System.out.println("An error occurred");

    }

    private void handleShowAllItems() {
        System.out.println("ALL ITEMS");
        System.out.println("1. All items");
        System.out.println("2. Search");
        System.out.println("3. Filter");
        System.out.println("4. Back");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1 -> {
                if (items.isEmpty()) {
                    System.out.println("No items found");
                } else {
                    items.forEach(Item::display);
                }
            }
            case 2 -> displaySearchMenu();
            case 3 -> displayFilterMenu();
            case 4 -> {}
            default -> {
                System.out.println("Invalid choice\nPlease Try again");
                handleShowAllItems();
            }
        }

    }

    private void displaySearchMenu() {
        System.out.println("SEARCH MENU");
        System.out.println("1. SKU");
        System.out.println("2. Back");
        System.out.print("Enter criteria to use: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                System.out.print("Enter sku: ");
                String sku = scanner.nextLine();
                Item item = itemService.getItemBySku(sku);
                item.display();
            }
            case 2 -> {}
            default -> {
                System.out.println("Invalid choice\nPlease Try again");
                displaySearchMenu();
            }
        }
    }

    private void displayFilterMenu() {
        System.out.println("FILTER MENU");
        System.out.println("1. Category");
        System.out.println("2. Price range");
        System.out.print("Enter criteria to use: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                Category category = getCategory();
                List<Item> itemList = itemService.filterByCategory(category);
                if (itemList != null) itemList.forEach(Item::display);
                else System.out.println("Items not found");
            }
            case 2 -> {
                System.out.print("Enter minimum price: ");
                double minPrice = scanner.nextDouble();
                System.out.print("Enter maximum price: ");
                double maxPrice = scanner.nextDouble();
                List<Item> itemList = itemService.filterByPriceRange(minPrice, maxPrice);
                if (itemList != null) itemList.forEach(Item::display);
                else System.out.println("Items not found");
            }
            default -> {
                System.out.println("Invalid choice\nPlease Try again");
                displayFilterMenu();
            }
        }
    }

    private void handleAddItem() {
        System.out.println("ADD ITEM");

        System.out.println("Enter item details: ");
        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Brand: ");
        String brand = scanner.nextLine();

        System.out.print("Model: ");
        String model = scanner.nextLine();

        System.out.print("SKU: ");
        String sku = scanner.nextLine();

        System.out.print("Quantity: ");
        int quantity = scanner.nextInt();

        System.out.print("Price: ");
        double price = scanner.nextDouble();

        Category category = getCategory();

        Item item = new Item(name, brand, model, sku, quantity, price, category);
        int result = itemService.saveItem(item);
        if (result > 0) System.out.println("Item added successfully");
        else System.out.println("An error occurred");
    }

    private Category getCategory() {
        System.out.println("CATEGORIES");
        List<Category> categories = categoryService.getAllCategories();
        categories.forEach(System.out::println);
        System.out.print("Choose category: ");
        int choice = scanner.nextInt();
        return categoryService.getCategoryById(choice);
    }

    private void handleUpdateItem() {
        System.out.println("UPDATE ITEM");

        System.out.print("Enter sku of item to update: ");
        String sku = scanner.nextLine();
        Item item = itemService.getItemBySku(sku);

        updateItem(item);
        int result = itemService.updateItem(sku, item);
        if (result > 0) System.out.println("Item updated successfully");
        else System.out.println("An error occurred");
    }

    private void updateItem(Item item) {
        item.display();
        System.out.println("Choose an attribute to update:");
        System.out.println("1. Name");
        System.out.println("2. Brand");
        System.out.println("3. Model");
        System.out.println("4. Quantity");
        System.out.println("5. Price");
        System.out.println("6. Category");
        System.out.println("7. Done");
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
                System.out.print("Enter new brand: ");
                String newBrand = scanner.nextLine();
                item.setBrand(newBrand);
            }
            case 3 -> {
                System.out.print("Enter new model: ");
                String newModel = scanner.nextLine();
                item.setModel(newModel);
            }
            case 4 -> {
                System.out.print("Enter new quantity: ");
                int newQuantity = scanner.nextInt();
                item.setQuantity(newQuantity);
            }
            case 5 -> {
                System.out.print("Enter new price: ");
                double newPrice = scanner.nextDouble();
                item.setPrice(newPrice);
            }
            case 6 -> {
                Category newCategory = getCategory();
                item.setCategory(newCategory);
            }
            case 7 -> {
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
        String sku = scanner.nextLine();

        int result = itemService.deleteItem(sku);
        if (result > 0) System.out.println("Item deleted successfully");
        else System.out.println("An error occurred");
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
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found");
        } else {
            users.forEach(User::display);
        }
    }

    private void handleAddUser() {
        System.out.println("Enter user details to continue");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Role: ");
        String userType = scanner.nextLine();

        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(Role.valueOf(userType));

        int result = userService.saveUser(user);
        if (result > 0) System.out.println("User was added successfully");
        else System.out.println("An error occurred while saving the user");
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
                case 1 -> userActions.displayUserProfile(user);
                case 2 -> userActions.updateProfile(user);
                case 3 -> userActions.deleteAccount(user);
                case 4 -> {
                    break A;
                }
                default -> System.out.println("Invalid choice\nPlease try again!");
            }
        }
    }

}
