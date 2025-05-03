package com.example.palengke.classes;

public class CartItem {
    private String id;
    private String name;
    private String price;
    private String quantity;
    private int imageResId;

    public CartItem() {
        // Required by Firebase
    }

    // Constructor
    public CartItem(String id, String name, String price, String quantity, int imageResId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageResId = imageResId;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Other getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}
