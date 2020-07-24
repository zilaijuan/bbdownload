package edu.zlj.bbdownload.config;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: XuyangHwang
 * Date: 2020-07-24
 * Time: 15:03
 */
public enum DownloadStatus {
    SUCCESS("success",1),FAILED("failed",2),READY("ready",0);
    private String name;
    private int state;
    private DownloadStatus(String name, int state){
        this.name = name;
        this.state = state;
    }
}
