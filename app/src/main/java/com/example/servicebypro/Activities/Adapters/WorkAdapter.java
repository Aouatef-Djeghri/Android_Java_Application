package com.example.servicebypro.Activities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.Remote.Models.Work;
import com.example.servicebypro.R;
import com.example.servicebypro.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.ViewHolder> {

    private ArrayList<Work> listItems;
    private OnWorkListener onWorkListener;
    private Context mContext;
    Util util ;

    SessionManager sessionManager;
    public WorkAdapter(ArrayList<Work> listItems, Context mContext, OnWorkListener onWorkListener) {
        this.listItems = listItems;
        this.mContext = mContext;
        this.onWorkListener = onWorkListener;
        util = new Util(mContext);
        sessionManager = new SessionManager(mContext);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_work, parent, false);
        return new ViewHolder(v, onWorkListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        final Work itemList = listItems.get(position);

        switch (itemList.getStatus()) {
            case "request":
                holder.textStatus.setTextColor(ContextCompat.getColor(mContext, R.color.my_green));
                switch (itemList.getType()) {
                    case "post":
                        holder.textStatus.setText("OPEN FOR APPLICATIONS");
                        holder.textDescription.setText(itemList.getTitle());
                        holder.textSomething.setText(itemList.getService().getName());
                        break;
                    case "proposal":
                        holder.textStatus.setText("Request for " + itemList.getApplications().get(0).getUser().getFirstName());
                        holder.textDescription.setText(itemList.getTitle());
                        holder.textSomething.setText(itemList.getService().getName());
                        if (itemList.getApplications().get(0).getStatus().equals("declined")) {
                            holder.textStatus.setTextColor(ContextCompat.getColor(mContext, R.color.my_purple));
                            holder.textStatus.setText("[CANCELED] Request for " + itemList.getApplications().get(0).getUser().getFirstName());
                        }
                        break;
                }
                break;

            case "finished":
                holder.textStatus.setText("COMPLETED");
                holder.textStatus.setTextColor(ContextCompat.getColor(mContext, R.color.mygray));
                holder.textDescription.setText(itemList.getTitle());
                holder.textSomething.setText(itemList.getService().getName());
                break;

            case "canceled":
                holder.textStatus.setText("CANCELED");
                holder.textStatus.setTextColor(ContextCompat.getColor(mContext, R.color.mygray));
                holder.textDescription.setText(itemList.getTitle());
                holder.textSomething.setText(itemList.getService().getName());
                break;

            case "pending":
            case "mid_finished":

                switch (itemList.getType()) {
                    case "post":

                        //todo i made changes here ,i need to retest it later in case of artisan
                        //if user is the client
                        if((itemList.getUser().getIdUser().equals(sessionManager.getUser().getIdUser()))){
                            holder.textStatus.setTextColor(ContextCompat.getColor(mContext, R.color.my_orange));
                        }else{
                            if(util.isMyApplicationAccepted(itemList, sessionManager.getUser())){
                                holder.textStatus.setTextColor(ContextCompat.getColor(mContext, R.color.my_orange));
                            }else{
                                holder.textStatus.setTextColor(ContextCompat.getColor(mContext, R.color.my_purple));
                            }
                        }
                        holder.textStatus.setText("CLOSED FOR APPLICATIONS");
                        holder.textDescription.setText(itemList.getTitle());
                        holder.textSomething.setText(itemList.getService().getName());
                        break;
                    case "proposal":
                        holder.textStatus.setTextColor(ContextCompat.getColor(mContext, R.color.my_orange));
                        holder.textStatus.setText("Project for " + itemList.getApplications().get(0).getUser().getFirstName());
                        holder.textDescription.setText(itemList.getTitle());
                        holder.textSomething.setText(itemList.getService().getName());
                        break;
                }
                break;
        }

        holder.textName.setText(itemList.getUser().getFirstName() + " " + itemList.getUser().getLastName());
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
        public TextView textStatus, textDescription, textName, textSomething;
        OnWorkListener onWorkListener;

        public ViewHolder(View itemView, WorkAdapter.OnWorkListener onWorkListener) {
            super(itemView);
            textStatus = itemView.findViewById(R.id.text_status);
            textDescription = itemView.findViewById(R.id.text_description);
            textName = itemView.findViewById(R.id.text_name);
            textImage = (ImageView) itemView.findViewById(R.id.text_image);
            textSomething = itemView.findViewById(R.id.text_something);
            this.onWorkListener = onWorkListener;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            onWorkListener.onWorkClick(getAdapterPosition());
        }
    }

    public interface OnWorkListener {
        void onWorkClick(int position);
    }

    public void setList(ArrayList<Work> listItems) {
        this.listItems = listItems;
        notifyDataSetChanged();
    }


}