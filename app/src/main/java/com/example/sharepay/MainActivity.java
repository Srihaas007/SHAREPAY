package com.example.sharepay;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mLoginEmail, mLoginPassword;
    private Button mLoginLogin;
    private TextView mLoginRegister, mLoginForgot;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressBar mLoginProgress;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    @Override
    protected void onStart(){
        super.onStart();
         FirebaseUser user = mAuth.getCurrentUser();
         if(user != null );
         Intent intent = new Intent(getApplicationContext(),HomePage.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoginEmail = findViewById(R.id.stLoginEmail);
        mLoginProgress = findViewById(R.id.stLoginProgress);
        mLoginPassword = findViewById(R.id.stLoginPassword);
        mLoginForgot = findViewById(R.id.stLoginForgot);
        mLoginForgot.setOnClickListener(this);
        mLoginLogin = findViewById(R.id.stLoginLogin);
        mLoginRegister = findViewById(R.id.stLoginRegister);
        mLoginRegister.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        GoogleRequest();
        findViewById(R.id.google_signIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();

            }

        });



        mLoginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mLoginEmail.getText().toString();
                String password  = mLoginPassword.getText().toString();
                if(email.isEmpty()){
                    mLoginEmail.setError("Email is required");
                    mLoginEmail.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mLoginEmail.setError(("Please provide valid email!"));
                    mLoginEmail.requestFocus();
                    return;
                }
                if(password.isEmpty()){
                    mLoginPassword.setError("Password is required");
                    mLoginPassword.requestFocus();
                    return;
                }
                if(password.length() < 6){
                    mLoginPassword.setError("Password should be atleast 6 character long");
                    mLoginPassword.requestFocus();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            mLoginProgress.setVisibility(View.GONE);
                            startActivity(new Intent(getApplicationContext(), HomePage.class));
                        }else{
                            Toast.makeText(MainActivity.this,"Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            mLoginProgress.setVisibility(View.GONE);
                        }
                    }
                });
            }

        });

    }




    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void GoogleRequest() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        //noinspection deprecation
        startActivityForResult(signInIntent, RC_SIGN_IN );

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

                SharedPreferences.Editor editor = getApplicationContext()
                        .getSharedPreferences("MyPrefs", MODE_PRIVATE)
                        .edit();
                editor.putString("username", account.getDisplayName());
                editor.putString("email", account.getDisplayName());

            } catch (ApiException e) {
                Toast.makeText(MainActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(MainActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), HomePage.class));

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(MainActivity.this,"Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
        }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.stLoginRegister:
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.stLoginForgot:
                startActivity(new Intent(this, ForgotPassword.class));
                break;
        }
    }
}
