package com.example.sharepay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener{

    private EditText mForgetEmail;
    private TextView mForgotWelcome;
    private Button mForgotReset,mForgotLogin;
    private ProgressBar mForgotProgress;
    private ImageButton mForgotBack;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mForgetEmail = (EditText)findViewById(R.id.stForgotEmail);
        mForgotWelcome = (TextView)findViewById(R.id.stForgotWelcome);
        mForgotReset = (Button)findViewById(R.id.stForgotReset);
        mForgotProgress = (ProgressBar)findViewById(R.id.stForgotProgress);
        mForgotLogin = (Button)findViewById(R.id.stForgotLogin);
        mForgotLogin.setOnClickListener(this);
        mForgotBack = (ImageButton)findViewById(R.id.stForgotBack);
        mForgotBack.setOnClickListener(this);


        auth = FirebaseAuth.getInstance();
        mForgotReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetpassword();
            }
        });

    }
    private void resetpassword(){
        String email = mForgetEmail.getText().toString().trim();

        if(email.isEmpty()){
            mForgetEmail.setError("Email is required");
            mForgetEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mForgetEmail.setError(("Please provide valid email!"));
            mForgetEmail.requestFocus();
            return;
        }
        mForgotProgress.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this,"Reset password link has been successfully sent to your email",Toast.LENGTH_SHORT).show();
                    mForgotProgress.setVisibility(View.GONE);
                }else{
                    Toast.makeText(ForgotPassword.this,"Something went wrong try again later", Toast.LENGTH_SHORT).show();
                    mForgotProgress.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.stForgotBack:
               startActivity(new Intent(this,MainActivity.class));
               break;
           case R.id.stForgotLogin:
               startActivity(new Intent(this,MainActivity.class));
               break;
       }
    }
}