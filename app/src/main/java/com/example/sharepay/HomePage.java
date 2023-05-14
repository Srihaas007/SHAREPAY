package com.example.sharepay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomePage extends AppCompatActivity implements View.OnClickListener {
    private Button mHomeLogout;
    private ImageView mTripSharing, mHomeSharing, mPartySharing,mDomesticSharing;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private TextView mHomeName, mHomeEmail, mHomeWelcome,mHomeViewall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        mHomeLogout = findViewById(R.id.stViewLogout);
        mTripSharing = findViewById(R.id.stViewTripSharing);
        mHomeSharing = findViewById(R.id.stViewHomeSharing);
        mPartySharing = findViewById(R.id.stViewPartySharing);
        mDomesticSharing = findViewById(R.id.stViewDomesticSharing);
        mHomeEmail = findViewById(R.id.stTextHomeEmail);
        mHomeName = findViewById(R.id.stTextHomeName);
        mHomeWelcome = findViewById(R.id.stTextWelcome);
        mHomeViewall = findViewById(R.id.stHomeViewall);

        mHomeLogout.setOnClickListener(this);
        mTripSharing.setOnClickListener(this);
        mHomeSharing.setOnClickListener(this);
        mPartySharing.setOnClickListener(this);
        mDomesticSharing.setOnClickListener(this);


        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null){
            mHomeWelcome.setText("Welcome");
            mHomeName.setText("Hello, "+signInAccount.getDisplayName());
            mHomeEmail.setText(signInAccount.getEmail());}

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView greetingTextView = (TextView) findViewById(R.id.stTextWelcome);
        final TextView fullnameTextView = (TextView) findViewById(R.id.stTextHomeName);
        final TextView emailTextView = (TextView) findViewById(R.id.stTextHomeEmail);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userprofile = snapshot.getValue(User.class);

                if(userprofile != null){
                    String fullname = userprofile.fullname;
                    String email = userprofile.email;

                    greetingTextView.setText("Welcome");
                    fullnameTextView.setText("Hello, "+ fullname+ "!");
                    emailTextView.setText(email);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomePage.this,"Something Wrong Happened", Toast.LENGTH_SHORT).show();

            }


        });

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.stViewLogout:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.stViewHomeSharing:
                startActivity(new Intent(this,HomeSharing.class));
                break;
            case R.id.stViewTripSharing:
                startActivity(new Intent(this,TripSharing.class));
                break;
            case R.id.stViewPartySharing:
                startActivity(new Intent(this,PartySharing.class));
                break;
            case R.id.stViewDomesticSharing:
                startActivity(new Intent(this,DomesticSharing.class));
                break;


        }

    }
}

