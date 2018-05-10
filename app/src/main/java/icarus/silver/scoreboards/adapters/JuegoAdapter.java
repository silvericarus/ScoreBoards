package icarus.silver.scoreboards.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import icarus.silver.scoreboards.R;
import icarus.silver.scoreboards.models.Juego;

public class JuegoAdapter extends RecyclerView.Adapter<JuegoAdapter.QuoteViewHolder> implements View.OnClickListener{

    private ArrayList<Juego> itemList;
    private View.OnClickListener mListener;


    public JuegoAdapter(ArrayList<Juego> itemList) {
        this.itemList = itemList;
    }

    public ArrayList<Juego> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<Juego> itemList) {
        this.itemList = itemList;
    }

    public void addItemToItemList(Juego juego){
        itemList.add(juego);
    }

    @Override
    public JuegoAdapter.QuoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_juego,parent,false);



        view.setOnClickListener(mListener);

        QuoteViewHolder viewHolder = new QuoteViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(JuegoAdapter.QuoteViewHolder holder, int position) {
        holder.bindQuoteItem(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        if(mListener != null){
            mListener.onClick(v);
        }
    }

    public class QuoteViewHolder extends RecyclerView.ViewHolder {

        private TextView titulojuego;
        private ImageView imagenjuego;


        public QuoteViewHolder(View itemView) {
            super(itemView);

            titulojuego    = (TextView)    itemView.findViewById(R.id.titulojuego);
            imagenjuego = (ImageView)   itemView.findViewById(R.id.imagenjuego);
        }

        public void bindQuoteItem(Juego item){
            titulojuego.setText(item.getNombre());
            Picasso.with(item.getContext()).load(item.getImagen()).into(imagenjuego);
        }

    }
}
