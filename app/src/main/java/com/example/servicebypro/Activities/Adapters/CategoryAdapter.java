package com.example.servicebypro.Activities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servicebypro.Activities.HelperClasses.RandomColors;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.Categorie;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private ArrayList<Categorie> listItems;
    private OnCategoryListener onCategoryListener;
    private Context mContext;
    RandomColors c = new RandomColors();
    Util util ;
    public CategoryAdapter(ArrayList<Categorie> listItems, Context mContext,OnCategoryListener onCategoryListener) {
        this.listItems = listItems;
        this.mContext = mContext;
        util = new Util(mContext);
        this.onCategoryListener = onCategoryListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_category, parent, false);
        return new ViewHolder(v,onCategoryListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Categorie itemList = listItems.get(position);

        util.setAvatar(itemList.getImage(),holder.categoryImage);
        holder.materialCardView.setCardBackgroundColor(c.getRandomColor());
        holder.categoryName.setText(itemList.getName());

    }

    @Override
    public int getItemCount() {

        if (listItems != null) {
            return listItems.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView categoryImage;
        public TextView categoryName;
        OnCategoryListener onCategoryListener;
        MaterialCardView materialCardView;

        public ViewHolder(View itemView, OnCategoryListener onCategoryListener) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.category_image);
            categoryName = itemView.findViewById(R.id.category_name);
            materialCardView = itemView.findViewById(R.id.materialCardView);
            this.onCategoryListener = onCategoryListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onCategoryListener.onCategoryClick(getAdapterPosition());
        }
    }

    public void setList(ArrayList<Categorie> listItems) {
        this.listItems = listItems;
        notifyDataSetChanged();
    }


    public interface OnCategoryListener {
        void onCategoryClick(int position);
    }
}
