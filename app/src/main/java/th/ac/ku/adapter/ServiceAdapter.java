package th.ac.ku.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import th.ac.ku.R;
import th.ac.ku.inventory.ServiceInventory;

public class ServiceAdapter extends BaseAdapter {

    private Context context ;
    private ArrayList<ServiceInventory> serviceInventories ;

    public ServiceAdapter(Context context,ArrayList<ServiceInventory> serviceInventories){
        this.context = context;
        this.serviceInventories = serviceInventories ;
    }

    @Override
    public int getCount() {
        return serviceInventories != null ? serviceInventories.size() : 0 ;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.service_items,viewGroup,false);
        TextView txtName = rootView.findViewById(R.id.name);
        ImageView image = rootView.findViewById(R.id.image);
        txtName.setText(serviceInventories.get(i).getServiceName());
        image.setImageResource(serviceInventories.get(i).getImage());
        return rootView;
    }
}
