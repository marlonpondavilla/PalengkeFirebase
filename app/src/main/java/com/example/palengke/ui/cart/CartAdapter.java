package com.example.palengke.ui.cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.palengke.R;
import com.example.palengke.classes.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<CartItem> cartItems = new ArrayList<>();
    private FirebaseAuth auth;
    private DatabaseReference cartRef;

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_card, parent, false);

        auth = FirebaseAuth.getInstance();
        cartRef = FirebaseDatabase.getInstance().getReference("cart");

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);

        holder.nameTextView.setText(cartItem.getName());
        holder.priceTextView.setText("₱" + cartItem.getPrice());
        holder.quantityTextView.setText(cartItem.getQuantity());

        // Remove button logic
        holder.removeButton.setOnClickListener(v -> {
            FirebaseUser user = auth.getCurrentUser();
            if (user == null) {
                Toast.makeText(v.getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
                return;
            }
            String userId = user.getUid();
            String cartItemId = cartItem.getId();

            if (cartItemId != null) {
                cartRef.child(userId).child(cartItemId)
                        .removeValue()
                        .addOnSuccessListener(aVoid ->
                                Toast.makeText(v.getContext(), "Item removed from cart", Toast.LENGTH_SHORT).show()
                        )
                        .addOnFailureListener(e ->
                                Toast.makeText(v.getContext(), "Failed to remove item", Toast.LENGTH_SHORT).show()
                        );
            } else {
                Toast.makeText(v.getContext(), "No valid item ID found", Toast.LENGTH_SHORT).show();
            }
        });

        // Checkout button logic (buy items)
        holder.checkoutButton.setOnClickListener(v -> {
            FirebaseUser user = auth.getCurrentUser();
            if (user == null) {
                Toast.makeText(v.getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
                return;
            }
            String userId = user.getUid();
            String productId = FirebaseDatabase.getInstance().getReference().push().getKey();

            if (productId == null) {
                Toast.makeText(v.getContext(), "Failed to generate product ID", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference buyRef = FirebaseDatabase.getInstance()
                    .getReference("BuyProducts")
                    .child(userId)
                    .child(productId);

            // Clean quantity and price strings (just numbers)
            String cleanQuantity = cartItem.getQuantity().replaceAll("[^\\d]", "");
            String cleanPrice = cartItem.getPrice().replaceAll("[^\\d.]", "");

            Map<String, Object> productData = new HashMap<>();
            productData.put("id", productId);
            productData.put("name", cartItem.getName());
            productData.put("price", cleanPrice);
            productData.put("quantity", cleanQuantity);
            productData.put("image", cartItem.getImageResId());

            buyRef.setValue(productData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(v.getContext(), "Product purchased successfully!", Toast.LENGTH_SHORT).show();

                        // Optional: Remove from cart after purchase
                        if (cartItem.getId() != null) {
                            cartRef.child(userId).child(cartItem.getId()).removeValue();
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(v.getContext(), "Failed to purchase product", Toast.LENGTH_SHORT).show()
                    );
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView priceTextView;
        TextView quantityTextView;
        Button removeButton, checkoutButton;

        public ViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.productName);
            priceTextView = view.findViewById(R.id.productPrize);
            quantityTextView = view.findViewById(R.id.productQuantity);
            removeButton = view.findViewById(R.id.removeButton);
            checkoutButton = view.findViewById(R.id.checkoutButton);
        }
    }
}
