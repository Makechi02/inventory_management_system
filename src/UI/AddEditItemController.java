package UI;

import entity.Category;
import entity.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.category.CategoryService;
import service.category.CategoryServiceImpl;

import java.util.List;

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
    private boolean saveClicked = false;
    private boolean isEditMode;

    private final List<Category> categories = categoryService.getAllCategories();
    private final ObservableList<Category> categoryObservableList = FXCollections.observableArrayList(categories);

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

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void handleUpdateButtonClicked() {
        if (isInputValid()) {
            item.setName(nameField.getText());
            item.setBrand(brandField.getText());
            item.setModel(modelField.getText());
            item.setQuantity(Integer.parseInt(quantityField.getText()));
            item.setPrice(Double.parseDouble(priceField.getText()));
            item.setCategory(categoryComboBox.getSelectionModel().getSelectedItem());
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
