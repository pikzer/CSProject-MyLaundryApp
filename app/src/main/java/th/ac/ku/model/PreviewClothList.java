package th.ac.ku.model;

public class PreviewClothList {

    String service;
    String clothType;
    Integer quantity ;
    Double pricePerU;
    Double amount ;

    public PreviewClothList(String service, String clothType, Integer quantity, Double pricePerU, Double amount) {
        this.service = service;
        this.clothType = clothType;
        this.quantity = quantity;
        this.pricePerU = pricePerU;
        this.amount = amount;
    }

    public String getService() {
        return service;
    }

    public String getClothType() {
        return clothType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPricePerU() {
        return pricePerU;
    }

    public Double getAmount() {
        return amount;
    }
}
