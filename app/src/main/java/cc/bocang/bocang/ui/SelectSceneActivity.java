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
import cc.bocang.bocang.data.model.GoodsAllAttr;
import cc.bocang.bocang.data.model.GoodsAttr;
import cc.bocang.bocang.data.model.Scene;
import cc.bocang.bocang.data.model.SceneAllAttr;
import cc.bocang.bocang.data.parser.ParseGetSceneListResp;
import cc.bocang.bocang.data.response.GetSceneListResp;
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

public class SelectSceneActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, OnFilterDoneListener, View.OnClickListener {
    private static final String TAG = "selectgoods";

    static DropDownMenu dropDownMenu;

    private HDApiService apiService;

    private String fitterStr;

    private List<GoodsAllAttr> goodsAllAttrs;

    private Integer[] goodsIds = { 0,0,0 };

    private boolean isSelectGoods;

    private List<Integer> itemPosList = new ArrayList();

    private MyAdapter mAdapter;

    private GridView mGridView;

    private Network mNetwork;

    private PullToRefreshLayout mPullToRefreshLayout;

    private int mScreenWidth;

    private int page = 1;

    private ProgressBar pd;

    private List<SceneAllAttr> sceneAllAttrs;

    private List<Scene> scenes;

    private TextView select_num_tv;

