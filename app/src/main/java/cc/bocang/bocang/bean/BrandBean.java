package cc.bocang.bocang.bean;

import java.util.List;

public class BrandBean {
    private List<Data> data;

    public List<Data> getData() { return this.data; }

    public void setData(List<Data> paramList) { this.data = paramList; }

    public class Data {
        private String desc;

        private String id;

        private String name;

        private String path;

        private String type;

        public String getDesc() { return this.desc; }

        public String getId() { return this.id; }

        public String getName() { return this.name; }

        public String getPath() { return this.path; }

        public String getType() { return this.type; }

        public void setDesc(String param1String) { this.desc = param1String; }

        public void setId(String param1String) { this.id = param1String; }

        public void setName(String param1String) { this.name = param1String; }

        public void setPath(String param1String) { this.path = param1String; }

        public void setType(String param1String) { this.type = param1String; }
    }
}
