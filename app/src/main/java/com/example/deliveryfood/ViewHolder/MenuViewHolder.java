package com.example.deliveryfood.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.deliveryfood.Common.Common;
import com.example.deliveryfood.Interface.ItemClickListener;
import com.example.deliveryfood.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {

    public TextView txtMenuName;
    public ImageView imageView;

    private ItemClickListener itemClickListener;
    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);
        txtMenuName=(TextView)itemView.findViewById(R.id.txtMenuNameRecycle);
        imageView=(ImageView)itemView.findViewById(R.id.menuImageRecycle);
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
    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select this action");
        contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
        contextMenu.add(0,1,getAdapterPosition(), Common.DELETE);
    }
}
