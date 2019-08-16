package cc.bocang.bocang.bean;

import java.util.List;

public class Attr {
    private List<Pro> pro;

    private List<String> spe;

    public List<Pro> getPro() { return this.pro; }

    public List<String> getSpe() { return this.spe; }

    public void setPro(List<Pro> paramList) { this.pro = paramList; }

    public void setSpe(List<String> paramList) { this.spe = paramList; }
}
