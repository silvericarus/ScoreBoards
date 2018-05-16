package icarus.silver.scoreboards.models;

import android.content.Context;

import com.google.gson.InstanceCreator;

import java.lang.reflect.Type;

public class LogrosDesbloqueadosContextInstanceCreator implements InstanceCreator<LogrosDesbloqueados> {
    private Context context;

    public LogrosDesbloqueadosContextInstanceCreator(Context context) {
        this.context = context;
    }

    @Override
    public LogrosDesbloqueados createInstance(Type type) {
        // create new object with our additional property
        LogrosDesbloqueados logrosDesbloqueadosContext = new LogrosDesbloqueados(context);

        // return it to gson for further usage
        return logrosDesbloqueadosContext;
    }
}
