package icarus.silver.scoreboards.models;

import android.content.Context;

import com.google.gson.annotations.Expose;

public class Logro {

    @Expose
    private int idLogro;

    @Expose
    private String nombre;

    @Expose
    private String imagen;

    @Expose
    private String imagengrey;

    @Expose
    private String descripcion;

    @Expose
    private int idJuego;

    public transient Context context;

    public Logro(int idLogro, String nombre, String imagen, String imagengrey, String descripcion, int idJuego) {
        this.idLogro = idLogro;
        this.nombre = nombre;
        this.imagen = imagen;
        this.imagengrey = imagengrey;
        this.descripcion = descripcion;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdJuego() {
        return idJuego;
    }

    public void setIdJuego(int idJuego) {
        this.idJuego = idJuego;
    }
}
