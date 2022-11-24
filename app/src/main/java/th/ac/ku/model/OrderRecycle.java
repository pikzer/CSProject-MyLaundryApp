package th.ac.ku.model;

public class OrderRecycle {
    int id;
    String orderNumTV ;
    String service_tv ;
    String pickordropTv ;
    String droporPickTime ;
    String statusNow_tv ;
    String ads_tv ;
    int statusImg ;

    public OrderRecycle(int id,String orderNumTV, String service_tv,String ads_tv, String pickordropTv, String droporPickTime, String statusNow_tv, int statusImg) {
        this.id = id ;
        this.orderNumTV = orderNumTV;
        this.service_tv = service_tv;
        this.ads_tv =ads_tv;
        this.pickordropTv = pickordropTv;
        this.droporPickTime = droporPickTime;
        this.statusNow_tv = statusNow_tv;
        this.statusImg = statusImg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAds_tv() {
        return ads_tv;
    }

    public void setAds_tv(String ads_tv) {
        this.ads_tv = ads_tv;
    }

    public String getOrderNumTV() {
        return orderNumTV;
    }

    public void setOrderNumTV(String orderNumTV) {
        this.orderNumTV = orderNumTV;
    }

    public String getService_tv() {
        return service_tv;
    }

    public void setService_tv(String service_tv) {
        this.service_tv = service_tv;
    }

    public String getPickordropTv() {
        return pickordropTv;
    }

    public void setPickordropTv(String pickordropTv) {
        this.pickordropTv = pickordropTv;
    }

    public String getDroporPickTime() {
        return droporPickTime;
    }

    public void setDroporPickTime(String droporPickTime) {
        this.droporPickTime = droporPickTime;
    }

    public String getStatusNow_tv() {
        return statusNow_tv;
    }

    public void setStatusNow_tv(String statusNow_tv) {
        this.statusNow_tv = statusNow_tv;
    }

    public int getStatusImg() {
        return statusImg;
    }

    public void setStatusImg(int statusImg) {
        this.statusImg = statusImg;
    }
}
