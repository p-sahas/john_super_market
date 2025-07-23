package com.example.johnssupermarket;

import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class InventoryManager {
    private static InventoryManager instance;
    private List<Item> inventory;
    private List<Dealer> dealers;

    private static final String RESOURCES_DIR = "resources";
    private static final String DATA_DIR = "data";
    public static final String IMAGE_DIR = "images";
    private static final String INVENTORY_FILE = "inventory.txt";
    private static final String DEALERS_FILE = "dealers.txt";
    private static final String DELIMITER = "||";

    private InventoryManager() {
        setupDirectories();
        loadOrInitializeData();
    }

    public static synchronized InventoryManager getInstance() {
        if (instance == null) instance = new InventoryManager();
        return instance;
    }
    //Getting Directories for Images and txt files
    private void setupDirectories() {
        try {
            Files.createDirectories(Paths.get(RESOURCES_DIR, DATA_DIR));
            Files.createDirectories(Paths.get(RESOURCES_DIR, IMAGE_DIR));
        } catch (IOException e) {
            System.err.println("Could not create resource directories: " + e.getMessage());
        }
    }

    private void loadOrInitializeData() {
        // Try loading existing data first
        boolean dealersLoaded = loadDealers();
        boolean inventoryLoaded = loadInventory();

        // Only create samples if loading failed or files were empty
        if (!dealersLoaded || dealers.isEmpty()) {
            createSampleDealers();
            saveDealersToText();
        }
        if (!inventoryLoaded || inventory.isEmpty()) {
            createSampleInventory();
            saveInventoryToText();
        }
    }

    public List<Item> getInventory() {
        return inventory;
    }
    public List<Dealer> getDealers() {
        return dealers;
    }
    public void addItem(Item item) {
        if (inventory.stream().noneMatch(
                i -> i.getItemCode().equalsIgnoreCase(item.getItemCode()))) {
            inventory.add(item);
            saveInventoryToText(); // Save after adding
        } else {
            UIHelper.showAlert(Alert.AlertType.WARNING,
                    "Duplicate Item Code",
                    "An item with this code already exists." +
                            " Please use 'Update Item' if you wish to modify it.");
        }
    }
    public void deleteItem(String itemCode) {
        boolean removed = inventory.removeIf(item -> item.getItemCode().equalsIgnoreCase(itemCode));
        if (removed) {
            saveInventoryToText(); // Save after deleting
        }
    }
    public void updateItem(Item updatedItem) {
        for (int i = 0; i < inventory.size(); i++)
        {
            if (inventory.get(i).getItemCode().equalsIgnoreCase(updatedItem.getItemCode())) {
                inventory.set(i, updatedItem);
                saveInventoryToText(); // Save after updating
                return;
            }
        }
    }
    public List<Item> getLowStockItems() {
        return inventory.stream().filter(Item::isLowStock).collect(Collectors.toList());
    }

    /*
     * Randomly selects a specified number of dealers using a custom Fisher-Yates shuffle.
     * @param count The number of random dealers to select.
     * @return A list of randomly selected dealers.
     */
    public List<Dealer> getRandomDealers(int count) {
        if (dealers.size() < count) {
            return new ArrayList<>(dealers); // Return all if not enough dealers
        }

        List<Dealer> shuffledList = new ArrayList<>(dealers); // Create a mutable copy

        // Custom Fisher-Yates shuffle algorithm
        Random rand = new Random();
        for (int i = shuffledList.size() - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1); // Generate a random index between 0 and i (inclusive)
            // Swap elements at i and j
            Dealer temp = shuffledList.get(i);
            shuffledList.set(i, shuffledList.get(j));
            shuffledList.set(j, temp);
        }

        return shuffledList.subList(0, count);
    }

    public List<Item> getItemsByDealer(Dealer dealer) {
        if (dealer == null) return new ArrayList<>();
        return inventory.stream().filter(item ->
                dealer.getDealerId() != null
                        && dealer.getDealerId().equals(item.getDealerId())).collect(Collectors.toList());
    }

    /*
     * Sorts the inventory list by item category (ascending), then by item code (ascending)
     * Using Bubble Sort algorithm.
     */
    public void sortInventory() {
        int n = inventory.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                Item item1 = inventory.get(j);
                Item item2 = inventory.get(j + 1);

                // Compare by category first
                int categoryComparison = item1.getCategory().compareToIgnoreCase(item2.getCategory());

                if (categoryComparison > 0) {
                    // If item1's category comes after item2's category, swap them
                    // Custom swap implementation
                    Item temp = inventory.get(j);
                    inventory.set(j, inventory.get(j + 1));
                    inventory.set(j + 1, temp);
                } else if (categoryComparison == 0) {
                    // If categories are the same, compare by item code
                    int itemCodeComparison = item1.getItemCode().compareToIgnoreCase(item2.getItemCode());
                    if (itemCodeComparison > 0) {
                        // If item1's item code comes after item2's item code, swap them
                        // Custom swap implementation
                        Item temp = inventory.get(j);
                        inventory.set(j, inventory.get(j + 1));
                        inventory.set(j + 1, temp);
                    }
                }
            }
        }
    }

    /*
     * Sorts a list of dealers by location (ascending) using a custom Selection Sort algorithm.
     * @param list The list of dealers to sort.
     */
    public void sortDealersByLocation(List<Dealer> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (list.get(j).getLocation().compareToIgnoreCase(list.get(minIndex).getLocation()) < 0) {
                    minIndex = j;
                }
            }
            // Custom swap implementation
            Dealer temp = list.get(minIndex);
            list.set(minIndex, list.get(i));
            list.set(i, temp);
        }
    }

    public void saveAllData() {
        saveInventoryToText();
        saveDealersToText();
    }

    private void saveInventoryToText() {
        Path filePath = Paths.get(RESOURCES_DIR, DATA_DIR, INVENTORY_FILE);
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(filePath))) {
            for (Item item : inventory) {
                String imagePath = item.getImagePath() == null ? "null" : item.getImagePath();
                String dealerId = item.getDealerId() == null ? "null" : item.getDealerId(); // Handle null dealerId
                String line = String.join(DELIMITER, item.getItemCode(),
                        item.getItemName(), item.getBrand(), String.valueOf(item.getPrice()),
                        String.valueOf(item.getQuantity()), item.getCategory(),
                        item.getPurchasedDate().toString(), imagePath,
                        String.valueOf(item.getLowStockThreshold()), dealerId);
                writer.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error saving inventory: " + e.getMessage());
        }
    }

    private boolean loadInventory() {
        inventory = new ArrayList<>();
        Path filePath = Paths.get(RESOURCES_DIR, DATA_DIR, INVENTORY_FILE);
        if (!Files.exists(filePath)) {
            return false;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(Pattern.quote(DELIMITER)); // Use Pattern.quote for literal delimiter
                if (parts.length == 10) {
                    try {
                        String category = parts[5].trim();
                        // Handle null for imagePath and dealerId during loading
                        inventory.add(new Item(parts[0], parts[1], parts[2],
                                Double.parseDouble(parts[3]), Integer.parseInt(parts[4]),
                                category, LocalDate.parse(parts[6]), parts[7].equals("null") ? null :
                                parts[7], Integer.parseInt(parts[8]), parts[9].equals("null") ? null : parts[9]));
                    } catch (Exception e) {
                        System.err.println("Skipping malformed inventory line: " + line + " Error: " + e.getMessage());
                    }
                } else {
                    System.err.println("Skipping malformed inventory line (incorrect number of parts): " + line);
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error reading inventory file: " + e.getMessage());
            return false;
        }
    }

    private void saveDealersToText() {
        Path filePath = Paths.get(RESOURCES_DIR, DATA_DIR, DEALERS_FILE);
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(filePath))) {
            for (Dealer dealer : dealers) {
                String line = String.join(DELIMITER, dealer.getDealerId(),
                        dealer.getName(), dealer.getLocation(),
                        dealer.getContactPerson(), dealer.getContactEmail());
                writer.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error saving dealers: " + e.getMessage());
        }
    }

    private boolean loadDealers() {
        dealers = new ArrayList<>();
        Path filePath = Paths.get(RESOURCES_DIR, DATA_DIR, DEALERS_FILE);
        if (!Files.exists(filePath)) {
            return false;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(Pattern.quote(DELIMITER)); // Use Pattern.quote for literal delimiter
                if (parts.length == 5) {
                    try {
                        dealers.add(new Dealer(parts[0], parts[1], parts[2], parts[3], parts[4]));
                    } catch (Exception e) {
                        System.err.println("Skipping malformed dealer line: "
                                + line + " Error: " + e.getMessage());
                    }
                } else {
                    System.err.println("Skipping malformed dealer line (incorrect number of parts): " + line);
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error reading dealers file: " + e.getMessage());
            return false;
        }
    }
    //Creating a Sample Data if Inventory and Dealers txt files does not exists.
    // This is not a necessary
    private void createSampleInventory() {
        inventory = new ArrayList<>();
        inventory.add(new Item("E001", " Inspire Laptop", "Dell",
                125000.00, 8, "Electronics", LocalDate.of(2025, 1, 15),
                "laptop.png", 5, "D01"));
        inventory.add(new Item("C001", "Wavves Slippers", "DSI",
                1200.00, 4, "Foot Wear", LocalDate.of(2025, 2, 1),
                "slippers.jpg", 5, "D02"));
        inventory.add(new Item("F001", "Red Apples", "Keells",
                175, 150, "Groceries", LocalDate.of(2025, 6, 10),
                "apple.png", 50, "D03"));
        inventory.add(new Item("E002", "Wireless Mouse", "Logitech",
                7000.00, 25, "Electronics", LocalDate.of(2025, 4, 5),
                "mouse.jpg", 20, "D01"));
        inventory.add(new Item("H001", "Drill Set", "Man Power",
                25000.00, 15, "Hardware", LocalDate.of(2024, 12, 20),
                "drill.jpg", 5, "D04"));
        inventory.add(new Item("B001", "Percy Jackson 01", "Rick Riordan",
                1500.00, 50, "Books", LocalDate.of(2024, 6, 1),
                "percyjackson.jpg", 10, null));
        inventory.add(new Item("T001", "T-Shirt", "Adidas",
                2500.00, 30, "Clothing", LocalDate.of(2024, 7, 10),
                "adidas.jpg", 10, "D02"));
    }
    private void createSampleDealers() {
        dealers = new ArrayList<>();
        dealers.add(new Dealer("D01", "Sahas Tech Inc.", "Gampaha",
                "Damru Nimsara ", "Damru@tech.com"));
        dealers.add(new Dealer("D02", "DSI Inc.", "Galle",
                "Sarah Sari", "sarah@dsi.com"));
        dealers.add(new Dealer("D03", "Keells", "Colombo 03",
                "Himasara Jay", "hima@keells.com"));
        dealers.add(new Dealer("D04", "Boomer Suppliers", "Naranwala",
                "Randika Achala", "achala@boomer.com"));
        dealers.add(new Dealer("D05", "Melsta Corp", "Dematagoda",
                "Harry Jay", "Harry@melsta.com"));
        dealers.add(new Dealer("D06", "Ranjan Lanka Ltd.", "Gampaha",
                "Nipun Navodya", "Nipun@ranjan.com"));
        dealers.add(new Dealer("D07", "Cargills", "Kandy",
                "Pavindu Perera", "pavindu@cargills.com"));
    }
}
