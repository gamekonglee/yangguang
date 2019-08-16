package cc.bocang.bocang.ui;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cc.bocang.bocang.R;
import cc.bocang.bocang.global.Constant;
import cc.bocang.bocang.utils.LogUtils;

public class WebViewActivity extends BaseActivity {
    private String localData;

    private String url;

    private WebView webview;

    @TargetApi(17)
    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        this.url = getIntent().getStringExtra(Constant.url);
        this.localData = getIntent().getStringExtra(Constant.local_data);
        setContentView(R.layout.activity_webview);
        this.webview = (WebView)findViewById(R.id.webView);
        this.webview.setWebChromeClient(new WebChromeClient());
        this.webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView param1WebView, String param1String) {
                WebViewActivity.this.webview.loadUrl(param1String);
                return true;
            }
        });
        this.webview.getSettings().setJavaScriptEnabled(true);
        this.webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        this.webview.getSettings().setSupportZoom(true);
        this.webview.getSettings().setUseWideViewPort(true);
        this.webview.getSettings().setLoadWithOverviewMode(true);
        this.webview.getSettings().setAllowFileAccess(true);
        this.webview.getSettings().setLoadsImagesAutomatically(true);
        this.webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        this.webview.getSettings().setAllowContentAccess(true);
        this.webview.getSettings().setMediaPlaybackRequiresUserGesture(false);
        this.webview.getSettings().setBuiltInZoomControls(false);
        this.webview.getSettings().setDomStorageEnabled(true);
        this.webview.getSettings().setDefaultTextEncodingName("utf-8");
        LogUtils.logE("localdata", this.localData);
        if (TextUtils.isEmpty(this.url)) {
            this.localData = this.localData.replace("<img src=\"/ueditor", "<img src=\"http://yangguang.bocang.cc/ueditor");
            this.webview.loadData(this.localData, "text/html; charset=UTF-8", null);
            return;
        }
        this.webview.loadUrl(this.url);
    }

    protected void onDestroy() {
        super.onDestroy();
        ((ViewGroup)getWindow().getDecorView()).removeAllViews();
        if (this.webview != null)
            this.webview.destroy();
    }
}
