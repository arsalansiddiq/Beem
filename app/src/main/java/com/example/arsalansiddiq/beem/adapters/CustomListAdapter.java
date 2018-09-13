package com.example.arsalansiddiq.beem.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.models.responsemodels.salesresponsemodels.SalesSKUArrayResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomListAdapter extends ArrayAdapter<SalesSKUArrayResponse> implements View.OnClickListener{

    private Context context;
    List<SalesSKUArrayResponse> salesSKUArrayResponse;
    CheckBox checkbox_loose, checkbox_carton;
    private LayoutInflater layoutInflater;
//    TextView txtView_name, txtView_brandImage;
//    EditText edtText_loose, edtText_carton;

    public CustomListAdapter(@NonNull Context context, int resource, @NonNull List<SalesSKUArrayResponse> objects) {
        super(context, 0, objects);

        this.context = context;
        this.salesSKUArrayResponse =  objects;
    }

    @Override
    public int getViewTypeCount() {
        return salesSKUArrayResponse.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return salesSKUArrayResponse.size();
//        return super.getCount();
    }

    @Nullable
    @Override
    public SalesSKUArrayResponse getItem(int position) {
        return salesSKUArrayResponse.get(position);
//        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
//        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ListViewHolder listViewHolder;

        SalesSKUArrayResponse salesSKUArrayResponse = getItem(position);

        if (convertView == null) {

            listViewHolder = new ListViewHolder();
            convertView = layoutInflater.from(getContext()).inflate(R.layout.listview_child, parent, false);
            listViewHolder.txtView_name = convertView.findViewById(R.id.txtView_name);
            listViewHolder.imgView_brandImage = convertView.findViewById(R.id.imgView_brandImage);
            listViewHolder.edtText_loose = convertView.findViewById(R.id.edtText_loose);
            listViewHolder.edtText_carton = convertView.findViewById(R.id.edtText_carton);
            listViewHolder.checkbox_loose = convertView.findViewById(R.id.checkbox_loose);
            listViewHolder.checkbox_carton = convertView.findViewById(R.id.checkbox_carton);
            convertView.setTag(listViewHolder);


        } else {
            listViewHolder = (ListViewHolder) convertView.getTag();
        }
//        Picasso.get().load(salesSKUArrayResponse.getSKUImage()).resize(100, 60).centerCrop().into(listViewHolder.imgView_brandImage);

        listViewHolder.checkbox_loose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean isChecked = listViewHolder.checkbox_loose.isChecked();
                if (isChecked) {
                    listViewHolder.edtText_loose.setText("0");
                    listViewHolder.edtText_loose.setEnabled(false);
                } else if (!isChecked) {
                    listViewHolder.edtText_loose.setText("");
                    listViewHolder.edtText_loose.setEnabled(true);
                }
            }
        });

        listViewHolder.checkbox_carton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean isChecked = listViewHolder.checkbox_carton.isChecked();
                if (isChecked) {
                    listViewHolder.edtText_carton.setText("0");
                    listViewHolder.edtText_carton.setEnabled(false);
                } else if (!isChecked) {
                    listViewHolder.edtText_carton.setText("");
                    listViewHolder.edtText_carton.setEnabled(true);
                }
            }
        });

        Picasso.get().load(salesSKUArrayResponse.getSKUImage()).fit().into(listViewHolder.imgView_brandImage);
        listViewHolder.txtView_name.setText(salesSKUArrayResponse.getName());
//        listViewHolder.imgView_brandImage.setText(salesSKUArrayResponse.getSKUImage());

        return convertView;

    }

    @Override
    public void onClick(View v) {

    }

    static class ListViewHolder {
        CheckBox checkbox_loose, checkbox_carton;
        TextView txtView_name;
        ImageView imgView_brandImage;
        EditText edtText_loose, edtText_carton;
    }
}