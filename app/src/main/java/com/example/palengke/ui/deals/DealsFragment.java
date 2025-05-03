package com.example.palengke.ui.deals;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.palengke.R;
import com.example.palengke.databinding.FragmentDealsBinding;

public class DealsFragment extends Fragment {

    private FragmentDealsBinding binding;
    private DealsAdapter dealsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDealsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        DealsViewModel dealsViewModel = new ViewModelProvider(this).get(DealsViewModel.class);

        RecyclerView recyclerView = binding.recyclerView;
        dealsAdapter = new DealsAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(dealsAdapter);

        // Static product data
        String[] productTitles = {"Nike Shoes", "Rolex Watch", "Banana", "Shein"};
        String[] productPrices = {"P120", "P5000", "P30", "P3490"};
        String[] quantities = {"1", "2", "3", "4"};
        Integer[] productImages = {
                R.drawable.nike_brand,
                R.drawable.rolex_brand,
                R.drawable.banana_img,
                R.drawable.shein_brand
        };

        // Set data to ViewModel first
        dealsViewModel.setProductData(productTitles, productPrices, productImages, quantities);

        // Now observe and update adapter
        dealsViewModel.getProductTitles().observe(getViewLifecycleOwner(), titles -> {
            String[] prices = dealsViewModel.getProductPrices().getValue();
            Integer[] images = dealsViewModel.getProductImages().getValue();
            String[] quantity = dealsViewModel.getProductQuantities().getValue();

            if (titles != null && prices != null && images != null && quantity != null) {
                dealsAdapter.setProductData(titles, prices, images, quantity);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
