package com.example.palengke.ui.cart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CartViewModel extends ViewModel {

    private MutableLiveData<String[]> productName = new MutableLiveData<>();
    private MutableLiveData<String[]> productPrice = new MutableLiveData<>();
    private MutableLiveData<String[]> productQuantity = new MutableLiveData<>();

    // Getter methods for LiveData

    public LiveData<String[]> getProductName() {
        return productName;
    }

    public LiveData<String[]> getProductPrice() {
        return productPrice;
    }

    public LiveData<String[]> getProductQuantity() {
        return productQuantity;
    }

    // Method to set data
    public void setProductData(String[] name, String[] price, String[] quantity) {
        productName.setValue(name);
        productPrice.setValue(price);
        productQuantity.setValue(quantity);
    }
}