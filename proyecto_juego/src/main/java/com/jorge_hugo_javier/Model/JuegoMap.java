package com.jorge_hugo_javier.Model;


import java.io.*;
import java.util.*;

public class JuegoMap {
    private Cell[][] grid;
    

    public JuegoMap(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }

        int rows = lines.size();
        int cols = lines.get(0).length();
        grid = new Cell[rows][cols];

        for (int i = 0; i < rows; i++) {
            char[] chars = lines.get(i).toCharArray();
            for (int j = 0; j < cols; j++) {
                Cell.Type type = (chars[j] == '#') ? Cell.Type.WALL : Cell.Type.FLOOR;
                grid[i][j] = new Cell(type);
            }
        }
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public Cell getCell(int x, int y) {
        return grid[y][x];
    }

    public boolean isInsideBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < grid.length && y < grid[0].length;
    }

    public void addEnemigo(Enemigo enemigo1) {
        throw new UnsupportedOperationException("Unimplemented method 'addEnemigo'");
    }
}
