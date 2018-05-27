package icarus.silver.scoreboards.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import icarus.silver.scoreboards.JuegoActivity;
import icarus.silver.scoreboards.R;
import icarus.silver.scoreboards.models.Achievement;
import icarus.silver.scoreboards.models.Logro;
import icarus.silver.scoreboards.models.LogrosDesbloqueados;

public class LogroAdapter extends RecyclerView.Adapter<LogroAdapter.LogroViewHolder> implements View.OnClickListener,View.OnLongClickListener {
    private ArrayList<Logro> itemList;
    private View.OnClickListener mListener;
    private View.OnLongClickListener mLongListener;
    private Context context;
    LogroViewHolder viewHolder;



    public LogroAdapter(ArrayList<Logro> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
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

        view.setOnLongClickListener(mLongListener);

        viewHolder = new LogroViewHolder(view,context);

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
        private Context mContext;


        public LogroViewHolder(View itemView,Context mContext) {

            super(itemView);

            logo_logro = (ImageView)   itemView.findViewById(R.id.logo_logro);

            this.mContext = mContext;
        }

        public void bindQuoteItem(Logro item) {
            int contador=0;
            try {
                for (Achievement achievement : ((JuegoActivity)mContext).logrosDesbloqueados.getPlayerstats().getAchievements()) {
                    Log.d("logroDesbloqueado",achievement.getName());
                    if (item.getApiname().equals(achievement.getName())) {
                        Log.d("coincidencia",achievement.getName()+""+item.getApiname());
                        Picasso.with(item.getContext()).load(item.getImagen()).into(logo_logro);
                        contador++;
                        break;
                    } else {
                        if(contador < 6) {
                            Log.d("coincidencia", "No coincidencia en logro "+item.getApiname());
                            Picasso.with(item.getContext()).load(item.getImagengrey()).into(logo_logro);
                        }else{
                            break;
                        }
                    }
                }
            }catch(NullPointerException ex){
                Picasso.with(item.getContext()).load(item.getImagengrey()).into(logo_logro);
                Log.w("BindWARN","WARN...Bindeo fallido "+ ex.getMessage());
                ((JuegoActivity)mContext).getLogrosDesbloqueados();
                ((JuegoActivity)mContext).logrosAdapter.notifyDataSetChanged();
            }
        }
    }
}

