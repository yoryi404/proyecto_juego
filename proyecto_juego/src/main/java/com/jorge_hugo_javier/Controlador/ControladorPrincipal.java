package com.jorge_hugo_javier.Controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

    private Jugador jugador;
    private char[][] mapa;

    // Método para recibir el jugador desde CreacionPersonaje
    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
        actualizarEstadisticas();

        // Aquí añadimos carga y dibujo del mapa
        cargarMapaDesdeFichero();
        dibujarMapa();
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
        List<char[]> filas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/com/jorge_hugo_javier/Mapa/Nivel1.txt")))) {

            String linea;
            while ((linea = br.readLine()) != null) {
                filas.add(linea.toCharArray());
            }

            mapa = filas.toArray(new char[0][]);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pintar el mapa en el GridPane
     */
    private void dibujarMapa() {
        gridMapa.getChildren().clear();

        for (int fila = 0; fila < mapa.length; fila++) {
            for (int col = 0; col < mapa[fila].length; col++) {
                char celda = mapa[fila][col];

                StackPane panel = new StackPane();
                panel.setPrefSize(40, 40);

                //Añadir constraint para que sean del mismo tamaño las columnas y las paredes.

                switch (celda) {
                    case '#': // Pared
                        Image imgPared = new Image(
                                getClass().getResourceAsStream("/com/jorge_hugo_javier/Vistas/Pared.jpg"));
                        ImageView viewPared = new ImageView(imgPared);
                        viewPared.setFitWidth(40);
                        viewPared.setFitHeight(40);
                        panel.getChildren().add(viewPared);
                        break;
                    case '.': // Suelo
                        Image imgSuelo = new Image(
                                getClass().getResourceAsStream("/com/jorge_hugo_javier/Vistas/suelo.png"));
                        ImageView viewSuelo = new ImageView(imgSuelo);
                        viewSuelo.setFitWidth(40);
                        viewSuelo.setFitHeight(40);
                        panel.getChildren().add(viewSuelo);
                        break;
                    default: // Error visual
                        panel.setStyle("-fx-background-color: red;");
                }

                gridMapa.add(panel, col, fila); // ¡Ojo!: (columna, fila)
            }
        }
    }
}