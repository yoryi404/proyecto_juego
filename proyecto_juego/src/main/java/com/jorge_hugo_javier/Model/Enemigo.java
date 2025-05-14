package com.jorge_hugo_javier.Model;

import java.util.ArrayList;
import java.util.List;

import com.jorge_hugo_javier.Observer.Observer;
import com.jorge_hugo_javier.Observer.Subject;

public class Enemigo extends JuegoCharacter implements Subject{
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

    public Enemigo(String name, int health, int attack, int x, int y) {
        super(name, health, attack, x, y);
    }

    /**
     * Acción del enemigo en su turno. Si el jugador está adyacente, lo ataca.
     * Si no, se mueve hacia él.
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
 * @param xJugador  Coordenada X del jugador
 * @param yJugador  Coordenada Y del jugador
 * @param mapa      El mapa del juego, con esPosicionValida(x,y)
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
                return;  // terminamos el turno tras moverse
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
     * Comprueba si la celda a la que quiere moverse es válida.
     */
    private boolean puedeMoverA(int x, int y, JuegoMap mapa) {
        if (!mapa.isInsideBounds(x, y))
            return false;
        Cell celda = mapa.getCell(x, y);
        return celda.isWalkable() && (celda.getOccupant() == null || celda.getOccupant().isDead());
    }

    public int getMaxHealth() {
        return 100;
    }

}