package com.jorge_hugo_javier.Controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.jorge_hugo_javier.Model.*;

public class ControladorPrincipal {

    @FXML
    private GridPane gridMapa;
    @FXML
    private Label nombreJugador;
    @FXML
    private Label saludJugador;
    @FXML
    private Label fuerzaJugador;
    @FXML
    private Label defensaJugador;
    @FXML
    private Label velocidadJugador;
    @FXML
    private ListView<String> listaTurnos;
    @FXML
    private Label estadisticasJugador;

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jorge_hugo_javier/Vistas/Game.fxml"));
            Parent root = loader.load();

            // Obtenemos el controlador de la nueva vista
            ControladorDeJuego controladorDeJuego = loader.getController();
            controladorDeJuego.setJugador(jugador);
            controladorDeJuego.setMapa(mapa);

            // Creamos y configuramos la nueva escena
            Scene escenaJuego = new Scene(root);

            // Esperamos a que gridMapa tenga escena antes de hacer el cambio
            javafx.application.Platform.runLater(() -> {
                Stage stage = (Stage) gridMapa.getScene().getWindow();
                stage.setScene(escenaJuego);
                escenaJuego.getRoot().requestFocus();
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
                new InputStreamReader(getClass().getResourceAsStream("/com/jorge_hugo_javier/Mapa/Nivel1.txt")))) {

            List<String> lineas = new ArrayList<>();
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }

            mapa = new JuegoMap(lineas);
            // Encuentra una posición alejada del jugador para el Orco
            Cell[][] grid = mapa.getGrid();
            int enemyX = grid[0].length - 2; // casi al final horizontal
            int enemyY = grid.length - 2; // casi al final vertical

            // Asegúrate de que sea una celda walkable
            while (!grid[enemyY][enemyX].isWalkable()) {
                if (enemyX > 0)
                    enemyX--;
                else if (enemyY > 0)
                    enemyY--;
                else
                    break;
            }

            Enemigo orco = new Enemigo("Orco", 50, 3, enemyX, enemyY);
            mapa.addEnemigo(orco);
            grid[enemyY][enemyX].setOccupant(orco); // coloca al orco en esa celda

            System.out.println(" Mapa cargado correctamente con enemigos.");

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

        for (int i = 0; i < numCols; i++) {
            gridMapa.getColumnConstraints().add(new ColumnConstraints(40));
        }
        for (int i = 0; i < numFilas; i++) {
            gridMapa.getRowConstraints().add(new RowConstraints(40));
        }

        for (int fila = 0; fila < numFilas; fila++) {
            for (int col = 0; col < numCols; col++) {
                Cell celda = grid[fila][col];
                StackPane panel = new StackPane();
                panel.setPrefSize(40, 40);
                char simbolo = celda.getSimboloOriginal();

                switch (simbolo) {
                    case '#':
                        Image imgPared = new Image(getClass().getResourceAsStream(
                                "/com/jorge_hugo_javier/Vistas/Pared.jpg"));
                        ImageView viewPared = new ImageView(imgPared);
                        viewPared.setFitWidth(40);
                        viewPared.setFitHeight(40);
                        panel.getChildren().add(viewPared);
                        break;

                    case '.':
                        Image imgSuelo = new Image(getClass().getResourceAsStream(
                                "/com/jorge_hugo_javier/Vistas/suelo.png"));
                        ImageView viewSuelo = new ImageView(imgSuelo);
                        viewSuelo.setFitWidth(40);
                        viewSuelo.setFitHeight(40);
                        panel.getChildren().add(viewSuelo);

                        if (fila == jugador.getY() && col == jugador.getX()) {
                            Image imgJugador = new Image(getClass().getResourceAsStream(
                                    "/com/jorge_hugo_javier/Vistas/jugador.jpg"));
                            ImageView viewJugador = new ImageView(imgJugador);
                            viewJugador.setFitWidth(35);
                            viewJugador.setFitHeight(35);
                            panel.getChildren().add(viewJugador);
                        }
                        break;

                    default:
                        panel.setStyle("-fx-background-color: red;");
                }

                gridMapa.add(panel, col, fila);
            }
        }
    }

    private void buscarPosicionInicialJugador() {
        Cell[][] grid = mapa.getGrid();

        for (int fila = 0; fila < grid.length; fila++) {
            for (int col = 0; col < grid[fila].length; col++) {
                if (grid[fila][col].getType() == Cell.Type.FLOOR) {
                    jugadorFila = fila;
                    jugadorCol = col;
                    jugador.setPosicion(col, fila);
                    System.out.println(" Jugador colocado en: X = " + col + ", Y = " + fila);
                    return;
                }
            }
        }
    }
}