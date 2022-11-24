package th.ac.ku.model;

public class Customer {
    Integer id ;
    String name;
    String phone;
    String email;
    Integer isMembership;
    String memService;
    Integer memCredit ;

    public Customer(Integer id, String name, String phone, String email, Integer isMembership, String memService, Integer memCredit) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.isMembership = isMembership;
        this.memService = memService;
        this.memCredit = memCredit;
    }

    public Customer(Integer id , String name,String phone, String email){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }





    public void setMemService(String memService) {
        this.memService = memService;
    }

    public void setMemCredit(Integer memCredit) {
        this.memCredit = memCredit;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public Integer getIsMembership() {
        return isMembership;
    }

    public String getMemService() {
        return memService;
    }

    public Integer getMemCredit() {
        return memCredit;
    }
}
