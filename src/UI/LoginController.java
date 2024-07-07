package UI;

import UI.utils.StyleUtil;
import entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import service.user.UserService;
import service.user.UserServiceImpl;

import java.io.IOException;
import java.util.List;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private final UserService userService = new UserServiceImpl();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isBlank()) {
            showAlert(AlertType.WARNING, "Wrong input format", "Username is blank");
            return;
        }

        if (password.isBlank()) {
            showAlert(AlertType.WARNING, "Wrong input format", "Password is blank");
            return;
        }

        User user = authenticateUser(username, password);
        if (user == null) {
            showAlert(AlertType.ERROR, "Login Failed", "Invalid username or password");
            return;
        }

        switch (user.getRole()) {
            case ADMIN -> showAdminDashboard();
            case USER -> showUserDashboard();
        }
    }

    private User authenticateUser(String username, String password) {
        User foundUser = null;
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                foundUser = user;
            }
        }

        return foundUser;
    }

    private void showAdminDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/views/admin-dashboard-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setTitle("Admin dashboard");
            Scene scene = new Scene(root);
            StyleUtil.applyStyles(scene);
            stage.setScene(scene);
            stage.setResizable(true);

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX(screenBounds.getMinX());
            stage.setY(screenBounds.getMinY());
            stage.setWidth(screenBounds.getWidth());
            stage.setHeight(screenBounds.getHeight());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showUserDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/views/user-dashboard-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setTitle("User dashboard");
            Scene scene = new Scene(root);
            StyleUtil.applyStyles(scene);
            stage.setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/views/registration-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene scene = new Scene(root);
            StyleUtil.applyStyles(scene);
            stage.setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
