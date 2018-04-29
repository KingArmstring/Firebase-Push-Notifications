package com.armstring.firebasepushnotifications.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Toast;

import com.armstring.firebasepushnotifications.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;


public class RegistrationActivity extends AppCompatActivity {


    private AppCompatEditText etUserName;
    private AppCompatEditText etUserEmail;
    private AppCompatEditText etPassword;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etUserName = (AppCompatEditText) findViewById(R.id.etUserNameRegister);
        etUserEmail = (AppCompatEditText) findViewById(R.id.etEmailRegister);
        etPassword = (AppCompatEditText) findViewById(R.id.etdPasswordRegister);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }


    public void btnOnRegister(View view) {
        String username;
        String password;
        String email;

        username = etUserName.getText().toString();
        email = etUserEmail.getText().toString();
        password = etPassword.getText().toString();

        if(!username.equals("") && !email.equals("") && !password.equals("")) {
            createUserWithEmailAndPassword(username, password, email);
        } else {
            handleError(username, password, email);
        }
    }

    private void createUserWithEmailAndPassword(String username, String password, String email) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener((task) -> {
                if(task.isSuccessful()) {
                    insertMeInDatabase(username);
                    travelToMainActivity();
                }else {
                    Toast.makeText(RegistrationActivity.this,
                            "Register Fail, Please try again",
                            Toast.LENGTH_SHORT).show();
                }
            }
        );
    }

    private void insertMeInDatabase(String username) {

        String userId = mAuth.getCurrentUser().getUid();
        String token =  FirebaseInstanceId.getInstance().getToken();

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userName", username);
        userMap.put("userId", userId);
        userMap.put("token", token);

        mFirestore.collection("Users").document(userId)
                .set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(RegistrationActivity.this,
                            "token has been saved successfully",
                            Toast.LENGTH_SHORT).show();
                    travelToMainActivity();
                } else {
                    Toast.makeText(RegistrationActivity.this,
                            "Something went wrong",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void handleError(String username, String password, String email) {
        if(username.equals("")) {
            etUserName.setError("CAN't BE EMPTY");
        }
        if(email.equals("")) {
            etUserEmail.setError("CAN't BE EMPTY");
        }
        if(password.equals("")) {
            etPassword.setError("CAN't BE EMPTY");
        }
    }

    public void travelToMainActivity() {
        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
