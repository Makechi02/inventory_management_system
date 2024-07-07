package UI;

import entity.Role;
import entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddEditUserController {
    @FXML private Label modalHeading;
    @FXML private Button actionButton;
    @FXML private TextField nameField;
    @FXML private TextField usernameField;
    @FXML private ComboBox<Role> roleComboBox;

    private Stage dialogStage;
    private User user;
    private boolean saveClicked = false;
    private boolean isEditMode;

    private final ObservableList<Role> roleObservableList = FXCollections.observableArrayList(Role.values());

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
            roleComboBox.getSelectionModel().select(user.getRole());

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

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void handleUpdateButtonClicked() {
        if (isInputValid()) {
            user.setName(nameField.getText());
            user.setUsername(usernameField.getText());
            user.setRole(roleComboBox.getSelectionModel().getSelectedItem());
            saveClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancelAction() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        return true;
    }
}
