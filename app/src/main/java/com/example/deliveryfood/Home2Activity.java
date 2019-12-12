package com.example.deliveryfood;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deliveryfood.Common.Common;
import com.example.deliveryfood.Interface.ItemClickListener;
import com.example.deliveryfood.Model.Category;
import com.example.deliveryfood.Service.ListenOrder;
import com.example.deliveryfood.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class Home2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseDatabase database;
    private DatabaseReference category;
    private TextView txtFullName;
    private RecyclerView recyclerMenu;
    private FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);
        database=FirebaseDatabase.getInstance();
        category=database.getReference("Category");
        Paper.init(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home2Activity.this,CartActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //txtFullName=(TextView)headerView.findViewById(R.id.txtFullName);
        //txtFullName.setText(Common.currentUser.getUsername());
        //Load menu
        recyclerMenu=(RecyclerView) findViewById(R.id.recycle_menu);
        recyclerMenu.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerMenu.setLayoutManager(layoutManager);
        if(Common.isConnectedToInternet(this)) {
            loadMenu();
        }
        else {
            Toast.makeText(Home2Activity.this,"Check your connection",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent service=new Intent(Home2Activity.this, ListenOrder.class);
        startService(service);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void loadMenu() {
        adapter=new FirebaseRecyclerAdapter<Category, MenuViewHolder>
                (Category.class,R.layout.menu_item,MenuViewHolder.class,category) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
                viewHolder.txtMenuName.setText(""+model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.imageView);
                final Category clickItem=model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Toast.makeText(HomeActivity.this,""+clickItem.getName(),Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Home2Activity.this,FoodList.class);
                        intent.putExtra("categoryId",adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerMenu.setAdapter(adapter);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh) {
            loadMenu();
        }
        else if (id == R.id.accountInform) {
            startActivity(new Intent(Home2Activity.this,MyAccountActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //OrderFragment orderFragment=new OrderFragment(this);
        if (id == R.id.nav_menu) {
            // Handle the camera action
            //startActivity(new Intent(Home2Activity.this,Home2Activity.class));
        }

        else if (id == R.id.nav_cart)
        {
            startActivity(new Intent(Home2Activity.this,CartActivity.class));
        }
        else if (id == R.id.nav_order) {
            //OrderFragment orderFragment=new Fragment(HomeActivity.this);
            //setFragment(orderFragment);
            startActivity(new Intent(Home2Activity.this,OrderActivity.class));

        } else if (id == R.id.nav_signout) {
            Paper.book().destroy();
            Intent signIn=new Intent(Home2Activity.this,LoginActivity.class);
            signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signIn);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }

}
