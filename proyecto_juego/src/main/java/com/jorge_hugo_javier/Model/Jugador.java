package com.jorge_hugo_javier.Model;

public class Jugador {

    private String nombre;
    private int salud;
    private int fuerza;
    private int defensa;
    private int velocidad;
    private int posX;
    private int posY;

    private int posX;
    private int posY;
    private int limiteX;
    private int limiteY;

    public Jugador(String nombre, int salud, int fuerza, int defensa, int velocidad) {
        this.nombre = nombre;
        this.salud = salud;
        this.fuerza = fuerza;
        this.defensa = defensa;
        this.velocidad = velocidad;
        this.posX = 0;
        this.posY = 0;
    }

    public void moverArriba() {
        if (posY > 0)
            posY--;
    }

    public void moverAbajo() {
        if (posY < limiteY - 1)
            posY++;
    }

    public void moverIzquierda() {
        if (posX > 0)
            posX--;
    }

    public void moverDerecha() {
        if (posX < limiteX - 1)
            posX++;
    }

    public void setLimites(int maxX, int maxY) {
        this.limiteX = maxX;
        this.limiteY = maxY;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosicion(int x, int y) {
        this.posX = x;
        this.posY = y;
    }

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

    public int getPosX() {
    return posX;
}

public int getPosY() {
    return posY;
}

public void setPosicion(int x, int y) {
    this.posX = x;
    this.posY = y;
}

    @Override
    public String toString() {
        return "Jugador{" +
                "nombre='" + nombre + '\'' +
                ", salud=" + salud +
                ", fuerza=" + fuerza +
                ", defensa=" + defensa +
                ", velocidad=" + velocidad +
                ", posX=" + posX +
                ", posY=" + posY +
                '}';
    }
}