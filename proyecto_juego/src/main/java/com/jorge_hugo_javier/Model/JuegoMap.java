package com.jorge_hugo_javier.Model;

import java.util.*;

public class JuegoMap {

    private Cell[][] grid;
    private List<Enemigo> enemigos = new ArrayList<>();

    public JuegoMap(List<String> lines) {
        int rows = lines.size();
        int cols = lines.get(0).length();
        grid = new Cell[rows][cols];

        for (int i = 0; i < rows; i++) {
            char[] chars = lines.get(i).toCharArray();
            for (int j = 0; j < cols; j++) {
                Cell.Type type = (chars[j] == '#') ? Cell.Type.WALL : Cell.Type.FLOOR;
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

    // ✅ NUEVO MÉTODO para comprobar si un enemigo puede moverse a una celda
    public boolean esPosicionValida(int x, int y) {
        if (!isInsideBounds(x, y)) return false;

        Cell celda = grid[y][x];
        return celda.getType() == Cell.Type.FLOOR && celda.getOccupant() == null;
    }
}