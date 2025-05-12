package com.jorge_hugo_javier.Model;

public class Cell {
    public enum Type {
        WALL, FLOOR
    }

    private Type type;
    private JuegoCharacter occupant; // Puede ser enemigo o jugador
    private char simboloOriginal;

    // Constructor usado en creación de mapa
    public Cell(Type type, char simbolo) {
        this.type = type;
        this.simboloOriginal = simbolo;
    }

    // Constructor alternativo si no se usa símbolo
    public Cell(Type type) {
        this.type = type;
        this.simboloOriginal = (type == Type.WALL) ? '#' : '.';
    }

    public Type getType() {
        return type;
    }

    public boolean isWalkable() {
        return type == Type.FLOOR && (occupant == null || occupant.isDead());
    }

    public void setOccupant(JuegoCharacter occupant) {
        this.occupant = occupant;
    }

    public JuegoCharacter getOccupant() {
        return occupant;
    }

    public char getSimboloOriginal() {
        return simboloOriginal;
    }

    // 🔧 MÉTODO CLAVE PARA getMapaChar()
    public char getTipo() {
        return simboloOriginal;
    }
}