package icarus.silver.scoreboards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import icarus.silver.scoreboards.models.Juego;

public class JuegoActivity extends AppCompatActivity {

    private ImageView header_juego;
    private TextView titulo_juego;
    private RecyclerView logros_rv;
    public Juego juego;

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
    }
}
