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
import com.example.arsalansiddiq.beem.models.responsemodels.merchant.merchanttask.Datum;

import java.util.List;

/**
 * Created by jellani on 12/2/2018.
 */

public class MerchantTaskAdapter extends ArrayAdapter<Datum> {

    private Context context;
    private List<Datum> merchantTaskList;

    public MerchantTaskAdapter(@NonNull Context context, int resource, @NonNull List<Datum> objects) {
        super(context, resource, objects);
        this.context = context;
        this.merchantTaskList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Datum datum = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_tasks, parent, false);
        }

        TextView txtView_taskType = convertView.findViewById(R.id.txtView_taskType);
        TextView txtView_shopName = convertView.findViewById(R.id.txtView_shopName);
//        txtView_taskType.setText(datum.getTasktype());
        txtView_shopName.setText(datum.getShopName());
        return convertView;
    }
}
