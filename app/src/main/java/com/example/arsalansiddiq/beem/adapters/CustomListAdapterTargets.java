package com.example.arsalansiddiq.beem.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.models.responsemodels.targetsandachievementsmodel.Target;

import java.util.List;

/**
 * Created by jellani on 9/4/2018.
 */

public class CustomListAdapterTargets extends ArrayAdapter<Target> {

    private Context context;
    private List<Target> targetsandAchievementsModelList;

    public CustomListAdapterTargets(@NonNull Context context, int resource,@NonNull List<Target> objects) {
        super(context, 0, objects);

        this.context = context;
        this.targetsandAchievementsModelList = objects;
    }


    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_items_targets, parent, false);
        }
//        return super.getView(position, convertView, parent);

        TextView txtView_skuName = convertView.findViewById(R.id.txtView_skuName);
        TextView txtView_target = convertView.findViewById(R.id.txtView_target);
        TextView txtView_achievedNumber = convertView.findViewById(R.id.txtView_achievedNumber);
        TextView txtView_achievedPercentage = convertView.findViewById(R.id.txtView_achievedPercentage);

        txtView_skuName.setText(targetsandAchievementsModelList.get(position).getSkuName().toString());
        txtView_target.setText(targetsandAchievementsModelList.get(position).getSkutargets().toString());
        txtView_achievedNumber.setText(targetsandAchievementsModelList.get(position).getAch().toString());
        txtView_achievedPercentage.setText(targetsandAchievementsModelList.get(position).getPer().toString() + "%");

        return convertView;

    }

}
