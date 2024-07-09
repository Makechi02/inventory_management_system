package com.makechi.inventory_management_system.controller;

import com.makechi.inventory_management_system.entity.Category;
import com.makechi.inventory_management_system.service.category.CategoryService;
import com.makechi.inventory_management_system.service.category.CategoryServiceImpl;
import com.makechi.inventory_management_system.utils.StyleUtil;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ManageCategoriesController {
    @FXML private TextField searchField;
    @FXML private TableView<Category> categoriesTable;
    @FXML private TableColumn<Category, Integer> serialNoColumn;
    @FXML private TableColumn<Category, String> nameColumn;
    @FXML private TableColumn<Category, Void> actionsColumn;

    private final CategoryService categoryService = new CategoryServiceImpl();
    private final List<Category> categories = categoryService.getAllCategories();
    private final ObservableList<Category> categoryObservableList = FXCollections.observableArrayList(categories);

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        serialNoColumn.setCellValueFactory(cellData -> {
            int rowIndex = categoriesTable.getItems().indexOf(cellData.getValue()) + 1;
            return new ReadOnlyObjectWrapper<>(rowIndex);
        });

        categoriesTable.setItems(categoryObservableList);
        categoriesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        addActionsToTable();

        categoriesTable.getItems().addListener((ListChangeListener<Category>) _ -> categoriesTable.refresh());
    }

    private void addActionsToTable() {
        Callback<TableColumn<Category, Void>, TableCell<Category, Void>> cellFactory = new Callback<>() {

            @Override
            public TableCell<Category, Void> call(TableColumn<Category, Void> itemVoidTableColumn) {
                return new TableCell<>() {
                    private final Button editButton = new Button("Edit");
                    private final Button deleteButton = new Button("Delete");
                    {
                        editButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white");
                        deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white");

                        editButton.setOnAction(_ -> {
                            Category category = getTableView().getItems().get(getIndex());
                            handleEditCategory(category);
                        });

                        deleteButton.setOnAction(_ -> {
                            Category category = getTableView().getItems().get(getIndex());
                            handleDeleteCategory(category);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox buttons = new HBox(editButton, deleteButton);
                            buttons.setSpacing(10);
                            buttons.setAlignment(Pos.CENTER);
                            setGraphic(buttons);
                        }
                    }
                };
            }
        };

        actionsColumn.setCellFactory(cellFactory);
    }

    private void handleEditCategory(Category category) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/makechi/inventory_management_system/fxml/add-edit-category.fxml"));
            Parent root = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(categoriesTable.getScene().getWindow());

            Scene scene = new Scene(root);
            StyleUtil.applyStyles(scene);
            dialogStage.setScene(scene);

            AddEditCategoryController addEditCategoryController = loader.getController();
            addEditCategoryController.setDialogStage(dialogStage);
            addEditCategoryController.setCategory(category, true);

            dialogStage.showAndWait();
            if (addEditCategoryController.isUpdateDone()) {
                categoriesTable.refresh();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleDeleteCategory(Category category) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to delete this category?", ButtonType.YES, ButtonType.CANCEL);
        Optional<ButtonType> response = alert.showAndWait();
        if (response.isPresent() && response.get() == ButtonType.YES) {
            int result = categoryService.deleteCategory(category.getName());
            if (result > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Category deletion", "Category deleted successfully");
            } else {
                showAlert(Alert.AlertType.ERROR, "Category deletion", "An error occurred");
            }
            categoriesTable.getItems().remove(category);
        }
    }

    @FXML
    private void handleAddItem() {
        Category category = new Category();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/makechi/inventory_management_system/fxml/add-edit-category.fxml"));
            Parent root = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(categoriesTable.getScene().getWindow());

            Scene scene = new Scene(root);
            StyleUtil.applyStyles(scene);
            dialogStage.setScene(scene);

            AddEditCategoryController addEditCategoryController = loader.getController();
            addEditCategoryController.setDialogStage(dialogStage);
            addEditCategoryController.setCategory(category, false);

            dialogStage.showAndWait();

            if (addEditCategoryController.isUpdateDone()) {
                categoriesTable.getItems().add(category);
                categoriesTable.refresh();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleSearch() {
        String searchString = searchField.getText();
        System.out.println(searchString);
//        categories = categoryService.searchItemsByName(searchString);
//        categoryObservableList.setAll(categories);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
