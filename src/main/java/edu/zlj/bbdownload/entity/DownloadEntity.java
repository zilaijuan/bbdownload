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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        this.filename = url.substring(url.lastIndexOf("/")+1);
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
}
