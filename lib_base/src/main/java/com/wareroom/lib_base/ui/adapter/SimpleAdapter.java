package com.wareroom.lib_base.ui.adapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        private SparseArray<View> mViews;
        private View mConvertView;
        public SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            mConvertView = itemView;
            mViews = new SparseArray();
        }

        public <T extends View> T getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        public void setText(int viewId, String text) {
            TextView tv = getView(viewId);
            tv.setText(text);
        }

        public void setTextColor(int viewId, int textColor) {
            TextView view = getView(viewId);
            view.setTextColor(textColor);
        }
    }

    public abstract void convert(SimpleViewHolder viewHolder, int position, T itemData);

    public abstract void onItemClick(T data, int position);

}
