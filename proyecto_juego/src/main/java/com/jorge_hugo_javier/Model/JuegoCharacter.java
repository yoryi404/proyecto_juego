/**
 * @author Jorge Alegre Maestre
 * @author Hugo Perez Muñoz
 * @author Javier Gil Garán
 */
package com.jorge_hugo_javier.Model;

/**
 * Clase abstracta que representa a un personaje en el juego, ya sea un jugador
 * o un enemigo.
 * Contiene métodos comunes para gestionar atributos como salud, ataque,
 * posición y movimiento.
 */
public abstract class JuegoCharacter {
    protected String name;
    protected int health;
    protected int attack;
    protected int x, y;
    protected int velocidad = 0;

    /**
     * Constructor para crear un nuevo personaje con atributos iniciales.
     *
     * @param name   Nombre del personaje.
     * @param health Cantidad de salud inicial del personaje.
     * @param attack Daño de ataque del personaje.
     * @param x      Coordenada X inicial del personaje.
     * @param y      Coordenada Y inicial del personaje.
     */
    public JuegoCharacter(String name, int health, int attack, int x, int y) {
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.x = x;
        this.y = y;
    }

    /**
     * Verifica si el personaje está vivo (salud mayor que 0).
     *
     * @return true si el personaje está vivo, false si está muerto.
     */
    public boolean isAlive() {
        return health > 0;
    }

    /**
     * Verifica si el personaje está muerto (salud igual o menor que 0).
     *
     * @return true si el personaje está muerto, false si está vivo.
     */
    public boolean isDead() {
        return !isAlive();
    }

    /**
     * Aplica daño al personaje, reduciendo su salud.
     *
     * @param damage Cantidad de daño recibido.
     */
    public void receiveDamage(int damage) {
        health -= damage;
    }

    /**
     * Mueve al personaje a una nueva posición en el mapa si la celda de destino es
     * válida y accesible.
     *
     * @param newX Nueva coordenada X de destino.
     * @param newY Nueva coordenada Y de destino.
     * @param map  El mapa del juego, utilizado para verificar si la celda de
     *             destino es válida.
     */
    public void moveTo(int newX, int newY, JuegoMap map) {
        if (!map.isInsideBounds(newX, newY))
            return;

        Cell newCell = map.getCell(newX, newY);
        if (newCell.isWalkable()) {
            map.getCell(x, y).setOccupant(null);
            x = newX;
            y = newY;
            newCell.setOccupant(this);
        }
    }

    /**
     * Acción del personaje en su turno. Este método debe ser implementado en las
     * subclases
     * para definir el comportamiento específico del personaje durante su turno (ej.
     * movimiento o ataque).
     *
     * @param map     El mapa del juego.
     * @param enemigo El enemigo al que el personaje podría atacar.
     * @param jugador El jugador objetivo (si el personaje es un enemigo).
     */
    public abstract void takeTurn(JuegoMap map, JuegoCharacter enemigo, JuegoCharacter jugador);

    /**
     * Devuelve la coordenada X del personaje.
     *
     * @return Coordenada X actual del personaje.
     */
    public int getX() {
        return x;
    }

    /**
     * Devuelve la coordenada Y del personaje.
     *
     * @return Coordenada Y actual del personaje.
     */
    public int getY() {
        return y;
    }

    /**
     * Devuelve la salud actual del personaje.
     *
     * @return La salud actual del personaje.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Devuelve el valor de ataque del personaje.
     *
     * @return El daño de ataque del personaje.
     */
    public int getAttack() {
        return attack;
    }

    /**
     * Devuelve el nombre del personaje.
     *
     * @return El nombre del personaje.
     */
    public String getName() {
        return name;
    }

    /**
     * Devuelve la velocidad del personaje.
     *
     * @return La velocidad del personaje.
     */
    public int getVelocidad() {
        return velocidad;
    }

    /**
     * Devuelve el nombre del personaje (alias de getName).
     *
     * @return El nombre del personaje.
     */
    public String getNombre() {
        return name;
    }

    /**
     * Establece la nueva coordenada X del personaje.
     *
     * @param x Nueva coordenada X del personaje.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Establece la nueva coordenada Y del personaje.
     *
     * @param y Nueva coordenada Y del personaje.
     */
    public void setY(int y) {
        this.y = y;
    }
}