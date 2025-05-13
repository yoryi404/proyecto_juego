package com.jorge_hugo_javier.Controlador;

import com.jorge_hugo_javier.Model.Cell;
import com.jorge_hugo_javier.Model.Enemigo;
import com.jorge_hugo_javier.Model.JuegoCharacter;
import com.jorge_hugo_javier.Model.JuegoMap;
import com.jorge_hugo_javier.Model.Jugador;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ControladorDeJuego {

    @FXML private GridPane gridPane;
    @FXML private Label labelVida;
    @FXML private Label labelNombre;
    @FXML private Label labelFuerza;
    @FXML private Label labelDefensa;
    @FXML private Label labelVelocidad;
    @FXML private Label labelNombreEnemigo;
    @FXML private Label labelVidaEnemigo;
    @FXML private Label labelFuerzaEnemigo;

    private Jugador jugador;
    private JuegoMap mapa;

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public void setMapa(JuegoMap mapa) {
        this.mapa = mapa;
        jugador.setLimites(mapa.getGrid()[0].length, mapa.getGrid().length);
        mapa.getCell(jugador.getX(), jugador.getY()).setOccupant(jugador);
        actualizarVista();
    }

    public void manejarTeclado(KeyEvent evento) {
        if (jugador.isDead()) return;

        int dx = 0, dy = 0;
        switch (evento.getCode()) {
            case W:
                dy = -1;
                break;
            case S:
                dy = 1;
                break;
            case A:
                dx = -1;
                break;
            case D:
                dx = 1;
                break;
            default:
                return; // Ignorar otras teclas // Ignorar otras teclas
        }

        int newX = jugador.getX() + dx;
        int newY = jugador.getY() + dy;

        if (mapa.isInsideBounds(newX, newY)) {
            Cell destino = mapa.getCell(newX, newY);
            JuegoCharacter ocupante = destino.getOccupant();

            if (ocupante instanceof Enemigo) {
                Enemigo enemigo = (Enemigo) ocupante;
                if (enemigo.isAlive()) {
                    enemigo.receiveDamage(jugador.getAttack());
                if (enemigo.isDead()) destino.setOccupant(null);
            }
            } else if (destino.isWalkable()) {
                mapa.getCell(jugador.getX(), jugador.getY()).setOccupant(null);
                jugador.setX(newX);
                jugador.setY(newY);
                destino.setOccupant(jugador);
            }
        }

        actualizarVista();

        if (jugador.getHealth() <= 0) {
            guardarEstadisticasJugador();
            irAPantallaDerrota();
            return;
        }

        moverEnemigos();
        if (jugador.isDead()) guardarEstadisticasYMostrarPantallaDerrota();
    }

    private void moverEnemigos() {
        for (Enemigo e : mapa.getEnemigos()) {
            if (e.isDead()) continue;
            if (estanAdyacentes(e, jugador)) {
                jugador.receiveDamage(e.getAttack());
                if (jugador.isDead()) {
                    mostrarFinPartida("Has sido derrotado por " + e.getName());
                    return;
                }
            } else {
                e.moverHacia(jugador.getX(), jugador.getY(), mapa);
            }
        }
        if (todosLosEnemigosDerrotados()) mostrarPantallaVictoria();
    }

    private void actualizarVista() {
        gridPane.getChildren().clear();
        char[][] celdas = mapa.getMapaChar();

        for (int fila = 0; fila < celdas.length; fila++) {
            for (int col = 0; col < celdas[fila].length; col++) {
                StackPane panel = new StackPane();
                panel.setPrefSize(40, 40);

                String rutaFondo = (celdas[fila][col] == '#') ? "/com/jorge_hugo_javier/Vistas/Pared.jpg" : "/com/jorge_hugo_javier/Vistas/Suelo.png";
                ImageView fondo = new ImageView(new Image(getClass().getResourceAsStream(rutaFondo)));
                fondo.setFitWidth(40); fondo.setFitHeight(40);
                panel.getChildren().add(fondo);

                for (Enemigo enemigo : mapa.getEnemigos()) {
                    if (!enemigo.isDead() && enemigo.getX() == col && enemigo.getY() == fila) {
                        ImageView imgEnemigo = new ImageView(new Image(getClass().getResourceAsStream("/com/jorge_hugo_javier/Vistas/Enemigo.jpg")));
                        imgEnemigo.setFitWidth(35); imgEnemigo.setFitHeight(35);
                        panel.getChildren().add(imgEnemigo);
                    }
                }

                if (jugador.getX() == col && jugador.getY() == fila) {
                    ImageView imgJugador = new ImageView(new Image(getClass().getResourceAsStream("/com/jorge_hugo_javier/Vistas/Jugador.jpg")));
                    imgJugador.setFitWidth(35); imgJugador.setFitHeight(35);
                    panel.getChildren().add(imgJugador);
                }

                gridPane.add(panel, col, fila);
            }
        }

        if (labelVida != null) labelVida.setText("Vida: " + jugador.getHealth());
        if (labelNombre != null) labelNombre.setText("Nombre: " + jugador.getNombre());
        if (labelFuerza != null) labelFuerza.setText("Fuerza: " + jugador.getFuerza());
        if (labelDefensa != null) labelDefensa.setText("Defensa: " + jugador.getDefensa());
        if (labelVelocidad != null) labelVelocidad.setText("Velocidad: " + jugador.getVelocidad());

        Enemigo enemigoCercano = mapa.getEnemigos().stream()
            .filter(e -> !e.isDead() && estanAdyacentes(jugador, e))
            .findFirst().orElse(null);

        if (enemigoCercano != null) {
            if (labelNombreEnemigo != null) labelNombreEnemigo.setText("Enemigo: " + enemigoCercano.getName());
            if (labelVidaEnemigo != null) labelVidaEnemigo.setText("Vida: " + enemigoCercano.getHealth());
            if (labelFuerzaEnemigo != null) labelFuerzaEnemigo.setText("Fuerza: " + enemigoCercano.getAttack());
        } else {
            if (labelNombreEnemigo != null) labelNombreEnemigo.setText("Enemigo: -");
            if (labelVidaEnemigo != null) labelVidaEnemigo.setText("Vida: -");
            if (labelFuerzaEnemigo != null) labelFuerzaEnemigo.setText("Fuerza: -");
        }
    }

    private boolean estanAdyacentes(JuegoCharacter a, JuegoCharacter b) {
        int dx = Math.abs(a.getX() - b.getX());
        int dy = Math.abs(a.getY() - b.getY());
        return dx + dy == 1;
    }

    private boolean todosLosEnemigosDerrotados() {
        return mapa.getEnemigos().stream().allMatch(Enemigo::isDead);
    }

    private void mostrarPantallaVictoria() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jorge_hugo_javier/Vistas/Victoria.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) gridPane.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                System.err.println("Error al cargar pantalla de victoria: " + e.getMessage());
            }
        });
    }

    private void guardarEstadisticasJugador() {
        String ruta = "src/main/resources/com/jorge_hugo_javier/Estadisticas/estadisticas.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ruta, true))) {
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            writer.write("🕒 " + ahora.format(formato)); writer.newLine();
            writer.write("→ Nombre: " + jugador.getNombre()); writer.newLine();
            writer.write("→ Vida restante: " + jugador.getHealth()); writer.newLine();
            writer.write("→ Fuerza: " + jugador.getAttack()); writer.newLine();
            writer.write("→ Defensa: " + jugador.getDefensa()); writer.newLine();
            writer.write("→ Velocidad: " + jugador.getVelocidad()); writer.newLine();
            writer.write("-----------------------------------------"); writer.newLine();
        } catch (IOException e) {
            System.err.println("Error al guardar estadísticas: " + e.getMessage());
        }
    }

    private void irAPantallaDerrota() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jorge_hugo_javier/Vistas/Derrota.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) gridPane.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                System.err.println("Error al cargar pantalla de derrota: " + e.getMessage());
            }
        });
    }

    private void guardarEstadisticasYMostrarPantallaDerrota() {
        guardarEstadisticasJugador();
        irAPantallaDerrota();
    }

    private void mostrarFinPartida(String mensaje) {
        Platform.runLater(() -> {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Fin del Juego");
            alerta.setHeaderText(null);
            alerta.setContentText(mensaje);
            alerta.showAndWait();
        });
    }
}
