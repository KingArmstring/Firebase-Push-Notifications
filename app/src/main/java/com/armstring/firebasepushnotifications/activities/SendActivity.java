package com.armstring.firebasepushnotifications.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.armstring.firebasepushnotifications.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SendActivity extends AppCompatActivity {

    private TextView txtUserId;
    private EditText etNotificationMessage;

    private FirebaseFirestore mFirestore;

    private String userName;
    private String userId;//this is the id of the user I am trying to send notification for.
    private String currentId;//this is the id of me(we will be used to tell the other guy that this is me)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        txtUserId = (TextView) findViewById(R.id.txtUserIdView);
        etNotificationMessage = (EditText) findViewById(R.id.etNotificationMessage);

        mFirestore = FirebaseFirestore.getInstance();
        currentId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("userName");
        txtUserId.setText("Send To" + userName);

    }

    public void btnSendNotification(View view) {
        String message = etNotificationMessage.getText().toString();
        etNotificationMessage.setText("");
        if (!TextUtils.isEmpty(message)) {
            Map<String, Object> notificationMessage = new HashMap<>();
            notificationMessage.put("message", message);
            notificationMessage.put("from", currentId);

            mFirestore.collection("Users").document(userId)
                .collection("Notification").add(notificationMessage)
                .addOnSuccessListener((documentReference) -> {
                    Toast.makeText(SendActivity.this, "done", Toast.LENGTH_SHORT).show();
                });
        }
    }
}