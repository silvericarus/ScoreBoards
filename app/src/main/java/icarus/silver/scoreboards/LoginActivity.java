package icarus.silver.scoreboards;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import icarus.silver.scoreboards.models.Usuario;

public class LoginActivity extends AppCompatActivity {

    private ImageButton steamLoginBtn;
    private RequestQueue queue;
    public long user;

    private String url = "http://10.0.2.2/MINI_apijson/usuarios.php?accion=loginusuario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        queue = Volley.newRequestQueue(LoginActivity.this);
        queue.start();
        steamLoginBtn = (ImageButton) findViewById(R.id.steamLogin);

        steamLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                        (Request.Method.GET,url,null , new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                //Aquí se parsearía el usuario que devolviera la petición a un objeto
                                // de clase Usuario
                                Gson gson = new Gson();
                                try {

                                    Usuario usuariologeado = gson.fromJson(response.getJSONObject(0).toString(),Usuario.class);
                                    Log.i("usuario", String.valueOf(usuariologeado.getIdUsuario()));
                                    user = usuariologeado.getIdUsuario();
                                    queue.stop();

                                    Intent intent = new Intent(LoginActivity.this,UsuarioActivity.class);
                                    intent.putExtra("user",user);
                                    startActivity(intent);

                                } catch (JSONException e) {
                                    Toast.makeText(LoginActivity.this,"Se ha producido un error al obtener el usuario",Toast.LENGTH_LONG).show();
                                    Log.e("GsonError",e.getMessage());
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(LoginActivity.this,"Se ha producido un error",Toast.LENGTH_LONG).show();
                                Log.e("VolleyError",error.toString());
                            }
                        });
                queue.add(jsonArrayRequest);
            }
        });

    }
}
