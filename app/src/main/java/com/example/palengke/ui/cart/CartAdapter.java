package com.example.palengke.ui.cart;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

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
        holder.priceTextView.setText(cartItem.getPrice());
        holder.quantityTextView.setText(cartItem.getQuantity());

        holder.removeButton.setOnClickListener(v -> {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String cartItemId = cartItem.getId();

            if (cartItemId != null) {
                FirebaseDatabase.getInstance()
                        .getReference("cart")
                        .child(userId)
                        .child(cartItemId)
                        .removeValue()
                        .addOnSuccessListener(aVoid -> {
                            // Only modify the UI after the item has been successfully removed from Firebase
                            Toast.makeText(v.getContext(), "Item removed from cart", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(v.getContext(), "Failed to remove item", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(v.getContext(), "No valid item ID found", Toast.LENGTH_SHORT).show();
            }
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
        Button removeButton;

        public ViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.productName);
            priceTextView = view.findViewById(R.id.productPrize);
            quantityTextView = view.findViewById(R.id.productQuantity);
            removeButton = view.findViewById(R.id.removeButton);
        }
    }
}
