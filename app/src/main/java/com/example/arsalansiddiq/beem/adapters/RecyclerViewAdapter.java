package com.example.arsalansiddiq.beem.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.models.responsemodels.targetsandachievementsmodel.Target;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private List<Target> targetArrayList = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(List<Target> targetArrayList) {
        this.targetArrayList = targetArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.list_items_targets, parent, false);

        // Return a new holder instance
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

        // Get the data model based on position
        Target target = targetArrayList.get(position);

        // Set item views based on your views and data model
        TextView txtView_skuName = holder.txtView_skuName;
        TextView txtView_target = holder.txtView_target;
        TextView txtView_achievedNumber = holder.txtView_achievedNumber;
        TextView txtView_achievedPercentage = holder.txtView_achievedPercentage;

        txtView_skuName.setText(target.getSkuName().toString());
        txtView_target.setText(target.getSkutargets().toString());
        txtView_achievedNumber.setText(target.getAch().toString());
        txtView_achievedPercentage.setText(target.getPer().toString() + "%");
    }

    @Override
    public int getItemCount() {
        return targetArrayList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView txtView_skuName;
        TextView txtView_target;
        TextView txtView_achievedNumber;
        TextView txtView_achievedPercentage;



        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public RecyclerViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            txtView_skuName = itemView.findViewById(R.id.txtView_skuName);
            txtView_target = itemView.findViewById(R.id.txtView_target);
            txtView_achievedNumber = itemView.findViewById(R.id.txtView_achievedNumber);
            txtView_achievedPercentage = itemView.findViewById(R.id.txtView_achievedPercentage);
        }
    }
}