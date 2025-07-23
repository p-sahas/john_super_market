package com.example.johnssupermarket;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Dealer implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L; // Version updated
    private final String dealerId;
    private final String name;
    private final String location;
    private final String contactPerson;
    private final String contactEmail;
    private List<String> suppliedItemCodes; // This field is managed by dealers.java

    public Dealer(String dealerId, String name, String location,
                  String contactPerson, String contactEmail)
    {
        this.dealerId = dealerId;
        this.name = name;
        this.location = location;
        this.contactPerson = contactPerson;
        this.contactEmail = contactEmail;
        this.suppliedItemCodes = new ArrayList<>();
    }

    //Getters and Setters for Dealers
    public String getDealerId() {
        return dealerId;
    }
    public String getName() {
        return name;
    }
    public String getLocation() {
        return location;
    }
    public String getContactPerson() {
        return contactPerson;
    }
    public String getContactEmail() {
        return contactEmail;
    }
    public List<String> getSuppliedItemCodes() {
        return suppliedItemCodes;
    }

    public void setSuppliedItemCodes(List<String> suppliedItemCodes) {
        this.suppliedItemCodes = suppliedItemCodes;
    }
}
