
package icarus.silver.scoreboards.models;

import android.content.Context;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Achievement implements Serializable
{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("achieved")
    @Expose
    private int achieved;
    private final static long serialVersionUID = 2688342483779443206L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Achievement() {
    }

    /**
     * 
     * @param achieved
     * @param name
     */
    public Achievement(String name, int achieved) {
        super();
        this.name = name;
        this.achieved = achieved;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAchieved() {
        return achieved;
    }

    public void setAchieved(int achieved) {
        this.achieved = achieved;
    }

    public Context context;

    public Achievement(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}
