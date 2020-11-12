package com.example.servicebypro.Activities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.Section;
import com.example.servicebypro.Remote.Models.Service;

import java.util.ArrayList;


public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.ViewHolder> {

    ArrayList<Section> sectionList;
    private Context mContext;

    public SectionAdapter(ArrayList<Section> sectionList, Context mContext) {
        this.sectionList = sectionList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_section, parent, false);
        return new SectionAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Section itemList = sectionList.get(position);
        String sectionName = itemList.getSectionName();
        ArrayList<Service> items = itemList.getSectionItems();

        holder.section_name_text_view.setText(sectionName);

        SectionItemsAdapter childRecyclerAdapter = new SectionItemsAdapter(items);
        holder.child_recycler_view.setAdapter(childRecyclerAdapter);

    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView section_name_text_view;
        RecyclerView child_recycler_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            section_name_text_view = itemView.findViewById(R.id.section_name_text_view);
            child_recycler_view = itemView.findViewById(R.id.child_recycler_view);
        }
    }
}
