package com.wareroom.lib_base.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import static android.view.View.DRAWING_CACHE_QUALITY_HIGH;

public class BitmapUtils {

    public static Bitmap view2Bitmap(View view) {
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();
        Bitmap bp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bp);
        view.draw(canvas);
        canvas.save();
        return bp;
    }

    public static String saveBitmap(Context context, Bitmap bitmap, String fileName) {
        String savePath;
        File file;

//        if (Build.BRAND.equals("Xiaomi")) {// 小米手机
//            savePath = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + bitName;
//        } else {// Meizu 、Oppo
//            savePath = Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + bitName;
//        }
        File externalFileRootDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        do {
            externalFileRootDir = Objects.requireNonNull(externalFileRootDir).getParentFile();
        } while (Objects.requireNonNull(externalFileRootDir).getAbsolutePath().contains("/Android"));

        String saveDir = Objects.requireNonNull(externalFileRootDir).getAbsolutePath();
        savePath = saveDir + "/" + Environment.DIRECTORY_DCIM + "/" + fileName;

        file = new File(savePath);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                out.flush();
                out.close();
                // 插入图库
                MediaStore.Images.Media.insertImage(context.getContentResolver(),
                        file.getAbsolutePath(), fileName, null);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 发送广播，通知刷新图库的显示
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + savePath)));
        return savePath;
    }

}
