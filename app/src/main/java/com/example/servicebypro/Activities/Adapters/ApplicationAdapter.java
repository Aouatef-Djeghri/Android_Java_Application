package com.example.servicebypro.Activities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.Application;

import java.util.ArrayList;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ViewHolder> {

    private ArrayList<Application> listItems;
    private OnApplicationListener onApplicationListener;
    private Context mContext;

    Util util ;
    public ApplicationAdapter(ArrayList<Application> listItems, Context mContext, OnApplicationListener onApplicationListener) {
        this.listItems = listItems;
        this.mContext = mContext;
        util = new Util(mContext);
        this.onApplicationListener = onApplicationListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_application, parent, false);
        return new ViewHolder(v, onApplicationListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        final Application itemList = listItems.get(position);
        util.setAvatar(itemList.getUser().getAvatar(),holder.userAvatar);
        holder.userName.setText(itemList.getUser().getFirstName() + " " + itemList.getUser().getLastName());
    }

    @Override
    public int getItemCount() {

        if (listItems != null) {
            return listItems.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView userAvatar;
        public TextView  userName; // userRating;
        OnApplicationListener onApplicationListener;

        public ViewHolder(View itemView, ApplicationAdapter.OnApplicationListener onApplicationListener) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name);
            userAvatar = (ImageView) itemView.findViewById(R.id.user_avatar);
            this.onApplicationListener = onApplicationListener;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            onApplicationListener.onApplicationClick(getAdapterPosition());
        }
    }

    public interface OnApplicationListener {
        void onApplicationClick(int position);
    }


}