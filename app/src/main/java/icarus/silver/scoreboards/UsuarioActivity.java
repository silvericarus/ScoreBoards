package icarus.silver.scoreboards;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import icarus.silver.scoreboards.adapters.JuegoAdapter;
import icarus.silver.scoreboards.models.Usuario;
import icarus.silver.scoreboards.models.*;

public class UsuarioActivity extends AppCompatActivity {

    public long user;
    private RequestQueue queue;
    public Usuario usuarioActual;
    private RecyclerView mJuegoList;
    private JuegoAdapter mJuegoAdapter;
    private ArrayList<Juego> juegoList = new ArrayList<Juego>();
    String url = "http://10.0.2.2/MINI_apijson/usuarios.php?accion=loginusuario";
    private TextView nick;
    private TextView descripcion;
    private TextView nivel;
    private ImageView userimage;
    private ImageView logoUsuario;
    private CardView userCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        Intent intent = getIntent();
        user = intent.getLongExtra("user",0);

        mJuegoList = (RecyclerView)findViewById(R.id.listaJuegos);
        mJuegoAdapter = new JuegoAdapter(juegoList);
        mJuegoList.setLayoutManager(new LinearLayoutManager(UsuarioActivity.this));
        mJuegoList.setAdapter(mJuegoAdapter);
        mJuegoAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Juego juegoSelected = mJuegoAdapter.getItemList().get(mJuegoList.getChildAdapterPosition(v));
                Intent intent1 = new Intent(UsuarioActivity.this,JuegoActivity.class);
                intent1.putExtra("juego",juegoSelected);
                startActivity(intent1);
            }
        });

        usuarioActual = new Usuario();
        nick = (TextView)findViewById(R.id.nick);
        descripcion = (TextView)findViewById(R.id.descripcion);
        nivel = (TextView)findViewById(R.id.nivel);
        userimage = (ImageView)findViewById(R.id.userimage);
        logoUsuario = (ImageView)findViewById(R.id.logo_usuario);
        userCardView = (CardView)findViewById(R.id.userCardView);

        queue = Volley.newRequestQueue(UsuarioActivity.this);
        queue.start();

        traerUsuario(user);
        rellenarJuegos();

    }

    public void traerUsuario(long user){
        String url = "http://10.0.2.2/MINI_apijson/usuarios.php?accion=selectuser&user="+user;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET,url,null , new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Aquí se parsearía el usuario que devolviera la petición a un objeto
                        // de clase Usuario
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.registerTypeAdapter(
                                Usuario.class,
                                new UserContextInstanceCreator(UsuarioActivity.this));
                        Gson gson = gsonBuilder.create();
                        try {

                            usuarioActual = gson.fromJson(response.getJSONObject(0).toString(),Usuario.class);
                            rellenarUsuario(nick,descripcion,nivel,userimage,logoUsuario);

                        } catch (JSONException e) {
                            Toast.makeText(UsuarioActivity.this,"Se ha producido un error al obtener el usuario",Toast.LENGTH_LONG).show();
                            Log.e("GsonError",e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UsuarioActivity.this,"Se ha producido un error",Toast.LENGTH_LONG).show();
                        Log.e("VolleyError",error.toString());
                    }
                });
        queue.add(jsonArrayRequest);
    }

    public void rellenarUsuario(TextView nick,TextView descripcion,TextView nivel,ImageView userimage,ImageView logoUsuario){
        nick.setText(usuarioActual.getNick());
        Log.i("nick",""+usuarioActual.getNick());
        descripcion.setText(usuarioActual.getDescripcion());
        nivel.setText(Integer.toString(usuarioActual.getNivel()));
        //Ponemos la imagen de perfil con Picasso
        Picasso.with(UsuarioActivity.this).load(usuarioActual.getFotodeperfil()).into(userimage);

        if(usuarioActual.isAmonestado()==1){
            logoUsuario.setImageDrawable(getDrawable(R.drawable.ic_warning_red_24dp));
            userCardView.setCardBackgroundColor(Color.rgb(244,67,54));
        }else{
            if(usuarioActual.getRol().equalsIgnoreCase("moderador")){
                logoUsuario.setImageDrawable(getDrawable(R.drawable.ic_security_orange_24dp));
            }else if(usuarioActual.getRol().equalsIgnoreCase("administrador")){
                logoUsuario.setImageDrawable(getDrawable(R.drawable.ic_grade_orange_24dp));
            }else{
                logoUsuario.setImageDrawable(getDrawable(R.drawable.ic_videogame_asset_black_24dp));
            }
        }
    }

    public void rellenarJuegos(){
        String url = "http://10.0.2.2/MINI_apijson/usuarios.php?accion=selectjuegoscompradospor&user="+user;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET,url,null , new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Aquí se parsearía el usuario que devolviera la petición a un objeto
                        // de clase Juego
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.registerTypeAdapter(
                                Juego.class,
                                new JuegoContextInstanceCreator(UsuarioActivity.this));
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
                                Juego juego = gson.fromJson(s,Juego.class);
                                Log.i("juego",juego.getNombre());
                                mJuegoAdapter.addItemToItemList(juego);
                                mJuegoAdapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(UsuarioActivity.this,"Se ha producido un error al obtener los juegos",Toast.LENGTH_LONG).show();
                            Log.e("GsonError",e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UsuarioActivity.this,"Se ha producido un error",Toast.LENGTH_LONG).show();
                        Log.e("VolleyError",error.toString());
                    }
                });
        queue.add(jsonArrayRequest);
    }
}
