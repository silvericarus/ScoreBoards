package icarus.silver.scoreboards;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import icarus.silver.scoreboards.models.Logro;

public class LogroActivity extends AppCompatActivity {

    public long user;
    public Logro logro;
    private ImageView icon_logro;
    private TextView nombre_logro;
    private TextView descripcion_logro;

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

    }
}
