package UI;

import UI.utils.StyleUtil;
import entity.Role;
import entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.user.UserService;
import service.user.UserServiceImpl;

import java.io.IOException;

public class RegistrationController {
    @FXML private TextField nameField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    private final UserService userService = new UserServiceImpl();

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/views/login-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene scene = new Scene(root);
            StyleUtil.applyStyles(scene);
            stage.setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleRegister() {
        String name = nameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (name.isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Wrong input format", "Name is blank");
            return;
        }

        if (username.isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Wrong input format", "Username is blank");
            return;
        }

        if (password.isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Wrong input format", "Password is blank");
            return;
        }

        if (confirmPassword.isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Wrong input format", "Password is blank");
            return;
        }

        if (!confirmPassword.equals(password)) {
            showAlert(Alert.AlertType.ERROR, "Validation", "Passwords don't match");
            return;
        }

        User user = new User(name, username, password, Role.USER);
        int result = userService.saveUser(user);
        if (result > 0) {
            showAlert(Alert.AlertType.INFORMATION, "Login successful", "Welcome " + username + "!\nPlease login to continue.");
            handleBack();
        } else {
            showAlert(Alert.AlertType.ERROR, "Login failed", "An error occurred");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
