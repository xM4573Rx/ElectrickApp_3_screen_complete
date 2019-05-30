package com.firebase.electrickapp_fb.transfer;

public class User {

    private String Uid;
    private String Watt;
    private String WattHora;
    private String Proyeccion;
    private String Nombre;

    public User() {
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getWatt() {
        return Watt;
    }

    public void setWatt(String watt) {
        Watt = watt;
    }

    public String getWattHora() {
        return WattHora;
    }

    public void setWattHora(String wattHora) {
        WattHora = wattHora;
    }

    public String getProyeccion() {
        return Proyeccion;
    }

    public void setProyeccion(String proyeccion) {
        Proyeccion = proyeccion;
    }
}
