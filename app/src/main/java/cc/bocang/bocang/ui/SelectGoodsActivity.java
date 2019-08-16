package cc.bocang.bocang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cc.bocang.bocang.R;
import cc.bocang.bocang.data.api.HDApiService;
import cc.bocang.bocang.data.api.HDRetrofit;
import cc.bocang.bocang.data.model.Goods;
import cc.bocang.bocang.data.model.GoodsAllAttr;
import cc.bocang.bocang.data.model.GoodsAttr;
import cc.bocang.bocang.data.parser.ParseGetGoodsListResp;
import cc.bocang.bocang.data.response.GetGoodsListResp;
import cc.bocang.bocang.global.Constant;
import cc.bocang.bocang.global.MyApplication;
import cc.bocang.bocang.utils.ConvertUtil;
import cc.bocang.bocang.utils.net.HttpListener;
import cc.bocang.bocang.utils.net.Network;
import com.baiiu.filter.DropDownMenu;
import com.baiiu.filter.interfaces.OnFilterDoneListener;
import com.lib.common.hxp.view.PullToRefreshLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import java.util.ArrayList;
import java.util.List;

public class SelectGoodsActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, OnFilterDoneListener, View.OnClickListener {
    private static final String TAG = "selectgoods";

    static DropDownMenu dropDownMenu;

    private HDApiService apiService;

    private String fitterStr;

    private List<GoodsAllAttr> goodsAllAttrs;

    private Integer[] goodsIds = { 0,0,0};

    private List<Goods> goodses;

    private boolean isSelectGoods;

    private List<Integer> itemPosList = new ArrayList();

    private MyAdapter mAdapter;

    private GridView mGridView;

    private Network mNetwork;

    private PullToRefreshLayout mPullToRefreshLayout;

    private int mScreenWidth;

    private int page = 1;

    private ProgressBar pd;

    private TextView select_num_tv;

