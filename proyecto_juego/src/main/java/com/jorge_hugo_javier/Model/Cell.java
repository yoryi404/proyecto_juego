package com.jorge_hugo_javier.Model;

public class Cell {
    public enum Type { WALL, FLOOR }

    private Type type;
    private Character occupant;

    public Cell(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public boolean isWalkable() {
        return type == Type.FLOOR && (occupant == null || occupant.isDead());
    }

    public void setOccupant(Character occupant) {
        this.occupant = occupant;
    }

    public Character getOccupant() {
        return occupant;
    }
}
