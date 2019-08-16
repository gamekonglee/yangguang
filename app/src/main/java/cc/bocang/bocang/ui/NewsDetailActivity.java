package cc.bocang.bocang.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import cc.bocang.bocang.R;
import cc.bocang.bocang.bean.NewsDetail;
import cc.bocang.bocang.global.Constant;
import cc.bocang.bocang.net.OkHttpUtils;
import cc.bocang.bocang.utils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NewsDetailActivity extends BaseActivity {
    private String content;

    private String id;

    private String img;

    private ImageView iv_img;

    private String title;

    private TextView tv_title;

    private WebView webview_content;

    public void goBack(View paramView) { finish(); }

    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_news_detail);
        this.tv_title = (TextView)findViewById(R.id.tv_title);
        this.webview_content = (WebView)findViewById(R.id.webview_content);
        this.iv_img = (ImageView)findViewById(R.id.iv_img);
        this.title = getIntent().getStringExtra(Constant.title);
        this.id = getIntent().getStringExtra(Constant.id);
        WebSettings webSettings = this.webview_content.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        this.tv_title.setText("" + this.title);
        OkHttpUtils.getNewsDetail(this.id, new Callback() {
            public void onFailure(Call param1Call, IOException param1IOException) {}

            public void onResponse(Call param1Call, Response param1Response) throws IOException {
                String str = param1Response.body().string();
                final List newsDetails = (List)(new Gson()).fromJson(str, (new TypeToken<List<NewsDetail>>() {

                }).getType());
                if (newsDetails != null && newsDetails.size() > 0)
                    NewsDetailActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            String str = ((NewsDetail)newsDetails.get(0)).getContent().replace("<img src=\"/ueditor", "<img src=\"http://yangguang.bocang.cc//ueditor").replace("<img alt=\"\" src=\"", "<img src=\"http://yangguang.bocang.cc/");
                            str = "<meta name=\"viewport\" content=\"width=device-width\">" + str;
                            LogUtils.logE("content", str);
                            webview_content.loadData(str, "text/html; charset=UTF-8", null);
                        }
                    });
            }
        });
    }
}
