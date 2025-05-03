package com.example.palengke.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.palengke.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private int[] image;
    private String[] name;
    private String[] price;
    private String[] quantity;

    public void setProductData(int[] image, String[] name, String[] price, String[] quantity) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageImageView.setImageResource(image[position]);
        holder.nameTextView.setText(name[position]);
        holder.priceTextView.setText(price[position]);
        holder.quantityTextView.setText(quantity[position]);

        holder.addToCartButton.setOnClickListener(v -> {
            // Create a cart product object
            Map<String, Object> cartItem = new HashMap<>();
            cartItem.put("imageResId", image[position]);
            cartItem.put("name", name[position]);
            cartItem.put("price", price[position]);
            cartItem.put("quantity", quantity[position]);

            // Get current user
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null) {
                String userId = user.getUid();
                FirebaseDatabase.getInstance()
                        .getReference("cart")
                        .child(userId)
                        .push()
                        .setValue(cartItem)
                        .addOnSuccessListener(aVoid ->
                                Toast.makeText(v.getContext(), "Added to your cart", Toast.LENGTH_SHORT).show()
                        )
                        .addOnFailureListener(e ->
                                Toast.makeText(v.getContext(), "Failed to add to cart", Toast.LENGTH_SHORT).show()
                        );
            } else {
                Toast.makeText(v.getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return name != null ? name.length : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageImageView;
        TextView nameTextView;
        TextView priceTextView;
        TextView quantityTextView;
        Button addToCartButton;

        public ViewHolder(View view) {
            super(view);
            imageImageView = view.findViewById(R.id.productImage);
            nameTextView = view.findViewById(R.id.productName);
            priceTextView = view.findViewById(R.id.productPrize);
            quantityTextView = view.findViewById(R.id.productQuantity);
            addToCartButton = view.findViewById(R.id.addToCartButton);
        }
    }
}
