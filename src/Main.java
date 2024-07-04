import entity.Inventory;
import entity.User;
import entity.UserType;
import menu.AdminMenu;
import menu.MainActions;
import menu.RegularMenu;
import menu.UserActions;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String FILENAME = "inventory.dat";
    private static final Inventory inventory = Inventory.loadFromFile(FILENAME);
    private static final List<User> users = inventory.getUsers();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        displayAccountActions();
    }    private static final MainActions mainActions = new MainActions() {
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
    }    private static final UserActions userActions = new UserActions() {
        @Override
        public void displayUserProfile(User user) {
            user.display();
        }

        @Override
        public void updateProfile(User user) {
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

        @Override
        public void deleteAccount(User user) {
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
                        deleteAccount(user);
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
    };

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

        AdminMenu adminMenu = new AdminMenu(inventory, mainActions, userActions, authenticatedUser);
        RegularMenu regularMenu = new RegularMenu(inventory, mainActions, userActions, authenticatedUser);

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

        for (User user : users) {
            if (user.getName().equals(username) && user.getPassword().equals(password)) {
                foundUser = user;
            }
        }

        return foundUser;
    }




}