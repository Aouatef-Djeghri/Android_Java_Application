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
import com.example.servicebypro.Remote.Models.Work;

import java.util.ArrayList;
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private ArrayList<Work> listItems;
    private OnSearchListener onSearchListener;
    private Context mContext;

    public SearchAdapter(ArrayList<Work> listItems, Context mContext, OnSearchListener onSearchListener) {
        this.listItems = listItems;
        this.mContext = mContext;
        this.onSearchListener = onSearchListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dropdown_menu_popup_item, parent, false);
        return new ViewHolder(v,onSearchListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Work itemList = listItems.get(position);
        holder.text.setText(itemList.getService().getName());
    }

    @Override
    public int getItemCount() {

        if (listItems != null) {
            return listItems.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView text;
        OnSearchListener onSearchListener;

        public ViewHolder(View itemView, OnSearchListener onSearchListener) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            itemView.setOnClickListener(this);
            this.onSearchListener = onSearchListener;
        }

        @Override
        public void onClick(View view) {
            onSearchListener.onSearchClick(getAdapterPosition());
        }
    }

    public void setList(ArrayList<Work> listItems) {
        this.listItems = listItems;
        notifyDataSetChanged();
    }


    public interface OnSearchListener {
        void onSearchClick(int position);
    }
}