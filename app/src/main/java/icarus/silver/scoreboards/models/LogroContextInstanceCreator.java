package icarus.silver.scoreboards.models;

import android.content.Context;

import com.google.gson.InstanceCreator;

import java.lang.reflect.Type;

public class LogroContextInstanceCreator implements InstanceCreator<Logro> {
    private Context context;

    public LogroContextInstanceCreator(Context context) {
        this.context = context;
    }

    @Override
    public Logro createInstance(Type type) {
        // create new object with our additional property
        Logro logroContext = new Logro(context);

        // return it to gson for further usage
        return logroContext;
    }
}
