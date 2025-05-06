package com.jorge_hugo_javier.Model;


public class Enemigo extends JuegoCharacter {

    public Enemigo(String name, int health, int attack, int x, int y) {
        super(name, health, attack, x, y);
    }

    @Override
    public void takeTurn(JuegoMap map, JuegoCharacter enemigo, JuegoCharacter jugador) {
        int dx = jugador.getX() - this.x;
        int dy = jugador.getY() - this.y;

        // Ataca si el jugador está adyacente (movimiento Manhattan de 1)
        if (Math.abs(dx) + Math.abs(dy) == 1) {
            jugador.receiveDamage(this.attack);
        } else {
            int newX = this.x + Integer.signum(dx);
            int newY = this.y + Integer.signum(dy);
            moveTo(newX, newY, map);
        }
    }
        // Aquí podrías añadir lógica futura como: patrullar, detectar distancia, huir, etc.
}