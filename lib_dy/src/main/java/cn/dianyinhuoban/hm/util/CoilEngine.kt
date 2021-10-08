package cn.dianyinhuoban.hm.util

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.ImageView
import coil.load
import com.luck.picture.lib.engine.ImageEngine
import com.luck.picture.lib.listener.OnImageCompleteCallback
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView
import java.io.File

class CoilEngine private constructor(): ImageEngine {

    companion object {
        val instance: CoilEngine by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CoilEngine()
        }
    }


    override fun loadImage(context: Context, url: String, imageView: ImageView) {
        Log.d("IMAGE",url)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            imageView.load(url)
        } else {
            imageView.load(File(url))
        }


    }

    override fun loadImage(
        context: Context,
        url: String,
        imageView: ImageView,
        longImageView: SubsamplingScaleImageView?,
        callback: OnImageCompleteCallback?
    ) {
        Log.d("IMAGE",url)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            imageView.load(url)
        } else {
            imageView.load(File(url))
        }

    }

    override fun loadImage(
        context: Context,
        url: String,
        imageView: ImageView,
        longImageView: SubsamplingScaleImageView?
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            imageView.load(url)
        } else {
            imageView.load(File(url))
        }
    }

    override fun loadFolderImage(context: Context, url: String, imageView: ImageView) {
        Log.d("IMAGE",url)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            imageView.load(url)
        } else {
            imageView.load(File(url))
        }
    }

    override fun loadAsGifImage(context: Context, url: String, imageView: ImageView) {
        Log.d("IMAGE",url)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            imageView.load(url)
        } else {
            imageView.load(File(url))
        }
    }

    override fun loadGridImage(context: Context, url: String, imageView: ImageView) {
        Log.d("IMAGE",url)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            imageView.load(url)
        } else {
            imageView.load(File(url))
        }
    }

}