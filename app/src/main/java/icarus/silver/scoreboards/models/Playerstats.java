
package icarus.silver.scoreboards.models;

import android.content.Context;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Playerstats implements Serializable
{

    @SerializedName("steamID")
    @Expose
    private String steamID;
    @SerializedName("gameName")
    @Expose
    private String gameName;
    @SerializedName("stats")
    @Expose
    private List<Stat> stats = null;
    @SerializedName("achievements")
    @Expose
    private List<Achievement> achievements = null;
    private final static long serialVersionUID = 1250302904752198733L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Playerstats() {
    }

    /**
     * 
     * @param achievements
     * @param stats
     * @param steamID
     * @param gameName
     */
    public Playerstats(String steamID, String gameName, List<Stat> stats, List<Achievement> achievements) {
        super();
        this.steamID = steamID;
        this.gameName = gameName;
        this.stats = stats;
        this.achievements = achievements;
    }

    public String getSteamID() {
        return steamID;
    }

    public void setSteamID(String steamID) {
        this.steamID = steamID;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public List<Stat> getStats() {
        return stats;
    }

    public void setStats(List<Stat> stats) {
        this.stats = stats;
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<Achievement> achievements) {
        this.achievements = achievements;
    }

    public Context context;

    public Playerstats(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}
