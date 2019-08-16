//package cc.bocang.bocang.data.parser;
//
//import android.util.Log;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cc.bocang.bocang.data.model.GoodsAllAttr;
//import cc.bocang.bocang.data.model.GoodsAttr;
//import cc.bocang.bocang.data.model.Goods;
//import cc.bocang.bocang.data.model.GoodsPro;
//import cc.bocang.bocang.data.response.GetGoodsListResp;
//
///**
// * Created by xpHuang on 2016/8/18.
// */
//public class ParseGetGoodsListResp {
//
//    private static int current;
//    private static String currentName;
//
//    /**
//     * @param json
//     * @return 成功返回XXXResp，错误返回null或XXXResp.success==false
//     */
//    public static GetGoodsListResp parse(String json) {
//        if (null == json) {
//            return null;
//        }
//        try {
//            GetGoodsListResp resp = new GetGoodsListResp();
//            List<GoodsAllAttr> goodsAllAttrs = new ArrayList<>();
//            List<Goods> goodses = new ArrayList<>();
//            JSONObject jsonObject = new JSONObject(json);
//
//            JSONArray allAttrArray = jsonObject.optJSONArray("all_attr_list");
//            for (int i = 0; i < allAttrArray.length(); i++) {
//                GoodsAllAttr goodsAllAttr = new GoodsAllAttr();
//                List<GoodsAttr> goodsAttrs = new ArrayList<>();
//                JSONObject allAttrObj = allAttrArray.optJSONObject(i);
//                String attrName = allAttrObj.optString("filter_attr_name");
//                goodsAllAttr.setAttrName(attrName);
//                JSONArray attrArray = allAttrObj.optJSONArray("attr_list");
//                for (int j = 0; j < attrArray.length(); j++) {
//                    GoodsAttr goodsAttr = new GoodsAttr();
//                    JSONObject attrObj = attrArray.optJSONObject(j);
//                    String attr_value = attrObj.optString("attr_value");
//                    String url = attrObj.optString("url");
//                    String goods_id = attrObj.optString("goods_id");
//                    String selected = attrObj.optString("selected");
//                    goodsAttr.setAttr_value(attr_value);
//                    goodsAttr.setUrl(url);
//                    if ("".equals(goods_id))
//                        goodsAttr.setGoods_id(0);
//                    else
//                        goodsAttr.setGoods_id(Integer.parseInt(goods_id));
//                    goodsAttr.setSelected(selected);
//
//                    goodsAttrs.add(goodsAttr);
//                }
//
//                goodsAllAttr.setGoodsAttrs(goodsAttrs);
//
//                goodsAllAttrs.add(goodsAllAttr);
//            }
//
//            JSONArray goodsArray = jsonObject.optJSONArray("goodslist");
//            if (null != goodsArray) {
//                for (int i = 0; i < goodsArray.length(); i++) {
//                    Goods goods = new Goods();
//                    List<GoodsPro> goodsPros = new ArrayList<>();
//                    JSONObject goodsObj = goodsArray.optJSONObject(i);
//                    int id = goodsObj.optInt("id");
//                    String name = goodsObj.optString("name");
//                    String img_url = goodsObj.optString("img_url");
//                    String shop_price = goodsObj.optString("shop_price");
//                    String sort = goodsObj.optString("sort");
//                    String is_best = goodsObj.optString("is_best");
//                    String is_new = goodsObj.optString("is_new");
//                    String is_hot = goodsObj.optString("is_hot");
//                    String click = goodsObj.optString("click");
//                    String market_price = goodsObj.optString("market_price");
//                    String goods_number = goodsObj.optString("goods_number");
//                    goods.setId(id);
//                    goods.setName(name);
//                    goods.setImg_url(img_url);
//                    current = i;
//                    currentName = name;
//                    goods.setShop_price(Float.parseFloat(shop_price));
//                    goods.setSort(sort);
//                    goods.setIs_best(is_best);
//                    goods.setIs_new(is_new);
//                    goods.setIs_hot(is_hot);
//                    goods.setClick(click);
//                    goods.setMarket_price(market_price);
//                    goods.setGoods_number(goods_number);
//                    JSONArray goodsAttrArray = goodsObj.optJSONArray("attr");
//                    for (int j = 0; j < goodsAttrArray.length(); j++) {
//                        GoodsPro goodsPro = new GoodsPro();
//                        JSONObject goodsAttrObj = goodsAttrArray.optJSONObject(j);
//                        name = goodsAttrObj.optString("name");
//                        String value = goodsAttrObj.optString("value");
//                        goodsPro.setName(name);
//                        goodsPro.setValue(value);
//
//                        goodsPros.add(goodsPro);
//                    }
//                    goods.setGoodsPros(goodsPros);
//                    goodses.add(goods);
//                }
//            }
//            resp.setSuccess(true);
//            resp.setGoodsAllAttrs(goodsAllAttrs);
//            resp.setGoodses(goodses);
//
//            return resp;
//
//        } catch (Exception e) {
//            Log.e("currentname",currentName);
//            e.printStackTrace();
//            return null;
//        }
//    }
//}
package cc.bocang.bocang.data.parser;

