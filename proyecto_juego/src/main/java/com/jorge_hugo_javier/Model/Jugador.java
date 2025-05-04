package com.jorge_hugo_javier.Model;

public class Jugador {

    private String nombre;
    private int salud;
    private int fuerza;
    private int defensa;
    private int velocidad;

    public Jugador(String nombre, int salud, int fuerza, int defensa, int velocidad) {
        this.nombre = nombre;
        this.salud = salud;
        this.fuerza = fuerza;
        this.defensa = defensa;
        this.velocidad = velocidad;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public int getSalud() {
        return salud;
    }

    public int getFuerza() {
        return fuerza;
    }

    public int getDefensa() {
        return defensa;
    }

    public int getVelocidad() {
        return velocidad;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setSalud(int salud) {
        this.salud = salud;
    }

    public void setFuerza(int fuerza) {
        this.fuerza = fuerza;
    }

    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    @Override
    public String toString() {
        return "Jugador{" +
               "nombre='" + nombre + '\'' +
               ", salud=" + salud +
               ", fuerza=" + fuerza +
               ", defensa=" + defensa +
               ", velocidad=" + velocidad +
               '}';
    }
}