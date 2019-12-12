package com.example.deliveryfood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.deliveryfood.Common.Common;
import com.example.deliveryfood.Interface.ItemClickListener;
import com.example.deliveryfood.Model.Category;
import com.example.deliveryfood.Model.Food;
import com.example.deliveryfood.ViewHolder.FoodViewHolder;
import com.example.deliveryfood.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class FoodList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase database;
    private DatabaseReference foodList;
    private FirebaseRecyclerAdapter<Food,FoodViewHolder>adapter;
    private String categoryId="";
    private RelativeLayout rootLayout;
    private FirebaseRecyclerAdapter<Food,FoodViewHolder> searchAdapter;
    List<String>suggestList=new ArrayList<>();
    MaterialSearchBar materialSearchBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        rootLayout=(RelativeLayout)findViewById(R.id.rootLayout);
        database=FirebaseDatabase.getInstance();
        foodList=database.getReference("Foods");
        recyclerView=(RecyclerView)findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(getIntent()!=null)
            categoryId=getIntent().getStringExtra("categoryId");//category ID is ID of item in RecycleView
        if(!categoryId.isEmpty()&&categoryId!=null){
            if (Common.isConnectedToInternet(this)) {
                loadListFood(categoryId);
            }
            else {
                Toast.makeText(FoodList.this,"Check your connection",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        materialSearchBar=(MaterialSearchBar)findViewById(R.id.search_bar);
        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ArrayList<String>suggest=new ArrayList<String>();
                for(String search:suggestList ){
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())) {
                        suggest.add(search);
                    }
                }
                materialSearchBar.setLastSuggestions(suggest);//set suggest
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //When searchbar is close, becoming original adapter
                if(!enabled)
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //When search is finish, show result
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearch(CharSequence text) {
        searchAdapter=new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("name").equalTo(text.toString())//compare by itemName
        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {
                viewHolder.foodName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.foodImage);
                final Food local=model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent=new Intent(FoodList.this,FoodDetailActivity.class);
                        intent.putExtra("foodId",searchAdapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(searchAdapter);//Set adapter for RecycleView when search Result
    }

    private void loadSuggest() {
        foodList.orderByChild("menuId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot:dataSnapshot.getChildren() ){
                            Food foodItem=postSnapshot.getValue(Food.class);
                            suggestList.add(foodItem.getName());//load list of suggest item

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void loadListFood(String categoryId) {
        adapter=new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("menuId").equalTo(categoryId)) {//like Select * From Food where MenuId=?
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, final Food model, int position) {
                viewHolder.foodName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.foodImage);
                final Food local=model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent=new Intent(FoodList.this,FoodDetailActivity.class);
                        intent.putExtra("foodId",adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }
}
