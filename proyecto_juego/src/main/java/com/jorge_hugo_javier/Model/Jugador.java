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

    // --- MOVIMIENTO con validación de límites y paredes ---
    public void moverArriba(JuegoMap mapa) {
        int newY = y - 1;
        if (mapa.isInsideBounds(x, newY) && mapa.getCell(x, newY).isWalkable()) {
            y = newY;
        }
    }

    public void moverAbajo(JuegoMap mapa) {
        int newY = y + 1;
        if (mapa.isInsideBounds(x, newY) && mapa.getCell(x, newY).isWalkable()) {
            y = newY;
        }
    }

    public void moverIzquierda(JuegoMap mapa) {
        int newX = x - 1;
        if (mapa.isInsideBounds(newX, y) && mapa.getCell(newX, y).isWalkable()) {
            x = newX;
        }
    }

    public void moverDerecha(JuegoMap mapa) {
        int newX = x + 1;
        if (mapa.isInsideBounds(newX, y) && mapa.getCell(newX, y).isWalkable()) {
            x = newX;
        }
    }

    // --- Establecer límites del mapa ---
    public void setLimites(int maxX, int maxY) {
        this.limiteX = maxX;
        this.limiteY = maxY;
    }

    // --- Setters ---
    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    public void setPosicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // --- Getters ---
    public int getDefensa() {
        return defensa;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public String getNombre() {
        return name;
    }

    public String getFuerza() {
        return String.valueOf(attack);
    }

    // --- Turno (sin usar en jugador) ---
    @Override
    public void takeTurn(JuegoMap map, JuegoCharacter enemigo, JuegoCharacter jugador) {
        // No implementado para el jugador
    }

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
}