package simulador;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author eejl_
 */
public class Camion2 {

    String Ruta;
    int id;
    String nombre;
    int capacidad;
String chofer;
    public Camion2(String Ruta, String id, String nombre, String capacidad, String chofer) {
        this.Ruta = Ruta;
        this.id = Integer.parseInt(id);
        this.nombre = nombre;
        this.capacidad = Integer.parseInt(capacidad);
        this.chofer=chofer;
    }
}
