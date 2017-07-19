package com.cpp.daniel.yardsalefinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.daniel.yardsalefinder.R;

import java.util.ArrayList;
import java.util.List;

public class ListAddressActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView fullAddressListView;
    private Button goBackButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_address);
        fullAddressListView=(ListView)findViewById(R.id.fullAddressListView);
        ArrayList userList = (ArrayList) getIntent().getSerializableExtra("addressAdapter");
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,userList);
        fullAddressListView.setAdapter(adapter);
        goBackButton=(Button)findViewById(R.id.goBackButton);
        goBackButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
