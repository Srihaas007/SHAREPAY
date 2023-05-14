package com.example.sharepay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewAll extends AppCompatActivity implements View.OnClickListener {
    private Button mViewHomeLogout;
    private ImageView mViewTripSharing, mViewHomeSharing, mViewPartySharing, mViewDomesticSharing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
        mViewHomeLogout = findViewById(R.id.stViewLogout);
        mViewTripSharing = findViewById(R.id.stViewTripSharing);
        mViewPartySharing = findViewById(R.id.stViewPartySharing);
        mViewHomeSharing = findViewById(R.id.stViewHomeSharing);
        mViewDomesticSharing = findViewById(R.id.stViewDomesticSharing);

        mViewHomeLogout.setOnClickListener(this);
        mViewTripSharing.setOnClickListener(this);
        mViewPartySharing.setOnClickListener(this);
        mViewHomeSharing.setOnClickListener(this);
        mViewDomesticSharing.setOnClickListener(this);
    }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stViewLogout:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.stViewHomeSharing:
                startActivity(new Intent(this, HomeSharing.class));
                break;
            case R.id.stViewTripSharing:
                startActivity(new Intent(this, TripSharing.class));
                break;
            case R.id.stViewPartySharing:
                startActivity(new Intent(this, PartySharing.class));
                break;
            case R.id.stViewDomesticSharing:
                startActivity(new Intent(this, DomesticSharing.class));
                break;
        }
    }
}