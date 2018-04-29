package com.armstring.firebasepushnotifications.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.armstring.firebasepushnotifications.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmailLogin;
    private EditText etPasswordLogin;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmailLogin = (EditText) findViewById(R.id.etUserNameLogin);
        etPasswordLogin = (EditText) findViewById(R.id.etPasswordLogin);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            travelToMainActivity();
        }

    }

    public void btnLoginClick(View view) {
        String email = etEmailLogin.getText().toString();
        String password = etPasswordLogin.getText().toString();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((task) -> {
                    if(task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this,
                                "Login Succeeded", Toast.LENGTH_SHORT).show();
                        travelToMainActivity();
                    }else {
                        Toast.makeText(LoginActivity.this,
                                "Login Error", Toast.LENGTH_SHORT).show();
                    }
                }
            );
        }
    }


    public void btnGoToRegistration(View view) {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }

    private void travelToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
