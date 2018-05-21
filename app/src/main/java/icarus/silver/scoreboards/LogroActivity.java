package icarus.silver.scoreboards;

import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dunst.check.CheckableImageButton;
import com.github.xizzhu.simpletooltip.ToolTip;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import icarus.silver.scoreboards.adapters.ComentarioAdapter;
import icarus.silver.scoreboards.models.Comentario;
import icarus.silver.scoreboards.models.ComentarioContextInstanceCreator;
import icarus.silver.scoreboards.models.Logro;

public class LogroActivity extends AppCompatActivity implements Comparator<Comentario>{

    public long user;
    public Logro logro;
    private ImageView icon_logro;
    private TextView nombre_logro;
    private TextView descripcion_logro;
    private RecyclerView comentarios_rv;
    public RequestQueue queue;
    ArrayList<Comentario> comentarios = new ArrayList<>();
    ComentarioAdapter comentarioAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logro);

        Intent intent = getIntent();
        logro = (Logro)intent.getSerializableExtra("logro");
        user = intent.getLongExtra("user",0);

        icon_logro = (ImageView)findViewById(R.id.icon_logro);
        nombre_logro = (TextView)findViewById(R.id.nombre_logro);
        descripcion_logro = (TextView)findViewById(R.id.descripcion_logro);

        Picasso.with(LogroActivity.this).load(logro.getImagen()).into(icon_logro);
        nombre_logro.setText(logro.getNombre());
        descripcion_logro.setText(logro.getDescripcion());
        queue = Volley.newRequestQueue(LogroActivity.this);
        queue.start();


        comentarios_rv = (RecyclerView)findViewById(R.id.comentarios_rv);
        comentarioAdapter = new ComentarioAdapter(comentarios,LogroActivity.this);
        comentarios_rv.setAdapter(comentarioAdapter);
        comentarios_rv.setLayoutManager(new LinearLayoutManager(LogroActivity.this));
        comentarioAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Comentario comentarioSelected = comentarioAdapter.getItemList().get(comentarios_rv.getChildAdapterPosition((View)v.getParent().getParent().getParent()));

                Log.i("comentarioSelected",comentarioSelected.getIdUsuario()+" "+comentarioSelected.getContenido());
                switch(v.getId()){
                    case R.id.boton_upvote: upvote(v,comentarioSelected);
                    break;

                    case R.id.boton_downvote: downvote(v,comentarioSelected);
                    break;

                    default: Log.i("vote","Algo salio mal con los votos");
                    break;
                }
            }
        });


        traerComentarios();

    }

    private void upvote(View v,Comentario comentario) {
        Log.i("comentario",comentario.getIdUsuario());
        if(comentario.getIdUsuario()==String.valueOf(user)){
            Toast.makeText(LogroActivity.this,"No puedes votar tus mismos comentarios",Toast.LENGTH_SHORT).show();
        }else {
            if (!((CheckableImageButton) v).isChecked()) {
                ((CheckableImageButton) v).setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp));
                comentario.setPuntuacion(comentario.getPuntuacion() -1);
            } else {
                ((CheckableImageButton) v).setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_orange_24dp));
                comentario.setPuntuacion(comentario.getPuntuacion() + 1);
            }
            actualizarLista();
        }
    }

    private void downvote(View v,Comentario comentario) {
        if(comentario.getIdUsuario()==String.valueOf(user)){
            Toast.makeText(LogroActivity.this,"No puedes votar tus mismos comentarios",Toast.LENGTH_SHORT).show();
        }else{
            if(!((CheckableImageButton)v).isChecked()){
                ((CheckableImageButton) v).setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black_24dp));
                comentario.setPuntuacion(comentario.getPuntuacion()+1);
            }else{
                ((CheckableImageButton) v).setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_orange_24dp));
                comentario.setPuntuacion(comentario.getPuntuacion()-1);
            }
            actualizarLista();
        }
    }

    public void actualizarLista(){
        comentarios.sort(LogroActivity.this);
        comentarioAdapter.notifyDataSetChanged();
    }

    private void traerComentarios() {
        String url = "http://10.0.2.2/MINI_apijson/usuarios.php?accion=selectcomentarios&logro="+logro.getIdLogro();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET,url,null , new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Aquí se parsearía el usuario que devolviera la petición a un objeto
                        // de clase Comentario
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.registerTypeAdapter(
                                Comentario.class,
                                new ComentarioContextInstanceCreator(LogroActivity.this));
                        Gson gson = gsonBuilder.create();
                        try {
                            ArrayList<String> listdata = new ArrayList<String>();
                            JSONArray jArray = (JSONArray)response;
                            if (jArray != null) {
                                for (int i=0;i<jArray.length();i++){
                                    listdata.add(jArray.getString(i));
                                }
                            }
                            for(String s:listdata){
                                Comentario juego = gson.fromJson(s,Comentario.class);
                                comentarioAdapter.addItemToItemList(juego);
                                actualizarLista();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(LogroActivity.this,"Se ha producido un error al obtener el usuario",Toast.LENGTH_LONG).show();
                            Log.e("GsonError",e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LogroActivity.this,"Se ha producido un error",Toast.LENGTH_LONG).show();
                        Log.e("VolleyError",error.toString());
                    }
                });
        queue.add(jsonArrayRequest);
    }

    @Override
    public int compare(Comentario o1, Comentario o2) {
        if (o1.getPuntuacion() < o2.getPuntuacion()){
            return 1;
        }
        if (o1.getPuntuacion() > o2.getPuntuacion()){
            return -1;
        }
        return 0;
    }
}
