package com.ugb.controlesbasicos;

import java.util.Base64;
public class utilidades {
    static String urlConsulta = "http://192.168.1.17:5984/proyectofinal/_design/ProyectoFinal/_view/ProyectoFinal";
    static String urlMto = "http://192.168.1.17:5984/proyectofinal";
    static String user = "CarlosAristides";
    static String passwd = "CARTOS12A?";
    static String credencialesCodificadas = Base64.getEncoder().encodeToString((user +":"+ passwd).getBytes());
    public String generarIdUnico(){
        return java.util.UUID.randomUUID().toString();
    }
}
