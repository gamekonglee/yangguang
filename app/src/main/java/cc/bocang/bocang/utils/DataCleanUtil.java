package cc.bocang.bocang.utils;

import android.content.Context;
import android.os.Environment;
import java.io.File;
import java.math.BigDecimal;

public class DataCleanUtil {
    public static void clearAllCache(Context paramContext) {
        deleteDir(paramContext.getCacheDir());
        if (Environment.getExternalStorageState().equals("mounted"))
            deleteDir(paramContext.getExternalCacheDir());
    }

    private static boolean deleteDir(File paramFile) {
        if (paramFile != null && paramFile.isDirectory()) {
            String[] arrayOfString = paramFile.list();
            for (byte b = 0; b < arrayOfString.length; b++) {
                if (!deleteDir(new File(paramFile, arrayOfString[b])))
                    return false;
            }
        }
        return paramFile.delete();
    }

    public static long getFolderSize(File paramFile) throws Exception {
        long l3;
        long l1 = 0L;
        long l2 = l1;
        try {
            File[] arrayOfFile = paramFile.listFiles();
            for (byte b = 0;; b++) {
                l2 = l1;
                l3 = l1;
                if (b < arrayOfFile.length) {
                    l2 = l1;
                    if (arrayOfFile[b].isDirectory()) {
                        l2 = l1;
                        l1 += getFolderSize(arrayOfFile[b]);
                    } else {
                        l2 = l1;
                        l3 = arrayOfFile[b].length();
                        l1 += l3;
                    }
                } else {
                    return l3;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            l3 = l2;
        }
        return l3;
    }

    public static String getFormatSize(double paramDouble) {
        double d = paramDouble / 1024.0D;
        if (d < 1.0D)
            return "0K";
        paramDouble = d / 1024.0D;
        if (paramDouble < 1.0D) {
            BigDecimal bigDecimal1 = new BigDecimal(Double.toString(d));
            return bigDecimal1.setScale(2, 4).toPlainString() + "KB";
        }
        d = paramDouble / 1024.0D;
        if (d < 1.0D) {
            BigDecimal bigDecimal1 = new BigDecimal(Double.toString(paramDouble));
            return bigDecimal1.setScale(2, 4).toPlainString() + "MB";
        }
        paramDouble = d / 1024.0D;
        if (paramDouble < 1.0D) {
            BigDecimal bigDecimal1 = new BigDecimal(Double.toString(d));
            return bigDecimal1.setScale(2, 4).toPlainString() + "GB";
        }
        BigDecimal bigDecimal = new BigDecimal(paramDouble);
        return bigDecimal.setScale(2, 4).toPlainString() + "TB";
    }

    public static String getTotalCacheSize(Context paramContext) throws Exception {
        long l2 = getFolderSize(paramContext.getCacheDir());
        long l1 = l2;
        if (Environment.getExternalStorageState().equals("mounted"))
            l1 = l2 + getFolderSize(paramContext.getExternalCacheDir());
        return getFormatSize(l1);
    }
}
