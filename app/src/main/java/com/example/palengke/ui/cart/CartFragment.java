package com.example.palengke.ui.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.palengke.R;
import com.example.palengke.databinding.FragmentCartBinding;

public class CartFragment extends Fragment {

    private FragmentCartBinding binding;
    private CartAdapter cartAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Correctly initialize binding
        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize RecyclerView and Adapter
        RecyclerView recyclerView = binding.recyclerView;
        cartAdapter = new CartAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(cartAdapter);

        // Set default product data
        int[] productImage = {
                R.drawable.banana_img,
                R.drawable.cabbage_img,
                R.drawable.eggs_img
        };

        String[] productName = {
                "Banana",
                "Cabbage",
                "Eggs"
        };

        String[] productPrice = {
                "₱50",
                "₱75",
                "₱210"
        };

        String[] productQuantity = {
                "Qty: 2 V",
                "Qty: 546 V",
                "Qty: 456 V"
        };

        cartAdapter.setProductData(productImage, productName, productPrice, productQuantity);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Clean up binding reference
    }
}
