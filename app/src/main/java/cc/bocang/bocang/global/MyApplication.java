package cc.bocang.bocang.global;

import android.app.Application;
import android.util.Log;
import cc.bocang.bocang.data.model.Goods;
import cc.bocang.bocang.data.model.Scene;
import cc.bocang.bocang.data.model.UserInfo;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.OkHttpNetworkExecutor;
import com.yanzhenjie.nohttp.cache.DBCacheStore;
import com.yanzhenjie.nohttp.cookie.DBCookieStore;
import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {
	public static int mLightIndex;
    private static MyApplication instance;

	public static List<Goods> mSelectProducts = new ArrayList();

	public static List<Scene> mSelectScenes = new ArrayList();

	private final String TAG = MyApplication.class.getSimpleName();

	public UserInfo mUserInfo;

	public static MyApplication getInstance() { return instance; }

	private void initImageLoader() {
		DisplayImageOptions displayImageOptions = (new DisplayImageOptions.Builder()).cacheInMemory(true).cacheOnDisk(true).build();
		ImageLoaderConfiguration imageLoaderConfiguration = (new ImageLoaderConfiguration.Builder(getApplicationContext())).defaultDisplayImageOptions(displayImageOptions).threadPriority(3).denyCacheImageMultipleSizesInMemory().diskCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(imageLoaderConfiguration);
	}

	private void initNoHttp() {
		NoHttp.initialize(this);
		NoHttp.initialize(this, (new NoHttp.Config()).setConnectTimeout(30000).setReadTimeout(30000).setCacheStore(new DBCacheStore(this)).setCookieStore(new DBCookieStore(this)).setNetworkExecutor(new OkHttpNetworkExecutor()));
	}

	public void onCreate() {
		Log.i(this.TAG, "==============================Application onCreate==========================");
		super.onCreate();
		instance = this;
		mSelectProducts = new ArrayList();
		mSelectScenes = new ArrayList();
		initImageLoader();
		initNoHttp();
	}
}
