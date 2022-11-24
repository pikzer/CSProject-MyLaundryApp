package th.ac.ku.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import th.ac.ku.R;
import th.ac.ku.model.AddressRecycle;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private List<AddressRecycle> addressRecycles ;
    private Adapter.OnNoteListener onNoteListener ;

    public AddressAdapter(List<AddressRecycle> addressRecycles, Adapter.OnNoteListener onNoteListener) {
        this.addressRecycles = addressRecycles;
        this.onNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ads_recycle_item,parent,false);
        return  new AddressAdapter.ViewHolder(view,onNoteListener) ;
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.ViewHolder holder, int position) {
        String nameAdsTv = addressRecycles.get(position).getNameAdsTv();
        String ucodeTv = addressRecycles.get(position).getUcodeTv();
        holder.setData(nameAdsTv,ucodeTv);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nameAdsTv,ucodeTv;
        Adapter.OnNoteListener onNoteListener ;
        public ViewHolder(@NonNull View itemView, Adapter.OnNoteListener onClickListener) {
            super(itemView);
            nameAdsTv = itemView.findViewById(R.id.nameAdsTv);
            ucodeTv = itemView.findViewById(R.id.ucodeTv);
        }
        public void setData(String nameAdsTv, String ucodeTv){
            this.nameAdsTv.setText(nameAdsTv);
            this.ucodeTv.setText(ucodeTv);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }
    public interface OnNoteListener {
        void onNoteClick(int pos) ;
    }

    @Override
    public int getItemCount() {
        return addressRecycles.size();
    }
}
