package com.mateomontero.pokepabellon.modelo;

import java.io.Serializable;

public class Producto implements Serializable {
    private String nombre;
    private int precio;
    private String descripcion;
    private String nombreImagen;
    private double size;
    private int cantidad_en_stock;
    private String key;
    public Producto(String nombre, int precio, String descripcion, String nombreImagen, double size) {
        super();
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.nombreImagen = nombreImagen;
        this.size = size;

    }
    public Producto(String txt){
        String data[]=txt.split(";");
        this.nombre = data[0];
        this.precio = Integer.parseInt((data[1]));
        this.descripcion = data[2];
        this.nombreImagen = data[3];
        this.size = Double.parseDouble(data[4]);



    }

    public String toString(){
        return nombre+";"+precio+";"+descripcion+";"+nombreImagen+";"+size+";";
    }
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }



    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public int getPrecio() {
        return precio;
    }
    public void setPrecio(int precio) {
        this.precio = precio;
    }


    public String getKey() { return key;
    }
    public void setKey(String key){
        this.key=key;
    }
}
