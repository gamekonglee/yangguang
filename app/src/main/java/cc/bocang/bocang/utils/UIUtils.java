package cc.bocang.bocang.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import cc.bocang.bocang.R;
import cc.bocang.bocang.global.MyApplication;
import java.io.ByteArrayOutputStream;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class UIUtils {
    public static byte[] Bitmap2Bytes(Bitmap paramBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        paramBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @TargetApi(21)
    public static Bitmap bytes2bitmap(byte[] paramArrayOfByte, BitmapFactory.Options paramOptions, Activity paramActivity, Size paramSize) {
        paramActivity = null;
        YuvImage yuvImage = new YuvImage(paramArrayOfByte, 17, paramSize.getWidth(), paramSize.getHeight(), null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, paramSize.getWidth(), paramSize.getHeight()), 80, byteArrayOutputStream);
        byte[] arrayOfByte = byteArrayOutputStream.toByteArray();
        Activity activity = paramActivity;
        if (arrayOfByte != null) {
            if (paramOptions != null)
                return BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length, paramOptions);
        } else {
            return null;
        }
        return BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length);
    }

    public static Bitmap converBitmap(Bitmap paramBitmap) {
        int i = paramBitmap.getWidth();
        int j = paramBitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(-1.0F, 1.0F);
        return Bitmap.createBitmap(paramBitmap, 0, 0, i, j, matrix, true);
    }

    public static void diallPhone(Context paramContext, String paramString) {
        Intent intent = new Intent("android.intent.action.DIAL");
        intent.setData(Uri.parse("tel:" + paramString));
        paramContext.startActivity(intent);
    }

    public static int dip2PX(int paramInt) {
        float f = (getResources().getDisplayMetrics()).density;
        return (int)(paramInt * f + 0.5F);
    }

    public static int dip2PX(Context paramContext, int paramInt) {
        float f = (paramContext.getResources().getDisplayMetrics()).density;
        return (int)(paramInt * f + 0.5F);
    }

    public static Bitmap drawableToBitmap(int paramInt1, int paramInt2, Drawable paramDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(paramInt1, paramInt2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        paramDrawable.setBounds(0, 0, paramInt1, paramInt2);
        paramDrawable.draw(canvas);
        return bitmap;
    }

    public static Context getContext() { return MyApplication.getInstance(); }

    public static String getInterfaceLocalmac() {
        String str3;
        String str1 = "";
        String str2 = str1;
        try {
            Enumeration enumeration = NetworkInterface.getNetworkInterfaces();
            while (true) {
                str2 = str1;
                str3 = str1;
                if (enumeration.hasMoreElements()) {
                    str2 = str1;
                    byte[] arrayOfByte = ((NetworkInterface)enumeration.nextElement()).getHardwareAddress();
                    if (arrayOfByte != null) {
                        str2 = str1;
                        if (arrayOfByte.length != 0) {
                            str2 = str1;
                            StringBuilder stringBuilder = new StringBuilder();
                            str2 = str1;
                            int i = arrayOfByte.length;
                            for (byte b = 0; b < i; b++) {
                                stringBuilder.append((str2 = str1).format("%02X:", new Object[] { Byte.valueOf(arrayOfByte[b]) }));
                            }
                            str2 = str1;
                            if (stringBuilder.length() > 0) {
                                str2 = str1;
                                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                            }
                            str2 = str1;
                            str1 = stringBuilder.toString();
                        }
                    }
                    continue;
                }
                break;
            }
        } catch (SocketException e) {
            e.printStackTrace();
            str3 = str2;
        }
        return str3;
    }

    public static String getLocalMac(Context paramContext) {
        String str2 = ((WifiManager)paramContext.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getMacAddress();
        String str1 = str2;
        if (str2.equals("02:00:00:00:00:00"))
            str1 = getInterfaceLocalmac();
        return str1;
    }

    public static Resources getResources() { return getContext().getResources(); }

    public static int getScreenHeight(Activity paramActivity) {
        WindowManager windowManager = paramActivity.getWindowManager();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int i = displayMetrics.widthPixels;
        return displayMetrics.heightPixels;
    }

    @TargetApi(17)
    public static int getScreenWidth(Activity paramActivity) {
        if (paramActivity == null || paramActivity.isFinishing() || paramActivity.isDestroyed())
            return dip2PX(480);
        WindowManager windowManager = paramActivity.getWindowManager();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int i = displayMetrics.widthPixels;
        int j = displayMetrics.heightPixels;
        return i;
    }

    public static String getString(int paramInt) { return getResources().getString(paramInt); }

    public static String getString(int paramInt, Object... paramVarArgs) { return getResources().getString(paramInt, paramVarArgs); }

    public static String[] getStringArr(int paramInt) { return getResources().getStringArray(paramInt); }

    public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    public static int getTvWidth(Context paramContext, TextView paramTextView, String paramString) { return getTextWidth(paramTextView.getPaint(), paramString) + dip2PX(20); }

    public static String getVerName(Context paramContext) {
        try {
            return (paramContext.getPackageManager().getPackageInfo(paramContext.getPackageName(), 0)).versionName;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getpackageName() { return getContext().getPackageName(); }

    @TargetApi(16)
    public static int initGridViewHeight(GridView paramGridView) {
        if (paramGridView == null)
            return 0;
        ListAdapter listAdapter = paramGridView.getAdapter();
        if (listAdapter == null)
            return 0;
        int k = listAdapter.getCount();
        int j = 0;
        int i = k;
        if (k % 2 != 0)
            i = k + 1;
        int m = i / 2;
        k = 0;
        i = j;
        for (j = k; j < m; j++) {
            View view = listAdapter.getView(j, null, paramGridView);
            view.measure(0, 0);
            k = i + view.getMeasuredHeight();
            i = k;
            if (j != m - 1)
                i = k + paramGridView.getVerticalSpacing();
        }
        ViewGroup.LayoutParams layoutParams = paramGridView.getLayoutParams();
        layoutParams.height = i;
        paramGridView.setLayoutParams(layoutParams);
        paramGridView.requestLayout();
        return i;
    }

    @TargetApi(16)
    public static int initGridViewHeight(GridView paramGridView, int paramInt) {
        if (paramGridView == null)
            return 0;
        ListAdapter listAdapter = paramGridView.getAdapter();
        if (listAdapter == null)
            return 0;
        int i = listAdapter.getCount();
        int k = 0;
        int j = i;
        if (i % paramInt != 0)
            while (true) {
                j = i;
                if (i % paramInt != 0) {
                    i++;
                    continue;
                }
                break;
            }
        int m = j / paramInt;
        i = 0;
        paramInt = k;
        while (i < m) {
            View view = listAdapter.getView(i, null, paramGridView);
            view.measure(0, 0);
            j = paramInt + view.getMeasuredHeight();
            paramInt = j;
            if (i != m - 1)
                paramInt = j + paramGridView.getVerticalSpacing();
            i++;
        }
        System.out.println("total:" + paramInt);
        ViewGroup.LayoutParams layoutParams = paramGridView.getLayoutParams();
        layoutParams.height = paramInt;
        paramGridView.setLayoutParams(layoutParams);
        paramGridView.requestLayout();
        return paramInt;
    }

    public static int initListViewHeight(ListView paramListView) {
        if (paramListView == null)
            return 0;
        ListAdapter listAdapter = paramListView.getAdapter();
        if (listAdapter == null)
            return 0;
        int j = listAdapter.getCount();
        int i = 0;
        for (byte b = 0; b < j; b++) {
            View view = listAdapter.getView(b, null, paramListView);
            view.measure(0, 0);
            int k = i + view.getMeasuredHeight();
            i = k;
            if (b != j - 1)
                i = k + paramListView.getDividerHeight();
        }
        ViewGroup.LayoutParams layoutParams = paramListView.getLayoutParams();
        layoutParams.height = i;
        paramListView.setLayoutParams(layoutParams);
        paramListView.requestLayout();
        return i;
    }

    public static int initListViewHeight(ListView paramListView, int paramInt) {
        if (paramListView == null)
            return 0;
        ListAdapter listAdapter = paramListView.getAdapter();
        if (listAdapter == null)
            return 0;
        int j = listAdapter.getCount();
        int i = 0;
        for (byte b = 0; b < j; b++) {
            listAdapter.getView(b, null, paramListView).measure(0, 0);
            int k = i + paramInt;
            i = k;
            if (b != j - 1)
                i = k + paramListView.getDividerHeight();
        }
        ViewGroup.LayoutParams layoutParams = paramListView.getLayoutParams();
        layoutParams.height = i;
        paramListView.setLayoutParams(layoutParams);
        paramListView.requestLayout();
        return i;
    }

//    @TargetApi(17)
//    public static boolean isValidContext(Context paramContext) {
//        Activity activity = (Activity)paramContext;
//        return !(activity.isDestroyed() || activity.isFinishing());
//    }
//
//
//    public static void setImmersionStateMode(Activity paramActivity) {
//        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT != 21) {
//            paramActivity.getWindow().addFlags(67108864);
//            return;
//        }
//        if (Build.VERSION.SDK_INT == 21) {
//            Window window = paramActivity.getWindow();
//            window.clearFlags(201326592);
//            window.getDecorView().setSystemUiVisibility(1280);
//            window.addFlags(-2147483648);
//            window.setStatusBarColor(0);
//            window.setNavigationBarColor(0);
//            return;
//        }
//    }

    public static Dialog showBottomInDialog(Activity paramActivity, int paramInt1, int paramInt2) {
        Dialog dialog = new Dialog(paramActivity, R.style.customDialog);
        dialog.setContentView(paramInt1);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        WindowManager windowManager = paramActivity.getWindowManager();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        layoutParams.width = displayMetrics.widthPixels;
        layoutParams.height = paramInt2;
        layoutParams.x = 0;
        window.setAttributes(layoutParams);
        dialog.show();
        return dialog;
    }

    public static void showShareDialog(final Activity activity, final Bitmap mBitmap, final String path, final String mLocalPath) {
        final String title = "来自 " + UIUtils.getString(R.string.app_name) + " App的分享";
        final Dialog dialog=UIUtils.showBottomInDialog(activity, R.layout.share_dialog,UIUtils.dip2PX(205));
        TextView tv_cancel= (TextView) dialog.findViewById(R.id.tv_cancel);
        LinearLayout ll_wx= (LinearLayout) dialog.findViewById(R.id.ll_wx);
        LinearLayout ll_pyq= (LinearLayout) dialog.findViewById(R.id.ll_pyq);
        LinearLayout ll_qq= (LinearLayout) dialog.findViewById(R.id.ll_qq);
        LinearLayout ll_link= (LinearLayout) dialog.findViewById(R.id.ll_link);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ll_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareUtil.shareWxPic(activity,title,mBitmap,true);
                dialog.dismiss();
            }
        });
        ll_pyq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareUtil.shareWxPic(activity,title,mBitmap,false);
                dialog.dismiss();
            }
        });
        ll_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareUtil.shareQQLocalpic(activity,mLocalPath ,title);
                dialog.dismiss();
            }
        });
        ll_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(path);
