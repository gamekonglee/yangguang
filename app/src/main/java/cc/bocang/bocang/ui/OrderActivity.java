package cc.bocang.bocang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cc.bocang.bocang.R;
import cc.bocang.bocang.data.api.HDApiService;
import cc.bocang.bocang.data.api.HDRetrofit;
import cc.bocang.bocang.data.dao.CartDao;
import cc.bocang.bocang.data.dao.LogisticDao;
import cc.bocang.bocang.data.model.Goods;
import cc.bocang.bocang.data.model.Logistics;
import cc.bocang.bocang.data.model.Product;
import cc.bocang.bocang.data.model.Result;
import cc.bocang.bocang.data.model.UserInfo;
import cc.bocang.bocang.global.Constant;
import cc.bocang.bocang.global.IntentKeys;
import cc.bocang.bocang.global.MyApplication;
import cc.bocang.bocang.utils.UIUtils;
import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.squareup.okhttp.ResponseBody;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class OrderActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout address_rl;

    private HDApiService apiService;

    private Intent intent;

    private MyAdapter mAdapter;

    private TextView mAdresstv;

    private HDApiService mApiService;

    private TextView mConsigneetv;

    private UserInfo mInfo;

    private ListView mListView;

    private LogisticDao mLogisticDao;

    private Logistics mLogistics;

    private TextView mMoneytv;

    private String mOrderDiscount;

    private TextView mPhonetv;

    private TextView mProductnumtv;

    private List<Product> mProducts;

    private TextView mShare_tv;

    private ArrayList<Goods> mShopCarBeans;

    private int mShopCarTotalCount;

    private double mShopCarTotalPrice;

    private TextView mSumAge;

    private View mTopLeftBtn;

    private MyAdapter myAdapter;

    private View rl_add_address;

    private TextView tv_total_price;
    private Result result;

    private void deleteCarShop() {
        CartDao cartDao = new CartDao(this);
        boolean bool = false;
        for (byte b = 0; b < this.mShopCarBeans.size(); b++) {
            if (-1 != cartDao.deleteOne(((Goods)this.mShopCarBeans.get(b)).getId())) {
                bool = true;
            } else {
                bool = false;
            }
        }
        if (bool == true)
            EventBus.getDefault().post(Integer.valueOf(67));
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        getReceiverAdress();
//    }

    private void getReceiverAdress() {
        this.mInfo = ((MyApplication)getApplication()).mUserInfo;
        if (this.mInfo == null)
            return;
        this.mLogisticDao = new LogisticDao(this);
        List list = this.mLogisticDao.getAll();
        if (list.size() > 0) {
            this.mLogistics = (Logistics)list.get(0);
            this.mConsigneetv.setText(this.mLogistics.getName());
            this.mPhonetv.setText(this.mLogistics.getTel());
            this.mAdresstv.setText(this.mLogistics.getAddress());
            this.address_rl.setVisibility(View.VISIBLE);
            return;
        }
        this.address_rl.setVisibility(View.GONE);
    }

    private void getSubmitOrder() {
        String str = "{   \"order_name\" : \"" + this.mLogistics.getName() + "\",   \"order_sum\" : \"" + this.mShopCarTotalPrice + "\",   \"order_phone\" : \"" + this.mLogistics.getTel() + "\",   \"delivery_time\" : \"\",   \"user_id\" : \"" + this.mInfo.getId() + "\",   \"order_address\" : \"" + this.mLogistics.getAddress() + "\",   \"order_discount\" : \"" + this.mOrderDiscount + "\" }";
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[\n");
        for (byte b = 0; b < this.mShopCarBeans.size(); b++) {
            stringBuffer.append("  \"{\\n  \\\"msg\\\" : \\\"\\\",");
            stringBuffer.append("\\n  \\\"goodsPath\\\" : \\\"http://yangguang.bocang.cc/App/yangguang/Public/uploads/goods/" + ((Goods)this.mShopCarBeans.get(b)).getImg_url() + "\\\",");
            stringBuffer.append("\\n  \\\"goods_id\\\" : \\\"" + ((Goods)this.mShopCarBeans.get(b)).getId() + "\\\",");
            stringBuffer.append("\\n  \\\"goods_name\\\" : \\\"" + ((Goods)this.mShopCarBeans.get(b)).getName() + "\\\",");
            if (((Goods)this.mShopCarBeans.get(b)).getAgio() == 0.0D) {
                stringBuffer.append("\\n  \\\"discount\\\" : \\\"100\\\",");
            } else {
                stringBuffer.append("\\n  \\\"discount\\\" : \\\"" + (((Goods)this.mShopCarBeans.get(b)).getAgio() * 100.0D) + "\\\",");
            }
            if (!TextUtils.isEmpty(this.mInfo.getMultiple()) && !TextUtils.isEmpty(this.mInfo.getInvite_code())) {
                StringBuilder stringBuilder = (new StringBuilder()).append("\\n  \\\"goods_price\\\" : \\\"");
                float f = ((Goods)this.mShopCarBeans.get(b)).getBuyCount();
                stringBuffer.append(stringBuilder.append((((Goods)this.mShopCarBeans.get(b)).getShop_price() * f) * Double.parseDouble(this.mInfo.getMultiple())).append("\\\",").toString());
            } else {
                StringBuilder stringBuilder = (new StringBuilder()).append("\\n  \\\"goods_price\\\" : \\\"");
                float f = ((Goods)this.mShopCarBeans.get(b)).getBuyCount();
                stringBuffer.append(stringBuilder.append(((Goods)this.mShopCarBeans.get(b)).getShop_price() * f).append("\\\",").toString());
            }
            stringBuffer.append("\\n  \\\"number\\\" : \\\"" + ((Goods)this.mShopCarBeans.get(b)).getBuyCount() + "\\\"");
            if (b == this.mShopCarBeans.size() - 1) {
                stringBuffer.append("\\n}\"\n");
            } else {
                stringBuffer.append("\\n}\",\n");
            }
        }
        stringBuffer.append("]");
        Log.v("520", "xx:" + stringBuffer.toString());
        submitOrder(this.apiService, str, stringBuffer.toString());
    }

    private void initData() {
        Intent intent = getIntent();
        String str="";
        Serializable serializable = intent.getSerializableExtra("productOrder");
        this.mShopCarTotalPrice = intent.getDoubleExtra("totalPrice", 0.0D);
        this.mShopCarTotalCount = intent.getIntExtra("totalCount", 0);
        if (TextUtils.isEmpty(intent.getStringExtra(IntentKeys.SHOPORDERDISCOUNT))) {
            str = "100";
        } else {
            str = intent.getStringExtra(IntentKeys.SHOPORDERDISCOUNT);
        }
        this.mOrderDiscount = str;
        if (serializable != null || this.mShopCarTotalCount >= 0) {
            this.mShopCarBeans = (ArrayList)serializable;
            Log.v("520it", "mShopCarBeans" + this.mShopCarBeans.toString() + "");
        } else {
            finish();
        }
        getReceiverAdress();
        this.mMoneytv.setText("￥" + this.mShopCarTotalPrice + "");
        this.tv_total_price.setText("￥" + this.mShopCarTotalPrice + "");
        this.mProductnumtv.setText(this.mShopCarTotalCount + "");
        if (!TextUtils.isEmpty(this.mOrderDiscount))
            this.mSumAge.setText(this.mOrderDiscount + "%");
    }

    private void initView() {
        setContentView(R.layout.activity_order);
        this.mConsigneetv = (TextView)findViewById(R.id.consigneetv);
        this.mPhonetv = (TextView)findViewById(R.id.phonetv);
        this.mListView = (ListView)findViewById(R.id.listView);
        this.mAdresstv = (TextView)findViewById(R.id.adresstv);
        this.mMoneytv = (TextView)findViewById(R.id.money_tv);
        this.mProductnumtv = (TextView)findViewById(R.id.productnum_tv);
        this.mTopLeftBtn = findViewById(R.id.topLeftBtn);
        this.rl_add_address = findViewById(R.id.rl_add_address);
        this.rl_add_address.setOnClickListener(this);
        this.mTopLeftBtn.setOnClickListener(this);
        this.mShare_tv = (TextView)findViewById(R.id.share_tv);
        this.mShare_tv.setOnClickListener(this);
        this.apiService = (HDApiService)HDRetrofit.create(HDApiService.class);
        this.myAdapter = new MyAdapter();
        this.mListView.setAdapter(this.myAdapter);
        this.mSumAge = (TextView)findViewById(R.id.sumAge);
        this.address_rl = (RelativeLayout)findViewById(R.id.address_rl);
        this.tv_total_price = (TextView)findViewById(R.id.tv_total_price);
        this.address_rl.setOnClickListener(this);
    }

    private void showShare(String paramString) {}

    private void submitOrder(HDApiService paramHDApiService, String paramString1, String paramString2) { paramHDApiService.submitOrder(paramString1, paramString2).enqueue(new Callback<ResponseBody>() {
        public void onFailure(Throwable param1Throwable) {
            if (OrderActivity.this == null || OrderActivity.this.isFinishing())
                return;
            OrderActivity.this.tip("订单提交失败");
        }

        public void onResponse(Response<ResponseBody> param1Response, Retrofit param1Retrofit) {
            if (OrderActivity.this == null || OrderActivity.this.isFinishing())
                return;
            try {
                String str = ((ResponseBody)param1Response.body()).string();
                Log.v("520it", "result.getResult()=" + str);
                result = (Result)JSON.parseObject(str, Result.class);
                Log.v("520it", "result.getResult()=" + result.getResult());
                if (result.getResult().equals("0")) {
                    OrderActivity.this.tip("订单提交失败");
                    return;
                }
            } catch (Exception e) {
                if (OrderActivity.this != null && !OrderActivity.this.isFinishing())
                    OrderActivity.this.tip("订单提交失败");
                e.printStackTrace();
                return;
            }
            UIUtils.showShareDialogWithThumb(OrderActivity.this, "来自" + OrderActivity.this.getString(R.string.app_name) + "配灯系统的分享", "http://yangguang.bocang.cc/index.php/Interface/order_show?id=" + result.getResult(), null, "");
            OrderActivity.this.deleteCarShop();
        }
    }); }

    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
        if (paramInt1 == 3 && paramIntent != null) {
            Logistics logistics = (Logistics)paramIntent.getSerializableExtra(Constant.address);
            if (logistics != null) {
                this.mLogistics = logistics;
                this.mConsigneetv.setText(logistics.getName());
                this.mPhonetv.setText(logistics.getTel());
                this.mAdresstv.setText(logistics.getAddress());
                return;
            }
        }
    }

    public void onClick(View paramView) {
        switch (paramView.getId()) {
            default:
                return;
            case R.id.topLeftBtn:
                finish();
                return;
            case R.id.share_tv:
                if (TextUtils.isEmpty(this.mPhonetv.getText().toString())) {
                    Toast.makeText(this, "请选择收货地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                getSubmitOrder();
                return;
            case R.id.rl_add_address:
            case R.id.address_rl:
                if (this.address_rl.getVisibility() != View.VISIBLE) {
                    this.intent = new Intent(this, UserAddressAddActivity.class);
                    startActivity(this.intent);
                    return;
                }
                this.intent = new Intent(this, UserAddressActivity.class);
                this.intent.putExtra("isSELECTADDRESS", true);
                startActivityForResult(this.intent, 3);
                break;


        }
       
    }

    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        initView();
        initData();
        setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    private class MyAdapter extends BaseAdapter {
        private ImageLoader imageLoader = ImageLoader.getInstance();

        private DisplayImageOptions options = (new DisplayImageOptions.Builder())
                .showImageOnLoading(R.mipmap.bg_default).showImageForEmptyUri(R.mipmap.bg_default).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();

        public int getCount() { return (OrderActivity.this.mShopCarBeans == null) ? 0 : OrderActivity.this.mShopCarBeans.size(); }

        public Goods getItem(int param1Int) { return (OrderActivity.this.mShopCarBeans == null) ? null : (Goods)OrderActivity.this.mShopCarBeans.get(param1Int); }

        public long getItemId(int param1Int) { return param1Int; }

        public View getView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
            ViewHolder viewHolder;
            if (param1View == null) {
                param1View = View.inflate(OrderActivity.this, R.layout.item_order, null);
                viewHolder = new ViewHolder();
                viewHolder.productIv = (ImageView)param1View.findViewById(R.id.product_iv);
                viewHolder.nameTv = (TextView)param1View.findViewById(R.id.name_tv);
                viewHolder.pricTv = (TextView)param1View.findViewById(R.id.price_tv);
                viewHolder.countTv = (TextView)param1View.findViewById(R.id.count_tv);
                viewHolder.agiotv = (TextView)param1View.findViewById(R.id.agiotv);
                param1View.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder)param1View.getTag();
            }
            this.imageLoader.displayImage("http://yangguang.bocang.cc/App/yangguang/Public/uploads/goods/" + ((Goods)OrderActivity.this.mShopCarBeans.get(param1Int)).getImg_url() + "!280X280.png", viewHolder.productIv, this.options);
            viewHolder.nameTv.setText(((Goods)OrderActivity.this.mShopCarBeans.get(param1Int)).getName());
            if (!TextUtils.isEmpty(OrderActivity.this.mInfo.getMultiple()) && !TextUtils.isEmpty(OrderActivity.this.mInfo.getInvite_code())) {
                TextView textView = viewHolder.pricTv;
                StringBuilder stringBuilder = (new StringBuilder()).append("￥");
                float f = ((Goods)OrderActivity.this.mShopCarBeans.get(param1Int)).getBuyCount();
                textView.setText(stringBuilder.append((((Goods)OrderActivity.this.mShopCarBeans.get(param1Int)).getShop_price() * f) * Double.parseDouble(OrderActivity.this.mInfo.getMultiple())).toString());
            } else {
                TextView textView = viewHolder.pricTv;
                StringBuilder stringBuilder = (new StringBuilder()).append("￥");
                float f = ((Goods)OrderActivity.this.mShopCarBeans.get(param1Int)).getBuyCount();
                textView.setText(stringBuilder.append(((Goods)OrderActivity.this.mShopCarBeans.get(param1Int)).getShop_price() * f).toString());
            }
            viewHolder.countTv.setText("X" + ((Goods)OrderActivity.this.mShopCarBeans.get(param1Int)).getBuyCount() + "");
            if (((Goods)OrderActivity.this.mShopCarBeans.get(param1Int)).getAgio() == 0.0D) {
                viewHolder.agiotv.setText("100%");
                return param1View;
            }
            viewHolder.agiotv.setText((((Goods)OrderActivity.this.mShopCarBeans.get(param1Int)).getAgio() * 100.0D) + "%");
            return param1View;
        }

        class ViewHolder {
            TextView agioName;

            TextView agiotv;

            TextView countTv;

            TextView nameTv;

            TextView pricTv;

            ImageView productIv;
        }
    }


}
