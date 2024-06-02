package com.mateomontero.pokepabellon.modelo;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String nombre,correo,passward,key;
    private Direccion direccion;


    public Usuario(String nombre, String correo, String passward) {

        this.nombre = nombre;
        this.correo = correo;
        this.passward = passward;

    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPassward() {
        return passward;
    }
    public void setPassward(String passward) {
        this.passward = passward;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        return nombre + ';' + correo + ';' +passward;
    }

    public Usuario (String txt){
        String[]data=txt.split(";");
        this.nombre=data[0];
        this.correo=data[1];
        this.passward=data[2];
    }
}
