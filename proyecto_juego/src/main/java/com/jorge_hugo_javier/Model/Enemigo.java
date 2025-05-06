package com.jorge_hugo_javier.Model;

import java.util.Map;

public class Enemigo extends Character {
    public Enemigo(String name, int health, int attack, int x, int y) {
        super(name, health, attack, x, y);
    }

    @Override
    public void takeTurn(Map map, Character Enemigo,Character Jugador) {

        int dx = Jugador.getX() - x;
        int dy = Jugador.getY() - y;
        if (Math.abs(dx) + Math.abs(dy) == 1) {
            Jugador.receiveDamage(attack);
        } else {

            int newX = x + Integer.signum(dx);
            int newY = y + Integer.signum(dy);
            moveTo(newX, newY, map);
        }
    }
}
