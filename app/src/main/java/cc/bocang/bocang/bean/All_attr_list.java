package cc.bocang.bocang.bean;

import java.util.List;

public class All_attr_list {
    private List<Attr_list> attr_list;

    public int currentP = 0;

    private String filter_attr_name;

    public List<Attr_list> getAttr_list() { return this.attr_list; }

    public String getFilter_attr_name() { return this.filter_attr_name; }

    public void setAttr_list(List<Attr_list> paramList) { this.attr_list = paramList; }

    public void setFilter_attr_name(String paramString) { this.filter_attr_name = paramString; }
}
