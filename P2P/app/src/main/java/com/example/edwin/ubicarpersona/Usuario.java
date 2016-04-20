package com.example.edwin.ubicarpersona;

public class Usuario {
    String nombre;
    String mac;
    String ubicacion;
    String fecha;

    public Usuario(String nombre, String mac, String ubicacion, String fecha){
        this.nombre=nombre;
        this.mac=mac;
        this.ubicacion=ubicacion;
        this.fecha=fecha;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