    private void callSceneList(HDApiService paramHDApiService, int paramInt1, final int page, String paramString1, String paramString2, String paramString3, final boolean initFilterDropDownView) {
        this.pd.setVisibility(View.VISIBLE);
        this.mNetwork.sendSceneList(paramInt1 + "", page, paramString1, paramString2, paramString3, this, new HttpListener() {
            public void onFailureListener(int param1Int, String param1String) {
                if (SelectSceneActivity.this != null && !SelectSceneActivity.this.isFinishing()) {
                    SelectSceneActivity.this.pd.setVisibility(View.GONE);
//                    SelectSceneActivity.access$610(SelectSceneActivity.this);

                    if (SelectSceneActivity.this.mPullToRefreshLayout != null) {
                        SelectSceneActivity.this.mPullToRefreshLayout.refreshFinish(0);
                        SelectSceneActivity.this.mPullToRefreshLayout.loadmoreFinish(0);
                        return;
                    }
                }
            }

            public void onSuccessListener(int param1Int, String param1String) {
                if (SelectSceneActivity.this != null && !SelectSceneActivity.this.isFinishing()) {
                    SelectSceneActivity.this.pd.setVisibility(View.GONE);
                    if (SelectSceneActivity.this.mPullToRefreshLayout != null) {
                        SelectSceneActivity.this.mPullToRefreshLayout.refreshFinish(0);
                        SelectSceneActivity.this.mPullToRefreshLayout.loadmoreFinish(0);
                    }
                    try {
                        Log.i("selectgoods", param1String);
                        GetSceneListResp getSceneListResp = ParseGetSceneListResp.parse(param1String);
                        if (getSceneListResp != null && getSceneListResp.isSuccess()) {
                            sceneAllAttrs=getSceneListResp.getSceneAllAttrs();
//                            SelectSceneActivity.access$202(SelectSceneActivity.this, getSceneListResp.getSceneAllAttrs());
                            if (initFilterDropDownView)
                                SelectSceneActivity.this.initFilterDropDownView(SelectSceneActivity.this.sceneAllAttrs);
                            List list = getSceneListResp.getScenes();
                            if (1 == page) {
                                scenes=list;
//                                SelectSceneActivity.access$402(SelectSceneActivity.this, list);
                            } else if (SelectSceneActivity.this.scenes != null) {
                                SelectSceneActivity.this.scenes.addAll(list);
                                if (list.isEmpty())
                                    Toast.makeText(SelectSceneActivity.this, getString(R.string.no_more), Toast.LENGTH_SHORT).show();
                            }
                            SelectSceneActivity.this.mAdapter.notifyDataSetChanged();
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void initFilterDropDownView(List<SceneAllAttr> paramList) {
        if (this.itemPosList.size() < paramList.size()) {
            this.itemPosList.add(Integer.valueOf(0));
            this.itemPosList.add(Integer.valueOf(0));
            this.itemPosList.add(Integer.valueOf(0));
        }
        SceneDropMenuAdapter sceneDropMenuAdapter = new SceneDropMenuAdapter(this, paramList, this.itemPosList, this);
        dropDownMenu.setMenuAdapter(sceneDropMenuAdapter);
    }

    private void initView() {
        this.isSelectGoods = getIntent().getBooleanExtra("ISSELECTSCRENES", false);
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
        if (this.isSelectGoods)
            view.setVisibility(View.VISIBLE);
    }

    public void onClick(View paramView) {
//        switch (paramView.getId()) {
//            default:
//                return;
//            case 2131558645:
//                break;
//        }
        setResult(52031, new Intent());
        finish();
    }

    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_scene_select);
        initView();
        this.mNetwork = new Network();
        this.fitterStr = this.goodsIds[0] + "." + this.goodsIds[1] + "." + this.goodsIds[2];
        Log.v("520", "fitterStr" + this.fitterStr);
        this.apiService = (HDApiService)HDRetrofit.create(HDApiService.class);
        this.page = 1;
        callSceneList(this.apiService, 0, this.page, null, null, this.fitterStr, true);
        this.pd.setVisibility(View.VISIBLE);
    }

    public void onFilterDone(int paramInt1, int paramInt2, String paramString) {
        dropDownMenu.close();
        if (paramInt2 == 0)
            paramString = ((GoodsAllAttr)this.goodsAllAttrs.get(paramInt1)).getAttrName();
        dropDownMenu.setPositionIndicatorText(paramInt1, paramString);
        if (paramInt1 < this.itemPosList.size())
            this.itemPosList.remove(paramInt1);
        this.itemPosList.add(paramInt1, Integer.valueOf(paramInt2));
        int j = ((GoodsAttr)((GoodsAllAttr)this.goodsAllAttrs.get(paramInt1)).getGoodsAttrs().get(paramInt2)).getGoods_id();
        int i = j;
        if (paramInt2 != 0) {
            i = j;
            if (j == 0)
                i = 999;
        }
        this.goodsIds[paramInt1] = Integer.valueOf(i);
        this.fitterStr = this.goodsIds[0] + "." + this.goodsIds[1] + "." + this.goodsIds[2];
        this.page = 1;
        callSceneList(this.apiService, 0, this.page, null, null, this.fitterStr, true);
//        if (Constant.isDebug)
//            Toast.makeText(this, paramString + " titlePos���" + paramInt1 + "  itemPos���" + paramInt2, 0).show();
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
        if (paramAdapterView == this.mGridView) {
//            if (Constant.isDebug)
//                Toast.makeText(this, "���������" + paramInt, 0).show();
            if (this.isSelectGoods) {
                int  b;
                for (b = 0; b < MyApplication.mSelectScenes.size(); b++) {
                    if (((Scene)MyApplication.mSelectScenes.get(b)).getId().equals(((Scene)this.scenes.get(paramInt)).getId())) {
                        MyApplication.mSelectScenes.remove(b);
                        this.mAdapter.notifyDataSetChanged();
                        this.select_num_tv.setText(MyApplication.mSelectScenes.size() + "");
                        return;
                    }
                }
                Scene scene = new Scene();
                scene.setId(((Scene)this.scenes.get(paramInt)).getId());
                scene.setPath(((Scene)this.scenes.get(paramInt)).getPath());
                scene.setName(((Scene)this.scenes.get(paramInt)).getName());
                MyApplication.mSelectScenes.add(scene);
                this.mAdapter.notifyDataSetChanged();
                this.select_num_tv.setText(MyApplication.mSelectProducts.size() + "");
                return;
            }
        } else {
            return;
        }
        Intent intent = new Intent(this, DiyActivity.class);
        intent.putExtra("from", "scene");
        if (Integer.parseInt(((Scene)this.scenes.get(paramInt)).getId()) > 1551) {
            intent.putExtra("path", "http://yangguang.bocang.cc/App/yangguang/Public/uploads/scene/" + ((Scene)this.scenes.get(paramInt)).getPath());
        } else {
            intent.putExtra("path", "http://bocang.oss-cn-shenzhen.aliyuncs.com/scene/" + ((Scene)this.scenes.get(paramInt)).getPath());
        }
        startActivity(intent);
    }

    public void onLoadMore(PullToRefreshLayout paramPullToRefreshLayout) {
        HDApiService hDApiService = this.apiService;
        int i = this.page + 1;
        this.page = i;
        callSceneList(hDApiService, 0, i, null, null, this.fitterStr, true);
    }

    public void onRefresh(PullToRefreshLayout paramPullToRefreshLayout) {
        this.page = 1;
        callSceneList(this.apiService, 0, this.page, null, null, this.fitterStr, true);
    }

    private class MyAdapter extends BaseAdapter {
        private ImageLoader imageLoader = ImageLoader.getInstance();

        private DisplayImageOptions options = (new DisplayImageOptions.Builder())
                .showImageOnLoading(R.mipmap.bg_default).showImageForEmptyUri(R.mipmap.bg_default).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();

        public int getCount() { return (SelectSceneActivity.this.scenes == null) ? 0 : SelectSceneActivity.this.scenes.size(); }

        public Scene getItem(int param1Int) { return (SelectSceneActivity.this.scenes == null) ? null : (Scene)SelectSceneActivity.this.scenes.get(param1Int); }

        public long getItemId(int param1Int) { return param1Int; }

        public View getView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
            ViewHolder viewHolder;
            if (param1View == null) {
                param1View = View.inflate(SelectSceneActivity.this, R.layout.item_gridview_fm_scene, null);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView) param1View.findViewById(R.id.imageView);
                viewHolder.textView = (TextView) param1View.findViewById(R.id.textView);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)viewHolder.imageView.getLayoutParams();
                float f = (SelectSceneActivity.this.mScreenWidth - ConvertUtil.dp2px(SelectSceneActivity.this, 45.8F)) / 2.0F;
                viewHolder.check_iv = (ImageView)param1View.findViewById(R.id.check_iv);
                layoutParams.height = (int)f;
                viewHolder.imageView.setLayoutParams(layoutParams);
                viewHolder.check_iv.setLayoutParams(layoutParams);
                param1View.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder)param1View.getTag();
            }
            viewHolder.textView.setText(((Scene)SelectSceneActivity.this.scenes.get(param1Int)).getName());
            if (Integer.parseInt(((Scene)SelectSceneActivity.this.scenes.get(param1Int)).getId()) > 1551) {
                this.imageLoader.displayImage("http://yangguang.bocang.cc/App/yangguang/Public/uploads/scene/" + ((Scene)SelectSceneActivity.this.scenes.get(param1Int)).getPath() + "!400X400.png", viewHolder.imageView, this.options);
            } else {
                this.imageLoader.displayImage("http://bocang.oss-cn-shenzhen.aliyuncs.com/scene/" + ((Scene)SelectSceneActivity.this.scenes.get(param1Int)).getPath() + "!400X400.png", viewHolder.imageView, this.options);
            }
            viewHolder.check_iv.setVisibility(View.GONE);
            if (SelectSceneActivity.this.isSelectGoods) {
                int b;
                for (b = 0;; b++) {
                    if (b < MyApplication.mSelectScenes.size()) {
                        String str = ((Scene)MyApplication.mSelectScenes.get(b)).getId();
                        if (((Scene)SelectSceneActivity.this.scenes.get(param1Int)).getId().equals(str)) {
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
