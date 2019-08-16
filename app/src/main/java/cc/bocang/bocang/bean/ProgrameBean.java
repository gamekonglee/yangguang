package cc.bocang.bocang.bean;

import cc.bocang.bocang.data.model.Goods;
import java.util.ArrayList;
import java.util.List;

public class ProgrameBean {
    private String add_time;

    private List<Goods> goods = new ArrayList();

    private String goods_id;

    private String id;

    private String name;

    private String path;

    private String phone;

    private String title;

    private String village;

    public String getAdd_time() { return this.add_time; }

    public List<Goods> getGoods() { return this.goods; }

    public String getGoods_id() { return this.goods_id; }

    public String getId() { return this.id; }

    public String getName() { return this.name; }

    public String getPath() { return this.path; }

    public String getPhone() { return this.phone; }

    public String getTitle() { return this.title; }

    public String getVillage() { return this.village; }

    public void setAdd_time(String paramString) { this.add_time = paramString; }

    public void setGoods(List<Goods> paramList) { this.goods = paramList; }

    public void setGoods_id(String paramString) { this.goods_id = paramString; }

    public void setId(String paramString) { this.id = paramString; }

    public void setName(String paramString) { this.name = paramString; }

    public void setPath(String paramString) { this.path = paramString; }

    public void setPhone(String paramString) { this.phone = paramString; }

    public void setTitle(String paramString) { this.title = paramString; }

    public void setVillage(String paramString) { this.village = paramString; }
}
