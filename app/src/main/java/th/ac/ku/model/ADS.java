package th.ac.ku.model;

public class ADS {
    private Integer id;
    private String name;
    private String uCode;
    private String lat;
    private String lng;
    private String hint;
    private String contact;

    public ADS(Integer id, String uCode) {
        this.id = id;
        this.uCode = uCode;
    }

    public ADS(Integer id, String name, String uCode) {
        this.id = id;
        this.name = name;
        this.uCode = uCode;
    }

    public ADS(Integer id, String name, String uCode, String lat, String lng, String hint, String contact) {
        this.id = id;
        this.name = name;
        this.uCode = uCode;
        this.lat = lat;
        this.lng = lng;
        this.hint = hint;
        this.contact = contact;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getuCode() {
        return uCode;
    }

    public void setuCode(String uCode) {
        this.uCode = uCode;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", uCode='" + uCode + '\'' +
                '}';
    }
}
