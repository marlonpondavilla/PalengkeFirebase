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
import com.example.palengke.classes.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
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
        final int pos = position; // Make a final copy of position

        holder.imageImageView.setImageResource(image[pos]);
        holder.nameTextView.setText(name[pos]);
        holder.priceTextView.setText(price[pos]);
        holder.quantityTextView.setText(quantity[pos]);

        // Buy Now button click
        holder.buyNowBtn.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(v.getContext())
                    .setTitle("Buy Now")
                    .setMessage("Are you sure you want to buy \"" + name[pos] + "\"?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();
                            String productId = FirebaseDatabase.getInstance().getReference().push().getKey();

                            if (productId != null) {
                                DatabaseReference buyRef = FirebaseDatabase.getInstance()
                                        .getReference("BuyProducts")
                                        .child(userId)
                                        .child(productId);

                                // Clean quantity and price from symbols like ₱, spaces, etc.
                                String cleanQuantity = quantity[pos].replaceAll("[^\\d]", "");
                                String cleanPrice = price[pos].replaceAll("[^\\d.]", "");

                                Map<String, Object> productData = new HashMap<>();
                                productData.put("id", productId);
                                productData.put("name", name[pos]);
                                productData.put("price", cleanPrice);
                                productData.put("quantity", cleanQuantity);
                                productData.put("image", image[pos]);

                                buyRef.setValue(productData)
                                        .addOnSuccessListener(aVoid ->
                                                Toast.makeText(v.getContext(), "You have successfully bought the product", Toast.LENGTH_SHORT).show()
                                        )
                                        .addOnFailureListener(e ->
                                                Toast.makeText(v.getContext(), "Failed to buy product", Toast.LENGTH_SHORT).show()
                                        );
                            }
                        } else {
                            Toast.makeText(v.getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        // Add to Cart button click
        holder.addToCartButton.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userId = user.getUid();
                DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("cart").child(userId);
                String productName = name[pos];
                String productQuantity = quantity[pos].replaceAll("[^\\d]", ""); // Clean quantity here too

                cartRef.orderByChild("name").equalTo(productName)
                        .get()
                        .addOnSuccessListener(dataSnapshot -> {
                            if (dataSnapshot.exists()) {
                                // Product already in cart: update quantity
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    CartItem existingItem = snapshot.getValue(CartItem.class);
                                    if (existingItem != null) {
                                        int existingQty = Integer.parseInt(existingItem.getQuantity());
                                        int newQty = existingQty + Integer.parseInt(productQuantity);

                                        snapshot.getRef().child("quantity").setValue(String.valueOf(newQty))
                                                .addOnSuccessListener(aVoid -> Toast.makeText(v.getContext(), "Quantity updated in cart", Toast.LENGTH_SHORT).show())
                                                .addOnFailureListener(e -> Toast.makeText(v.getContext(), "Failed to update cart", Toast.LENGTH_SHORT).show());
                                    }
                                }
                            } else {
                                // Product not in cart: add new item
                                String cartItemId = cartRef.push().getKey();
                                if (cartItemId != null) {
                                    // Clean price here as well
                                    String cleanPrice = price[pos].replaceAll("[^\\d.]", "");

                                    CartItem newCartItem = new CartItem(cartItemId, productName, cleanPrice, productQuantity, image[pos]);
                                    cartRef.child(cartItemId).setValue(newCartItem)
                                            .addOnSuccessListener(aVoid -> Toast.makeText(v.getContext(), "Added to your cart", Toast.LENGTH_SHORT).show())
                                            .addOnFailureListener(e -> Toast.makeText(v.getContext(), "Failed to add to cart", Toast.LENGTH_SHORT).show());
                                }
                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(v.getContext(), "Error accessing cart", Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(v.getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            }
        });

        holder.plusButton.setOnClickListener(v -> {
            int currentQty = Integer.parseInt(quantity[pos].replaceAll("[^\\d]", ""));
            currentQty++;
            quantity[pos] = String.valueOf(currentQty);
            holder.quantityTextView.setText(quantity[pos]);
        });

        holder.minusButton.setOnClickListener(v -> {
            int currentQty = Integer.parseInt(quantity[pos].replaceAll("[^\\d]", ""));
            if (currentQty > 0) {
                currentQty--;
                quantity[pos] = String.valueOf(currentQty);
                holder.quantityTextView.setText(quantity[pos]);
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
        Button addToCartButton, plusButton, minusButton, buyNowBtn;

        public ViewHolder(View view) {
            super(view);
            imageImageView = view.findViewById(R.id.productImage);
            nameTextView = view.findViewById(R.id.productName);
            priceTextView = view.findViewById(R.id.productPrize);
            quantityTextView = view.findViewById(R.id.productQuantity);
            addToCartButton = view.findViewById(R.id.addToCartButton);
            buyNowBtn = view.findViewById(R.id.buyNowBtn);
            plusButton = view.findViewById(R.id.plusButton);
            minusButton = view.findViewById(R.id.minusButton);
        }
    }
}
