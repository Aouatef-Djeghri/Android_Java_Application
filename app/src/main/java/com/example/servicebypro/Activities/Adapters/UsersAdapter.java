package com.example.servicebypro.Activities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.User;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private ArrayList<User> listItems;
    private OnUserListener onUserListener;
    private Context mContext;

    public UsersAdapter(ArrayList<User> listItems, Context mContext, OnUserListener onUserListener) {
        this.listItems = listItems;
        this.mContext = mContext;
        this.onUserListener = onUserListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item__user, parent, false);
        return new ViewHolder(v,onUserListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final User itemList = listItems.get(position);
        holder.textUserName.setText(itemList.getFirstName()+" "+itemList.getLastName());
        holder.distance_between.setText(String.format("%.2f", itemList.getDistance())+" Km");//itemList.getDistance()+
    }

    @Override
    public int getItemCount() {

        if (listItems != null) {
            return listItems.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textUserName;
        public TextView distance_between;
        OnUserListener onUserListener;

        public ViewHolder(View itemView, UsersAdapter.OnUserListener onUserListener) {
            super(itemView);
            textUserName = itemView.findViewById(R.id.text_user_name);
            distance_between = itemView.findViewById(R.id.distance_between);
            itemView.setOnClickListener(this);
            this.onUserListener = onUserListener;
        }

        @Override
        public void onClick(View view) {
            onUserListener.onUserClick(getAdapterPosition());
        }
    }

    public void setList(ArrayList<User> listItems) {
        this.listItems = listItems;
        notifyDataSetChanged();
    }


    public interface OnUserListener {
        void onUserClick(int position);
    }
}