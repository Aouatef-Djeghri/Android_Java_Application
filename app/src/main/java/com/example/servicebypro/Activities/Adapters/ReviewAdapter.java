package com.example.servicebypro.Activities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servicebypro.Activities.HelperClasses.RandomColors;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.Categorie;
import com.example.servicebypro.Remote.Models.Review;

import java.util.ArrayList;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private ArrayList<Review> listItems;
    private OnReviewListener onReviewListener;
    private Context mContext;
    RandomColors c = new RandomColors();
    Util util ;
    public ReviewAdapter(ArrayList<Review> listItems, Context mContext,OnReviewListener onReviewListener) {
        this.listItems = listItems;
        this.mContext = mContext;
        util = new Util(mContext);
        this.onReviewListener = onReviewListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_review, parent, false);
        return new ViewHolder(v,onReviewListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Review itemList = listItems.get(position);
        util.setAvatar(itemList.getUser().getAvatar(),holder.reviewer_image);
        holder.review_title.setText(itemList.getTitle());
        holder.review_body.setText(itemList.getBody());
        holder.review_service.setText(itemList.getWork().getService().getName());
        holder.review_rating.setRating(itemList.getRating());
        String userFirstName = itemList.getUser().getFirstName();
        String userLastName = itemList.getUser().getLastName();
        holder.reviewer_name.setText(userFirstName.substring(0, 1).toUpperCase() + userFirstName.substring(1)+" "+userLastName.substring(0, 1).toUpperCase() + userLastName.substring(1));
    }

    @Override
    public int getItemCount() {

        if (listItems != null) {
            return listItems.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView reviewer_image;
        public TextView review_title;
        public TextView reviewer_name;
        public TextView review_body;
        public TextView review_service;
        public RatingBar review_rating;
        OnReviewListener onReviewListener;

        public ViewHolder(View itemView, OnReviewListener onReviewListener) {
            super(itemView);
            reviewer_image = itemView.findViewById(R.id.reviewer_image);
            review_title = itemView.findViewById(R.id.review_title);
            reviewer_name = itemView.findViewById(R.id.reviewer_name);
            review_body =  itemView.findViewById(R.id.review_body);
            review_service =  itemView.findViewById(R.id.review_service);
            review_rating =  itemView.findViewById(R.id.review_rating);

            this.onReviewListener = onReviewListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onReviewListener.onReviewClick(getAdapterPosition());
        }
    }

    public void setList(ArrayList<Review> listItems) {
        this.listItems = listItems;
        notifyDataSetChanged();
    }


    public interface OnReviewListener {
        void onReviewClick(int position);
    }
}