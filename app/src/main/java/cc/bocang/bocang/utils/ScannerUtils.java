package cc.bocang.bocang.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;

public class ScannerUtils {

    private static File file;
    private static FileOutputStream fileOutputStream;

    private static void ScannerByMedia(Context paramContext, String paramString) {
        MediaScannerConnection.scanFile(paramContext, new String[] { paramString }, null, null);
        Log.v("TAG", "media scanner completed");
    }

    private static void ScannerByReceiver(Context paramContext, String paramString) {
        paramContext.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse("file://" + paramString)));
        Log.v("TAG", "receiver scanner completed");
    }

    public static String saveImageToGallery(Context paramContext, Bitmap paramBitmap, ScannerType paramScannerType) {
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "yangguang");
        if (!file.exists())
            file.mkdirs();
        file = new File(file, System.currentTimeMillis() + ".jpg");
        try {
            fileOutputStream = new FileOutputStream(file);
            paramBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            return file.getAbsolutePath();
        } catch (Exception exception) {
            return file.getAbsolutePath();
        } finally {
            Object object = null;
            if (paramScannerType == ScannerType.RECEIVER) {
                ScannerByReceiver(paramContext, file.getAbsolutePath());
            } else if (paramScannerType == ScannerType.MEDIA) {
                ScannerByMedia(paramContext, file.getAbsolutePath());
            }
            if (paramBitmap != null && !paramBitmap.isRecycled())
                System.gc();
        }
    }

    public static String saveImageToGallery02(Context paramContext, Bitmap paramBitmap, ScannerType paramScannerType) {
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "yangguang");
        if (!file.exists())
            file.mkdirs();
        file = new File(file, System.currentTimeMillis() + ".jpg");
        try {
            fileOutputStream = new FileOutputStream(file);
            paramBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            return file.getAbsolutePath();
        } catch (Exception exception) {
            return file.getAbsolutePath();
        } finally {
            Object object = null;
            if (paramScannerType == ScannerType.RECEIVER) {
                ScannerByReceiver(paramContext, file.getAbsolutePath());
            } else if (paramScannerType == ScannerType.MEDIA) {
                ScannerByMedia(paramContext, file.getAbsolutePath());
            }
            if (!paramBitmap.isRecycled())
                System.gc();
        }
    }

    public enum ScannerType {
        MEDIA, RECEIVER;

        static  {
//            MEDIA = new ScannerType("MEDIA", 1);
//            $VALUES = new ScannerType[] { RECEIVER, MEDIA };
        }
    }
}
