package com.jorge_hugo_javier.Controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.jorge_hugo_javier.Model.Cell;
import com.jorge_hugo_javier.Model.Enemigo;
import com.jorge_hugo_javier.Model.JuegoMap;
import com.jorge_hugo_javier.Model.Jugador;

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

    // Método para recibir el jugador desde CreacionPersonaje
    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
        actualizarEstadisticas();

        // Aquí añadimos carga y dibujo del mapa
        cargarMapaDesdeFichero();
        dibujarMapa();
        buscarPosicionInicialJugador();

        // Cargar la vista del juego y pasar datos al GameController
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/jorge_hugo_javier/Vistas/Game.fxml"));
            Parent root = loader.load();
            ControladorDeJuego ControladorDeJuego = loader.getController();
            ControladorDeJuego.setJugador(jugador);
            ControladorDeJuego.setMapa(mapa);

            // Opcional: cambiar la escena actual al nuevo layout
            Stage stage = (Stage) gridMapa.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void actualizarEstadisticas() {
        nombreJugador.setText("Nombre: " + jugador.getNombre());
        saludJugador.setText("Salud: " + jugador.getSalud());
        fuerzaJugador.setText("Fuerza: " + jugador.getFuerza());
        defensaJugador.setText("Defensa: " + jugador.getDefensa());
        velocidadJugador.setText("Velocidad: " + jugador.getVelocidad());
    }

    /**
     * Cargar el mapa desde el archivo Nivel1.txt
     */
    private void cargarMapaDesdeFichero() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/com/jorge_hugo_javier/Mapa/Nivel1.txt")))) {

            List<String> lineas = new ArrayList<>();
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }

            mapa = new JuegoMap(lineas);
            // Ahora que mapa existe, ya podemos añadir enemigos
            Enemigo enemigo1 = new Enemigo("Goblin", 10, 2, 1, 1);
            Enemigo enemigo2 = new Enemigo("Orco", 15, 3, 2, 1);
            mapa.addEnemigo(enemigo1);
            mapa.addEnemigo(enemigo2);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pintar el mapa en el GridPane
     */
    private void dibujarMapa() {
        gridMapa.getChildren().clear();
        gridMapa.getColumnConstraints().clear();
        gridMapa.getRowConstraints().clear();

        Cell[][] grid = mapa.getGrid();
        int numFilas = grid.length;
        int numCols = grid[0].length;

        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConst = new ColumnConstraints(40);
            gridMapa.getColumnConstraints().add(colConst);
        }

        for (int i = 0; i < numFilas; i++) {
            RowConstraints rowConst = new RowConstraints(40);
            gridMapa.getRowConstraints().add(rowConst);
        }

        for (int fila = 0; fila < numFilas; fila++) {
            for (int col = 0; col < numCols; col++) {
                Cell celda = grid[fila][col];

                StackPane panel = new StackPane();
                panel.setPrefSize(40, 40);
                char simbolo = celda.getSimboloOriginal();

                switch (simbolo) {
                    case '#': // Pared
                        Image imgPared = new Image(getClass().getResourceAsStream(
                                "/com/jorge_hugo_javier/Vistas/Pared.jpg"));
                        ImageView viewPared = new ImageView(imgPared);
                        viewPared.setFitWidth(40);
                        viewPared.setFitHeight(40);
                        panel.getChildren().add(viewPared);
                        break;

                    case '.': // Suelo
                        Image imgSuelo = new Image(getClass().getResourceAsStream(
                                "/com/jorge_hugo_javier/Vistas/suelo.png"));
                        ImageView viewSuelo = new ImageView(imgSuelo);
                        viewSuelo.setFitWidth(40);
                        viewSuelo.setFitHeight(40);
                        panel.getChildren().add(viewSuelo);

                        // Jugador
                        if (fila == jugadorFila && col == jugadorCol) {
                            Image imgJugador = new Image(getClass().getResourceAsStream(
                                    "/com/jorge_hugo_javier/Vistas/jugador.png"));
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

    /**
     * Buscar hueco libre en el mapa para colocar al jugador
     */
    private void buscarPosicionInicialJugador() {
        Cell[][] grid = mapa.getGrid();

        for (int fila = 0; fila < grid.length; fila++) {
            for (int col = 0; col < grid[fila].length; col++) {
                if (grid[fila][col].getType() == Cell.Type.FLOOR) {
                    jugadorFila = fila;
                    jugadorCol = col;
                    return;
                }
            }
        }
    }

}