package com.jorge_hugo_javier.Model;

public class Cell {
    public enum Type {
        WALL, FLOOR
    }

    private Type type;
    private JuegoCharacter occupant; // Puede ser enemigo o jugador
    private char simboloOriginal;

    // 🧱 Constructor con tipo y símbolo original (desde el mapa)
    public Cell(Type type, char simbolo) {
        this.type = type;
        this.simboloOriginal = simbolo;
    }

    // 🧱 Constructor alternativo (sin símbolo explícito)
    public Cell(Type type) {
        this.type = type;
        this.simboloOriginal = (type == Type.WALL) ? '#' : '.';
    }

    // ✅ Tipo de celda (WALL o FLOOR)
    public Type getType() {
        return type;
    }

    // ✅ Devuelve si se puede caminar por esta celda (suelo y sin ocupante vivo)
    public boolean isWalkable() {
        return type == Type.FLOOR && (occupant == null || occupant.isDead());
    }

    // ✅ Asigna ocupante (jugador o enemigo)
    public void setOccupant(JuegoCharacter occupant) {
        this.occupant = occupant;
    }

    // ✅ Devuelve el ocupante actual (o null si no hay)
    public JuegoCharacter getOccupant() {
        return occupant;
    }

    // ✅ Devuelve el carácter original del mapa (ej: '#', '.')
    public char getSimboloOriginal() {
        return simboloOriginal;
    }

    // ✅ Utilizado para imprimir o convertir a matriz de chars
    public char getTipo() {
        return simboloOriginal;
    }
}
