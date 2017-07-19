package com.cpp.daniel.yardsalefinder;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.daniel.yardsalefinder.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

import static java.lang.Double.parseDouble;

public class showMapActivity extends FragmentActivity implements OnMapReadyCallback,Serializable{

    private GoogleMap mMap;
    private String uID;
    private DatabaseReference fireRef;
    private TextView zipEditText;
   private Button submitButton;
    private Button listViewButton;
    ArrayList<String> fullAddressArray = new ArrayList<>();
    DataSnapshot newDS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        fireRef = FirebaseDatabase.getInstance().getReference();
        zipEditText = (TextView)findViewById(R.id.zipEditText);
        submitButton = (Button)findViewById(R.id.submitButton);
        listViewButton = (Button)findViewById(R.id.listViewAddress);
        listViewButton.setOnClickListener(new SearchZipCode());
        submitButton.setOnClickListener(new SearchZipCode());
        fireRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                grabUserObj(dataSnapshot);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
    private void grabUserObj(DataSnapshot dataSnapshot) {
        newDS = dataSnapshot;
        for(DataSnapshot ds : dataSnapshot.getChildren()){
//            currentZipCode = ds.child(uID).child("zipCode").getValue().toString();

            for(DataSnapshot ds2 : ds.getChildren()){
                for(DataSnapshot ds3 : ds2.getChildren()){
                    Log.i("***********","1");
                    if(ds3.getValue().equals(ds.child(uID).child("zipCode").getValue())){
                         double lat = parseDouble(ds2.child("latitude").getValue().toString());
                         double lon = parseDouble(ds2.child("longitude").getValue().toString());
                        String userName = ds2.child("userName").getValue().toString();
                        LatLng latLng = new LatLng(lat,lon);
                        addMarker(latLng,userName);
                        fullAddressArray.add("seller:"+userName+"  "+"address:"+ds2.child("fullAddress").getValue().toString());
                    }
                }
            }
        }
    }

    private void addMarker(LatLng latLng,String userName) {
        mMap.addMarker(new MarkerOptions().position(latLng)
                .title(userName));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
    class SearchZipCode implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(v==submitButton){

                String newZip = zipEditText.getText().toString();
                mMap.clear();
                fullAddressArray.clear();
                newMarker(newZip);
            }
            if(v==listViewButton){
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(showMapActivity.this,android.R.layout.simple_list_item_1,fullAddressArray);
                Intent intent = new Intent();
                intent.setClass(showMapActivity.this,ListAddressActivity.class);
                intent.putExtra("addressAdapter", fullAddressArray);
                startActivity(intent);
            }
        }
    }

    private void newMarker(String newZip) {
        for(DataSnapshot ds: newDS.getChildren()){
            for(DataSnapshot ds2 : ds.getChildren()){
                for(DataSnapshot ds3 : ds2.getChildren()){
                    if(ds3.getValue().equals(newZip)){
                        double lat = parseDouble(ds2.child("latitude").getValue().toString());
                        double lon = parseDouble(ds2.child("longitude").getValue().toString());
                        String userName = ds2.child("userName").getValue().toString();
                        LatLng latLng = new LatLng(lat,lon);
                        addMarker(latLng,userName);
                        String newUserName = ds2.child("userName").getValue().toString();
                        fullAddressArray.add("seller:  "+userName+"  "+"address:   "+ds2.child("fullAddress").getValue().toString());

                    }

                }

            }


        }


    }

}