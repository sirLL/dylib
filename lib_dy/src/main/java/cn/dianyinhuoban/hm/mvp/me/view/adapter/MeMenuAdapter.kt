package cn.dianyinhuoban.hm.mvp.me.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.MeMenuBean
import com.wareroom.lib_base.ui.adapter.BaseAdapter
import kotlinx.android.synthetic.main.dy_item_me_menu.view.*

class MeMenuAdapter : BaseAdapter<MeMenuBean, MeMenuAdapter.MenuViewHolder>() {
    var onMenuClickListener: OnMenuClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.dy_item_me_menu, parent, false)
        return MenuViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        var menuBean: MeMenuBean? = null
        if (mData != null && mData.size > position) {
            menuBean = mData[position]
        }
        if (menuBean != null) {
            holder.itemView.tv_menu.text = menuBean.name
            holder.itemView.tv_menu.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                ContextCompat.getDrawable(holder.itemView.context, menuBean.icon),
                null,
                null
            )
        } else {
            holder.itemView.tv_menu.text = ""
            holder.itemView.tv_menu.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                null,
                null
            )
        }
        holder.itemView.setOnClickListener {
            if (menuBean == null) return@setOnClickListener
            onMenuClickListener?.onMenuClick(menuBean)
        }
    }

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    interface OnMenuClickListener {
        fun onMenuClick(menuBean: MeMenuBean)
    }

}