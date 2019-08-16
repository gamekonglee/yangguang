package cc.bocang.bocang.ui;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cc.bocang.bocang.R;
import cc.bocang.bocang.broadcast.Broad;
import cc.bocang.bocang.data.api.OtherApi;
import cc.bocang.bocang.data.dao.CartDao;
import cc.bocang.bocang.data.model.Goods;
import cc.bocang.bocang.data.model.Result;
import cc.bocang.bocang.global.Constant;
import cc.bocang.bocang.utils.AppUtil;
import cc.bocang.bocang.utils.VersionUtil;
import cc.bocang.bocang.view.MyViewpage;
import com.alibaba.fastjson.JSON;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.lib.common.hxp.global.UserSp;
import java.util.Iterator;
import java.util.List;

public class IndexActivity extends BaseActivity implements View.OnClickListener {
    public static int goodsId;

    public static boolean isClickFmHomeItem;

    public static int itemPos;

    public static String mCId = "";

    public static int titlePos;

    private final int CONN_FAIL = 0;

    private final int NEED_UPDATE = 1;

    private final String TAG = IndexActivity.class.getSimpleName();

    private UpdateApkBroadcastReceiver broadcastReceiver;

    private IndexActivity ct = this;

    protected boolean[] fragmentsUpdateFlag = { false, false, false, false };

    public List<Goods> goodses;

    private MyBroadcastReciver mBroadcastReciver;

    private ImageButton mCartImgBtn;

    private RelativeLayout mCartRl;

    private TextView mCartTv;

    private Button mCollectBtn;

    private TextView mDeletetv;

    protected FragmentPagerAdapter mFragmentPagerAdapter;

    private Handler mHandler = new Handler() {
        @TargetApi(9)
        public void handleMessage(Message param1Message) {
            if (!IndexActivity.this.isFinishing()) {
                switch (param1Message.what) {
                    default:
                        return;
                    case 0:
                        Toast.makeText(IndexActivity.this, R.string.no_net, Toast.LENGTH_LONG).show();
                        return;
                    case 1:
                        break;
                }
                IndexActivity.this.showInstallDialog();
                return;
            }
        }
    };

    private ImageButton mHomeImgBtn;

    private LinearLayout mHomeLl;

    private TextView mHomeTv;

    private LocalBroadcastManager mLocalBroadcastManager;

    private ImageButton mMatchImgBtn;

    private LinearLayout mMatchLl;

    private TextView mMatchTv;

    private ImageButton mMoreImgBtn;

    private LinearLayout mMoreLl;

    private TextView mMoreTv;

    private ImageButton mProductImgBtn;

    private LinearLayout mProductLl;

    private TextView mProductTv;

    private Button mRightBtn;

    private Button mScanBtn;

    private int mScreenWidth;

    private TextView mUnReadTv;

    public MyViewpage mViewPager;

    private void connFail() { this.mHandler.sendEmptyMessage(0); }

    private void getSeachProduct() {
        Intent intent = new Intent(this.ct, SearchActivity.class);
        intent.putExtra("title", "产品搜索");
        intent.putExtra("okcat_id", 0);
        intent.putExtra("fm", FmCollect.class.getSimpleName());
        this.ct.startActivity(intent);
    }

