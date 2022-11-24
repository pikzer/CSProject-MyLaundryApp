package th.ac.ku.model;

public class ClothListRecycle {
    int id;
    String cl_serviceTv ;
    String cl_cateTv;
    String quantityTv;
    String amt_tb;

    public ClothListRecycle(int id, String cl_serviceTv, String cl_cateTv, String quantityTv, String amt_tb) {
        this.id = id;
        this.cl_serviceTv = cl_serviceTv;
        this.cl_cateTv = cl_cateTv;
        this.quantityTv = quantityTv;
        this.amt_tb = amt_tb;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCl_serviceTv() {
        return cl_serviceTv;
    }

    public void setCl_serviceTv(String cl_serviceTv) {
        this.cl_serviceTv = cl_serviceTv;
    }

    public String getCl_cateTv() {
        return cl_cateTv;
    }

    public void setCl_cateTv(String cl_cateTv) {
        this.cl_cateTv = cl_cateTv;
    }

    public String getQuantityTv() {
        return quantityTv;
    }

    public void setQuantityTv(String quantityTv) {
        this.quantityTv = quantityTv;
    }

    public String getAmt_tb() {
        return amt_tb;
    }

    public void setAmt_tb(String amt_tb) {
        this.amt_tb = amt_tb;
    }
}
