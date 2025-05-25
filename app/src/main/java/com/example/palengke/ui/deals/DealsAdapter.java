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
        Button addToCartButton, plusButton, minusButton, buttonBuyNow;

        public DealsViewHolder(View itemView) {
            super(itemView);
            productTitle = itemView.findViewById(R.id.product_title);
            productPrice = itemView.findViewById(R.id.product_price);
            productImage = itemView.findViewById(R.id.product_image);
            productQuantity = itemView.findViewById(R.id.quantity);
            addToCartButton = itemView.findViewById(R.id.buttonAddToCart);
            plusButton = itemView.findViewById(R.id.button_increase);
            minusButton = itemView.findViewById(R.id.button_decrease);
            buttonBuyNow = itemView.findViewById(R.id.button_buy_now);
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
        final int pos = position; // fix position for async calls

        holder.productTitle.setText(productTitles[pos]);
        holder.productPrice.setText(productPrices[pos]);
        holder.productImage.setImageResource(productImages[pos]);
        holder.productQuantity.setText(productQuantity[pos]);

        holder.plusButton.setOnClickListener(v -> {
            int qty = Integer.parseInt(productQuantity[pos].replaceAll("[^\\d]", ""));
            qty++;
            productQuantity[pos] = String.valueOf(qty);
            holder.productQuantity.setText(productQuantity[pos]);
        });

        holder.minusButton.setOnClickListener(v -> {
            int qty = Integer.parseInt(productQuantity[pos].replaceAll("[^\\d]", ""));
            if (qty > 0) {
                qty--;
                productQuantity[pos] = String.valueOf(qty);
                holder.productQuantity.setText(productQuantity[pos]);
            }
        });

        holder.buttonBuyNow.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(v.getContext())
                    .setTitle("Buy Now")
                    .setMessage("Are you sure you want to buy \"" + productTitles[pos] + "\"?")
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

                                String cleanQuantity = productQuantity[pos].replaceAll("[^\\d]", "");
                                String cleanPrice = productPrices[pos].replaceAll("[^\\d.]", "");

                                Map<String, Object> productData = new HashMap<>();
                                productData.put("id", productId);
                                productData.put("name", productTitles[pos]);
                                productData.put("price", cleanPrice);
                                productData.put("quantity", cleanQuantity);
                                productData.put("image", productImages[pos]);

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

        holder.addToCartButton.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userId = user.getUid();
                DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("cart").child(userId);
                String productName = productTitles[pos];
                String cleanQuantity = productQuantity[pos].replaceAll("[^\\d]", "");
                String cleanPrice = productPrices[pos].replaceAll("[^\\d.]", "");

                cartRef.orderByChild("name").equalTo(productName)
                        .get()
                        .addOnSuccessListener(dataSnapshot -> {
                            if (dataSnapshot.exists()) {
                                // Product exists: update quantity
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String existingQtyStr = snapshot.child("quantity").getValue(String.class);
                                    int existingQty = existingQtyStr != null ? Integer.parseInt(existingQtyStr) : 0;
                                    int newQty = existingQty + Integer.parseInt(cleanQuantity);

                                    snapshot.getRef().child("quantity").setValue(String.valueOf(newQty))
                                            .addOnSuccessListener(aVoid -> Toast.makeText(v.getContext(), "Cart updated", Toast.LENGTH_SHORT).show())
                                            .addOnFailureListener(e -> Toast.makeText(v.getContext(), "Failed to update cart", Toast.LENGTH_SHORT).show());
                                }
                            } else {
                                // Product doesn't exist: add new
                                String cartItemId = cartRef.push().getKey();
                                if (cartItemId != null) {
                                    Map<String, Object> cartItem = new HashMap<>();
                                    cartItem.put("id", cartItemId);
                                    cartItem.put("name", productName);
                                    cartItem.put("price", cleanPrice);
                                    cartItem.put("quantity", cleanQuantity);
                                    cartItem.put("imageResId", productImages[pos]);

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
