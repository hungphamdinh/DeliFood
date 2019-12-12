package com.example.deliveryfood;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deliveryfood.Common.Common;
import com.example.deliveryfood.Model.Order;
import com.example.deliveryfood.Model.Request;
import com.example.deliveryfood.SQliteDatabase.BaseResipistory;
import com.example.deliveryfood.ViewHolder.CartAdapter;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    private DatabaseReference requests;
    private FirebaseDatabase database;
    private RecyclerView recyclerView;
    private TextView totalPrice;
    private Button btnPlaceOrder;
    private ArrayList<Order>cartList;
    private CartAdapter cartAdapter;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");
        recyclerView=(RecyclerView)findViewById(R.id.recyclerlistCart);
        totalPrice=(TextView)findViewById(R.id.txtTotalCart);
        btnPlaceOrder=(Button)findViewById(R.id.btnPlaceOrder);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        loadListFood();
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cartList.size()>0)
                    openDialog();
                else
                    Toast.makeText(CartActivity.this,"Your cart is empty",Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void openDialog() {
            LayoutInflater inflater=LayoutInflater.from(CartActivity.this);
            View subView=inflater.inflate(R.layout.alert_dialog_data,null);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CartActivity.this);
            alertDialog.setTitle("Save Data");
            alertDialog.setMessage("Enter your address");
            // final EditText inputValue = (EditText) subView.findViewById(R.id.edtValue);
            final EditText inputKey = (EditText) subView.findViewById(R.id.edtKey);
            alertDialog.setView(subView);
            alertDialog.create();
            alertDialog.setPositiveButton("Save Data", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String key=inputKey.getText().toString();
                    Request request=new Request(
                            key,cartList,
                            Common.currentUser.getUsername(),
                            Common.currentUser.getPhone(),
                            totalPrice.getText().toString()
                            );
               //     Firebase mRefchild=mRef.child(key);
               //     mRefchild.setValue(data);
                 requests.child(String.valueOf(System.currentTimeMillis()))
                         .setValue(request);
                 new BaseResipistory(getBaseContext()).cleanCart();
                    Toast.makeText(CartActivity.this,"Thanks for your oder",Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();

    }

    private void loadListFood() {
        cartList=new BaseResipistory(this).getinform();
        cartAdapter=new CartAdapter(cartList,this);
        recyclerView.setAdapter(cartAdapter);
        int total=0;
        for(Order order:cartList)
            total+=Integer.parseInt(order.getPrice())*Integer.parseInt(order.getQuantity());
        Locale locale=new Locale("en","US");
        NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);
        totalPrice.setText(fmt.format(total));
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE)){
           // showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else {
            deleteCart(item.getOrder());
        }
        return super.onContextItemSelected(item);

    }

    private void deleteCart(int position) {
        cartList.remove(position);
        new BaseResipistory(this).cleanCart();//Clean old data from Sqlite
        for(Order item:cartList){
            new BaseResipistory(this).insert(item);//add new Data from castList to Sqlite
        }
        loadListFood();//Refresh

    }

}
