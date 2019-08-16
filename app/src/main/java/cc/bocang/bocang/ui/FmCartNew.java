package cc.bocang.bocang.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cc.bocang.bocang.R;
import cc.bocang.bocang.broadcast.Broad;
import cc.bocang.bocang.data.dao.CartDao;
import cc.bocang.bocang.data.model.Goods;
import cc.bocang.bocang.data.model.UserInfo;
import cc.bocang.bocang.global.IntentKeys;
import cc.bocang.bocang.global.MyApplication;
import cc.bocang.bocang.listener.INumberInputListener;
import cc.bocang.bocang.view.NumberInputView;
import com.lib.common.hxp.view.StatedFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yjn.swipelistview.swipelistviewlibrary.widget.SwipeMenu;
import com.yjn.swipelistview.swipelistviewlibrary.widget.SwipeMenuCreator;
import com.yjn.swipelistview.swipelistviewlibrary.widget.SwipeMenuItem;
import com.yjn.swipelistview.swipelistviewlibrary.widget.SwipeMenuListView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FmCartNew extends StatedFragment implements AdapterView.OnItemClickListener {
    private LinearLayout agiolv;

    private List<Integer> buyNum = new ArrayList();

    private List<Goods> goodses;

    private MyAdapter mAdapter;

    private CheckBox mAllCB;

    private boolean mCheckedAll;

    private UserInfo mInfo;

    private SwipeMenuListView mListView;

    private boolean mNotify = true;

    private TextView money_tv;

    private TextView settle_tv;

    private EditText sumAgioTv;

    private int dp2px(int paramInt) { return (int)TypedValue.applyDimension(1, paramInt, getResources().getDisplayMetrics()); }

    private View initView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup) {
        View view = paramLayoutInflater.inflate(R.layout.fm_cart_new, paramViewGroup, false);
        this.goodses = ((IndexActivity)getActivity()).goodses;
        this.mListView = (SwipeMenuListView)view.findViewById(R.id.listView);
        this.money_tv = (TextView)view.findViewById(R.id.money_tv);
        this.sumAgioTv = (EditText)view.findViewById(R.id.sumAgioTv);
        this.settle_tv = (TextView)view.findViewById(R.id.settle_tv);
        this.agiolv = (LinearLayout)view.findViewById(R.id.agiolv);
        this.mAllCB = (CheckBox)view.findViewById(R.id.checkAll);
        this.agiolv.setVisibility(View.VISIBLE);
        this.mAdapter = new MyAdapter();
        this.mListView.setAdapter(this.mAdapter);
        this.mAdapter.getBuyCount();
        for (byte b = 0; b < this.goodses.size(); b++) {
            ((Goods)this.goodses.get(b)).setBuyCount(1);
            ((Goods)this.goodses.get(b)).setAgio(0.0D);
        }
        this.mInfo = ((MyApplication)getActivity().getApplication()).mUserInfo;
        SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
            public void create(SwipeMenu param1SwipeMenu) {
                SwipeMenuItem swipeMenuItem = new SwipeMenuItem(FmCartNew.this.getActivity());
                swipeMenuItem.setBackground(new ColorDrawable(Color.parseColor("#fe3c3a")));
                swipeMenuItem.setWidth(FmCartNew.this.dp2px(80));
                swipeMenuItem.setTitle("删除");
                swipeMenuItem.setTitleColor(-1);
                swipeMenuItem.setTitleSize(20);
                param1SwipeMenu.addMenuItem(swipeMenuItem);
            }
        };
        this.mListView.setMenuCreator(swipeMenuCreator);
        this.mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            public boolean onMenuItemClick(int position1, SwipeMenu param1SwipeMenu, int position2) {
                if (position2 == 0 && -1 != (new CartDao(FmCartNew.this.getActivity())).deleteOne(((Goods)FmCartNew.this.goodses.get(position1)).getId())) {
                    Broad.sendLocalBroadcast(FmCartNew.this.getActivity(), "cart_change_ACTION", null);
                    FmCartNew.this.mAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
        this.sumAgioTv.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable param1Editable) {
                FmCartNew.this.money_tv.setText("¥" + FmCartNew.this.mAdapter.getMoney() + "");
//                Log.v("520it", "������:" + FmCartNew.this.money_tv.getText());
            }

            public void beforeTextChanged(CharSequence param1CharSequence, int position1, int position2, int position3) {}

            public void onTextChanged(CharSequence param1CharSequence, int position1, int position2, int position3) {}
        });
        this.mAllCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
                if (param1Boolean) {
                    mCheckedAll=true;
//                    FmCartNew.access$502(FmCartNew.this, true);
                    FmCartNew.this.mAdapter.notifyDataSetChanged();
                    return;
                }
                mCheckedAll=false;
