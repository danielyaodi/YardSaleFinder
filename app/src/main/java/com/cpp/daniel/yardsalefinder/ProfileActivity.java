package com.cpp.daniel.yardsalefinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.daniel.yardsalefinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    public DatabaseReference getUserInfo;
    public String uID;
    public ListView userInfoListView;
    public Button sellerButton;
    public Button buyerButton;
    public Button logoutButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userInfoListView = (ListView) findViewById(R.id.userInfoListView) ;
        sellerButton = (Button)findViewById(R.id.sellerButton);
        buyerButton = (Button)findViewById(R.id.buyerButton);
        logoutButton =(Button)findViewById(R.id.logoutButton);
        getUserInfo =  FirebaseDatabase.getInstance().getReference();
        uID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        sellerButton.setOnClickListener(this);
        buyerButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);


        getUserInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userInfo(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void userInfo(DataSnapshot dataSnapshot) {
        UserInformation userInfo = new UserInformation();
        ArrayList<String> userInfoArray = new ArrayList<>();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Log.i("ProActivity",ds.toString());

            userInfo.setEmail(ds.child(uID).getValue(UserInformation.class).getEmail());
            userInfo.setTelephone(ds.child(uID).getValue(UserInformation.class).getTelephone());
            userInfo.setUserName(ds.child(uID).getValue(UserInformation.class).getUserName());
            Log.i("ProActivity",userInfo.toString());
            userInfoArray.add("User Account:");
            userInfoArray.add("Username:     "+userInfo.getUserName());
            userInfoArray.add("Email:          "+userInfo.getEmail());
            userInfoArray.add("Telephone:    "+userInfo.getTelephone());
            if(!ds.child(uID).child("fullAddress").exists()){
                userInfoArray.add("You posted Sale Info");
            }
            ArrayAdapter adapter = new ArrayAdapter(ProfileActivity.this,android.R.layout.simple_list_item_1,userInfoArray);
            userInfoListView.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == sellerButton) {
            Intent intent = new Intent();
            intent.setClass(ProfileActivity.this, postYardSaleInfoActivity.class);
            startActivity(intent);

        }
        if (v == buyerButton) {
            Intent intent = new Intent();
            intent.setClass(ProfileActivity.this, showMapActivity.class);
            startActivity(intent);

        }
        if (v== logoutButton){
            finish();
        }
    }


}

