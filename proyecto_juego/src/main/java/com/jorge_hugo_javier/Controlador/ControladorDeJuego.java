package com.jorge_hugo_javier.Controlador;

import com.jorge_hugo_javier.Model.Enemigo;
import com.jorge_hugo_javier.Model.JuegoMap;
import com.jorge_hugo_javier.Model.Jugador;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

public class ControladorDeJuego {

    @FXML
    private GridPane gridPane;

    private Jugador jugador;
    private JuegoMap mapa; // CAMBIO: Usamos JuegoMap para poder manejar enemigos

    /**
     * Recibe al jugador desde el menú de creación
     */
    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    /**
     * Recibe el mapa completo con enemigos desde el controlador principal
     */
    public void setMapa(JuegoMap mapa) {
        this.mapa = mapa;

        // Establecer los límites del mapa en el jugador
        jugador.setLimitesMapa(mapa.getAncho(), mapa.getAlto());

        actualizarVista();
    }

    /**
     * Maneja pulsaciones de teclas para mover al jugador con WASD
     */
    public void manejarTeclado(KeyEvent evento) {
        switch (evento.getCode()) {
            case W -> jugador.moverArriba();
            case S -> jugador.moverAbajo();
            case A -> jugador.moverIzquierda();
            case D -> jugador.moverDerecha();
        }

        actualizarVista();
        moverEnemigos(); // Los enemigos reaccionan después del movimiento del jugador
    }

    /**
     * Imprime la posición del jugador (puedes mejorar esto con dibujo en el GridPane)
     */
    private void actualizarVista() {
        System.out.println("Jugador en: " + jugador.getPosX() + "," + jugador.getPosY());
    }

    /**
     * Mueve cada enemigo hacia el jugador
     */
    private void moverEnemigos() {
        for (Enemigo e : mapa.getEnemigos()) {
            e.moverHacia(jugador.getPosX(), jugador.getPosY());
            System.out.println("Enemigo " + e.getNombre() + " se mueve a (" + e.getX() + "," + e.getY() + ")");
        }
    }
}
