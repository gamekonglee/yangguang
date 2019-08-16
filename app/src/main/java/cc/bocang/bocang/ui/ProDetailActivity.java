package cc.bocang.bocang.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import cc.bocang.bocang.R;
import cc.bocang.bocang.broadcast.Broad;
import cc.bocang.bocang.data.api.HDApiService;
import cc.bocang.bocang.data.api.HDRetrofit;
import cc.bocang.bocang.data.dao.CartDao;
import cc.bocang.bocang.data.dao.CollectDao;
import cc.bocang.bocang.data.model.Goods;
import cc.bocang.bocang.data.model.GoodsGallery;
import cc.bocang.bocang.data.model.GoodsPro;
import cc.bocang.bocang.data.model.UserInfo;
import cc.bocang.bocang.data.parser.ParseGetGoodsInfoResp;
import cc.bocang.bocang.data.response.GetGoodsInfoResp;
import cc.bocang.bocang.global.Constant;
import cc.bocang.bocang.global.MyApplication;
import cc.bocang.bocang.utils.StringUtil;
import cc.bocang.bocang.utils.UIUtils;
import com.bigkoo.convenientbanner.CBPageAdapter;
import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.lib.common.hxp.global.UserSp;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.okhttp.ResponseBody;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ProDetailActivity extends BaseActivity implements View.OnClickListener {
    public int IMAGE = 1;

    private final String TAG = ProDetailActivity.class.getSimpleName();

    private HDApiService apiService;

    private boolean collected;

    private ProDetailActivity ct = this;

    private Goods goods;

    private int id = -1;

    private LinearLayout mCallLl;

    private ImageView mCollectIv;

    private LinearLayout mCollectLl;

    private ConvenientBanner mConvenientBanner;

    private String mGoodsname;

    private String mImgUrl;

    private UserInfo mInfo;

    private ListView mListView;

    private TextView mProNameTv;

    private TextView mProPriceTv;

    private Button mShareBtn;

    private TextView mTextView;

    private Button mToCartBtn;

    private Button mToDiyBtn;

    private WebView mWebView;

    private ProgressBar pd;

    private void callGoodsInfo(HDApiService paramHDApiService, int paramInt) {
        this.pd.setVisibility(View.VISIBLE);
        paramHDApiService.getGoodsInfo(paramInt).enqueue(new Callback<ResponseBody>() {
            public void onFailure(Throwable param1Throwable) {
                if (ProDetailActivity.this.ct == null || ProDetailActivity.this.ct.isFinishing())
                    return;
                ProDetailActivity.this.pd.setVisibility(View.GONE);
            }

            public void onResponse(Response<ResponseBody> param1Response, Retrofit param1Retrofit) {
                if (ProDetailActivity.this.ct != null && !ProDetailActivity.this.ct.isFinishing()) {
                    ProDetailActivity.this.pd.setVisibility(View.GONE);
                    try {
                        String str = ((ResponseBody)param1Response.body()).string();
                        Log.i(ProDetailActivity.this.TAG, str);
                        GetGoodsInfoResp getGoodsInfoResp = ParseGetGoodsInfoResp.parse(str);
                        if (getGoodsInfoResp != null && getGoodsInfoResp.isSuccess()) {
//                            ProDetailActivity.access$402(ProDetailActivity.this, getGoodsInfoResp.getGoods());
                            goods=getGoodsInfoResp.getGoods();
                            ProDetailActivity.this.setGallery();
                            ProDetailActivity.this.setOthers();
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void initView() {
        int i = (getResources().getDisplayMetrics()).widthPixels;
        ((ScrollView)findViewById(R.id.scrollView)).smoothScrollTo(0, 0);
        this.pd = (ProgressBar)findViewById(R.id.pd);
        this.mCallLl = (LinearLayout)findViewById(R.id.callLl);
        this.mCollectLl = (LinearLayout)findViewById(R.id.collectLl);
        this.mCollectIv = (ImageView)findViewById(R.id.collectIv);
        this.mToCartBtn = (Button)findViewById(R.id.toCartBtn);
        this.mToDiyBtn = (Button)findViewById(R.id.toDiyBtn);
        this.mShareBtn = (Button)findViewById(R.id.sharebtn);
        this.mCallLl.setOnClickListener(this);
        this.mCollectLl.setOnClickListener(this);
        this.mToCartBtn.setOnClickListener(this);
        this.mToDiyBtn.setOnClickListener(this);
        this.mShareBtn.setOnClickListener(this);
        this.mConvenientBanner = (ConvenientBanner)findViewById(R.id.convenientBanner);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)this.mConvenientBanner.getLayoutParams();
        layoutParams.width = i;
        layoutParams.height = i;
        this.mConvenientBanner.setLayoutParams(layoutParams);
        this.mProNameTv = (TextView)findViewById(R.id.proNameTv);
        this.mProPriceTv = (TextView)findViewById(R.id.proPriceTv);
        this.mTextView = (TextView)findViewById(R.id.textView);
        this.mListView = (ListView)findViewById(R.id.listView);
        this.mWebView = (WebView)findViewById(R.id.webView);
        this.mWebView.setWebChromeClient(new WebChromeClient());
        this.mWebView.setWebViewClient(new WebViewClient());
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        this.mWebView.getSettings().setSupportZoom(true);
        this.mWebView.getSettings().setUseWideViewPort(true);
        this.mWebView.getSettings().setLoadWithOverviewMode(true);
        this.mWebView.getSettings().setDefaultTextEncodingName("utf-8");
    }

    private void setGallery() {
        List list2 = this.goods.getGoodsGalleries();
        List list1 = new ArrayList();
        if (list2.isEmpty()) {
            list1.add(this.goods.getImg_url());
        } else {
            Iterator iterator = list2.iterator();
            while (true) {
                if (iterator.hasNext()) {
                    list1.add(((GoodsGallery)iterator.next()).getImg_url());
                    continue;
                }
                list1 = StringUtil.preToStringArray("http://yangguang.bocang.cc/App/yangguang/Public/uploads/goods/", list1);
                this.mConvenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
                    public ProDetailActivity.NetworkImageHolderView createHolder() { return new ProDetailActivity.NetworkImageHolderView(); }
                },  list1);
                return;
            }
        }
        list1 = StringUtil.preToStringArray("http://yangguang.bocang.cc/App/yangguang/Public/uploads/goods/", list1);
        this.mConvenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            public ProDetailActivity.NetworkImageHolderView createHolder() { return new ProDetailActivity.NetworkImageHolderView(); }
        },  list1);
    }

    private void setOthers() {
        this.mProNameTv.setText(this.goods.getName());
        if (this.mInfo != null) {
            if (!TextUtils.isEmpty(this.mInfo.getMultiple()) && !TextUtils.isEmpty(this.mInfo.getInvite_code())) {
                this.mProPriceTv.setText("￥" + (this.goods.getShop_price() * Double.parseDouble(this.mInfo.getMultiple())));
            } else {
                this.mProPriceTv.setText("￥" + this.goods.getShop_price());
            }
        } else {
            this.mProPriceTv.setText("￥" + this.goods.getShop_price());
        }
        this.mTextView.setText("商品参数");
        ArrayList arrayList = new ArrayList();
        for (GoodsPro goodsPro : this.goods.getGoodsPros()) {
            if (!TextUtils.isEmpty(goodsPro.getValue())) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(goodsPro.getName());
                stringBuffer.append(":");
                stringBuffer.append(goodsPro.getValue());
                arrayList.add(stringBuffer.toString());
            }
        }
        ProductAdapter productAdapter = new ProductAdapter(this);
        this.mListView.setAdapter(productAdapter);
        productAdapter.setData(this.goods.getGoodsPros());
        productAdapter.notifyDataSetChanged();
        String str = this.goods.getGoods_desc().replace("<img src=\"", "<img src=\"http://yangguang.bocang.cc");
        str = "<meta name=\"viewport\" content=\"width=device-width\">" + str;
        this.mWebView.loadData(str, "text/html; charset=UTF-8", null);
    }

    private void showShare(final int id, final String phone) {
        if (id == -1)
            return;
        ImageLoader.getInstance().loadImage("http://yangguang.bocang.cc/App/yangguang/Public/uploads/goods/" + this.mImgUrl + "!400X400.png", new ImageLoadingListener() {
            public void onLoadingCancelled(String param1String, View param1View) {}

            public void onLoadingComplete(String param1String, View param1View, Bitmap param1Bitmap) { UIUtils.showShareDialogWithThumb(ProDetailActivity.this, ProDetailActivity.this.mGoodsname, "http://yangguang.bocang.cc/Interface/goods_show?id=" + id + "&phone=" + phone, param1Bitmap, param1String); }

            public void onLoadingFailed(String param1String, View param1View, FailReason param1FailReason) {}

            public void onLoadingStarted(String param1String, View param1View) {}
        });
    }

    public void goBack(View paramView) { finish(); }

    public void onClick(View paramView) {
        Intent intent;
        if (paramView == this.mCallLl) {
            UserSp userSp = new UserSp(this.ct);
            String mobile = userSp.getString(userSp.getSP_ZHU_PHONE(), null);
            Intent intent1 = new Intent();
            intent1.setAction("android.intent.action.CALL");
            intent1.setData(Uri.parse("tel:" + mobile));
            startActivity(intent1);
            return;
        }
        if (paramView == this.mCollectLl) {
            if (this.collected) {
                this.collected = false;
                (new CollectDao(this.ct)).deleteOne(this.id);
                this.mCollectIv.setImageResource(R.mipmap.ic_collect_normal);
                return;
            }
            this.collected = true;
            (new CollectDao(this.ct)).replaceOne(this.goods);
            this.mCollectIv.setImageResource(R.mipmap.ic_collect_press);
            return;
        }
        if (paramView == this.mToCartBtn) {
            if (-1L != (new CartDao(this.ct)).replaceOne(this.goods)) {
                Toast.makeText(this.ct, "已添加到购物车", Toast.LENGTH_SHORT).show();
                Broad.sendLocalBroadcast(this.ct, "cart_change_ACTION", null);
                return;
            }
            return;
        }
        if (paramView == this.mToDiyBtn) {
            if (this.goods == null) {
                Toast.makeText(this, "数据加载中，请稍等", Toast.LENGTH_SHORT).show();
                return;
            }
            intent = new Intent(this.ct, DiyActivity.class);
            intent.putExtra("from", "goods");
            intent.putExtra("goods", this.goods);
            startActivity(intent);
            return;
        }
        if (paramView == this.mShareBtn) {
            showShare(this.id, this.mInfo.getPhone());
            return;
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_pro_detail);
        initView();
        Intent intent = getIntent();
        this.id = intent.getIntExtra("id", 0);
        this.mImgUrl = intent.getStringExtra("imgurl");
        this.mGoodsname = intent.getStringExtra("goodsname");
        this.collected = (new CollectDao(this.ct)).isExist(this.id);
        if (this.collected) {
            this.mCollectIv.setImageResource(R.mipmap.ic_collect_press);
        } else {
            this.mCollectIv.setImageResource(R.mipmap.ic_collect_normal);
        }
        this.apiService = (HDApiService)HDRetrofit.create(HDApiService.class);
        callGoodsInfo(this.apiService, this.id);
        this.mInfo = ((MyApplication)getApplication()).mUserInfo;
//        setColor(this, getResources().getColor(2131492891));
    }

    class NetworkImageHolderView extends Object implements CBPageAdapter.Holder<String> {
        private ImageView imageView;

        public void UpdateUI(Context param1Context, final int position, String param1String) {
            this.imageView.setImageResource(R.mipmap.bg_default);
            ImageLoader.getInstance().displayImage(param1String, this.imageView);
            this.imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View param2View) {
//                    if (Constant.isDebug)
//                        Toast.makeText(param2View.getContext(), "������������" + position + "���", 0).show();
                }
            });
        }

        public View createView(Context param1Context) {
            this.imageView = new ImageView(param1Context);
            this.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return this.imageView;
        }
    }


    class ProductAdapter extends BaseAdapter {
        private Context mContext;

        private LayoutInflater mInflater;

        List<GoodsPro> mdata;

        public ProductAdapter(Context param1Context) {
            this.mContext = param1Context;
            this.mInflater = LayoutInflater.from(param1Context);
        }

        public int getCount() { return (this.mdata != null) ? this.mdata.size() : 0; }

        public GoodsPro getItem(int param1Int) { return (this.mdata == null) ? null : (GoodsPro)this.mdata.get(param1Int); }

        public long getItemId(int param1Int) { return 0L; }

        public View getView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
            if (param1View == null) {
                param1View = this.mInflater.inflate(R.layout.item_productdetail, null);
                ViewHolder viewHolder1 = new ViewHolder();
                viewHolder1.tv = (TextView)param1View.findViewById(R.id.tv_name);
                viewHolder1.tv_value = (TextView)param1View.findViewById(R.id.tv_value);
                param1View.setTag(viewHolder1);
                viewHolder1.tv.setText(((GoodsPro)this.mdata.get(param1Int)).getName());
                viewHolder1.tv_value.setText(((GoodsPro)this.mdata.get(param1Int)).getValue());
                return param1View;
            }
            ViewHolder viewHolder = (ViewHolder)param1View.getTag();
            viewHolder.tv.setText(((GoodsPro)this.mdata.get(param1Int)).getName());
            viewHolder.tv_value.setText(((GoodsPro)this.mdata.get(param1Int)).getValue());
            return param1View;
        }

        public void setData(List<GoodsPro> param1List) { this.mdata = param1List; }

        class ViewHolder {
            TextView tv;

            TextView tv_value;
        }
    }

    class ViewHolder {
        TextView tv;

        TextView tv_value;

        ViewHolder() {}
    }
}
