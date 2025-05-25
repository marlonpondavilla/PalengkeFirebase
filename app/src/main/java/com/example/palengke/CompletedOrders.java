package com.example.palengke;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.palengke.classes.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CompletedOrders extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrdersAdapter ordersAdapter;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_orders);

        recyclerView = findViewById(R.id.ordersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderList = new ArrayList<>();
        ordersAdapter = new OrdersAdapter(orderList);
        recyclerView.setAdapter(ordersAdapter);

        fetchOrdersFromFirebase();

        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }

    private void fetchOrdersFromFirebase() {
        String userId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : null;

        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference ordersRef = FirebaseDatabase.getInstance()
                .getReference("BuyProducts")
                .child(userId);

        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                orderList.clear();

                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    String id = orderSnapshot.child("id").getValue(String.class);
                    String date = orderSnapshot.child("date").getValue(String.class);
                    String products = orderSnapshot.child("name").getValue(String.class);
                    String quantity = orderSnapshot.child("quantity").getValue(String.class);
                    String totalPrice = orderSnapshot.child("price").getValue(String.class);
                    String status = orderSnapshot.child("status").getValue(String.class);

                    if (date == null) date = "N/A";
                    if (quantity == null) quantity = "1";
                    if (status == null) status = "Completed";

                    Order order = new Order(id, date, products, quantity, totalPrice, status);
                    orderList.add(order);
                }

                ordersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(CompletedOrders.this, "Failed to load orders", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
