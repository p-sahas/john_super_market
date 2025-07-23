
package com.example.johnssupermarket;

import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void testApplicationInitialization() {
        // Test Case: Verify that the main application class can be instantiated.
        try {
            Main main = new Main();
            assertNotNull("Main application instance should not be null.", main);
        } catch (Exception e) {
            fail("Main instantiation should not throw an exception: " + e.getMessage());
        }
    }
}
