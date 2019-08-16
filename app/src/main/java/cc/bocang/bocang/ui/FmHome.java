//package cc.bocang.bocang.ui;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.AnimationSet;
//import android.view.animation.TranslateAnimation;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bigkoo.convenientbanner.CBPageAdapter;
//import com.bigkoo.convenientbanner.CBViewHolderCreator;
//import com.bigkoo.convenientbanner.ConvenientBanner;
//import com.lib.common.hxp.view.PullToRefreshLayout;
//import com.lib.common.hxp.view.StatedFragment;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//import com.squareup.okhttp.ResponseBody;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cc.bocang.bocang.R;
//import cc.bocang.bocang.data.api.HDApiService;
//import cc.bocang.bocang.data.api.HDRetrofit;
//import cc.bocang.bocang.data.model.Ad;
//import cc.bocang.bocang.data.model.Goods;
//import cc.bocang.bocang.data.model.GoodsAllAttr;
//import cc.bocang.bocang.data.model.GoodsAttr;
//import cc.bocang.bocang.data.parser.ParseGetAdResp;
//import cc.bocang.bocang.data.parser.ParseGetGoodsListResp;
//import cc.bocang.bocang.data.response.GetAdResp;
//import cc.bocang.bocang.data.response.GetGoodsListResp;
//import cc.bocang.bocang.global.Constant;
//import cc.bocang.bocang.global.IntentKeys;
//import cc.bocang.bocang.utils.ConvertUtil;
//import cc.bocang.bocang.utils.ResUtil;
//import cc.bocang.bocang.utils.StringUtil;
//import cc.bocang.bocang.utils.UniversalUtil;
//import cc.bocang.bocang.utils.net.HttpListener;
//import cc.bocang.bocang.utils.net.Network;
//import retrofit.Call;
//import retrofit.Callback;
//import retrofit.Response;
//import retrofit.Retrofit;
//
//public class FmHome extends StatedFragment implements View.OnClickListener, AdapterView.OnItemClickListener, PullToRefreshLayout.OnRefreshListener {
//    private final String TAG = FmHome.class.getSimpleName();
//
//    @Override
//    protected void onRestoreState(Bundle savedInstanceState) {
//        super.onRestoreState(savedInstanceState);
//    }
//
//    @Override
//    protected void onSaveState(Bundle outState) {
//        super.onSaveState(outState);
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (Constant.isDebug)
//            Log.i(TAG, "onCreate...");
//    }
//    private HDApiService apiService;
//    private int page = 1;
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (Constant.isDebug)
//            Log.i(TAG, "onCreateView...");
//        View view = initView(inflater, container);
//        mNetwork=new Network();
//        initList();
//
//        apiService = HDRetrofit.create(HDApiService.class);
//        callAd(apiService);
//        page = 1;
//        callGoodsList(apiService, IndexActivity.mCId, page, null, "is_best", null);
//
//        mTypeTv.performClick();
//
//        return view;
//    }
//
//    private void initList() {
//        typeList.clear();
//        spaceList.clear();
//        styleList.clear();
//        typeResList.clear();
//        spaceResList.clear();
//        styleResList.clear();
//
////        typeList.add("现代家居灯");
////        typeList.add("现代水晶灯");
////        typeList.add("金色水晶灯");
////        typeList.add("欧式灯");
////        typeList.add("中式艺术灯");
////        typeList.add("商业照明灯");
//        typeList.add("后现代装饰灯");
//        typeList.add("现代家居灯");
//        typeList.add("现代艺术灯");
//        typeList.add("轻奢水晶灯");
//        typeList.add("欧式蜡烛灯");
//        typeList.add("中式艺术灯");
//        typeList.add("商业照明");
//        typeList.add("更多");
//
//        spaceList.add("客厅");
//        spaceList.add("餐厅");
//        spaceList.add("卧室");
//        spaceList.add("书房");
//        spaceList.add("卫生间");
//        spaceList.add("阳台");
//        spaceList.add("玄关/过道");
//        spaceList.add("更多");
//
//        styleList.add("现代简约");
//        styleList.add("中式");
//        styleList.add("新中式");
//        styleList.add("欧式");
//        styleList.add("美式");
//        styleList.add("田园");
//        styleList.add("新古典");
//        styleList.add("更多");
//
//        for (int i = 0; i < 8; i++) {
//            if (i == 7) {
//                typeResList.add(ResUtil.getResMipmap(getActivity(), "type_" + i));
//                spaceResList.add(ResUtil.getResMipmap(getActivity(), "type_" + i));
//                styleResList.add(ResUtil.getResMipmap(getActivity(), "type_" + i));
//            } else {
//                typeResList.add(ResUtil.getResMipmap(getActivity(), "type_" + i));
//                spaceResList.add(ResUtil.getResMipmap(getActivity(), "space_" + i));
//                styleResList.add(ResUtil.getResMipmap(getActivity(), "style_" + i));
//            }
//        }
//
//        //默认选中类型
//        nameList = typeList;
//        imageResList = typeResList;
//        mItemAdapter.notifyDataSetChanged();
//    }
//
//    private List<String> nameList;
//    private List<Integer> imageResList;
//
//    //下级选项名称列表
//    private List<String> typeList = new ArrayList<>();
//    private List<String> spaceList = new ArrayList<>();
//    private List<String> styleList = new ArrayList<>();
//    //对应的按钮图片
//    private List<Integer> typeResList = new ArrayList<>();
//    private List<Integer> spaceResList = new ArrayList<>();
//    private List<Integer> styleResList = new ArrayList<>();
//    //对应的筛选值列表
//    private List<Integer> typeGoodsIdList = new ArrayList<>();
//    private List<Integer> spaceGoodsIdList = new ArrayList<>();
//    private List<Integer> styleGoodsIdList = new ArrayList<>();
//
//    private List<Goods> goodses;
//    private Network mNetwork;
//
//    private void callGoodsList(HDApiService apiService, String c_id, final int page, String keywords, String type, String filter_attr) {
//        //重新获取，先清空列表
//        typeGoodsIdList.clear();
//        spaceGoodsIdList.clear();
//        styleGoodsIdList.clear();
//        mNetwork.sendGoodsList(c_id, page, "1", keywords, type, filter_attr, FmHome.this.getActivity(), new HttpListener() {
//            @Override
//            public void onSuccessListener(int what, String response) {
//                if (null == getActivity() || getActivity().isFinishing())
//                    return;
//                if (null != mPullToRefreshLayout) {
//                    mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
//                    mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
//                }
//                try {
//                    String json = response;
//                    Log.i(TAG, json);
//                    GetGoodsListResp resp = ParseGetGoodsListResp.parse(json);
//                    if (null != resp && resp.isSuccess()) {
//                        List<GoodsAllAttr> goodsAllAttrs = resp.getGoodsAllAttrs();
//                        for (int i = 0; i < goodsAllAttrs.size(); i++) {
//                            GoodsAllAttr goodsAllAttr = goodsAllAttrs.get(i);
//                            if (i == 0) {//类型
//                                String attrName = goodsAllAttr.getAttrName();
//                                mTypeTv.setText(attrName);
//                                List<GoodsAttr> goodsAttrs = goodsAllAttr.getGoodsAttrs();
//                                for (int j = 0; j < goodsAttrs.size(); j++) {
//                                    if (j != 0) {//过滤掉“全部”
////                                        if (!goodsAttrs.get(j).getAttr_value().equals("全部")) {//过滤掉“全部”
//                                        GoodsAttr goodsAttr = goodsAttrs.get(j);
//                                        typeGoodsIdList.add(goodsAttr.getGoods_id());
//                                    }
//                                }
//                            } else if (i == 1) {//空间
//                                String attrName = goodsAllAttr.getAttrName();
//                                mSpaceTv.setText(attrName);
//                                List<GoodsAttr> goodsAttrs = goodsAllAttr.getGoodsAttrs();
//                                for (int j = 0; j < goodsAttrs.size(); j++) {
//                                    if (j != 0) {//过滤掉“全部”
//                                        GoodsAttr goodsAttr = goodsAttrs.get(j);
//                                        spaceGoodsIdList.add(goodsAttr.getGoods_id());
//                                    }
//                                }
//                            } else if (i == 2) {//风格
//                                String attrName = goodsAllAttr.getAttrName();
//                                mStyleTv.setText(attrName);
//                                List<GoodsAttr> goodsAttrs = goodsAllAttr.getGoodsAttrs();
//                                for (int j = 0; j < goodsAttrs.size(); j++) {
//                                    if (j != 0) {//过滤掉“全部”
//                                        GoodsAttr goodsAttr = goodsAttrs.get(j);
//                                        styleGoodsIdList.add(goodsAttr.getGoods_id());
//                                    }
//                                }
//                            }
//                        }
//
//                        List<Goods> goodsList = resp.getGoodses();
//                        if (1 == page)
//                            goodses = goodsList;
//                        else if (null != goodses){
//                            goodses.addAll(goodsList);
//                            if(goodsList.isEmpty())
//                                Toast.makeText(getActivity(), "没有更多内容了", Toast.LENGTH_LONG).show();
//                        }
//                        mProAdapter.notifyDataSetChanged();
//                    }
//                } catch (Exception e) {
//                    if (null != getActivity() && !getActivity().isFinishing())
//                        e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailureListener(int what, String ans) {
//                if (null == getActivity() || getActivity().isFinishing())
//                    return;
//                FmHome.this.page--;
//                if (null != mPullToRefreshLayout) {
//                    mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
//                    mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
//                }
//            }
//        });
//    }
//
////    private void callGoodsList(HDApiService apiService, String c_id, final int page, String keywords, String type, String filter_attr) {
////        //重新获取，先清空列表
////        typeGoodsIdList.clear();
////        spaceGoodsIdList.clear();
////        styleGoodsIdList.clear();
////        Call<ResponseBody> call = apiService.getGoodsList(c_id, page,1, null, type, filter_attr);
////        call.enqueue(new Callback<ResponseBody>() {//开启异步网络请求
////            @Override
////            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
////                if (null == getActivity() || getActivity().isFinishing())
////                    return;
////                if (null != mPullToRefreshLayout) {
////                    mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
////                    mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
////                }
////                try {
////                    String json = response.body().string();
////                    Log.i(TAG, json);
////                    GetGoodsListResp resp = ParseGetGoodsListResp.parse(json);
////                    if (null != resp && resp.isSuccess()) {
////                        List<GoodsAllAttr> goodsAllAttrs = resp.getGoodsAllAttrs();
////                        for (int i = 0; i < goodsAllAttrs.size(); i++) {
////                            GoodsAllAttr goodsAllAttr = goodsAllAttrs.get(i);
////                            if (i == 0) {//类型
////                                String attrName = goodsAllAttr.getAttrName();
////                                mTypeTv.setText(attrName);
////                                List<GoodsAttr> goodsAttrs = goodsAllAttr.getGoodsAttrs();
////                                for (int j = 0; j < goodsAttrs.size(); j++) {
////                                    if (j != 0) {//过滤掉“全部”
////                                        GoodsAttr goodsAttr = goodsAttrs.get(j);
////                                        typeGoodsIdList.add(goodsAttr.getGoods_id());
////                                    }
////                                }
////                            } else if (i == 1) {//空间
////                                String attrName = goodsAllAttr.getAttrName();
////                                mSpaceTv.setText(attrName);
////                                List<GoodsAttr> goodsAttrs = goodsAllAttr.getGoodsAttrs();
////                                for (int j = 0; j < goodsAttrs.size(); j++) {
////                                    if (j != 0) {//过滤掉“全部”
////                                        GoodsAttr goodsAttr = goodsAttrs.get(j);
////                                        spaceGoodsIdList.add(goodsAttr.getGoods_id());
////                                    }
////                                }
////                            } else if (i == 2) {//风格
////                                String attrName = goodsAllAttr.getAttrName();
////                                mStyleTv.setText(attrName);
////                                List<GoodsAttr> goodsAttrs = goodsAllAttr.getGoodsAttrs();
////                                for (int j = 0; j < goodsAttrs.size(); j++) {
////                                    if (j != 0) {//过滤掉“全部”
////                                        GoodsAttr goodsAttr = goodsAttrs.get(j);
////                                        styleGoodsIdList.add(goodsAttr.getGoods_id());
////                                    }
////                                }
////                            }
////                        }
////
////                        List<Goods> goodsList = resp.getGoodses();
////                        if (1 == page)
////                            goodses = goodsList;
////                        else if (null != goodses){
////                            goodses.addAll(goodsList);
////                            if(goodsList.isEmpty())
////                                Toast.makeText(getActivity(), "没有更多内容了", Toast.LENGTH_LONG).show();
////                        }
////                        mProAdapter.notifyDataSetChanged();
////                    }
////                } catch (Exception e) {
////                    if (null != getActivity() && !getActivity().isFinishing())
//////                    Toast.makeText(getActivity(), "数据异常...", Toast.LENGTH_SHORT).show();
////                    e.printStackTrace();
////                }
////            }
////
////            @Override
////            public void onFailure(Throwable t) {
////                if (null == getActivity() || getActivity().isFinishing())
////                    return;
////                FmHome.this.page--;
////                if (null != mPullToRefreshLayout) {
////                    mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
////                    mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
////                }
//////                if (null != getActivity() && !getActivity().isFinishing())
//////                Toast.makeText(getActivity(), "无法连接服务器...", Toast.LENGTH_SHORT).show();
////            }
////        });
////    }
//
//    /**
//     * 广告背景
//     */
//    private void callAd(HDApiService apiService) {
//        Call<ResponseBody> call = apiService.getAd(Constant.AD_PARAM);
//        call.enqueue(new Callback<ResponseBody>() {//开启异步网络请求
//            @Override
//            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
//                if (null == getActivity() || getActivity().isFinishing())
//                    return;
//                try {
//                    String json = response.body().string();
//                    Log.i(TAG, "广告：" + json);
//                    GetAdResp resp = ParseGetAdResp.parse(json);
//                    if (null != resp && resp.isSuccess()) {
//                        List<Ad> ads = resp.getBeans();
//                        List<String> imgUrl = new ArrayList<>();
//                        for (Ad ad : ads) {
//                            imgUrl.add(ad.getPath());
//                        }
//                        List<String> paths = StringUtil.preToStringArray(Constant.AD_URL, imgUrl);
//
//                        mConvenientBanner.setPages(
//                                new CBViewHolderCreator<NetworkImageHolderView>() {
//                                    @Override
//                                    public NetworkImageHolderView createHolder() {
//                                        return new NetworkImageHolderView();
//                                    }
//                                }, paths);
//                    }
//                } catch (Exception e) {
////                    Toast.makeText(getActivity(), "数据异常...", Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                if (null == getActivity() || getActivity().isFinishing())
//                    return;
////                Toast.makeText(getActivity(), "无法连接服务器...", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    class NetworkImageHolderView implements CBPageAdapter.Holder<String> {
//        private ImageView imageView;
//
//        @Override
//        public View createView(Context context) {
//            // 你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
//            imageView = new ImageView(context);
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            return imageView;
//        }
//
//        @Override
//        public void UpdateUI(Context context, final int position, String data) {
//            imageView.setImageResource(R.mipmap.bg_default);
//            ImageLoader.getInstance().displayImage(data, imageView);
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // 点击事件
//                    if (Constant.isDebug)
//                        Toast.makeText(view.getContext(), "点击了第" + position + "个", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
//
//    private float mCurrentCheckedRadioLeft;//当前被选中的Button距离左侧的距离
//    private TextView mCheckedTv;//当前被选中的tab
//
//    @Override
//    public void onClick(View v) {
//        AnimationSet animationSet = new AnimationSet(true);
//        TranslateAnimation translateAnimation = null;
//        reSetTextColor();
//        if (v == mTypeTv) {
//            mCheckedTv = mTypeTv;
//            mTypeTv.setTextColor(getResources().getColor(R.color.colorPrimaryRed));
//            translateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, 0f, -20f, -20f);
//            nameList = typeList;
//            imageResList = typeResList;
//            mItemAdapter.notifyDataSetChanged();
//        } else if (v == mSpaceTv) {
//            mCheckedTv = mSpaceTv;
//            mSpaceTv.setTextColor(getResources().getColor(R.color.colorPrimaryRed));
//            translateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, mScreenWidth / 3f, -20f, -20f);
//            nameList = spaceList;
//            imageResList = spaceResList;
//            mItemAdapter.notifyDataSetChanged();
//        } else if (v == mStyleTv) {
//            mCheckedTv = mStyleTv;
//            mStyleTv.setTextColor(getResources().getColor(R.color.colorPrimaryRed));
//            translateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, mScreenWidth * 2f / 3f, -20f, -20f);
//            nameList = styleList;
//            imageResList = styleResList;
//            mItemAdapter.notifyDataSetChanged();
//        }
//        animationSet.addAnimation(translateAnimation);
//        animationSet.setFillBefore(false);
//        animationSet.setFillAfter(true);
//        animationSet.setDuration(100);
//        mLineIv.startAnimation(animationSet);
//
//        mCurrentCheckedRadioLeft = getCurrentCheckedRadioLeft(v);//更新当前横条距离左边的距离
//    }
//
//    private void reSetTextColor() {
//        if(IntentKeys.ISSPECIALSHOw){
//            mTypeTv.setTextColor(Color.WHITE);
//            mSpaceTv.setTextColor(Color.WHITE);
//            mStyleTv.setTextColor(Color.WHITE);
//        }else{
//            mTypeTv.setTextColor(Color.GRAY);
//            mSpaceTv.setTextColor(Color.GRAY);
//            mStyleTv.setTextColor(Color.GRAY);
//        }
//    }
//
//    /**
//     * 获得当前被选中的Button距离左侧的距离
//     */
//    private float getCurrentCheckedRadioLeft(View v) {
//        if (v == mTypeTv) {
//            return 0f;
//        } else if (v == mSpaceTv) {
//            return mScreenWidth / 3f;
//        } else if (v == mStyleTv) {
//            return mScreenWidth * 2f / 3f;
//        }
//        return 0f;
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if (parent == mItemGridView) {
//            if (Constant.isDebug)
//                Toast.makeText(getActivity(), ((TextView) view.findViewById(R.id.textView)).getText(), Toast.LENGTH_SHORT).show();
//            if (mCheckedTv == mTypeTv) {
//                ((IndexActivity) getActivity()).titlePos = 0;
//                ((IndexActivity) getActivity()).itemPos =  position == 7 ? 0 : position + 1;
//                ((IndexActivity) getActivity()).goodsId =  position == 7 ? 0 : typeGoodsIdList.get(position);
//            } else if (mCheckedTv == mSpaceTv) {
//                ((IndexActivity) getActivity()).titlePos = 1;
//                ((IndexActivity) getActivity()).itemPos = position >= spaceGoodsIdList.size() - 1 || position == 7 ? 0 : position + 1;
//                ((IndexActivity) getActivity()).goodsId = position >= spaceGoodsIdList.size() - 1 || position == 7 ? 0 : spaceGoodsIdList.get(position);
//            } else if (mCheckedTv == mStyleTv) {
//                ((IndexActivity) getActivity()).titlePos = 2;
//                ((IndexActivity) getActivity()).itemPos = position >= styleGoodsIdList.size() - 1 || position == 7 ? 0 : position + 1;
//                ((IndexActivity) getActivity()).goodsId = position >= styleGoodsIdList.size() - 1 || position == 7 ? 0 : styleGoodsIdList.get(position);
//            }
//            ((IndexActivity) getActivity()).isClickFmHomeItem = true;
////            ((IndexActivity) getActivity()).fragmentsUpdateFlag[1] = true;//FmProduct重新onCreate（界面会有短暂的延迟）
//            ((IndexActivity) getActivity()).mFragmentPagerAdapter.notifyDataSetChanged();
//            ((IndexActivity) getActivity()).mViewPager.setCurrentItem(1, true);
//        } else if (parent == mProGridView) {
//            if (Constant.isDebug)
//                Toast.makeText(getActivity(), "点击产品：" + position, Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(getActivity(), ProDetailActivity.class);
//            intent.putExtra("id", goodses.get(position).getId());
//            intent.putExtra("imgurl",goodses.get(position).getImg_url());
//            intent.putExtra("goodsname",goodses.get(position).getName());
//
//            startActivity(intent);
//        }
//    }
//
//    @Override
//    public void onRefresh(final PullToRefreshLayout pullToRefreshLayout)
//    {
//        callAd(apiService);
//        page = 1;
//        callGoodsList(apiService, IndexActivity.mCId, page, null, "is_best", null);
//    }
//
//    @Override
//    public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout)
//    {
//        callGoodsList(apiService, IndexActivity.mCId, ++page, null, "is_best", null);
//    }
//
//    private class ItemAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            if (null == nameList)
//                return 0;
//            return nameList.size();
//        }
//
//        @Override
//        public String getItem(int position) {
//            if (null == nameList)
//                return null;
//            return nameList.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder;
//
//            if (convertView == null) {
//                convertView = View.inflate(getActivity(), R.layout.item_gridview_fm_home, null);
//
//                holder = new ViewHolder();
//                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
//                holder.textView = (TextView) convertView.findViewById(R.id.textView);
//
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//
//            holder.textView.setText(nameList.get(position));
//            holder.imageView.setImageResource(imageResList.get(position));
//
//            return convertView;
//        }
//
//        class ViewHolder {
//            ImageView imageView;
//            TextView textView;
//        }
//    }
//
//    private class ProAdapter extends BaseAdapter {
//        private DisplayImageOptions options;
//        private ImageLoader imageLoader;
//
//        public ProAdapter() {
//            options = new DisplayImageOptions.Builder()
//                    // 设置图片下载期间显示的图片
//                    .showImageOnLoading(R.mipmap.bg_default)
//                    // 设置图片Uri为空或是错误的时候显示的图片
//                    .showImageForEmptyUri(R.mipmap.bg_default)
//                    // 设置图片加载或解码过程中发生错误显示的图片
//                    // .showImageOnFail(R.drawable.ic_error)
//                    // 设置下载的图片是否缓存在内存中
//                    .cacheInMemory(true)
//                    // 设置下载的图片是否缓存在SD卡中
//                    .cacheOnDisk(true)
//                    // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
//                    // 是否考虑JPEG图像EXIF参数（旋转，翻转）
//                    .considerExifParams(true)
//                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片可以放大（要填满ImageView必须配置memoryCacheExtraOptions大于Imageview）
//                    // .displayer(new FadeInBitmapDisplayer(100))//
//                    // 图片加载好后渐入的动画时间
//                    .build(); // 构建完成
//
//            // 得到ImageLoader的实例(使用的单例模式)
//            imageLoader = ImageLoader.getInstance();
//        }
//
//        @Override
//        public int getCount() {
//            if (null == goodses)
//                return 0;
//            return goodses.size();
//        }
//
//        @Override
//        public Goods getItem(int position) {
//            if (null == goodses)
//                return null;
//            return goodses.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder;
//
//            if (convertView == null) {
//                convertView = View.inflate(getActivity(), R.layout.item_gridview_fm_product, null);
//
//                holder = new ViewHolder();
//                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
//                holder.textView = (TextView) convertView.findViewById(R.id.textView);
//                RelativeLayout.LayoutParams lLp = (RelativeLayout.LayoutParams) holder.imageView.getLayoutParams();
//                float h = (mScreenWidth - ConvertUtil.dp2px(getActivity(), 45.8f)) / 2;
//                lLp.height = (int) h;
//                holder.imageView.setLayoutParams(lLp);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//
//            holder.textView.setText(goodses.get(position).getName());
//
//            imageLoader.displayImage(Constant.PRODUCT_URL + goodses.get(position).getImg_url() + "!400X400.png", holder.imageView, options);
//
//            return convertView;
//        }
//
//        class ViewHolder {
//            ImageView imageView;
//            TextView textView;
//        }
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        // 开始自动翻页
//        mConvenientBanner.startTurning(UniversalUtil.randomA2B(3000, 5000));
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        // 停止翻页
//        mConvenientBanner.stopTurning();
//    }
//
//
//    private GridView mItemGridView, mProGridView;
//    private ItemAdapter mItemAdapter;
//    private ProAdapter mProAdapter;
//    private ConvenientBanner mConvenientBanner;
//    private int mScreenWidth;
//    private TextView mTypeTv, mSpaceTv, mStyleTv;
//    private ImageView mLineIv;
//    private PullToRefreshLayout mPullToRefreshLayout;
//
//    private View initView(LayoutInflater inflater, ViewGroup container) {
//        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
//        View v = inflater.inflate(R.layout.fm_home, container, false);
//
//        ScrollView sv = (ScrollView) v.findViewById(R.id.scrollView);
//        sv.smoothScrollTo(0, 0);
//
//        mConvenientBanner = (ConvenientBanner) v.findViewById(R.id.convenientBanner);
//        LinearLayout.LayoutParams rlp = (LinearLayout.LayoutParams) mConvenientBanner.getLayoutParams();
//        rlp.width = mScreenWidth;
//        rlp.height = (int) (mScreenWidth * (360f / 640f));
//        mConvenientBanner.setLayoutParams(rlp);
//
//        RelativeLayout gridViewRl = (RelativeLayout) v.findViewById(R.id.gridViewRl);
//        rlp = (LinearLayout.LayoutParams) gridViewRl.getLayoutParams();
//        rlp.height = (int) (mScreenWidth / 4f * 2f + 30);
//        gridViewRl.setLayoutParams(rlp);
//
//        mItemGridView = (GridView) v.findViewById(R.id.itemGridView);
//        mItemGridView.setOnItemClickListener(this);
//        mItemAdapter = new ItemAdapter();
//        mItemGridView.setAdapter(mItemAdapter);
//
//        mProGridView = (GridView) v.findViewById(R.id.priductGridView);
//        mProGridView.setOnItemClickListener(this);
//        mProAdapter = new ProAdapter();
//        mProGridView.setAdapter(mProAdapter);
//
//        mTypeTv = (TextView) v.findViewById(R.id.typeTv);
//        mSpaceTv = (TextView) v.findViewById(R.id.spaceTv);
//        mStyleTv = (TextView) v.findViewById(R.id.styleTv);
//        mTypeTv.setOnClickListener(this);
//        mSpaceTv.setOnClickListener(this);
//        mStyleTv.setOnClickListener(this);
//        mLineIv = (ImageView) v.findViewById(R.id.lineIv);
//        rlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        rlp.width = (int) (mScreenWidth / 3f);
//        mLineIv.setLayoutParams(rlp);
//
//        mPullToRefreshLayout = ((PullToRefreshLayout) v.findViewById(R.id.refresh_view));
//        mPullToRefreshLayout.setOnRefreshListener(this);
//        return v;
//    }
//}
package cc.bocang.bocang.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import astuetz.MyPagerSlidingTabStrip;
import cc.bocang.bocang.R;
import cc.bocang.bocang.adapter.BaseAdapterHelper;
import cc.bocang.bocang.adapter.QuickAdapter;
import cc.bocang.bocang.bean.All_attr_list;
import cc.bocang.bocang.bean.Attr_list;
import cc.bocang.bocang.bean.BrandBean;
import cc.bocang.bocang.bean.NewsBean;
import cc.bocang.bocang.data.api.HDApiService;
import cc.bocang.bocang.data.api.HDRetrofit;
import cc.bocang.bocang.data.model.Ad;
import cc.bocang.bocang.data.model.Goods;
import cc.bocang.bocang.data.model.GoodsAllAttr;
import cc.bocang.bocang.data.model.GoodsAttr;
import cc.bocang.bocang.data.parser.ParseGetAdResp;
import cc.bocang.bocang.data.parser.ParseGetGoodsListResp;
import cc.bocang.bocang.data.response.GetAdResp;
import cc.bocang.bocang.data.response.GetGoodsListResp;
import cc.bocang.bocang.global.Constant;
import cc.bocang.bocang.net.OkHttpUtils;
import cc.bocang.bocang.utils.ConvertUtil;
import cc.bocang.bocang.utils.LogUtils;
import cc.bocang.bocang.utils.ResUtil;
import cc.bocang.bocang.utils.StringUtil;
import cc.bocang.bocang.utils.UIUtils;
import cc.bocang.bocang.utils.UniversalUtil;
import cc.bocang.bocang.utils.net.HttpListener;
import cc.bocang.bocang.utils.net.Network;
import com.bigkoo.convenientbanner.CBPageAdapter;
import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lib.common.hxp.view.PullToRefreshLayout;
import com.lib.common.hxp.view.StatedFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.ResponseBody;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class FmHome extends StatedFragment implements View.OnClickListener, AdapterView.OnItemClickListener, PullToRefreshLayout.OnRefreshListener {
    private final String TAG = FmHome.class.getSimpleName();

    private List<All_attr_list> all_attr_lists;

    private HDApiService apiService;

    private Map<String, Bitmap> articleBitmap;

    private BrandBean brandBeans;

    private int curStr;

    private int current;

    private int currentGoodsFilter;

    private TextView et_search;

    private String filter_attr = "";

    private List<Goods> goodsList;

    private QuickAdapter<Goods> goodsQuickAdapter;

    private List<Goods> goodses;

    private GridView gv_home_bottom;

    private List<Integer> imageResList;

    private ImageView iv_msg;

    private ImageView iv_scale;

    private ImageView iv_switcher;

    private LinearLayout ll_brand;

    private List<NewsBean> mArticlesBeans;

    private TextView mCheckedTv;

    private ConvenientBanner mConvenientBanner;

    private float mCurrentCheckedRadioLeft;

//    private ItemAdapter mItemAdapter;

    private GridView mItemGridView;

    private ImageView mLineIv;

    private Network mNetwork;

    private int mNewsPoistion = 0;

    private ProAdapter mProAdapter;

    private GridView mProGridView;

    private PullToRefreshLayout mPullToRefreshLayout;

    private int mScreenWidth;

    private TextView mSpaceTv;

    private TextView mStyleTv;

    private TextView mTypeTv;

    private List<String> nameList;

    private int page = 1;

    private PagerAdapter pagerAdapter;

    private List<Integer> spaceGoodsIdList = new ArrayList();

    private List<String> spaceList = new ArrayList();

    private List<Integer> spaceResList = new ArrayList();

    private List<Integer> styleGoodsIdList = new ArrayList();

    private List<String> styleList = new ArrayList();

    private List<Integer> styleResList = new ArrayList();

    private String[] tabArray;

    private MyPagerSlidingTabStrip tabStrip;

    private TextSwitcher textSwitcher_title;

    private List<Integer> typeGoodsIdList = new ArrayList();

    private List<String> typeList = new ArrayList();

    private List<Integer> typeResList = new ArrayList();

    private ViewPager viewPager;
    private List<GoodsAttr> goodsAttrs;
    private int count=0;
    private int m;

    private void callAd(HDApiService paramHDApiService) { paramHDApiService.getAd(3).enqueue(new Callback<ResponseBody>() {
        public void onFailure(Throwable param1Throwable) {
            if (FmHome.this.getActivity() == null || FmHome.this.getActivity().isFinishing());
        }

        public void onResponse(Response<ResponseBody> param1Response, Retrofit param1Retrofit) {
            if (FmHome.this.getActivity() != null && !FmHome.this.getActivity().isFinishing())
                try {
                    String str = ((ResponseBody)param1Response.body()).string();
//                    Log.i(FmHome.this.TAG, "���������" + str);
                    GetAdResp getAdResp = ParseGetAdResp.parse(str);
                    if (getAdResp != null && getAdResp.isSuccess()) {
                        List list2 = getAdResp.getBeans();
                        ArrayList arrayList = new ArrayList();
                        Iterator iterator = list2.iterator();
                        while (iterator.hasNext())
                            arrayList.add(((Ad)iterator.next()).getPath());
                        List list1 = StringUtil.preToStringArray("http://yangguang.bocang.cc/App/yangguang/Public/uploads/ad/", arrayList);
                        FmHome.this.mConvenientBanner.setPages(new CBViewHolderCreator<FmHome.NetworkImageHolderView>() {
                            public FmHome.NetworkImageHolderView createHolder() { return new FmHome.NetworkImageHolderView(); }
                        },  list1);
                        return;
                    }
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
        }
    }); }

    private void callGoodsList(HDApiService paramHDApiService, String paramString1, final int page, String paramString2, String paramString3, String paramString4) {
        this.typeGoodsIdList.clear();
        this.spaceGoodsIdList.clear();
        this.styleGoodsIdList.clear();
        this.mNetwork.sendGoodsList(paramString1, page, "1", paramString2, paramString3, paramString4, getActivity(), new HttpListener() {
            public void onFailureListener(int param1Int, String param1String) {
                if (FmHome.this.getActivity() != null && !FmHome.this.getActivity().isFinishing()) {
//                    FmHome.access$710(FmHome.this);
                    if (FmHome.this.mPullToRefreshLayout != null) {
                        FmHome.this.mPullToRefreshLayout.refreshFinish(0);
                        FmHome.this.mPullToRefreshLayout.loadmoreFinish(0);
                        return;
                    }
                }
            }

            public void onSuccessListener(int param1Intsss, String param1String) {
                if (FmHome.this.getActivity() != null && !FmHome.this.getActivity().isFinishing()) {
                    if (FmHome.this.mPullToRefreshLayout != null) {
                        FmHome.this.mPullToRefreshLayout.refreshFinish(0);
                        FmHome.this.mPullToRefreshLayout.loadmoreFinish(0);
                    }
                    try {
                        Log.i(FmHome.this.TAG, param1String);
                        GetGoodsListResp getGoodsListResp = ParseGetGoodsListResp.parse(param1String);
                        if (getGoodsListResp != null && getGoodsListResp.isSuccess()) {
                            List list = getGoodsListResp.getGoodsAllAttrs();
                            for (int param1Int = 0;param1Int<list.size(); param1Int++) {
                                    GoodsAllAttr list1 = (GoodsAllAttr)list.get(param1Int);
                                    if (param1Int == 0) {
                                        String str = list1.getAttrName();
                                        FmHome.this.mTypeTv.setText(str);
                                        goodsAttrs = list1.getGoodsAttrs();
                                        for (int b = 0; b < goodsAttrs.size(); b++) {
                                            if(b!=0){
                                                GoodsAttr goodsAttr = (GoodsAttr) goodsAttrs.get(b);
                                                FmHome.this.typeGoodsIdList.add(Integer.valueOf(goodsAttr.getGoods_id()));
                                            }
                                        }
                                    } else if (param1Int == 1) {
                                        String str = list1.getAttrName();
                                        FmHome.this.mSpaceTv.setText(str);
                                        goodsAttrs = list1.getGoodsAttrs();
                                        for (int  b = 0; b < goodsAttrs.size(); b++) {
                                            if (b!=0) {
                                                GoodsAttr goodsAttr = (GoodsAttr)goodsAttrs.get(b);
                                                FmHome.this.spaceGoodsIdList.add(Integer.valueOf(goodsAttr.getGoods_id()));
                                            }
                                        }
                                    } else if (param1Int == 2) {
                                        String str = list1.getAttrName();
                                        FmHome.this.mStyleTv.setText(str);
                                        goodsAttrs = list1.getGoodsAttrs();
                                        for (int  b = 0; b < goodsAttrs.size(); b++) {
                                            if (b!=0) {
                                                GoodsAttr goodsAttr = (GoodsAttr)goodsAttrs.get(b);
                                                FmHome.this.styleGoodsIdList.add(Integer.valueOf(goodsAttr.getGoods_id()));
                                            }
                                        }
                                    }
                                }
                                    List list1 = getGoodsListResp.getGoodses();
                                    if (1 == page) {
                                        goodses=list1;
//                                        FmHome.access$2702(FmHome.this, list1);
                                    } else if (FmHome.this.goodses != null) {
                                        FmHome.this.goodses.addAll(list1);
                                        if (list1.isEmpty())
                                            Toast.makeText(FmHome.this.getActivity(), getActivity().getString(R.string.no_more), Toast.LENGTH_SHORT).show();
                                    }
                                    FmHome.this.mProAdapter.notifyDataSetChanged();
                                    View view = FmHome.this.mProAdapter.getView(0, null, FmHome.this.mProGridView);
                                    view.measure(0, 0);
                                    int param1Int = UIUtils.dip2PX(100);
                                    int i = view.getMeasuredHeight();
                                    FmHome.this.mProGridView.setLayoutParams(new LinearLayout.LayoutParams(-1, param1Int + i * 4));
                                    return;
                                }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void getBrandList() { OkHttpUtils.getBrandList(new okhttp3.Callback() {
        @Override
        public void onFailure(okhttp3.Call call, IOException e) {

        }

        @Override
        public void onResponse(okhttp3.Call call, okhttp3.Response param1Response) throws IOException {
            brandBeans=(new Gson()).fromJson(param1Response.body().string(), BrandBean.class);
            if (FmHome.this.brandBeans != null && FmHome.this.brandBeans.getData() != null && FmHome.this.brandBeans.getData().size() > 0)
                FmHome.this.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        setBrandView();
                    }
                });
        }

    }); }

    private float getCurrentCheckedRadioLeft(View paramView) {
        if (paramView != this.mTypeTv) {
            if (paramView == this.mSpaceTv)
                return this.mScreenWidth / 3.0F;
            if (paramView == this.mStyleTv)
                return this.mScreenWidth * 2.0F / 3.0F;
        }
        return 0.0F;
    }

    private void getGoodsList() { OkHttpUtils.getGoodsList("0", this.page, "", "", this.filter_attr, new okhttp3.Callback() {
        public void onFailure(okhttp3.Call param1Call, IOException param1IOException) {}

        public void onResponse(okhttp3.Call param1Call, okhttp3.Response param1Response) throws IOException {
            try {
                FmHome.this.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        if (FmHome.this.mPullToRefreshLayout != null) {
                            FmHome.this.mPullToRefreshLayout.refreshFinish(0);
                            FmHome.this.mPullToRefreshLayout.loadmoreFinish(0);
                        }
                    }
                });
                List list = (List)(new Gson()).fromJson((new JSONObject(param1Response.body().string())).getJSONArray(Constant.goodslist).toString(), (new TypeToken<List<Goods>>() {

                }).getType());
                if (list != null) {
                    if (list.size() == 0)
                        return;
                    FmHome.this.goodsList.addAll(list);
                    FmHome.this.getActivity().runOnUiThread(new Runnable() {
                        public void run() { FmHome.this.goodsQuickAdapter.replaceAll(FmHome.this.goodsList); }
                    });
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }); }

    private void getNews() { OkHttpUtils.getNews(new okhttp3.Callback() {
        public void onFailure(okhttp3.Call param1Call, IOException param1IOException) {}

        public void onResponse(okhttp3.Call param1Call, okhttp3.Response param1Response) throws IOException {
            mArticlesBeans=(new Gson()).fromJson(param1Response.body().string(), (new TypeToken<List<NewsBean>>() {}).getType());
            if (FmHome.this.mArticlesBeans != null && FmHome.this.mArticlesBeans.size() > 0) {

//                FmHome.access$1402(FmHome.this, new HashMap());
                articleBitmap=new HashMap<>();
                for (int b = 0; b < FmHome.this.mArticlesBeans.size(); b++) {
                    ImageLoader.getInstance().loadImage("http://yangguang.bocang.cc/App/yangguang/Public/uploads/" + ((NewsBean)FmHome.this.mArticlesBeans.get(b)).getPath(), new ImageLoadingListener() {
                        public void onLoadingCancelled(String param2String, View param2View) {}

                        public void onLoadingComplete(String param2String, View param2View, Bitmap param2Bitmap) {
                            LogUtils.logE("s", param2String);
                            FmHome.this.articleBitmap.put(param2String, param2Bitmap);
                            count++;
                            if (count == FmHome.this.mArticlesBeans.size())
                            FmHome.this.getActivity().runOnUiThread(new Runnable() {
                                public void run() { FmHome.this.setNews(); }
                            });
                        }

                        public void onLoadingFailed(String param2String, View param2View, FailReason param2FailReason) {}

                        public void onLoadingStarted(String param2String, View param2View) {}
                    });
                }
            }
        }
    }); }

    private void getType() { OkHttpUtils.getGoodsAttr("0", 1, "", "", "", new okhttp3.Callback() {
        public void onFailure(okhttp3.Call param1Call, IOException param1IOException) {}

        public void onResponse(okhttp3.Call param1Call, okhttp3.Response param1Response) throws IOException {
            try {
                JSONObject jSONObject = new JSONObject(param1Response.body().string());
                LogUtils.logE("goods", jSONObject.toString());
                all_attr_lists=(new Gson()).fromJson(jSONObject.getJSONArray(Constant.all_attr_list).toString(), (new TypeToken<List<All_attr_list>>() {
                }).getType());
                goodsList=(new Gson()).fromJson(jSONObject.getJSONArray(Constant.goodslist).toString(), (new TypeToken<List<Goods>>() {
                }).getType());
                if (FmHome.this.all_attr_lists != null && FmHome.this.all_attr_lists.size() > 0)
                    for (int b = 0;; b++) {
                        if (b < FmHome.this.all_attr_lists.size()) {
                            if (((All_attr_list)FmHome.this.all_attr_lists.get(b)).getFilter_attr_name().contains("空间")) {
                                tabArray=new String[all_attr_lists.get(b).getAttr_list().size()];
//                                FmHome.access$202(FmHome.this, new String[((All_attr_list)FmHome.this.all_attr_lists.get(b)).getAttr_list().size()]);
//                                FmHome.access$302(FmHome.this, b);
                                for (int b1 = 0; b1 < ((All_attr_list)FmHome.this.all_attr_lists.get(b)).getAttr_list().size(); b1++)
                                    FmHome.this.tabArray[b1] = ((Attr_list)((All_attr_list)FmHome.this.all_attr_lists.get(b)).getAttr_list().get(b1)).getAttr_value();
                                FmHome.this.getActivity().runOnUiThread(new Runnable() {
                                    public void run() { FmHome.this.initTab(); }
                                });
                                return;
                            }
                        } else {
                            return;
                        }
                    }
                return;
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
        }
    }); }

    private void initList() {
        this.typeList.clear();
        this.spaceList.clear();
        this.styleList.clear();
        this.typeResList.clear();
        this.spaceResList.clear();
        this.styleResList.clear();
        this.typeList.add("吊灯");
        this.typeList.add("吸顶灯");
        this.typeList.add("壁灯");
        this.typeList.add("台灯");
        this.typeList.add("落地灯");
        this.typeList.add("镜前灯");
        this.typeList.add("烛台灯");
        this.typeList.add("更多");
        this.spaceList.add("客厅");
        this.spaceList.add("餐厅");
        this.spaceList.add("卧室");
        this.spaceList.add("书房");
        this.spaceList.add("卫生间");
        this.spaceList.add("阳台");
        this.spaceList.add("玄关/过道");
        this.spaceList.add("更多");
        this.styleList.add("现代简约");
        this.styleList.add("中式");
        this.styleList.add("新中式");
        this.styleList.add("欧式");
        this.styleList.add("美式");
        this.styleList.add("田园");
        this.styleList.add("新古典");
        this.styleList.add("更多");
        for (byte b = 0; b < 8; b++) {
            if (b == 7) {
                this.typeResList.add(Integer.valueOf(ResUtil.getResMipmap(getActivity(), "type_" + b)));
                this.spaceResList.add(Integer.valueOf(ResUtil.getResMipmap(getActivity(), "type_" + b)));
                this.styleResList.add(Integer.valueOf(ResUtil.getResMipmap(getActivity(), "type_" + b)));
            } else {
                this.typeResList.add(Integer.valueOf(ResUtil.getResMipmap(getActivity(), "type_" + b)));
                this.spaceResList.add(Integer.valueOf(ResUtil.getResMipmap(getActivity(), "space_" + b)));
                this.styleResList.add(Integer.valueOf(ResUtil.getResMipmap(getActivity(), "style_" + b)));
            }
        }
        this.nameList = this.typeList;
        this.imageResList = this.typeResList;
//        this.mItemAdapter.notifyDataSetChanged();
    }

    private void initTab() {
        this.pagerAdapter = new PagerAdapter() {
            public void destroyItem(@NonNull ViewGroup param1ViewGroup, int param1Int, @NonNull Object param1Object) { param1ViewGroup.removeView((View)param1Object); }

            public int getCount() { return FmHome.this.tabArray.length; }

            @Nullable
            public CharSequence getPageTitle(int param1Int) { return FmHome.this.tabArray[param1Int]; }

            @NonNull
            public Object instantiateItem(@NonNull ViewGroup param1ViewGroup, int param1Int) { return new ListView(FmHome.this.getActivity()); }

            public boolean isViewFromObject(@NonNull View param1View, @NonNull Object param1Object) { return (param1View == param1Object); }
        };
        this.viewPager.setAdapter(this.pagerAdapter);
        this.tabStrip.setViewPager(this.viewPager);
        this.tabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int param1Int) {}

            public void onPageScrolled(int param1Int1, float param1Float, int param1Int2) {
//                FmHome.access$502(FmHome.this, param1Int1);
                }

            public void onPageSelected(int param1Int) {
//                FmHome.access$502(FmHome.this, param1Int);
                if (FmHome.this.tabArray != null && param1Int < FmHome.this.tabArray.length) {
//                    FmHome.access$602(FmHome.this, "");
                    filter_attr="";
                    for (int b = 0; b < FmHome.this.all_attr_lists.size(); b++) {
                        if (b == FmHome.this.currentGoodsFilter) {
                            filter_attr+=((Attr_list)((All_attr_list)FmHome.this.all_attr_lists.get(b)).getAttr_list().get(param1Int)).getGoods_id();
//                            FmHome.access$602(FmHome.this, FmHome.this.filter_attr + ((Attr_list)((All_attr_list)FmHome.this.all_attr_lists.get(b)).getAttr_list().get(param1Int)).getGoods_id());
                        } else {
                            filter_attr+="0";
//                            FmHome.access$602(FmHome.this, FmHome.this.filter_attr + "0");
                        }
                        filter_attr+=".";
//                        FmHome.access$602(FmHome.this, FmHome.this.filter_attr + ".");
                    }
                    filter_attr=filter_attr.substring(0, FmHome.this.filter_attr.length() - 1);
                    LogUtils.logE("filter",filter_attr);
//                    FmHome.access$602(FmHome.this, FmHome.this.filter_attr.substring(0, FmHome.this.filter_attr.length() - 1));
                    page=1;
                    goodsList=new ArrayList<>();
                    FmHome.this.getGoodsList();
                }
            }
        });
        this.viewPager.setCurrentItem(this.current);
        this.goodsQuickAdapter.replaceAll(this.goodsList);
        this.goodsQuickAdapter.notifyDataSetChanged();
    }

    private View initView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup) {
        this.mScreenWidth = (getResources().getDisplayMetrics()).widthPixels - UIUtils.dip2PX(30);
        View view1 = paramLayoutInflater.inflate(R.layout.fm_home, paramViewGroup, false);
        ((ScrollView)view1.findViewById(R.id.scrollView)).smoothScrollTo(0, 0);
        this.mConvenientBanner = (ConvenientBanner)view1.findViewById(R.id.convenientBanner);
        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams)this.mConvenientBanner.getLayoutParams();
        layoutParams1.width = this.mScreenWidth;
        layoutParams1.height = (int)(this.mScreenWidth * 0.5625F);
        this.mConvenientBanner.setLayoutParams(layoutParams1);
        RelativeLayout relativeLayout = (RelativeLayout)view1.findViewById(R.id.gridViewRl);
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams)relativeLayout.getLayoutParams();
        layoutParams2.height = (int)(this.mScreenWidth / 4.0F * 2.0F + 30.0F);
        relativeLayout.setLayoutParams(layoutParams2);
        this.mItemGridView = (GridView)view1.findViewById(R.id.itemGridView);
        this.mItemGridView.setOnItemClickListener(this);
//        this.mItemAdapter = new ItemAdapter();
//        this.mItemGridView.setAdapter(this.mItemAdapter);
        this.mProGridView = (GridView)view1.findViewById(R.id.priductGridView);
        this.mProGridView.setOnItemClickListener(this);
        this.mProAdapter = new ProAdapter();
        this.mProGridView.setAdapter(this.mProAdapter);
        this.mTypeTv = (TextView)view1.findViewById(R.id.typeTv);
        this.mSpaceTv = (TextView)view1.findViewById(R.id.spaceTv);
        this.mStyleTv = (TextView)view1.findViewById(R.id.styleTv);
        this.mTypeTv.setOnClickListener(this);
        this.mSpaceTv.setOnClickListener(this);
        this.mStyleTv.setOnClickListener(this);
        this.mLineIv = (ImageView)view1.findViewById(R.id.lineIv);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.mLineIv.getLayoutParams();
        layoutParams.width = (int)(this.mScreenWidth / 3.0F);
        this.mLineIv.setLayoutParams(layoutParams);
        this.mPullToRefreshLayout = (PullToRefreshLayout)view1.findViewById(R.id.refresh_view);
        this.mPullToRefreshLayout.setOnRefreshListener(this);
        this.et_search = (TextView)view1.findViewById(R.id.et_search);
        this.iv_scale = (ImageView)view1.findViewById(R.id.iv_scale);
        this.iv_msg = (ImageView)view1.findViewById(R.id.iv_msg);
        textSwitcher_title = view1.findViewById(R.id.textSwitcher_title);
        this.ll_brand = (LinearLayout)view1.findViewById(R.id.ll_brand);
        this.iv_switcher = (ImageView)view1.findViewById(R.id.iv_switcher);
        this.tabStrip = (MyPagerSlidingTabStrip)view1.findViewById(R.id.tabstrip);
        this.tabStrip.selectColor = getResources().getColor(R.color.theme_red);
        this.tabStrip.defaultColor = getResources().getColor(R.color.tv_333333);
        this.tabStrip.setDividerColor(0);
        this.tabStrip.setIndicatorColor(getResources().getColor(R.color.theme_red));
        this.tabStrip.setIndicatorHeight(UIUtils.dip2PX(1));
        this.tabStrip.setUnderlineColor(getResources().getColor(R.color.theme_red));
        this.tabStrip.setUnderlineHeight(0);
        this.viewPager = (ViewPager)view1.findViewById(R.id.viewPager);
        this.gv_home_bottom = (GridView)view1.findViewById(R.id.gv_home_bottom);
        View view2 = view1.findViewById(R.id.ll_programme);
        View view3 = view1.findViewById(R.id.ll_brand_list);
        View view4 = view1.findViewById(R.id.ll_news);
        View view5 = view1.findViewById(R.id.ll_diy);
        view2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) { FmHome.this.startActivity(new Intent(FmHome.this.getActivity(), ProgrammeActivity.class)); }
        });
        view3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) { FmHome.this.startActivity(new Intent(FmHome.this.getActivity(), BrandListActivity.class)); }
        });
        view4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) { FmHome.this.startActivity(new Intent(FmHome.this.getActivity(), NewsListActivity.class)); }
        });
        view5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) { FmHome.this.startActivity(new Intent(FmHome.this.getActivity(), DiyActivity.class)); }
        });
        this.goodsQuickAdapter = new QuickAdapter<Goods>(getActivity(), R.layout.item_goods) {
            protected void convert(BaseAdapterHelper param1BaseAdapterHelper, Goods param1Goods) {
                param1BaseAdapterHelper.setText(R.id.tv_name, param1Goods.getName());
                param1BaseAdapterHelper.setText(R.id.tv_price, "￥" + param1Goods.getShop_price());
                ImageView imageView = (ImageView)param1BaseAdapterHelper.getView(R.id.iv_img);
                ImageLoader.getInstance().displayImage("http://yangguang.bocang.cc/App/yangguang/Public/uploads/goods/" + param1Goods.getImg_url(), imageView);
            }
        };
        this.gv_home_bottom.setAdapter(this.goodsQuickAdapter);
        this.gv_home_bottom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
                Intent intent = new Intent(FmHome.this.getActivity(), ProDetailActivity.class);
                intent.putExtra("id", ((Goods)FmHome.this.goodsList.get(param1Int)).getId());
                intent.putExtra("imgurl", ((Goods)FmHome.this.goodsList.get(param1Int)).getImg_url());
                intent.putExtra("goodsname", ((Goods)FmHome.this.goodsList.get(param1Int)).getName());
                FmHome.this.startActivity(intent);
            }
        });
        this.iv_scale.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                Intent intent = new Intent();
                intent.setClass(FmHome.this.getActivity(), SimpleScannerActivity.class);
                FmHome.this.startActivity(intent);
            }
        });
        this.et_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                Intent intent = new Intent(FmHome.this.getActivity(), SearchActivity.class);
                intent.putExtra("title", "产品搜索");
                intent.putExtra("okcat_id", 0);
                intent.putExtra("fm", FmCollect.class.getSimpleName());
                FmHome.this.startActivity(intent);
            }
        });
        return view1;
    }

    private void reSetTextColor() {
        this.mTypeTv.setTextColor(-7829368);
        this.mSpaceTv.setTextColor(-7829368);
        this.mStyleTv.setTextColor(-7829368);
    }

    private void setBrandView() {
        this.ll_brand.removeAllViews();
        for ( int finalI = 0; finalI < this.brandBeans.getData().size(); finalI++) {
            View view = View.inflate(getActivity(), R.layout.view_brand, null);
            TextView textView = (TextView)view.findViewById(R.id.tv_name);
            ImageView imageView = (ImageView)view.findViewById(R.id.iv_img);
            textView.setText(((BrandBean.Data)this.brandBeans.getData().get(finalI)).getName());
            ImageLoader.getInstance().displayImage("http://yangguang.bocang.cc/App/yangguang/Public/uploads/" + ((BrandBean.Data)this.brandBeans.getData().get(finalI)).getPath(), imageView);
            final int finalI1 = finalI;
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View param1View) {
                    Intent intent = new Intent(FmHome.this.getActivity(), BrandListActivity.class);
                    intent.putExtra(Constant.title, ((BrandBean.Data)FmHome.this.brandBeans.getData().get(finalI1)).getName());
                    intent.putExtra(Constant.id, ((BrandBean.Data)FmHome.this.brandBeans.getData().get(finalI1)).getId());
                    FmHome.this.startActivity(intent);
                }
            });
            this.ll_brand.addView(view);
        }
    }

    private void setNews() {
        try {
            textSwitcher_title.setFactory(new ViewSwitcher.ViewFactory() {
                public View makeView() {
                    TextView textView = new TextView(getActivity());
                    textView.setTextSize(2, 16.0F);
                    textView.setGravity(16);
                    textView.setCompoundDrawablePadding(UIUtils.dip2PX(10));
                    textView.setLines(1);
                    textView.setEllipsize(TextUtils.TruncateAt.END);
                    textView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View param2View) {
                            Intent intent = new Intent(FmHome.this.getActivity(), NewsDetailActivity.class);
                            intent.putExtra(Constant.id, ((NewsBean)FmHome.this.mArticlesBeans.get(FmHome.this.mNewsPoistion)).getId());
                            intent.putExtra(Constant.title, ((NewsBean)FmHome.this.mArticlesBeans.get(FmHome.this.mNewsPoistion)).getName());
                            FmHome.this.startActivity(intent);
                        }
                    });
                    return textView;
                }
            });
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
//                    LogUtils.logE("curStr", FmHome.this.curStr + "," + FmHome.this.mArticlesBeans.size() + "");
//                    FmHome.access$1602(FmHome.this, FmHome.access$1708(FmHome.this) % FmHome.this.mArticlesBeans.size());
                    synchronized (textSwitcher_title){
//                        mNewsPoistion++;
//                        if(mNewsPoistion>=mArticlesBeans.size()){
//                            mNewsPoistion=0;
//                        }
                        if(((IndexActivity)getActivity()).mViewPager.getCurrentItem()!=0){
                            return;
                        }
                        mNewsPoistion = curStr++ % mArticlesBeans.size();
                        if (textSwitcher_title != null && mArticlesBeans != null && FmHome.this.mArticlesBeans.size() > FmHome.this.mNewsPoistion && FmHome.this.mArticlesBeans.get(FmHome.this.mNewsPoistion) != null){
                            textSwitcher_title.setText(mArticlesBeans.get(mNewsPoistion).getName());
                            FmHome.this.iv_switcher.setImageBitmap((Bitmap)FmHome.this.articleBitmap.get("http://yangguang.bocang.cc/App/yangguang/Public/uploads/" + ((NewsBean)FmHome.this.mArticlesBeans.get(FmHome.this.mNewsPoistion)).getPath()));
                        }
                        handler.postDelayed(this, 5000L);
                    }

                }
            },1000L);
        } catch (Exception exception) {

        }
    }

    public void onClick(View paramView) {
//        AnimationSet animationSet = new AnimationSet(true);
//        TranslateAnimation translateAnimation = null;
//        reSetTextColor();
//        if (paramView == this.mTypeTv) {
//            this.mCheckedTv = this.mTypeTv;
//            this.mTypeTv.setTextColor(getResources().getColor(2131492893));
//            translateAnimation = new TranslateAnimation(this.mCurrentCheckedRadioLeft, 0.0F, -20.0F, -20.0F);
//            this.nameList = this.typeList;
//            this.imageResList = this.typeResList;
//            this.mItemAdapter.notifyDataSetChanged();
//        } else if (paramView == this.mSpaceTv) {
//            this.mCheckedTv = this.mSpaceTv;
//            this.mSpaceTv.setTextColor(getResources().getColor(2131492893));
//            translateAnimation = new TranslateAnimation(this.mCurrentCheckedRadioLeft, this.mScreenWidth / 3.0F, -20.0F, -20.0F);
//            this.nameList = this.spaceList;
//            this.imageResList = this.spaceResList;
//            this.mItemAdapter.notifyDataSetChanged();
//        } else if (paramView == this.mStyleTv) {
//            this.mCheckedTv = this.mStyleTv;
//            this.mStyleTv.setTextColor(getResources().getColor(2131492893));
//            translateAnimation = new TranslateAnimation(this.mCurrentCheckedRadioLeft, this.mScreenWidth * 2.0F / 3.0F, -20.0F, -20.0F);
//            this.nameList = this.styleList;
//            this.imageResList = this.styleResList;
//            this.mItemAdapter.notifyDataSetChanged();
//        }
//        animationSet.addAnimation(translateAnimation);
//        animationSet.setFillBefore(false);
//        animationSet.setFillAfter(true);
//        animationSet.setDuration(100L);
//        this.mLineIv.startAnimation(animationSet);
//        this.mCurrentCheckedRadioLeft = getCurrentCheckedRadioLeft(paramView);
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        if (Constant.isDebug)
            Log.i(this.TAG, "onCreate...");
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        if (Constant.isDebug)
            Log.i(this.TAG, "onCreateView...");
        View view = initView(paramLayoutInflater, paramViewGroup);
        this.mNetwork = new Network();
        initList();
        getNews();
        getBrandList();
        this.apiService = (HDApiService)HDRetrofit.create(HDApiService.class);
        callAd(this.apiService);
        this.page = 1;
        callGoodsList(this.apiService, IndexActivity.mCId, this.page, null, "is_best", null);
        getType();
        this.mTypeTv.performClick();
        return view;
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
        IndexActivity indexActivity;
        int j = 0;
        int k = 0;
        int i = 0;
        if (paramAdapterView == this.mItemGridView) {
//            if (Constant.isDebug)
//                Toast.makeText(getActivity(), ((TextView)paramView.findViewById(2131558621)).getText(), 0).show();
            if (this.mCheckedTv == this.mTypeTv) {
                IndexActivity indexActivity1 = (IndexActivity)getActivity();
                IndexActivity.titlePos = 0;
                indexActivity1 = (IndexActivity)getActivity();
                if (paramInt >= this.typeGoodsIdList.size() - 1 || paramInt == 7) {
                    m = 0;
                } else {
                    m = paramInt + 1;
                }
                IndexActivity.itemPos = m;
                indexActivity1 = (IndexActivity)getActivity();
                int m = i;
                if (paramInt < this.typeGoodsIdList.size() - 1)
                    if (paramInt == 7) {
                        m = i;
                    } else {
                        m = ((Integer)this.typeGoodsIdList.get(paramInt)).intValue();
                    }
                IndexActivity.goodsId = m;
            } else if (this.mCheckedTv == this.mSpaceTv) {
                IndexActivity indexActivity1 = (IndexActivity)getActivity();
                IndexActivity.titlePos = 1;
                indexActivity1 = (IndexActivity)getActivity();
                if (paramInt >= this.spaceGoodsIdList.size() - 1 || paramInt == 7) {
                    m = 0;
                } else {
                    m = paramInt + 1;
                }
                IndexActivity.itemPos = m;
//                indexActivity1 = (IndexActivity)getActivity();
                int m = j;
                if (paramInt < this.spaceGoodsIdList.size() - 1)
                    if (paramInt == 7) {
                        m = j;
                    } else {
                        m = ((Integer)this.spaceGoodsIdList.get(paramInt)).intValue();
                    }
                IndexActivity.goodsId = m;
            } else if (this.mCheckedTv == this.mStyleTv) {
                IndexActivity indexActivity1 = (IndexActivity)getActivity();
                IndexActivity.titlePos = 2;
                indexActivity1 = (IndexActivity)getActivity();
                if (paramInt >= this.styleGoodsIdList.size() - 1 || paramInt == 7) {
                    m = 0;
                } else {
                    m = paramInt + 1;
                }
                IndexActivity.itemPos = m;
                indexActivity1 = (IndexActivity)getActivity();
                int m = k;
                if (paramInt < this.styleGoodsIdList.size() - 1)
                    if (paramInt == 7) {
                        m = k;
                    } else {
                        m = ((Integer)this.styleGoodsIdList.get(paramInt)).intValue();
                    }
                IndexActivity.goodsId = m;
            }
            indexActivity = (IndexActivity)getActivity();
            IndexActivity.isClickFmHomeItem = true;
            ((IndexActivity)getActivity()).mFragmentPagerAdapter.notifyDataSetChanged();
            ((IndexActivity)getActivity()).mViewPager.setCurrentItem(1, true);
            return;
        }
        if (paramAdapterView == this.mProGridView) {
//            if (Constant.isDebug)
//                Toast.makeText(getActivity(), "���������������" + paramInt, 0).show();
            Intent intent = new Intent(getActivity(), ProDetailActivity.class);
            intent.putExtra("id", ((Goods)this.goodses.get(paramInt)).getId());
            intent.putExtra("imgurl", ((Goods)this.goodses.get(paramInt)).getImg_url());
            intent.putExtra("goodsname", ((Goods)this.goodses.get(paramInt)).getName());
            startActivity(intent);
            return;
        }
    }

    public void onLoadMore(PullToRefreshLayout paramPullToRefreshLayout) {
        this.page++;
        getGoodsList();
    }

    public void onPause() {
        super.onPause();
        this.mConvenientBanner.stopTurning();
    }

    public void onRefresh(PullToRefreshLayout paramPullToRefreshLayout) {
        callAd(this.apiService);
        this.page = 1;
        callGoodsList(this.apiService, IndexActivity.mCId, this.page, null, "is_best", null);
    }

    protected void onRestoreState(Bundle paramBundle) { super.onRestoreState(paramBundle); }

    public void onResume() {
        super.onResume();
        this.mConvenientBanner.startTurning(UniversalUtil.randomA2B(3000, 5000));
    }

    protected void onSaveState(Bundle paramBundle) { super.onSaveState(paramBundle); }

