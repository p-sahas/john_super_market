package com.example.johnssupermarket;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
//This is  connected with dealer_view.fxml
public class DealerController {
    @FXML private TableView<Item> dealerItemsTable;
    @FXML private TableColumn<Item, String> colDealerItemImage,
            colDealerItemCode, colDealerItemName, colDealerItemBrand, colDealerItemCategory;
    @FXML private TableColumn<Item, Double> colDealerItemPrice;
    @FXML private TableColumn<Item, Integer> colDealerItemQty;
    @FXML private Label dealerNameLabel, dealerLocationLabel,
            dealerContactPersonLabel, dealerContactEmailLabel;
    @FXML private ListView<Dealer> dealerListView;
    @FXML private Button refreshDealersButton, viewAllDealersButton;

    private final InventoryManager manager = InventoryManager.getInstance();
    private final ObservableList<Dealer> dealerData = FXCollections.observableArrayList();
    private final ObservableList<Item> dealerItemData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        UIHelper.setupButtonIcons(
                new Button[]{refreshDealersButton, viewAllDealersButton},
                new String[]{"refresh.png", "dealers.png"} // Reusing dealers.png for "View All Dealers"
        );
        setupDealerView();

    }

    private void setupDealerView() {
        dealerListView.setItems(dealerData);
        dealerListView.setCellFactory(p -> new ListCell<>() {
            @Override protected void updateItem(Dealer d, boolean e) {
                super.updateItem(d, e);
                setText(e || d == null ? null : d.getName() + " (" + d.getLocation() + ")");
            }
        });
        dealerListView.getSelectionModel().selectedItemProperty().addListener(
                (o, oldVal, newVal) -> showDetailsForDealer(newVal));

        colDealerItemImage.setCellValueFactory(new PropertyValueFactory<>("imagePath"));
        colDealerItemImage.setCellFactory(param -> new ImageTableCell<>());
        colDealerItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDealerItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colDealerItemBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colDealerItemCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colDealerItemPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colDealerItemQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        dealerItemsTable.setItems(dealerItemData);
        dealerItemsTable.setRowFactory(tv ->
                UIHelper.createRowWithDoubleClick(this::showItemDetailsPopup));

        // Set preferred widths for better readability
        colDealerItemImage.setPrefWidth(60);
        colDealerItemCode.setPrefWidth(80);
        colDealerItemName.setPrefWidth(150);
        colDealerItemBrand.setPrefWidth(100);
        colDealerItemCategory.setPrefWidth(100);
        colDealerItemPrice.setPrefWidth(80);
        colDealerItemQty.setPrefWidth(80);
    }
    //Refresh Dealers Button
    @FXML
    private void handleRefreshDealers() {
        List<Dealer> r = manager.getRandomDealers(4);
        manager.sortDealersByLocation(r);
        dealerData.setAll(r);
        dealerListView.getSelectionModel().clearSelection();
        dealerItemData.clear();
        clearDealerDetails();
    }

    //View All Dealers Button
    @FXML
    private void handleViewAllDealers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("all-dealers-view.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("All Registered Dealers");
            UIHelper.setAlertIcon(stage); // Set icon for the new window
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            UIHelper.showAlert(Alert.AlertType.ERROR, "Error", "Could not load all dealers view.");
        }
    }
    //Dealers Detail Section
    private void showDetailsForDealer(Dealer d) {
        if (d != null) {
            dealerNameLabel.setText(d.getName());
            dealerLocationLabel.setText(d.getLocation());
            dealerContactPersonLabel.setText(d.getContactPerson());
            dealerContactEmailLabel.setText(d.getContactEmail());
            dealerItemData.setAll(manager.getItemsByDealer(d));
        }
    }
    //View Before Select a Dealer
    private void clearDealerDetails() {
        dealerNameLabel.setText("N/A");
        dealerLocationLabel.setText("N/A");
        dealerContactPersonLabel.setText("N/A");
        dealerContactEmailLabel.setText("N/A");
    }
    //Item Detail Popup when double click
    private void showItemDetailsPopup(Item item) { UIHelper.showItemDetailsPopup(item);
    }
}
