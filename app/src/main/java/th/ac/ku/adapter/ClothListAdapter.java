package th.ac.ku.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import th.ac.ku.R;
import th.ac.ku.model.ClothListRecycle;
import th.ac.ku.model.OrderRecycle;

public class ClothListAdapter extends RecyclerView.Adapter<ClothListAdapter.ViewHolder>  {

    private List<ClothListRecycle> clothListRecycles ;
    private Adapter.OnNoteListener onNoteListener ;

    public ClothListAdapter(List<ClothListRecycle> clothListRecycles, Adapter.OnNoteListener onNoteListener) {
        this.clothListRecycles = clothListRecycles;
        this.onNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public ClothListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cloth_recycle_item,parent,false);
        return  new ClothListAdapter.ViewHolder(view,onNoteListener) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ClothListAdapter.ViewHolder holder, int position) {
            String cl_serviceTv = clothListRecycles.get(position).getCl_serviceTv();
            String cate_tv = clothListRecycles.get(position).getCl_cateTv();
            String quantityTv = clothListRecycles.get(position).getQuantityTv();
            String amt_tb = clothListRecycles.get(position).getAmt_tb();
            holder.setData(cl_serviceTv,cate_tv,quantityTv,amt_tb);
    }

    @Override
    public int getItemCount() {
        return   clothListRecycles.size() ;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView cl_serviceTv,cate_tv,quantityTv,amt_tb;
        Adapter.OnNoteListener onNoteListener ;
        public ViewHolder(@NonNull View itemView, Adapter.OnNoteListener onClickListener) {
            super(itemView);
            cl_serviceTv = itemView.findViewById(R.id.cl_serviceTv);
            cate_tv = itemView.findViewById(R.id.cl_cateTv);
            quantityTv = itemView.findViewById(R.id.quantityTv);
            amt_tb = itemView.findViewById(R.id.amt_tb);
        }
        public void setData(String ser, String cate, String quan, String amt){
            cl_serviceTv.setText(ser);
            cate_tv.setText(cate);
            quantityTv.setText(quan);
            amt_tb.setText(amt);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }
    public interface OnNoteListener {
        void onNoteClick(int pos) ;
    }

}
