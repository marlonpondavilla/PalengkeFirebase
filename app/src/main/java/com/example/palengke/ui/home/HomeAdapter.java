package com.example.palengke.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.palengke.R;

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

        public ViewHolder(View view) {
            super(view);
            imageImageView = view.findViewById(R.id.productImage);
            nameTextView = view.findViewById(R.id.productName);
            priceTextView = view.findViewById(R.id.productPrize);
            quantityTextView = view.findViewById(R.id.productQuantity);
        }
    }
}
