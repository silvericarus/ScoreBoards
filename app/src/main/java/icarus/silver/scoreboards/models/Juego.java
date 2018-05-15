package icarus.silver.scoreboards.models;

import android.content.Context;

import java.io.Serializable;

public class Juego implements Serializable {

    private long idJuego;
    private String nombre;
    private float precio;
    private int rebaja;
    private String imagen;

    private transient Context context;

    public Juego(int idJuego, String nombre, float precio, int rebaja, String imagen) {
        this.idJuego = idJuego;
        this.nombre = nombre;
        this.precio = precio;
        this.rebaja = rebaja;
        this.imagen = imagen;
    }

    public Juego(Context context) {
        this.context = context;
    }

    public Juego() {
    }

    public long getIdJuego() {
        return idJuego;
    }

    public void setIdJuego(long idJuego) {
        this.idJuego = idJuego;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getRebaja() {
        return rebaja;
    }

    public void setRebaja(int rebaja) {
        this.rebaja = rebaja;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
