/**
 * @author Jorge Alegre Maestre
 * @author Hugo Perez Muñoz
 * @author Javier Gil Garán
 */
package com.jorge_hugo_javier.Model;

import java.util.*;

/**
 * Clase que representa el mapa del juego. El mapa está compuesto por una
 * cuadrícula de celdas
 * donde los personajes pueden moverse. El mapa también puede contener enemigos
 * y permite
 * comprobar si una celda es válida para moverse.
 */
public class JuegoMap {

    private Cell[][] grid;
    private List<Enemigo> enemigos = new ArrayList<>();

    /**
     * Constructor que crea un mapa a partir de una lista de cadenas que representan
     * las filas del mapa.
     * Cada carácter en la cadena se interpreta como un tipo de celda:
     * '#' representa una pared, '.' representa un suelo transitable, y 'T' una trampa.
     *
     * @param lines Lista de cadenas que representan las filas del mapa.
     */
    public JuegoMap(List<String> lines) {
        int rows = lines.size();
        int cols = lines.get(0).length();
        grid = new Cell[rows][cols];

        for (int i = 0; i < rows; i++) {
            char[] chars = lines.get(i).toCharArray();
            for (int j = 0; j < cols; j++) {
                //  Trampa ('^')
                Cell.Type type;
                if (chars[j] == '#') {
                    type = Cell.Type.WALL;
                } else if (chars[j] == '^') {
                    type = Cell.Type.TRAP;
                } else {
                    type = Cell.Type.FLOOR;
                }
                grid[i][j] = new Cell(type, chars[j]);
            }
        }
    }

    public void addEnemigo(Enemigo e) {
        enemigos.add(e);
        if (isInsideBounds(e.getX(), e.getY())) {
            getCell(e.getX(), e.getY()).setOccupant(e);
        }
    }

    public List<Enemigo> getEnemigos() {
        return enemigos;
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public Cell getCell(int x, int y) {
        return grid[y][x];
    }

    public boolean isInsideBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < grid[0].length && y < grid.length;
    }

    public char[][] getMapaChar() {
        int filas = grid.length;
        int columnas = grid[0].length;
        char[][] resultado = new char[filas][columnas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                resultado[i][j] = grid[i][j].getTipo(); // ← Este método debe existir en Cell
            }
        }

        return resultado;
    }

    /**
     * Verifica si una celda es válida para moverse. Una celda es válida si es del
     * tipo FLOOR o TRAP y no está ocupada por otro personaje.
     *
     * @param x Coordenada X de la celda.
     * @param y Coordenada Y de la celda.
     * @return true si la celda es válida para moverse, false en caso contrario.
     */
    public boolean esPosicionValida(int x, int y) {
        if (!isInsideBounds(x, y))
            return false;

        Cell celda = grid[y][x];
        return (celda.getType() == Cell.Type.FLOOR || celda.getType() == Cell.Type.TRAP)
                && celda.getOccupant() == null;
    }
}