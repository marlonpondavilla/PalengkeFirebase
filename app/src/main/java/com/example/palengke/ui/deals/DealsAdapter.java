package com.example.palengke.ui.deals;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.DealsViewHolder> {

    private String[] productTitles;
    private String[] productPrices;
    private Integer[] productImages;
    private String[] productQuantity;

    public static class DealsViewHolder extends RecyclerView.ViewHolder {
        TextView productTitle, productPrice, productQuantity;
        ImageView productImage;
        Button addToCartButton, plusButton, minusButton;

        public DealsViewHolder(View itemView) {
            super(itemView);
            productTitle = itemView.findViewById(R.id.product_title);
            productPrice = itemView.findViewById(R.id.product_price);
            productImage = itemView.findViewById(R.id.product_image);
            productQuantity = itemView.findViewById(R.id.quantity);
            addToCartButton = itemView.findViewById(R.id.buttonAddToCart);
            plusButton = itemView.findViewById(R.id.button_increase);
            minusButton = itemView.findViewById(R.id.button_decrease);
        }
    }


    @NonNull
    @Override
    public DealsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deals, parent, false);
        return new DealsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DealsViewHolder holder, int position) {
        holder.productTitle.setText(productTitles[position]);
        holder.productPrice.setText(productPrices[position]);
        holder.productImage.setImageResource(productImages[position]);
        holder.productQuantity.setText(productQuantity[position]);

        holder.plusButton.setOnClickListener(v -> {
            int qty = Integer.parseInt(productQuantity[position]);
            qty++;
            productQuantity[position] = String.valueOf(qty);
            holder.productQuantity.setText(productQuantity[position]);
        });

        holder.minusButton.setOnClickListener(v -> {
            int qty = Integer.parseInt(productQuantity[position]);
            if (qty > 0) {
                qty--;
                productQuantity[position] = String.valueOf(qty);
                holder.productQuantity.setText(productQuantity[position]);
            }
        });

        holder.addToCartButton.setOnClickListener(v -> {
            Map<String, Object> cartItem = new HashMap<>();
            cartItem.put("name", productTitles[position]);
            cartItem.put("price", productPrices[position]);
            cartItem.put("quantity", productQuantity[position]);
            cartItem.put("imageResId", productImages[position]);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userId = user.getUid();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference cartRef = database.getReference("cart").child(userId);
                String productName = productTitles[position];

                cartRef.orderByChild("name").equalTo(productName)
                        .get()
                        .addOnSuccessListener(dataSnapshot -> {
                            if (dataSnapshot.exists()) {
                                // Product exists: update quantity
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String existingQtyStr = snapshot.child("quantity").getValue(String.class);
                                    int existingQty = existingQtyStr != null ? Integer.parseInt(existingQtyStr) : 0;
                                    int newQty = existingQty + Integer.parseInt(productQuantity[position]);

                                    snapshot.getRef().child("quantity").setValue(String.valueOf(newQty))
                                            .addOnSuccessListener(aVoid -> Toast.makeText(v.getContext(), "Cart updated", Toast.LENGTH_SHORT).show())
                                            .addOnFailureListener(e -> Toast.makeText(v.getContext(), "Failed to update cart", Toast.LENGTH_SHORT).show());
                                }
                            } else {
                                // Product doesn't exist: add new
                                String cartItemId = cartRef.push().getKey();
                                if (cartItemId != null) {
                                    cartItem.put("id", cartItemId);
                                    cartRef.child(cartItemId).setValue(cartItem)
                                            .addOnSuccessListener(aVoid -> Toast.makeText(v.getContext(), "Added to your cart", Toast.LENGTH_SHORT).show())
                                            .addOnFailureListener(e -> Toast.makeText(v.getContext(), "Failed to add to cart", Toast.LENGTH_SHORT).show());
                                }
                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(v.getContext(), "Error checking cart", Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(v.getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return productTitles != null ? productTitles.length : 0;
    }

    public void setProductData(String[] titles, String[] prices, Integer[] images, String[] quantity) {
        this.productTitles = titles;
        this.productPrices = prices;
        this.productImages = images;
        this.productQuantity = quantity;
        notifyDataSetChanged();
    }
}
