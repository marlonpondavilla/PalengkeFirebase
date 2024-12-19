package com.example.palengke.ui.deals;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.palengke.R;

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.DealsViewHolder> {

    private String[] productTitles;
    private String[] productPrices;
    private Integer[] productImages;
    private String[] productQuantity;

    public static class DealsViewHolder extends RecyclerView.ViewHolder {
        TextView productTitle, productPrice, productQuantity;
        ImageView productImage;

        public DealsViewHolder(View itemView) {
            super(itemView);
            productTitle = itemView.findViewById(R.id.product_title);
            productPrice = itemView.findViewById(R.id.product_price);
            productImage = itemView.findViewById(R.id.product_image);
            productQuantity = itemView.findViewById(R.id.quantity);
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