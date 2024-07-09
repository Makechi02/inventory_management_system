package com.makechi.inventory_management_system.controller;

import com.makechi.inventory_management_system.entity.Category;
import com.makechi.inventory_management_system.exception.DuplicateResourceException;
import com.makechi.inventory_management_system.exception.RequestValidationException;
import com.makechi.inventory_management_system.service.category.CategoryService;
import com.makechi.inventory_management_system.service.category.CategoryServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddEditCategoryController {
    private final CategoryService categoryService = new CategoryServiceImpl();
    @FXML private Label modalHeading;
    @FXML private Button actionButton;
    @FXML private TextField nameField;

    private Stage dialogStage;
    private Category category;
    private boolean updateDone = false;
    private boolean isEditMode;

    @FXML
    public void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCategory(Category category, boolean isEditMode) {
        this.isEditMode = isEditMode;
        this.category = category;

        if (isEditMode) {
            nameField.setText(category.getName());

            dialogStage.setTitle("Edit Category");
            modalHeading.setText("Update Category");
            actionButton.setText("Update");
        } else {
            nameField.clear();

            dialogStage.setTitle("Add Category");
            modalHeading.setText("Add Category");
            actionButton.setText("Add");
        }
    }

    @FXML
    private void handleUpdateButtonClicked() {
        try {
            if (!isInputValid()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter valid input");
                return;
            }

            category.setName(nameField.getText());
            if (isEditMode) {
                int result = categoryService.updateCategory(category.getId(), category.getName());
                if (result > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Category updated", "Category updated successfully");
                    updateDone = true;
                    dialogStage.close();
                } else showAlert(Alert.AlertType.ERROR, "Error", "An error occurred");
            } else {
                int result = categoryService.saveCategory(category);
                if (result > 0) {
                    Category newCategory = categoryService.getCategoryByName(category.getName());
                    category.setId(newCategory.getId());
                    showAlert(Alert.AlertType.INFORMATION, "Category saved", "Category saved successfully");
                    updateDone = true;
                    dialogStage.close();
                } else showAlert(Alert.AlertType.ERROR, "Error", "An error occurred");
            }
        } catch (RequestValidationException | DuplicateResourceException exception) {
            showAlert(Alert.AlertType.ERROR, "Request validation", exception.getMessage());
        }
    }

    public boolean isUpdateDone() {
        return updateDone;
    }

    @FXML
    private void handleCancelAction() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        Pattern pattern = Pattern.compile("^[a-zA-Z][a-zA-Z _-]{1,48}[a-zA-Z]$");
        Matcher matcher = pattern.matcher(nameField.getText());
        return matcher.matches();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
