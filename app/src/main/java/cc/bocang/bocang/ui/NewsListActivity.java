package cc.bocang.bocang.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import cc.bocang.bocang.R;
import cc.bocang.bocang.adapter.BaseAdapterHelper;
import cc.bocang.bocang.adapter.QuickAdapter;
import cc.bocang.bocang.bean.NewsBean;
import cc.bocang.bocang.global.Constant;
import cc.bocang.bocang.net.OkHttpUtils;
import cc.bocang.bocang.utils.DateUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.io.IOException;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NewsListActivity extends BaseActivity {
    private List<NewsBean> mArticlesBeans;

    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_news_list);
        ListView listView = (ListView)findViewById(R.id.lv_news);
        final QuickAdapter<NewsBean> adapter = new QuickAdapter<NewsBean>(this, R.layout.item_news_list) {
            protected void convert(BaseAdapterHelper param1BaseAdapterHelper, NewsBean param1NewsBean) {
                ImageView imageView = (ImageView)param1BaseAdapterHelper.getView(R.id.iv_img);
                param1BaseAdapterHelper.setText(R.id.tv_name, param1NewsBean.getName());
                param1BaseAdapterHelper.setText(R.id.tv_time, "" + DateUtils.getStrTime(param1NewsBean.getTime()));
                ImageLoader.getInstance().displayImage("http://yangguang.bocang.cc/App/yangguang/Public/uploads/" + param1NewsBean.getPath(), imageView);
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
                Intent intent = new Intent(NewsListActivity.this, NewsDetailActivity.class);
                intent.putExtra(Constant.id, ((NewsBean)NewsListActivity.this.mArticlesBeans.get(param1Int)).getId());
                intent.putExtra(Constant.title, ((NewsBean)NewsListActivity.this.mArticlesBeans.get(param1Int)).getName());
                NewsListActivity.this.startActivity(intent);
            }
        });
        OkHttpUtils.getNews(new Callback() {
            public void onFailure(Call param1Call, IOException param1IOException) {}

            public void onResponse(Call param1Call, Response param1Response) throws IOException {
                mArticlesBeans=new Gson().fromJson(param1Response.body().string(), new TypeToken<List<NewsBean>>() {}.getType());
                if (NewsListActivity.this.mArticlesBeans != null && NewsListActivity.this.mArticlesBeans.size() > 0)
                    NewsListActivity.this.runOnUiThread(new Runnable() {
                        public void run() { adapter.replaceAll(NewsListActivity.this.mArticlesBeans); }
                    });
            }
        });
    }
}
