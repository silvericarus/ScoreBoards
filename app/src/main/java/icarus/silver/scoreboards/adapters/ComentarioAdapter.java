package icarus.silver.scoreboards.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dunst.check.CheckableImageButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import icarus.silver.scoreboards.R;
import icarus.silver.scoreboards.models.Comentario;

public class ComentarioAdapter extends RecyclerView.Adapter<ComentarioAdapter.ComentarioViewHolder> implements View.OnClickListener{
    private ArrayList<Comentario> itemList;
    private View.OnClickListener mListener;
    private Context context;



    public ComentarioAdapter(ArrayList<Comentario> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    public ArrayList<Comentario> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<Comentario> itemList) {
        this.itemList = itemList;
    }

    public void addItemToItemList(Comentario comentario){
        itemList.add(comentario);
    }

    @Override
    public ComentarioAdapter.ComentarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comentario,parent,false);



        view.setOnClickListener(mListener);


        ComentarioViewHolder viewHolder = new ComentarioViewHolder(view,context);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ComentarioAdapter.ComentarioViewHolder holder, int position) {
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



    public class ComentarioViewHolder extends RecyclerView.ViewHolder {

        private ImageButton icono_usuario;
        private TextView contenido;
        private CheckableImageButton upvote_btn;
        private CheckableImageButton downvote_btn;
        private TextView puntuacion_comentario;
        private Context mContext;


        public ComentarioViewHolder(View itemView,Context mContext) {

            super(itemView);

            icono_usuario = (ImageButton)itemView.findViewById(R.id.icono_usuario);
            contenido = (TextView)itemView.findViewById(R.id.contenido_coment);
            upvote_btn = (CheckableImageButton)itemView.findViewById(R.id.boton_upvote);
            downvote_btn = (CheckableImageButton)itemView.findViewById(R.id.boton_downvote);
            puntuacion_comentario = (TextView)itemView.findViewById(R.id.puntuación_comentario);

            this.mContext = mContext;
        }

        public void bindQuoteItem(Comentario item) {

            Picasso.with(mContext).load(item.getFotodeperfil()).into(icono_usuario);
            contenido.setText(item.getContenido());
            upvote_btn.setOnClickListener(ComentarioAdapter.this);
            downvote_btn.setOnClickListener(ComentarioAdapter.this);
            icono_usuario.setOnClickListener(ComentarioAdapter.this);
            puntuacion_comentario.setText((String.valueOf(item.getPuntuacion())));
            if(Integer.parseInt(puntuacion_comentario.getText().toString())<0){
                puntuacion_comentario.setTextColor(Color.RED);
            }else{
                puntuacion_comentario.setTextColor(Color.BLACK);
            }

        }
    }
}

