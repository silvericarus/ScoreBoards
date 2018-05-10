package icarus.silver.scoreboards.models;

import android.content.Context;

public class Usuario {
    long idUsuario;
    String nick;
    String fotodeperfil;
    String descripcion;
    String rol;
    int nivel;
    int amonestado;

    private Context context;

    public Usuario(long idUsuario, String nick, String fotodeperfil, String descripcion, String rol, int nivel, int amonestado) {
        this.idUsuario = idUsuario;
        this.nick = nick;
        this.fotodeperfil = fotodeperfil;
        this.descripcion = descripcion;
        this.rol = rol;
        this.nivel = nivel;
        this.amonestado = amonestado;
    }

    public Usuario() {
    }

    public Usuario(Context context) {
        this.context = context;
    }

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getFotodeperfil() {
        return fotodeperfil;
    }

    public void setFotodeperfil(String fotodeperfil) {
        this.fotodeperfil = fotodeperfil;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int isAmonestado() {
        return amonestado;
    }

    public void setAmonestado(int amonestado) {
        this.amonestado = amonestado;
    }
}
