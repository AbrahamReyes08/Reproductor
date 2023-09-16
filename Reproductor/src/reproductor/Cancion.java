/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package reproductor;

/**
 *
 * @author dell
 */
public class Cancion {
    private String nombre;
    private String path;

    public Cancion(String nombre, String path) {
        this.nombre = nombre;
        this.path = path;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPath() {
        return path;
    }
    
    public String toString() {
        return nombre;
    }
}