    private void initView() {
        this.mScreenWidth = (getResources().getDisplayMetrics()).widthPixels;
        this.mViewPager = (MyViewpage)findViewById(R.id.viewPager);
        this.mViewPager.setNoScroll(true);
        this.mHomeLl = (LinearLayout)findViewById(R.id.homeLl);
        this.mProductLl = (LinearLayout)findViewById(R.id.productLl);
        this.mMatchLl = (LinearLayout)findViewById(R.id.matchLl);
        this.mCartRl = (RelativeLayout)findViewById(R.id.cartRl);
        this.mMoreLl = (LinearLayout)findViewById(R.id.moreLl);
        this.mHomeLl.setOnClickListener(this);
        this.mProductLl.setOnClickListener(this);
        this.mMatchLl.setOnClickListener(this);
        this.mCartRl.setOnClickListener(this);
        this.mMoreLl.setOnClickListener(this);
        this.mHomeImgBtn = (ImageButton)findViewById(R.id.homeImgBtn);
        this.mProductImgBtn = (ImageButton)findViewById(R.id.productImgBtn);
        this.mMatchImgBtn = (ImageButton)findViewById(R.id.matchImgBtn);
        this.mCartImgBtn = (ImageButton)findViewById(R.id.cartImgBtn);
        this.mMoreImgBtn = (ImageButton)findViewById(R.id.moreImgBtn);
        this.mHomeTv = (TextView)findViewById(R.id.homeTv);
        this.mProductTv = (TextView)findViewById(R.id.productTv);
        this.mMatchTv = (TextView)findViewById(R.id.matchTv);
        this.mCartTv = (TextView)findViewById(R.id.cartTv);
        this.mUnReadTv = (TextView)findViewById(R.id.unReadTv);
        this.mMoreTv = (TextView)findViewById(R.id.moreTv);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        this.mScanBtn = (Button)findViewById(R.id.topLeftBtn);
        this.mScanBtn.setOnClickListener(this);
        this.mRightBtn = (Button)findViewById(R.id.topRightBtn);
        this.mRightBtn.setOnClickListener(this);
        this.mCollectBtn = (Button)findViewById(R.id.collectBtn);
        this.mCollectBtn.setOnClickListener(this);
        this.mDeletetv = (TextView)findViewById(R.id.deletetv);
        this.mDeletetv.setOnClickListener(this);
        this.mLocalBroadcastManager = LocalBroadcastManager.getInstance(this.ct);
        this.mBroadcastReciver = new MyBroadcastReciver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("cart_change_ACTION");
        this.mLocalBroadcastManager.registerReceiver(this.mBroadcastReciver, intentFilter);
    }

    private void needUpdate() { this.mHandler.sendEmptyMessage(1); }

