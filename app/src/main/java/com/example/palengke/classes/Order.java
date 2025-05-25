package com.example.palengke.classes;

public class Order {
    private String id;
    private String date;
    private String products;
    private String quantity;
    private String totalPrice;
    private String status;

    public Order() {
        // Required for Firebase deserialization
    }

    public Order(String id, String date, String products, String quantity, String totalPrice, String status) {
        this.id = id;
        this.date = date;
        this.products = products;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getProducts() { return products; }
    public void setProducts(String products) { this.products = products; }

    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }

    public String getTotalPrice() { return totalPrice; }
    public void setTotalPrice(String totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
