package icarus.silver.scoreboards;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dunst.check.CheckableImageButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;

import icarus.silver.scoreboards.adapters.ComentarioAdapter;
import icarus.silver.scoreboards.models.Comentario;
import icarus.silver.scoreboards.models.ComentarioContextInstanceCreator;
import icarus.silver.scoreboards.models.Logro;
import icarus.silver.scoreboards.models.UserContextInstanceCreator;
import icarus.silver.scoreboards.models.Usuario;

public class LogroActivity extends AppCompatActivity{

    public long user;
    public Logro logro;
    public Usuario usuario;
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
                try{
                    final Comentario comentarioSelected = comentarioAdapter.getItemList().get(comentarios_rv.getChildAdapterPosition((View)v.getParent().getParent().getParent()));

                    Log.i("comentarioSelected",comentarioSelected.getIdUsuario()+" "+comentarioSelected.getContenido());
                    switch(v.getId()){
                        case R.id.boton_upvote: upvote(v,comentarioSelected);
                        break;

                        case R.id.boton_downvote: downvote(v,comentarioSelected);
                        break;

                        case R.id.icono_usuario: dialogousuario(comentarioSelected);
                        break;

                        default: Log.i("vote","Algo salio mal con los votos");
                        break;
                    }
                } catch(ClassCastException ignored){
                    //ignorar clicks en cosas que no sean lo que no controlamos
                }
            }
        });

        traerUsuario(user);
        traerComentarios();

    }

    /**
     * Se crea el menú y se infla en la vista.
     * @param menu el objeto menú
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logro_menu, menu);
        return true;
    }

    /**
     * Aquí se maneja las opciones del menú superior.
     * @param item el ítem seleccionado
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.crear_comentario:
                crearComentario();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Método que crea un comentario, se encarga de llamar al diálogo y hacer la petición posterio.
     */
    private void crearComentario() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LogroActivity.this);

        LayoutInflater inflater = LogroActivity.this.getLayoutInflater();

        View v = inflater.inflate(R.layout.dialogo_crear_comentario, null);

        builder.setView(v);

        Button aceptar = (Button) v.findViewById(R.id.aceptar_comentario);
        Button cancelar = (Button) v.findViewById(R.id.cancelar_comentario);
        final EditText contenido = (EditText) v.findViewById(R.id.contenido_coment);
        final String[] contenido_comen = new String[1];


        final AlertDialog alert = builder.create();

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contenido_comen[0] = contenido.getText().toString();
                llamarApiCrearComentario(contenido_comen[0],user,logro.getIdLogro());
                alert.dismiss();
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });


        alert.show();
    }

    /**
     * Método que hace una petición a la BD para crear un comentario.
     * @param contenido_coment el contenido del comentario
     * @param user el ID del usuario que lo comenta
     * @param idLogro el ID del logro en el que lo ha comentado
     */
    private void llamarApiCrearComentario(String contenido_coment, long user, int idLogro) {
        String url = "http://10.0.2.2/MINI_apijson/usuarios.php?accion=crearcomentario&user="+user+"&logro="+idLogro+"&contenido="+contenido_coment;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.get("estado").equals(true)){
                        Toast.makeText(LogroActivity.this,"Comentario creado con éxito", Toast.LENGTH_SHORT).show();
                        darExp(String.valueOf(usuario.getIdUsuario()),20);
                        traerComentarios();
                    }else{
                        Toast.makeText(LogroActivity.this,"Ha ocurrido un error al crear el comentario", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError",error.getMessage());
            }
        });
        queue.add(jsonObjectRequest);
    }

    /**
     * Método que crea un diálogo al pulsar sobre la imagen del usuario de cada comentario.
     * @param comentarioSelected el comentario sobre el que se ha actuado
     */
    private void dialogousuario(final Comentario comentarioSelected) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LogroActivity.this);
        if(usuario.getRol().equals("moderador")||usuario.getRol().equals("administrador")) {
            builder.setItems(R.array.acciones_usuario_avanzado, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (getResources().getStringArray(R.array.acciones_usuario_avanzado)[which]) {
                        case "Ver información":
                            info_usuario(comentarioSelected.getIdUsuario());
                            dialog.dismiss();
                            break;

                        case "Amonestar":
                            amonestar(comentarioSelected);
                            dialog.dismiss();
                            break;

                        case "Eliminar comentario":
                            eliminarComentario(comentarioSelected);
                            dialog.dismiss();
                            break;

                    }
                }
            }).setIcon(R.mipmap.logo_launcher)
                    .setTitle("Interacciones de comentario")
                    .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }else{
            builder.setItems(R.array.acciones_usuario, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (getResources().getStringArray(R.array.acciones_usuario)[which]) {
                        case "Ver información":
                            info_usuario(comentarioSelected.getIdUsuario());
                            dialog.dismiss();
                            break;
                    }
                }
            })
                    .setIcon(R.mipmap.logo_launcher)
                    .setTitle("Interacciones de comentario")
                    .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }
        Dialog alert = builder.create();
        alert.show();
    }

    /**
     * Método que elimina un comentario, tanto de la BD como del recyclerview.
     * @param comentarioSelected el comentario a eliminar
     */
    private void eliminarComentario(Comentario comentarioSelected) {
        comentarios.remove(comentarioSelected);
        eliminarComentarioDB(comentarioSelected);
        actualizarLista();
    }

    /**
     * Método que abre la Actividad de usuario en modo información, para mostrar sólo los datos útiles al usuario.
     * @param usuarioComentarioSelected el ID del usuario
     */
    private void info_usuario(String usuarioComentarioSelected) {
        Intent intent = new Intent(LogroActivity.this,UsuarioActivity.class);
        intent.putExtra("user",Long.valueOf(usuarioComentarioSelected));
        intent.putExtra("infomode",true);
        startActivity(intent);
    }

    /**
     * Método que amonesta a un usuario al pulsar en el menú de interacciones del comentario.
     * @param comentarioSelected comentario sobre el que se actúa.
     */
    private void amonestar(Comentario comentarioSelected) {
        quitarExpDeAmonestar(comentarioSelected);
        amonestarUsuario(comentarioSelected);
    }

    /**
     * Petición a la BD para que amoneste a un usuario.
     * @param comentarioSelected el comentario sobre el que se hace la petición
     */
    private void amonestarUsuario(Comentario comentarioSelected){
        String url = "http://10.0.2.2/MINI_apijson/usuarios.php?accion=amonestar&user="+comentarioSelected.getIdUsuario();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.get("estado").equals(true)){
                        Toast.makeText(LogroActivity.this,"Usuario amonestado con éxito", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(LogroActivity.this,"Ha ocurrido un error al amonestar al usuario", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError",error.getMessage());
            }
        });
        queue.add(jsonObjectRequest);
    }

    /**
     * Petición a la BD para que elimine un comentario.
     * @param comentarioSelected el comentario sobre el que se hace la petición
     */
    private void eliminarComentarioDB(Comentario comentarioSelected){
        String url = "http://10.0.2.2/MINI_apijson/usuarios.php?accion=eliminarcomentario&comentario="+comentarioSelected.getIdComentario();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.get("estado").equals(true)){
                        Toast.makeText(LogroActivity.this,"Comentario eliminado con éxito", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(LogroActivity.this,"Ha ocurrido un error al eliminar el comentario", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError",error.getMessage());
            }
        });
        queue.add(jsonObjectRequest);
    }

    /**
     * Método especial para el caso de quitar EXP en amonestación.
     * @param comentario el comentario sobre el que se actúa
     */
    private void quitarExpDeAmonestar(Comentario comentario){
        if(comentario.getVecesAmonestado()==0){
            quitarExp(comentario.getIdUsuario(),15);
        }else if(comentario.getVecesAmonestado()==1){
            quitarExp(comentario.getIdUsuario(),30);
        }else if(comentario.getVecesAmonestado()>=2){
            quitarExp(comentario.getIdUsuario(),35);
        }
    }
    /**
     * Este método hace la lógica tras quitar experiencia a un usuario.
     * @param idUsuario el ID del usuario.
     * @param exp la cantidad de EXP a quitar.
     */
    private void quitarExp(final String idUsuario, final int exp) {
        String url = "http://10.0.2.2/MINI_apijson/usuarios.php?accion=quitarexp&user="+idUsuario+"&exp="+exp;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.get("estado").equals(true)){
                        Log.i("EXPsystem",exp+"retirados a"+usuario);
                    }else{
                        Log.i("EXPsystem","Algo salió mal");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError",error.getMessage());
            }
        });
        queue.add(jsonObjectRequest);
    }
    /**
     * Este método hace la lógica tras un voto positivo.
     * @param v la vista que ha llamado al método
     * @param comentario el comentario sobre el que se ha hecho el voto
     */
    private void upvote(View v,Comentario comentario) {
        if(comentario.getIdUsuario().equals(String.valueOf(user))){
            Toast.makeText(LogroActivity.this,"No puedes votar tus mismos comentarios",Toast.LENGTH_SHORT).show();
        }else {
            if ((((CheckableImageButton) v).isChecked())) {
                ((CheckableImageButton) v).setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_orange_24dp));
                upvoteDB(comentario);
                darExp(comentario.getIdUsuario(),20);
                darExp(String.valueOf(usuario.getIdUsuario()),25);
                comentario.setPuntuacion(comentario.getPuntuacion()+1);
            } else {
                ((CheckableImageButton) v).setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp));
                downvoteDB(comentario);
                comentario.setPuntuacion(comentario.getPuntuacion()-1);
            }
            actualizarLista();
        }
    }
    /**
     * Esta función hace la llamada a la BD para actualizar un voto positivo.
     * @param comentario el comentario sobre el que se hizo el voto positivo.
     */
    private void upvoteDB(Comentario comentario) {
        String url = "http://10.0.2.2/MINI_apijson/usuarios.php?accion=upvote&comentario="+comentario.getIdComentario()+"&usuario="+comentario.getIdUsuario();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.get("estado").equals(true)){
                        Log.i("vote","upvote registrado en DB");
                    }else{
                        Log.i("vote","Algo salió mal");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError",error.getMessage());
            }
        });
        queue.add(jsonObjectRequest);
    }

    /**
     * Esta función hace la llamada a la BD para actualizar un voto negativo.
     * @param comentario el comentario sobre el que se hizo el voto negativo.
     */
    private void downvoteDB(Comentario comentario) {
        String url = "http://10.0.2.2/MINI_apijson/usuarios.php?accion=downvote&comentario="+comentario.getIdComentario()+"&usuario="+comentario.getIdUsuario();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.get("estado").equals(true)){
                        Log.i("vote","downvote registrado en DB");
                    }else{
                        Log.i("vote","Algo salió mal");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError",error.getMessage());
            }
        });
        queue.add(jsonObjectRequest);
    }

    /**
     * Esta función hace la lógica tras dar experiencia a un usuario.
     * @param idUsuario el ID del usuario.
     * @param exp la cantidad de EXP a dar.
     */
    private void darExp(final String idUsuario, final int exp){
        String url = "http://10.0.2.2/MINI_apijson/usuarios.php?accion=darexp&user="+idUsuario+"&exp="+exp;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.get("estado").equals(true)){
                        Log.i("EXPsystem",exp+"otorgados a"+idUsuario);
                    }else{
                        Log.i("EXPsystem","Algo salió mal");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError",error.getMessage());
            }
        });
        queue.add(jsonObjectRequest);
    }

    /**
     * Este método trae el usuario actual logueado en la aplicación.
     * @param user el ID del usuario
     */
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
                                new UserContextInstanceCreator(LogroActivity.this));
                        Gson gson = gsonBuilder.create();
                        try {

                            usuario = gson.fromJson(response.getJSONObject(0).toString(),Usuario.class);

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

    /**
     * Este método hace la lógica tras un voto negativo.
     * @param v la vista que ha llamado al método
     * @param comentario el comentario sobre el que se ha hecho el voto
     */
    private void downvote(View v,Comentario comentario) {
        if(comentario.getIdUsuario().equals(String.valueOf(user))){
            Toast.makeText(LogroActivity.this,"No puedes votar tus mismos comentarios",Toast.LENGTH_SHORT).show();
        }else{
            if((((CheckableImageButton)v).isChecked())){
                ((CheckableImageButton) v).setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_orange_24dp));
                downvoteDB(comentario);
                quitarExp(comentario.getIdUsuario(),20);
                darExp(String.valueOf(usuario.getIdUsuario()),25);
                comentario.setPuntuacion(comentario.getPuntuacion()-1);
            }else{
                ((CheckableImageButton) v).setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black_24dp));
                upvoteDB(comentario);
                comentario.setPuntuacion(comentario.getPuntuacion()+1);
            }
            actualizarLista();
        }
    }

    public void actualizarLista(){
        comentarioAdapter.notifyDataSetChanged();
    }

    /**
     * Este método llama a la petición que trae los comentarios actuales en la base de datos
     */
    private void traerComentarios() {
        if(comentarios.size() > 0){
            comentarios.clear();
        }
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
                        gsonBuilder.disableHtmlEscaping(); //Intento de arreglar el bug de UTF-8
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
                                Comentario comentario = gson.fromJson(s,Comentario.class);
                                comentarioAdapter.addItemToItemList(comentario);
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
    protected void onDestroy() {
        super.onDestroy();
        queue.stop();
    }
}
