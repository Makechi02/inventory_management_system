package UI;

import UI.utils.StyleUtil;
import entity.Category;
import entity.Item;
import javafx.collections.FXCollections;
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
import service.item.ItemService;
import service.item.ItemServiceImpl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ManageItemsController {
    @FXML private TextField searchField;
    @FXML private TableView<Item> itemsTable;
    @FXML private TableColumn<Item, Integer> idColumn;
    @FXML private TableColumn<Item, String> nameColumn;
    @FXML private TableColumn<Item, String> brandColumn;
    @FXML private TableColumn<Item, String> modelColumn;
    @FXML private TableColumn<Item, String> skuColumn;
    @FXML private TableColumn<Item, Integer> quantityColumn;
    @FXML private TableColumn<Item, String> priceColumn;
    @FXML private TableColumn<Item, Category> categoryColumn;
    @FXML private TableColumn<Item, Void> actionsColumn;

    private final ItemService itemService = new ItemServiceImpl();
    private List<Item> items = itemService.getAllItems();
    private final ObservableList<Item> itemObservableList = FXCollections.observableArrayList(items);

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        skuColumn.setCellValueFactory(new PropertyValueFactory<>("sku"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

//        priceColumn.setCellFactory(new Callback<>() {
//            @Override
//            public TableCell<Item, String> call(TableColumn<Item, String> itemStringTableColumn) {
//                return new TableCell<>() {
//                    @Override
//                    public void updateItem(String item, boolean empty) {
//                        super.updateItem(item, empty);
//                        if (empty || getTableRow() == null) {
//                            setText(null);
//                        } else {
//                            Item currentItem = getTableRow().getItem();
//                            if (currentItem != null) {
//                                setText(currentItem.getFormattedPrice());
//                            }
//                        }
//                    }
//                };
//            }
//        });

        itemsTable.setItems(itemObservableList);
        itemsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        addActionsToTable();
    }

    private void addActionsToTable() {
        Callback<TableColumn<Item, Void>, TableCell<Item, Void>> cellFactory = new Callback<>() {

            @Override
            public TableCell<Item, Void> call(TableColumn<Item, Void> itemVoidTableColumn) {
                return new TableCell<>() {
                    private final Button editButton = new Button("Edit");
                    private final Button deleteButton = new Button("Delete");
                    {
                        editButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white");
                        deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white");

                        editButton.setOnAction(_ -> {
                            Item item = getTableView().getItems().get(getIndex());
                            handleEditItem(item);
                        });

                        deleteButton.setOnAction(_ -> {
                            Item item = getTableView().getItems().get(getIndex());
                            handleDeleteItem(item);
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

    private void handleEditItem(Item item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/views/add-edit-item.fxml"));
            Parent root = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(itemsTable.getScene().getWindow());

            Scene scene = new Scene(root);
            StyleUtil.applyStyles(scene);
            dialogStage.setScene(scene);

            AddEditItemController addEditItemController = loader.getController();
            addEditItemController.setDialogStage(dialogStage);
            addEditItemController.setItem(item, true);

            dialogStage.showAndWait();

            if (addEditItemController.isSaveClicked()) {
                itemService.updateItem(item.getSku(), item);
                itemsTable.refresh();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleDeleteItem(Item item) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to delete this item?", ButtonType.YES, ButtonType.CANCEL);
        Optional<ButtonType> response = alert.showAndWait();
        if (response.isPresent() && response.get() == ButtonType.YES) {
            int result = itemService.deleteItem(item.getSku());
            if (result > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Item deletion", "Item deleted successfully");
            } else {
                showAlert(Alert.AlertType.ERROR, "Item deletion", "An error occurred");
            }
            itemsTable.getItems().remove(item);
        }
    }

    @FXML
    private void handleAddItem() {
        Item item = new Item();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/views/add-edit-item.fxml"));
            Parent root = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(itemsTable.getScene().getWindow());

            Scene scene = new Scene(root);
            StyleUtil.applyStyles(scene);
            dialogStage.setScene(scene);

            AddEditItemController addEditItemController = loader.getController();
            addEditItemController.setDialogStage(dialogStage);
            addEditItemController.setItem(item, false);

            dialogStage.showAndWait();

            if (addEditItemController.isSaveClicked()) {
                itemService.saveItem(item);
                itemsTable.refresh();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleSearch() {
        String searchString = searchField.getText();
        items = itemService.searchItemsByName(searchString);
        itemObservableList.setAll(items);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
