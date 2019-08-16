package cc.bocang.bocang.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import cc.bocang.bocang.R;
import cc.bocang.bocang.bean.ProgrameBean;
import cc.bocang.bocang.global.Constant;
import cc.bocang.bocang.net.OkHttpUtils;
import cc.bocang.bocang.utils.DateUtils;
import cc.bocang.bocang.utils.UIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lib.common.hxp.view.PullToRefreshLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class ProgrammeActivity extends BaseActivity implements AdapterView.OnItemClickListener, PullToRefreshLayout.OnRefreshListener {
    private static final String TAG = "programme";

    private String fitterStr;

    private List<Integer> itemPosList = new ArrayList();

    private MyAdapter mAdapter;

    private GridView mGridView;

    private PullToRefreshLayout mPullToRefreshLayout;

    private int mScreenWidth;

    private int page = 1;

    private ProgressBar pd;

    private Integer[] sceneIds = { 0,0,0};

    private List<ProgrameBean> scenes;

    private void getProgrammeList(final int page, boolean paramBoolean) {
        this.pd.setVisibility(View.VISIBLE);
        OkHttpUtils.getProgrammeList(page, new Callback() {
            public void onFailure(Call param1Call, IOException param1IOException) { ProgrammeActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    if (ProgrammeActivity.this!= null && !ProgrammeActivity.this.isFinishing()) {
                        pd.setVisibility(View.GONE);
//                        ProgrammeActivity.access$110(ProgrammeActivity.this);
                        if (ProgrammeActivity.this.mPullToRefreshLayout != null) {
                            ProgrammeActivity.this.mPullToRefreshLayout.refreshFinish(0);
                            ProgrammeActivity.this.mPullToRefreshLayout.loadmoreFinish(0);
                            return;
                        }
                    }
                }
            }); }

            public void onResponse(Call param1Call, Response param1Response) throws IOException {
                try {
                    JSONObject jSONObject = new JSONObject(param1Response.body().string());
                    final List programeBeans = (List)(new Gson()).fromJson(jSONObject.getJSONArray(Constant.result).toString(), (new TypeToken<List<ProgrameBean>>() {

                    }).getType());
                    if (programeBeans != null && programeBeans.size() > 0)
                        ProgrammeActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                if (ProgrammeActivity.this == null || ProgrammeActivity.this.isFinishing())
                                return;
                                ProgrammeActivity.this.pd.setVisibility(View.GONE);
                                if (ProgrammeActivity.this.mPullToRefreshLayout != null) {
                                    ProgrammeActivity.this.mPullToRefreshLayout.refreshFinish(0);
                                    ProgrammeActivity.this.mPullToRefreshLayout.loadmoreFinish(0);
                                }
                                if (1 == page) {
                                    scenes=programeBeans;
//                                    ProgrammeActivity.access$302(ProgrammeActivity.this, programeBeans);
                                } else if (ProgrammeActivity.this.scenes != null) {
                                    ProgrammeActivity.this.scenes.addAll(programeBeans);
                                    if (programeBeans.isEmpty())
                                        Toast.makeText(ProgrammeActivity.this, R.string.no_more, Toast.LENGTH_LONG).show();
                                }
                                ProgrammeActivity.this.mAdapter.notifyDataSetChanged();
                            }
                        });
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            }
        });
    }

    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_programme);
        this.mScreenWidth = (getResources().getDisplayMetrics()).widthPixels;
        this.pd = (ProgressBar)findViewById(R.id.pd);
        this.mGridView = (GridView)findViewById(R.id.gridView);
        this.mGridView.setOnItemClickListener(this);
        this.mAdapter = new MyAdapter();
        this.mGridView.setAdapter(this.mAdapter);
        this.mPullToRefreshLayout = (PullToRefreshLayout)findViewById(R.id.mFilterContentView);
        this.mPullToRefreshLayout.setOnRefreshListener(this);
        getProgrammeList(this.page, true);
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
        Intent intent = new Intent(this, ProgrammeDetailActivity.class);
        intent.putExtra(Constant.data, (new Gson()).toJson(this.scenes.get(paramInt), ProgrameBean.class));
        startActivity(intent);
    }

    public void onLoadMore(PullToRefreshLayout paramPullToRefreshLayout) {
        int i = this.page + 1;
        this.page = i;
        getProgrammeList(i, false);
    }

    public void onRefresh(PullToRefreshLayout paramPullToRefreshLayout) {
        this.page = 1;
        getProgrammeList(this.page, false);
    }

    private class MyAdapter extends BaseAdapter {
        private ImageLoader imageLoader = ImageLoader.getInstance();

        private DisplayImageOptions options = (new DisplayImageOptions.Builder()).showImageOnLoading(R.mipmap.bg_default).showImageForEmptyUri(R.mipmap.bg_default).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();

        public int getCount() { return (ProgrammeActivity.this.scenes == null) ? 0 : ProgrammeActivity.this.scenes.size(); }

        public ProgrameBean getItem(int param1Int) { return (ProgrammeActivity.this.scenes == null) ? null : (ProgrameBean)ProgrammeActivity.this.scenes.get(param1Int); }

        public long getItemId(int param1Int) { return param1Int; }

        public View getView(final int position, View param1View, ViewGroup param1ViewGroup) {
            if (param1View == null) {
                param1View = View.inflate(ProgrammeActivity.this, R.layout.item_gridview_programme_scene, null);
                ViewHolder viewHolder1 = new ViewHolder();
                viewHolder1.imageView = (ImageView)param1View.findViewById(R.id.imageView);
                viewHolder1.textView = (TextView)param1View.findViewById(R.id.textView);
                viewHolder1.tv_name = (TextView)param1View.findViewById(R.id.tv_name);
                viewHolder1.tv_share = (TextView)param1View.findViewById(R.id.tv_share);
                param1View.setTag(viewHolder1);
                viewHolder1.textView.setText(((ProgrameBean)ProgrammeActivity.this.scenes.get(position)).getName());
                viewHolder1.tv_name.setText("" + DateUtils.getStrTime(((ProgrameBean)ProgrammeActivity.this.scenes.get(position)).getAdd_time()));
                viewHolder1.tv_share.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View param2View) { ImageLoader.getInstance().loadImage("http://yangguang.bocang.cc/App/yangguang/Public/uploads//plan/" + scenes.get(position).getPath(), new ImageLoadingListener() {
                        public void onLoadingCancelled(String param3String, View param3View) {}

                        public void onLoadingComplete(String param3String, View param3View, Bitmap param3Bitmap) { UIUtils.showShareDialog(ProgrammeActivity.this, param3Bitmap, "", param3String); }

                        public void onLoadingFailed(String param3String, View param3View, FailReason param3FailReason) {}

                        public void onLoadingStarted(String param3String, View param3View) {}
                    }); }
                });
                this.imageLoader.displayImage("http://yangguang.bocang.cc/App/yangguang/Public/uploads//plan/" + ((ProgrameBean)ProgrammeActivity.this.scenes.get(position)).getPath(), viewHolder1.imageView, this.options);
                return param1View;
            }
            ViewHolder viewHolder = (ViewHolder)param1View.getTag();
            viewHolder.textView.setText(((ProgrameBean)ProgrammeActivity.this.scenes.get(position)).getName());
            viewHolder.tv_name.setText("" + DateUtils.getStrTime(((ProgrameBean)ProgrammeActivity.this.scenes.get(position)).getAdd_time()));
            viewHolder.tv_share.setOnClickListener(new View.OnClickListener() {
                public void onClick(View param2View) { ImageLoader.getInstance().loadImage("http://yangguang.bocang.cc/App/yangguang/Public/uploads//plan/" + scenes.get(position).getPath(), new ImageLoadingListener() {
                    public void onLoadingCancelled(String param3String, View param3View) {}

                    public void onLoadingComplete(String param3String, View param3View, Bitmap param3Bitmap) { UIUtils.showShareDialog(ProgrammeActivity.this, param3Bitmap, "", param3String); }

                    public void onLoadingFailed(String param3String, View param3View, FailReason param3FailReason) {}

                    public void onLoadingStarted(String param3String, View param3View) {}
                }); }
            });
            this.imageLoader.displayImage("http://yangguang.bocang.cc/App/yangguang/Public/uploads//plan/" + ((ProgrameBean)ProgrammeActivity.this.scenes.get(position)).getPath(), viewHolder.imageView, this.options);
            return param1View;
        }

        class ViewHolder {
            ImageView imageView;

            TextView textView;

            TextView tv_name;

            TextView tv_share;
        }
    }
