package com.example.servicebypro.Activities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.Service;

import java.util.ArrayList;


public class UserServicesLabelsAdapter extends RecyclerView.Adapter<UserServicesLabelsAdapter.ViewHolder> {

    private ArrayList<Service> listItems;
    private OnServiceListener onServiceListener;
    private Context mContext;

    public UserServicesLabelsAdapter(ArrayList<Service> listItems, Context mContext, OnServiceListener onServiceListener) {
        this.listItems = listItems;
        this.mContext = mContext;
        this.onServiceListener = onServiceListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item__user_service_label, parent, false);
        return new ViewHolder(v, onServiceListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Service itemList = listItems.get(position);
        holder.textServiceName.setText(itemList.getName());
    }

    @Override
    public int getItemCount() {

        if (listItems != null) {
            return listItems.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textServiceName;
        OnServiceListener onServiceListener;

        public ViewHolder(View itemView, UserServicesLabelsAdapter.OnServiceListener onServiceListener) {
            super(itemView);
            textServiceName = itemView.findViewById(R.id.text_servie_name);
            itemView.setOnClickListener(this);
            this.onServiceListener = onServiceListener;
        }

        @Override
        public void onClick(View view) {
            onServiceListener.onServiceClick(getAdapterPosition());
        }
    }

    public void setList(ArrayList<Service> listItems) {
        this.listItems = listItems;
        notifyDataSetChanged();
    }


    public interface OnServiceListener {
        void onServiceClick(int position);
    }
}