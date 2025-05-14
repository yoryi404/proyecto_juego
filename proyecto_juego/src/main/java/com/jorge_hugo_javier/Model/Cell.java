/**
 * @author Jorge Alegre Maestre
 * @author Hugo Perez Muñoz
 * @author Javier Gil Garán
 */
package com.jorge_hugo_javier.Model;

/**
 * Representa una celda del mapa, que puede ser suelo o pared y puede tener un
 * ocupante.
 */
public class Cell {
    /**
     * Tipos posibles de celda en el mapa: pared o suelo.
     */
    public enum Type {
        WALL, FLOOR
    }

    private Type type;
    private JuegoCharacter occupant; // Puede ser enemigo o jugador
    private char simboloOriginal;

    /**
     * Crea una celda con un tipo específico y un símbolo original leído del mapa.
     * 
     * @param type    Tipo de la celda (WALL o FLOOR).
     * @param simbolo Carácter representativo de la celda.
     */
    public Cell(Type type, char simbolo) {
        this.type = type;
        this.simboloOriginal = simbolo;
    }

    /**
     * Crea una celda con un tipo, asignando automáticamente el símbolo (# o .).
     * 
     * @param type Tipo de la celda (WALL o FLOOR).
     */
    public Cell(Type type) {
        this.type = type;
        this.simboloOriginal = (type == Type.WALL) ? '#' : '.';
    }

    /**
     * Devuelve el tipo de la celda.
     * 
     * @return Tipo de celda (WALL o FLOOR).
     */
    public Type getType() {
        return type;
    }

    /**
     * Indica si la celda es transitable (es suelo y no tiene ocupante vivo).
     * 
     * @return true si se puede caminar sobre la celda; false en caso contrario.
     */
    public boolean isWalkable() {
        return type == Type.FLOOR && (occupant == null || occupant.isDead());
    }

    /**
     * Establece el ocupante de la celda (jugador o enemigo).
     * 
     * @param occupant El personaje que ocupa esta celda.
     */
    public void setOccupant(JuegoCharacter occupant) {
        this.occupant = occupant;
    }

    /**
     * Devuelve el personaje que ocupa esta celda, o null si está vacía.
     * 
     * @return Ocupante actual o null.
     */
    public JuegoCharacter getOccupant() {
        return occupant;
    }

    /**
     * Devuelve el símbolo original de esta celda en el mapa (por ejemplo, '#' o
     * '.').
     * 
     * @return Carácter representativo de la celda.
     */
    public char getSimboloOriginal() {
        return simboloOriginal;
    }

    /**
     * Devuelve el símbolo asociado al tipo de celda, útil para impresión o
     * exportación.
     * 
     * @return Carácter del tipo de celda.
     */
    public char getTipo() {
        return simboloOriginal;
    }
}