//    private class ItemAdapter extends BaseAdapter {
//        private ItemAdapter() {}
//
//        public int getCount() { return (FmHome.this.nameList == null) ? 0 : FmHome.this.nameList.size(); }
//
//        public String getItem(int param1Int) { return (FmHome.this.nameList == null) ? null : (String)FmHome.this.nameList.get(param1Int); }
//
//        public long getItemId(int param1Int) { return param1Int; }
//
//        public View getView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
//            if (param1View == null) {
//                param1View = View.inflate(FmHome.this.getActivity(), 2130968668, null);
//                ViewHolder viewHolder1 = new ViewHolder();
//                viewHolder1.imageView = (ImageView)param1View.findViewById(2131558625);
//                viewHolder1.textView = (TextView)param1View.findViewById(2131558621);
//                param1View.setTag(viewHolder1);
//                viewHolder1.textView.setText((CharSequence)FmHome.this.nameList.get(param1Int));
//                viewHolder1.imageView.setImageResource(((Integer)FmHome.this.imageResList.get(param1Int)).intValue());
//                return param1View;
//            }
//            ViewHolder viewHolder = (ViewHolder)param1View.getTag();
//            viewHolder.textView.setText((CharSequence)FmHome.this.nameList.get(param1Int));
//            viewHolder.imageView.setImageResource(((Integer)FmHome.this.imageResList.get(param1Int)).intValue());
//            return param1View;
//        }
//
//        class ViewHolder {
//            ImageView imageView;
//
//            TextView textView;
//        }
//    }
//
//    class ViewHolder {
//        ImageView imageView;
//
//        TextView textView;
//
//        ViewHolder() {}
//    }

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


    private class ProAdapter extends BaseAdapter {
        private ImageLoader imageLoader = ImageLoader.getInstance();

        private DisplayImageOptions options = (new DisplayImageOptions.Builder())
                .showImageOnLoading(R.mipmap.bg_default)
                .showImageForEmptyUri(R.mipmap.bg_default)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();

        public int getCount() { return (FmHome.this.goodses == null) ? 0 : FmHome.this.goodses.size(); }

        public Goods getItem(int param1Int) { return (FmHome.this.goodses == null) ? null : (Goods)FmHome.this.goodses.get(param1Int); }

        public long getItemId(int param1Int) { return param1Int; }

        public View getView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
            if (param1View == null) {
                param1View = View.inflate(FmHome.this.getActivity(), R.layout.item_goods, null);
                ViewHolder viewHolder1 = new ViewHolder();
                viewHolder1.imageView = (ImageView)param1View.findViewById(R.id.iv_img);
                viewHolder1.textView = (TextView)param1View.findViewById(R.id.tv_name);
                viewHolder1.tv_price = (TextView)param1View.findViewById(R.id.tv_price);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)viewHolder1.imageView.getLayoutParams();
                layoutParams.height = (int)((FmHome.this.mScreenWidth - ConvertUtil.dp2px(FmHome.this.getActivity(), 60.0F)) / 3.0F);
                viewHolder1.imageView.setLayoutParams(layoutParams);
                param1View.setTag(viewHolder1);
                viewHolder1.textView.setText(((Goods)FmHome.this.goodses.get(param1Int)).getName());
                this.imageLoader.displayImage("http://yangguang.bocang.cc/App/yangguang/Public/uploads/goods/" + ((Goods)FmHome.this.goodses.get(param1Int)).getImg_url() + "!400X400.png", viewHolder1.imageView, this.options);
                viewHolder1.tv_price.setText("￥" + ((Goods)FmHome.this.goodses.get(param1Int)).getShop_price());
                return param1View;
            }
            ViewHolder viewHolder = (ViewHolder)param1View.getTag();
            viewHolder.textView.setText(((Goods)FmHome.this.goodses.get(param1Int)).getName());
            this.imageLoader.displayImage("http://yangguang.bocang.cc/App/yangguang/Public/uploads/goods/" + ((Goods)FmHome.this.goodses.get(param1Int)).getImg_url() + "!400X400.png", viewHolder.imageView, this.options);
            viewHolder.tv_price.setText("￥" + ((Goods)FmHome.this.goodses.get(param1Int)).getShop_price());
            return param1View;
        }

        class ViewHolder {
            ImageView imageView;

            TextView textView;

            TextView tv_price;
        }
    }

//    class ViewHolder {
//        ImageView imageView;
//
//        TextView textView;
//
//        TextView tv_price;
//
//        ViewHolder() {}
//    }
}
