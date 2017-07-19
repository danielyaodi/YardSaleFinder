package com.cpp.daniel.yardsalefinder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daniel.yardsalefinder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView loginTextView;
    FirebaseAuth mAuth;
    private ProgressDialog loginProDialog;
    private FirebaseAuth.AuthStateListener mAuthListener;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEditText=(EditText)findViewById(R.id.emailEditText);
        passwordEditText=(EditText)findViewById(R.id.passwordEditText);
        loginButton=(Button)findViewById(R.id.loginButton);
        loginTextView=(TextView)findViewById(R.id.loginTextView);
        mAuth= FirebaseAuth.getInstance();
        loginButton.setOnClickListener(this);
        loginTextView.setOnClickListener(this);
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//        mAuthListener= new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    // User is signed in
//                   Toast.makeText(LoginActivity.this,"Listening: User is sign in ",Toast.LENGTH_SHORT).show();
//                } else {
//                    // User is signed out
//                    Toast.makeText(LoginActivity.this,"Listening: User is sign out ",Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        };
    }

    @Override
    public void onClick(View v) {
        if (v == loginButton) {
            login();
        }
        if (v == loginTextView) {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
        }

    }
    private void login() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"The email is emapty!",Toast.LENGTH_SHORT).show();
        }else
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"The password is emapty!",Toast.LENGTH_SHORT).show();
        }else {
            loginProDialog = new ProgressDialog(this);
            loginProDialog.setMessage("Signing In");
            loginProDialog.show();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        loginProDialog.dismiss();
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, ProfileActivity.class);
                        startActivity(intent);

                    }
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Password or Email incorrect", Toast.LENGTH_SHORT).show();

                    }


                }
            });
             }
//        loginProDialog.dismiss();






    }


    }