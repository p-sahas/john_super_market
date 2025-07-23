package com.example.johnssupermarket;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Optional;
//Connected to inventory-view.fxml
public class InventoryController {
    @FXML private TableView<Item> inventoryTable;
    @FXML private TableColumn<Item, String> colImage, colItemCode, colItemName, colBrand, colCategory, colDealer;
    @FXML private TableColumn<Item, Double> colPrice;
    @FXML private TableColumn<Item, Integer> colQuantity, colLowStock;
    @FXML private TableColumn<Item, LocalDate> colPurchasedDate;
    @FXML private Label totalItemsLabel, totalValueLabel;
    @FXML private Button addItemButton, updateItemButton, deleteItemButton;

    private final InventoryManager manager = InventoryManager.getInstance();
    private final ObservableList<Item> inventoryData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        UIHelper.setupButtonIcons(
                new Button[]{addItemButton, updateItemButton, deleteItemButton},
                new String[]{"add.png", "edit.png", "delete.png"}
        );
        setupInventoryTable();
        loadAndRefreshInventory();
    }

    private void setupInventoryTable() {
        colImage.setCellValueFactory(new PropertyValueFactory<>("imagePath"));

        colImage.setCellFactory(param -> new ImageTableCell<>());

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));

        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));

        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));

        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));

        colPurchasedDate.setCellValueFactory(new PropertyValueFactory<>("purchasedDate"));

        colLowStock.setCellValueFactory(new PropertyValueFactory<>("lowStockThreshold"));
        //Dealer id for inventory table. If no dealer found, It shows as "N/A"
        colDealer.setCellValueFactory(cellData -> {
            String dealerId = cellData.getValue().getDealerId();
            String dealerName = (dealerId == null || dealerId.equals("null")) ? "N/A" :
                    manager.getDealers().stream()
                            .filter(d -> d.getDealerId().equals(dealerId))
                            .map(Dealer::getName)
                            .findFirst()
                            .orElse("N/A");
            return new SimpleStringProperty(dealerName);
        });
        inventoryTable.setItems(inventoryData);
        inventoryTable.setRowFactory(tv -> UIHelper.createRowWithDoubleClick(this::showItemDetailsPopup));

        // Set preferred widths for better readability
        colImage.setPrefWidth(60);
        colItemCode.setPrefWidth(80);
        colItemName.setPrefWidth(150);
        colBrand.setPrefWidth(100);
        colPrice.setPrefWidth(80);
        colQuantity.setPrefWidth(80);
        colCategory.setPrefWidth(100);
        colPurchasedDate.setPrefWidth(120);
        colLowStock.setPrefWidth(120);
        colDealer.setPrefWidth(150);
    }

    private void loadAndRefreshInventory() {
        manager.sortInventory();
        inventoryData.setAll(manager.getInventory());
        updateTotals();
    }

    private void updateTotals() {
        int count = inventoryData.stream().mapToInt(Item::getQuantity).sum();
        double value = inventoryData.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
        totalItemsLabel.setText("Total Item Count: " + count);
        totalValueLabel.setText(String.format("Total Inventory Value: LKR %.2f", value));
    }

    @FXML private void handleAddItem() {
        showItemDialog(null);
    }
    @FXML private void handleUpdateItem() {
        Item s = inventoryTable.getSelectionModel().getSelectedItem();
        if (s == null) {
            UIHelper.showAlert(Alert.AlertType.ERROR, "No Selection", "Please select an item to update.");
        } else showItemDialog(s);
    }
    @FXML private void handleDeleteItem() {
        Item s = inventoryTable.getSelectionModel().getSelectedItem();
        if (s == null) {
            UIHelper.showAlert(Alert.AlertType.ERROR, "No Selection", "Please select an item to delete.");
            return;
        } Alert c = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete " + s.getItemName() + "?", ButtonType.OK, ButtonType.CANCEL);
        UIHelper.setAlertIcon(c); c.showAndWait().ifPresent(r -> { if (r == ButtonType.OK) {
            manager.deleteItem(s.getItemCode());
            loadAndRefreshInventory();
        }
        });
    }

    private void showItemDialog(Item item) {
        Dialog<Item> dialog = new Dialog<>();
        UIHelper.setAlertIcon(dialog);
        dialog.setTitle(item == null ? "Add New Item" : "Update Item");
        dialog.setHeaderText("Enter details for " + (item == null ? "a new item" : item.getItemName()));
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane(); grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));
        TextField code = new TextField(),
                name = new TextField(),
                brand = new TextField(),
                price = new TextField(),
                qty = new TextField(),
                cat = new TextField(),
                low = new TextField();
        DatePicker date = new DatePicker(LocalDate.now());
        TextField imagePathField = new TextField();
        imagePathField.setPromptText("Click Browse to select an image...");
        imagePathField.setEditable(false); imagePathField.setPrefWidth(250);

        // ComboBox for Dealer selection
        ComboBox<Dealer> dealerComboBox = new ComboBox<>();
        // Add a "None" option to allow null dealer
        ObservableList<Dealer> dealersWithNone = FXCollections.observableArrayList();
        dealersWithNone.add(null); // Represents "None" or no dealer
        dealersWithNone.addAll(manager.getDealers());
        dealerComboBox.setItems(dealersWithNone);
        dealerComboBox.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Dealer dealer) { return dealer == null ? "None" : dealer.getName();
            }
            @Override public Dealer fromString(String string) { return null; } // Not used for selection
        });
        dealerComboBox.getSelectionModel().select(0); // Select "None" by default

        Button browseBtn = new Button("Browse...");
        browseBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser(); fileChooser.setTitle("Select Item Image");
            fileChooser.getExtensionFilters().add(new FileChooser
                    .ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File selectedFile = fileChooser.showOpenDialog(dialog.getDialogPane().getScene().getWindow());
            if (selectedFile != null) {
                try {
                    Path sourcePath = selectedFile.toPath();
                    Path destinationDir = Paths.get(/*InventoryManager.RESOURCES_DIR,*/InventoryManager.IMAGE_DIR);
                    Files.createDirectories(destinationDir);
                    Path destinationPath = destinationDir.resolve(sourcePath.getFileName());
                    Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    imagePathField.setText(destinationPath.getFileName().toString());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    UIHelper.showAlert(Alert.AlertType.ERROR, "Image Error",
                            "Could not copy the image file.");
                }
            }
        });
        if (item != null) {
            code.setText(item.getItemCode());
            code.setEditable(false);
            name.setText(item.getItemName());
            brand.setText(item.getBrand());
            price.setText(""+item.getPrice());
            qty.setText(""+item.getQuantity());
            cat.setText(item.getCategory());
            date.setValue(item.getPurchasedDate());
            low.setText(""+item.getLowStockThreshold());
            imagePathField.setText(item.getImagePath());
            // Select the correct dealer in the ComboBox, or "None" if dealerId is null
            if (item.getDealerId() == null || item.getDealerId().equals("null")) {
                dealerComboBox.getSelectionModel().select(0); // Select "None"
            } else {
                manager.getDealers().stream().filter(d ->
                        d.getDealerId().equals(item.getDealerId())).findFirst().ifPresent(dealerComboBox::setValue);
            }
        }

        grid.add(new Label("Item Code:"), 0, 0);
        grid.add(code, 1, 0, 2, 1);
        grid.add(new Label("Item Name:"), 0, 1);
        grid.add(name, 1, 1, 2, 1);
        grid.add(new Label("Brand:"), 0, 2);
        grid.add(brand, 1, 2, 2, 1);
        grid.add(new Label("Dealer:"), 0, 3);
        grid.add(dealerComboBox, 1, 3, 2, 1);
        grid.add(new Label("Price:"), 0, 4);
        grid.add(price, 1, 4, 2, 1);
        grid.add(new Label("Quantity:"), 0, 5);
        grid.add(qty, 1, 5, 2, 1);
        grid.add(new Label("Category:"), 0, 6);
        grid.add(cat, 1, 6, 2, 1);
        grid.add(new Label("Purchased Date:"), 0, 7);
        grid.add(date, 1, 7, 2, 1);
        grid.add(new Label("Low Stock Threshold:"), 0, 8);
        grid.add(low, 1, 8, 2, 1);
        grid.add(new Label("Image:"), 0, 9);
        grid.add(imagePathField, 1, 9); grid.add(browseBtn, 2, 9);
        dialog.getDialogPane().setContent(grid);

        // Get the Save button and add an event filter for validation
        final Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            // Perform validation before allowing the dialog to close
            if (code.getText().isBlank() || name.getText().isBlank() || cat.getText().isBlank()) {
                UIHelper.showAlert(Alert.AlertType.ERROR,
                        "Validation Error", "Item Code, Name, and Category cannot be empty.");
                event.consume(); // Consume the event to prevent the dialog from closing when validation error
            } else {
                try {
                    Double.parseDouble(price.getText());
                    Integer.parseInt(qty.getText());
                    Integer.parseInt(low.getText());
                    // Dealer selection is now optional (can be null)
                } catch (NumberFormatException e) {
                    UIHelper.showAlert(Alert.AlertType.ERROR,
                            "Validation Error", "Price, Quantity," +
                                    " and Low Stock Threshold must be valid numbers.");
                    event.consume(); // Consume the event to prevent the dialog from closing when validation error
                }
            }
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                // If this point is reached, validation in the event filter has passed.
                // Now, safely create and return the Item object.
                Dealer selectedDealer = dealerComboBox.getValue();
                String dealerId = (selectedDealer == null) ? null : selectedDealer.getDealerId();
                // Get dealerId or null
                return new Item(code.getText(), name.getText(),
                        brand.getText(), Double.parseDouble(price.getText()),
                        Integer.parseInt(qty.getText()), cat.getText(), date.getValue(),
                        imagePathField.getText(), Integer.parseInt(low.getText()), dealerId);
            }
            return null;
        });

        Optional<Item> result = dialog.showAndWait();
        result.ifPresent(newItem -> {
            if (item == null) manager.addItem(newItem);
            else manager.updateItem(newItem);
            loadAndRefreshInventory();
        });
    }

    private void showItemDetailsPopup(Item item) { UIHelper.showItemDetailsPopup(item);
    }
}
