package icarus.silver.scoreboards.models;

import android.content.Context;

import com.google.gson.InstanceCreator;

import java.lang.reflect.Type;

public class PlayerstatsContextInstanceCreator implements InstanceCreator<Playerstats> {
    private Context context;

    public PlayerstatsContextInstanceCreator(Context context) {
        this.context = context;
    }

    @Override
    public Playerstats createInstance(Type type) {
        // create new object with our additional property
        Playerstats playerstatsContext = new Playerstats(context);

        // return it to gson for further usage
        return playerstatsContext;
    }
}
