package cc.bocang.bocang.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cc.bocang.bocang.R;
import cc.bocang.bocang.adapter.BaseAdapterHelper;
import cc.bocang.bocang.adapter.QuickAdapter;
import cc.bocang.bocang.bean.BrandBean;
import cc.bocang.bocang.global.Constant;
import cc.bocang.bocang.net.OkHttpUtils;
import cc.bocang.bocang.utils.LogUtils;
import cc.bocang.bocang.utils.UIUtils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.io.IOException;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BrandListActivity extends BaseActivity implements View.OnClickListener {
    private List<BrandBean.Data> data;

    private GridView gv_brandlist;

    private int mScreenWidth;

    private String result;

    public void onClick(View paramView) {
        if (this.data == null || this.data.size() == 0) {
            Toast.makeText(this, R.string.loading, Toast.LENGTH_LONG).show();
            return;
        }
    }

    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_brand_list);
        this.gv_brandlist = (GridView)findViewById(R.id.gv_brandlist);
        this.mScreenWidth = UIUtils.getScreenWidth(this);
        final QuickAdapter<BrandBean.Data> adapter = new QuickAdapter<BrandBean.Data>(this, R.layout.item_brand_list) {
            protected void convert(BaseAdapterHelper param1BaseAdapterHelper, BrandBean.Data param1Data) {
                ImageView imageView = (ImageView)param1BaseAdapterHelper.getView(R.id.iv_img);
                TextView textView = (TextView)param1BaseAdapterHelper.getView(R.id.tv_name);
                View view = param1BaseAdapterHelper.getView(R.id.view_bg);
                imageView.setLayoutParams(new RelativeLayout.LayoutParams((BrandListActivity.this.mScreenWidth - UIUtils.dip2PX(15)) / 2, (BrandListActivity.this.mScreenWidth - UIUtils.dip2PX(15)) / 2 * 100 / 168));
                textView.setLayoutParams(new RelativeLayout.LayoutParams((BrandListActivity.this.mScreenWidth - UIUtils.dip2PX(15)) / 2, (BrandListActivity.this.mScreenWidth - UIUtils.dip2PX(15)) / 2 * 100 / 168));
                view.setLayoutParams(new RelativeLayout.LayoutParams((BrandListActivity.this.mScreenWidth - UIUtils.dip2PX(15)) / 2, (BrandListActivity.this.mScreenWidth - UIUtils.dip2PX(15)) / 2 * 100 / 168));
                ImageLoader.getInstance().displayImage("http://yangguang.bocang.cc/App/yangguang/Public/uploads/" + param1Data.getPath(), imageView);
                param1BaseAdapterHelper.setText(R.id.tv_name, param1Data.getName());
            }
        };
        this.gv_brandlist.setAdapter(adapter);
        OkHttpUtils.getBrandList(new Callback() {
            public void onFailure(Call param1Call, IOException param1IOException) {}

            public void onResponse(Call param1Call, Response param1Response) throws IOException {
                BrandBean brandBean = (BrandBean)(new Gson()).fromJson(param1Response.body().string(), BrandBean.class);
                if (brandBean != null && brandBean.getData() != null && brandBean.getData().size() > 0) {
                    data=brandBean.getData();
//                    BrandListActivity.access$102(BrandListActivity.this, brandBean.getData());
                    BrandListActivity.this.runOnUiThread(new Runnable() {
                        public void run() { adapter.replaceAll(data); }
                    });
                }
            }
        });
        this.gv_brandlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> param1AdapterView, View param1View, final int position, long param1Long) { OkHttpUtils.getbrandDetail(((BrandBean.Data)BrandListActivity.this.data.get(position)).getId(), new Callback() {
                public void onFailure(Call param2Call, IOException param2IOException) {}

                public void onResponse(Call param2Call, Response param2Response) throws IOException {
                    try {
//                        BrandListActivity.access$202(BrandListActivity.this, param2Response.body().string());
//                        LogUtils.logE("detail", BrandListActivity.this.result);
                        result=param2Response.body().string();
                        if (result != null) {
                            if (data.get(position).getType().equals("1")) {
                                JSONObject jSONObject = new JSONObject(result);
                                Intent intent = new Intent(BrandListActivity.this, WebViewActivity.class);
                                String str = jSONObject.getString(Constant.content);
                                intent.putExtra(Constant.local_data, str);
                                startActivity(intent);
                                return;
                            }
                            if (!((BrandBean.Data)BrandListActivity.this.data.get(position)).getType().equals("2"))
                            if (((BrandBean.Data)BrandListActivity.this.data.get(position)).getType().equals("3")) {
                                JSONObject jSONObject = new JSONObject(BrandListActivity.this.result);
                                if (jSONObject != null) {
                                    JSONArray jSONArray = jSONObject.getJSONArray(Constant.data);
                                    if (jSONArray != null && jSONArray.length() > 0) {
                                        Intent intent = new Intent(BrandListActivity.this, BrandPlayActivity.class);
                                        intent.putExtra(Constant.url, jSONArray.getJSONObject(0).getString(Constant.filepath));
                                        BrandListActivity.this.startActivity(intent);
                                        return;
                                    }
                                }
                            } else if (((BrandBean.Data)BrandListActivity.this.data.get(position)).getType().equals("4")) {
                                Intent intent = new Intent(BrandListActivity.this, GalleryListActivity.class);
                                intent.putExtra(Constant.result, BrandListActivity.this.result);
                                intent.putExtra(Constant.title, ((BrandBean.Data)BrandListActivity.this.data.get(position)).getName());
                                BrandListActivity.this.startActivity(intent);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }); }
        });
    }
}
