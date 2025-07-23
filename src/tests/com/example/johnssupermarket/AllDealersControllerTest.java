package com.example.johnssupermarket;

import org.junit.Test;
import static org.junit.Assert.*;

public class AllDealersControllerTest {

    @Test
    public void testControllerInitialization() {
        // Test Case: Verify that the controller can be instantiated.
        // This Test to ensure the class doesn't crash on creation.
        try {
            AllDealersController controller = new AllDealersController();
            assertNotNull("Controller instance should not be null.", controller);
        } catch (Exception e) {
            fail("AllDealersController instantiation should not throw an exception: " + e.getMessage());
        }
    }
}
