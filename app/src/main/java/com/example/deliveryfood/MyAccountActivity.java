package com.example.deliveryfood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MyAccountActivity extends AppCompatActivity {
    private EditText edtUserNameAc,edtPhoneAc,edtEmailAc;
    private FirebaseDatabase databaseAc;
    private DatabaseReference userAc;
    private String phoneNumAC;
    private Button btnAddEmailAc;
    private User userAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        edtUserNameAc=(EditText)findViewById(R.id.edtUsernameMyAc);
        edtPhoneAc=(EditText)findViewById(R.id.edtPhoneMyAc);
        edtEmailAc=(EditText)findViewById(R.id.edtEmailAc);
        btnAddEmailAc=(Button)findViewById(R.id.btnAddEmail);
        databaseAc=FirebaseDatabase.getInstance();
        userAc=databaseAc.getReference("User");
        if(getIntent()!=null)
            phoneNumAC=getIntent().getStringExtra("phoneNum");
        if(!phoneNumAC.isEmpty()&&phoneNumAC!=null){
                getDataFromAc(phoneNumAC);
        }
    }

    private void getDataFromAc(final String phoneNumAC) {
        final DatabaseReference userArcChild=userAc.child(phoneNumAC);
        final DatabaseReference userArcSecondChild=userArcChild.child("email");
        userAc.child(phoneNumAC).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                userAccount=dataSnapshot.getValue(User.class);
                //User userAccountEmail=dataSnapshot.getValue(User.class);
                //edtEmailAc.setText(userAccountEmail.getEmail());
                edtUserNameAc.setText(userAccount.getUsername());
                edtPhoneAc.setText(phoneNumAC);
                //edtEmailAc.setText(dataSnapshot.getValue(User.class).getEmail());
                updateEmail(phoneNumAC);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void updateEmail(final String phoneNumAC) {
        userAc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                final String emailAc=edtEmailAc.getText().toString();
          //      final User userAccountEmail=dataSnapshot.getValue(User.class);
          //      edtEmailAc.setText(""+userAc.child("email").getKey());
                    btnAddEmailAc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //if(dataSnapshot.child("email").exists()){
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                if (!data.getValue(User.class).getEmail().equals(emailAc)) {
                                    Toast.makeText(MyAccountActivity.this, "This email was registed by another account, Please fill new Email", Toast.LENGTH_LONG).show();
                                    btnAddEmailAc.setEnabled(true);
                                } else {
                                    userAccount = new User(edtUserNameAc.getText().toString(), userAccount.getPassword(), edtEmailAc.getText().toString());
                                    userAc.child(phoneNumAC).setValue(userAccount);
                                    btnAddEmailAc.setEnabled(false);
                                }
                            }

                        }
                    });


//                    edtEmailAc.setEnabled(false);
//                    btnAddEmailAc.setEnabled(false);
//                    edtEmailAc.setEnabled(false);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
