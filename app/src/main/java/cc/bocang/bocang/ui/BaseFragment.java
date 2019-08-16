package cc.bocang.bocang.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class BaseFragment extends Fragment {
    private RotateAnimation animation;

    private View contentView;

    private View errorView;

    private ImageView error_iv;

    private TextView error_tv;

    private boolean isDestroy;

    private boolean showDialog;

    private void errorinit() {}

    private void initDiaLog() {}

    protected abstract int getLayout();

    public void hideLoading() {
        if (this.isDestroy);
    }

    protected abstract void initController();

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void initViewData();

    public void onActivityCreated(@Nullable Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
        initDiaLog();
        initData();
        initView();
        initController();
        initViewData();
    }

    @Nullable
    public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle) { return paramLayoutInflater.inflate(getLayout(), null); }

    public void onDestroy() {
        super.onDestroy();
        this.isDestroy = true;
    }

    public void onDetach() { super.onDetach(); }

    public void onPause() { super.onPause(); }

    public void onResume() { super.onResume(); }

    public void onStart() { super.onStart(); }

    public void setShowDialog(String paramString) {
        if (this.isDestroy)
            return;
        if (paramString == null) {
            this.showDialog = false;
            return;
        }
        this.showDialog = true;
    }

    public void setShowDialog(boolean paramBoolean) { this.showDialog = paramBoolean; }

    public void showContentView() {
        errorinit();
        if (this.errorView != null && this.error_iv != null && this.error_tv != null && this.contentView != null) {
            this.contentView.setVisibility(0);
            this.errorView.setVisibility(8);
            return;
        }
    }

    public void showErrorView(String paramString, int paramInt) {
        errorinit();
        if (this.errorView != null && this.error_iv != null && this.error_tv != null && this.contentView != null) {
            this.error_iv.setImageResource(paramInt);
            if (!TextUtils.isEmpty(paramString)) {
                this.error_tv.setText(paramString);
            } else {
                this.error_tv.setText("���������������������");
            }
            this.error_iv.setAnimation(null);
            this.errorView.setVisibility(0);
            this.contentView.setVisibility(8);
            return;
        }
    }

    public void showLoading() {
        if (this.isDestroy);
    }

    public void showLoadingPage(String paramString, int paramInt) {
        errorinit();
        if (this.errorView != null && this.error_iv != null && this.error_tv != null && this.contentView != null) {
            this.errorView.setVisibility(0);
            this.contentView.setVisibility(8);
            if (!TextUtils.isEmpty(paramString)) {
                this.error_tv.setText(paramString);
            } else {
                this.error_tv.setText("������������������...");
            }
            this.error_iv.setImageResource(paramInt);
            if (this.animation == null) {
                this.animation = new RotateAnimation(0.0F, 359.0F, 1, 0.5F, 1, 0.5F);
                this.animation.setDuration(1000L);
                this.animation.setRepeatCount(1);
                this.animation.startNow();
            }
            this.error_iv.setAnimation(this.animation);
            return;
        }
    }
}
