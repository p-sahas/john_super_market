
package com.example.johnssupermarket;

import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class InventoryManagerTest {

    private InventoryManager inventoryManager;

    @Before
    public void setUp() throws Exception {
        // Reset the InventoryManager singleton instance for each test.
        // This is crucial to ensure each test starts with a clean state for the singleton.
        try {
            Field instance = InventoryManager.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(null, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Could not reset InventoryManager singleton: " + e.getMessage());
        }

        inventoryManager = InventoryManager.getInstance();

        // Ensure in-memory lists are clear for a fresh start
        inventoryManager.getInventory().clear();
        inventoryManager.getDealers().clear();
    }

    @Test
    public void getInstance() {
        InventoryManager instance1 = InventoryManager.getInstance();
        InventoryManager instance2 = InventoryManager.getInstance();
        assertNotNull("InventoryManager instance should not be null", instance1);
        assertSame("getInstance() should return the same instance", instance1, instance2);
    }

    @Test
    public void addItem() {
        Item newItem = new Item("T001", "Test Item", "Test Brand",
                10.0, 5, "Test Category",
                LocalDate.now(), "test.png", 2, "D01");
        inventoryManager.addItem(newItem);
        assertEquals("Inventory should have 1 item after adding a new one.",
                1, inventoryManager.getInventory().size());
        assertEquals("The added item's name should match.",
                "Test Item", inventoryManager.getInventory().get(0).getItemName());
    }
/*
    @Test
    public void addDuplicateItem() {
        Item item1 = new Item("T001", "Test Item 1", "Test Brand", 10.0, 5, "Test Category",
                LocalDate.now(), "test.png", 2, "D01");
        Item item2 = new Item("T001", "Test Item 2", "Test Brand", 20.0, 10, "Test Category",
                LocalDate.now(), "test2.png", 3, "D02");
        inventoryManager.addItem(item1);
        // The second addItem call will trigger UIHelper.showAlert, which causes the JavaFX error.
        // The test will still pass if the in-memory state is correct, but the error will occur.
        inventoryManager.addItem(item2);
        assertEquals("Inventory should not add an item with a duplicate code.", 1,
         inventoryManager.getInventory().size());
    }
*/
    @Test
    public void deleteItem() {
        Item item = new Item("T001", "Test Item", "Test Brand",
                10.0, 5, "Test Category",
                LocalDate.now(), "test.png", 2, "D01");
        inventoryManager.addItem(item);
        assertEquals("Inventory should have 1 item before deletion.", 1,
                inventoryManager.getInventory().size());

        inventoryManager.deleteItem("T001");

        assertTrue("Inventory should be empty after deleting the item.",
                inventoryManager.getInventory().isEmpty());
    }

    @Test
    public void updateItem() {
        Item originalItem = new Item("T001", "Original Name",
                "Original Brand", 10.0, 5, "Original Category",
                LocalDate.now(), "original.png", 2, "D01");
        inventoryManager.addItem(originalItem);

        Item updatedItem = new Item("T001", "Updated Name", "Updated Brand",
                20.0, 10, "Updated Category",
                LocalDate.now(), "updated.png", 3, "D02");
        inventoryManager.updateItem(updatedItem);

        assertEquals("Inventory size should remain 1 after update.", 1,
                inventoryManager.getInventory().size());
        assertEquals("Item name should be updated.", "Updated Name",
                inventoryManager.getInventory().get(0).getItemName());
        assertEquals("Item price should be updated.", 20.0,
                inventoryManager.getInventory().get(0).getPrice(), 0.001);
    }

    @Test
    public void getLowStockItems() {
        Item lowStockItem = new Item("L001", "Low Stock Item", "Brand",
                10.0, 1, "Category",
                LocalDate.now(), "low.png", 5, "D01");
        Item normalStockItem = new Item("N001", "Normal Stock Item", "Brand",
                10.0, 10, "Category",
                LocalDate.now(), "normal.png", 5, "D01");
        inventoryManager.addItem(lowStockItem);
        inventoryManager.addItem(normalStockItem);

        List<Item> lowStockItems = inventoryManager.getLowStockItems();

        assertEquals("Should only find 1 low stock item.", 1, lowStockItems.size());
        assertEquals("The found item should be the correct low stock item.", "L001",
                lowStockItems.get(0).getItemCode());
    }

    @Test
    public void getRandomDealers() {
        inventoryManager.getDealers().addAll(List.of(
            new Dealer("D01", "Dealer 1", "Location A", "P1", "e1@t.com"),
            new Dealer("D02", "Dealer 2", "Location B", "P2", "e2@t.com"),
            new Dealer("D03", "Dealer 3", "Location C", "P3", "e3@t.com"),
            new Dealer("D04", "Dealer 4", "Location D", "P4", "e4@t.com"),
            new Dealer("D05", "Dealer 5", "Location E", "P5", "e5@t.com")
        ));

        List<Dealer> randomDealers = inventoryManager.getRandomDealers(4);

        assertEquals("Should return the specified number of random dealers.", 4, randomDealers.size());
    }

    @Test
    public void getItemsByDealer() {
        Dealer dealer1 = new Dealer("D01", "Dealer 1", "Location A",
                "P1", "e1@t.com");
        inventoryManager.getDealers().add(dealer1);
        inventoryManager.addItem(new Item("A01", "Item 1", "B1",
                10, 5, "C1", LocalDate.now(), "i1.png",
                2, "D01"));
        inventoryManager.addItem(new Item("A02", "Item 2", "B2",
                20, 10, "C2", LocalDate.now(), "i2.png",
                3, "D02"));
        inventoryManager.addItem(new Item("A03", "Item 3", "B3",
                30, 15, "C3", LocalDate.now(), "i3.png",
                4, "D01"));

        List<Item> dealerItems = inventoryManager.getItemsByDealer(dealer1);

        assertEquals("Should find 2 items for the specified dealer.", 2, dealerItems.size());
        assertTrue("All items found should belong to the correct dealer.",
                dealerItems.stream().allMatch(item -> item.getDealerId().equals("D01")));
    }

    @Test
    public void sortInventory() {
        Item itemA = new Item("A001", "Item A", "Brand", 10.0,
                5, "Category B", LocalDate.now(), "a.png", 2, "D01");
        Item itemB = new Item("B001", "Item B", "Brand", 10.0,
                5, "Category A", LocalDate.now(), "b.png", 2, "D01");
        Item itemC = new Item("C001", "Item C", "Brand", 10.0,
                5, "Category A", LocalDate.now(), "c.png", 2, "D01");
        inventoryManager.addItem(itemA);
        inventoryManager.addItem(itemB);
        inventoryManager.addItem(itemC);

        inventoryManager.sortInventory();

        assertEquals("First item should be B001 after sorting.", "B001",
                inventoryManager.getInventory().get(0).getItemCode());
        assertEquals("Second item should be C001 after sorting.", "C001",
                inventoryManager.getInventory().get(1).getItemCode());
        assertEquals("Third item should be A001 after sorting.", "A001",
                inventoryManager.getInventory().get(2).getItemCode());
    }

    @Test
    public void sortDealersByLocation() {
        Dealer dealerA = new Dealer("D01", "Dealer A", "Location C",
                "P1", "e1@t.com");
        Dealer dealerB = new Dealer("D02", "Dealer B", "Location A",
                "P2", "e2@t.com");
        Dealer dealerC = new Dealer("D03", "Dealer C", "Location B",
                "P3", "e3@t.com");
        List<Dealer> dealers = new ArrayList<>(List.of(dealerA, dealerB, dealerC));

        inventoryManager.sortDealersByLocation(dealers);

        assertEquals("First dealer should be from Location A after sorting.", "D02", dealers.get(0).getDealerId());
        assertEquals("Second dealer should be from Location B after sorting.", "D03", dealers.get(1).getDealerId());
        assertEquals("Third dealer should be from Location C after sorting.", "D01", dealers.get(2).getDealerId());
    }

    @Test
    public void saveAllData() {
        // This test only verifies that the method can be called without throwing an exception.

        try {
            inventoryManager.saveAllData();
        } catch (Exception e) {
            fail("saveAllData should not throw an exception: " + e.getMessage());
        }
    }
}
