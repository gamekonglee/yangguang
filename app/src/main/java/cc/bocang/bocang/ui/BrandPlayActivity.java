package cc.bocang.bocang.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.transition.Transition;
import android.view.View;

import cc.bocang.bocang.R;
import cc.bocang.bocang.global.Constant;
import cc.bocang.bocang.utils.AppUtils;
import cc.bocang.bocang.view.LandLayoutVideo;
import com.alibaba.fastjson.JSONObject;
import com.shuyu.gsyvideoplayer.GSYVideoPlayer;
import com.shuyu.gsyvideoplayer.utils.FileUtils;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import java.io.File;

public class BrandPlayActivity extends BaseActivity {
    public static final String IMG_TRANSITION = "IMG_TRANSITION";

    public static final String TRANSITION = "TRANSITION";

    public String id;

    private boolean isTransition;

    public JSONObject mData;

    public String name = "";

    OrientationUtils orientationUtils;

    String path = "http://7xt9qi.com1.z0.glb.clouddn.com/juhaogongcheng1";

    private LandLayoutVideo play;

    private Transition transition;

    private String url;

    @TargetApi(21)
    private boolean addTransitionListener() {
        this.transition = getWindow().getSharedElementEnterTransition();
        if (this.transition != null) {
            this.transition.addListener(new Transition.TransitionListener() {
                public void onTransitionCancel(Transition param1Transition) {}

                public void onTransitionEnd(Transition param1Transition) {
                    BrandPlayActivity.this.play.startPlayLogic();
                    param1Transition.removeListener(this);
                }

                public void onTransitionPause(Transition param1Transition) {}

                public void onTransitionResume(Transition param1Transition) {}

                public void onTransitionStart(Transition param1Transition) {}
            });
            return true;
        }
        return false;
    }

    private void initTransition() {
        if (this.isTransition && Build.VERSION.SDK_INT >= 21) {
            postponeEnterTransition();
            ViewCompat.setTransitionName(this.play, "IMG_TRANSITION");
            addTransitionListener();
            startPostponedEnterTransition();
            return;
        }
        this.play.startPlayLogic();
    }

    public void onBackPressed() {
        if (AppUtils.isEmpty(this.orientationUtils)) {
            finish();
            return;
        }
        if (this.orientationUtils.getScreenType() == 0) {
            this.play.getFullscreenButton().performClick();
            return;
        }
        this.play.setStandardVideoAllCallBack(null);
        GSYVideoPlayer.releaseAllVideos();
        if (this.isTransition && Build.VERSION.SDK_INT >= 21) {
            super.onBackPressed();
            return;
        }
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                BrandPlayActivity.this.finish();
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        }, 500);
    }

    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        Intent intent = getIntent();
        this.url = intent.getStringExtra(Constant.url);
        this.id = intent.getStringExtra(Constant.id);
        setContentView(R.layout.activity_play);
        this.play = (LandLayoutVideo)findViewById(R.id.play);
        startPlayVideo(this.url);
    }

    protected void onDestroy() {
        if (this.orientationUtils != null)
            this.orientationUtils.releaseListener();
        super.onDestroy();
    }

    protected void onPause() { super.onPause(); }

    public void startPlayVideo(String paramString) {
        this.play.setUp(paramString, true, new File(FileUtils.getPath()), new Object[] { "" });
        this.play.getTitleTextView().setVisibility(View.VISIBLE);
        this.play.getTitleTextView().setText(this.name);
        this.play.getBackButton().setVisibility(View.VISIBLE);
        this.orientationUtils = new OrientationUtils(this, this.play);
        this.play.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) { BrandPlayActivity.this.orientationUtils.resolveByClick(); }
        });
        GSYVideoType.setShowType(4);
        this.play.setLooping(true);
        this.play.setIsTouchWiget(true);
        this.play.getBackButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) { BrandPlayActivity.this.onBackPressed(); }
        });
        this.play.setLockLand(true);
        this.play.setHideKey(true);
        this.play.setBottomProgressBarDrawable(getResources().getDrawable(R.drawable.video_new_progress));
        initTransition();
    }
}
