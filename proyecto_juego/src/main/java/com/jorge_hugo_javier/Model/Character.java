package com.jorge_hugo_javier.Model;

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

}

