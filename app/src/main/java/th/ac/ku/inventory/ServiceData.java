package th.ac.ku.inventory;

import java.util.ArrayList;
import java.util.List;

import th.ac.ku.R;

public class ServiceData {
    public static ArrayList<ServiceInventory> getServiceList(){
        ArrayList<ServiceInventory> serviceInventories = new ArrayList<>();
        serviceInventories.add(new ServiceInventory("ซักอบ", R.drawable.washfold));
        serviceInventories.add(new ServiceInventory("ซักรีด",R.drawable.washandirons_ic));
        serviceInventories.add(new ServiceInventory("ซักแห้ง",R.drawable.dryclean_ic));
        serviceInventories.add(new ServiceInventory("รีด",R.drawable.irons_ic));
        return serviceInventories;
    }

}
