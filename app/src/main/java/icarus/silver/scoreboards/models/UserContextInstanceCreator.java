package icarus.silver.scoreboards.models;

import android.content.Context;

import com.google.gson.InstanceCreator;

import java.lang.reflect.Type;

public class UserContextInstanceCreator implements InstanceCreator<Usuario> {
    private Context context;

    public UserContextInstanceCreator(Context context) {
        this.context = context;
    }

    @Override
    public Usuario createInstance(Type type) {
        // create new object with our additional property
        Usuario userContext = new Usuario(context);

        // return it to gson for further usage
        return userContext;
    }
}

