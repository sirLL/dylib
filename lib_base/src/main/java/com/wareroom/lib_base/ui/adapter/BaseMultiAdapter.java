package com.wareroom.lib_base.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMultiAdapter extends RecyclerView.Adapter<BaseMultiAdapter.BaseMultiViewHolder> {
    protected List<Object> mData;
    protected List<ItemType> mItemTypeList;

    public void setData(List<Object> data) {
        if (mData == null) {
            mData = new ArrayList<>();
        } else {
            mData.clear();
        }
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                Object dataBean = data.get(i);
                if (dataBean != null) {
                    mData.add(dataBean);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void appendData(List<Object> data) {
        if (mData == null) {
            mData = new ArrayList<>();
        }

        if (data != null && data.size() > 0) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseMultiAdapter.BaseMultiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int itemViewLayout = 0;
        if (mItemTypeList != null && mItemTypeList.size() > 0) {
            for (ItemType itemType : mItemTypeList) {
                if (itemType != null && viewType == itemType.viewType) {
                    itemViewLayout = itemType.itemViewLayout;
                    break;
                }
            }
        }
        if (itemViewLayout > 0) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(itemViewLayout, parent, false);
            return new BaseMultiViewHolder(itemView);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseMultiAdapter.BaseMultiViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mData != null && mData.size() > position) {
            Object data = mData.get(position);
            if (data != null && mItemTypeList != null && mItemTypeList.size() > 0) {
                for (ItemType itemType : mItemTypeList) {
                    if (itemType.dataClass.getName().equals(data.getClass().getName())) {
                        return itemType.viewType;
                    }
                }
            }
        }
        return super.getItemViewType(position);
    }

    public class ItemType {
        private int viewType;
        private int itemViewLayout;
        private Class<?> dataClass;

        public ItemType(int viewType, int itemViewLayout, Class<?> dataClass) {
            this.viewType = viewType;
            this.itemViewLayout = itemViewLayout;
            this.dataClass = dataClass;
        }

        public int getViewType() {
            return viewType;
        }

        public void setViewType(int viewType) {
            this.viewType = viewType;
        }

        public int getItemViewLayout() {
            return itemViewLayout;
        }

        public void setItemViewLayout(int itemViewLayout) {
            this.itemViewLayout = itemViewLayout;
        }

        public Class<?> getDataClass() {
            return dataClass;
        }

        public void setDataClass(Class<?> dataClass) {
            this.dataClass = dataClass;
        }
    }

    public class BaseMultiViewHolder extends RecyclerView.ViewHolder {
        public BaseMultiViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
