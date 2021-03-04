package com.example.printlibrary.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by jc on 2017/11/19.
 */

public class URIUtil {

    /**
     * 根据uri获取文件路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getFilePathByUri(final Context context, final Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public static Bitmap getBitmapByUri(Context context, Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(
                    context.getContentResolver(), uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 需要加载的图片可能是大图，我们需要对其进行合适的缩小处理
     *
     * @param imageUri
     */
    public static Bitmap getSrcImage(Context context, Uri imageUri, int width, int height) {
        try {
            BitmapFactory.Options ops = new BitmapFactory.Options();
            ops.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeStream(context.getContentResolver()
                    .openInputStream(imageUri), null, ops);
            int wRatio = (int) Math.ceil(ops.outWidth / (float) width);//400为可调参数
            int hRatio = (int) Math.ceil(ops.outHeight / (float) height);

            if (wRatio > 1 && hRatio > 1) {
                if (wRatio > hRatio) {
                    ops.inSampleSize = wRatio;
                } else {
                    ops.inSampleSize = hRatio;
                }
            }

            ops.inJustDecodeBounds = false;
            bmp = BitmapFactory.decodeStream(context.getContentResolver()
                    .openInputStream(imageUri), null, ops);

            return bmp;

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e(context.getClass().getName(), e.getMessage());
        }

        return null;
    }
}

