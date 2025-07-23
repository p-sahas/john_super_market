
package com.example.johnssupermarket;

import org.junit.Test;
import static org.junit.Assert.*;

public class DealerTest {

    @Test
    public void testDealerGetters() {
        // Test Case: Verify that the getters of the Dealer class return the correct values.
        // Input: A new Dealer object initialized with specific data.
        Dealer dealer = new Dealer("D01", "Test Dealer", "Test Location",
                "Test Person", "test@email.com");

        // Expected Output: The getter methods should return the values they were initialized with.
        assertEquals("Dealer ID should match the initialized value.",
                "D01", dealer.getDealerId());
        assertEquals("Name should match the initialized value.",
                "Test Dealer", dealer.getName());
        assertEquals("Location should match the initialized value.",
                "Test Location", dealer.getLocation());
        assertEquals("Contact person should match the initialized value.",
                "Test Person", dealer.getContactPerson());
        assertEquals("Contact email should match the initialized value.",
                "test@email.com", dealer.getContactEmail());
    }

    @Test
    public void testSuppliedItemCodes() {
        // Test Case: Verify that the supplied item codes list can be set and get correctly.
        // Input: A list of item codes.
        Dealer dealer = new Dealer("D02", "Another Dealer", "Location B",
                "Person 2", "p2@email.com");
        java.util.List<String> itemCodes = new java.util.ArrayList<>();
        itemCodes.add("A001");
        itemCodes.add("B002");

        dealer.setSuppliedItemCodes(itemCodes);

        // Expected Output: The getSuppliedItemCodes method should return the list that was set.
        assertEquals("The list of supplied item codes should have 2 items.",
                2, dealer.getSuppliedItemCodes().size());
        assertEquals("The retrieved list should match the set list.",
                itemCodes, dealer.getSuppliedItemCodes());
    }
}