    private void getGoodsList(String paramString1, final int page, String paramString2, String paramString3, String paramString4, final boolean initFilterDropDownView) {
        this.mNetwork.sendGoodsList(paramString1, page, "0", paramString2, paramString3, paramString4, this, new HttpListener() {
        public void onFailureListener(int param1Int, String param1String) {
            if (SelectGoodsActivity.this != null && !SelectGoodsActivity.this.isFinishing()) {
                SelectGoodsActivity.this.pd.setVisibility(View.GONE);
//                SelectGoodsActivity.access$610(SelectGoodsActivity.this);
                if (SelectGoodsActivity.this.mPullToRefreshLayout != null) {
                    SelectGoodsActivity.this.mPullToRefreshLayout.refreshFinish(0);
                    SelectGoodsActivity.this.mPullToRefreshLayout.loadmoreFinish(0);
                    return;
                }
            }
        }

        public void onSuccessListener(int param1Int, String param1String) {
            if (SelectGoodsActivity.this != null && !SelectGoodsActivity.this.isFinishing()) {
                SelectGoodsActivity.this.pd.setVisibility(View.GONE);
                if (SelectGoodsActivity.this.mPullToRefreshLayout != null) {
                    SelectGoodsActivity.this.mPullToRefreshLayout.refreshFinish(0);
                    SelectGoodsActivity.this.mPullToRefreshLayout.loadmoreFinish(0);
                }
                try {
                    Log.i("selectgoods", param1String);
                    GetGoodsListResp getGoodsListResp = ParseGetGoodsListResp.parse(param1String);
                    if (getGoodsListResp != null && getGoodsListResp.isSuccess()) {
                        goodsAllAttrs=getGoodsListResp.getGoodsAllAttrs();
//                        SelectGoodsActivity.access$202(SelectGoodsActivity.this, getGoodsListResp.getGoodsAllAttrs());
                        if (initFilterDropDownView)
                            SelectGoodsActivity.this.initFilterDropDownView(SelectGoodsActivity.this.goodsAllAttrs);
                        List list = getGoodsListResp.getGoodses();
                        if (1 == page) {
                            goodses=list;
//                            SelectGoodsActivity.access$402(SelectGoodsActivity.this, list);
                        } else if (SelectGoodsActivity.this.goodses != null) {
                            SelectGoodsActivity.this.goodses.addAll(list);
                            if (list.isEmpty())
                                Toast.makeText(SelectGoodsActivity.this, "没有更多内容了", Toast.LENGTH_LONG).show();
                        }
                        SelectGoodsActivity.this.mAdapter.notifyDataSetChanged();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }); }

    private void initFilterDropDownView(List<GoodsAllAttr> paramList) {
        if (this.itemPosList.size() < paramList.size()) {
            this.itemPosList.add(Integer.valueOf(0));
            this.itemPosList.add(Integer.valueOf(0));
            this.itemPosList.add(Integer.valueOf(0));
        }
        if (this.itemPosList.size() < 0)
            this.itemPosList.remove(0);
        this.itemPosList.add(0, Integer.valueOf(0));
        ProDropMenuAdapter proDropMenuAdapter = new ProDropMenuAdapter(this, paramList, this.itemPosList, this);
        dropDownMenu.setMenuAdapter(proDropMenuAdapter);
    }

    private void initView() {
        this.isSelectGoods = getIntent().getBooleanExtra("isselectedGoods", false);
        this.mScreenWidth = (getResources().getDisplayMetrics()).widthPixels;
        this.pd = (ProgressBar)findViewById(R.id.pd);
        dropDownMenu = (DropDownMenu)findViewById(R.id.dropDownMenu);
        this.mGridView = (GridView)findViewById(R.id.gridView);
        this.mGridView.setOnItemClickListener(this);
        this.mAdapter = new MyAdapter();
        this.mGridView.setAdapter(this.mAdapter);
        this.mPullToRefreshLayout = (PullToRefreshLayout)findViewById(R.id.mFilterContentView);
        this.mPullToRefreshLayout.setOnRefreshListener(this);
        View view = findViewById(R.id.select_rl);
        this.select_num_tv = (TextView)findViewById(R.id.select_num_tv);
        view.setOnClickListener(this);
        if (this.isSelectGoods) {
            view.setVisibility(View.VISIBLE);
            this.select_num_tv.setText("" + MyApplication.mSelectProducts.size());
        }
    }

    public void onClick(View paramView) {
        setResult(52031, new Intent());
        finish();
    }

    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_select_goods);
        initView();
        this.mNetwork = new Network();
        this.fitterStr = this.goodsIds[0] + "." + this.goodsIds[1] + "." + this.goodsIds[2];
        Log.v("520", "fitterStr" + this.fitterStr);
        this.apiService = (HDApiService)HDRetrofit.create(HDApiService.class);
        this.page = 1;
        getGoodsList("0", this.page, null, null, this.fitterStr, true);
        this.pd.setVisibility(View.VISIBLE);
    }

    public void onFilterDone(int page1, int page2, String paramString) {
        dropDownMenu.close();
        if (page2 == 0)
            paramString = ((GoodsAllAttr)this.goodsAllAttrs.get(page1)).getAttrName();
        dropDownMenu.setPositionIndicatorText(page1, paramString);
        if (page1 < this.itemPosList.size())
            this.itemPosList.remove(page1);
        this.itemPosList.add(page1, Integer.valueOf(page2));
        int j = ((GoodsAttr)((GoodsAllAttr)this.goodsAllAttrs.get(page1)).getGoodsAttrs().get(page2)).getGoods_id();
        int i = j;
        if (page2 != 0) {
            i = j;
            if (j == 0)
                i = 999;
        }
        this.goodsIds[page1] = Integer.valueOf(i);
        this.fitterStr = this.goodsIds[0] + "." + this.goodsIds[1] + "." + this.goodsIds[2];
        this.page = 1;
        getGoodsList("0", this.page, null, null, this.fitterStr, false);
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int page, long paramLong) {
        if (paramAdapterView == this.mGridView) {
            if (this.isSelectGoods) {
                byte b;
                for (b = 0; b < MyApplication.mSelectProducts.size(); b++) {
                    if (((Goods)MyApplication.mSelectProducts.get(b)).getName().equals(((Goods)this.goodses.get(page)).getName())) {
                        MyApplication.mSelectProducts.remove(b);
                        this.mAdapter.notifyDataSetChanged();
                        this.select_num_tv.setText(MyApplication.mSelectProducts.size() + "");
                        return;
                    }
                }
                Goods goods = new Goods();
                goods.setId(((Goods)this.goodses.get(page)).getId());
                goods.setImg_url(((Goods)this.goodses.get(page)).getImg_url());
                goods.setShop_price(((Goods)this.goodses.get(page)).getShop_price());
                goods.setName(((Goods)this.goodses.get(page)).getName());
                MyApplication.mSelectProducts.add(goods);
                this.mAdapter.notifyDataSetChanged();
                this.select_num_tv.setText(MyApplication.mSelectProducts.size() + "");
                return;
            }
        } else {
            return;
        }
        Intent intent = new Intent(this, ProDetailActivity.class);
        intent.putExtra("id", ((Goods)this.goodses.get(page)).getId());
        intent.putExtra("imgurl", ((Goods)this.goodses.get(page)).getImg_url());
        intent.putExtra("goodsname", ((Goods)this.goodses.get(page)).getName());
        startActivity(intent);
    }

    public void onLoadMore(PullToRefreshLayout paramPullToRefreshLayout) {
        int i = this.page + 1;
        this.page = i;
        getGoodsList("0", i, null, null, this.fitterStr, false);
    }

    public void onRefresh(PullToRefreshLayout paramPullToRefreshLayout) {
        this.page = 1;
        getGoodsList("0", this.page, null, null, this.fitterStr, false);
    }

    private class MyAdapter extends BaseAdapter {
        private ImageLoader imageLoader = ImageLoader.getInstance();

        private DisplayImageOptions options = (new DisplayImageOptions.Builder()).showImageOnLoading(R.mipmap.bg_default).showImageForEmptyUri(R.mipmap.bg_default).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();

        public int getCount() { return (SelectGoodsActivity.this.goodses == null) ? 0 : SelectGoodsActivity.this.goodses.size(); }

        public Goods getItem(int param1Int) { return (SelectGoodsActivity.this.goodses == null) ? null : (Goods)SelectGoodsActivity.this.goodses.get(param1Int); }

        public long getItemId(int param1Int) { return param1Int; }

        public View getView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
            ViewHolder viewHolder;
            if (param1View == null) {
                param1View = View.inflate(SelectGoodsActivity.this, R.layout.item_gridview_fm_product, null);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView)param1View.findViewById(R.id.imageView);
                viewHolder.textView = (TextView)param1View.findViewById(R.id.textView);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)viewHolder.imageView.getLayoutParams();
                float f = (SelectGoodsActivity.this.mScreenWidth - ConvertUtil.dp2px(SelectGoodsActivity.this, 45.8F)) / 2.0F;
                viewHolder.check_iv = (ImageView)param1View.findViewById(R.id.check_iv);
                layoutParams.height = (int)f;
                viewHolder.imageView.setLayoutParams(layoutParams);
                viewHolder.check_iv.setLayoutParams(layoutParams);
                param1View.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder)param1View.getTag();
            }
            viewHolder.textView.setText(((Goods)SelectGoodsActivity.this.goodses.get(param1Int)).getName());
            this.imageLoader.displayImage("http://yangguang.bocang.cc/App/yangguang/Public/uploads/goods/" + ((Goods)SelectGoodsActivity.this.goodses.get(param1Int)).getImg_url() + "!400X400.png", viewHolder.imageView, this.options);
            viewHolder.check_iv.setVisibility(View.GONE);
            if (SelectGoodsActivity.this.isSelectGoods) {
                int  b;
                for (b = 0;; b++) {
                    if (b < MyApplication.mSelectProducts.size()) {
                        String str = ((Goods)MyApplication.mSelectProducts.get(b)).getName();
                        if (((Goods)SelectGoodsActivity.this.goodses.get(param1Int)).getName().equals(str)) {
                            viewHolder.check_iv.setVisibility(View.VISIBLE);
                            return param1View;
                        }
                    } else {
                        return param1View;
                    }
                }
            }
            return param1View;
        }

        class ViewHolder {
            ImageView check_iv;

            ImageView imageView;

            TextView textView;
        }
    }
}
