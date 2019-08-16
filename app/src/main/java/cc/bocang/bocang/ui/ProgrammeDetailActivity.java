package cc.bocang.bocang.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import cc.bocang.bocang.R;
import cc.bocang.bocang.adapter.BaseAdapterHelper;
import cc.bocang.bocang.adapter.QuickAdapter;
import cc.bocang.bocang.bean.ProgrameBean;
import cc.bocang.bocang.data.model.Goods;
import cc.bocang.bocang.global.Constant;
import cc.bocang.bocang.utils.DateUtils;
import cc.bocang.bocang.utils.UIUtils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ProgrammeDetailActivity extends BaseActivity {
    private ProgrameBean programeBean;

    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_programme_detail);
        this.programeBean = (ProgrameBean)(new Gson()).fromJson(getIntent().getStringExtra(Constant.data), ProgrameBean.class);
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        TextView textView1 = (TextView)findViewById(R.id.textView);
        TextView textView2 = (TextView)findViewById(R.id.tv_time);
        TextView textView3 = (TextView)findViewById(R.id.tv_share);
        ListView listView = (ListView)findViewById(R.id.lv_list);
        ImageLoader.getInstance().displayImage("http://yangguang.bocang.cc/App/yangguang/Public/uploads//plan/" + this.programeBean.getPath(), imageView);
        if (this.programeBean != null) {
            textView1.setText("" + this.programeBean.getName());
            textView2.setText("" + DateUtils.getStrTime(this.programeBean.getAdd_time()));
            textView3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View param1View) { ImageLoader.getInstance().loadImage("http://yangguang.bocang.cc/App/yangguang/Public/uploads//plan/" + ProgrammeDetailActivity.this.programeBean.getPath(), new ImageLoadingListener() {
                    public void onLoadingCancelled(String param2String, View param2View) {}

                    public void onLoadingComplete(String param2String, View param2View, Bitmap param2Bitmap) {
                        UIUtils.showShareDialog(ProgrammeDetailActivity.this, param2Bitmap, "", param2String); }

                    public void onLoadingFailed(String param2String, View param2View, FailReason param2FailReason) {}

                    public void onLoadingStarted(String param2String, View param2View) {}
                }); }
            });
            QuickAdapter<Goods> quickAdapter = new QuickAdapter<Goods>(this, R.layout.item_goods) {
                protected void convert(BaseAdapterHelper param1BaseAdapterHelper, Goods param1Goods) {
                    param1BaseAdapterHelper.setText(R.id.tv_name, param1Goods.getName());
                    param1BaseAdapterHelper.setText(R.id.tv_price, "" + param1Goods.getShop_price());
                    ImageView imageView = (ImageView)param1BaseAdapterHelper.getView(R.id.iv_img);
                    ImageLoader.getInstance().displayImage("http://yangguang.bocang.cc/App/yangguang/Public/uploads/goods/" + param1Goods.getImg_url(), imageView);
                }
            };
            listView.setAdapter(quickAdapter);
            quickAdapter.replaceAll(this.programeBean.getGoods());
        }
    }
}
