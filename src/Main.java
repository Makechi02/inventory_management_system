import entity.Role;
import entity.User;
import menu.AdminMenu;
import menu.MainActions;
import menu.RegularMenu;
import menu.UserActions;
import service.user.UserService;
import service.user.UserServiceImpl;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final UserService userService = new UserServiceImpl();

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        displayAccountActions();
    }

    private static final MainActions mainActions = new MainActions() {
        @Override
        public void onExit() {
            System.out.println("Exiting...");
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
    }

    private static final UserActions userActions = new UserActions() {
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
            System.out.println("2. Username");
            System.out.println("3. Password");
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
                    System.out.print("Enter new username: ");
                    String username = scanner.nextLine();
                    user.setUsername(username);
                }
                case 3 -> {
                    System.out.print("Enter new password: ");
                    String password = scanner.nextLine();
                    user.setPassword(password);
                }
            }

            int result = userService.updateUser(user.getUsername(), user);
            if (result > 0) System.out.println("Profile updated successfully");
            else System.out.println("An error occurred");
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
                        int result = userService.deleteUser(user.getUsername());
                        if (result > 0) System.out.println("User deleted successfully");
                        else System.out.println("An error occurred");

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

        AdminMenu adminMenu = new AdminMenu(mainActions, userActions, authenticatedUser);
        RegularMenu regularMenu = new RegularMenu(mainActions, userActions, authenticatedUser);

        if (authenticatedUser.getRole() == Role.ADMIN) {
            adminMenu.displayMenu();
        } else if (authenticatedUser.getRole() == Role.USER) {
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
        user.setRole(Role.USER);

        int result = userService.saveUser(user);
        if (result > 0) System.out.println("User was added successfully");
        else System.out.println("An error occurred while saving the user");

        displayLoginMenu();
    }

    private static User authenticateUser(String username, String password) {
        User foundUser = null;
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                foundUser = user;
            }
        }

        return foundUser;
    }

}