package edu.zlj.bbdownload.entity;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: XuyangHwang
 * Date: 2020-07-22
 * Time: 17:46
 */
public class SourceDetail {
    private String id;
    private String epoch;
    private String iPeak;
    private String pPeak;
    private String EVPA;
    private String viewImage;
    //    private String  iMap;
    private String psFile;
    private String fitsFile;
    private String pMap;
    private String UVdata;
    private String CLEANModel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEpoch() {
        return epoch;
    }

    public void setEpoch(String epoch) {
        this.epoch = epoch;
    }

    public String getiPeak() {
        return iPeak;
    }

    public void setiPeak(String iPeak) {
        this.iPeak = iPeak;
    }

    public String getpPeak() {
        return pPeak;
    }

    public void setpPeak(String pPeak) {
        this.pPeak = pPeak;
    }

    public String getEVPA() {
        return EVPA;
    }

    public void setEVPA(String EVPA) {
        this.EVPA = EVPA;
    }

    public String getViewImage() {
        return viewImage;
    }

    public void setViewImage(String viewImage) {
        this.viewImage = viewImage;
    }

    public String getPsFile() {
        return psFile;
    }

    public void setPsFile(String psFile) {
        this.psFile = psFile;
    }

    public String getFitsFile() {
        return fitsFile;
    }

    public void setFitsFile(String fitsFile) {
        this.fitsFile = fitsFile;
    }

    public String getpMap() {
        return pMap;
    }

    public void setpMap(String pMap) {
        this.pMap = pMap;
    }

    public String getUVdata() {
        return UVdata;
    }

    public void setUVdata(String UVdata) {
        this.UVdata = UVdata;
    }

    public String getCLEANModel() {
        return CLEANModel;
    }

    public void setCLEANModel(String CLEANModel) {
        this.CLEANModel = CLEANModel;
    }
}
