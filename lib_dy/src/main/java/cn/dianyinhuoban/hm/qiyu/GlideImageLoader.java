package cn.dianyinhuoban.hm.qiyu;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.qiyukf.unicorn.api.ImageLoaderListener;
import com.qiyukf.unicorn.api.UnicornImageLoader;

import java.util.concurrent.ExecutionException;

/**
 * Created by zhoujianghua on 2016/12/27.
 */

public class GlideImageLoader implements UnicornImageLoader {
    private Context context;

    public GlideImageLoader(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public Bitmap loadImageSync(String uri, int width, int height) {
        Log.d("GlideImageLoader", "loadImageSync: "+uri);
        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(context).asBitmap()
                    .load(uri)
                    .into(width, height).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public void loadImage(String uri, int width, int height, final ImageLoaderListener listener) {
        if (width <= 0) {
            width = Integer.MIN_VALUE;
        }

        if (height <= 0) {
            height = Integer.MIN_VALUE;
        }

        Glide.with(context)
                .asBitmap()
                .load(uri)
                .load(uri).into(new SimpleTarget<Bitmap>() {

            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                if (listener != null) {
                    listener.onLoadComplete(resource);
                }
            }
        });

//        Glide.with(context).
//                asBitmap()
//                .load(uri)
//                .into(new CustomTarget<Bitmap>(width, height) {
//
//            @Override
//            public void onLoadStarted(@Nullable Drawable placeholder) {
//
//            }
//
//            @Override
//            public void onLoadFailed(@Nullable Drawable errorDrawable) {
//
//            }
//
//            @Override
//            public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
//
//            }
//
//            @Override
//            public void onLoadCleared(@Nullable Drawable placeholder) {
//
//            }
//        });
    }
}
