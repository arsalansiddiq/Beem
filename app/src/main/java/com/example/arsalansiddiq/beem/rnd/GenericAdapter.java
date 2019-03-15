package com.example.arsalansiddiq.beem.rnd;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericAdapter<E> extends RecyclerView.Adapter <RecyclerView.ViewHolder>{

    private Context context;
    private List<E> arrayList;

    public GenericAdapter(Context context, List<E> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public abstract RecyclerView.ViewHolder setViewHolder(ViewGroup parent);

    public abstract void onBindData(RecyclerView.ViewHolder holder, E val);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = setViewHolder(parent);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBindData(holder,arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void addItems( ArrayList<E> savedCardItemz){
        arrayList = savedCardItemz;
        this.notifyDataSetChanged();
    }

    public E getItem(int position){
        return arrayList.get(position);
    }


}
