package cc.bocang.bocang.net;

import cc.bocang.bocang.global.Constant;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OkHttpUtils {
    public static void getBrandList(Callback paramCallback) {
        OkHttpClient okHttpClient = getOkHttpInstance();
        FormBody formBody = (new FormBody.Builder()).add("key", "1").build();
        okHttpClient.newCall((new Request.Builder()).post(formBody).url("http://yangguang.bocang.cc/Interface/Category_list").build()).enqueue(paramCallback);
    }

    public static void getGoodsAttr(String paramString1, int paramInt, String paramString2, String paramString3, String paramString4, Callback paramCallback) {
        OkHttpClient okHttpClient = getOkHttpInstance();
        FormBody formBody = (new FormBody.Builder()).add("c_id", paramString1).add("page", paramInt + "").add("okcat_id", "0").add("keywords", paramString2).add("type", paramString3).add("filter_attr", paramString4).build();
        okHttpClient.newCall((new Request.Builder()).post(formBody).url("http://yangguang.bocang.cc/Interface/get_goods_list").build()).enqueue(paramCallback);
    }

    public static void getGoodsClass(Callback paramCallback) {
        OkHttpClient okHttpClient = getOkHttpInstance();
        FormBody formBody = (new FormBody.Builder()).build();
        okHttpClient.newCall((new Request.Builder()).post(formBody).url(Constant.GOODSCLASS).build()).enqueue(paramCallback);
    }

    public static void getGoodsList(String paramString1, int paramInt, String paramString2, String paramString3, String paramString4, Callback paramCallback) {
        OkHttpClient okHttpClient = getOkHttpInstance();
        FormBody formBody = (new FormBody.Builder()).add("c_id", paramString1).add("page", paramInt + "").add("okcat_id", "0").add("keywords", paramString2).add("type", paramString3).add("filter_attr", paramString4).build();
        okHttpClient.newCall((new Request.Builder()).post(formBody).url("http://yangguang.bocang.cc/Interface/get_goods_list").build()).enqueue(paramCallback);
    }

    public static HostnameVerifier getHostnameVerifier() { return new HostnameVerifier() {
        public boolean verify(String param1String, SSLSession param1SSLSession) { return true; }
    }; }

    public static void getNews(Callback paramCallback) {
        OkHttpClient okHttpClient = getOkHttpInstance();
        FormBody formBody = (new FormBody.Builder()).build();
        okHttpClient.newCall((new Request.Builder()).post(formBody).url("http://yangguang.bocang.cc/Interface/News_list?").build()).enqueue(paramCallback);
    }

    public static void getNewsDetail(String paramString, Callback paramCallback) {
        OkHttpClient okHttpClient = getOkHttpInstance();
        FormBody formBody = (new FormBody.Builder()).add("id", paramString).build();
        okHttpClient.newCall((new Request.Builder()).post(formBody).url("http://yangguang.bocang.cc/Interface/News_text").build()).enqueue(paramCallback);
    }

    public static OkHttpClient getOkHttpInstance() { return (new OkHttpClient.Builder()).sslSocketFactory(getSSLSocketFactory()).hostnameVerifier(getHostnameVerifier()).build(); }

    public static void getProgrammeList(int paramInt, Callback paramCallback) {
        OkHttpClient okHttpClient = getOkHttpInstance();
        FormBody formBody = (new FormBody.Builder()).add("page", "" + paramInt).build();
        okHttpClient.newCall((new Request.Builder()).post(formBody).url("http://yangguang.bocang.cc/Interface/plan_list").build()).enqueue(paramCallback);
    }

    public static SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sSLContext = SSLContext.getInstance("SSL");
            sSLContext.init(null, getTrustManager(), new SecureRandom());
            return sSLContext.getSocketFactory();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private static TrustManager[] getTrustManager() { return new TrustManager[] { new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] param1ArrayOfX509Certificate, String param1String) {}

        public void checkServerTrusted(X509Certificate[] param1ArrayOfX509Certificate, String param1String) {}

        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
    } }; }

    public static void getbrandDetail(String paramString, Callback paramCallback) {
        OkHttpClient okHttpClient = getOkHttpInstance();
        FormBody formBody = (new FormBody.Builder()).add("id", "" + paramString).build();
        okHttpClient.newCall((new Request.Builder()).post(formBody).url("http://yangguang.bocang.cc/Interface/Category_info").build()).enqueue(paramCallback);
    }
}
