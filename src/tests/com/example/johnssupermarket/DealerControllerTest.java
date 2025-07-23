
package com.example.johnssupermarket;

import org.junit.Test;
import static org.junit.Assert.*;

public class DealerControllerTest {

    @Test
    public void testControllerInitialization() {
        // Test Case: Verify that the controller can be instantiated.
        // This is ensured the class doesn't crash on creation.
        try {
            DealerController controller = new DealerController();
            assertNotNull("Controller instance should not be null.", controller);
        } catch (Exception e) {
            fail("DealerController instantiation should not throw an exception: " + e.getMessage());
        }
    }
}
