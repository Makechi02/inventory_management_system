package com.makechi.inventory_management_system.controller;

import com.makechi.inventory_management_system.entity.Role;
import com.makechi.inventory_management_system.entity.User;
import com.makechi.inventory_management_system.service.user.UserService;
import com.makechi.inventory_management_system.service.user.UserServiceImpl;
import com.makechi.inventory_management_system.utils.StyleUtil;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ManageUsersController {
    @FXML private TextField searchField;
    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, Integer> idColumn;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, Role> roleColumn;
    @FXML private TableColumn<User, Void> actionsColumn;

    private final UserService userService = new UserServiceImpl();
    private List<User> users = userService.getAllUsers();
    private final ObservableList<User> userObservableList = FXCollections.observableArrayList(users);

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> {
            int rowIndex = usersTable.getItems().indexOf(cellData.getValue()) + 1;
            return new ReadOnlyObjectWrapper<>(rowIndex);
        });
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        usersTable.setItems(userObservableList);
        usersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        addActionsToTable();
        usersTable.getItems().addListener((ListChangeListener<User>) _ -> usersTable.refresh());
    }

    private void addActionsToTable() {
        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = new Callback<>() {

            @Override
            public TableCell<User, Void> call(TableColumn<User, Void> userVoidTableColumn) {
                return new TableCell<>() {
                    private final Button deleteButton = new Button("Delete");
                    {
                        deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white");

                        deleteButton.setOnAction(_ -> {
                            User user = getTableView().getItems().get(getIndex());
                            handleDeleteUser(user);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(deleteButton);
                        }
                    }
                };
            }
        };

        actionsColumn.setCellFactory(cellFactory);
    }

    private void handleDeleteUser(User user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to delete this user?", ButtonType.YES, ButtonType.CANCEL);
        Optional<ButtonType> response = alert.showAndWait();
        if (response.isPresent() && response.get() == ButtonType.YES) {
            int result = userService.deleteUser(user.getUsername());
            if (result > 0) {
                showAlert(Alert.AlertType.INFORMATION, "User deleted successfully");
            } else {
                showAlert(Alert.AlertType.ERROR, "An error occurred");
            }
            usersTable.getItems().remove(user);
        }
    }

    @FXML
    private void handleAddUser() {
        User user = new User();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/makechi/inventory_management_system/fxml/add-edit-user.fxml"));
            Parent root = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(usersTable.getScene().getWindow());

            Scene scene = new Scene(root);
            StyleUtil.applyStyles(scene);
            dialogStage.setScene(scene);

            AddEditUserController addEditItemController = loader.getController();
            addEditItemController.setDialogStage(dialogStage);
            addEditItemController.setUser(user, false);

            dialogStage.showAndWait();

            if (addEditItemController.isUpdateDone()) {
                usersTable.getItems().add(user);
                usersTable.refresh();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleSearch() {
        String searchString = searchField.getText();
        users = userService.searchUserByUsername(searchString);
        userObservableList.setAll(users);
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle("User deletion");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
