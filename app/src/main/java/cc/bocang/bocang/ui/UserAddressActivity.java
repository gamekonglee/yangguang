package cc.bocang.bocang.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cc.bocang.bocang.R;
import cc.bocang.bocang.data.dao.LogisticDao;
import cc.bocang.bocang.data.model.Logistics;
import cc.bocang.bocang.global.Constant;
import com.lib.common.hxp.view.ListViewForScrollView;
import java.io.Serializable;
import java.util.ArrayList;

public class UserAddressActivity extends BaseActivity implements View.OnClickListener {
    private View btn_add;

    public boolean isSelectLogistics = false;

    private Intent mIntent;

    private LogisticDao mLogisticDao;

    private ArrayList<Logistics> mLogisticList;

    private View mNullNet;

    private TextView mNullNetTv;

    private View mNullView;

    private TextView mNullViewTv;

    private ProAdapter mProAdapter;

    private Button mRefeshBtn;

    private ListViewForScrollView order_sv;

    private int page = 1;

    private View topLeftBtn;

    private void initData() { this.isSelectLogistics = getIntent().getBooleanExtra("isSELECTADDRESS", false); }

    private void initView() {
        this.btn_add = (Button)findViewById(R.id.btn_add);
        this.btn_add.setOnClickListener(this);
        this.order_sv = (ListViewForScrollView)findViewById(R.id.order_sv);
        this.order_sv.setDivider(null);
        this.mProAdapter = new ProAdapter();
        this.order_sv.setAdapter(this.mProAdapter);
        this.order_sv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {}
        });
        this.mNullView = findViewById(R.id.null_view);
        this.mNullNet = findViewById(R.id.null_net);
        this.mRefeshBtn = (Button)this.mNullNet.findViewById(R.id.refesh_btn);
        this.mNullNetTv = (TextView)this.mNullNet.findViewById(R.id.tv);
        this.mNullViewTv = (TextView)this.mNullView.findViewById(R.id.tv);
        this.mLogisticDao = new LogisticDao(this);
        this.topLeftBtn = findViewById(R.id.topLeftBtn);
        this.topLeftBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) { UserAddressActivity.this.finish(); }
        });
    }

    private void initViewData() {}

    public void onClick(View paramView) {
//        switch (paramView.getId()) {
//            default:
//                return;
//            case 2131558659:
//                break;
//        }
        startActivity(new Intent(this, UserAddressAddActivity.class));
    }

    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_user_address);
        initData();
        initView();
        initViewData();
    }

    protected void onStart() {
        super.onStart();
        this.mLogisticList = (ArrayList)this.mLogisticDao.getAll();
        this.mProAdapter.notifyDataSetChanged();
    }

    private class ProAdapter extends BaseAdapter {
        public int getCount() { return (UserAddressActivity.this.mLogisticList == null) ? 0 : UserAddressActivity.this.mLogisticList.size(); }

        public Logistics getItem(int param1Int) { return (UserAddressActivity.this.mLogisticList == null) ? null : (Logistics)UserAddressActivity.this.mLogisticList.get(param1Int); }

        public long getItemId(int param1Int) { return param1Int; }

        public View getView(final int param1Int, View param1View, ViewGroup param1ViewGroup) {
            byte b;
            ViewHolder viewHolder;
            if (param1View == null) {
                param1View = View.inflate(UserAddressActivity.this, R.layout.item_user_address, null);
                viewHolder = new ViewHolder();
                viewHolder.consignee_tv = (TextView)param1View.findViewById(R.id.consignee_title_tv);
                viewHolder.address_tv = (TextView)param1View.findViewById(R.id.address_tv);
                viewHolder.phone_tv = (TextView)param1View.findViewById(R.id.phone_tv);
                viewHolder.default_addr_tv = (TextView)param1View.findViewById(R.id.default_addr_tv);
                viewHolder.delete_bt = (Button)param1View.findViewById(R.id.delete_bt);
                viewHolder.edit_tv = (Button)param1View.findViewById(R.id.edit_tv);
                viewHolder.ll = (LinearLayout)param1View.findViewById(R.id.ll);
                viewHolder.edit_rl = (RelativeLayout)param1View.findViewById(R.id.edit_rl);
                param1View.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder)param1View.getTag();
            }
            Logistics logistics = (Logistics)UserAddressActivity.this.mLogisticList.get(param1Int);
            viewHolder.consignee_tv.setText(logistics.getName());
            viewHolder.address_tv.setText(logistics.getAddress());
            viewHolder.phone_tv.setText(logistics.getTel());
            RelativeLayout relativeLayout = viewHolder.edit_rl;
            if (UserAddressActivity.this.isSelectLogistics == true) {
                b = 8;
            } else {
                b = 0;
            }
            relativeLayout.setVisibility(b);
            viewHolder.delete_bt.setOnClickListener(new View.OnClickListener() {
                public void onClick(View param2View) { (new AlertDialog.Builder(UserAddressActivity.this)).setTitle(null)
                        .setMessage("是否删除该物流地址").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface param3DialogInterface, int param3Int) {
                        mLogisticDao.deleteOne((mLogisticList.get(param1Int)).getId());
                        mLogisticList= (ArrayList<Logistics>) mLogisticDao.getAll();
//                        UserAddressActivity.access$002(UserAddressActivity.ProAdapter.this, (ArrayList)UserAddressActivity.ProAdapter.this.mLogisticDao.getAll());
                        mProAdapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface param3DialogInterface, int param3Int) {}
                }).show(); }
            });
            viewHolder.edit_tv.setOnClickListener(new View.OnClickListener() {
                public void onClick(View param2View) {
//                    UserAddressActivity.access$302(UserAddressActivity.ProAdapter.this, new Intent(UserAddressActivity.this, UserAddressAddActivity.class));
                    mIntent=new Intent(UserAddressActivity.this, UserAddressAddActivity.class);
                    mIntent.putExtra(Constant.address, (Serializable)mLogisticList.get(param1Int));
                    startActivityForResult(mIntent, 5);
                }
            });
            viewHolder.ll.setOnClickListener(new View.OnClickListener() {
                public void onClick(View param2View) {
                    if (UserAddressActivity.this.isSelectLogistics) {
                        Intent intent = new Intent();
                        intent.putExtra(Constant.address, (Serializable)mLogisticList.get(param1Int));
                        setResult(3, intent);
                        finish();
                    }
                }
            });
            if (logistics.getIsDefault() == 1) {
                viewHolder.default_addr_tv.setVisibility(View.INVISIBLE);
                return param1View;
            }
            viewHolder.default_addr_tv.setVisibility(View.GONE);
            return param1View;
        }

        class ViewHolder {
            TextView address_tv;

            TextView consignee_tv;

            TextView default_addr_tv;

            Button delete_bt;

            RelativeLayout edit_rl;

            Button edit_tv;

            LinearLayout ll;

            TextView phone_tv;
        }
    }

}