//                MyToast.show(activity,"链接复制成功！");
                dialog.dismiss();
            }
        });
    }
//
    public static void showShareDialogWithThumb(final Activity activity, final String name, final String path, final Bitmap thumb, final String shareimg) {
        final String title = "来自 " + getString(R.string.app_name) + " App的分享";
        final Dialog dialog = showBottomInDialog(activity, R.layout.share_dialog, dip2PX(205));
        TextView textView = (TextView)dialog.findViewById(R.id.tv_cancel);
        LinearLayout linearLayout1 = (LinearLayout)dialog.findViewById(R.id.ll_wx);
        LinearLayout linearLayout2 = (LinearLayout)dialog.findViewById(R.id.ll_pyq);
        LinearLayout linearLayout3 = (LinearLayout)dialog.findViewById(R.id.ll_qq);
        LinearLayout linearLayout4 = (LinearLayout)dialog.findViewById(R.id.ll_link);
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) { dialog.dismiss(); }
        });
        linearLayout1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                String str;
                if (name == null) {
                    str = title;
                } else {
                    str = name;
                }
                ShareUtil.shareWx(activity, str, path, thumb);
                dialog.dismiss();
            }
        });
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                String str;
                if (name == null) {
                    str = title;
                } else {
                    str = name;
                }
                ShareUtil.sharePyq(activity, str, path, thumb);
                dialog.dismiss();
            }
        });
        linearLayout3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                ShareUtil.shareQQ(activity, title, path, shareimg);
                dialog.dismiss();
            }
        });
        linearLayout4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                ((ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE)).setText(path);
                dialog.dismiss();
            }
        });
    }

    public static void showSystemStopDialog(Activity paramActivity, View paramView, String paramString1, String paramString2) {}

    public static int sp2px(Context paramContext, float paramFloat) { return (int)(paramFloat * (paramContext.getResources().getDisplayMetrics()).scaledDensity + 0.5F); }

    public static Bitmap view2Bitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());view.buildDrawingCache();Bitmap bitmap=view.getDrawingCache();return bitmap;
    }
}
