package com.example.sharepay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {
    private TextView mRegisterLogin;
    private Button mRegister;
    private ProgressBar mRegisterProgress;
    private EditText mRegisterFullname, mRegisterEmail, mRegisterPassword, mRegisterRepassword;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        mRegister = (Button)findViewById(R.id.stRegister);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stRegister();
            }
        });

        mRegisterLogin =(TextView)findViewById(R.id.stRegisterLogin);
        mRegisterLogin.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        mRegisterFullname = (EditText)findViewById(R.id.stRegisterFullname);
        mRegisterEmail = (EditText)findViewById(R.id.stRegisterEmail);
        mRegisterPassword = (EditText)findViewById(R.id.stRegisterPassword);
        mRegisterRepassword = (EditText)findViewById(R.id.stRegisterRepassword);

        mRegisterProgress = (ProgressBar)findViewById(R.id.stRegisterProgress);



    }


    private void stRegister() {
        String fullname = mRegisterFullname.getText().toString().trim();
        String email = mRegisterEmail.getText().toString().trim();
        String password = mRegisterPassword.getText().toString().trim();
        String Reenterpassword = mRegisterRepassword.getText().toString().trim();

        if(fullname.isEmpty()){
            mRegisterFullname.setError("Fullname is required");
            mRegisterFullname.requestFocus();
            return;
        }
        if(email.isEmpty()){
            mRegisterEmail.setError("Email is required");
            mRegisterEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mRegisterEmail.setError(("Please provide valid email!"));
            mRegisterEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            mRegisterPassword.setError("Password is required");
            mRegisterPassword.requestFocus();
            return;
        }
        if(Reenterpassword.isEmpty()){
            mRegisterRepassword.setError("Password must be Re-entered");
            mRegisterRepassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            mRegisterRepassword.setError("Password should be atleast 6 character long");
            mRegisterRepassword.requestFocus();
            return;
        }
        if (!(password.equals(Reenterpassword))){
            mRegisterRepassword.setError("Password not matched");
            mRegisterRepassword.requestFocus();
            return;
        } else {
            mRegisterProgress.setVisibility(View.VISIBLE);
            mUser = mAuth.getCurrentUser();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                User user = new User(fullname, email);
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            Toast.makeText(RegisterUser.this, "User have been successfully Registered!", Toast.LENGTH_LONG).show();
                                            mRegisterProgress.setVisibility(View.GONE);
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                                        }  else {
                                            // User registration failed
                                            Exception ex = task.getException();
                                            Log.e("RegisterUser", "Failed to register user", ex);
                                            Toast.makeText(RegisterUser.this, "Failed to Register User!", Toast.LENGTH_LONG).show();
                                            mRegisterProgress.setVisibility(View.GONE);
                                        }
                                    }
                                });


                            } else {
                                Toast.makeText(RegisterUser.this, "Failed to Register User!", Toast.LENGTH_LONG).show();
                                mRegisterProgress.setVisibility(View.GONE);
                            }
                        }
                    });
        }


    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.stRegisterLogin:
                startActivity(new Intent(this, MainActivity.class));
                break;



        }

    }
}
