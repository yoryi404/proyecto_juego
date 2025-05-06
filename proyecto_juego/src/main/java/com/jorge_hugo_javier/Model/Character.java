package com.jorge_hugo_javier.Model;

import java.util.Map;

import javafx.scene.control.Cell;

public abstract class Character {
    protected String name;
    protected int health;
    protected int attack;
    protected int x, y;

    public Character(String name, int health, int attack, int x, int y) {
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.x = x;
        this.y = y;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public boolean isDead() {
        return !isAlive();
    }

    public void receiveDamage(int damage) {
        health -= damage;
    }

    public void moveTo(int newX, int newY, Map map) {
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

    public abstract void takeTurn(Map map, Character Enemigo,Character Jugador);

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }

    public int getAttack() {
        return attack;
    }

    public String getName() {
        return name;
    }
}
