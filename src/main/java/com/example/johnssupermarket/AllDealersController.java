package com.example.johnssupermarket;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
//This class for Managing All Dealers.
public class AllDealersController {

    @FXML private TableView<Dealer> allDealersTable;
    @FXML private TableColumn<Dealer, String> colDealerId;
    @FXML private TableColumn<Dealer, String> colName;
    @FXML private TableColumn<Dealer, String> colLocation;
    @FXML private TableColumn<Dealer, String> colContactPerson;
    @FXML private TableColumn<Dealer, String> colContactEmail;
    @FXML private Button closeButton;

    private final InventoryManager manager = InventoryManager.getInstance();
    private final ObservableList<Dealer> allDealersData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadAllDealers();
    }
//All Dealers Detail Table
    private void setupTableColumns() {
        colDealerId.setCellValueFactory(new PropertyValueFactory<>("dealerId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colContactPerson.setCellValueFactory(new PropertyValueFactory<>("contactPerson"));
        colContactEmail.setCellValueFactory(new PropertyValueFactory<>("contactEmail"));

        // Set preferred widths for better readability
        colDealerId.setPrefWidth(80);
        colName.setPrefWidth(150);
        colLocation.setPrefWidth(120);
        colContactPerson.setPrefWidth(150);
        colContactEmail.setPrefWidth(200);
    }

    private void loadAllDealers() {
        // Sort dealers by ID for consistent display in "All Dealers" view
        List<Dealer> sortedDealers = new ArrayList<>(manager.getDealers());
        sortedDealers.sort(Comparator.comparing(Dealer::getDealerId));
        allDealersData.setAll(sortedDealers);
        allDealersTable.setItems(allDealersData);
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
