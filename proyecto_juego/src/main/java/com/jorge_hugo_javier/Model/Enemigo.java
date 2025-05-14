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
 * Representa un enemigo en el juego. Puede moverse, atacar y notificar eventos
 * a observadores.
 */
public class Enemigo extends JuegoCharacter implements Subject {
    private final List<Observer> observers = new ArrayList<>();

    /**
     * Añade un observador para recibir eventos del enemigo.
     * 
     * @param o Observador a registrar.
     */
    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    /**
     * Elimina un observador previamente registrado.
     * 
     * @param o Observador a eliminar.
     */
    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    /**
     * Notifica a todos los observadores sobre un evento específico.
     *
     * @param event Tipo de evento (por ejemplo: "health", "death", "position").
     */
    @Override
    public void notifyObservers(String event) {
        for (Observer o : observers)
            o.update(this, event);
    }

    /**
     * Aplica daño al enemigo, notifica cambio de salud y posible muerte.
     * 
     * @param damage Cantidad de daño recibido.
     */
    @Override
    public void receiveDamage(int damage) {
        super.receiveDamage(damage);
        notifyObservers("health");
        if (isDead())
            notifyObservers("death");
    }

    /**
     * Establece la nueva coordenada X del enemigo y notifica cambio de posición.
     *
     * @param x Nueva coordenada X.
     */
    @Override
    public void setX(int x) {
        super.setX(x);
        notifyObservers("position");
    }

    /**
     * Establece la nueva coordenada Y del enemigo y notifica cambio de posición.
     *
     * @param y Nueva coordenada Y.
     */
    @Override
    public void setY(int y) {
        super.setY(y);
        notifyObservers("position");
    }

    protected int maxHealth;

    /**
     * Crea un enemigo con sus atributos iniciales.
     *
     * @param name   Nombre del enemigo.
     * @param health Vida inicial.
     * @param attack Daño de ataque.
     * @param x      Posición X inicial.
     * @param y      Posición Y inicial.
     */
    public Enemigo(String name, int health, int attack, int x, int y) {
        super(name, health, attack, x, y);
    }

    /**
     * Ejecuta la acción del enemigo en su turno. Ataca si el jugador está
     * adyacente,
     * si no, se mueve hacia él.
     *
     * @param map     Mapa del juego.
     * @param enemigo El propio enemigo (referencia a sí mismo).
     * @param jugador El jugador objetivo.
     */
    @Override
    public void takeTurn(JuegoMap map, JuegoCharacter enemigo, JuegoCharacter jugador) {
        int dx = jugador.getX() - this.x;
        int dy = jugador.getY() - this.y;

        if (Math.abs(dx) + Math.abs(dy) == 1) {
            jugador.receiveDamage(this.attack);
        } else {
            moverHacia(jugador.getX(), jugador.getY(), map);
        }
    }

    /**
     * Mueve al enemigo en dirección al jugador, respetando muros y ocupantes.
     *
     * @param xJugador Coordenada X del jugador
     * @param yJugador Coordenada Y del jugador
     * @param mapa     El mapa del juego, con esPosicionValida(x,y)
     */
    public void moverHacia(int xJugador, int yJugador, JuegoMap mapa) {
        // Calcula hacia dónde habría que moverse en X y en Y
        int dx = Integer.compare(xJugador, this.getX());
        int dy = Integer.compare(yJugador, this.getY());

        // 1) Intentar mover en X si dx != 0
        if (dx != 0) {
            int nuevoX = this.getX() + dx;
            int nuevoY = this.getY();
            // Solo movemos si la celda destino es válida
            if (mapa.esPosicionValida(nuevoX, nuevoY)) {
                moveTo(nuevoX, nuevoY, mapa);
                return; // terminamos el turno tras moverse
            }
        }

        // 2) Si no pudo en X, intentar en Y si dy != 0
        if (dy != 0) {
            int nuevoX = this.getX();
            int nuevoY = this.getY() + dy;
            if (mapa.esPosicionValida(nuevoX, nuevoY)) {
                moveTo(nuevoX, nuevoY, mapa);
            }
        }
    }

    /**
     * Verifica si el enemigo puede moverse a una celda dada.
     *
     * @param x    Coordenada X de destino.
     * @param y    Coordenada Y de destino.
     * @param mapa El mapa del juego.
     * @return true si la celda es válida para moverse.
     */
    private boolean puedeMoverA(int x, int y, JuegoMap mapa) {
        if (!mapa.isInsideBounds(x, y))
            return false;
        Cell celda = mapa.getCell(x, y);
        return celda.isWalkable() && (celda.getOccupant() == null || celda.getOccupant().isDead());
    }

    /**
     * Devuelve la salud máxima del enemigo.
     * @return Valor de salud máxima (por defecto, 100).
     */
    public int getMaxHealth() {
        return 100;
    }
}