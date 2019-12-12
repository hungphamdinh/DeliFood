package com.example.deliveryfood.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.deliveryfood.Interface.ItemClickListener;
import com.example.deliveryfood.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView foodName;
    public ImageView foodImage;
    private ItemClickListener itemClickListener;
    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);
        foodName=(TextView)itemView.findViewById(R.id.txtFoodName);
        foodImage=(ImageView)itemView.findViewById(R.id.foodImage);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }
    @Override
    public void onClick(View v)
    {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }

}
