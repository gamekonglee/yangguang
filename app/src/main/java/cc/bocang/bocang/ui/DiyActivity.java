//package cc.bocang.bocang.ui;
//
//import android.annotation.SuppressLint;
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.provider.MediaStore;
//import android.text.InputType;
//import android.text.TextUtils;
//import android.util.Log;
//import android.util.SparseArray;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.alibaba.fastjson.JSON;
//import com.lib.common.hxp.view.PullToRefreshLayout;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.assist.FailReason;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
//import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
//import com.squareup.okhttp.ResponseBody;
//
//import java.io.ByteArrayOutputStream;
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//import java.util.UUID;
//
//import cc.bocang.bocang.R;
//import cc.bocang.bocang.broadcast.Broad;
//import cc.bocang.bocang.data.api.HDApiService;
//import cc.bocang.bocang.data.api.HDRetrofit;
//import cc.bocang.bocang.data.api.OtherApi;
//import cc.bocang.bocang.data.dao.CartDao;
//import cc.bocang.bocang.data.model.Goods;
//import cc.bocang.bocang.data.model.GoodsAllAttr;
//import cc.bocang.bocang.data.model.GoodsAttr;
//import cc.bocang.bocang.data.model.GoodsClass;
//import cc.bocang.bocang.data.model.Result;
//import cc.bocang.bocang.data.model.Scene;
//import cc.bocang.bocang.data.model.SceneAllAttr;
//import cc.bocang.bocang.data.model.SceneAttr;
//import cc.bocang.bocang.data.model.UserInfo;
//import cc.bocang.bocang.data.parser.ParseGetGoodsListResp;
//import cc.bocang.bocang.data.parser.ParseGetSceneListResp;
//import cc.bocang.bocang.data.response.GetGoodsListResp;
//import cc.bocang.bocang.data.response.GetSceneListResp;
//import cc.bocang.bocang.global.Constant;
//import cc.bocang.bocang.global.MyApplication;
//import cc.bocang.bocang.utils.FileUtil;
//import cc.bocang.bocang.utils.ImageUtil;
//import cc.bocang.bocang.utils.LoadingDailog;
//import cc.bocang.bocang.utils.PermissionUtils;
//import cc.bocang.bocang.utils.ShareUtil;
//import cc.bocang.bocang.utils.UIUtils;
//import cc.bocang.bocang.utils.net.HttpListener;
//import cc.bocang.bocang.utils.net.Network;
//import cc.bocang.bocang.view.TouchView;
//import it.sephiroth.android.library.picasso.Picasso;
//import retrofit.Call;
//import retrofit.Callback;
//import retrofit.Response;
//import retrofit.Retrofit;
//
//public class DiyActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, PullToRefreshLayout.OnRefreshListener {
//    private final String TAG = DiyActivity.class.getSimpleName();
//    private DiyActivity ct = this;
//
//    private final int PHOTO_WITH_DATA = 1; // 从SD卡中得到图片
//    private final int PHOTO_WITH_CAMERA = 2;// 拍摄照片
//
//    private String photoName;// 拍照保存的相片名称（不包含后缀名）
//    private File cameraPath;// 拍照保存的相片路径
//
//    private DisplayImageOptions options;
//    private ImageLoader imageLoader;
//
//    private HDApiService apiService;
//    private boolean displayFirstScene;
//    private UserInfo mInfo;
//    private LoadingDailog mLodingDailog;
//    private String screePath;
//    private boolean isShare=false;
//    private boolean isCreate=true;
//    private int goodsclassId;
//    private Network mNetwork;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_diy);
//        mNetwork=new Network();
//        if(isCreate) {
//            apiService = HDRetrofit.create(HDApiService.class);
//            mInfo = ((MyApplication)getApplication()).mUserInfo;
//            Intent intent = getIntent();
//            initView();
//            initImageLoader();
//            mLodingDailog=new LoadingDailog(this, R.style.CustomDialog);
//            String from = intent.getStringExtra("from");
//            if ("scene".equals(from)) {
//                String url=intent.getStringExtra("url");
//                String path = intent.getStringExtra("path");
//                displaySceneBg(url + path);
//                mProIv.performClick();
//            } else if ("goods".equals(from)) {
//                Goods goods = (Goods) intent.getSerializableExtra("goods");
//                displayFirstScene = true;
//                mSceneIv.performClick();
//                displayCheckedGoods(goods);
//            }
//        }
//
//        Log.v("520it","出发到A"+isCreate);
//        //沉浸式状态栏
//        setColor(this,getResources().getColor(R.color.colorPrimary));
//
//    }
//
//    private boolean isFullScreen;
//    private int mSelectedTab;
//    private int page = 1;
//
//    @Override
//    public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
//        page = 1;
//        if (mSelectedTab == 1) {
//            callGoodsClass(false);
//            callGoodsListItem(apiService, goodsclassId,page);
////            callGoodsList(apiService, 0, page, null, null, fitterStr);
//
//        }
//        else if (mSelectedTab == 2)
//            callSceneList(apiService, 0, page, null, null, fitterStr);
//    }
//
//    @Override
//    public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
//        if (mSelectedTab == 1) {
//            callGoodsList(apiService, IndexActivity.mCId, ++page, null, null, fitterStr);
//            callGoodsListItem(apiService, goodsclassId,++page);
////            callGoodsClass(apiService);
//        }
//        else if (mSelectedTab == 2)
//            callSceneList(apiService, 0, ++page, null, null, fitterStr);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, new PermissionUtils.PermissionGrant() {
//            @Override
//            public void onPermissionGranted(int requestCode) {
//                takePhoto();
//            }
//        });
//    }
//
//    @Override
//    public void onClick(View view) {
//        if (view == mFrameLayout) {
//            if (!isFullScreen) {
//                mDiyContainerRl.setVisibility(View.INVISIBLE);
//                isFullScreen = true;
//            } else {
//                mDiyContainerRl.setVisibility(View.VISIBLE);
//                isFullScreen = false;
//            }
//        } else if (view == mProIv) {
//            setTabBg(mProIv);
//            mListViewAdapter.setSelection(0);
//            page = 1;
//            callGoodsClass(true);
//
//            mSelectedTab = 1;
//            mOtherRl.setVisibility(View.GONE);
//            mListView.setVisibility(View.VISIBLE);
//        } else if (view == mSceneIv) {
//            setTabBg(mSceneIv);
//            mListViewAdapter.setSelection(0);
//            page = 1;
//            callSceneList(apiService, 0, 1, null, null, "0.0.0");
//            mSelectedTab = 2;
//            mOtherRl.setVisibility(View.GONE);
//            mListView.setVisibility(View.VISIBLE);
//        } else if (view == mOtherIv) {
//            setTabBg(mOtherIv);
//            mOtherRl.setVisibility(View.VISIBLE);
//            mListView.setVisibility(View.GONE);
//        } else if (view == mCameraIv) {
//            PermissionUtils.requestPermission(DiyActivity.this, PermissionUtils.CODE_CAMERA, new PermissionUtils.PermissionGrant() {
//                @Override
//                public void onPermissionGranted(int requestCode) {
//                    takePhoto();
//                }
//            });
//        } else if (view == mAlbumIv) {
//            pickPhoto();
//        }else {
//            if (view == mShareIv) {//分享
//                if(isShare==true) return;
//                isShare=true;
////                产品ID的累加
//                StringBuffer goodsid = new StringBuffer();
//                for (int i = 0; i < mSelectedLightSA.size(); i++) {
//                    goodsid.append(mSelectedLightSA.get(i).getId() + "");
//                    if (i < mSelectedLightSA.size() - 1) {
//                        goodsid.append(",");
//                    }
//                }
//
//                mDiyContainerRl.setVisibility(View.INVISIBLE);
//                Log.v("520", "前时间："+System.currentTimeMillis());
//                //截图
//                final Bitmap imageData =ImageUtil.takeScreenShot(this);
//                mDiyContainerRl.setVisibility(View.VISIBLE);
//                mLodingDailog.show();
//                Log.v("520", "后时间："+System.currentTimeMillis());
//                final String url =Constant.SUBMITPLAN;//地址
//                final Map<String, String> params = new HashMap<String, String>();
//                params.put("goods_id", goodsid.toString());
//                params.put("phone", "android");
//                params.put("title", "share");
//                params.put("user_id", mInfo.getId() + "");
//                params.put("village", "unknown");
//
//                final String imageName= new SimpleDateFormat("yyyyMMddhhmmss").format(new Date())+".png";
//                new Thread(new Runnable() { //开启线程上传文件
//                    @Override
//                    public void run() {
//                        final String resultJson=uploadFile(imageData, url, params,imageName);
//                        final Result result=JSON.parseObject(resultJson,Result.class);
//                        Log.v("520", "上传时间："+System.currentTimeMillis());
//                        //分享的操作
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mLodingDailog.dismiss();
//                                Log.v("520it","分享成功!");
//                                if(TextUtils.isEmpty(result.getResult())||result.getResult().equals("0")){
//                                    return;
//                                }
//                                showShare(result.getResult(), Constant.SHAPE_SCEEN + result.getPath());
//                            }
//                        });
//
//                    }
//                }).start();
////                showShare("84");
//                isShare=false;
//
//
//            } else if (view == mGocarIv) {//加入购物车
//                CartDao dao = new CartDao(ct);
//                boolean isCarShop=false;
//                for(int i=0;i<mSelectedLightSA.size();i++){
//                    if (-1 != dao.replaceOne(mSelectedLightSA.valueAt(i))) {
//                        isCarShop=true;
//                    }
//                }
//                if(isCarShop==true){
//                    tip("已添加到购物车");
//                    Broad.sendLocalBroadcast(ct, Broad.CART_CHANGE_ACTION, null);//发送广播
//                }
//            }
//        }
//    }
//
//
//    /**
//     * 截屏
//     * @param v			视图
//     */
//    private Bitmap getScreenHot(View v)
//    {
//        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas();
//        canvas.setBitmap(bitmap);
//        v.draw(canvas);
//
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
//
//        try {
//            out.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return BitmapFactory.decodeByteArray(out.toByteArray(), 0, out.toByteArray().length);
//    }
//
////    /**
////     * 提交方案
////     * @param apiService
////     * @param goodsid
////     * @param phone
////     * @param title
////     * @param userID
////     * @param village
////     * @param file
////     */
////    private void getSubmitPlan(HDApiService  apiService,String goodsid,String phone,String title,String userID,String village, Image file) {
////        Call<ResponseBody> call = apiService.submitPlan(file,goodsid,phone,title,userID,village);
////        call.enqueue(new Callback<ResponseBody>() {//开启异步网络请求
////            @Override
////            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
////                if (null == DiyActivity.this || DiyActivity.this.isFinishing())
////                    return;
////
////                try {
////                    String json = response.body().string();
////                    Result result= JSON.parseObject(json,Result.class);
////                    Log.v("520it","result"+result.getResult());
////                    if(result.getResult().equals("0")){
////                        tip("上传方案失败!");
////                    }else{
////                        //分享操作
//////                        showShare(result.getResult());
////
////                        tip("上传成功!");
////
////                    }
////                } catch (Exception e) {
////                    if (null != DiyActivity.this && ! DiyActivity.this.isFinishing())
////                        tip("上传方案失败!");
////                    e.printStackTrace();
////                }
////            }
////
////            @Override
////            public void onFailure(Throwable t) {
////                if (null ==  DiyActivity.this ||  DiyActivity.this.isFinishing())
////                    return;
////                tip("上传方案失败");
////            }
////        });
////    }
// /**
//     * 分享操作
//     */
//    private void showShare(final String id,final String sceenpath) {
//
//        if(TextUtils.isEmpty(id)){
//            return;
//        }
//        final Dialog dialog= UIUtils.showBottomInDialog(this,R.layout.share_dialog,UIUtils.dip2PX(150));
//        TextView tv_cancel= (TextView) dialog.findViewById(R.id.tv_cancel);
//        LinearLayout ll_wx= (LinearLayout) dialog.findViewById(R.id.ll_wx);
//        LinearLayout ll_pyq= (LinearLayout) dialog.findViewById(R.id.ll_pyq);
//        LinearLayout ll_qq= (LinearLayout) dialog.findViewById(R.id.ll_qq);
//        final String mGoodsname="来自"+getString(R.string.app_name)+"配灯的分享";
//        tv_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//        final String url=Constant.SHAREPLAN+"id="+id;
//        ll_wx.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ShareUtil.shareWx(DiyActivity.this, mGoodsname, url);
//                dialog.dismiss();
//            }
//        });
//        ll_pyq.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ShareUtil.sharePyq(DiyActivity.this, mGoodsname, url);
//                dialog.dismiss();
//            }
//        });
//        ll_qq.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ShareUtil.shareQQ(DiyActivity.this, mGoodsname, url,sceenpath);
//                dialog.dismiss();
//            }
//        });
//    }
//
////    /**
////     * 分享操作
////     */
////    private void showShare(final String id,final String sceenpath) {
////
////        if(TextUtils.isEmpty(id)){
////            return;
////        }
////        ShareSDK.initSDK(this);
////        OnekeyShare oks = new OnekeyShare();
////        //关闭sso授权
////        oks.disableSSOWhenAuthorize();
////
////        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
////        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
////        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
////        oks.setTitle("来自"+getString(R.string.app_name)+"配灯的分享");
////        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
////        oks.setTitleUrl(Constant.SHAREPLAN+"id="+id);
////        // text是分享文本，所有平台都需要这个字段
////        oks.setText("来自"+getString(R.string.app_name)+"配灯的分享");
////        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
////        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
////        // url仅在微信（包括好友和朋友圈）中使用
////        oks.setUrl(Constant.SHAREPLAN+"id="+id);
////        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
////        oks.setComment("来自"+getString(R.string.app_name)+"配灯的分享");
////        // site是分享此内容的网站名称，仅在QQ空间使用
////        oks.setSite(getString(R.string.app_name));
////        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
////        oks.setSiteUrl(Constant.SHAREPLAN+"id="+id);
////        //图片地址
//////        mImgUrl= Constant.PRODUCT_URL+mImgUrl+ "!400X400.png";
//////        Log.v("520it","'分享:"+mImgUrl);
//////        Log.v("520it","产品地址:"+Constant.SHAREPLAN+"id="+id));
////        oks.setImageUrl(sceenpath);
////
////        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
////            @Override
////            public void onShare(Platform platform, final cn.sharesdk.framework.Platform.ShareParams paramsToShare) {
////                if ("QZone".equals(platform.getName())) {
////                    paramsToShare.setTitle(null);
////                    paramsToShare.setTitleUrl(null);
////                }
////                if ("SinaWeibo".equals(platform.getName())) {
////                    paramsToShare.setUrl(null);
////                    paramsToShare.setText("分享文本 "+Constant.SHAREPLAN+"id="+id);
////                }
////                if ("Wechat".equals(platform.getName())) {
////                    ImageView img=new ImageView(DiyActivity.this);
////                    Picasso.with(DiyActivity.this).load(sceenpath).into(img);
////                }
////                if ("WechatMoments".equals(platform.getName())) {
////                    ImageView img=new ImageView(DiyActivity.this);
////                    Picasso.with(DiyActivity.this).load(sceenpath).into(img);
////                }
////
////            }
////        });
////       Log.v("520it","");
////        // 启动分享GUI
////        oks.show(this);
////    }
//
//
//
//    private String fitterStr;
//
//
//    /**
//     * 弹出提示
//     */
//    private void showInstallDialog() {
//        final EditText inputServer = new EditText(this);
//        inputServer.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("请输入密码查看此分类!").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
//                .setNegativeButton("取消", null);
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//            public void onClick(DialogInterface dialog, int which) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        String urlPath=Constant.SETUPPROUCT+inputServer.getText().toString();
//                        String json = OtherApi.doGet(urlPath);
//                        final Result result= JSON.parseObject(json, Result.class);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (result.getCode().equals("1")) {
//                                    callGoodsListItem(apiService, goodsclassId,1);
//                                } else {
//                                    Toast.makeText(DiyActivity.this, "密码错误，请重新输入!", Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        });
//                    }
//                }).start();
//
//            }
//        });
//        builder.show();
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//       Log.v("520it","ffff");
//        if (parent == mListView) {
//            mListViewAdapter.setSelection(position);
//            mListViewAdapter.notifyDataSetChanged();
//            if (mSelectedTab == 1) {
//                if (null != goodsAllAttrs)
////                    fitterStr = goodsAllAttrs.get(0).getGoodsAttrs().get(position).getGoods_id() + ".0.0";
////                    fitterStr = 33 + ".0.0";
//                Log.v("520it",fitterStr);
//                page = 1;
//                goodsclassId = goodsClassList.get(position).getId();
//                Log.v("520it","goodsclassId:"+goodsclassId);
////
//                Log.v("520it","xx:"+IndexActivity.mCId);
//                if(TextUtils.isEmpty(IndexActivity.mCId)){
//                    callGoodsListItem(apiService, goodsclassId, 1);
//                    return;
//                }
//
//                String[] cIdArrys = IndexActivity.mCId.split(",");
//                Log.v("520it","cIdArrys:"+cIdArrys.toString());
//                if (cIdArrys.length > 0) {
//                    if (Arrays.binarySearch(cIdArrys, goodsclassId + "") >= 0) {
//                        showInstallDialog();
//                    }
//                } else {
////                    callGoodsListItem(apiService, goodsclassId, 1);
//                }
////                callGoodsListItem(apiService, goodsclassId, 1);
//            } else if (mSelectedTab == 2) {
//                if (null != sceneAllAttrs)
//                    fitterStr = sceneAllAttrs.get(0).getSceneAttrs().get(position).getScene_id() + ".0";
//                page = 1;
//                callSceneList(apiService, 0, 1, null, null, fitterStr);
//            }
//        } else if (parent == mGridView) {
//            if (mSelectedTab == 1) {
//                Log.v("520it","出发到B");
//                displayCheckedGoods(goodses.get(position));
//            } else if (mSelectedTab == 2) {
//                int ids= Integer.parseInt(scenes.get(position).getId());
//                if(ids>1551){
//                    displaySceneBg(Constant.SCENE_URL_2 + scenes.get(position).getPath());
//                }else {
//                    displaySceneBg(Constant.SCENE_URL + scenes.get(position).getPath());
//                }
//
//            }
//        }
//    }
//
//    private List<GoodsAllAttr> goodsAllAttrs;
//
//    private List<Goods> goodses;
//    private List<String> goodsTypeList = new ArrayList<>();
//    private List<GoodsClass> goodsClassList = new ArrayList<>();
//
//    private void callGoodsList(HDApiService apiService, String c_id, final int page, String keywords, String type, String filter_attr){
//        pd.setVisibility(View.VISIBLE);
//        mNetwork.sendGoodsList(c_id, page,1+"", keywords, type, filter_attr, this, new HttpListener() {
//            @Override
//            public void onSuccessListener(int what, String response) {
//                if (null == ct || ct.isFinishing())
//                    return;
//                pd.setVisibility(View.GONE);
//                mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
//                mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
//                try {
//                    String json = response;
//                    Log.i(TAG, json);
//                    GetGoodsListResp resp = ParseGetGoodsListResp.parse(json);
//                    if (null != resp && resp.isSuccess()) {
//                        if (null == goodsAllAttrs)
//                            goodsAllAttrs = resp.getGoodsAllAttrs();
//                        if (goodsTypeList.isEmpty())
//                            for (GoodsAttr goodsAttr : goodsAllAttrs.get(0).getGoodsAttrs()) {
//                                goodsTypeList.add(goodsAttr.getAttr_value());
//                            }
//                        mListViewAdapter.setData(goodsTypeList);
//                        mListViewAdapter.notifyDataSetChanged();
//
//                        List<Goods> goodsList = resp.getGoodses();
//                        if (1 == page)
//                            goodses = goodsList;
//                        else if (null != goodses) {
//                            goodses.addAll(goodsList);
//                            if (goodsList.isEmpty())
//                                Toast.makeText(ct, "没有更多内容了", Toast.LENGTH_LONG).show();
//                        }
//
//                        List<String> names = new ArrayList<>();
//                        List<String> paths = new ArrayList<>();
//                        for (Goods goods : goodses) {
//                            names.add(goods.getName());
//                            paths.add(goods.getImg_url());
//                        }
//                        mGridViewAdapter.setNames(names);
//                        mGridViewAdapter.setPaths(paths);
//                        mGridViewAdapter.setShow(1);
//                        mGridViewAdapter.notifyDataSetChanged();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailureListener(int what, String ans) {
//                if (null == ct || ct.isFinishing())
//                    return;
//                pd.setVisibility(View.GONE);
//                mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
//                mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
//            }
//        });
//    }
//
//
//    private void callGoodsList02(HDApiService apiService, String c_id, final int page, String keywords, String type, String filter_attr) {
//        pd.setVisibility(View.VISIBLE);
//        Call<ResponseBody> call = apiService.getGoodsList(c_id, page,1, keywords, type, filter_attr);
//        call.enqueue(new Callback<ResponseBody>() {//开启异步网络请求
//            @Override
//            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
//                if (null == ct || ct.isFinishing())
//                    return;
//                pd.setVisibility(View.GONE);
//                mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
//                mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
//                try {
//                    String json = response.body().string();
//                    Log.i(TAG, json);
//                    GetGoodsListResp resp = ParseGetGoodsListResp.parse(json);
//                    if (null != resp && resp.isSuccess()) {
//                        if (null == goodsAllAttrs)
//                            goodsAllAttrs = resp.getGoodsAllAttrs();
//                        if (goodsTypeList.isEmpty())
//                            for (GoodsAttr goodsAttr : goodsAllAttrs.get(0).getGoodsAttrs()) {
//                                goodsTypeList.add(goodsAttr.getAttr_value());
//                            }
//                        mListViewAdapter.setData(goodsTypeList);
//                        mListViewAdapter.notifyDataSetChanged();
//
//                        List<Goods> goodsList = resp.getGoodses();
//                        if (1 == page)
//                            goodses = goodsList;
//                        else if (null != goodses) {
//                            goodses.addAll(goodsList);
//                            if (goodsList.isEmpty())
//                                Toast.makeText(ct, "没有更多内容了", Toast.LENGTH_LONG).show();
//                        }
//
//                        List<String> names = new ArrayList<>();
//                        List<String> paths = new ArrayList<>();
//                        for (Goods goods : goodses) {
//                            names.add(goods.getName());
//                            paths.add(goods.getImg_url());
//                        }
//                        mGridViewAdapter.setNames(names);
//                        mGridViewAdapter.setPaths(paths);
//                        mGridViewAdapter.setShow(1);
//                        mGridViewAdapter.notifyDataSetChanged();
//                    }
//                } catch (Exception e) {
////                    Toast.makeText(ct, "数据异常...", Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                if (null == ct || ct.isFinishing())
//                    return;
//                pd.setVisibility(View.GONE);
//                mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
//                mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
////                Toast.makeText(ct, "无法连接服务器...", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//
//    private void callGoodsListItem(HDApiService apiService, int c_id, final int page) {
//        pd.setVisibility(View.VISIBLE);
//        Call<ResponseBody> call = apiService.getGoodsListItem(c_id, page);
//        call.enqueue(new Callback<ResponseBody>() {//开启异步网络请求
//            @Override
//            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
//                if (null == ct || ct.isFinishing())
//                    return;
//                pd.setVisibility(View.GONE);
//                mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
//                mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
//                try {
//                    String json = response.body().string();
//                    Log.i(TAG, json);
//                    GetGoodsListResp resp = ParseGetGoodsListResp.parse(json);
//                    if (null != resp && resp.isSuccess()) {
////                        if (null == goodsAllAttrs)
////                            goodsAllAttrs = resp.getGoodsAllAttrs();
////                        if (goodsTypeList.isEmpty())
////                            for (GoodsAttr goodsAttr : goodsAllAttrs.get(0).getGoodsAttrs()) {
////                                goodsTypeList.add(goodsAttr.getAttr_value());
////                            }
////                        mListViewAdapter.setData(goodsTypeList);
////                        mListViewAdapter.notifyDataSetChanged();
//
//                        List<Goods> goodsList = resp.getGoodses();
//                        if (1 == page)
//                            goodses = goodsList;
//                        else if (null != goodses) {
//                            goodses.addAll(goodsList);
//                            if (goodsList.isEmpty())
//                                Toast.makeText(ct, "没有更多内容了", Toast.LENGTH_LONG).show();
//                        }
//
//                        List<String> names = new ArrayList<>();
//                        List<String> paths = new ArrayList<>();
//                        for (Goods goods : goodses) {
//                            names.add(goods.getName());
//                            paths.add(goods.getImg_url());
//                        }
//                        mGridViewAdapter.setNames(names);
//                        mGridViewAdapter.setPaths(paths);
//                        mGridViewAdapter.setShow(1);
//                        mGridViewAdapter.notifyDataSetChanged();
//                    }
//                } catch (Exception e) {
////                    Toast.makeText(ct, "数据异常...", Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                if (null == ct || ct.isFinishing())
//                    return;
//                pd.setVisibility(View.GONE);
//                mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
//                mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
////                Toast.makeText(ct, "无法连接服务器...", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//
//    /**
//     * 获取产品列表
//     * @param isClick 判断是否是点击按钮触发的事件
//     */
//    private void callGoodsClass(final boolean isClick){
//        Log.v("520it","刷新");
//        pd.setVisibility(View.VISIBLE);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final String json = OtherApi.getAppGoodsClass();
//                Log.i(TAG, json);
//
//               runOnUiThread(new Runnable() {
//                   @Override
//                   public void run() {
//                       goodsClassList=JSON.parseArray(
//                               json, GoodsClass.class);
//                       if (goodsTypeList.isEmpty())
//                           for (GoodsClass goodsClass : goodsClassList) {
//                               goodsTypeList.add(goodsClass.getName());
//                           }
//                       mListViewAdapter.setData(goodsTypeList);
//                       mListViewAdapter.notifyDataSetChanged();
//                       if(isClick){
//                           callGoodsListItem(apiService, goodsClassList.get(0).getId(),1);
//                       }
//                   }
//               });
//            }
//        }).start();
//
//    }
//
//
//    private List<SceneAllAttr> sceneAllAttrs;
//    private List<Scene> scenes;
//    private List<String> sceneSpaceList = new ArrayList<>();
//
//    private void callSceneList(HDApiService apiService, int c_id, final int page, String keywords, String type, String filter_attr) {
//        pd.setVisibility(View.VISIBLE);
//        mNetwork.sendSceneList(c_id + "", page, keywords, type, filter_attr, this, new HttpListener() {
//            @Override
//            public void onSuccessListener(int what, String response) {
//                if (null == ct || ct.isFinishing())
//                    return;
//                pd.setVisibility(View.GONE);
//                mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
//                mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
//                try {
//                    String json = response;
//                    Log.i(TAG, json);
//                    GetSceneListResp resp = ParseGetSceneListResp.parse(json);
//                    if (null != resp && resp.isSuccess()) {
//                        if (null == sceneAllAttrs)
//                            sceneAllAttrs = resp.getSceneAllAttrs();
//                        if (sceneSpaceList.isEmpty())
//                            for (SceneAttr sceneAttr : sceneAllAttrs.get(0).getSceneAttrs()) {
//                                sceneSpaceList.add(sceneAttr.getAttr_value());
//                            }
//                        mListViewAdapter.setData(sceneSpaceList);
//                        mListViewAdapter.notifyDataSetChanged();
//
//                        List<Scene> sceneList = resp.getScenes();
//                        if (1 == page)
//                            scenes = sceneList;
//                        else if (null != scenes) {
//                            scenes.addAll(sceneList);
//                            if (sceneList.isEmpty())
//                                Toast.makeText(ct, "没有更多内容了", Toast.LENGTH_LONG).show();
//                        }
//
//                        if (displayFirstScene && null != scenes && !scenes.isEmpty()) {//点击产品进来第一次展示背景
//                            int position=new Random().nextInt(scenes.size());
//                            int ids= Integer.parseInt(scenes.get(position).getId());
//                            if(ids>1551){
//                                displaySceneBg(Constant.SCENE_URL_2 + scenes.get(position).getPath());
//                            }else {
//                                displaySceneBg(Constant.SCENE_URL + scenes.get(position).getPath());
//                            }
//                            displayFirstScene = false;
//                        }
//
//                        List<String> names = new ArrayList<>();
//                        List<String> paths = new ArrayList<>();
//                        for (Scene scene : scenes) {
//                            names.add(scene.getName());
//                            paths.add(scene.getPath());
//                        }
//                        mGridViewAdapter.setNames(names);
//                        mGridViewAdapter.setPaths(paths);
//                        mGridViewAdapter.setScene(scenes);
//                        mGridViewAdapter.setShow(2);
//                        mGridViewAdapter.notifyDataSetChanged();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailureListener(int what, String ans) {
//                if (null == ct || ct.isFinishing())
//                    return;
//                pd.setVisibility(View.GONE);
//                ct.page--;
//                mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
//                mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
//            }
//        });
//    }
//
//
//    private void callSceneList02(HDApiService apiService, int c_id, final int page, String keywords, String type, String filter_attr) {
//        pd.setVisibility(View.VISIBLE);
//        Call<ResponseBody> call = apiService.getSceneList(c_id, page, keywords, type, filter_attr);
//        call.enqueue(new Callback<ResponseBody>() {//开启异步网络请求
//            @Override
//            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
//                if (null == ct || ct.isFinishing())
//                    return;
//                pd.setVisibility(View.GONE);
//                mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
//                mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
//                try {
//                    String json = response.body().string();
//                    Log.i(TAG, json);
//                    GetSceneListResp resp = ParseGetSceneListResp.parse(json);
//                    if (null != resp && resp.isSuccess()) {
//                        if (null == sceneAllAttrs)
//                            sceneAllAttrs = resp.getSceneAllAttrs();
//                        if (sceneSpaceList.isEmpty())
//                            for (SceneAttr sceneAttr : sceneAllAttrs.get(0).getSceneAttrs()) {
//                                sceneSpaceList.add(sceneAttr.getAttr_value());
//                            }
//                        mListViewAdapter.setData(sceneSpaceList);
//                        mListViewAdapter.notifyDataSetChanged();
//
//                        List<Scene> sceneList = resp.getScenes();
//                        if (1 == page)
//                            scenes = sceneList;
//                        else if (null != scenes) {
//                            scenes.addAll(sceneList);
//                            if (sceneList.isEmpty())
//                                Toast.makeText(ct, "没有更多内容了", Toast.LENGTH_LONG).show();
//                        }
//
//                        if (displayFirstScene && null != scenes && !scenes.isEmpty()) {//点击产品进来第一次展示背景
//                            int position=new Random().nextInt(scenes.size());
//                            int ids= Integer.parseInt(scenes.get(position).getId());
//                            if(ids>1551){
//                                displaySceneBg(Constant.SCENE_URL_2 + scenes.get(position).getPath());
//                            }else {
//                                displaySceneBg(Constant.SCENE_URL + scenes.get(position).getPath());
//                            }
//                            displayFirstScene = false;
//                        }
//
//                        List<String> names = new ArrayList<>();
//                        List<String> paths = new ArrayList<>();
//                        for (Scene scene : scenes) {
//                            names.add(scene.getName());
//                            paths.add(scene.getPath());
//                        }
//                        mGridViewAdapter.setNames(names);
//                        mGridViewAdapter.setPaths(paths);
//                        mGridViewAdapter.setScene(scenes);
//                        mGridViewAdapter.setShow(2);
//                        mGridViewAdapter.notifyDataSetChanged();
//                    }
//                } catch (Exception e) {
////                    Toast.makeText(ct, "数据异常...", Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                if (null == ct || ct.isFinishing())
//                    return;
//                pd.setVisibility(View.GONE);
//                ct.page--;
//                mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
//                mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
////                Toast.makeText(ct, "无法连接服务器...", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void setTabBg(ImageView imageView) {
//        mProIv.setBackgroundResource(android.R.color.transparent);
//        mSceneIv.setBackgroundResource(android.R.color.transparent);
//        mOtherIv.setBackgroundResource(android.R.color.transparent);
//        imageView.setBackgroundResource(R.color.colorPrimary);
//    }
//
//    private int mLightNumber = -1;// 点出来的灯的编号
//    public SparseArray<Goods> mSelectedLightSA = new SparseArray<>();// key为自增编号，value为点出来的灯
//    private int leftMargin = 0;
//
//    private void displayCheckedGoods(final Goods goods) {
//        if (mSelectedLightSA.size() >= 3) {
//            Toast.makeText(DiyActivity.this, "调出灯超数，长按可删除", Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        imageLoader.loadImage(Constant.PRODUCT_URL + goods.getImg_url() + "!400X400.png", options,
//                new SimpleImageLoadingListener() {
//
//                    @Override
//                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                        super.onLoadingComplete(imageUri, view, loadedImage);
//
//                        // 被点击的灯的编号加1
//                        mLightNumber++;
//                        // 把点击的灯放到集合里
//                        mSelectedLightSA.put(mLightNumber, goods);
//
//                        // 设置灯图的ImageView的初始宽高和位置
//                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
//                                mScreenWidth / 3 * 2 / 3,
//                                (mScreenWidth / 3 * 2 / 3 * loadedImage.getHeight()) / loadedImage.getWidth());
//                        // 设置灯点击出来的位置
//                        if (mSelectedLightSA.size() == 1) {
//                            leftMargin = mScreenWidth / 3 * 2 / 3;
//                        } else if (mSelectedLightSA.size() == 2) {
//                            leftMargin = mScreenWidth / 3 * 2 / 3 * 2;
//                        } else if (mSelectedLightSA.size() == 3) {
//                            leftMargin = 0;
//                        }
//                        lp.setMargins(leftMargin, 0, 0, 0);
//
//                        TouchView touchView = new TouchView(ct);
//                        touchView.setLayoutParams(lp);
//                        touchView.setImageBitmap(loadedImage);// 设置被点击的灯的图片
//                        touchView.setmLightCount(mLightNumber);// 设置被点击的灯的编号
//                        FrameLayout.LayoutParams newLp = new FrameLayout.LayoutParams(
//                                FrameLayout.LayoutParams.MATCH_PARENT,
//                                FrameLayout.LayoutParams.MATCH_PARENT);
//                        FrameLayout newFrameLayout = new FrameLayout(ct);
//                        newFrameLayout.setLayoutParams(newLp);
//                        newFrameLayout.addView(touchView);
//                        mFrameLayout.addView(newFrameLayout);
//                        touchView.setContainer(mFrameLayout, newFrameLayout);
//
//                    }
//
//                });
//    }
//
//    /**
//     * 加载场景背景图
//     *
//     * @param path 场景img_url
//     */
//    private void displaySceneBg(String path) {
//        screePath=path;
//        imageLoader.displayImage(path, mSceneBgIv, options,
//                new ImageLoadingListener() {
//
//                    @Override
//                    public void onLoadingStarted(String imageUri, View view) {
//                        pd2.setVisibility(View.VISIBLE);
//                    }
//
//                    @Override
//                    public void onLoadingFailed(String imageUri, View view,
//                                                FailReason failReason) {
//                        pd2.setVisibility(View.GONE);
//                        Toast.makeText(DiyActivity.this, failReason.getCause() + "请重试！", Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onLoadingComplete(String imageUri, View view,
//                                                  Bitmap loadedImage) {
//                        pd2.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onLoadingCancelled(String imageUri, View view) {
//                        pd2.setVisibility(View.GONE);
//                    }
//                });
//    }
//
//    private void initImageLoader() {
//        options = new DisplayImageOptions.Builder()
//                // 设置图片下载期间显示的图片
//                .showImageOnLoading(R.mipmap.bg_default)
//                // 设置图片Uri为空或是错误的时候显示的图片
//                .showImageForEmptyUri(R.mipmap.bg_default)
//                // 设置图片加载或解码过程中发生错误显示的图片
//                .showImageOnFail(R.mipmap.bg_default)
//                // 设置下载的图片是否缓存在内存中
//                .cacheInMemory(false)
//                //设置图片的质量
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                // 设置下载的图片是否缓存在SD卡中
//                .cacheOnDisk(true)
//                // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
//                // 是否考虑JPEG图像EXIF参数（旋转，翻转）
////                .considerExifParams(true)
//                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 设置图片可以放大（要填满ImageView必须配置memoryCacheExtraOptions大于Imageview）
//                // 图片加载好后渐入的动画时间
//                // .displayer(new FadeInBitmapDisplayer(100))
//                .build(); // 构建完成
//
//        // 得到ImageLoader的实例(使用的单例模式)
//        imageLoader = ImageLoader.getInstance();
//    }
//
//    /**
//     * 拍照获取相片
//     **/
//    private void takePhoto() {
//        // 图片名称 时间命名
//        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
//        Date date = new Date(System.currentTimeMillis());
//        photoName = format.format(date);
//        cameraPath = FileUtil.getOwnFilesDir(this, Constant.CAMERA_PATH);
//
//        Uri imageUri = Uri.fromFile(new File(cameraPath, photoName + ".jpg"));
//        System.out.println("imageUri" + imageUri.toString());
//
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 调用系统相机
//        // 指定照片保存路径（SD卡）
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//
//        startActivityForResult(intent, PHOTO_WITH_CAMERA); // 用户点击了从相机获取
//    }
//
//    /**
//     * 从相册获取图片
//     **/
//    private void pickPhoto() {
//        Intent intent = new Intent();
//        intent.setType("image/*"); // 开启Pictures画面Type设定为image
//        intent.setAction(Intent.ACTION_GET_CONTENT); // 使用Intent.ACTION_PICK这个Action则是直接打开系统图库
//
//        startActivityForResult(intent, PHOTO_WITH_DATA); // 取得相片后返回到本画面
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (resultCode == RESULT_OK) { // 返回成功
//            switch (requestCode) {
//                case PHOTO_WITH_CAMERA: {// 拍照获取图片
//                    String status = Environment.getExternalStorageState();
//                    if (status.equals(Environment.MEDIA_MOUNTED)) { // 是否有SD卡
//
//                        File imageFile = new File(cameraPath, photoName + ".jpg");
//
//                        if (imageFile.exists()) {
//                            String imagePath = "file://" + imageFile.toString();
//
//                            displaySceneBg(imagePath);
//                        } else {
//                            Toast.makeText(this, "读取图片失败！", Toast.LENGTH_LONG)
//                                    .show();
//                        }
//                    } else {
//                        Toast.makeText(this, "没有SD卡", Toast.LENGTH_LONG).show();
//                    }
//                    break;
//                }
//                case PHOTO_WITH_DATA: // 从图库中选择图片
//                    // 照片的原始资源地址
//                    Uri originalUri = data.getData();
//                    displaySceneBg(originalUri.toString());
//                    break;
//
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    public void back(View v) {
//        finish();
//    }
//
//    public void close(View v) {
//        mDiyContainerRl.setVisibility(View.INVISIBLE);
//        isFullScreen = true;
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mHideHandler.removeCallbacks(mHidePart2Runnable);
//        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
//    }
//
//    private static final int UI_ANIMATION_DELAY = 300;
//    private final Handler mHideHandler = new Handler();
//    private final Runnable mHidePart2Runnable = new Runnable() {
//        @SuppressLint("InlinedApi")
//        @Override
//        public void run() {
//            mFrameLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//                    | View.SYSTEM_UI_FLAG_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//        }
//    };
//
//    private int mScreenWidth;
//    private ProgressBar pd, pd2;
//    private ImageView mSceneBgIv,mGocarIv;
//    private FrameLayout mFrameLayout, mDiyGridViewContainer;
//    private RelativeLayout mDiyContainerRl, mDiyMenuContainerRl, mOtherRl;
//    private LinearLayout mDiyTabLl;
//    public ImageView mProIv, mSceneIv, mOtherIv, mCameraIv, mAlbumIv, mShareIv;
//    private ListView mListView;
//    private DiyListViewAdapter mListViewAdapter;
//    private GridView mGridView;
//    private DiyGridViewAdapter mGridViewAdapter;
//    private PullToRefreshLayout mPullToRefreshLayout;
//
//    private void initView() {
//        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
//
//        mFrameLayout = (FrameLayout) findViewById(R.id.sceneFrameLayout);
////        FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams) mFrameLayout.getLayoutParams();
////        flp.width = mScreenWidth;
////        flp.height = (int) (mScreenWidth / 4.0f * 3.0f);
////        mFrameLayout.setLayoutParams(flp);
//        mFrameLayout.setOnClickListener(ct);
//
//        pd = (ProgressBar) findViewById(R.id.pd);
//        mSceneBgIv = (ImageView) findViewById(R.id.sceneBgIv);
//        mGocarIv = (ImageView)findViewById(R.id.gocarIv);
//        mDiyContainerRl = (RelativeLayout) findViewById(R.id.diyContainerRl);
//
//        // 三分之一屏幕的RelativeLayout
//        mDiyMenuContainerRl = (RelativeLayout) findViewById(R.id.diyMenuContainerRl);
//        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mDiyMenuContainerRl.getLayoutParams();
//        lp.width = (int) (mScreenWidth / 3.0f);
//        mDiyMenuContainerRl.setLayoutParams(lp);
//
//        mDiyTabLl = (LinearLayout) findViewById(R.id.diyTabLl);
//        lp = (RelativeLayout.LayoutParams) mDiyTabLl.getLayoutParams();
//        lp.height = (int) (80.0f / 1200.0f * mScreenWidth);
//        mDiyTabLl.setLayoutParams(lp);
//
//        mProIv = (ImageView) findViewById(R.id.diyProIv);
//        mSceneIv = (ImageView) findViewById(R.id.diySceneIv);
//        mOtherIv = (ImageView) findViewById(R.id.diyOtherIv);
//        mCameraIv = (ImageView) findViewById(R.id.cameraIv);
//        mAlbumIv = (ImageView) findViewById(R.id.albumIv);
//        mShareIv = (ImageView) findViewById(R.id.diyShareIv);
//        mProIv.setOnClickListener(this);
//        mSceneIv.setOnClickListener(this);
//        mOtherIv.setOnClickListener(this);
//        mCameraIv.setOnClickListener(this);
//        mAlbumIv.setOnClickListener(this);
//        mShareIv.setOnClickListener(this);
//        mGocarIv.setOnClickListener(this);
//        apiService = HDRetrofit.create(HDApiService.class);
//
//        // ListView
//        mListView = (ListView) findViewById(R.id.listView);
//        mListViewAdapter = new DiyListViewAdapter(ct);
//        mListView.setAdapter(mListViewAdapter);
//        mListView.setOnItemClickListener(this);
//        mOtherRl = (RelativeLayout) findViewById(R.id.otherRl);
//
//        // 包裹GridView的FrameLayout
//        mDiyGridViewContainer = (FrameLayout) findViewById(R.id.diyGridViewContainer);
//        lp = (RelativeLayout.LayoutParams) mDiyGridViewContainer.getLayoutParams();
//        lp.width = (int) (mScreenWidth / 3f / 4f * 3f);
//        mDiyGridViewContainer.setPadding((int) (8.0f / 1200.0f * mScreenWidth),
//                (int) (8.0f / 1200.0f * mScreenWidth),
//                (int) (8.0f / 1200.0f * mScreenWidth),
//                (int) (8.0f / 1200.0f * mScreenWidth));
//        mDiyGridViewContainer.setLayoutParams(lp);
//
//        // GridView
//        mGridView = (GridView) findViewById(R.id.diyGridView);
//        mGridView.setVerticalSpacing((int) (8.0f / 1200.0f * mScreenWidth));
//        mGridViewAdapter = new DiyGridViewAdapter(ct);
//        mGridView.setAdapter(mGridViewAdapter);
//        mGridView.setOnItemClickListener(this);
//
//        pd2 = (ProgressBar) findViewById(R.id.pd2);
//
//        mPullToRefreshLayout = ((PullToRefreshLayout) findViewById(R.id.refresh_view));
//        mPullToRefreshLayout.setOnRefreshListener(this);
//
//    }
//
////    上传图片
//
//    private  int TIME_OUT = 10*1000;   //超时时间
//    private  String CHARSET = "utf-8"; //设置编码
//    /**
//     * android上传文件到服务器
//     * @param file  需要上传的文件
//     * @param RequestURL  请求的rul
//     * @return  返回响应的内容
//     */
//    private String uploadFile(Bitmap file, String RequestURL, Map<String, String> param,String imageName){
//        String result = null;
//        String  BOUNDARY =  UUID.randomUUID().toString();  //边界标识   随机生成
//        String PREFIX = "--" , LINE_END = "\r\n";
//        String CONTENT_TYPE = "multipart/form-data";   //内容类型
//        // 显示进度框
//        //      showProgressDialog();
//        try {
//            URL url = new URL(RequestURL);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setReadTimeout(TIME_OUT);
//            conn.setConnectTimeout(TIME_OUT);
//            conn.setDoInput(true);  //允许输入流
//            conn.setDoOutput(true); //允许输出流
//            conn.setUseCaches(false);  //不允许使用缓存
//            conn.setRequestMethod("POST");  //请求方式
//            conn.setRequestProperty("Charset", CHARSET);  //设置编码
//            conn.setRequestProperty("connection", "keep-alive");
//            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
//            if(file!=null){
//                /**
//                 * 当文件不为空，把文件包装并且上传
//                 */
//                DataOutputStream dos = new DataOutputStream( conn.getOutputStream());
//                StringBuffer sb = new StringBuffer();
//
//                String params = "";
//                if (param != null && param.size() > 0) {
//                    Iterator<String> it = param.keySet().iterator();
//                    while (it.hasNext()) {
//                        sb = null;
//                        sb = new StringBuffer();
//                        String key = it.next();
//                        String value = param.get(key);
//                        sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
//                        sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END).append(LINE_END);
//                        sb.append(value).append(LINE_END);
//                        params = sb.toString();
//                        dos.write(params.getBytes());
//                    }
//                }
//                sb = new StringBuffer();
//                sb.append(PREFIX);
//                sb.append(BOUNDARY);
//                sb.append(LINE_END);
//                /**
//                 * 这里重点注意：
//                 * name里面的值为服务器端需要key   只有这个key 才可以得到对应的文件
//                 * filename是文件的名字，包含后缀名的   比如:abc.png
//                 */
//                sb.append("Content-Disposition: form-data; name=\"").append("file").append("\"")
//                        .append(";filename=\"").append(imageName).append("\"\n");
//                sb.append("Content-Type: image/png");
//                sb.append(LINE_END).append(LINE_END);
//                dos.write(sb.toString().getBytes());
//                InputStream is = ImageUtil.Bitmap2InputStream(file);
//                byte[] bytes = new byte[1024];
//                int len = 0;
//                while((len=is.read(bytes))!=-1){
//                    dos.write(bytes, 0, len);
//                }
//
//
//                is.close();
////                dos.write(file);
//                dos.write(LINE_END.getBytes());
//                byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
//                dos.write(end_data);
//
//                dos.flush();
//                /**
//                 * 获取响应码  200=成功
//                 * 当响应成功，获取响应的流
//                 */
//
//                int res = conn.getResponseCode();
//                System.out.println("res========="+res);
//                if(res==200){
//                    InputStream input =  conn.getInputStream();
//                    StringBuffer sb1= new StringBuffer();
//                    int ss ;
//                    while((ss=input.read())!=-1){
//                        sb1.append((char)ss);
//                    }
//                    result = sb1.toString();
//                }
//                else{
//                }
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
////
//
//
//
//}

package cc.bocang.bocang.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cc.bocang.bocang.R;
import cc.bocang.bocang.broadcast.Broad;
import cc.bocang.bocang.data.api.HDApiService;
import cc.bocang.bocang.data.api.HDRetrofit;
import cc.bocang.bocang.data.api.OtherApi;
import cc.bocang.bocang.data.dao.CartDao;
import cc.bocang.bocang.data.model.Goods;
import cc.bocang.bocang.data.model.GoodsAllAttr;
import cc.bocang.bocang.data.model.GoodsAttr;
import cc.bocang.bocang.data.model.GoodsClass;
import cc.bocang.bocang.data.model.Result;
import cc.bocang.bocang.data.model.Scene;
import cc.bocang.bocang.data.model.SceneAllAttr;
import cc.bocang.bocang.data.model.SceneAttr;
import cc.bocang.bocang.data.model.UserInfo;
import cc.bocang.bocang.data.parser.ParseGetGoodsListResp;
import cc.bocang.bocang.data.parser.ParseGetSceneListResp;
import cc.bocang.bocang.data.response.GetGoodsListResp;
import cc.bocang.bocang.data.response.GetSceneListResp;
import cc.bocang.bocang.global.Constant;
import cc.bocang.bocang.global.MyApplication;
import cc.bocang.bocang.utils.FileUtil;
import cc.bocang.bocang.utils.ImageUtil;
import cc.bocang.bocang.utils.LoadingDailog;
import cc.bocang.bocang.utils.LogUtils;
import cc.bocang.bocang.utils.PermissionUtils;
import cc.bocang.bocang.utils.ScannerUtils;
import cc.bocang.bocang.utils.UIUtils;
import cc.bocang.bocang.utils.net.HttpListener;
import cc.bocang.bocang.utils.net.Network;
import cc.bocang.bocang.view.TouchView;
import com.alibaba.fastjson.JSON;
import com.lib.common.hxp.view.PullToRefreshLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.okhttp.ResponseBody;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class DiyActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, PullToRefreshLayout.OnRefreshListener {
    private static final int UI_ANIMATION_DELAY = 300;

    private String CHARSET = "utf-8";

    private final int PHOTO_WITH_CAMERA = 2;

    private final int PHOTO_WITH_DATA = 1;

    private final String TAG = DiyActivity.class.getSimpleName();

    private int TIME_OUT = 10000;

    private View add_data_ll;

    private TextView add_data_tv;

    private HDApiService apiService;

    private File cameraPath;

    private DiyActivity ct = this;

    private boolean displayFirstScene;

    private String fitterStr;

    private List<GoodsAllAttr> goodsAllAttrs;

    private List<GoodsClass> goodsClassList = new ArrayList();

    private List<String> goodsTypeList = new ArrayList();

    private int goodsclassId;

    private List<Goods> goodses;

    private ImageLoader imageLoader;

    private String imgUri;

    private boolean isFullScreen;

    private boolean isShare = false;

    private int leftMargin = 0;

    public ImageView mAlbumIv;

    public ImageView mCameraIv;

    private RelativeLayout mDiyContainerRl;

    private FrameLayout mDiyGridViewContainer;

    private RelativeLayout mDiyMenuContainerRl;

    private LinearLayout mDiyTabLl;

    private FrameLayout mFrameLayout;

    private ImageView mGocarIv;

    private GridView mGridView;

    private MyDiyGridViewAdapter mGridViewAdapter;

    private final Handler mHideHandler = new Handler();

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint({"InlinedApi"})
        public void run() { DiyActivity.this.mFrameLayout.setSystemUiVisibility(4871); }
    };

    private UserInfo mInfo;

    private Intent mIntent;

    private int mLightNumber = -1;

    private ListView mListView;

    private DiyListViewAdapter mListViewAdapter;

    private LoadingDailog mLodingDailog;

    private Network mNetwork;

    public ImageView mOtherIv;

    private RelativeLayout mOtherRl;

    View mProIv;

    private ImageView mSceneBgIv;

    View mSceneIv;

    private int mScreenWidth;

    public SparseArray<Goods> mSelectedLightSA = new SparseArray();

    private int mSelectedTab = 1;

    public ImageView mShareIv;

    private DisplayImageOptions options;

    private int page = 1;

    private ProgressBar pd;

    private ProgressBar pd2;

    private String photoName;

    private List<SceneAllAttr> sceneAllAttrs;

    private List<String> sceneSpaceList = new ArrayList();

    private List<Scene> scenes;

    private String screePath;

    private View view_gallery;

    private View view_save;

    private View view_sc;

    private View view_share;

    private View view_take_photo;
    private boolean isClick;
    private String str1;

    private void callGoodsClass(boolean paramBoolean) {
//        Log.v("520it", "������");
        this.pd.setVisibility(View.VISIBLE);
        (new Thread(new Runnable() {
            public void run() {
                final String json = OtherApi.getAppGoodsClass();
//                Log.i(DiyActivity.this.TAG, str);
                DiyActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
//                        DiyActivity.access$1502(DiyActivity.this, JSON.parseArray(json, GoodsClass.class));
                        goodsClassList = JSON.parseArray(json, GoodsClass.class);
                        if (goodsTypeList.isEmpty())
                        for (GoodsClass goodsClass : DiyActivity.this.goodsClassList)
                        DiyActivity.this.goodsTypeList.add(goodsClass.getName());
                        DiyActivity.this.mListViewAdapter.setData(DiyActivity.this.goodsTypeList);
                        DiyActivity.this.mListViewAdapter.notifyDataSetChanged();
                        if (isClick)
                            DiyActivity.this.callGoodsListItem(DiyActivity.this.apiService, ((GoodsClass)DiyActivity.this.goodsClassList.get(0)).getId(), 1);
                    }
                });
            }
        })).start();
    }

    private void callGoodsList(HDApiService paramHDApiService, String paramString1, final int page, String paramString2, String paramString3, String paramString4) {
        this.pd.setVisibility(View.VISIBLE);
        this.mNetwork.sendGoodsList(paramString1, page, "1", paramString2, paramString3, paramString4, this, new HttpListener() {
            public void onFailureListener(int param1Int, String param1String) {
                if (DiyActivity.this.ct == null || DiyActivity.this.ct.isFinishing())
                    return;
                DiyActivity.this.pd.setVisibility(View.GONE);
            }

            public void onSuccessListener(int param1Int, String param1String) {
                if (DiyActivity.this.ct != null && !DiyActivity.this.ct.isFinishing()) {
                    DiyActivity.this.pd.setVisibility(View.GONE);
                    try {
                        Log.i(DiyActivity.this.TAG, param1String);
                        GetGoodsListResp getGoodsListResp = ParseGetGoodsListResp.parse(param1String);
                        if (getGoodsListResp != null && getGoodsListResp.isSuccess()) {
                            if (DiyActivity.this.goodsAllAttrs == null)
                                goodsAllAttrs=getGoodsListResp.getGoodsAllAttrs();
//                                DiyActivity.access$1002(DiyActivity.this, getGoodsListResp.getGoodsAllAttrs());
                            if (DiyActivity.this.goodsTypeList.isEmpty())
                                for (GoodsAttr goodsAttr : ((GoodsAllAttr)DiyActivity.this.goodsAllAttrs.get(0)).getGoodsAttrs())
                                    DiyActivity.this.goodsTypeList.add(goodsAttr.getAttr_value());
                            DiyActivity.this.mListViewAdapter.setData(DiyActivity.this.goodsTypeList);
                            DiyActivity.this.mListViewAdapter.notifyDataSetChanged();
                            List list = getGoodsListResp.getGoodses();
                            if (1 == page) {
                                goodses=getGoodsListResp.getGoodses();
//                                DiyActivity.access$1302(DiyActivity.this, list);
                            } else if (DiyActivity.this.goodses != null) {
                                DiyActivity.this.goodses.addAll(list);
                                if (list.isEmpty())
                                    Toast.makeText(DiyActivity.this.ct, getString(R.string.no_more), Toast.LENGTH_LONG).show();
                            }
                            list = new ArrayList();
                            ArrayList arrayList1 = new ArrayList();
                            ArrayList arrayList2 = new ArrayList();
                            for (Goods goods : DiyActivity.this.goodses) {
                                list.add(goods.getName());
                                arrayList1.add(goods.getImg_url());
                                arrayList2.add(Integer.valueOf(goods.getId()));
                            }
                            DiyActivity.this.mGridViewAdapter.setShow(1);
                            DiyActivity.this.mGridViewAdapter.setGoods(DiyActivity.this.goodses);
                            DiyActivity.this.mGridViewAdapter.notifyDataSetChanged();
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void callGoodsListItem(HDApiService paramHDApiService, int paramInt1, final int page) {
        this.pd.setVisibility(View.VISIBLE);
        paramHDApiService.getGoodsListItem(paramInt1, page).enqueue(new Callback<ResponseBody>() {
            public void onFailure(Throwable param1Throwable) {
                if (DiyActivity.this.ct == null || DiyActivity.this.ct.isFinishing())
                    return;
                DiyActivity.this.pd.setVisibility(View.GONE);
            }

            public void onResponse(Response<ResponseBody> param1Response, Retrofit param1Retrofit) {
                if (DiyActivity.this.ct != null && !DiyActivity.this.ct.isFinishing()) {
                    DiyActivity.this.pd.setVisibility(View.GONE);
                    try {
                        String str = ((ResponseBody)param1Response.body()).string();
                        Log.i(DiyActivity.this.TAG, str);
                        GetGoodsListResp getGoodsListResp = ParseGetGoodsListResp.parse(str);
                        if (getGoodsListResp != null && getGoodsListResp.isSuccess()) {
                            List list = getGoodsListResp.getGoodses();
                            if (1 == page) {
                                goodses=list;
//                                DiyActivity.access$1302(DiyActivity.this, list);
                            } else if (DiyActivity.this.goodses != null) {
                                DiyActivity.this.goodses.addAll(list);
                                if (list.isEmpty())
                                    Toast.makeText(DiyActivity.this.ct, getString(R.string.no_more), Toast.LENGTH_SHORT).show();
                            }
                            list = new ArrayList();
                            ArrayList arrayList1 = new ArrayList();
                            ArrayList arrayList2 = new ArrayList();
                            for (Goods goods : DiyActivity.this.goodses) {
                                list.add(goods.getName());
                                arrayList1.add(goods.getImg_url());
                                arrayList2.add(Integer.valueOf(goods.getId()));
                            }
                            DiyActivity.this.mGridViewAdapter.setShow(1);
                            DiyActivity.this.mGridViewAdapter.setGoods(DiyActivity.this.goodses);
                            DiyActivity.this.mGridViewAdapter.notifyDataSetChanged();
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void callSceneList(HDApiService paramHDApiService, int paramInt1, final int page, String paramString1, String paramString2, String paramString3) {
        this.pd.setVisibility(View.VISIBLE);
        this.mNetwork.sendSceneList(paramInt1 + "", page, paramString1, paramString2, paramString3, this, new HttpListener() {
            public void onFailureListener(int param1Int, String param1String) {
                if (DiyActivity.this.ct == null || DiyActivity.this.ct.isFinishing())
                    return;
                DiyActivity.this.pd.setVisibility(View.GONE);
//                DiyActivity.access$2110(DiyActivity.this.ct);
            }

            public void onSuccessListener(int param1Int, String param1String) {
                if (DiyActivity.this.ct != null && !DiyActivity.this.ct.isFinishing()) {
                    DiyActivity.this.pd.setVisibility(View.GONE);
                    try {
                        Log.i(DiyActivity.this.TAG, param1String);
                        GetSceneListResp getSceneListResp = ParseGetSceneListResp.parse(param1String);
                        if (getSceneListResp != null && getSceneListResp.isSuccess()) {
                            if (DiyActivity.this.sceneAllAttrs == null)
                                sceneAllAttrs=getSceneListResp.getSceneAllAttrs();
//                                DiyActivity.access$1602(DiyActivity.this, getSceneListResp.getSceneAllAttrs());
                            if (DiyActivity.this.sceneSpaceList.isEmpty())
                                for (SceneAttr sceneAttr : ((SceneAllAttr)DiyActivity.this.sceneAllAttrs.get(0)).getSceneAttrs())
                                    DiyActivity.this.sceneSpaceList.add(sceneAttr.getAttr_value());
                            DiyActivity.this.mListViewAdapter.setData(DiyActivity.this.sceneSpaceList);
                            DiyActivity.this.mListViewAdapter.notifyDataSetChanged();
                            List list = getSceneListResp.getScenes();
                            if (1 == page) {
                                scenes=list;
//                                DiyActivity.access$1802(DiyActivity.this, list);
                            } else if (DiyActivity.this.scenes != null) {
                                DiyActivity.this.scenes.addAll(list);
                                if (list.isEmpty())
                                    Toast.makeText(DiyActivity.this.ct, R.string.no_more, Toast.LENGTH_SHORT).show();
                            }
                            if (DiyActivity.this.displayFirstScene && DiyActivity.this.scenes != null && !DiyActivity.this.scenes.isEmpty()) {
                                String str;
                                param1Int = (new Random()).nextInt(DiyActivity.this.scenes.size());
                                if (Integer.parseInt(((Scene)DiyActivity.this.scenes.get(param1Int)).getId()) > 1551) {
                                    str = "http://yangguang.bocang.cc/App/yangguang/Public/uploads/scene/" + ((Scene)DiyActivity.this.scenes.get(param1Int)).getPath();
                                } else {
                                    str = "http://bocang.oss-cn-shenzhen.aliyuncs.com/scene/" + ((Scene)DiyActivity.this.scenes.get(param1Int)).getPath();
                                }
                                DiyActivity.this.displaySceneBg(str);
                                displayFirstScene=false;
//                                DiyActivity.access$1902(DiyActivity.this, false);
                            }
                            list = new ArrayList();
                            ArrayList arrayList1 = new ArrayList();
                            ArrayList arrayList2 = new ArrayList();
                            for (Scene scene : DiyActivity.this.scenes) {
                                list.add(scene.getName());
                                arrayList1.add(scene.getPath());
                                arrayList2.add(Integer.valueOf(scene.getId()));
                            }
                            DiyActivity.this.mGridViewAdapter.setShow(2);
                            DiyActivity.this.mGridViewAdapter.setIds(arrayList2);
                            DiyActivity.this.mGridViewAdapter.setScenes(DiyActivity.this.scenes);
                            DiyActivity.this.mGridViewAdapter.notifyDataSetChanged();
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void displayCheckedGoods(final Goods goods) {
        if (this.mSelectedLightSA.size() >= 3) {
            Toast.makeText(this, "调出灯超数，长按可删除", Toast.LENGTH_SHORT).show();
            return;
        }
        this.imageLoader.loadImage("http://yangguang.bocang.cc/App/yangguang/Public/uploads/goods/" + goods.getImg_url() + "!400X400.png", this.options, new SimpleImageLoadingListener() {
            public void onLoadingComplete(String param1String, View param1View, Bitmap param1Bitmap) {
                super.onLoadingComplete(param1String, param1View, param1Bitmap);
//                DiyActivity.access$2208(DiyActivity.this);
                DiyActivity.this.mSelectedLightSA.put(DiyActivity.this.mLightNumber, goods);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(DiyActivity.this.mScreenWidth / 3 * 2 / 3, DiyActivity.this.mScreenWidth / 3 * 2 / 3 * param1Bitmap.getHeight() / param1Bitmap.getWidth());
                if (DiyActivity.this.mSelectedLightSA.size() == 1) {
                    leftMargin=mScreenWidth / 3 * 2 / 3;
                } else if (DiyActivity.this.mSelectedLightSA.size() == 2) {
                    leftMargin=mScreenWidth / 3 * 2 / 3 * 2;
                } else if (DiyActivity.this.mSelectedLightSA.size() == 3) {
                    leftMargin=0;
                }
                layoutParams.setMargins(DiyActivity.this.leftMargin, 0, 0, 0);
                TouchView touchView = new TouchView(DiyActivity.this.ct);
                touchView.setLayoutParams(layoutParams);
                touchView.setImageBitmap(param1Bitmap);
                touchView.setmLightCount(DiyActivity.this.mLightNumber);
                layoutParams = new FrameLayout.LayoutParams(-1, -1);
                FrameLayout frameLayout = new FrameLayout(DiyActivity.this.ct);
                frameLayout.setLayoutParams(layoutParams);
                frameLayout.addView(touchView);
                DiyActivity.this.mFrameLayout.addView(frameLayout);
                touchView.setContainer(DiyActivity.this.mFrameLayout, frameLayout);
            }
        });
    }

    private void displaySceneBg(String paramString) {
        this.screePath = paramString;
        LogUtils.logE("dispBg", "path:" + paramString);
        this.imageLoader.displayImage(paramString, this.mSceneBgIv, this.options, new ImageLoadingListener() {
            public void onLoadingCancelled(String param1String, View param1View) { DiyActivity.this.pd2.setVisibility(View.GONE); }

            public void onLoadingComplete(String param1String, View param1View, Bitmap param1Bitmap) { DiyActivity.this.pd2.setVisibility(View.GONE); }

            public void onLoadingFailed(String param1String, View param1View, FailReason param1FailReason) {
                DiyActivity.this.pd2.setVisibility(View.GONE);
                Toast.makeText(DiyActivity.this, param1FailReason.getCause() + "加载失败", Toast.LENGTH_SHORT).show();
            }

            public void onLoadingStarted(String param1String, View param1View) { DiyActivity.this.pd2.setVisibility(View.VISIBLE); }
        });
    }

    private Bitmap getScreenHot(View paramView) {
        Bitmap bitmap = Bitmap.createBitmap(paramView.getWidth(), paramView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas();
        canvas.setBitmap(bitmap);
        paramView.draw(canvas);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        try {
            byteArrayOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeByteArray(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.toByteArray().length);
    }

    private void initImageLoader() {
        this.options = (new DisplayImageOptions.Builder())
                .showImageOnLoading(R.mipmap.bg_default)
                .showImageForEmptyUri(R.mipmap.bg_default)
                .showImageOnFail(R.mipmap.bg_default)
                .cacheInMemory(false).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
        this.imageLoader = ImageLoader.getInstance();
    }

    private void initView() {
        this.mScreenWidth = (getResources().getDisplayMetrics()).widthPixels;
        this.mFrameLayout = (FrameLayout)findViewById(R.id.sceneFrameLayout);
        this.mFrameLayout.setOnClickListener(this.ct);
        this.pd = (ProgressBar)findViewById(R.id.pd);
        this.mSceneBgIv = (ImageView)findViewById(R.id.sceneBgIv);
        this.mGocarIv = (ImageView)findViewById(R.id.gocarIv);
        this.mDiyContainerRl = (RelativeLayout)findViewById(R.id.diyContainerRl);
        this.mDiyMenuContainerRl = (RelativeLayout)findViewById(R.id.diyMenuContainerRl);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.mDiyMenuContainerRl.getLayoutParams();
        this.mDiyTabLl = (LinearLayout)findViewById(R.id.diyTabLl);
        layoutParams = (RelativeLayout.LayoutParams)this.mDiyTabLl.getLayoutParams();
        layoutParams.height = (int)(0.06666667F * this.mScreenWidth);
        this.mDiyTabLl.setLayoutParams(layoutParams);
        this.mProIv = findViewById(R.id.diyProIv);
        this.mSceneIv = findViewById(R.id.diySceneIv);
        this.mOtherIv = (ImageView)findViewById(R.id.diyOtherIv);
        this.mCameraIv = (ImageView)findViewById(R.id.cameraIv);
        this.mAlbumIv = (ImageView)findViewById(R.id.albumIv);
        this.mShareIv = (ImageView)findViewById(R.id.diyShareIv);
        this.view_take_photo = findViewById(R.id.view_take_photo);
        this.view_gallery = findViewById(R.id.view_gallery);
        this.view_sc = findViewById(R.id.view_sc);
        this.view_save = findViewById(R.id.view_save);
        this.view_share = findViewById(R.id.view_share);
        this.add_data_ll = findViewById(R.id.add_data_ll);
        this.add_data_ll.setOnClickListener(this);
        this.view_take_photo.setOnClickListener(this);
        this.view_gallery.setOnClickListener(this);
        this.view_sc.setOnClickListener(this);
        this.view_save.setOnClickListener(this);
        this.view_share.setOnClickListener(this);
        this.mProIv.setOnClickListener(this);
        this.mSceneIv.setOnClickListener(this);
        this.mOtherIv.setOnClickListener(this);
        this.mCameraIv.setOnClickListener(this);
        this.mAlbumIv.setOnClickListener(this);
        this.mShareIv.setOnClickListener(this);
        this.mGocarIv.setOnClickListener(this);
        this.apiService = (HDApiService)HDRetrofit.create(HDApiService.class);
        this.mListView = (ListView)findViewById(R.id.listView);
        this.mListViewAdapter = new DiyListViewAdapter(this.ct);
        this.mListView.setAdapter(this.mListViewAdapter);
        this.mListView.setOnItemClickListener(this);
        this.mOtherRl = (RelativeLayout)findViewById(R.id.otherRl);
        this.mDiyGridViewContainer = (FrameLayout)findViewById(R.id.diyGridViewContainer);
        layoutParams = (RelativeLayout.LayoutParams)this.mDiyGridViewContainer.getLayoutParams();
        layoutParams.width = (int)(this.mScreenWidth / 3.0F / 4.0F * 3.0F);
        this.mDiyGridViewContainer.setPadding((int)(this.mScreenWidth * 0.006666667F), (int)(this.mScreenWidth * 0.006666667F), (int)(this.mScreenWidth * 0.006666667F), (int)(this.mScreenWidth * 0.006666667F));
        this.mDiyGridViewContainer.setLayoutParams(layoutParams);
        this.mGridView = (GridView)findViewById(R.id.diyGridView);
        this.add_data_tv = (TextView)findViewById(R.id.add_data_tv);
        this.mGridView.setVerticalSpacing((int)(this.mScreenWidth * 0.006666667F));
        this.mGridViewAdapter = new MyDiyGridViewAdapter(this.ct);
        this.mGridView.setAdapter(this.mGridViewAdapter);
        this.mGridView.setOnItemClickListener(this);
        this.pd2 = (ProgressBar)findViewById(R.id.pd2);
    }

    private void pickPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(intent, 1);
    }

    private void setTabBg(View paramView) {
        mProIv.setBackgroundResource(android.R.color.transparent);
        mSceneIv.setBackgroundResource(android.R.color.transparent);
        mOtherIv.setBackgroundResource(android.R.color.transparent);
        paramView.setBackgroundResource(R.color.colorPrimary);
    }
    /**
     //     * 弹出提示
     //     */
    private void showInstallDialog() {
        final EditText inputServer = new EditText(this);
        inputServer.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入密码查看此分类!").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String urlPath=Constant.SETUPPROUCT+inputServer.getText().toString();
                        String json = OtherApi.doGet(urlPath);
                        final Result result= JSON.parseObject(json, Result.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (result.getCode().equals("1")) {
                                    callGoodsListItem(apiService, goodsclassId,1);
                                } else {
                                    Toast.makeText(DiyActivity.this, "密码错误，请重新输入!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }).start();

            }
        });
        builder.show();
    }
//
//    private void showInstallDialog() {
//        final EditText inputServer = new EditText(this);
//        inputServer.setInputType(InputType.);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("������������������������������!").setIcon(17301659).setView(inputServer).setNegativeButton("������", null);
//        builder.setPositiveButton("������", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface param1DialogInterface, int param1Int) { (new Thread(new Runnable(this) {
//                public void run() {
//                    final Result result = (Result)JSON.parseObject(OtherApi.doGet(Constant.SETUPPROUCT + inputServer.getText().toString()), Result.class);
//                    DiyActivity.this.runOnUiThread(new Runnable() {
//                        public void run() {
//                            if (result.getCode().equals("1")) {
//                                DiyActivity.this.callGoodsListItem(DiyActivity.this.apiService, DiyActivity.this.goodsclassId, 1);
//                                return;
//                            }
//                            Toast.makeText(DiyActivity.this, "������������������������������!", 1).show();
//                        }
//                    });
//                }
//            })).start(); }
//        });
//        builder.show();
//    }

    private void switchProOrDiy() {
        if (this.mSelectedTab == 1) {
            this.mGridViewAdapter.setShow(1);
            setTabBg(this.mProIv);
            this.add_data_tv.setText("选择产品");
            this.goodses = MyApplication.mSelectProducts;
            this.mGridViewAdapter.setGoods(this.goodses);
            this.mGridViewAdapter.notifyDataSetChanged();
            return;
        }
        setTabBg(this.mSceneIv);
        this.mGridViewAdapter.setShow(2);
        this.scenes = MyApplication.mSelectScenes;
        this.mGridViewAdapter.setScenes(this.scenes);
        this.mGridViewAdapter.notifyDataSetChanged();
        this.add_data_tv.setText("选择场景");
    }

    private void takePhoto() {
        this.photoName = (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date(System.currentTimeMillis()));
        this.cameraPath = FileUtil.getOwnFilesDir(this, "yangguang/camera");
        Uri uri = Uri.fromFile(new File(this.cameraPath, this.photoName + ".jpg"));
        System.out.println("imageUri" + uri.toString());
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra("output", uri);
        startActivityForResult(intent, 2);
    }


//    上传图片

//    private  int TIME_OUT = 10*1000;   //超时时间
//    private  String CHARSET = "utf-8"; //设置编码
    /**
     * android上传文件到服务器
     * @param file  需要上传的文件
     * @param RequestURL  请求的rul
     * @return  返回响应的内容
     */
    private String uploadFile(Bitmap file, String RequestURL, Map<String, String> param,String imageName){
        String result = null;
        String  BOUNDARY =  UUID.randomUUID().toString();  //边界标识   随机生成
        String PREFIX = "--" , LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";   //内容类型
        // 显示进度框
        //      showProgressDialog();
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true);  //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false);  //不允许使用缓存
            conn.setRequestMethod("POST");  //请求方式
            conn.setRequestProperty("Charset", CHARSET);  //设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            if(file!=null){
                /**
                 * 当文件不为空，把文件包装并且上传
                 */
                DataOutputStream dos = new DataOutputStream( conn.getOutputStream());
                StringBuffer sb = new StringBuffer();

                String params = "";
                if (param != null && param.size() > 0) {
                    Iterator<String> it = param.keySet().iterator();
                    while (it.hasNext()) {
                        sb = null;
                        sb = new StringBuffer();
                        String key = it.next();
                        String value = param.get(key);
                        sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                        sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END).append(LINE_END);
                        sb.append(value).append(LINE_END);
                        params = sb.toString();
                        dos.write(params.getBytes());
                    }
                }
                sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意：
                 * name里面的值为服务器端需要key   只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的   比如:abc.png
                 */
                sb.append("Content-Disposition: form-data; name=\"").append("file").append("\"")
                        .append(";filename=\"").append(imageName).append("\"\n");
                sb.append("Content-Type: image/png");
                sb.append(LINE_END).append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = ImageUtil.Bitmap2InputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while((len=is.read(bytes))!=-1){
                    dos.write(bytes, 0, len);
                }


                is.close();
//                dos.write(file);
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
                dos.write(end_data);

                dos.flush();
                /**
                 * 获取响应码  200=成功
                 * 当响应成功，获取响应的流
                 */

                int res = conn.getResponseCode();
                System.out.println("res========="+res);
                if(res==200){
                    InputStream input =  conn.getInputStream();
                    StringBuffer sb1= new StringBuffer();
                    int ss ;
                    while((ss=input.read())!=-1){
                        sb1.append((char)ss);
                    }
                    result = sb1.toString();
                }
                else{
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void back(View paramView) { finish(); }

    public void close(View paramView) {
        this.mDiyContainerRl.setVisibility(View.INVISIBLE);
        this.isFullScreen = true;
    }

    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        if (paramInt2 == -1) {
            switch (paramInt1) {
                default:
                    super.onActivityResult(paramInt1, paramInt2, paramIntent);
                    return;
                case 2:
                    if (Environment.getExternalStorageState().equals("mounted")) {
                        File file = new File(this.cameraPath, this.photoName + ".jpg");
                        if (file.exists()) {
                            displaySceneBg("file://" + file.toString());
                        } else {
                            Toast.makeText(this, "读取图片失败", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, "没有sd卡", Toast.LENGTH_SHORT).show();
                    }
                case 1:
                    break;
            }
            displaySceneBg(paramIntent.getData().toString());
        }
        if (paramInt1 == 52031) {
            this.mSelectedTab = 1;
            switchProOrDiy();
        }
        if (paramInt1 == 21534) {
            this.mSelectedTab = 2;
            switchProOrDiy();
        }
    }

    public void onClick(View paramView) {
        CartDao cartDao;
        if (paramView == this.mFrameLayout) {
            if (!this.isFullScreen) {
                this.mDiyContainerRl.setVisibility(View.INVISIBLE);
                this.isFullScreen = true;
                return;
            }
            this.mDiyContainerRl.setVisibility(View.VISIBLE);
            this.isFullScreen = false;
            return;
        }
        if (paramView == this.mProIv) {
            setTabBg(this.mProIv);
            this.mListViewAdapter.setSelection(0);
            this.page = 1;
            this.mSelectedTab = 1;
            this.mOtherRl.setVisibility(View.GONE);
            switchProOrDiy();
            return;
        }
        if (paramView == this.mSceneIv) {
            setTabBg(this.mSceneIv);
            this.mListViewAdapter.setSelection(0);
            this.page = 1;
            this.mSelectedTab = 2;
            this.mOtherRl.setVisibility(View.GONE);
            switchProOrDiy();
            return;
        }
        if (paramView == this.mOtherIv) {
            setTabBg(this.mOtherIv);
            this.mOtherRl.setVisibility(View.VISIBLE);
            this.mListView.setVisibility(View.GONE);
            return;
        }
        if (paramView == this.mCameraIv || paramView == this.view_take_photo) {
            PermissionUtils.requestPermission(this, 4, new PermissionUtils.PermissionGrant() {
                public void onPermissionGranted(int param1Int) { DiyActivity.this.takePhoto(); }
            });
            return;
        }
        if (paramView == this.mAlbumIv || paramView == this.view_gallery) {
            pickPhoto();
            return;
        }
        if (paramView == this.mShareIv || paramView == this.view_share) {
            if (this.isShare != true) {
                this.isShare = true;
                StringBuffer sb = new StringBuffer();
                for (byte b = 0; b < this.mSelectedLightSA.size(); b++) {
                    if (this.mSelectedLightSA.get(b) != null)
                        sb.append(((Goods)this.mSelectedLightSA.get(b)).getId() + "");
                    if (b < this.mSelectedLightSA.size() - 1)
                        sb.append(",");
                }
                this.mDiyContainerRl.setVisibility(View.INVISIBLE);
//                Log.v("520", "������������" + System.currentTimeMillis());
                final Bitmap bitmap = ImageUtil.takeScreenShot(this);
                this.imgUri = ScannerUtils.saveImageToGallery(this, bitmap, ScannerUtils.ScannerType.RECEIVER);
                this.mDiyContainerRl.setVisibility(View.VISIBLE);
                this.mLodingDailog.show();
//                Log.v("520", "������������" + System.currentTimeMillis());
                final HashMap hashMap = new HashMap();
                hashMap.put("goods_id", sb.toString());
                hashMap.put("phone", "android");
                hashMap.put("title", "share");
                hashMap.put("user_id", this.mInfo.getId() + "");
                hashMap.put("village", "unknown");
                (new Thread(new Runnable() {
                    public void run() {
                        final Result result = (Result)JSON.parseObject(DiyActivity.this.uploadFile(bitmap, "http://yangguang.bocang.cc/index.php/Interface/upload_plan", hashMap, (new SimpleDateFormat("yyyyMMddhhmmss")).format(new Date()) + ".png"), Result.class);
//                        Log.v("520", "���������������" + System.currentTimeMillis());
                        DiyActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                DiyActivity.this.mLodingDailog.dismiss();
                                Log.v("520it", "������������!");
                                if (TextUtils.isEmpty(result.getResult()) || result.getResult() == "0")
                                    return;
                                UIUtils.showShareDialog(DiyActivity.this, bitmap, "", DiyActivity.this.imgUri);
                            }
                        });
                    }
                })).start();
                this.isShare = false;
                return;
            }
            return;
        }
        if (paramView == this.mGocarIv || paramView == this.view_sc) {
            cartDao = new CartDao(this.ct);
            boolean bool = false;
            for (byte b = 0; b < this.mSelectedLightSA.size(); b++) {
                if (-1L != cartDao.replaceOne((Goods)this.mSelectedLightSA.valueAt(b)))
                    bool = true;
            }
            if (bool == true) {
                tip("添加成功！");
                Broad.sendLocalBroadcast(this.ct, "cart_change_ACTION", null);
                return;
            }
            return;
        }
        if (paramView == this.add_data_ll) {
            if (this.mSelectedTab == 1) {
                selectProduct();
                return;
            }
            selectSceneDatas();
            return;
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_diy_2);
        setColor(this, getResources().getColor(R.color.colorPrimary));
        this.mNetwork = new Network();
        this.apiService = (HDApiService)HDRetrofit.create(HDApiService.class);
        this.mInfo = ((MyApplication)getApplication()).mUserInfo;
        Intent intent = getIntent();
        initView();
        initImageLoader();
        this.mLodingDailog = new LoadingDailog(this, R.style.CustomDialog);
        String str = intent.getStringExtra("from");
        if ("scene".equals(str)) {
            displaySceneBg(intent.getStringExtra("url"));
            this.mSceneIv.performClick();
        } else if ("goods".equals(str)) {
            Goods goods = (Goods)intent.getSerializableExtra("goods");
            this.displayFirstScene = true;
            boolean noContain = true;
//            int  b = 0;
//            while (true) {
//                boolean bool1 = bool;
            for(int i=0;i<MyApplication.mSelectProducts.size();i++){
                if (((Goods)MyApplication.mSelectProducts.get(i)).getId() == goods.getId()) {
                    noContain=false;
                }
            }
                if (noContain)
                    MyApplication.mSelectProducts.add(goods);
                displayCheckedGoods(goods);
                this.mProIv.performClick();
//            }
        }
//        setColor(this, getResources().getColor(2131492891));
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
        String[] arrayOfString;
        Log.v("520it", "ffff");
        if (paramAdapterView == this.mListView) {
            this.mListViewAdapter.setSelection(paramInt);
            this.mListViewAdapter.notifyDataSetChanged();
            if (this.mSelectedTab == 1) {
                if (this.goodsAllAttrs != null)
                    Log.v("520it", this.fitterStr);
                this.page = 1;
                this.goodsclassId = ((GoodsClass)this.goodsClassList.get(paramInt)).getId();
                Log.v("520it", "goodsclassId:" + this.goodsclassId);
                Log.v("520it", "xx:" + IndexActivity.mCId);
                if (TextUtils.isEmpty(IndexActivity.mCId)) {
                    callGoodsListItem(this.apiService, this.goodsclassId, 1);
                    return;
                }
                arrayOfString = IndexActivity.mCId.split(",");
                Log.v("520it", "cIdArrys:" + arrayOfString.toString());
                if (arrayOfString.length > 0 && Arrays.binarySearch(arrayOfString, this.goodsclassId + "") >= 0) {
                    showInstallDialog();
                    return;
                }
                return;
            }
            if (this.mSelectedTab == 2) {
                if (this.sceneAllAttrs != null)
                    this.fitterStr = ((SceneAttr)((SceneAllAttr)this.sceneAllAttrs.get(0)).getSceneAttrs().get(paramInt)).getScene_id() + ".0";
                this.page = 1;
                callSceneList(this.apiService, 0, 1, null, null, this.fitterStr);
                return;
            }
            return;
        }
        if (paramAdapterView == this.mGridView) {
            if (this.mSelectedTab == 1) {
//                Log.v("520it", "���������B");
                displayCheckedGoods((Goods)this.goodses.get(paramInt));
                return;
            }
            if (this.mSelectedTab == 2) {
                String str;
                if (Integer.parseInt(((Scene)this.scenes.get(paramInt)).getId()) > 1551) {
                    str = "http://yangguang.bocang.cc/App/yangguang/Public/uploads/scene/" + ((Scene)this.scenes.get(paramInt)).getPath();
                } else {
                    str = "http://bocang.oss-cn-shenzhen.aliyuncs.com/scene/" + ((Scene)this.scenes.get(paramInt)).getPath();
                }
                displaySceneBg(str);
                return;
            }
        }
    }

    public void onLoadMore(PullToRefreshLayout paramPullToRefreshLayout) {
        if (this.mSelectedTab == 1) {
            HDApiService hDApiService = this.apiService;
            String str = IndexActivity.mCId;
            int i = this.page + 1;
            this.page = i;
            callGoodsList(hDApiService, str, i, null, null, this.fitterStr);
            hDApiService = this.apiService;
            i = this.goodsclassId;
            int j = this.page + 1;
            this.page = j;
            callGoodsListItem(hDApiService, i, j);
            return;
        }
        if (this.mSelectedTab == 2) {
            HDApiService hDApiService = this.apiService;
            int i = this.page + 1;
            this.page = i;
            callSceneList(hDApiService, 0, i, null, null, this.fitterStr);
            return;
        }
    }

    public void onRefresh(PullToRefreshLayout paramPullToRefreshLayout) {
        this.page = 1;
        if (this.mSelectedTab == 1) {
            callGoodsClass(false);
            callGoodsListItem(this.apiService, this.goodsclassId, this.page);
            return;
        }
        if (this.mSelectedTab == 2) {
            callSceneList(this.apiService, 0, this.page, null, null, this.fitterStr);
            return;
        }
    }

    public void onRequestPermissionsResult(int paramInt, String[] paramArrayOfString, int[] paramArrayOfInt) {
        super.onRequestPermissionsResult(paramInt, paramArrayOfString, paramArrayOfInt);
        PermissionUtils.requestPermissionsResult(this, paramInt, paramArrayOfString, paramArrayOfInt, new PermissionUtils.PermissionGrant() {
            public void onPermissionGranted(int param1Int) { DiyActivity.this.takePhoto(); }
        });
    }

    protected void onResume() { super.onResume(); }

    public void selectProduct() {
        this.mIntent = new Intent(this, SelectGoodsActivity.class);
        this.mIntent.putExtra("isselectedGoods", true);
        startActivityForResult(this.mIntent, 52031);
    }

    public void selectSceneDatas() {
        this.mIntent = new Intent(this, SelectSceneActivity.class);
        this.mIntent.putExtra("ISSELECTSCRENES", true);
        startActivityForResult(this.mIntent, 21534);
    }

    class MyDiyGridViewAdapter extends BaseAdapter {
        List<Goods> goods = new ArrayList();

        private List<Integer> ids;

        private ImageLoader imageLoader;

        private Context mContext;

        private LayoutInflater mInflater;

        private int mScreenHeight;

        private int mScreenWidth;

        private DisplayImageOptions options;

        List<Scene> scenes = new ArrayList();

        int show = 1;

        public MyDiyGridViewAdapter(Context param1Context) {
            this.mContext = param1Context;
            this.mScreenWidth = (param1Context.getResources().getDisplayMetrics()).widthPixels;
            this.mScreenHeight = (param1Context.getResources().getDisplayMetrics()).heightPixels;
            this.mInflater = LayoutInflater.from(param1Context);
            this.options = (new DisplayImageOptions.Builder()).considerExifParams(true).imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).build();
            this.imageLoader = ImageLoader.getInstance();
        }

        public int getCount() { return (this.show == 1) ? ((this.goods != null) ? this.goods.size() : 0) : ((this.scenes != null) ? this.scenes.size() : 0); }

        public Object getItem(int param1Int) { return ((this.show == 1) ? (this.goods == null) : (this.scenes == null)) ? null : ((this.show == 1) ? this.goods.get(param1Int) : this.scenes.get(param1Int)); }

        public long getItemId(int param1Int) { return param1Int; }

        public View getView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
            ViewHolder viewHolder;
            View view;
            String str4 = "";
            if (param1View == null) {
                view = this.mInflater.inflate(R.layout.item_gv_diy, null);
                viewHolder = new ViewHolder();
                viewHolder.iv = (ImageView)view.findViewById(R.id.imageView);
                viewHolder.tv = (TextView)view.findViewById(R.id.textView);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder)param1View.getTag();
                view = param1View;
            }
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)viewHolder.iv.getLayoutParams();
            int j = (int)(this.mScreenWidth / 3.0F - 0.06666667F * this.mScreenWidth - 0.013333334F * this.mScreenWidth);
            int i = 0;
            String str2 = "";
            String str3 = "";
            if (this.show == 1) {
                i = j;
                str1 = "http://yangguang.bocang.cc/App/yangguang/Public/uploads/goods/";
                str2 = ((Goods)this.goods.get(param1Int)).getName();
                str3 = ((Goods)this.goods.get(param1Int)).getImg_url();
            } else {
                str1 = str4;
                if (this.show == 2) {
                    str2 = ((Scene)this.scenes.get(param1Int)).getName();
                    str3 = ((Scene)this.scenes.get(param1Int)).getPath();
                    i = (int)(j / 4.0F * 3.0F);
                    if (Integer.parseInt(((Scene)this.scenes.get(param1Int)).getId()) > 1551) {
                        str1 = "http://yangguang.bocang.cc/App/yangguang/Public/uploads/scene/";
                    } else {
                        str1 = "http://bocang.oss-cn-shenzhen.aliyuncs.com/scene/";
                    }
                }
            }
            layoutParams.width = j;
            layoutParams.height = i;
            viewHolder.iv.setLayoutParams(layoutParams);
            if (!str2.isEmpty()) {
                viewHolder.tv.setText(str2);
                viewHolder.tv.setTextColor(Color.parseColor("#ffffff"));
            }
            try {
                this.imageLoader.displayImage(str1 + str3, viewHolder.iv, this.options);
                return view;
            } catch (Exception str1) {
                return view;
            }
        }

        public void setGoods(List<Goods> param1List) { this.goods = param1List; }

        public void setIds(List<Integer> param1List) { this.ids = param1List; }

        public void setScenes(List<Scene> param1List) { this.scenes = param1List; }

        public void setShow(int param1Int) { this.show = param1Int; }

        class ViewHolder {
            ImageView iv;

            TextView tv;
        }
    }

}
