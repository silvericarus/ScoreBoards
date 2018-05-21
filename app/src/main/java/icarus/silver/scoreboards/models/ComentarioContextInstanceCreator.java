package icarus.silver.scoreboards.models;

import android.content.Context;

import com.google.gson.InstanceCreator;

import java.lang.reflect.Type;

public class ComentarioContextInstanceCreator implements InstanceCreator<Comentario> {
    private Context context;

    public ComentarioContextInstanceCreator(Context context) {
        this.context = context;
    }

    @Override
    public Comentario createInstance(Type type) {
        // create new object with our additional property
        Comentario comentarioContext = new Comentario(context);

        // return it to gson for further usage
        return comentarioContext;
    }
}

