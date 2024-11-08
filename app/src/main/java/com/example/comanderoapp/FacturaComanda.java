package com.example.comanderoapp;

public class FacturaComanda {

    //Atributos
    private String Producto;

    private int Cantidad;

    private float PrecioUnidad;

    private float PrecioTotal;


    //Constructor
    public FacturaComanda(String producto, int Cantidad, float PrecioUnidad){

        this.Producto = producto;
        this.Cantidad = Cantidad;
        this.PrecioUnidad =  PrecioUnidad;
        this.PrecioTotal = PrecioUnidad * Cantidad;
    }


    //Getters y Setters
    public String getProducto() {
        return Producto;
    }

    public void setProducto(String producto) {
        Producto = producto;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int cantidad) {
        Cantidad = cantidad;
    }

    public float getPrecioUnidad() {
        return PrecioUnidad;
    }

    public void setPrecioUnidad(float precioUnidad) {
        PrecioUnidad = precioUnidad;
    }

    public float getPrecioTotal() {
        return PrecioTotal;
    }

    public void setPrecioTotal(float precioTotal) {
        PrecioTotal = precioTotal;
    }
}
