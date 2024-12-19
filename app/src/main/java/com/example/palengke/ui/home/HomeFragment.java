package com.example.palengke.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.palengke.R;
import com.example.palengke.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeAdapter homeAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize RecyclerView and Adapter
        RecyclerView recyclerView = binding.recyclerView;
        homeAdapter = new HomeAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(homeAdapter);

        // Set default product data directly or via ViewModel
        int[] productImage = {
                R.drawable.banana_img, R.drawable.broccoli_img, R.drawable.beef_img,
                R.drawable.cabbage_img, R.drawable.carrots_img, R.drawable.chicken_img,
                R.drawable.eggs_img, R.drawable.grapes_img, R.drawable.pork_img, R.drawable.tomato_img
        };

        String[] productName = {
                "Banana", "Broccoli", "Beef",
                "Cabbage", "Carrots", "Chicken",
                "Eggs", "Grapes", "Pork", "Tomato"
        };

        String[] productPrice = {
                "₱50", "₱100", "₱350",
                "₱75", "₱80", "₱170",
                "₱210", "₱310", "₱250", "₱180"
        };

        String[] productQuantity = {
                "Qty: 2 V", "Qty: 3 V", "Qty: 68 V",
                "Qty: 546 V", "Qty: 45 V", "Qty: 243 V",
                "Qty: 456 V", "Qty: 21 V", "Qty: 987 V", "Qty: 545 V"
        };

        homeAdapter.setProductData(productImage, productName, productPrice, productQuantity);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
