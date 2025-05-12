package com.jorge_hugo_javier.Model;

public class Jugador extends JuegoCharacter {

    private int defensa;
    private int velocidad;

    private int limiteX;
    private int limiteY;

    public Jugador(String nombre, int salud, int fuerza, int defensa, int velocidad) {
        super(nombre, salud, fuerza, 0, 0); // x = 0, y = 0
        this.defensa = defensa;
        this.velocidad = velocidad;
    }

    public void moverArriba() {
        if (y > 0)
            y--;
    }

    public void moverAbajo() {
        if (y < limiteY - 1)
            y++;
    }

    public void moverIzquierda() {
        if (x > 0)
            x--;
    }

    public void moverDerecha() {
        if (x < limiteX - 1)
            x++;
    }

    public void setLimites(int maxX, int maxY) {
        this.limiteX = maxX;
        this.limiteY = maxY;
    }

    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    public int getDefensa() {
        return defensa;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setPosicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Ya tienes: getX(), getY(), getAttack(), getHealth() heredados

    @Override
    public String toString() {
        return "Jugador{" +
                "nombre='" + name + '\'' +
                ", salud=" + health +
                ", fuerza=" + attack +
                ", defensa=" + defensa +
                ", velocidad=" + velocidad +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public void takeTurn(JuegoMap map, JuegoCharacter enemigo, JuegoCharacter jugador) {
        // Se puede dejar vacío si no se usa
    }
}