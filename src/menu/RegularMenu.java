package menu;

import entity.Inventory;
import entity.Item;
import entity.User;

import java.util.List;
import java.util.Scanner;

public class RegularMenu {
    private final List<Item> items;
    private final Scanner scanner = new Scanner(System.in);
    private final MainActions mainActions;
    private final UserActions userActions;

    private final User user;

    public RegularMenu(Inventory inventory, MainActions mainActions, UserActions userActions, User user) {
        items = inventory.getItems();
        this.mainActions = mainActions;
        this.userActions = userActions;
        this.user = user;
    }

    public void displayMenu() {
        A:
        while (true) {
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
        A:
        while (true) {
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
        A:
        while (true) {
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
