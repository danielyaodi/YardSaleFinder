package com.cpp.daniel.yardsalefinder;

import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.daniel.yardsalefinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Calendar;

public class postYardSaleInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText addr1EditText;
    private EditText addr2EditText;
    private EditText cityEditText;
    private Spinner statePicker;
    private EditText zipCodeEditText;
    private DatabaseReference postInfo;
    private Button postYardSaleButton;
    private String uID;
    private String fullAddress;
    private Spinner timeStartSpinner;
    private Spinner timeEndSpinner;
    private TextView dateStartTextView;
    private TextView dateEndTextView;
    Geocoder geocoder  ;
    private Button cancelPostButton;
    Calendar calendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_yard_sale_info);
        addr1EditText = (EditText) findViewById(R.id.addr1EditText);
        addr2EditText = (EditText) findViewById(R.id.addr2EditText);
        cityEditText = (EditText)findViewById(R.id.cityEditText);
        statePicker =  (Spinner) findViewById(R.id.statePicker);
        statePicker.setPrompt("Please select a state:");
        statePicker.setSelection(4);
        zipCodeEditText =(EditText)findViewById(R.id.zipCodeEditText);
        postYardSaleButton = (Button)findViewById(R.id.postYardSaleButton);
        postYardSaleButton.setOnClickListener(this);
        cancelPostButton = (Button)findViewById(R.id.cancelPostButton);
        cancelPostButton.setOnClickListener(this);
        uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        postInfo= FirebaseDatabase.getInstance().getReference();

        timeStartSpinner=(Spinner)findViewById(R.id.timeStartSpinner);
        timeStartSpinner.setPrompt("Start Time");
        timeStartSpinner.setSelection(1);

        timeEndSpinner=(Spinner)findViewById(R.id.timeEndSpinner);
        timeEndSpinner.setPrompt("End Time");
        timeEndSpinner.setSelection(9);

        geocoder = new Geocoder(this);

        dateStartTextView=(TextView) findViewById(R.id.dateBeginTextView);
//        dateStartTextView.setText(calendar.MONTH+" "+calendar.DAY_OF_MONTH+" "+calendar.YEAR );
        updateDateFormat(dateStartTextView);
        dateStartTextView.setOnClickListener(this);

        dateEndTextView=(TextView) findViewById(R.id.dateEndTextView);
        dateEndTextView.setOnClickListener(this);
        updateDateFormat(dateEndTextView);
//        dateEndTextView.setText(calendar.MONTH+" "+calendar.DAY_OF_MONTH+" "+calendar.YEAR );
    }
    @Override
    public void onClick(View v) {
        if(v==postYardSaleButton) {
            postAddress();
            postLatLng();
            postTime();
        }
        if(v==cancelPostButton){
            finish();
        }
        if(v==dateStartTextView){
           datePick();
            updateDateFormat(dateStartTextView);
        }
        if(v==dateEndTextView){
            datePick();
            updateDateFormat(dateEndTextView);
        }
    }

    private void datePick( ) {
          DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
             @Override
             public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                 calendar.set(Calendar.YEAR,year);
                 calendar.set(Calendar.MONTH,month);
                 calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
             }
         };
        new DatePickerDialog(postYardSaleInfoActivity.this, listener, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
//        updateDateFormat(textView);
    }

    private void updateDateFormat(TextView textView) {
        String myFormat = "MM dd yyyy EEE"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        textView.setText(sdf.format(calendar.getTime()));
    }

    private void postLatLng() {

        try {
            double latitude = geocoder.getFromLocationName(fullAddress,1).get(0).getLatitude();
            double longitude = geocoder.getFromLocationName(fullAddress,1).get(0).getLongitude();
            postInfo.child("Users").child(uID).child("latitude").setValue(latitude);
            postInfo.child("Users").child(uID).child("longitude").setValue(longitude);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void postTime() {
        String timeStart = timeStartSpinner.getSelectedItem().toString();
        String timeEnd = timeEndSpinner.getSelectedItem().toString();

        postInfo.child("Users").child(uID).child("timeStart").setValue(timeStart);
        postInfo.child("Users").child(uID).child("timeEnd").setValue(timeEnd);
//        postInfo.child("Users").child(uID).child("Year").setValue(dateStartTextView);
//        postInfo.child("Users").child(uID).child("Month").setValue(beginDateList.get(1));
//        postInfo.child("Users").child(uID).child("Day").setValue(beginDateList.get(2));

    }

    private void postAddress()  {
        String addrLine1 = addr1EditText.getText().toString().trim();
        String addrLine2 = addr2EditText.getText().toString().trim();
        String city = cityEditText.getText().toString().trim();
        String state = statePicker.getSelectedItem().toString();
        String zipCode = zipCodeEditText.getText().toString().trim();
        fullAddress= addrLine1 +" "+ addrLine2 +" "+ city +" "+ state +" "+ zipCode;

        postInfo.child("Users").child(uID).child("addressLine1").setValue(addrLine1);
        postInfo.child("Users").child(uID).child("addressLine2").setValue(addrLine2);
        postInfo.child("Users").child(uID).child("city").setValue(city);
        postInfo.child("Users").child(uID).child("state").setValue(state);
        postInfo.child("Users").child(uID).child("zipCode").setValue(zipCode);
        postInfo.child("Users").child(uID).child("fullAddress").setValue(fullAddress);
    }

}
