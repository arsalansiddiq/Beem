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
import com.example.arsalansiddiq.beem.models.responsemodels.tasksresponsemodels.Task;

import java.util.List;

/**
 * Created by jellani on 9/21/2018.
 */

public class CustomListAdapterTasks extends ArrayAdapter<Task> {

    private Context context;
    private List<Task> task;

    public CustomListAdapterTasks(@NonNull Context context, int resource, @NonNull List<Task> objects) {
        super(context, 0, objects);
        this.context = context;
        this.task = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Task task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_tasks, parent, false);
        }

        TextView txtView_taskType = convertView.findViewById(R.id.txtView_taskType);
//      TextView txtView_hyphine = convertView.findViewById(R.id.txtView_hyphine);
        TextView txtView_shopName = convertView.findViewById(R.id.txtView_shopName);
//        TextView txtView_shopDescription = convertView.findViewById(R.id.txtView_shopDescription);

        txtView_taskType.setText(task.getTasktype());
        txtView_shopName.setText(task.getShopName());
//        txtView_shopDescription.setText(task.getDescription());

//        return super.getView(position, convertView, parent);

        return convertView;
    }
}
