package com.jorge_hugo_javier.Model;

public class Cell {
    public enum Type { WALL, FLOOR }

    private Type type;
    private JuegoCharacter occupant; // ← cambiado aquí

    public Cell(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public boolean isWalkable() {
        return type == Type.FLOOR && (occupant == null || occupant.isDead());
    }

    public void setOccupant(JuegoCharacter occupant) { // ← cambiado
        this.occupant = occupant;
    }

    public JuegoCharacter getOccupant() { // ← cambiado
        return occupant;
    }
}
