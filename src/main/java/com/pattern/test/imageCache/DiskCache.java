package com.pattern.test.imageCache;

import android.content.Context;
import android.graphics.Bitmap;

import com.pattern.test.utils.ImageLoaderUitls;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 本地sdcard缓存
 * Created by frog on 7/16/16.
 */
public class DiskCache implements IImageCache {
    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 50; //磁盘缓存最大缓存大小
    private static final int DISK_CACHE_INDEX = 0;//磁盘缓存获取的角标始终为1
    private DiskLruCache mDiskLruCache;
    private ImageResizer mImageResizer = new ImageResizer();//处理图片压缩

    public DiskCache(Context mContext) {
        File diskCacheDir = ImageLoaderUitls.getDiskCacheDir(mContext, "bitmap");
        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdirs();
        }

        if (ImageLoaderUitls.getUsableSpace(diskCacheDir) > DISK_CACHE_SIZE) {
            try {
                mDiskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void put(String url, Bitmap bitmap) {
        try {
            if (mDiskLruCache == null) {
                return;
            }
            String key = ImageLoaderUitls.hashKeyFromUrl(url);
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
                boolean flag = bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                if (flag) {
                    editor.commit();
                } else {
                    editor.abort();
                }
                mDiskLruCache.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Bitmap get(String url, int reqWidth, int reqHeight) {
        if (mDiskLruCache == null) {
            return null;
        }

        Bitmap bitmap = null;
        try {
            String key = ImageLoaderUitls.hashKeyFromUrl(url);
            DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
            if (snapShot != null) {
                FileInputStream fileInputStream = (FileInputStream) snapShot.getInputStream(DISK_CACHE_INDEX);
                FileDescriptor fileDescriptor = fileInputStream.getFD();
                bitmap = mImageResizer.decodeSampledBitmapFromFileDescriptor(fileDescriptor, reqWidth, reqHeight);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
