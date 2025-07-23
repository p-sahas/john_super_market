
package com.example.johnssupermarket;

import org.junit.Test;
import java.time.LocalDate;
import static org.junit.Assert.*;

public class ItemTest {

    @Test
    public void isLowStock_WhenQuantityIsLessThanThreshold() {
        // Test Case: Check if an item is correctly identified as low stock.
        // Input: An item where the quantity is less than the low stock threshold.
        Item item = new Item("T001", "Test Item", "Brand",
                10.0, 4, "Category",
                LocalDate.now(), "image.png", 5, "D01");

        // Expected Output: The isLowStock() method should return true.
        assertTrue("Item should be low stock when quantity < threshold.", item.isLowStock());
    }

    @Test
    public void isLowStock_WhenQuantityIsEqualToThreshold() {
        // Test Case: Check if an item is correctly identified as not low stock when quantity equals threshold.
        // Input: An item where the quantity is equal to the low stock threshold.
        Item item = new Item("T002", "Test Item", "Brand",
                10.0, 5, "Category",
                LocalDate.now(), "image.png", 5, "D01");

        // Expected Output: The isLowStock() method should return false.
        assertFalse("Item should not be low stock when quantity == threshold.", item.isLowStock());
    }

    @Test
    public void isLowStock_WhenQuantityIsGreaterThanThreshold() {
        // Test Case: Check if an item is correctly identified as not low stock when quantity is greater than threshold.
        // Input: An item where the quantity is greater than the low stock threshold.
        Item item = new Item("T003", "Test Item", "Brand",
                10.0, 6, "Category",
                LocalDate.now(), "image.png", 5, "D01");

        // Expected Output: The isLowStock() method should return false.
        assertFalse("Item should not be low stock when quantity > threshold.", item.isLowStock());
    }
}
