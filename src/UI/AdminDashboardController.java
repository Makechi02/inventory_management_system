package UI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;

import java.io.IOException;
import java.util.Objects;

public class AdminDashboardController {
    @FXML private ScrollPane mainContent;

    @FXML
    public void initialize() {
        handleManageItems();
    }

    @FXML
    private void handleManageItems() {
        loadView("/UI/views/manage-items.fxml");
    }

    @FXML
    private void handleManageCategories() {
        loadView("/UI/views/manage-items.fxml");
    }

    @FXML
    private void handleManageUsers() {
        loadView("/UI/views/manage-users.fxml");
    }

    @FXML
    private void handleProfile() {
        loadView("/UI/views/manage-items.fxml");
    }

    private void loadView(String path) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
            mainContent.setContent(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
