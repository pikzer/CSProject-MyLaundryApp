package th.ac.ku.inventory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServiceInventory implements Serializable {

    private String serviceName;
    private int image ;

    public ServiceInventory() {
    }

    public ServiceInventory(String serviceName, int image) {
        this.serviceName = serviceName;
        this.image = image;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}

