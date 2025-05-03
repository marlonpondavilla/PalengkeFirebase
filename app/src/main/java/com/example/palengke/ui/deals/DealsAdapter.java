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
        Button addToCartButton;

        public DealsViewHolder(View itemView) {
            super(itemView);
            productTitle = itemView.findViewById(R.id.product_title);
            productPrice = itemView.findViewById(R.id.product_price);
            productImage = itemView.findViewById(R.id.product_image);
            productQuantity = itemView.findViewById(R.id.quantity);
            addToCartButton = itemView.findViewById(R.id.buttonAddToCart);
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

        holder.addToCartButton.setOnClickListener(v -> {
            Map<String, Object> cartItem = new HashMap<>();
            cartItem.put("name", productTitles[position]);
            cartItem.put("price", productPrices[position]);
            cartItem.put("quantity", productQuantity[position]);
            cartItem.put("imageResId", productImages[position]);  // Storing drawable ID

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userId = user.getUid();
                FirebaseDatabase.getInstance()
                        .getReference("cart")
                        .child(userId)
                        .push()
                        .setValue(cartItem)
                        .addOnSuccessListener(aVoid ->
                                Toast.makeText(v.getContext(), "Added to your cart", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e ->
                                Toast.makeText(v.getContext(), "Failed to add to cart", Toast.LENGTH_SHORT).show());
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
