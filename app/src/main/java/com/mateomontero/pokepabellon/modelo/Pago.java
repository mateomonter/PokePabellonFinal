package com.mateomontero.pokepabellon.modelo;

public class Pago {
    String metodoDePago;
    double precio;

    public Pago(String txt){
        String data[]=txt.split(";");
        this.metodoDePago = data[0];
        this.precio = Integer.parseInt((data[1]));


    }

    public String toString(){
        return metodoDePago+";"+precio;
    }


    public String getMetodoDePago() {
        return metodoDePago;
    }

    public void setMetodoDePago(String metodoDePago) {
        this.metodoDePago = metodoDePago;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
