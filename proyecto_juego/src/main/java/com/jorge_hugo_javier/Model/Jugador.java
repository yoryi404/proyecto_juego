/**
 * @author Jorge Alegre Maestre
 * @author Hugo Perez Muñoz
 * @author Javier Gil Garán
 */
package com.jorge_hugo_javier.Model;

import java.util.ArrayList;
import java.util.List;

import com.jorge_hugo_javier.Observer.Observer;
import com.jorge_hugo_javier.Observer.Subject;

/**
 * Representa al jugador dentro del juego. La clase extiende de `JuegoCharacter`
 * y implementa el patrón `Observer` para notificar cambios como salud,
 * posición, etc.
 * Esta clase permite al jugador moverse en el mapa y realizar acciones en el
 * juego.
 */
public class Jugador extends JuegoCharacter implements Subject {
    private final List<Observer> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String event) {
        for (Observer o : observers)
            o.update(this, event);
    }

    @Override
    public void receiveDamage(int damage) {
        super.receiveDamage(damage);
        notifyObservers("health");
        if (isDead())
            notifyObservers("death");
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

    /**
     * Constructor del jugador. Inicializa el jugador con un nombre, salud, fuerza,
     * defensa y velocidad.
     * Las coordenadas iniciales del jugador (x, y) se establecen en 0, 0.
     *
     * @param nombre    Nombre del jugador.
     * @param salud     Salud inicial del jugador.
     * @param fuerza    Fuerza de ataque inicial del jugador.
     * @param defensa   Defensa inicial del jugador.
     * @param velocidad Velocidad inicial del jugador.
     */
    public Jugador(String nombre, int salud, int fuerza, int defensa, int velocidad) {
        super(nombre, salud, fuerza, 0, 0); // x = 0, y = 0
        this.defensa = defensa;
        this.velocidad = velocidad;
    }

    /**
     * Mueve al jugador una casilla hacia arriba, si la celda es transitable.
     *
     * @param mapa El mapa del juego donde el jugador se mueve.
     */
    public void moverArriba(JuegoMap mapa) {
        int newY = y - 1;
        if (mapa.isInsideBounds(x, newY) && mapa.getCell(x, newY).isWalkable()) {
            y = newY;
        }
    }

    /**
     * Mueve al jugador una casilla hacia abajo, si la celda es transitable.
     *
     * @param mapa El mapa del juego donde el jugador se mueve.
     */
    public void moverAbajo(JuegoMap mapa) {
        int newY = y + 1;
        if (mapa.isInsideBounds(x, newY) && mapa.getCell(x, newY).isWalkable()) {
            y = newY;
        }
    }

    /**
     * Mueve al jugador una casilla hacia la izquierda, si la celda es transitable.
     *
     * @param mapa El mapa del juego donde el jugador se mueve.
     */
    public void moverIzquierda(JuegoMap mapa) {
        int newX = x - 1;
        if (mapa.isInsideBounds(newX, y) && mapa.getCell(newX, y).isWalkable()) {
            x = newX;
        }
    }

    /**
     * Mueve al jugador una casilla hacia la derecha, si la celda es transitable.
     *
     * @param mapa El mapa del juego donde el jugador se mueve.
     */
    public void moverDerecha(JuegoMap mapa) {
        int newX = x + 1;
        if (mapa.isInsideBounds(newX, y) && mapa.getCell(newX, y).isWalkable()) {
            x = newX;
        }
    }

    /**
     * Establece los límites del mapa, es decir, las coordenadas máximas en X e Y
     * que el jugador puede alcanzar.
     *
     * @param maxX Coordenada máxima en el eje X.
     * @param maxY Coordenada máxima en el eje Y.
     */
    public void setLimites(int maxX, int maxY) {
        this.limiteX = maxX;
        this.limiteY = maxY;
    }

    /**
     * Establece el valor de defensa del jugador.
     *
     * @param defensa Valor de defensa a establecer.
     */
    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }

    /**
     * Establece el valor de velocidad del jugador.
     *
     * @param velocidad Valor de velocidad a establecer.
     */
    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    /**
     * Establece la posición del jugador en las coordenadas (x, y).
     *
     * @param x Nueva coordenada X del jugador.
     * @param y Nueva coordenada Y del jugador.
     */
    public void setPosicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Devuelve el valor de defensa del jugador.
     *
     * @return El valor de defensa del jugador.
     */
    public int getDefensa() {
        return defensa;
    }

    /**
     * Devuelve el valor de velocidad del jugador.
     *
     * @return El valor de velocidad del jugador.
     */
    public int getVelocidad() {
        return velocidad;
    }

    /**
     * Devuelve el nombre del jugador.
     *
     * @return El nombre del jugador.
     */
    public String getNombre() {
        return name;
    }

    /**
     * Devuelve la fuerza de ataque del jugador como una cadena.
     *
     * @return La fuerza de ataque del jugador.
     */
    public String getFuerza() {
        return String.valueOf(attack);
    }

    /**
     * Método abstracto que define la acción que debe realizar el jugador en su
     * turno.
     * Este método no está implementado para el jugador, ya que el jugador controla
     * su turno directamente.
     *
     * @param map     El mapa en el que el jugador se encuentra.
     * @param enemigo El enemigo contra el cual el jugador puede interactuar.
     * @param jugador El jugador mismo.
     */
    @Override
    public void takeTurn(JuegoMap map, JuegoCharacter enemigo, JuegoCharacter jugador) {
        // No implementado para el jugador
    }

    /**
     * Devuelve una representación en cadena del jugador, incluyendo su nombre,
     * salud,
     * fuerza, defensa, velocidad y su posición en el mapa (coordenadas X e Y).
     *
     * @return Una cadena con la representación del jugador.
     */
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

    /**
     * Devuelve la salud máxima del jugador. Este valor está fijado como 100 en la
     * implementación actual.
     *
     * @return La salud máxima del jugador.
     */
    public int getMaxHealth() {
        return 100;
    }
}