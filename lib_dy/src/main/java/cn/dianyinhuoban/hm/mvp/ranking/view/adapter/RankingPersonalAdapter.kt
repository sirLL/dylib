package cn.dianyinhuoban.hm.mvp.ranking.view.adapter

import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.RankItemBean
import coil.load
import com.wareroom.lib_base.ui.adapter.BaseAdapter
import com.wareroom.lib_base.utils.NumberUtils
import com.wareroom.lib_base.widget.image_view.CircleImageView
import kotlinx.android.synthetic.main.item_ranking_list.view.*

class RankingPersonalAdapter : BaseAdapter<RankItemBean, RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var itemView: View? = null
        return when (viewType) {
            ITEM_TYPE_HEADER -> {
                itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.header_ranking_list, parent, false)
                HeaderViewHolder(itemView)
            }
            else -> {
                itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_ranking_list, parent, false)
                ItemViewHolder(itemView)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            //第一、二、三名
            bindHeaderData(holder)
        } else if (holder is ItemViewHolder) {
            var data: RankItemBean? = null
            if (mData.size > position + 2) {
                data = mData[position + 2]
            }

//            if (data != null && data.uid == MMKVUtil.getUserID()) {
//                //自己
//                holder.itemView.setBackgroundColor(
//                    ContextCompat.getColor(
//                        holder.itemView.context,
//                        R.color.color_fff2da
//                    )
//                )
//                holder.itemView.view_bg.background = null
//            } else {
            //其他人
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
            holder.itemView.view_bg.background = if (position == 1) {
                //第一条
                if (mData.size == 4) {
                    ContextCompat.getDrawable(
                        holder.itemView.context,
                        R.drawable.dy_shape_ffffff_radius_6
                    )
                } else {
                    ContextCompat.getDrawable(
                        holder.itemView.context,
                        R.drawable.dy_shape_ffffff_radius_top_6
                    )
                }
            } else if (position + 3 == mData.size) {
                //最后一条
                ContextCompat.getDrawable(
                    holder.itemView.context,
                    R.drawable.dy_shape_ffffff_radius_bottom_6
                )
            } else {
                ContextCompat.getDrawable(holder.itemView.context, R.drawable.dy_shape_ffffff)
            }
//            }
            holder.itemView.tv_no.text = data?.rank ?: ""
            holder.itemView.tv_name.text = if (data == null) {
                "--"
            } else {
                when {
                    TextUtils.isEmpty(data.name) -> {
                        data.username
                    }
                    else -> {
                        data.name
                    }
                }
            }
            holder.itemView.tv_amount.text = NumberUtils.formatMoney(data?.total ?: "")
            holder.itemView.iv_avatar.load(data?.avatar ?: "") {
                crossfade(true)//淡入效果
                allowHardware(false)
                placeholder(R.drawable.img_avatar_def)
                error(R.drawable.img_avatar_def)
            }
        }
    }

    private fun bindHeaderData(holder: HeaderViewHolder) {
        var no1: RankItemBean? = null
        var no2: RankItemBean? = null
        var no3: RankItemBean? = null
        if (mData != null) {
            if (mData.size > 0) {
                no1 = mData[0]
            }
            if (mData.size > 1) {
                no2 = mData[1]
            }
            if (mData.size > 2) {
                no3 = mData[2]
            }
        }

        //第一名
        holder.ivAvatarNo1.load(no1?.avatar ?: "") {
            crossfade(true)//淡入效果
            allowHardware(false)
            placeholder(R.drawable.img_avatar_def)
            error(R.drawable.img_avatar_def)
        }
        holder.tvNameNo1.text = if (no1 == null) {
            "--"
        } else {
            when {
                TextUtils.isEmpty(no1.name) -> {
                    no1.username
                }
                else -> {
                    no1.name
                }
            }
        }
        holder.tvAmountNo1.text = if (no1 == null) {
            "--"
        } else {
            NumberUtils.formatMoney(no1.total)
        }

        //第二名
        holder.ivAvatarNo2.load(no2?.avatar ?: "") {
            crossfade(true)//淡入效果
            allowHardware(false)
            placeholder(R.drawable.img_avatar_def)
            error(R.drawable.img_avatar_def)
        }
        holder.tvNameNo2.text = if (no2 == null) {
            "--"
        } else {
            when {
                TextUtils.isEmpty(no2.name) -> {
                    no2.username
                }
                else -> {
                    no2.name
                }
            }
        }
        holder.tvAmountNo2.text = if (no2 == null) {
            "--"
        } else {
            NumberUtils.formatMoney(no2.total)
        }

        //第三名
        holder.ivAvatarNo3.load(no3?.avatar ?: "") {
            crossfade(true)//淡入效果
            allowHardware(false)
            placeholder(R.drawable.img_avatar_def)
            error(R.drawable.img_avatar_def)
        }
        holder.tvNameNo3.text = if (no3 == null) {
            "--"
        } else {
            when {
                TextUtils.isEmpty(no3.name) -> {
                    no3.username
                }
                else -> {
                    no3.name
                }
            }
        }
        holder.tvAmountNo3.text = if (no3 == null) {
            "--"
        } else {
            NumberUtils.formatMoney(no3.total)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> {
                ITEM_TYPE_HEADER
            }
            else -> {
                ITEM_TYPE_ITEM
            }
        }
    }

    override fun getItemCount(): Int {
        return when {
            (mData == null || mData.size == 0) -> {
                0
            }
            mData.size <= 3 -> {
                1
            }
            else -> {
                mData.size - 2
            }
        }
    }

    internal class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivAvatarNo1 = itemView.findViewById<CircleImageView>(R.id.iv_avatar_center)
        val tvNameNo1 = itemView.findViewById<TextView>(R.id.tv_name_center)
        val tvAmountNo1 = itemView.findViewById<TextView>(R.id.tv_amount_center)

        val ivAvatarNo2 = itemView.findViewById<CircleImageView>(R.id.iv_avatar_left)
        val tvNameNo2 = itemView.findViewById<TextView>(R.id.tv_name_left)
        val tvAmountNo2 = itemView.findViewById<TextView>(R.id.tv_amount_left)

        val ivAvatarNo3 = itemView.findViewById<CircleImageView>(R.id.iv_avatar_right)
        val tvNameNo3 = itemView.findViewById<TextView>(R.id.tv_name_right)
        val tvAmountNo3 = itemView.findViewById<TextView>(R.id.tv_amount_right)
    }

    internal class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    companion object ItemType {
        const val ITEM_TYPE_HEADER = 1
        const val ITEM_TYPE_ITEM = 2
    }
}