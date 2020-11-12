package com.example.servicebypro.Activities.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.Service;
import com.example.servicebypro.Remote.Models.Work;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BrowseWorkAdapter extends RecyclerView.Adapter<BrowseWorkAdapter.ViewHolder> {


    private ArrayList<Work> listItems;
    private OnWorkListener onWorkListener;
    private Context mContext;
    Util util ;
    public BrowseWorkAdapter(ArrayList<Work> listItems, Context mContext, OnWorkListener onWorkListener) {
        this.listItems = listItems;
        this.mContext = mContext;
        this.onWorkListener = onWorkListener;
        util = new Util(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_browse_work, parent, false);
        return new ViewHolder(v, onWorkListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Work itemList = listItems.get(position);
        holder.textDescription.setText(itemList.getTitle());
        holder.textSomething.setText(itemList.getService().getName());
        holder.textName.setText(itemList.getUser().getFirstName());
        util.setAvatar(itemList.getUser().getAvatar(),holder.textImage);
    }

    @Override
    public int getItemCount() {

        if (listItems != null) {
            return listItems.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView textImage;
        public TextView textDescription, textName, textSomething;
        OnWorkListener onWorkListener;

        public ViewHolder(View itemView, OnWorkListener onWorkListener) {
            super(itemView);

            textDescription = itemView.findViewById(R.id.text_description);
            textName = itemView.findViewById(R.id.text_name);
            textImage = (ImageView) itemView.findViewById(R.id.text_image);
            textSomething = itemView.findViewById(R.id.text_something);

            itemView.setOnClickListener(this);
            this.onWorkListener = onWorkListener;
        }

        @Override
        public void onClick(View view) {
            onWorkListener.onWorkClick(getAdapterPosition());
        }
    }


    public void setList(ArrayList<Work> listItems) {
        this.listItems = listItems;
        notifyDataSetChanged();
    }


    public interface OnWorkListener {
        void onWorkClick(int position);
    }
}