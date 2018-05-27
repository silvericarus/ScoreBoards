package icarus.silver.scoreboards.models;

import android.content.Context;

public class Comentario{

    private int idComentario;

    private String contenido;

    private int puntuacion;

    private String fotodeperfil;

    private String idUsuario;

    private int vecesAmonestado;

    private Context context;

    public Comentario() {
    }

    public Comentario(int idComentario, String contenido, int puntuacion, String fotodeperfil, String idUsuario, int vecesAmonestado) {
        this.idComentario = idComentario;
        this.contenido = contenido;
        this.puntuacion = puntuacion;
        this.fotodeperfil = fotodeperfil;
        this.idUsuario = idUsuario;
        this.vecesAmonestado = vecesAmonestado;
    }

    public Comentario(Context context) {
        this.context = context;
    }

    public int getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(int idComentario) {
        this.idComentario = idComentario;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getFotodeperfil() {
        return fotodeperfil;
    }

    public void setFotodeperfil(String fotodeperfil) {
        this.fotodeperfil = fotodeperfil;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getVecesAmonestado() {
        return vecesAmonestado;
    }

    public void setVecesAmonestado(int vecesAmonestado) {
        this.vecesAmonestado = vecesAmonestado;
    }
}
