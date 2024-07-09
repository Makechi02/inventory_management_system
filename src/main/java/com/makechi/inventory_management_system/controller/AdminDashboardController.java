package com.makechi.inventory_management_system.controller;

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
        loadView("/com/makechi/inventory_management_system/fxml/manage-items.fxml");
    }

    @FXML
    private void handleManageCategories() {
        loadView("/com/makechi/inventory_management_system/fxml/manage-categories.fxml");
    }

    @FXML
    private void handleManageUsers() {
        loadView("/com/makechi/inventory_management_system/fxml/manage-users.fxml");
    }

    @FXML
    private void handleProfile() {
        loadView("/com/makechi/inventory_management_system/fxml/manage-profile.fxml");
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
