package com.jorge_hugo_javier.Model;


import java.io.*;
import java.util.*;

public class Mapa {
    private Celda[][] grid;

    public void Map(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }

        int rows = lines.size();
        int cols = lines.get(0).length();
        grid = new Celda[rows][cols];

        for (int i = 0; i < rows; i++) {
            char[] chars = lines.get(i).toCharArray();
            for (int j = 0; j < cols; j++) {
                Celda.Type type = (chars[j] == '#') ? Celda.Type.WALL : Celda.Type.FLOOR;
                grid[i][j] = new Celda(type);
            }
        }
    }

    public Celda[][] getGrid() {
        return grid;
    }

    public Celda getCell(int x, int y) {
        return grid[y][x]; // y = fila, x = columna
    }

    public boolean isInsideBounds(int x, int y) {
        return y >= 0 && y < grid.length && x >= 0 && x < grid[0].length;
    }
}
