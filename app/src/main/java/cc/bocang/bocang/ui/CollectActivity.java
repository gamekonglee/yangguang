package cc.bocang.bocang.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import cc.bocang.bocang.R;

public class CollectActivity extends BaseActivity {
    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_collect);
        FmCollect fmCollect = new FmCollect();
        fmCollect.setArguments(new Bundle());
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, fmCollect).commitAllowingStateLoss();
    }
}
