package com.jorge_hugo_javier.Controlador;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.image.Image; 

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.jorge_hugo_javier.Model.*;

public class ControladorPrincipal {

    @FXML private GridPane gridMapa;
    @FXML private Label nombreJugador;
    @FXML private Label saludJugador;
    @FXML private Label fuerzaJugador;
    @FXML private Label defensaJugador;
    @FXML private Label velocidadJugador;
    @FXML private ListView<String> listaTurnos;
    @FXML private Label estadisticasJugador;

    private Jugador jugador;
    protected JuegoMap mapa;
    private int jugadorFila;
    private int jugadorCol;

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
        actualizarEstadisticas();

        cargarMapaDesdeFichero();
        buscarPosicionInicialJugador();
        dibujarMapa();

        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/jorge_hugo_javier/Vistas/Game.fxml")
            );
            Parent root = loader.load();

            // 1) Obtener controlador de la nueva vista
            ControladorDeJuego controladorDeJuego = loader.getController();
            controladorDeJuego.setJugador(jugador);
            controladorDeJuego.setMapa(mapa);

            // 2) Crear la escena una sola vez
            Scene escenaJuego = new Scene(root);

            // 3) Registrar el handler de teclado sobre la escena
            //    Usamos un nombre distinto para el parámetro lambda (event)
            escenaJuego.setOnKeyPressed(event -> 
                controladorDeJuego.manejarTeclado(event)
            );

            // 4) Pedir foco al root para capturar teclas
            escenaJuego.getRoot().setFocusTraversable(true);
            escenaJuego.getRoot().requestFocus();

            // 5) Cambiar la escena en el mismo stage
            Platform.runLater(() -> {
                Stage stage = (Stage) gridMapa.getScene().getWindow();
                stage.setScene(escenaJuego);
                stage.show();
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void actualizarEstadisticas() {
        nombreJugador.setText("Nombre: " + jugador.getName());
        saludJugador.setText("Salud: " + jugador.getHealth());
        fuerzaJugador.setText("Fuerza: " + jugador.getAttack());
        defensaJugador.setText("Defensa: " + jugador.getDefensa());
        velocidadJugador.setText("Velocidad: " + jugador.getVelocidad());
    }

    private void cargarMapaDesdeFichero() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                    getClass().getResourceAsStream("/com/jorge_hugo_javier/Mapa/Nivel1.txt")
                )
        )) {
            List<String> lineas = new ArrayList<>();
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
            mapa = new JuegoMap(lineas);

            // Colocar un orco casi al final
            Cell[][] grid = mapa.getGrid();
            int enemyX = grid[0].length - 2;
            int enemyY = grid.length - 2;
            while (!grid[enemyY][enemyX].isWalkable()) {
                if (enemyX > 0) enemyX--;
                else if (enemyY > 0) enemyY--;
                else break;
            }
            Enemigo orco = new Enemigo("Orco", 50, 3, enemyX, enemyY);
            mapa.addEnemigo(orco);
            grid[enemyY][enemyX].setOccupant(orco);

            System.out.println("Mapa cargado correctamente con enemigos.");
        } catch (IOException e) {
            System.err.println("Error al leer el archivo Nivel1.txt");
            e.printStackTrace();
        }
    }

    private void dibujarMapa() {
        gridMapa.getChildren().clear();
        gridMapa.getColumnConstraints().clear();
        gridMapa.getRowConstraints().clear();

        Cell[][] grid = mapa.getGrid();
        int numFilas = grid.length;
        int numCols = grid[0].length;

        for (int i = 0; i < numCols; i++)
            gridMapa.getColumnConstraints().add(new ColumnConstraints(40));
        for (int i = 0; i < numFilas; i++)
            gridMapa.getRowConstraints().add(new RowConstraints(40));

        for (int fila = 0; fila < numFilas; fila++) {
            for (int col = 0; col < numCols; col++) {
                Cell celda = grid[fila][col];
                StackPane panel = new StackPane();
                panel.setPrefSize(40, 40);
                char simbolo = celda.getSimboloOriginal();

                switch (simbolo) {
                    case '#':
                        panel.getChildren().add(createImageView("/com/jorge_hugo_javier/Vistas/Pared.jpg"));
                        break;
                    case '.':
                        panel.getChildren().add(createImageView("/com/jorge_hugo_javier/Vistas/suelo.png"));
                        if (fila == jugador.getY() && col == jugador.getX()) {
                            panel.getChildren().add(createImageView("/com/jorge_hugo_javier/Vistas/jugador.jpg", 35, 35));
                        }
                        break;
                    default:
                        panel.setStyle("-fx-background-color: red;");
                }

                gridMapa.add(panel, col, fila);
            }
        }
    }

    private ImageView createImageView(String path) {
        return createImageView(path, 40, 40);
    }

    private ImageView createImageView(String path, int w, int h) {
        ImageView iv = new ImageView(new Image(getClass().getResourceAsStream(path)));
        iv.setFitWidth(w);
        iv.setFitHeight(h);
        return iv;
    }

    private void buscarPosicionInicialJugador() {
        Cell[][] grid = mapa.getGrid();
        for (int fila = 0; fila < grid.length; fila++) {
            for (int col = 0; col < grid[fila].length; col++) {
                if (grid[fila][col].getType() == Cell.Type.FLOOR) {
                    jugador.setPosicion(col, fila);
                    jugadorFila = fila;
                    jugadorCol = col;
                    System.out.println("Jugador colocado en: X=" + col + ", Y=" + fila);
                    return;
                }
            }
        }
    }
}