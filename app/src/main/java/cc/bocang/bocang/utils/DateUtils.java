package cc.bocang.bocang.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String getStrTime(String paramString) { return (new SimpleDateFormat("yyyy-MM-dd HH:mm")).format(new Date(1000L * Long.valueOf(paramString).longValue())); }

    public static String getStrTime02(String paramString) { return (new SimpleDateFormat("yyyy-MM-dd")).format(new Date(1000L * Long.valueOf(paramString).longValue())); }

    public static String getStrTime03(String paramString) { return (new SimpleDateFormat("HH:mm")).format(new Date(1000L * Long.valueOf(paramString).longValue())); }

    public static long getTimeStamp(String paramString1, String paramString2) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(paramString2);
        try {
            return simpleDateFormat.parse(paramString1).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
    }
}
