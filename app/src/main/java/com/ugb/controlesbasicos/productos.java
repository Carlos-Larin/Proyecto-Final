package com.ugb.controlesbasicos;

public class productos {

    String idProducto;
    String marca;
    String descripcion;
    String presentacion;
    String stock;
    String precio;
    String urlFotoProducto;
    String urlFotoProductoFirestore;
    String Token;

    public productos(String idProducto, String marca, String descripcion, String presentacion, String stock, String  precio, String urlFoto, String urlFotoProductoFirestore, String Token) {
        this.idProducto = idProducto;
        this.marca = marca;
        this.descripcion = descripcion;
        this.presentacion =presentacion;
        this.stock = stock;
        this.precio = precio;
        this.urlFotoProducto = urlFoto;
        this.urlFotoProductoFirestore=urlFotoProductoFirestore;
        this.Token=Token;

    }

    public String getUrlFotoProductoFirestore() {
        return urlFotoProductoFirestore;
    }

    public void setUrlFotoProductoFirestore(String urlFotoProductoFirestore){
        this.urlFotoProductoFirestore=urlFotoProductoFirestore;
    }

    public String getToken() {
        return Token;
    }
    public void setToken(String token) {
        this.Token = token;
    }
    public String getUrlFotoProducto() {
        return urlFotoProducto;
    }

    public void setUrlFotoProducto(String urlFotoProducto) {
        this.urlFotoProducto = urlFotoProducto;
    }

    public String getidProducto() {
        return idProducto;
    }

    public void setidProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}