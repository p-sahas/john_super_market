
package com.example.johnssupermarket;

import org.junit.Test;
import static org.junit.Assert.*;

public class MainControllerTest {

    @Test
    public void testControllerInitialization() {
        // Test Case: Verify that the controller can be instantiated.
        // This ensures the class doesn't crash on creation.

        try {
            MainController controller = new MainController();
            assertNotNull("Controller instance should not be null.", controller);
        } catch (Exception e) {
            fail("MainController instantiation should not throw an exception: " + e.getMessage());
        }
    }
}
