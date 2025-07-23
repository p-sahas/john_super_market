
package com.example.johnssupermarket;

import org.junit.Test;
import static org.junit.Assert.*;

public class InventoryControllerTest {

    @Test
    public void testControllerInitialization() {
        // Test Case: Verify that the controller can be instantiated.
        try {
            InventoryController controller = new InventoryController();
            assertNotNull("Controller instance should not be null.", controller);
        } catch (Exception e) {
            fail("InventoryController instantiation should not throw an exception: " + e.getMessage());
        }
    }
}
