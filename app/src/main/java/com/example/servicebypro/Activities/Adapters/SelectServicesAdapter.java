package com.example.servicebypro.Activities.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.Service;

import java.util.ArrayList;


public class SelectServicesAdapter extends RecyclerView.Adapter<SelectServicesAdapter.ViewHolder> implements Filterable{

    private ArrayList<Service> listItems;

    private ArrayList<Service> listItemsFull;
    private OnServiceListener onServiceListener;
    private Context mContext;

    public SelectServicesAdapter(ArrayList<Service> listItems, Context mContext, OnServiceListener onServiceListener) {
        this.listItems = listItems;
        listItemsFull = new ArrayList<>(listItems);
        this.mContext = mContext;
        this.onServiceListener = onServiceListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_select_service, parent, false);
        return new ViewHolder(v,onServiceListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Service itemList = listItems.get(position);
        holder.textServiceName.setText(itemList.getName());
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(itemList.isSelected());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holder.checkBox.setChecked(isChecked);
                listItems.get(position).setSelected(isChecked);
            }
        });

    }

    @Override
    public int getItemCount() {

        if (listItems != null) {
            return listItems.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Service> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listItemsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Service item : listItemsFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            listItems.clear();
            listItems.addAll((ArrayList) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textServiceName;
        public CheckBox checkBox;
        OnServiceListener onServiceListener;

        public ViewHolder(View itemView, OnServiceListener onServiceListener) {
            super(itemView);
            textServiceName = itemView.findViewById(R.id.text_servie_name);
            checkBox = itemView.findViewById(R.id.service_check);
            //item click event listener
            itemView.setOnClickListener(this);
            this.onServiceListener = onServiceListener;
        }

        @Override
        public void onClick(View view) {


            if(listItems.get(getAdapterPosition()).isSelected()){
                listItems.get(getAdapterPosition()).setSelected(false);
                checkBox.setChecked(false);
            }else {
                listItems.get(getAdapterPosition()).setSelected(true);
                checkBox.setChecked(true);
            }

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