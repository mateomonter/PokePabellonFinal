package com.mateomontero.pokepabellon.modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Pedido implements Serializable {



    String key;
    ArrayList<ItemCarrrito> interiorCarrito;
    Usuario usuario;
    int precio;

    public Pedido(String key, ArrayList<ItemCarrrito> interiorCarrito, Usuario usuario, int precio) {
        this.key = key;
        this.interiorCarrito = interiorCarrito;
        this.usuario = usuario;
        this.precio=precio;

    }


    public String toString(){
        String txt=usuario.getCorreo()+";"+precio+";"+key+";";
        for (ItemCarrrito it :interiorCarrito){
            txt+=it.toString();
        }
        return txt;


    }
}
