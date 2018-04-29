package com.armstring.firebasepushnotifications.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.armstring.firebasepushnotifications.R;
import com.armstring.firebasepushnotifications.User;
import com.armstring.firebasepushnotifications.UsersRecyclerViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView rvUsers;

    private List<User> users;
    private UsersRecyclerViewAdapter adapter;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvUsers = (RecyclerView) findViewById(R.id.rvUsers);

        mFirestore = FirebaseFirestore.getInstance();
        users = new ArrayList<>();
        adapter = new UsersRecyclerViewAdapter(users, this);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        rvUsers.setAdapter(adapter);

        fillUsersList();
    }

    public void fillUsersList() {

        users.clear();
        mFirestore.collection("Users")
            .addSnapshotListener(this, (queryDocumentSnapshots, e) -> {
                for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    if(doc.getType() == DocumentChange.Type.ADDED) {
                        User user = doc.getDocument().toObject(User.class);
                        users.add(user);
                        adapter.notifyDataSetChanged();
                    }
                }
                Log.d("Armstring", "users size: " + String.valueOf(users.size()));
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_logout:
                logout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
