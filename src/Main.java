import entity.Inventory;
import entity.User;
import entity.UserType;
import menu.AdminMenu;
import menu.MainActions;
import menu.RegularMenu;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String FILENAME = "inventory.dat";
    private static final Inventory inventory = Inventory.loadFromFile(FILENAME);
    private static final List<User> users = inventory.getUsers();
    private static final Scanner scanner = new Scanner(System.in);

    private static final MainActions mainActions = new MainActions() {
        @Override
        public void saveChanges() {
            Inventory.saveToFile(inventory, FILENAME);
        }

        @Override
        public void onExit() {
            System.out.println("Exiting...");
            Inventory.saveToFile(inventory, FILENAME);
            scanner.close();
        }

        @Override
        public void onLogout() {
            System.out.println("Logging out\n");
            displayAccountActions();
        }
    };

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

        AdminMenu adminMenu = new AdminMenu(inventory, mainActions, authenticatedUser);
        RegularMenu regularMenu = new RegularMenu(inventory, mainActions, authenticatedUser);

        if (authenticatedUser.getUserType() == UserType.ADMIN) {
            adminMenu.displayMenu();
        } else if (authenticatedUser.getUserType() == UserType.REGULAR) {
            regularMenu.displayMenu();
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
}