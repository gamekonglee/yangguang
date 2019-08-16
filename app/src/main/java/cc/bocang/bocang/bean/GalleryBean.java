package cc.bocang.bocang.bean;

import java.util.List;

public class GalleryBean {
    private List<Data> data;

    private String desc;

    private String id;

    private String name;

    private String path;

    public List<Data> getData() { return this.data; }

    public String getDesc() { return this.desc; }

    public String getId() { return this.id; }

    public String getName() { return this.name; }

    public String getPath() { return this.path; }

    public void setData(List<Data> paramList) { this.data = paramList; }

    public void setDesc(String paramString) { this.desc = paramString; }

    public void setId(String paramString) { this.id = paramString; }

    public void setName(String paramString) { this.name = paramString; }

    public void setPath(String paramString) { this.path = paramString; }

    public class Data {
        private String atta_id;

        private String cat_id;

        private String filepath;

        private String filesize;

        private String filetype;

        private String id;

        private String name;

        private String sort;

        private String title;

        private String uploadtime;

        public String getAtta_id() { return this.atta_id; }

        public String getCat_id() { return this.cat_id; }

        public String getFilepath() { return this.filepath; }

        public String getFilesize() { return this.filesize; }

        public String getFiletype() { return this.filetype; }

        public String getId() { return this.id; }

        public String getName() { return this.name; }

        public String getSort() { return this.sort; }

        public String getTitle() { return this.title; }

        public String getUploadtime() { return this.uploadtime; }

        public void setAtta_id(String param1String) { this.atta_id = param1String; }

        public void setCat_id(String param1String) { this.cat_id = param1String; }

        public void setFilepath(String param1String) { this.filepath = param1String; }

        public void setFilesize(String param1String) { this.filesize = param1String; }

        public void setFiletype(String param1String) { this.filetype = param1String; }

        public void setId(String param1String) { this.id = param1String; }

        public void setName(String param1String) { this.name = param1String; }

        public void setSort(String param1String) { this.sort = param1String; }

        public void setTitle(String param1String) { this.title = param1String; }

        public void setUploadtime(String param1String) { this.uploadtime = param1String; }
    }
}
