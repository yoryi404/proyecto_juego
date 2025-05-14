package com.jorge_hugo_javier.Model;

public class Enemigo extends JuegoCharacter {
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
     * Mueve al enemigo una celda en dirección al jugador, si es posible.
     */
    public void moverHacia(int xJugador, int yJugador, JuegoMap mapa) {
        int dx = Integer.compare(xJugador, this.getX());
        int dy = Integer.compare(yJugador, this.getY());

        // Prioridad en X
        if (dx != 0) {
            int nuevoX = this.getX() + dx;
            int nuevoY = this.getY();
            if (puedeMoverA(nuevoX, nuevoY, mapa)) {
                moveTo(nuevoX, nuevoY, mapa);
                return;
            }
        }

        // Luego intenta Y
        if (dy != 0) {
            int nuevoX = this.getX();
            int nuevoY = this.getY() + dy;
            if (puedeMoverA(nuevoX, nuevoY, mapa)) {
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