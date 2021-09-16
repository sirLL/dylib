package cn.dianyinhuoban.hm.mvp.home.view.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wareroom.lib_base.ui.adapter.BaseAdapter;

public class MyAdapter extends BaseAdapter<HomeItemBean, RecyclerView.ViewHolder> {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    class PersonalViewHolder extends RecyclerView.ViewHolder{
        public PersonalViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
