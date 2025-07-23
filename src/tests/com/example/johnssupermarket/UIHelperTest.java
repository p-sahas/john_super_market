package com.example.johnssupermarket;

import org.junit.Test;
import static org.junit.Assert.*;

public class UIHelperTest {

    @Test
    public void testUIHelperInstantiation() {
        // Test Case: Verify that the UIHelper class can be instantiated.

        try {
            UIHelper helper = new UIHelper();
            assertNotNull("UIHelper instance should not be null.", helper);
        } catch (Exception e) {
            fail("UIHelper instantiation should not throw an exception: " + e.getMessage());
        }
    }
}