
package icarus.silver.scoreboards.models;

import android.content.Context;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogrosDesbloqueados implements Serializable
{

    @SerializedName("playerstats")
    @Expose
    private Playerstats playerstats;
    private final static long serialVersionUID = -5195828872040786425L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public LogrosDesbloqueados() {
    }

    /**
     * 
     * @param playerstats
     */
    public LogrosDesbloqueados(Playerstats playerstats) {
        super();
        this.playerstats = playerstats;
    }

    public Playerstats getPlayerstats() {
        return playerstats;
    }

    public void setPlayerstats(Playerstats playerstats) {
        this.playerstats = playerstats;
    }

    public Context context;

    public LogrosDesbloqueados(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
