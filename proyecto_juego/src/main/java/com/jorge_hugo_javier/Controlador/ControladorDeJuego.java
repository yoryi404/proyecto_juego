package com.jorge_hugo_javier.Controlador;

import com.jorge_hugo_javier.Model.Enemigo;
import com.jorge_hugo_javier.Model.JuegoCharacter;
import com.jorge_hugo_javier.Model.JuegoMap;
import com.jorge_hugo_javier.Model.Jugador;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class ControladorDeJuego {

    @FXML
    private GridPane gridPane;

    @FXML
    private Label labelVida; // ← nuevo Label para mostrar la vida

    private Jugador jugador;
    private JuegoMap mapa;

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public void setMapa(JuegoMap mapa) {
        this.mapa = mapa;
        jugador.setLimites(mapa.getGrid()[0].length, mapa.getGrid().length);
        actualizarVista();
    }

    public void manejarTeclado(KeyEvent evento) {
        switch (evento.getCode()) {
    case W:
        jugador.moverArriba();
        break;
    case S:
        jugador.moverAbajo();
        break;
    case A:
        jugador.moverIzquierda();
        break;
    case D:
        jugador.moverDerecha();
        break;
    case SPACE:
        atacarEnemigo();
        break;
    default:
        break;
    }

        actualizarVista();
        moverEnemigos();
    }

    private void moverEnemigos() {
        for (Enemigo e : mapa.getEnemigos()) {
            if (e.isDead()) continue;

            if (estanAdyacentes(e, jugador)) {
                jugador.receiveDamage(e.getAttack());
                System.out.println(e.getName() + " ataca al jugador: -" + e.getAttack() + " vida.");
            } else {
                e.moverHacia(jugador.getX(), jugador.getY());
            }
        }
    }

    private void atacarEnemigo() {
        for (Enemigo enemigo : mapa.getEnemigos()) {
            if (!enemigo.isDead() && estanAdyacentes(jugador, enemigo)) {
                enemigo.receiveDamage(jugador.getAttack());
                System.out.println("Atacaste a " + enemigo.getName() + ", vida restante: " + enemigo.getHealth());

                if (enemigo.isDead()) {
                    System.out.println(enemigo.getName() + " ha sido derrotado.");
                    mapa.getCell(enemigo.getX(), enemigo.getY()).setOccupant(null);
                }
            }
        }
    }

    private boolean estanAdyacentes(JuegoCharacter a, JuegoCharacter b) {
        int dx = Math.abs(a.getX() - b.getX());
        int dy = Math.abs(a.getY() - b.getY());
        return (dx + dy == 1); // vecinos ortogonales
    }

    private void actualizarVista() {
        gridPane.getChildren().clear();
        char[][] celdas = mapa.getMapaChar();

        for (int fila = 0; fila < celdas.length; fila++) {
            for (int col = 0; col < celdas[fila].length; col++) {
                StackPane panel = new StackPane();
                panel.setPrefSize(40, 40);

                char celda = celdas[fila][col];
                String imagePath = (celda == '#')
                        ? "/com/jorge_hugo_javier/Vistas/Pared.jpg"
                        : "/com/jorge_hugo_javier/Vistas/Suelo.png";

                ImageView fondo = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
                fondo.setFitWidth(40);
                fondo.setFitHeight(40);
                panel.getChildren().add(fondo);

                if (jugador.getY() == fila && jugador.getX() == col) {
                    ImageView imgJugador = new ImageView(new Image(
                            getClass().getResourceAsStream("/com/jorge_hugo_javier/Vistas/jugador.png")));
                    imgJugador.setFitWidth(35);
                    imgJugador.setFitHeight(35);
                    panel.getChildren().add(imgJugador);
                }

                for (Enemigo enemigo : mapa.getEnemigos()) {
                    if (!enemigo.isDead() && enemigo.getY() == fila && enemigo.getX() == col) {
                        ImageView imgEnemigo = new ImageView(new Image(
                                getClass().getResourceAsStream("/com/jorge_hugo_javier/Vistas/enemigo.png")));
                        imgEnemigo.setFitWidth(35);
                        imgEnemigo.setFitHeight(35);
                        panel.getChildren().add(imgEnemigo);
                    }
                }

                gridPane.add(panel, col, fila);
            }
        }

        // Actualizar vida en el Label
        if (labelVida != null) {
            labelVida.setText("Vida: " + jugador.getHealth());
        }
    }
}