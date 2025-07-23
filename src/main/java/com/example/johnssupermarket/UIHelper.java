package com.example.johnssupermarket;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UIHelper {


     // Sets icons; for an array of buttons.
    public static void setupButtonIcons(Button[] buttons, String[] iconNames) {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] != null) {
                buttons[i].setGraphic(createIcon(iconNames[i]));
            }
        }
    }


     // Creates an ImageView for a button icon. Returns null if the icon is not found.

    private static ImageView createIcon(String iconName) {
        try {
            // Ensure icons are in src/main/resources/icons/
            Image image = new Image(Objects.requireNonNull(UIHelper.class.getResource(
                    "/icons/" + iconName)).toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            imageView.setPreserveRatio(true);
            return imageView;
        } catch (NullPointerException e) {
            System.err.println("Error: Icon '" + iconName +
                    "' not found. Please ensure it's in src/main/resources/icons/.");
            return null;
        } catch (Exception e) {
            System.err.println("Warning: Could not load icon: " + iconName + " - " + e.getMessage());
            return null;
        }
    }


     // A generic; method to show alerts.

    public static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        setAlertIcon(alert); // Apply custom icon to alert
        alert.showAndWait();
    }


     //Shows the detailed pop-up for a selected item.

    public static void showItemDetailsPopup(Item item) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.getDialogPane().getStylesheets().add(UIHelper.class.getResource("styles.css").toExternalForm());
        setAlertIcon(dialog);
        dialog.setTitle("Item Details");
        dialog.setHeaderText(item.getItemName() + " (" + item.getItemCode() + ")");
        dialog.getDialogPane().getStyleClass().add("item-details-popup");

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.CENTER);
        ImageView largeImageView = new ImageView();
        largeImageView.setFitHeight(250);
        largeImageView.setPreserveRatio(true);
        try {
            if (item.getImagePath() != null && !item.getImagePath().isEmpty()
                    && !item.getImagePath().equals("null")) {
                File imageFile = Paths.get(/*InventoryManager.RESOURCES_DIR, */InventoryManager.IMAGE_DIR,
                        item.getImagePath()).toFile();
                if (imageFile.exists()) {
                    largeImageView.setImage(new Image(imageFile.toURI().toString()));
                } else {
                    System.err.println("Image file not found: " + imageFile.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading image for item " + item.getItemCode() + ": " + e.getMessage());
        }
        GridPane detailsGrid = new GridPane(); detailsGrid.setHgap(10); detailsGrid.setVgap(8);
        detailsGrid.add(createBoldLabel("Brand:"),
                0, 0); detailsGrid.add(new Label(item.getBrand()),
                1, 0);
        detailsGrid.add(createBoldLabel("Category:"),
                0, 1); detailsGrid.add(new Label(item.getCategory()),
                1, 1);
        detailsGrid.add(createBoldLabel("Price:"),
                0, 2); detailsGrid.add(new Label(String.format("$%.2f", item.getPrice())),
                1, 2);
        detailsGrid.add(createBoldLabel("Quantity:"),
                0, 3); detailsGrid.add(new Label(String.valueOf(item.getQuantity())),
                1, 3);
        detailsGrid.add(createBoldLabel("Low Stock Threshold:"),
                0, 4); detailsGrid.add(new Label(String.valueOf(item.getLowStockThreshold())),
                1, 4);
        detailsGrid.add(createBoldLabel("Purchased Date:"),
                0, 5); detailsGrid.add(new Label(item.getPurchasedDate().toString()),
                1, 5);
        // Add dealer information
        InventoryManager manager = InventoryManager.getInstance();
        String dealerName = manager.getDealers().stream()
                .filter(d -> d.getDealerId().equals(item.getDealerId()))
                .map(Dealer::getName)
                .findFirst()
                .orElse("N/A");
        detailsGrid.add(createBoldLabel("Dealer:"),
                0, 6); detailsGrid.add(new Label(dealerName),
                1, 6);


        content.getChildren().addAll(largeImageView, new Separator(), detailsGrid);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }


     // Creates a TableRow that responds to a double-click.

    public static TableRow<Item> createRowWithDoubleClick(java.util.function.Consumer<Item> action) {
        TableRow<Item> row = new TableRow<>();
        row.setOnMouseClicked(event -> {
            if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                action.accept(row.getItem());
            }
        });
        return row;
    }


     //Sets the application icon on any dialog window.
    public static void setAlertIcon(Dialog<?> dialog) {
        try {
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            dialog.getDialogPane().getStylesheets().add(UIHelper.class.getResource("styles.css").toExternalForm());
            stage.getIcons().add(new Image(Objects.requireNonNull(UIHelper.class.getResource(
                    "/icons/logo.png")).toExternalForm()));
        } catch (NullPointerException e) {
            System.err.println("Error: Dialog icon 'logo.png' not found." +
                    " Please ensure it's in src/main/resources/icons/.");
        } catch (Exception e) {
            System.err.println("Warning: Could not load dialog icon: " + e.getMessage());
        }
    }


     // Sets the application icon on any Stage (for new windows).

    public static void setAlertIcon(Stage stage) {
        try {
            stage.getIcons().add(new Image(Objects.requireNonNull(UIHelper.class.getResource(
                    "/icons/logo.png")).toExternalForm()));
        } catch (NullPointerException e) {
            System.err.println("Error: Stage icon 'logo.png' not found." +
                    " Please ensure it's in src/main/resources/icons/.");
        } catch (Exception e) {
            System.err.println("Warning: Could not load stage icon: " + e.getMessage());
        }
    }

    private static Label createBoldLabel(String text) {
        Label label = new Label(text); label.setFont(Font.font("System", FontWeight.BOLD, 12));
        return label; }

    public static void showLowStockAlert(List<Item> lowStockItems) {
        if (!lowStockItems.isEmpty()) {
            String content = lowStockItems.stream().map(i ->
                    "• " + i.getItemName() +
                            " (Qty: " + i.getQuantity() + ", Threshold: " +
                            i.getLowStockThreshold() + ")").collect(Collectors.joining("\n"));
            showAlert(Alert.AlertType.WARNING, "Low Stock Alert", content);
        } else {
            showAlert(Alert.AlertType.INFORMATION, "No Low Stock",
                    "All items are above their low stock threshold.");
        }
    }
    //Help Button
    public static void showHelpDialog() {
        String helpContent = """
            Welcome to John's Super Market IMS!
            ---
            Main Menu (Left Side)
            - Inventory: The main screen to view and manage all your products.
            - Dealers: A view to see details about your suppliers.
            - Check Low Stock: Instantly generates a report of items running low.
            - Export as Text: Saves your current inventory list as a formatted text file.
            - Help: Shows this information window.
            - Save All: Manually saves all changes to disk.
            - Exit: Closes the application (also saves automatically).
            ---
            Key Features
            - You can select sort in tables by clicking the column header.
            - In-Table Images: See a thumbnail of your product directly in the table.
            - Double-Click for Details: Double-click any item in any table to see a larger image and full details.
            - Image Management: When you add an image to an item,
             the app saves a copy in a local 'resources/images' folder.
              This means your images won't get lost if you move the app or the original file.
            """;
        showAlert(Alert.AlertType.INFORMATION, "Application Features Guide", helpContent);
    }
}
