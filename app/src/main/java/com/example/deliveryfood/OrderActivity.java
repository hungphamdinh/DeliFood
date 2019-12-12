package com.example.deliveryfood;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.deliveryfood.Common.Common;
import com.example.deliveryfood.Interface.ItemClickListener;
import com.example.deliveryfood.Model.Request;
import com.example.deliveryfood.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.deliveryfood.Common.Common.changeCodeToStatus;

public class OrderActivity extends AppCompatActivity {
    private RecyclerView recyclerMenu;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference order;
    private FirebaseDatabase database;
    private FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        database= FirebaseDatabase.getInstance();
        order=database.getReference("Requests");
        recyclerMenu=(RecyclerView)findViewById(R.id.listOrderRecycler);
        recyclerMenu.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerMenu.setLayoutManager(layoutManager);
        if(getIntent()==null) {
            loadOrders(Common.currentUser.getPhone());
        }
        else
            loadOrders(getIntent().getStringExtra("userPhone"));
    }
    private void loadOrders(String phone) {
        adapter=new FirebaseRecyclerAdapter<Request, OrderViewHolder>
                (Request.class,R.layout.order_layout,
                        OrderViewHolder.class,
                        order.orderByChild("phone").equalTo(phone)) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {
                viewHolder.txtId.setText(adapter.getRef(position).getKey());
                viewHolder.txtAddress.setText(model.getAddress());
     //           String a=changeCodeToStatus(model.getStatus());
                viewHolder.txtStatus.setText(changeCodeToStatus(model.getStatus()));
                viewHolder.txtPhone.setText(model.getPhone());
                viewHolder.setItemClickListener(new ItemClickListener()
                {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }
        };
        recyclerMenu.setAdapter(adapter);
    }


}
