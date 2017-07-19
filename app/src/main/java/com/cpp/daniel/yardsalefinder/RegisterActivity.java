package com.cpp.daniel.yardsalefinder;

import android.app.ProgressDialog;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText retypePassEditText;
    private EditText telEditText;
    private EditText userEditText;
    private Button registerButton;
    private TextView registerTextView;
    private FirebaseAuth mAuth;
    private DatabaseReference userInfo;
    private String email;
    private String password;
    private String retypePass;
    private String tel;
    private String userName;
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        emailEditText=(EditText)findViewById(R.id.emailEditText);
        passwordEditText=(EditText)findViewById(R.id.passwordEditText);
        retypePassEditText=(EditText)findViewById(R.id.retypePassEditText);
        telEditText=(EditText)findViewById(R.id.telEditText);
        userEditText=(EditText)findViewById(R.id.userEditText);
        registerButton=(Button)findViewById(R.id.registerButton);
        registerTextView=(TextView)findViewById(R.id.registerTextView);
        mAuth= FirebaseAuth.getInstance();
        registerButton.setOnClickListener(this);
        registerTextView.setOnClickListener(this);
        userInfo =  FirebaseDatabase.getInstance().getReference();



    }

    @Override
    public void onClick(View v) {
        if (v == registerButton) {
            register();
        }
        if (v == registerTextView) {
            finish();
//            Intent intent = new Intent();
//            intent.setClass(LoginActivity.this,RegisterActivity.class);
//            startActivity(intent);
        }
    }

    private void register() {
        email = emailEditText.getText().toString().trim();
        password= passwordEditText.getText().toString().trim();
         retypePass= retypePassEditText.getText().toString().trim();
       tel=telEditText.getText().toString().trim();
        userName=userEditText.getText().toString().trim();
        if(!password.equals(retypePass)){
            Toast.makeText(this,"The password doesn't match",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"The email is empty!",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"The password is empty!",Toast.LENGTH_SHORT).show();
        }else {


            ProgressDialog regProDialog = new ProgressDialog(this);
            regProDialog.setMessage("Registering");
            regProDialog.show();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        uid = mAuth.getCurrentUser().getUid();
                        Toast.makeText(RegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                        addUserInfo();
                    }


//                Log.d(, "createUserWithEmail:onComplete:" + task.isSuccessful());
                    if (!task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Register Fail", Toast.LENGTH_SHORT).show();
                    }
                }


            });
            regProDialog.dismiss();
        }
    }

    private void addUserInfo() {

                userInfo.child("Users").child(uid).child("userName").setValue(userName);
                userInfo.child("Users").child(uid).child("email").setValue(email);
                 userInfo.child("Users").child(uid).child("telephone").setValue(tel);
//
    }


}
