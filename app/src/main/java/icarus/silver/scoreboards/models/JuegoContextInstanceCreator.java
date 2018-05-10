package icarus.silver.scoreboards.models;

import android.content.Context;

import com.google.gson.InstanceCreator;

import java.lang.reflect.Type;

public class JuegoContextInstanceCreator implements InstanceCreator<Juego> {
    private Context context;

    public JuegoContextInstanceCreator(Context context) {
        this.context = context;
    }

    @Override
    public Juego createInstance(Type type) {
        // create new object with our additional property
        Juego juegoContext = new Juego(context);

        // return it to gson for further usage
        return juegoContext;
    }
}

