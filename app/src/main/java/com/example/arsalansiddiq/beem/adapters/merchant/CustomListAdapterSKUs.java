package com.example.arsalansiddiq.beem.adapters.merchant;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.activities.PriceUpdateActivity;
import com.example.arsalansiddiq.beem.models.responsemodels.merchant.competitionsku.DatumMerchant;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jellani on 12/2/2018.
 */

public class CustomListAdapterSKUs extends ArrayAdapter<DatumMerchant> {

        private Context context;
        List<DatumMerchant> datumMerchantList;
        String tag;

        public CustomListAdapterSKUs(@NonNull Context context, int resource, @NonNull List<DatumMerchant> objects,
                                     String tag) {
            super(context, 0, objects);
            this.context = context;
            this.tag = tag;
            this.datumMerchantList =  objects;
        }

    @Override
    public int getCount() {
        return datumMerchantList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Nullable
    @Override
    public DatumMerchant getItem(int position) {
        return datumMerchantList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ListViewHolderMerchant listViewHolderMerchant;

        DatumMerchant datum = getItem(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            listViewHolderMerchant = new ListViewHolderMerchant();

            convertView = layoutInflater.from(getContext()).inflate(R.layout.list_child_merchant_skus, parent, false);
            listViewHolderMerchant.imgView_brandImageMerchant = convertView.findViewById(R.id.imgView_brandImageMerchant);

            listViewHolderMerchant.txtView_priceMerchant = convertView.findViewById(R.id.txtView_priceMerchant);
            listViewHolderMerchant.edtText_priceMerchant = convertView.findViewById(R.id.edtText_priceMerchant);

            listViewHolderMerchant.txtView_stockMerchant = convertView.findViewById(R.id.txtView_stockMerchant);
            listViewHolderMerchant.edtText_stockMerchant = convertView.findViewById(R.id.edtText_stockMerchant);

            if (tag.equals("price")) {
                listViewHolderMerchant.txtView_stockMerchant.setVisibility(View.GONE);
                listViewHolderMerchant.edtText_stockMerchant.setVisibility(View.GONE);
            } else {
                listViewHolderMerchant.txtView_priceMerchant.setVisibility(View.GONE);
                listViewHolderMerchant.edtText_priceMerchant.setVisibility(View.GONE);
            }

            convertView.setTag(listViewHolderMerchant);
        } else {
            listViewHolderMerchant = (CustomListAdapterSKUs.ListViewHolderMerchant) convertView.getTag();
        }

        listViewHolderMerchant.edtText_priceMerchant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                for (int j = 0; j < PriceUpdateActivity.listViewModelCheckMerchantArrayList.size(); j++) {
                    int localId = PriceUpdateActivity.listViewModelCheckMerchantArrayList.get(j).getId();
                    int serverId = datum.getId();
                    if (localId == serverId) {
                        Log.i("Id Comparison: ", PriceUpdateActivity.listViewModelCheckMerchantArrayList.get(j).getId() + " " +datum.getId());
                        PriceUpdateActivity.listViewModelCheckMerchantArrayList.get(j).setedtText_priceMerchant(listViewHolderMerchant.edtText_priceMerchant.getText().toString());
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        listViewHolderMerchant.edtText_stockMerchant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Picasso.get().load(datum.getSKUImage()).into(listViewHolderMerchant.imgView_brandImageMerchant);

        return convertView;
    }

    static class ListViewHolderMerchant {
        TextView txtView_priceMerchant, txtView_stockMerchant;
        ImageView imgView_brandImageMerchant;
        EditText edtText_priceMerchant, edtText_stockMerchant;
    }
}