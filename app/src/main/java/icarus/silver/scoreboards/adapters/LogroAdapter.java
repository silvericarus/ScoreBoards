package icarus.silver.scoreboards.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import icarus.silver.scoreboards.R;
import icarus.silver.scoreboards.models.Logro;

public class LogroAdapter extends RecyclerView.Adapter<LogroAdapter.LogroViewHolder> implements View.OnClickListener,View.OnLongClickListener {
    private ArrayList<Logro> itemList;
    private View.OnClickListener mListener;
    private View.OnLongClickListener mLongListener;


    public LogroAdapter(ArrayList<Logro> itemList) {
        this.itemList = itemList;
    }

    public ArrayList<Logro> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<Logro> itemList) {
        this.itemList = itemList;
    }

    public void addItemToItemList(Logro logro){
        itemList.add(logro);
    }

    @Override
    public LogroAdapter.LogroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_logro,parent,false);



        view.setOnClickListener(mListener);

        LogroViewHolder viewHolder = new LogroViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LogroAdapter.LogroViewHolder holder, int position) {
        holder.bindQuoteItem(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.mListener = listener;
    }

    public void setOnLongClickListener(View.OnLongClickListener listener){ this.mLongListener = listener; }

    @Override
    public void onClick(View v) {
        if(mListener != null){
            mListener.onClick(v);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if(mLongListener != null){
            mLongListener.onLongClick(v);
            return true;
        }else {
            return false;
        }
    }

    public class LogroViewHolder extends RecyclerView.ViewHolder {

        private ImageView logo_logro;


        public LogroViewHolder(View itemView) {

            super(itemView);

            logo_logro = (ImageView)   itemView.findViewById(R.id.logo_logro);
        }

        public void bindQuoteItem(Logro item) {
            Log.i("imagen", item.getImagengrey());
            Picasso.with(item.getContext()).load(item.getImagengrey()).into(logo_logro);
        }
    }

}
