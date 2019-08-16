package cc.bocang.bocang.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cc.bocang.bocang.R;
import cc.bocang.bocang.utils.DataCleanUtil;
import cc.bocang.bocang.utils.UIUtils;

public class SettingActivity extends BaseActivity {
    private String mTotalCacheSize;

    private TextView tv_cache;

    private TextView tv_version;

    private void getTotalCacheSize() {
        try {
            this.mTotalCacheSize = DataCleanUtil.getTotalCacheSize(this);
            this.tv_cache.setText(this.mTotalCacheSize);
            return;
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
    }

    public void clearCache() {
        if (this.mTotalCacheSize.equals("0K")) {
            Toast.makeText(this, "������������������������!", 0).show();
            return;
        }
        (new Thread(new Runnable() {
            public void run() {
                DataCleanUtil.clearAllCache(SettingActivity.this);
                SettingActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(SettingActivity.this, "������������������!", 0).show();
                        SettingActivity.this.getTotalCacheSize();
                    }
                });
            }
        })).start();
    }

    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_setting);
//        findViewById(R.id.rl_version);
        findViewById(R.id.rl_cache).setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) { SettingActivity.this.clearCache(); }
        });
        this.tv_version = (TextView)findViewById(R.id.tv_version);
        this.tv_cache = (TextView)findViewById(R.id.tv_cache);
        this.tv_version.setText(UIUtils.getVerName(this));
        getTotalCacheSize();
    }
}