import android.util.Log;
import cc.bocang.bocang.data.model.Goods;
import cc.bocang.bocang.data.model.GoodsAllAttr;
import cc.bocang.bocang.data.model.GoodsAttr;
import cc.bocang.bocang.data.model.GoodsPro;
import cc.bocang.bocang.data.response.GetGoodsListResp;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParseGetGoodsListResp {
    private static int i2;

    private static int ij;

    private static int ix;

    private static int j2;
    private static JSONObject jSONObject;

    public static GetGoodsListResp parse(String paramString) {
        ArrayList arrayList2;
        ArrayList arrayList1;
        GetGoodsListResp getGoodsListResp;
        if (paramString == null)
            return null;
        ix = 0;
        ij = 0;
        i2 = 0;
        j2 = 0;
        try {
            getGoodsListResp = new GetGoodsListResp();
            arrayList1 = new ArrayList();
            arrayList2 = new ArrayList();
            jSONObject = new JSONObject(paramString);
            JSONArray jSONArray1 = jSONObject.optJSONArray("all_attr_list");
            for (int  b = 0; b < jSONArray1.length(); b++) {
                ix = b;
                GoodsAllAttr goodsAllAttr = new GoodsAllAttr();
                ArrayList arrayList = new ArrayList();
                JSONObject jSONObject1 = jSONArray1.optJSONObject(b);
                goodsAllAttr.setAttrName(jSONObject1.optString("filter_attr_name"));
                JSONArray jSONArray2 = jSONObject1.optJSONArray("attr_list");
                for (byte b1 = 0; b1 < jSONArray2.length(); b1++) {
                    ij = b1;
                    GoodsAttr goodsAttr = new GoodsAttr();
                    JSONObject jSONObject2 = jSONArray2.optJSONObject(b1);
                    String str1 = jSONObject2.optString("attr_value");
                    String str2 = jSONObject2.optString("url");
                    String str3 = jSONObject2.optString("goods_id");
                    String str4 = jSONObject2.optString("selected");
                    goodsAttr.setAttr_value(str1);
                    goodsAttr.setUrl(str2);
                    if ("".equals(str3)) {
                        goodsAttr.setGoods_id(0);
                    } else {
                        goodsAttr.setGoods_id(Integer.parseInt(str3));
                    }
                    goodsAttr.setSelected(str4);
                    arrayList.add(goodsAttr);
                }
                goodsAllAttr.setGoodsAttrs(arrayList);
                arrayList1.add(goodsAllAttr);
            }
        } catch (Exception e) {
            Log.e("i,j", ix + "," + ij + "," + i2 + "," + j2);
            e.printStackTrace();
            return null;
        }
        JSONArray jSONArray = null;
        try {
            jSONArray = jSONObject.getJSONArray("goodslist");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jSONArray != null)
            for (byte b = 0; b < jSONArray.length(); b++) {
                i2 = b;
                Goods goods = new Goods();
                ArrayList arrayList = new ArrayList();
                JSONObject jSONObject = jSONArray.optJSONObject(b);
                int i = jSONObject.optInt("id");
                String str1 = jSONObject.optString("name");
                String str2 = jSONObject.optString("img_url");
                String str3 = jSONObject.optString("shop_price");
                String str4 = jSONObject.optString("sort");
                String str5 = jSONObject.optString("is_best");
                String str6 = jSONObject.optString("is_new");
                String str7 = jSONObject.optString("is_hot");
                String str8 = jSONObject.optString("click");
                String str9 = jSONObject.optString("market_price");
                String str10 = jSONObject.optString("goods_number");
                goods.setId(i);
                goods.setName(str1);
                goods.setImg_url(str2);
                goods.setShop_price(Float.parseFloat(str3));
                goods.setSort(str4);
                goods.setIs_best(str5);
                goods.setIs_new(str6);
                goods.setIs_hot(str7);
                goods.setClick(str8);
                goods.setMarket_price(str9);
                goods.setGoods_number(str10);
                JSONArray jSONArray1 = jSONObject.optJSONArray("attr");
                if (jSONArray1 != null)
                    for (i = 0; i < jSONArray1.length(); i++) {
                        j2 = i;
                        GoodsPro goodsPro = new GoodsPro();
                        JSONObject jSONObject1 = jSONArray1.optJSONObject(i);
                        str2 = jSONObject1.optString("name");
                        String str = jSONObject1.optString("value");
                        goodsPro.setName(str2);
                        goodsPro.setValue(str);
                        arrayList.add(goodsPro);
                    }
                goods.setGoodsPros(arrayList);
                arrayList2.add(goods);
            }
        getGoodsListResp.setSuccess(true);
        getGoodsListResp.setGoodsAllAttrs(arrayList1);
        getGoodsListResp.setGoodses(arrayList2);
        return getGoodsListResp;
    }
}
