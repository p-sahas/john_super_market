package com.example.johnssupermarket;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

public class MainController {

    @FXML private BorderPane mainContentPane;
    @FXML private Button viewInventoryButton, manageDealersButton,
            showLowStockButton, helpButton, saveButton, exitButton, exportTextButton;

    private final InventoryManager manager = InventoryManager.getInstance();

    @FXML
    public void initialize() {
        UIHelper.setupButtonIcons(
                new Button[]{viewInventoryButton, manageDealersButton,
                        showLowStockButton, exportTextButton, helpButton, saveButton, exitButton},
                new String[]{"inventory.png", "dealers.png", "warning.png",
                        "export.png", "help.png", "save.png", "exit.png"}
        );
        handleViewInventory(); // Default view on startup

        // It ensures the UI is fully rendered before the alert pops up.
        //Platform.runLater(this::handleShowLowStock);
    }

    @FXML private void handleViewInventory() { loadView("inventory-view.fxml"); }

    @FXML private void handleViewDealers() { loadView("dealer-view.fxml"); }

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent view = loader.load();
            mainContentPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
            UIHelper.showAlert(Alert.AlertType.ERROR, "Error", "Could not load view: " + fxmlFile);
        }
    }
    //show Low Stock When Application Open
    @FXML public void handleShowLowStock() {
        UIHelper.showLowStockAlert(manager.getLowStockItems());
    }
    //Export to Text Button
    @FXML private void handleExportToText() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Inventory to Text File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(mainContentPane.getScene().getWindow());
        //Store data in Text File asa table
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println("===== John's Super Market Inventory Report =====");
                writer.println();
                String format = "%-10s | %-25s | %-15s | %-15s | %-10s | %-10s%n";
                writer.printf(format, "Code", "Name", "Brand", "Category", "Price", "Quantity");
                writer.println("-".repeat(95));
                for (Item item : manager.getInventory()) {
                    writer.printf(format, item.getItemCode(), item.getItemName(), item.getBrand(),
                            item.getCategory(), String.format("LKR %.2f", item.getPrice()), item.getQuantity());
                }
                UIHelper.showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Inventory data has been exported to " + file.getName());
            } catch (IOException e) {
                e.printStackTrace();
                UIHelper.showAlert(Alert.AlertType.ERROR, "Error", "Could not write the text file.");
            }
        }
    }
    @FXML private void handleHelp() {
        UIHelper.showHelpDialog();
    }
    @FXML private void handleSave() {
        manager.saveAllData();
        UIHelper.showAlert(Alert.AlertType.INFORMATION,
                "Success", "Data saved successfully.");
    }
    //Handle Exit
    @FXML public void handleExit() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Exit");
        confirm.setHeaderText("Are you sure you want to exit?");
        confirm.setContentText("This will also save any changes you've made.");
        UIHelper.setAlertIcon(confirm);
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            manager.saveAllData();
            Platform.exit();
        }
    }
}
