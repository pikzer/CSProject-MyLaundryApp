package th.ac.ku.model;

public class AddressRecycle {
    String nameAdsTv;
    String ucodeTv ;

    public AddressRecycle(String nameAdsTv, String ucodeTv) {
        this.nameAdsTv = nameAdsTv;
        this.ucodeTv = ucodeTv;
    }

    public String getNameAdsTv() {
        return nameAdsTv;
    }

    public void setNameAdsTv(String nameAdsTv) {
        this.nameAdsTv = nameAdsTv;
    }

    public String getUcodeTv() {
        return ucodeTv;
    }

    public void setUcodeTv(String ucodeTv) {
        this.ucodeTv = ucodeTv;
    }
}
