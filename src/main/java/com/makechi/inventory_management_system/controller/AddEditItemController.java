package com.makechi.inventory_management_system.controller;

import com.makechi.inventory_management_system.entity.Category;
import com.makechi.inventory_management_system.entity.Item;
import com.makechi.inventory_management_system.exception.DuplicateResourceException;
import com.makechi.inventory_management_system.exception.InputValidationException;
import com.makechi.inventory_management_system.exception.RequestValidationException;
import com.makechi.inventory_management_system.exception.ResourceNotFoundException;
import com.makechi.inventory_management_system.service.category.CategoryService;
import com.makechi.inventory_management_system.service.category.CategoryServiceImpl;
import com.makechi.inventory_management_system.service.item.ItemService;
import com.makechi.inventory_management_system.service.item.ItemServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddEditItemController {
    @FXML private Label modalHeading;
    @FXML private Button actionButton;
    @FXML private TextField nameField;
    @FXML private TextField brandField;
    @FXML private TextField modelField;
    @FXML private TextField quantityField;
    @FXML private TextField priceField;
    @FXML private ComboBox<Category> categoryComboBox;

    private final CategoryService categoryService = new CategoryServiceImpl();

    private Stage dialogStage;
    private Item item;
    private boolean updateDone = false;
    private boolean isEditMode;

    private final List<Category> categories = categoryService.getAllCategories();
    private final ObservableList<Category> categoryObservableList = FXCollections.observableArrayList(categories);
    private final ItemService itemService = new ItemServiceImpl();

    @FXML
    public void initialize() {
        categoryComboBox.setItems(categoryObservableList);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setItem(Item item, boolean isEditMode) {
        this.isEditMode = isEditMode;
        this.item = item;

        if (isEditMode) {
            nameField.setText(item.getName());
            brandField.setText(item.getBrand());
            modelField.setText(item.getModel());
            quantityField.setText(String.valueOf(item.getQuantity()));
            priceField.setText(String.valueOf(item.getPrice()));
            categoryComboBox.getSelectionModel().select(item.getCategory());

            dialogStage.setTitle("Edit Item");
            modalHeading.setText("Update Item");
            actionButton.setText("Update");
        } else {
            nameField.clear();
            brandField.clear();
            modelField.clear();
            quantityField.clear();
            priceField.clear();

            dialogStage.setTitle("Add Item");
            modalHeading.setText("Add Item");
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

            item.setName(nameField.getText());
            item.setBrand(brandField.getText());
            item.setModel(modelField.getText());
            item.setQuantity(Integer.parseInt(quantityField.getText()));
            item.setPrice(Double.parseDouble(priceField.getText()));
            item.setCategory(categoryComboBox.getSelectionModel().getSelectedItem());

            if (isEditMode) {
                int result = itemService.updateItem(item.getSku(), item);
                if (result > 0) {
                    Item updatedItem = itemService.getItemBySku(item.getSku());
                    item.setId(updatedItem.getId());

                    showAlert(Alert.AlertType.INFORMATION, "Item updated", "Item updated successfully");
                    updateDone = true;
                    dialogStage.close();
                } else showAlert(Alert.AlertType.ERROR, "Error", "An error occurred");
            } else {
                int result = itemService.saveItem(item);
                if (result > 0) {
                    Item newItem = itemService.getItemBySku(item.getSku());
                    item.setId(newItem.getId());
                    showAlert(Alert.AlertType.INFORMATION, "Item saved", "Item saved successfully");
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
        if (nameField.getText().isBlank()) throw new InputValidationException("Item name is blank");
        if (brandField.getText().isBlank()) throw new InputValidationException("Item brand is blank");
        if (modelField.getText().isBlank()) throw new InputValidationException("Item model is blank");
        if (categoryComboBox.getSelectionModel().getSelectedItem() == null)
            throw new InputValidationException("Category not selected");

        Pattern quantityPattern = Pattern.compile("[0-9]{1,9}");
        Matcher quantityMatcher = quantityPattern.matcher(quantityField.getText());
        if (!quantityMatcher.matches()) throw new InputValidationException("Quantity is invalid");

        Pattern pricePattern = Pattern.compile("^[0-9][0-9.]{1,9}$");
        Matcher priceMatcher = pricePattern.matcher(priceField.getText());
        if (!priceMatcher.matches()) throw new InputValidationException("Price is invalid");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
