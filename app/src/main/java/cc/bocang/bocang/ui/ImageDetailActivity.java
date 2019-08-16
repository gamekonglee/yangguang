package cc.bocang.bocang.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import cc.bocang.bocang.R;
import cc.bocang.bocang.global.Constant;
import com.bm.library.PhotoView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageDetailActivity extends BaseActivity {
    private String mImagePath;

    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_img_detail);
        this.mImagePath = getIntent().getStringExtra(Constant.photo);
        PhotoView photoView = (PhotoView)findViewById(R.id.photo_iv);
        photoView.enable();
        ImageLoader.getInstance().displayImage(this.mImagePath, photoView);
    }
}
