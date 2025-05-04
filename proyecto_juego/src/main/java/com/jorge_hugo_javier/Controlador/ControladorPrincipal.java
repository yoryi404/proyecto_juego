package com.jorge_hugo_javier.Controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import com.jorge_hugo_javier.Model.Jugador;

public class ControladorPrincipal {

    @FXML private GridPane gridMapa;
    @FXML private Label nombreJugador;
    @FXML private Label saludJugador;
    @FXML private Label fuerzaJugador;
    @FXML private Label defensaJugador;
    @FXML private Label velocidadJugador;
    @FXML private ListView<String> listaTurnos;

    private Jugador jugador;

    // Método para recibir el jugador desde CreacionPersonaje
    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
        actualizarEstadisticas();
    }

    private void actualizarEstadisticas() {
        nombreJugador.setText("Nombre: " + jugador.getNombre());
        saludJugador.setText("Salud: " + jugador.getSalud());
        fuerzaJugador.setText("Fuerza: " + jugador.getFuerza());
        defensaJugador.setText("Defensa: " + jugador.getDefensa());
        velocidadJugador.setText("Velocidad: " + jugador.getVelocidad());
    }
}