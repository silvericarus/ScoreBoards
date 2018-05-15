package icarus.silver.scoreboards;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.xizzhu.simpletooltip.ToolTip;
import com.github.xizzhu.simpletooltip.ToolTipView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import icarus.silver.scoreboards.adapters.LogroAdapter;
import icarus.silver.scoreboards.models.Juego;
import icarus.silver.scoreboards.models.Logro;
import icarus.silver.scoreboards.models.LogroContextInstanceCreator;

public class JuegoActivity extends AppCompatActivity {

    private ImageView header_juego;
    private TextView titulo_juego;
    private RecyclerView logros_rv;
    private LogroAdapter logrosAdapter;
    public ArrayList<Logro> logroList = new ArrayList<Logro>();
    public Juego juego;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        header_juego = (ImageView)findViewById(R.id.header_juego);
        titulo_juego = (TextView)findViewById(R.id.titulo_juego);
        logros_rv = (RecyclerView)findViewById(R.id.logros_rv);

        Intent intent = getIntent();
        juego = (Juego) intent.getSerializableExtra("juego");

        Picasso.with(JuegoActivity.this).load(juego.getImagen()).into(header_juego);
        titulo_juego.setText(juego.getNombre());

        queue = Volley.newRequestQueue(JuegoActivity.this);
        queue.start();

        logrosAdapter = new LogroAdapter(logroList);
        logros_rv.setAdapter(logrosAdapter);
        logros_rv.setLayoutManager(new GridLayoutManager(this,3));
        logrosAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Logro logroSelected = logrosAdapter.getItemList().get(logros_rv.getChildAdapterPosition(v));
                ToolTip toolTip = new ToolTip.Builder()
                        .withText(logroSelected.getNombre())
                        .withBackgroundColor(getResources().getColor(R.color.colorAccent))
                        .withCornerRadius(10)
                        .build();
                ToolTipView toolTipView = new ToolTipView.Builder(JuegoActivity.this)
                        .withAnchor(v)
                        .withToolTip(toolTip)
                        .withGravity(Gravity.TOP)
                        .build();
                toolTipView.show();
            }
        });

        cargarLogros();

    }

    private void cargarLogros() {
        String url = "http://10.0.2.2/MINI_apijson/usuarios.php?accion=getlogros&juego="+juego.getIdJuego();
        Log.i("juego", juego.getNombre());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET,url,null , new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Aquí se parsearía el usuario que devolviera la petición a un objeto
                        // de clase Logro
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.registerTypeAdapter(
                                Logro.class,
                                new LogroContextInstanceCreator(JuegoActivity.this));
                        Gson gson = gsonBuilder.create();
                        try {
                            if(response != null) {
                                ArrayList<String> listdata = new ArrayList<String>();
                                JSONArray jArray = (JSONArray) response;
                                if (jArray != null) {
                                    for (int i = 0; i < jArray.length(); i++) {
                                        listdata.add(jArray.getString(i));
                                    }
                                }
                                for (String s : listdata) {
                                    Logro logro = gson.fromJson(s, Logro.class);
                                    Log.i("logro", logro.getNombre());
                                    logrosAdapter.addItemToItemList(logro);
                                    logrosAdapter.notifyDataSetChanged();
                                }
                            }else{
                                throw new JSONException("No se ha recibido ninguna respuesta de la Base de Datos");
                            }
                        } catch (JSONException e) {
                            Toast.makeText(JuegoActivity.this,"Se ha producido un error al obtener los logros",Toast.LENGTH_LONG).show();
                            Log.e("GsonError",e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(JuegoActivity.this,"Se ha producido un error",Toast.LENGTH_LONG).show();
                        Log.e("VolleyError",error.toString());
                    }
                });
        queue.add(jsonArrayRequest);
    }
}
