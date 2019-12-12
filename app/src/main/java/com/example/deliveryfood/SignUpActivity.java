package com.example.deliveryfood;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.deliveryfood.Common.Common;
import com.example.deliveryfood.Model.User;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {
    private EditText edtphone,password,edtUsername;
    private Button btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtUsername= findViewById(R.id.edtUsernameSignUp);
        edtphone=findViewById(R.id.edtPhone);
        password= findViewById(R.id.edtPasswordSignUp);
        btnSignUp= findViewById(R.id.btnSignUpSignIn);
        setupUI(findViewById(R.id.parentSignUp));
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=firebaseDatabase.getReference("User");
        Firebase.setAndroidContext(SignUpActivity.this);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        SignUp(table_user);


    }

    public void SignUp(final DatabaseReference table_user) {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                if(Common.isConnectedToInternet(getBaseContext())) {
                    final ProgressDialog progress = new ProgressDialog(SignUpActivity.this);
                    progress.setTitle("Loading");
                    progress.setMessage("Wait while loading...");
                    progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                    progress.show();
                    final String usernameTemp = edtUsername.getText().toString();
                    final String passwordTemp = password.getText().toString();
                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (usernameTemp.equals("") || passwordTemp.equals("") || edtphone.getText().toString().equals("")) {
                                progress.dismiss();
                                Toast.makeText(SignUpActivity.this, "Please check your username. phone and password", Toast.LENGTH_SHORT).show();
                            } else {
                                if (dataSnapshot.child(edtphone.getText().toString()).exists()) {
                                    progress.dismiss();
                                    Toast.makeText(SignUpActivity.this, "This phone number is exist", Toast.LENGTH_SHORT).show();
                                } else {
                                    progress.dismiss();
                                    User user = new User(usernameTemp, passwordTemp,"");
                                    table_user.child(edtphone.getText().toString()).setValue(user);
                                    Toast.makeText(SignUpActivity.this, "Sign up successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    Toast.makeText(SignUpActivity.this,"Check your connection",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(SignUpActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
}
