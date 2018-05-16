package icarus.silver.scoreboards.models;

import android.content.Context;

import com.google.gson.InstanceCreator;

import java.lang.reflect.Type;

public class AchievementContextInstanceCreator implements InstanceCreator<Achievement> {
    private Context context;

    public AchievementContextInstanceCreator(Context context) {
        this.context = context;
    }

    @Override
    public Achievement createInstance(Type type) {
        // create new object with our additional property
        Achievement logroContext = new Achievement(context);

        // return it to gson for further usage
        return logroContext;
    }
}
