/**
 * @author Jorge Alegre Maestre
 * @author Hugo Perez Muñoz
 * @author Javier Gil Garán
 */
package com.jorge_hugo_javier.Model;

/**
 * Representa una celda del mapa, que puede ser suelo, pared o trampa, y puede tener un
 * ocupante.
 */
public class Cell {
    /**
     * Tipos de celda en el mapa: pared, suelo y trampa.
     */
    public enum Type {
        WALL, FLOOR, TRAP
    }

    private Type type;
    private JuegoCharacter occupant; // Puede ser enemigo o jugador
    private char simboloOriginal;

    /**
     * Crea una celda con un tipo específico y un símbolo original leído del mapa.
     * 
     * @param type    Tipo de la celda (WALL, FLOOR o TRAMPA).
     * @param simbolo Carácter representativo de la celda.
     */
    public Cell(Type type, char simbolo) {
        this.type = type;
        this.simboloOriginal = simbolo;
    }

    /**
     * Crea una celda con un tipo, asignando automáticamente el símbolo (#, . o ^).
     * 
     * @param type Tipo de la celda (WALL, FLOOR o TRAMPA).
     */
    public Cell(Type type) {
        this.type = type;
        this.simboloOriginal = (type == Type.WALL) ? '#' : (type == Type.TRAP ? '^' : '.');
    }

    /**
     * Devuelve el tipo de la celda.
     * 
     * @return Tipo de celda (WALL, FLOOR o TRAMPA).
     */
    public Type getType() {
        return type;
    }

    /**
     * Indica si la celda es transitable (es suelo o trampa y no tiene ocupante vivo).
     * 
     * @return true si se puede caminar sobre la celda; false en caso contrario.
     */
    public boolean isWalkable() {
        return (type == Type.FLOOR || type == Type.TRAP) && (occupant == null || occupant.isDead());
    }

    /**
     * Indica si la celda es una trampa.
     * 
     * @return true si es trampa, false si no.
     */
    public boolean isTrap() {
        return type == Type.TRAP;
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
     * Devuelve el símbolo original de esta celda en el mapa (por ejemplo, '#', '.', '^').
     * 
     * @return Carácter representativo de la celda.
     */
    public char getSimboloOriginal() {
        return simboloOriginal;
    }

    /**
     * Devuelve el símbolo asociado al tipo de celda, útil para impresión o exportación.
     * 
     * @return Carácter del tipo de celda.
     */
    public char getTipo() {
        return simboloOriginal;
    }
}
