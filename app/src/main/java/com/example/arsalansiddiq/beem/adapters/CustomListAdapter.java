package com.example.arsalansiddiq.beem.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.activities.OrderActivity;
import com.example.arsalansiddiq.beem.models.responsemodels.salesresponsemodels.SalesSKUArrayResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomListAdapter extends ArrayAdapter<SalesSKUArrayResponse> implements View.OnClickListener{

    private Context context;
    List<SalesSKUArrayResponse> salesSKUArrayResponse;
    CheckBox checkbox_loose, checkbox_carton;
//    public static ArrayList<ListViewModelCheck> listViewModelCheckArrayList;

//    TextView txtView_name, txtView_brandImage;
//    EditText edtText_loose, edtText_carton;

    public CustomListAdapter(@NonNull Context context, int resource, @NonNull List<SalesSKUArrayResponse> objects) {
        super(context, 0, objects);
        this.context = context;
        this.salesSKUArrayResponse =  objects;
    }

    @Override
    public int getViewTypeCount() {
       return getCount();
//        return salesSKUArrayResponse.size();
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

//        View view = convertView;

        final ListViewHolder listViewHolder;

        SalesSKUArrayResponse salesSKUArrayResponse = getItem(position);

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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


        listViewHolder.edtText_loose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                for (int j = 0; j < OrderActivity.listViewModelCheckArrayList.size(); j++) {
                    int localId = OrderActivity.listViewModelCheckArrayList.get(j).getId();
                    int serverId = salesSKUArrayResponse.getId();
//                    if (OrderActivity.listViewModelCheckArrayList.get(j).getId() == salesSKUArrayResponse.getId()) {
                    if (localId == serverId) {
                        Log.i("Id Comparison: ", OrderActivity.listViewModelCheckArrayList.get(j).getId() + " " +salesSKUArrayResponse.getId());
                        OrderActivity.listViewModelCheckArrayList.get(j).setEditTextView_loose(listViewHolder.edtText_loose.getText().toString());

                    }
//                listViewModelCheckArrayList.get(i).setEditTextView_loose(listViewHolder.edtText_loose.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        listViewHolder.edtText_carton.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                for (int j = 0; j < OrderActivity.listViewModelCheckArrayList.size(); j++) {

                    int localId = OrderActivity.listViewModelCheckArrayList.get(j).getId();
                    int serverId = salesSKUArrayResponse.getId();

//                    if (OrderActivity.listViewModelCheckArrayList.get(j).getId() == salesSKUArrayResponse.getId()) {
                    if (localId == serverId) {
                        OrderActivity.listViewModelCheckArrayList.get(j).setEditTextView_carton(listViewHolder.edtText_carton.getText().toString());
                    }
                }
//                listViewModelCheckArrayList.get(i).setEditTextView_carton(listViewHolder.edtText_carton.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Picasso.get().load(salesSKUArrayResponse.getSKUImage()).into(listViewHolder.imgView_brandImage);
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