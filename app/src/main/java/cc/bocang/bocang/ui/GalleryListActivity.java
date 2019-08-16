package cc.bocang.bocang.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import astuetz.MyPagerSlidingTabStrip;
import cc.bocang.bocang.R;
import cc.bocang.bocang.adapter.BaseAdapterHelper;
import cc.bocang.bocang.adapter.QuickAdapter;
import cc.bocang.bocang.bean.GalleryBean;
import cc.bocang.bocang.global.Constant;
import cc.bocang.bocang.utils.LogUtils;
import cc.bocang.bocang.utils.UIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.List;

public class GalleryListActivity extends BaseActivity {
    private QuickAdapter<GalleryBean.Data> adapter;

    private int current;

    private List<GalleryBean> galleryBeanList;

    private GridView gv_home_bottom;

    private PagerAdapter pagerAdapter;

    private String result;

    private MyPagerSlidingTabStrip tabStrip;

    private String title;

    private ViewPager viewPager;

    private void initTab() {
        this.pagerAdapter = new PagerAdapter() {
            public void destroyItem(@NonNull ViewGroup param1ViewGroup, int param1Int, @NonNull Object param1Object) { param1ViewGroup.removeView((View)param1Object); }

            public int getCount() { return GalleryListActivity.this.galleryBeanList.size(); }

            @Nullable
            public CharSequence getPageTitle(int param1Int) { return ((GalleryBean)GalleryListActivity.this.galleryBeanList.get(param1Int)).getName(); }

            @NonNull
            public Object instantiateItem(@NonNull ViewGroup param1ViewGroup, int param1Int) { return new ListView(GalleryListActivity.this); }

            public boolean isViewFromObject(@NonNull View param1View, @NonNull Object param1Object) { return (param1View == param1Object); }
        };
        this.viewPager.setAdapter(this.pagerAdapter);
        this.tabStrip.setViewPager(viewPager);
        this.tabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int param1Int) {}

            public void onPageScrolled(int param1Int1, float param1Float, int param1Int2) {
                current=param1Int1;
//                GalleryListActivity.access$002(GalleryListActivity.this, param1Int1);
            }

            public void onPageSelected(int param1Int) {
//                GalleryListActivity.access$002(GalleryListActivity.this, param1Int);
                current=param1Int;
                if (GalleryListActivity.this.galleryBeanList != null && param1Int < GalleryListActivity.this.galleryBeanList.size())
                    GalleryListActivity.this.adapter.replaceAll(((GalleryBean)GalleryListActivity.this.galleryBeanList.get(GalleryListActivity.this.current)).getData());
            }
        });
        this.viewPager.setCurrentItem(this.current);
        this.adapter.replaceAll(((GalleryBean)this.galleryBeanList.get(this.current)).getData());
        this.adapter.notifyDataSetChanged();
    }

    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_gallery_list);
        this.result = getIntent().getStringExtra(Constant.result);
        this.title = getIntent().getStringExtra(Constant.title);
        ((TextView)findViewById(R.id.tv_title)).setText("" + this.title);
        this.tabStrip = (MyPagerSlidingTabStrip)findViewById(R.id.tabstrip);
        this.tabStrip.selectColor = getResources().getColor(R.color.theme_red);
        this.tabStrip.defaultColor = getResources().getColor(R.color.tv_333333);
        this.tabStrip.setDividerColor(0);
        this.tabStrip.setIndicatorColor(getResources().getColor(R.color.theme_red));
        this.tabStrip.setIndicatorHeight(UIUtils.dip2PX(1));
        this.tabStrip.setUnderlineColor(getResources().getColor(R.color.theme_red));
        this.tabStrip.setUnderlineHeight(0);
        this.viewPager = (ViewPager)findViewById(R.id.viewPager);
        this.gv_home_bottom = (GridView)findViewById(R.id.gv_home_bottom);
        LogUtils.logE("result", this.result);
        this.galleryBeanList = (List)(new Gson()).fromJson(this.result, (new TypeToken<List<GalleryBean>>() {

        }).getType());
        this.adapter = new QuickAdapter<GalleryBean.Data>(this, R.layout.item_gallery) {
            protected void convert(BaseAdapterHelper param1BaseAdapterHelper, GalleryBean.Data param1Data) {
                ImageView imageView = (ImageView)param1BaseAdapterHelper.getView(R.id.iv_img);
                ImageLoader.getInstance().displayImage("http://yangguang.bocang.cc/App/yangguang/Public/uploads/" + param1Data.getFilepath(), imageView);
            }
        };
        this.gv_home_bottom.setAdapter(this.adapter);
        this.gv_home_bottom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
                Intent intent = new Intent(GalleryListActivity.this, ImageDetailActivity.class);
                intent.putExtra(Constant.photo, "http://yangguang.bocang.cc/App/yangguang/Public/uploads/" + ((GalleryBean.Data)((GalleryBean)GalleryListActivity.this.galleryBeanList.get(GalleryListActivity.this.current)).getData().get(param1Int)).getFilepath());
                GalleryListActivity.this.startActivity(intent);
            }
        });
        initTab();
    }
}
