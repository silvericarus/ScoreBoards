package icarus.silver.scoreboards.models;

import android.content.Context;

import com.google.gson.annotations.Expose;

public class Logro {

    private int idLogro;

    private String nombre;

    private String apiname;

    private String imagen;

    private String imagengrey;

    private int idJuego;

    public transient Context context;

    public Logro(int idLogro, String nombre, String apiname, String imagen, String imagengrey, int idJuego) {
        this.idLogro = idLogro;
        this.nombre = nombre;
        this.apiname = apiname;
        this.imagen = imagen;
        this.imagengrey = imagengrey;
        this.idJuego = idJuego;
    }

    public Logro(Context context) {
        this.context = context;
    }

    public Logro() {
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getIdLogro() {
        return idLogro;
    }

    public void setIdLogro(int idLogro) {
        this.idLogro = idLogro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getImagengrey() {
        return imagengrey;
    }

    public void setImagengrey(String imagengrey) {
        this.imagengrey = imagengrey;
    }


    public int getIdJuego() {
        return idJuego;
    }

    public void setIdJuego(int idJuego) {
        this.idJuego = idJuego;
    }

    public String getApiname() {
        return apiname;
    }

    public void setApiname(String apiname) {
        this.apiname = apiname;
    }
}
