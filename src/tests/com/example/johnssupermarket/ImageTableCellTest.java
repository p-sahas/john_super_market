
package com.example.johnssupermarket;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

public class ImageTableCellTest {

    @Ignore("Requires JavaFX Toolkit to be initialized. Not suitable for standard JUnit.")
    @Test
    public void testCellInitialization() {
        // Test Case: Verify that the custom table cell can be instantiated.
        // This test primarily ensures the class can be created without immediate errors.
        try {
            ImageTableCell<Object> cell = new ImageTableCell<>();
            assertNotNull("ImageTableCell instance should not be null.", cell);
        } catch (Exception e) {
            fail("ImageTableCell instantiation should not throw an exception: " + e.getMessage());
        }
    }
}
