package UI;

import entity.Role;
import entity.User;
import exception.DuplicateResourceException;
import exception.InputValidationException;
import exception.RequestValidationException;
import exception.ResourceNotFoundException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import service.user.UserService;
import service.user.UserServiceImpl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddEditUserController {
    @FXML private Label roleLabel;
    @FXML private Label modalHeading;
    @FXML private Button actionButton;
    @FXML private TextField nameField;
    @FXML private TextField usernameField;
    @FXML private ComboBox<Role> roleComboBox;

    private Stage dialogStage;
    private User user;
    private boolean updateDone = false;
    private boolean isEditMode;

    private final ObservableList<Role> roleObservableList = FXCollections.observableArrayList(Role.values());
    private final UserService userService = new UserServiceImpl();

    @FXML
    public void initialize() {
        roleComboBox.setItems(roleObservableList);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setUser(User user, boolean isEditMode) {
        this.isEditMode = isEditMode;
        this.user = user;

        if (isEditMode) {
            nameField.setText(user.getName());
            usernameField.setText(user.getUsername());
            roleLabel.setVisible(false);
            roleComboBox.setVisible(false);

            dialogStage.setTitle("Edit User");
            modalHeading.setText("Update User");
            actionButton.setText("Update");
        } else {
            nameField.clear();
            usernameField.clear();

            dialogStage.setTitle("Add User");
            modalHeading.setText("Add User");
            actionButton.setText("Add");
        }
    }

    public boolean isUpdateDone() {
        return updateDone;
    }

    @FXML
    private void handleUpdateButtonClicked() {
        try {
            checkValidInputs();

            user.setName(nameField.getText());
            user.setUsername(usernameField.getText());
            user.setRole(roleComboBox.getSelectionModel().getSelectedItem());

            if (isEditMode) {
                int result = userService.updateUser(user.getId(), user);
                if (result > 0) {
                    User updatedUser = userService.getUserByUsername(user.getUsername());
                    user.setId(updatedUser.getId());
                    user.setName(updatedUser.getName());
                    user.setUsername(updatedUser.getUsername());
                    user.setRole(updatedUser.getRole());

                    showAlert(Alert.AlertType.INFORMATION, "User updated", "User updated successfully");
                    updateDone = true;
                    dialogStage.close();
                } else showAlert(Alert.AlertType.ERROR, "Error", "An error occurred");
            } else {
                int result = userService.saveUser(user);
                if (result > 0) {
                    User newUser = userService.getUserByUsername(user.getUsername());
                    user.setId(newUser.getId());
                    showAlert(Alert.AlertType.INFORMATION, "User saved", "User saved successfully");
                    updateDone = true;
                    dialogStage.close();
                } else showAlert(Alert.AlertType.ERROR, "Error", "An error occurred");
            }
        } catch (DuplicateResourceException | ResourceNotFoundException | RequestValidationException exception) {
            showAlert(Alert.AlertType.ERROR, "Request validation", exception.getMessage());
        } catch (InputValidationException exception) {
            showAlert(Alert.AlertType.ERROR, "Invalid inputs", exception.getMessage());
        }
    }

    @FXML
    private void handleCancelAction() {
        dialogStage.close();
    }

    private void checkValidInputs() {
        Pattern namePattern = Pattern.compile("^[a-zA-Z][a-zA-Z ]{1,48}[a-zA-Z]$");
        Matcher nameMatcher = namePattern.matcher(nameField.getText());
        if (!nameMatcher.matches()) throw new InputValidationException("Name is not valid");

        Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9_-]{1,48}[a-zA-Z0-9]$");
        Matcher usernameMatcher = usernamePattern.matcher(usernameField.getText());
        if (!usernameMatcher.matches()) throw new InputValidationException("Username is not valid");

        if (!isEditMode) {
            if (roleComboBox.getSelectionModel().getSelectedItem() == null)
                throw new InputValidationException("Role is not selected");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
