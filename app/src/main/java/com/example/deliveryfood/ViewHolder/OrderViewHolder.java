package com.example.deliveryfood.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.deliveryfood.Interface.ItemClickListener;
import com.example.deliveryfood.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtId,txtPhone,txtStatus,txtAddress;
    private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);
        txtId=(TextView)itemView.findViewById(R.id.txtOrderId);
        txtStatus=(TextView)itemView.findViewById(R.id.txtOrderStatus);
        txtPhone=(TextView)itemView.findViewById(R.id.txtOrderPhone);
        txtAddress=(TextView)itemView.findViewById(R.id.txtOrderAddress);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }


}
