package com.pichu.nanamare.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.Objects;

public class BitmapUtils {
    private static final String TAG = BitmapUtils.class.getSimpleName();

    public static Bitmap getScreenShot(int width, int height) {
        IntBuffer pixelBuffer = IntBuffer.allocate(width * height);
        GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE,
                pixelBuffer);
        int[] pixelMirroredArray = new int[width * height];
        int[] pixelArray = pixelBuffer.array();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixelMirroredArray[(height - i - 1) * width + j] = pixelArray[i * width + j];
            }
        }
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    }

    private static void saveIntBufferAsBitmap(IntBuffer buf, String filePath, int width, int height) {
        mkDirs(filePath);
        final int[] pixelMirroredArray = new int[width * height];
        Log.d(TAG, "Creating " + filePath);
        BufferedOutputStream bos = null;
        try {
            int[] pixelArray = buf.array();
            // rotate 180 deg with x axis because y is reversed
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    pixelMirroredArray[(height - i - 1) * width + j] = pixelArray[i * width + j];
                }
            }
            bos = new BufferedOutputStream(new FileOutputStream(filePath));
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bmp.copyPixelsFromBuffer(IntBuffer.wrap(pixelMirroredArray));
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, bos);
            bmp.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Bitmap loadBitmapFromFile(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        return BitmapFactory.decodeFile(filePath);
    }

    public static Bitmap loadBitmapFromAssets(Context context, String filePath) {
        InputStream inputStream = null;
        try {
            inputStream = context.getResources().getAssets().open(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (inputStream == null) return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        return BitmapFactory.decodeStream(inputStream);
    }

    public static Bitmap loadBitmapFromRaw(Context context, int resourceId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        return BitmapFactory.decodeResource(context.getResources(), resourceId, options);
    }

    public static void mkDirs(String fileName) {
        File file = new File(fileName);
        if (!Objects.requireNonNull(file.getParentFile()).exists()) {
            file.getParentFile().mkdirs();
        }
    }

    public static void savePNGBitmap(final Bitmap bitmap, final String outputFilePath) {
        mkDirs(outputFilePath);
        try {
            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(outputFilePath));
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap loadARGBBitmapFromRGBAByteArray(byte[] bytes, int width, int height, boolean ignoreAlphaChannel) {
        int totSize = width * height;
        if (bytes.length != totSize * 4) throw new RuntimeException("Illegal argument");
        int[] histogram = new int[totSize];
        for (int i = 0, pos = 0; i < totSize; i++, pos += 4) {
            histogram[i] = 0;
            histogram[i] |= (((int) bytes[pos + 0]) & 0xff) << 16;
            histogram[i] |= (((int) bytes[pos + 1]) & 0xff) << 8;
            histogram[i] |= (((int) bytes[pos + 2]) & 0xff) << 0;
            histogram[i] |= ignoreAlphaChannel ? 0xff000000 : (((int) bytes[pos + 3]) & 0xff) << 24;
        }
        return Bitmap.createBitmap(histogram, width, height, Bitmap.Config.ARGB_8888);
    }

}
