package com.armstring.firebasepushnotifications;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.armstring.firebasepushnotifications.activities.SendActivity;

import java.util.List;

public class UsersRecyclerViewAdapter extends RecyclerView.Adapter<UsersRecyclerViewAdapter.MyViewHolder> {

    List<User> users;
    Context context;

    public UsersRecyclerViewAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false);
        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtUserEmail.setText(users.get(position).getUserName());
        String user_id = users.get(position).getUserId();
        String user_name = users.get(position).getUserName();

        holder.mView.setOnClickListener((view) -> {
            Intent sendIntent = new Intent(context, SendActivity.class);
            sendIntent.putExtra("userId", user_id);
            sendIntent.putExtra("userName", user_name);
            context.startActivity(sendIntent);
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView txtUserEmail;

        public MyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            txtUserEmail = (TextView) mView.findViewById(R.id.txtUserEmailListItem);
        }
    }
}
