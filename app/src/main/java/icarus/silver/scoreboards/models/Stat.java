
package icarus.silver.scoreboards.models;

import android.content.Context;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stat implements Serializable
{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("value")
    @Expose
    private int value;
    private final static long serialVersionUID = 5404858593607049630L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Stat() {
    }

    /**
     * 
     * @param name
     * @param value
     */
    public Stat(String name, int value) {
        super();
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Context context;

    public Stat(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}
