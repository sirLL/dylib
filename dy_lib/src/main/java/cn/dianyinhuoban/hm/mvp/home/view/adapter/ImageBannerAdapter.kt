package cn.dianyinhuoban.hm.mvp.home.view.adapter

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import cn.dianyinhuoban.hm.mvp.WebActivity
import cn.dianyinhuoban.hm.mvp.bean.BannerBean
import coil.load
import com.youth.banner.adapter.BannerAdapter

class ImageBannerAdapter(data: List<BannerBean>) :
    BannerAdapter<BannerBean, ImageBannerAdapter.BannerViewHolder>(data) {

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerViewHolder {
        val imageView = ImageView(parent!!.context)
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return BannerViewHolder(imageView)
    }

    override fun onBindView(
        holder: BannerViewHolder?,
        data: BannerBean?,
        position: Int,
        size: Int
    ) {
        holder?.imageView?.load(data?.thumb ?: "") {
            crossfade(true)
        }
        holder?.imageView?.setOnClickListener {
            WebActivity.openWebActivity(it.context, data?.title ?: "", data?.url ?: "")
        }
    }

    class BannerViewHolder(view: ImageView) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view
    }
}