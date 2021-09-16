package com.wareroom.lib_base.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class SimpleAdapter<T> extends BaseAdapter<T, SimpleAdapter.SimpleViewHolder> {
    private @LayoutRes
    int mItemLayout;

    public SimpleAdapter(@LayoutRes int itemLayout) {
        mItemLayout = itemLayout;
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(mItemLayout, parent, false);
        return new SimpleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
        T itemData = null;
        if (mData != null && mData.size() > position) {
            itemData = mData.get(position);
        }
        T finalItemData = itemData;
        holder.itemView.setOnClickListener(v -> {
            onItemClick(finalItemData, position);
        });
        convert(holder, position, itemData);
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public abstract void convert(SimpleViewHolder viewHolder, int position, T itemData);

    public abstract void onItemClick(T data, int position);

}
