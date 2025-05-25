package com.example.palengke;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.palengke.classes.Order;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private List<Order> ordersList;

    public OrdersAdapter(List<Order> ordersList) {
        this.ordersList = ordersList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_card, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = ordersList.get(position);

        holder.orderId.setText("Order ID: " + order.getId());
        holder.orderDate.setText("Date: " + order.getDate());
        holder.orderProducts.setText("Product: " + order.getProducts());
        holder.orderQuantity.setText("Quantity: " + order.getQuantity());

        double unitPrice = 0;
        int quantity = 1;
        try {
            unitPrice = Double.parseDouble(order.getTotalPrice());
        } catch (NumberFormatException e) {
            unitPrice = 0;
        }
        try {
            quantity = Integer.parseInt(order.getQuantity());
        } catch (NumberFormatException e) {
            quantity = 1;
        }

        double totalPrice = unitPrice * quantity;

        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
        String formattedUnitPrice = format.format(unitPrice);
        String formattedTotalPrice = format.format(totalPrice);

        holder.orderUnitPrice.setText("Unit Price: " + formattedUnitPrice);
        holder.orderTotalPrice.setText("Total Price: " + formattedTotalPrice);
        holder.orderStatus.setText("Status: " + order.getStatus());
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, orderDate, orderProducts, orderQuantity, orderUnitPrice, orderTotalPrice, orderStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            orderId = itemView.findViewById(R.id.textViewOrderId);
            orderDate = itemView.findViewById(R.id.textViewDate);
            orderProducts = itemView.findViewById(R.id.textViewProducts);
            orderQuantity = itemView.findViewById(R.id.textViewQuantity);
            orderUnitPrice = itemView.findViewById(R.id.textViewUnitPrice);   // NEW
            orderTotalPrice = itemView.findViewById(R.id.textViewTotalPrice);
            orderStatus = itemView.findViewById(R.id.textViewStatus);
        }
    }
}
