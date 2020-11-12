package com.example.servicebypro.Activities.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.servicebypro.Activities.Dashboard.Add.AddFragment;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.Service;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;

public class SectionItemsAdapter extends RecyclerView.Adapter<SectionItemsAdapter.ViewHolder>{


    ArrayList<Service> items;

    public SectionItemsAdapter(ArrayList<Service> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_item_section_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.item_text_view.setText("- "+items.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView item_text_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item_text_view = itemView.findViewById(R.id.item_text_view);
        }
    }
}
