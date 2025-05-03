package com.example.palengke.classes;

public class DealsItem {
    private String name;
    private String price;
    private String quantity;

    // Default constructor for Firebase
    public DealsItem() {}

    public DealsItem(String name, String price, String quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }
}
