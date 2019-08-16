package cc.bocang.bocang.ui;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cc.bocang.bocang.R;
import cc.bocang.bocang.data.model.UserInfo;
import cc.bocang.bocang.global.MyApplication;
import cc.bocang.bocang.utils.UIUtils;

public class FmMine extends BaseFragment implements View.OnClickListener {
    private void showShare() { UIUtils.showShareDialogWithThumb(getActivity(), getString(R.string.app_name)+"配灯系统App", "http://app.bocang.cc/Ewm/index/url/yangguang.bocang.cc", null, "http://app.08138.com/icon.jpg"); }

    protected int getLayout() { return R.layout.fm_mine; }


    protected void initController() {}

    protected void initData() {}

    protected void initView() {
        TextView textView1 = (TextView)getView().findViewById(R.id.tv_name);
        TextView textView2 = (TextView)getView().findViewById(R.id.tv_mobile);
        RelativeLayout relativeLayout1 = (RelativeLayout)getView().findViewById(R.id.view_collect);
        RelativeLayout relativeLayout2 = (RelativeLayout)getView().findViewById(R.id.view_share);
        RelativeLayout relativeLayout3 = (RelativeLayout)getView().findViewById(R.id.view_setting);
        View view = getView().findViewById(R.id.view_distrubution);
        relativeLayout1.setOnClickListener(this);
        relativeLayout2.setOnClickListener(this);
        relativeLayout3.setOnClickListener(this);
        view.setOnClickListener(this);
        UserInfo userInfo = (MyApplication.getInstance()).mUserInfo;
        if (userInfo != null && userInfo.getPhone() != null) {
            textView1.setText("" + userInfo.getName());
            textView2.setText(userInfo.getPhone().substring(0, userInfo.getPhone().length() - 5) + "*****");
        }
    }

    protected void initViewData() {}

    public void onClick(View paramView) {
        switch (paramView.getId()) {
            default:
                return;
            case R.id.view_collect:
                startActivity(new Intent(getActivity(), CollectActivity.class));
                return;
            case R.id.view_share:
                showShare();
                return;
            case R.id.view_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                return;
            case R.id.view_distrubution:
                break;
        }
        Intent intent = new Intent(getActivity(), ContainerActivity.class);
        intent.putExtra("title", "经销商管理");
        intent.putExtra("fm", FmDistributor.class.getSimpleName());
        getActivity().startActivity(intent);
    }
}
