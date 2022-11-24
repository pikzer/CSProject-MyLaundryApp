package th.ac.ku.model;

public class Order {

    Integer id;
    String service ;
    String name ;
    String cus_phone;
    String pickDate ;
    String pickTime ;
    String deliDate ;
    String deliTime ;
    String address ;
    String responder ;
    String deliver ;
    Integer payStatus ;
    String payMethod ;
    Double pickSerCharge ;
    Double deliSerCharge ;
    Double total;
    String status ;
    Integer isMemOrder ;

    String created_at;


    public Order(String service, String payMethod, String status, Integer isMemOrder,Integer payStatus) {
        this.service = service;
        this.payMethod = payMethod;
        this.status = status;
        this.isMemOrder = isMemOrder;
        this.payStatus = payStatus ;
    }

    public Order(String service, String deliDate, String deliTime, String address, Integer payStatus, String payMethod, Integer isMemOrder) {
        this.service = service;
        this.deliDate = deliDate;
        this.deliTime = deliTime ;
        this.address = address;
        this.payStatus = payStatus;
        this.payMethod = payMethod;
        this.isMemOrder = isMemOrder;
    }


    public Order(String service, String pickDate, String pickTime, String address, String payMethod) {
        this.service = service;
        this.pickDate = pickDate;
        this.pickTime = pickTime;
        this.address = address;
        this.payMethod = payMethod;
    }

    public Order(Integer id, String cus_phone, String service, String name, String pickDate, String pickTime, String deliDate, String deliTime, String address, String responder, String deliver, Integer payStatus, String payMethod, Double pickSerCharge, Double deliSerCharge, Double total, String status, Integer isMemOrder) {
        this.id = id;
        this.cus_phone = cus_phone;
        this.service = service;
        this.name = name;
        this.pickDate = pickDate;
        this.pickTime = pickTime;
        this.deliDate = deliDate;
        this.deliTime = deliTime;
        this.address = address;
        this.responder = responder;
        this.deliver = deliver;
        this.payStatus = payStatus;
        this.payMethod = payMethod;
        this.pickSerCharge = pickSerCharge;
        this.deliSerCharge = deliSerCharge;
        this.total = total;
        this.status = status;
        this.isMemOrder = isMemOrder;
    }

    public Order(Integer id, String service, String name, String cus_phone, String pickDate, String pickTime, String deliDate, String deliTime, String address, String responder, String deliver, Integer payStatus, String payMethod, Double pickSerCharge, Double deliSerCharge, Double total, String status, Integer isMemOrder, String created_at) {
        this.id = id;
        this.service = service;
        this.name = name;
        this.cus_phone = cus_phone;
        this.pickDate = pickDate;
        this.pickTime = pickTime;
        this.deliDate = deliDate;
        this.deliTime = deliTime;
        this.address = address;
        this.responder = responder;
        this.deliver = deliver;
        this.payStatus = payStatus;
        this.payMethod = payMethod;
        this.pickSerCharge = pickSerCharge;
        this.deliSerCharge = deliSerCharge;
        this.total = total;
        this.status = status;
        this.isMemOrder = isMemOrder;
        this.created_at = created_at.substring(0,10);
    }

    public String getCus_phone() {
        return cus_phone;
    }

    public void setCus_phone(String cus_phone) {
        this.cus_phone = cus_phone;
    }

    public Integer getIsMemOrder() {
        return isMemOrder;
    }

    public void setIsMemOrder(Integer isMemOrder) {
        this.isMemOrder = isMemOrder;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getName() {
        return name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPickDate() {
        return pickDate;
    }

    public void setPickDate(String pickDate) {
        this.pickDate = pickDate;
    }

    public String getPickTime() {
        return pickTime;
    }

    public void setPickTime(String pickTime) {
        this.pickTime = pickTime;
    }

    public String getDeliDate() {
        return deliDate;
    }

    public void setDeliDate(String deliDate) {
        this.deliDate = deliDate;
    }

    public String getDeliTime() {
        return deliTime;
    }

    public void setDeliTime(String deliTime) {
        this.deliTime = deliTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getResponder() {
        return responder;
    }

    public void setResponder(String responder) {
        this.responder = responder;
    }

    public String getDeliver() {
        return deliver;
    }

    public void setDeliver(String deliver) {
        this.deliver = deliver;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public Double getPickSerCharge() {
        return pickSerCharge;
    }

    public void setPickSerCharge(Double pickSerCharge) {
        this.pickSerCharge = pickSerCharge;
    }

    public Double getDeliSerCharge() {
        return deliSerCharge;
    }

    public void setDeliSerCharge(Double deliSerCharge) {
        this.deliSerCharge = deliSerCharge;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getMemOrder() {
        return isMemOrder;
    }

    public void setMemOrder(Integer memOrder) {
        isMemOrder = memOrder;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", service='" + service + '\'' +
                ", name='" + name + '\'' +
                ", cus_phone='" + cus_phone + '\'' +
                ", pickDate='" + pickDate + '\'' +
                ", pickTime='" + pickTime + '\'' +
                ", deliDate='" + deliDate + '\'' +
                ", deliTime='" + deliTime + '\'' +
                ", address='" + address + '\'' +
                ", responder='" + responder + '\'' +
                ", deliver='" + deliver + '\'' +
                ", payStatus=" + payStatus +
                ", payMethod='" + payMethod + '\'' +
                ", pickSerCharge=" + pickSerCharge +
                ", deliSerCharge=" + deliSerCharge +
                ", total=" + total +
                ", status='" + status + '\'' +
                ", isMemOrder=" + isMemOrder +
                '}';
    }
}
