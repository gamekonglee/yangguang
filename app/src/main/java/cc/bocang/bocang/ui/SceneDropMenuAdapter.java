//package cc.bocang.bocang.ui;
//
//import android.content.Context;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.FrameLayout;
//
//import com.baiiu.filter.adapter.MenuAdapter;
//import com.baiiu.filter.adapter.SimpleTextAdapter;
//import com.baiiu.filter.interfaces.OnFilterDoneListener;
//import com.baiiu.filter.interfaces.OnFilterItemClickListener;
//import com.baiiu.filter.typeview.SingleGridView;
//import com.baiiu.filter.util.UIUtil;
//import com.baiiu.filter.view.FilterCheckedTextView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cc.bocang.bocang.R;
//import cc.bocang.bocang.data.model.SceneAllAttr;
//import cc.bocang.bocang.data.model.SceneAttr;
//
///**
// * 场景筛选条件
// */
//public class SceneDropMenuAdapter implements MenuAdapter {
//    private final Context mContext;
//    private OnFilterDoneListener onFilterDoneListener;
//    private List<SceneAllAttr> sceneAllAttrs;
//    private List<Integer> itemPosList;
//
//    public SceneDropMenuAdapter(Context context, List<SceneAllAttr> sceneAllAttrs, List<Integer> itemPosList, OnFilterDoneListener onFilterDoneListener) {
//        this.mContext = context;
//        this.sceneAllAttrs = sceneAllAttrs;
//        this.onFilterDoneListener = onFilterDoneListener;
//        this.itemPosList = itemPosList;
//    }
//
//    @Override
//    public int getMenuCount() {
//        return sceneAllAttrs.size();
//    }
//
//    @Override
//    public String getMenuTitle(int position) {
//        if (position < itemPosList.size()) {
//            int itemPos = itemPosList.get(position);
//            if (itemPos != 0) {
//                return sceneAllAttrs.get(position).getSceneAttrs().get(itemPos).getAttr_value();
//            }
//        }
//        return sceneAllAttrs.get(position).getAttrName();
//    }
//
//    @Override
//    public int getBottomMargin(int position) {
//
//        return 0;
//    }
//
//    @Override
//    public View getView(int position, FrameLayout parentContainer) {
//        return createSingleGridView(position);
//    }
//
//    private View createSingleGridView(final int position) {
//        SingleGridView<String> singleGridView = new SingleGridView<String>(mContext)
//                .adapter(new SimpleTextAdapter<String>(null, mContext) {
//                    @Override
//                    public String provideText(String s) {
//                        return s;
//                    }
//
//                    @Override
//                    protected void initCheckedTextView(FilterCheckedTextView checkedTextView) {
//                        checkedTextView.setPadding(0, UIUtil.dp(context, 3), 0, UIUtil.dp(context, 3));
//                        checkedTextView.setGravity(Gravity.CENTER);
//                        checkedTextView.setBackgroundResource(R.drawable.selector_filter_grid);
//                    }
//                })
//                .onItemClick(new OnFilterItemClickListener<String>() {
//                    @Override
//                    public void onItemClick(int itemPos, String itemStr) {
//
//                        if (onFilterDoneListener != null) {
//                            onFilterDoneListener.onFilterDone(position, itemPos, itemStr);
//                        }
//
//                    }
//                });
//
//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < sceneAllAttrs.get(position).getSceneAttrs().size(); ++i) {
//            SceneAttr sceneAttr = sceneAllAttrs.get(position).getSceneAttrs().get(i);
//            list.add(sceneAttr.getAttr_value());
//        }
//        int itemPos = 0;
//        if (position < itemPosList.size())
//            itemPos = itemPosList.get(position);
//        singleGridView.setList(list, itemPos);
//
//        return singleGridView;
//    }
//}

package cc.bocang.bocang.ui;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import cc.bocang.bocang.R;
import cc.bocang.bocang.data.model.SceneAllAttr;
import cc.bocang.bocang.data.model.SceneAttr;
import com.baiiu.filter.adapter.MenuAdapter;
import com.baiiu.filter.adapter.SimpleTextAdapter;
import com.baiiu.filter.interfaces.OnFilterDoneListener;
import com.baiiu.filter.interfaces.OnFilterItemClickListener;
import com.baiiu.filter.typeview.SingleGridView;
import com.baiiu.filter.util.UIUtil;
import com.baiiu.filter.view.FilterCheckedTextView;
import java.util.ArrayList;
import java.util.List;

public class SceneDropMenuAdapter implements MenuAdapter {
    private List<Integer> itemPosList;

    private final Context mContext;

    private OnFilterDoneListener onFilterDoneListener;

    private List<SceneAllAttr> sceneAllAttrs;

    public SceneDropMenuAdapter(Context paramContext, List<SceneAllAttr> paramList1, List<Integer> paramList2, OnFilterDoneListener paramOnFilterDoneListener) {
        this.mContext = paramContext;
        this.sceneAllAttrs = paramList1;
        this.onFilterDoneListener = paramOnFilterDoneListener;
        this.itemPosList = paramList2;
    }

    private View createSingleGridView(final int position) {
        SingleGridView singleGridView = (new SingleGridView(this.mContext)).adapter(new SimpleTextAdapter<String>(null, this.mContext) {
            protected void initCheckedTextView(FilterCheckedTextView param1FilterCheckedTextView) {
                param1FilterCheckedTextView.setPadding(0, UIUtil.dp(this.context, 3), 0, UIUtil.dp(this.context, 3));
                param1FilterCheckedTextView.setGravity(Gravity.CENTER);
                param1FilterCheckedTextView.setBackgroundResource(R.drawable.selector_filter_grid);
            }

            public String provideText(String param1String) { return param1String; }
        }).onItemClick(new OnFilterItemClickListener<String>() {
            public void onItemClick(int param1Int, String param1String) {
                if (SceneDropMenuAdapter.this.onFilterDoneListener != null)
                    SceneDropMenuAdapter.this.onFilterDoneListener.onFilterDone(position, param1Int, param1String);
            }
        });
        ArrayList arrayList = new ArrayList();
        int i;
        for (i = 0; i < ((SceneAllAttr)this.sceneAllAttrs.get(position)).getSceneAttrs().size(); i++)
            arrayList.add(((SceneAttr)((SceneAllAttr)this.sceneAllAttrs.get(position)).getSceneAttrs().get(i)).getAttr_value());
        i = 0;
        if (position < this.itemPosList.size())
            i = ((Integer)this.itemPosList.get(position)).intValue();
        singleGridView.setList(arrayList, i);
        return singleGridView;
    }

    public int getBottomMargin(int paramInt) { return 0; }

    public int getMenuCount() { return this.sceneAllAttrs.size(); }

    public String getMenuTitle(int paramInt) {
        if (paramInt < this.itemPosList.size()) {
            int i = ((Integer)this.itemPosList.get(paramInt)).intValue();
            if (i != 0)
                return ((SceneAttr)((SceneAllAttr)this.sceneAllAttrs.get(paramInt)).getSceneAttrs().get(i)).getAttr_value();
        }
        return ((SceneAllAttr)this.sceneAllAttrs.get(paramInt)).getAttrName();
    }

    public View getView(int paramInt, FrameLayout paramFrameLayout) { return createSingleGridView(paramInt); }
}