//                FmCartNew.access$502(FmCartNew.this, false);
                if (FmCartNew.this.mNotify) {
                    FmCartNew.this.mAdapter.notifyDataSetChanged();
                    return;
                }
            }
        });
        this.settle_tv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (FmCartNew.this.mAdapter.getTotalCount() <= 0) {
                    Toast.makeText(FmCartNew.this.getActivity(), "请选择要购买的商品", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(FmCartNew.this.getActivity(), OrderActivity.class);
                intent.putExtra("productOrder", FmCartNew.this.mAdapter.getShopCarChecked());
                intent.putExtra("totalPrice", FmCartNew.this.mAdapter.getMoney());
                intent.putExtra("totalCount", FmCartNew.this.mAdapter.getTotalCount());
                String str = FmCartNew.this.sumAgioTv.getText().toString();
                intent.putExtra(IntentKeys.SHOPORDERDISCOUNT, str);
                FmCartNew.this.startActivity(intent);
            }
        });
        return view;
    }

    public void onCreate(Bundle paramBundle) { super.onCreate(paramBundle); }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        View view = initView(paramLayoutInflater, paramViewGroup);
        EventBus.getDefault().register(this);
        return view;
    }

    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
        if (paramAdapterView == this.mListView) {
            Intent intent = new Intent(getActivity(), ProDetailActivity.class);
            intent.putExtra("id", ((Goods)this.goodses.get(paramInt)).getId());
            startActivity(intent);
        }
    }

    protected void onRestoreState(Bundle paramBundle) { super.onRestoreState(paramBundle); }

    protected void onSaveState(Bundle paramBundle) { super.onSaveState(paramBundle); }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserEvent(Integer paramInteger) {
        if (paramInteger.intValue() == 67)
            Broad.sendLocalBroadcast(getActivity(), "cart_change_ACTION", null);
    }

    private class MyAdapter extends BaseAdapter {
        private ImageLoader imageLoader = ImageLoader.getInstance();

        private DisplayImageOptions options = (new DisplayImageOptions.Builder())
                .showImageOnLoading(R.mipmap.bg_default)
                .showImageForEmptyUri(R.mipmap.bg_default)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();

        private double getMoney() {
            double d2 = 0.0D;
            byte b = 0;
            while (b < FmCartNew.this.goodses.size()) {
                double d = d2;
                if ((goodses.get(b)).delete == true)
                    if (((Goods)FmCartNew.this.goodses.get(b)).getAgio() == 0.0D) {
                        if (!TextUtils.isEmpty(FmCartNew.this.mInfo.getMultiple()) && !TextUtils.isEmpty(FmCartNew.this.mInfo.getMultiple())) {
                            float f = ((Goods)FmCartNew.this.goodses.get(b)).getShop_price();
                            d = d2 + (((Goods)FmCartNew.this.goodses.get(b)).getBuyCount() * f * 1.0F) * Double.parseDouble(FmCartNew.this.mInfo.getMultiple());
                        } else {
                            float f = ((Goods)FmCartNew.this.goodses.get(b)).getShop_price();
                            d = d2 + (((Goods)FmCartNew.this.goodses.get(b)).getBuyCount() * f * 1.0F);
                        }
                    } else if (!TextUtils.isEmpty(FmCartNew.this.mInfo.getMultiple()) && !TextUtils.isEmpty(FmCartNew.this.mInfo.getMultiple())) {
                        Log.v("520it", ((Goods)FmCartNew.this.goodses.get(b)).getAgio() + ":agio");
                        float f = ((Goods)FmCartNew.this.goodses.get(b)).getShop_price();
                        d = d2 + (((Goods)FmCartNew.this.goodses.get(b)).getBuyCount() * f) * ((Goods)FmCartNew.this.goodses.get(b)).getAgio() * Double.parseDouble(FmCartNew.this.mInfo.getMultiple());
                    } else {
                        Log.v("520it", ((Goods)FmCartNew.this.goodses.get(b)).getAgio() + ":agio");
                        float f = ((Goods)FmCartNew.this.goodses.get(b)).getShop_price();
                        d = d2 + (((Goods)FmCartNew.this.goodses.get(b)).getBuyCount() * f) * ((Goods)FmCartNew.this.goodses.get(b)).getAgio();
                    }
                b++;
                d2 = d;
            }
            String str = FmCartNew.this.sumAgioTv.getText().toString();
            if (TextUtils.isEmpty(str)) {
                double d = d2 * 1.0D;
                return (new BigDecimal(d)).setScale(2, 4).doubleValue();
            }
            double d1 = Double.parseDouble(str) * d2 * 0.01D;
            return (new BigDecimal(d1)).setScale(2, 4).doubleValue();
        }

        public void getBuyCount() {
            for (byte b = 0; b < FmCartNew.this.goodses.size(); b++)
                FmCartNew.this.buyNum.add(Integer.valueOf(((Goods)FmCartNew.this.goodses.get(b)).getBuyCount()));
        }

        public int getCount() { return (FmCartNew.this.goodses == null) ? 0 : FmCartNew.this.goodses.size(); }

        public Goods getItem(int position) { return (FmCartNew.this.goodses == null) ? null : (Goods)FmCartNew.this.goodses.get(position); }

        public long getItemId(int position) { return position; }

        public ArrayList<Goods> getShopCarChecked() {
            ArrayList arrayList = new ArrayList();
            for (byte b = 0; b < FmCartNew.this.goodses.size(); b++) {
                if (((Goods)goodses.get(b)).delete == true)
                    arrayList.add(FmCartNew.this.goodses.get(b));
            }
            return arrayList;
        }

        public int getTotalCount() {
            int i = 0;
            byte b = 0;
            while (b < FmCartNew.this.goodses.size()) {
                int j = i;
                if (((Goods)goodses.get(b)).delete == true)
                    j = i + ((Goods)FmCartNew.this.goodses.get(b)).getBuyCount();
                b++;
                i = j;
            }
            return i;
        }

        public View getView(final int position, View param1View, ViewGroup param1ViewGroup) {
            final ViewHolder viewHolder;
            if (param1View == null) {
                param1View = View.inflate(FmCartNew.this.getActivity(), R.layout.item_lv_cart, null);
                viewHolder = new ViewHolder();
                viewHolder.checkBox = (CheckBox)param1View.findViewById(R.id.checkbox);
                viewHolder.imageView = (ImageView)param1View.findViewById(R.id.imageView);
                viewHolder.nameTv = (TextView)param1View.findViewById(R.id.nameTv);
                viewHolder.numTv = (TextView)param1View.findViewById(R.id.numTv);
                viewHolder.priceTv = (TextView)param1View.findViewById(R.id.priceTv);
                viewHolder.InputView = (NumberInputView)param1View.findViewById(R.id.number_input_et);
                viewHolder.agioTv = (EditText)param1View.findViewById(R.id.agioTv);
                viewHolder.agioitemiv = (LinearLayout)param1View.findViewById(R.id.agioitemiv);
                param1View.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder)param1View.getTag();
            }
            viewHolder.agioitemiv.setVisibility(View.VISIBLE);
            viewHolder.InputView.setMax(10000);
            viewHolder.nameTv.setText(((Goods)FmCartNew.this.goodses.get(position)).getName());
                this.imageLoader.displayImage("http://yangguang.bocang.cc/App/yangguang/Public/uploads/goods/" + ((Goods)FmCartNew.this.goodses.get(position)).getImg_url() + "!280X280.png", viewHolder.imageView, this.options);
            if (!TextUtils.isEmpty(FmCartNew.this.mInfo.getMultiple()) && !TextUtils.isEmpty(FmCartNew.this.mInfo.getMultiple())) {
                viewHolder.priceTv.setText("￥" + (((Goods)FmCartNew.this.goodses.get(position)).getShop_price() * Double.parseDouble(FmCartNew.this.mInfo.getMultiple())));
            }else {
                viewHolder.priceTv.setText("￥" + (((Goods)FmCartNew.this.goodses.get(position)).getShop_price() ));
            }
                viewHolder.InputView.setListener(new INumberInputListener() {
                    public void onTextChange(int param2Int) {
                        goodses.get(position).setBuyCount(Integer.parseInt(viewHolder.numTv.getText().toString()));
                        FmCartNew.MyAdapter.this.setBuyCount(position, Integer.parseInt(viewHolder.numTv.getText().toString()));
//                        Log.v("520it", "���������");
                        money_tv.setText("¥" + FmCartNew.MyAdapter.this.getMoney() + "");
//                        Log.v("520it", "���������1:" + FmCartNew.MyAdapter.this.money_tv.getText());
                    }
                });
                viewHolder.checkBox.setChecked(FmCartNew.this.mCheckedAll);
                viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton param2CompoundButton, boolean param2Boolean) {
                        if (param2Boolean) {
                            goodses.get(position).delete = true;
                        } else {
                            mNotify = false;
                            mAllCB.setChecked(false);
                            mNotify = true;
                            goodses.get(position).delete = false;
                        }
                        goodses.get(position).setBuyCount(Integer.parseInt(viewHolder.numTv.getText().toString()));
                        setBuyCount(position, Integer.parseInt(viewHolder.numTv.getText().toString()));
                        money_tv.setText("￥" + FmCartNew.MyAdapter.this.getMoney());
                        if (position == goodses.size() - 1)
                            money_tv.setText("￥" + FmCartNew.MyAdapter.this.getMoney());
                    }
                });
                viewHolder.agioTv.addTextChangedListener(new TextWatcher() {
                    public void afterTextChanged(Editable param2Editable) {
                        if (viewHolder.nameTv.getText().toString().equals(goodses.get(position).getName()) && !TextUtils.isEmpty(viewHolder.agioTv.getText().toString())) {
                            (goodses.get(position)).setAgio(Double.parseDouble(viewHolder.agioTv.getText().toString()) * 0.01D);
                            money_tv.setText("￥" + FmCartNew.MyAdapter.this.getMoney() + "");
//                            Log.v("520it", "���������");
//                            Log.v("520it", "������san:" + FmCartNew.MyAdapter.this.money_tv.getText());
                        }
                    }

                    public void beforeTextChanged(CharSequence param2CharSequence, int param2Int1, int param2Int2, int param2Int3) {}

                    public void onTextChanged(CharSequence param2CharSequence, int param2Int1, int param2Int2, int param2Int3) {}
                });

                return param1View;
        }

        public void setBuyCount(int position1, int position2) { FmCartNew.this.buyNum.set(position1, Integer.valueOf(position2)); }

        class ViewHolder {
            NumberInputView InputView;

            EditText agioTv;

            LinearLayout agioitemiv;

            CheckBox checkBox;

            ImageView imageView;

            TextView nameTv;

            TextView numTv;

            TextView priceTv;
        }
    }

//
//    class ViewHolder {
//        NumberInputView InputView;
//
//        EditText agioTv;
//
//        LinearLayout agioitemiv;
//
//        CheckBox checkBox;
//
//        ImageView imageView;
//
//        TextView nameTv;
//
//        TextView numTv;
//
//        TextView priceTv;
//
//        ViewHolder() {}
//    }
}
