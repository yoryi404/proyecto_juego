package com.jorge_hugo_javier.Model;

import java.util.ArrayList;
import java.util.List;

import com.jorge_hugo_javier.Observer.Observer;
import com.jorge_hugo_javier.Observer.Subject;

public class Jugador extends JuegoCharacter implements Subject{
    private final List<Observer> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer o)   { observers.add(o); }
    @Override
    public void removeObserver(Observer o){ observers.remove(o); }
    @Override
    public void notifyObservers(String event) {
        for (Observer o : observers) o.update(this, event);
    }

    @Override
    public void receiveDamage(int damage) {
        super.receiveDamage(damage);
        notifyObservers("health");
        if (isDead()) notifyObservers("death");
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        notifyObservers("position");
    }
    @Override
    public void setY(int y) {
        super.setY(y);
        notifyObservers("position");
    }
 
    protected int maxHealth;

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

    public int getMaxHealth() {
        return 100;
    }
}