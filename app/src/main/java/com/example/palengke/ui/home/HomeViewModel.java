package com.example.palengke.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<Integer[]> productImage = new MutableLiveData<>();
    private MutableLiveData<String[]> productName = new MutableLiveData<>();
    private MutableLiveData<String[]> productPrice = new MutableLiveData<>();
    private MutableLiveData<String[]> productQuantity = new MutableLiveData<>();

    // Getter methods for LiveData
    public LiveData<Integer[]> getProductImage() {
        return productImage;
    }

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
    public void setProductData(Integer[] image, String[] name, String[] price, String[] quantity) {
        productImage.setValue(image);
        productName.setValue(name);
        productPrice.setValue(price);
        productQuantity.setValue(quantity);
    }
}