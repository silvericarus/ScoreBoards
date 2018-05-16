package icarus.silver.scoreboards.models;

import android.content.Context;

import com.google.gson.InstanceCreator;

import java.lang.reflect.Type;

public class StatContextInstanceCreator implements InstanceCreator<Stat> {
    private Context context;

    public StatContextInstanceCreator(Context context) {
        this.context = context;
    }

    @Override
    public Stat createInstance(Type type) {
        // create new object with our additional property
        Stat statContext = new Stat(context);

        // return it to gson for further usage
        return statContext;
    }
}