//
//    class null implements View.OnClickListener {
//        public void onClick(View param1View) { ImageLoader.getInstance().loadImage("http://yangguang.bocang.cc/App/yangguang/Public/uploads//plan/" + ((ProgrameBean)this.this$1.scenes.get(position)).getPath(), new ImageLoadingListener() {
//            public void onLoadingCancelled(String param3String, View param3View) {}
//
//            public void onLoadingComplete(String param3String, View param3View, Bitmap param3Bitmap) {
//                UIUtils.showShareDialog(this.this$2.this$1, param3Bitmap, "", param3String); }
//
//            public void onLoadingFailed(String param3String, View param3View, FailReason param3FailReason) {}
//
//            public void onLoadingStarted(String param3String, View param3View) {}
//        }); }
//    }

//    class null implements ImageLoadingListener {
//        public void onLoadingCancelled(String param1String, View param1View) {}
//
//        public void onLoadingComplete(String param1String, View param1View, Bitmap param1Bitmap) { UIUtils.showShareDialog(this.this$2.this$1, param1Bitmap, "", param1String); }
//
//        public void onLoadingFailed(String param1String, View param1View, FailReason param1FailReason) {}
//
//        public void onLoadingStarted(String param1String, View param1View) {}
//    }

    class ViewHolder {
        ImageView imageView;

        TextView textView;

        TextView tv_name;

        TextView tv_share;

        ViewHolder() {}
    }
}