    private void showInstallDialog() {
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
        dialogBuilder.withTitle("提示").withTitleColor("#FFFFFF")
                .withDividerColor("#666666").withMessage("发现有新版本，是否前往下载？")
                .withMessageColor("#666666")
                .withDialogColor(this.ct.getResources().getColor(R.color.tv_333333))
                .isCancelableOnTouchOutside(false).isCancelable(false).withDuration(700)
                .withEffect(Effectstype.Shake)
                .withButton1Text("取消")
                .withButton2Text("下载").setButton1Click(new View.OnClickListener() {
            public void onClick(View param1View) { dialogBuilder.dismiss(); }
        }).setButton2Click(new View.OnClickListener() {
            public void onClick(View param1View) {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("http://app.08138.com/yangguang.apk"));
                IndexActivity.this.startActivity(intent);
            }
        }).show();
    }

    public void onBackPressed() {
        if (FmProduct.dropDownMenu != null && FmProduct.dropDownMenu.isShowing()) {
            FmProduct.dropDownMenu.close();
            return;
        }
        if (FmScene.dropDownMenu != null && FmScene.dropDownMenu.isShowing()) {
            FmScene.dropDownMenu.close();
            return;
        }
        super.onBackPressed();
    }

    public void onClick(View paramView) {
        Intent intent;
        if (paramView == this.mHomeLl) {
            this.mViewPager.setCurrentItem(0, false);
            return;
        }
        if (paramView == this.mProductLl) {
            this.mViewPager.setCurrentItem(1, false);
            return;
        }
        if (paramView == this.mMatchLl) {
            this.mViewPager.setCurrentItem(2, false);
            return;
        }
        if (paramView == this.mCartRl) {
            this.mViewPager.setCurrentItem(3, false);
            return;
        }
        if (paramView == this.mMoreLl) {
            this.mViewPager.setCurrentItem(4, false);
            return;
        }
        if (paramView == this.mScanBtn) {
            intent = new Intent();
            intent.setClass(this, SimpleScannerActivity.class);
            startActivity(intent);
            return;
        }
        if (paramView == this.mRightBtn) {
            int i = this.mViewPager.getCurrentItem();
            Log.v("520it", i + "");
            if (i == 1) {
                intent = new Intent(this.ct, ContainerActivity.class);
                intent.putExtra("title", "我的收藏");
                intent.putExtra("fm", FmCollect.class.getSimpleName());
                this.ct.startActivity(intent);
                return;
            }
            return;
        }
        if (paramView == this.mCollectBtn) {
            int i = this.mViewPager.getCurrentItem();
            Log.v("520it", i + "");
            if (i == 0 || i == 1) {
                getSeachProduct();
                return;
            }
            return;
        }
        if (paramView == this.mDeletetv) {
            boolean bool = false;
            Iterator iterator = this.goodses.iterator();
            while (iterator.hasNext()) {
                Goods goods = (Goods)iterator.next();
                if (goods.delete) {
                    iterator.remove();
                    (new CartDao(this.ct)).deleteOne(goods.getId());
                    bool = true;
                }
            }
            if (bool) {
                Broad.sendLocalBroadcast(this.ct, "cart_change_ACTION", null);
                return;
            }
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_index);
        initView();
        this.goodses = (new CartDao(this.ct)).getAll();
        Log.i(this.TAG, this.goodses.toString());
        if (this.goodses.isEmpty()) {
            this.mUnReadTv.setVisibility(View.GONE);
        } else {
            this.mUnReadTv.setVisibility(View.VISIBLE);
            this.mUnReadTv.setText(this.goodses.size() + "");
        }
        setColor(this, getResources().getColor(R.color.white));
        (new Thread(new Runnable() {
            public void run() {
                String str1 = OtherApi.getAppVersion();
                String str2 = AppUtil.localVersionName(IndexActivity.this.ct.getApplicationContext());
                if ("-1".equals(str1)) {
                    IndexActivity.this.connFail();
                    return;
                }
                UserSp userSp = new UserSp(IndexActivity.this.ct);
                userSp.setString(userSp.getSP_SERVER_VERSION(), str1);
                if (VersionUtil.isNeedUpdate(str2, str1)) {
                    IndexActivity.this.needUpdate();
                    return;
                }
            }
        })).start();
        (new Thread(new Runnable() {
            public void run() {
                Result result = new Result();
                String str = OtherApi.doGet(Constant.PROUCT_CID);
                if (!TextUtils.isEmpty(str))
                    result = (Result)JSON.parseObject(str, Result.class);
                IndexActivity.mCId = result.getContent();
            }
        })).start();
        this.mFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        this.mViewPager.setAdapter(this.mFragmentPagerAdapter);
        this.mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        this.mViewPager.setCurrentItem(0);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.mLocalBroadcastManager.unregisterReceiver(this.mBroadcastReciver);
    }

    private class MyBroadcastReciver extends BroadcastReceiver {
        private MyBroadcastReciver() {}

        public void onReceive(Context param1Context, Intent param1Intent) {
            if (param1Intent.getAction().equals("cart_change_ACTION")) {
                CartDao cartDao = new CartDao(IndexActivity.this.ct);
                IndexActivity.this.goodses = cartDao.getAll();
                if (IndexActivity.this.goodses.isEmpty()) {
                    IndexActivity.this.mUnReadTv.setVisibility(View.GONE);
                } else {
                    IndexActivity.this.mUnReadTv.setVisibility(View.VISIBLE);
                    IndexActivity.this.mUnReadTv.setText(IndexActivity.this.goodses.size() + "");
                }
                IndexActivity.this.fragmentsUpdateFlag[3] = true;
                IndexActivity.this.mFragmentPagerAdapter.notifyDataSetChanged();
            }
        }
    }

    public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        public MyFragmentPagerAdapter(FragmentManager param1FragmentManager) { super(param1FragmentManager); }

        public int getCount() { return 5; }

        public Fragment getItem(int param1Int) {
            Fragment fmMine = null;
            if (param1Int == 0) {
                fmMine = new FmHome();
                fmMine.setArguments(new Bundle());
                return fmMine;
            }
            if (1 == param1Int) {
                FmProduct fmProduct = new FmProduct();
                fmProduct.setArguments(new Bundle());
                return fmProduct;
            }
            if (2 == param1Int) {
                FmScene fmScene = new FmScene();
                fmScene.setArguments(new Bundle());
                return fmScene;
            }
            if (3 == param1Int) {
                FmCartNew fmCartNew = new FmCartNew();
                fmCartNew.setArguments(new Bundle());
                return fmCartNew;
            }
            if (4 == param1Int) {
                fmMine = new FmMine();
                fmMine.setArguments(new Bundle());
                return fmMine;
            }
            return fmMine;
        }

        public int getItemPosition(Object param1Object) { return -2; }

        public Object instantiateItem(ViewGroup param1ViewGroup, int param1Int) {
            Fragment fragment1 = (Fragment)super.instantiateItem(param1ViewGroup, param1Int);
            String str = fragment1.getTag();
            Fragment fragment2 = fragment1;
            if (IndexActivity.this.fragmentsUpdateFlag[param1Int % IndexActivity.this.fragmentsUpdateFlag.length]) {
                FragmentTransaction fragmentTransaction = IndexActivity.this.getSupportFragmentManager().beginTransaction();
                if (param1Int == 0) {
                    FmHome fmHome = new FmHome();
                    fmHome.setArguments(new Bundle());
                } else if (param1Int == 1) {
                    FmProduct fmProduct = new FmProduct();
                } else if (param1Int == 2) {
                    FmScene fmScene = new FmScene();
                    fmScene.setArguments(new Bundle());
                } else if (param1Int == 3) {
                    FmCartNew fmCartNew = new FmCartNew();
                    fmCartNew.setArguments(new Bundle());
                } else if (param1Int == 4) {
                    fragment1 = new FmMine();
                    fragment1.setArguments(new Bundle());
                }
                fragmentTransaction.add(param1ViewGroup.getId(), fragment1, str);
                fragmentTransaction.attach(fragment1);
                fragmentTransaction.commitAllowingStateLoss();
                IndexActivity.this.fragmentsUpdateFlag[param1Int % IndexActivity.this.fragmentsUpdateFlag.length] = false;
                fragment2 = fragment1;
            }
            return fragment2;
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private void setTabSelector(ImageButton param1ImageButton, TextView param1TextView) {
            IndexActivity.this.mHomeImgBtn.setBackgroundResource(R.mipmap.ic_home_normal);
            IndexActivity.this.mProductImgBtn.setBackgroundResource(R.mipmap.ic_product_normal);
            IndexActivity.this.mMatchImgBtn.setBackgroundResource(R.mipmap.ic_match_normal);
            IndexActivity.this.mCartImgBtn.setBackgroundResource(R.mipmap.ic_cart_normal);
            IndexActivity.this.mMoreImgBtn.setBackgroundResource(R.mipmap.ic_more_normal);
            if (param1ImageButton == IndexActivity.this.mHomeImgBtn) {
                IndexActivity.this.mRightBtn.setVisibility(View.GONE);
                IndexActivity.this.mDeletetv.setVisibility(View.GONE);
                IndexActivity.this.mCollectBtn.setVisibility(View.VISIBLE);
//                IndexActivity.this.mCollectBtn.setBackgroundResource(R.mipmap);
                IndexActivity.this.mHomeImgBtn.setBackgroundResource(R.mipmap.ic_home_press);
//                IndexActivity.this.mRightBtn.setBackgroundResource(2130903147);
            } else if (param1ImageButton == IndexActivity.this.mProductImgBtn) {
                IndexActivity.this.mRightBtn.setVisibility(View.VISIBLE);
                IndexActivity.this.mDeletetv.setVisibility(View.GONE);
                IndexActivity.this.mCollectBtn.setVisibility(View.VISIBLE);
//                IndexActivity.this.mCollectBtn.setBackgroundResource(2130903147);
//                IndexActivity.this.mRightBtn.setBackgroundResource(2130903066);
                IndexActivity.this.mProductImgBtn.setBackgroundResource(R.mipmap.ic_product_press);
            } else if (param1ImageButton == IndexActivity.this.mMatchImgBtn) {
                IndexActivity.this.mRightBtn.setVisibility(View.GONE);
                IndexActivity.this.mDeletetv.setVisibility(View.GONE);
                IndexActivity.this.mCollectBtn.setVisibility(View.GONE);
                IndexActivity.this.mMatchImgBtn.setBackgroundResource(R.mipmap.ic_match_press);
            } else if (param1ImageButton == IndexActivity.this.mCartImgBtn) {
                IndexActivity.this.mRightBtn.setVisibility(View.GONE);
                IndexActivity.this.mDeletetv.setVisibility(View.VISIBLE);
                IndexActivity.this.mCollectBtn.setVisibility(View.GONE);
//                IndexActivity.this.mCollectBtn.setBackgroundResource(2130837647);
                IndexActivity.this.mCartImgBtn.setBackgroundResource(R.mipmap.ic_cart_press);
            } else if (param1ImageButton == IndexActivity.this.mMoreImgBtn) {
                IndexActivity.this.mRightBtn.setVisibility(View.GONE);
                IndexActivity.this.mDeletetv.setVisibility(View.GONE);
                IndexActivity.this.mCollectBtn.setVisibility(View.GONE);
                IndexActivity.this.mMoreImgBtn.setBackgroundResource(R.mipmap.ic_more_press);
            }
            IndexActivity.this.mHomeTv.setTextColor(Color.parseColor("#555555"));
            IndexActivity.this.mProductTv.setTextColor(Color.parseColor("#555555"));
            IndexActivity.this.mMatchTv.setTextColor(Color.parseColor("#555555"));
            IndexActivity.this.mCartTv.setTextColor(Color.parseColor("#555555"));
            IndexActivity.this.mMoreTv.setTextColor(Color.parseColor("#555555"));
            param1TextView.setTextColor(-1695447);
        }

        public void onPageScrollStateChanged(int param1Int) {}

        public void onPageScrolled(int param1Int1, float param1Float, int param1Int2) {}

        public void onPageSelected(int param1Int) {
            switch (param1Int) {
                default:
                    return;
                case 0:
                    setTabSelector(IndexActivity.this.mHomeImgBtn, IndexActivity.this.mHomeTv);
                    return;
                case 1:
                    setTabSelector(IndexActivity.this.mProductImgBtn, IndexActivity.this.mProductTv);
                    return;
                case 2:
                    setTabSelector(IndexActivity.this.mMatchImgBtn, IndexActivity.this.mMatchTv);
                    return;
                case 3:
                    setTabSelector(IndexActivity.this.mCartImgBtn, IndexActivity.this.mCartTv);
                    return;
                case 4:
                    break;
            }
            setTabSelector(IndexActivity.this.mMoreImgBtn, IndexActivity.this.mMoreTv);
        }
    }

    private class UpdateApkBroadcastReceiver extends BroadcastReceiver {
        @TargetApi(11)
        public void onReceive(Context param1Context, Intent param1Intent) {
            if (param1Intent.getAction().equals("android.intent.action.DOWNLOAD_COMPLETE")) {
                IndexActivity.this.unregisterReceiver(IndexActivity.this.broadcastReceiver);
//                IndexActivity.access$102(IndexActivity.this, null);
                long l = param1Intent.getLongExtra("extra_download_id", -1L);
                Uri uri = ((DownloadManager)IndexActivity.this.getSystemService(Context.DOWNLOAD_SERVICE)).getUriForDownloadedFile(l);
//                if (Constant.isDebug)
//                    Log.i(IndexActivity.this.TAG, System.currentTimeMillis() + "���������������Uri���" + uri);
                UserSp userSp = new UserSp(IndexActivity.this.ct);
                userSp.setString(userSp.getSP_APK_URI(), uri.toString());
                IndexActivity.this.showInstallDialog();
            }
        }
    }
}
