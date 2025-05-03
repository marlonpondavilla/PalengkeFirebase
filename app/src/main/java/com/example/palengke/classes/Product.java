package com.example.palengke.classes;

public class Product {
    private int imageResId; // Store as Integer in Firebase
    private String name;
    private String price;
    private String quantity;

    public Product() {
        // Required by Firebase
    }

    public Product(int imageResId, String name, String price, String quantity) {
        this.imageResId = imageResId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public int getImageResId() { return imageResId; }
    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getQuantity() { return quantity; }

    public void setImageResId(int imageResId) { this.imageResId = imageResId; }
    public void setName(String name) { this.name = name; }
    public void setPrice(String price) { this.price = price; }
    public void setQuantity(String quantity) { this.quantity = quantity; }
}

