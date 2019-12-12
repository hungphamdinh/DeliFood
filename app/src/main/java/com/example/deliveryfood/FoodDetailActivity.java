package com.example.deliveryfood;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.deliveryfood.Common.Common;
import com.example.deliveryfood.Model.Food;
import com.example.deliveryfood.Model.Order;
import com.example.deliveryfood.SQliteDatabase.BaseResipistory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetailActivity extends AppCompatActivity {
    private DatabaseReference food;
    private FirebaseDatabase database;
    private FloatingActionButton btnCart;
    private ElegantNumberButton numberButton;
    private TextView foodDescription,foodName,foodPrice;
    private ImageView foodImage;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private String foodId;
    private BaseResipistory baseResipistory;
    private Food curentFood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        database=FirebaseDatabase.getInstance();
        food=database.getReference("Foods");
        numberButton=(ElegantNumberButton)findViewById(R.id.btnNumber);
        btnCart=(FloatingActionButton)findViewById(R.id.btnCart);
        foodDescription=(TextView)findViewById(R.id.txtFoodDescriptionDetail);
        foodName=(TextView)findViewById(R.id.txtFoodNameDetail);
        foodPrice=(TextView)findViewById(R.id.txtFoodPriceDetail);
        foodImage=(ImageView)findViewById(R.id.imgFoodDetail);
        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);
        baseResipistory=new BaseResipistory(this);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //baseResipistory=new BaseResipistory(FoodDetailActivity.this);
                baseResipistory.insert(new Order(foodId,
                        curentFood.getName(),
                        numberButton.getNumber(),
                        curentFood.getPrice(),
                        curentFood.getDiscount()));
                Toast.makeText(FoodDetailActivity.this,"Add to cart successed",Toast.LENGTH_SHORT).show();
            }
        });

        if(getIntent()!=null)
            foodId=getIntent().getStringExtra("foodId");
        if(!foodId.isEmpty()&&foodId!=null){
            if(Common.isConnectedToInternet(this)) {
                getDetailFood(foodId);
            }
            else {
                Toast.makeText(FoodDetailActivity.this,"Check your connection",Toast.LENGTH_SHORT).show();
                return;
            }
        }

    }

    private void getDetailFood(String foodId) {
        food.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                curentFood=dataSnapshot.getValue(Food.class);
                Picasso.with(getBaseContext()).load(curentFood.getImage()).into(foodImage);
                collapsingToolbarLayout.setTitle(curentFood.getName());
                foodDescription.setText(curentFood.getDescription());
                foodPrice.setText(curentFood.getPrice());
                foodName.setText(curentFood.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
