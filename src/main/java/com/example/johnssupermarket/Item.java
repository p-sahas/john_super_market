package com.example.johnssupermarket;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class Item implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L; // Version updated
    private final String itemCode;
    private String itemName;
    private String brand;
    private String category;
    private String imagePath;
    private String dealerId;
    private double price;
    private int quantity, lowStockThreshold;
    private LocalDate purchasedDate;

    public Item(String itemCode, String itemName, String brand, double price,
                int quantity, String category, LocalDate purchasedDate, String imagePath,
                int lowStockThreshold, String dealerId) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.purchasedDate = purchasedDate;
        this.imagePath = imagePath;
        this.lowStockThreshold = lowStockThreshold;
        this.dealerId = dealerId;
    }

    // Getters
    public String getItemCode() {
        return itemCode;
    }
    public String getItemName() {
        return itemName;
    }
    public String getBrand() {
        return brand;
    }
    public double getPrice() {
        return price;
    }
    public int getQuantity() {
        return quantity;
    }
    public String getCategory() {
        return category;
    }
    public LocalDate getPurchasedDate() {
        return purchasedDate;
    }
    public String getImagePath() {
        return imagePath;
    }
    public int getLowStockThreshold() {
        return lowStockThreshold;
    }
    public String getDealerId() {
        return dealerId;
    }

    //Setters (if it needed for updates, ensure they are used in updateItem logic) ---
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setLowStockThreshold(int lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }
    public void setPurchasedDate(LocalDate purchasedDate) {
        this.purchasedDate = purchasedDate;
    }


    public boolean isLowStock() {
        return this.quantity < this.lowStockThreshold;
    }
}
