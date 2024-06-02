package com.mateomontero.pokepabellon.modelo;

import java.io.Serializable;

public class Direccion implements Serializable {
    String ciudad;
    String cp;
    String pais;
    String provincia;
    String calle;
    int id;
    String key;

    public Direccion(String ciudad, String cp, String pais, String provincia, String calle) {
        this.ciudad = ciudad;
        this.cp = cp;
        this.pais = pais;
        this.provincia = provincia;
        this.calle = calle;
    }

    public Direccion(String txt){
        String data[]=txt.split(";");
        this.ciudad = data[0];
        this.cp = data[1];
        this.pais = data[2];
        this.provincia = data[3];
        this.calle = data[4];


    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String toString(){
        return ciudad+";"+cp+";"+pais+";"+provincia+";"+calle;
    }
    public String toStringFront(){
        return ciudad+" "+cp+" "+pais+" "+provincia+" "+calle;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }
}
