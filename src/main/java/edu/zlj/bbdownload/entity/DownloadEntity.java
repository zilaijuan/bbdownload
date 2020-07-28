package edu.zlj.bbdownload.entity;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: XuyangHwang
 * Date: 2020-07-23
 * Time: 14:56
 */
public class DownloadEntity {
    private String url;
    private String path;
    private SourceDetail sourceDetail;
    private String filename;
    private boolean needUnZip;
    private String project;
    private String source_name;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
//        this.filename = url.substring(url.lastIndexOf("/")+1);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public SourceDetail getSourceDetail() {
        return sourceDetail;
    }

    public void setSourceDetail(SourceDetail sourceDetail) {
        this.sourceDetail = sourceDetail;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public boolean isNeedUnZip() {
        return needUnZip;
    }

    public void setNeedUnZip(boolean needUnZip) {
        this.needUnZip = needUnZip;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getSource_name() {
        return source_name;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    @Override
    public String  toString(){
        return project+" "+source_name+" "+sourceDetail.getEpoch();
    }
}
