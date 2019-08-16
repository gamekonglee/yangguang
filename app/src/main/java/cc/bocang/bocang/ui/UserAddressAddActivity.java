package cc.bocang.bocang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import cc.bocang.bocang.R;
import cc.bocang.bocang.data.dao.LogisticDao;
import cc.bocang.bocang.data.model.Logistics;
import cc.bocang.bocang.global.Constant;
import cc.bocang.bocang.utils.AppUtils;
import cc.bocang.bocang.utils.StringUtil;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.util.ConvertUtils;
import com.alibaba.fastjson.JSON;
import com.baiiu.filter.util.CommonUtil;
import java.util.ArrayList;

public class UserAddressAddActivity extends BaseActivity implements View.OnClickListener {
    private String address;

    private View btnSave;

    private String mId = "";

    private Intent mIntent;

    public Logistics mLogistics;

    private String mRegion;

    private CheckBox select_cb;

    private View topLeftBtn;

    private TextView tv_region;

    private TextView user_addr_editName;

    private TextView user_addr_editPhone;

    private TextView user_detail_addr;

    private void initView() {
        this.btnSave = (Button)findViewById(R.id.btnSave);
        this.btnSave.setOnClickListener(this);
        this.topLeftBtn = findViewById(R.id.topLeftBtn);
        this.topLeftBtn.setOnClickListener(this);
        this.user_addr_editName = (TextView)findViewById(R.id.user_addr_editName);
        this.user_addr_editPhone = (TextView)findViewById(R.id.user_addr_editPhone);
        this.user_detail_addr = (TextView)findViewById(R.id.user_detail_addr);
        this.select_cb = (CheckBox)findViewById(R.id.select_cb);
        this.tv_region = (TextView)findViewById(R.id.tv_region);
        this.tv_region.setOnClickListener(this);
    }

    private void initViewData() {
        if (StringUtil.isEmpty(this.mLogistics))
            return;
        this.user_addr_editName.setText(this.mLogistics.getName());
        this.user_detail_addr.setText(this.mLogistics.getAddress());
        this.user_addr_editPhone.setText(this.mLogistics.getTel());
        if (this.mLogistics.getIsDefault() == 0) {
            this.select_cb.setChecked(false);
        } else {
            this.select_cb.setChecked(true);
        }
        this.mId = this.mLogistics.getId() + "";
    }

    protected void initData() {
        this.mIntent = getIntent();
        this.mLogistics = (Logistics)this.mIntent.getSerializableExtra(Constant.address);
    }

    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.btnSave:
                sendAddLogistic();
                break;
            case R.id.topLeftBtn:
                finish();
                break;
            case R.id.tv_region:
                selectAddress();
                break;
        }

    }

    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_user_address_add);
        initData();
        initView();
        initViewData();
    }

    public void selectAddress() {
        try {
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(JSON.parseArray(ConvertUtils.toString(getAssets().open("city.json")), Province.class));
            AddressPicker addressPicker = new AddressPicker(this, arrayList);
            addressPicker.setHideProvince(false);
            addressPicker.setSelectedItem("广东", "佛山", "禅城");
            addressPicker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                public void onAddressPicked(Province param1Province, City param1City, County param1County) {
                    param1Province.getAreaId();
                    param1City.getAreaId();
                    param1County.getAreaId();
                    address=param1Province.getAreaName() + " " + param1City.getAreaName() + " " + param1County.getAreaName();
//                    UserAddressAddActivity.access$002(UserAddressAddActivity.this, param1Province.getAreaName() + " " + param1City.getAreaName() + " " + param1County.getAreaName());
                    if (AppUtils.isEmpty(param1County.getCityId())) {
                        mRegion=param1City.getAreaId();
                    } else {
                        mRegion=param1County.getAreaId();
//                        UserAddressAddActivity.access$102(UserAddressAddActivity.this, param1County.getAreaId());
                    }
                    UserAddressAddActivity.this.tv_region.setText(UserAddressAddActivity.this.address);
                }
            });
            addressPicker.show();
            return;
        } catch (Exception exception) {
            return;
        }
    }

    public void sendAddLogistic() {
        byte b;
        String str1 = this.user_addr_editName.getText().toString();
        String str2 = this.user_addr_editPhone.getText().toString();
        String str3 = this.user_detail_addr.getText().toString();
        if (StringUtil.isEmpty(str1)) {
            Toast.makeText(this, "收货人名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isEmpty(str2)) {
            Toast.makeText(this, "收货人电话不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isEmpty(str3)) {
            Toast.makeText(this, "收货地址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!CommonUtil.isMobileNO(str2)) {
            Toast.makeText(this, "请输入正确的号码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(this.address)) {
            Toast.makeText(this, "请选择地区", Toast.LENGTH_SHORT).show();
            return;
        }
        LogisticDao logisticDao = new LogisticDao(this);
        if (this.select_cb.isChecked())
            logisticDao.UpdateDefault("");
        Logistics logistics = new Logistics();
        logistics.setName(str1);
        logistics.setTel(str2);
        logistics.setAddress(this.address + str3);
        if (this.select_cb.isChecked()) {
            b = 1;
        } else {
            b = 0;
        }
        logistics.setIsDefault(b);
        if (logisticDao.replaceOne(logistics) == -1L) {
            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
        if (!StringUtil.isEmpty(this.mId))
            logisticDao.deleteOne(Integer.parseInt(this.mId));
        this.mIntent = new Intent();
        setResult(5, this.mIntent);
        finish();
    }
}
