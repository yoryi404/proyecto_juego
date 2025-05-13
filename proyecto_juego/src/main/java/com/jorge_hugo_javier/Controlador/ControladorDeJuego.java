package com.jorge_hugo_javier.Controlador;

import com.jorge_hugo_javier.Model.Enemigo;
import com.jorge_hugo_javier.Model.JuegoCharacter;
import com.jorge_hugo_javier.Model.JuegoMap;
import com.jorge_hugo_javier.Model.Jugador;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.application.Platform;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ControladorDeJuego {

    @FXML
    private GridPane gridPane;

    @FXML
    private Label labelVida; // Label para mostrar la vida

    @FXML
    private Label labelNombre;

    @FXML
    private Label labelFuerza;

    @FXML
    private Label labelDefensa;

    @FXML
    private Label labelVelocidad;

    private Jugador jugador;
    private JuegoMap mapa;

    public void setJugador(Jugador jugador) {
        System.out.println("[DEBUG] setJugador() en ControladorDeJuego ejecutado.");
        this.jugador = jugador;
    }

    public void setMapa(JuegoMap mapa) {
        System.out.println("[DEBUG] setMapa() en ControladorDeJuego ejecutado.");
        this.mapa = mapa;
        jugador.setLimites(mapa.getGrid()[0].length, mapa.getGrid().length);
        actualizarVista();
    }

    /**
     * Manejo de teclado: W A S D para moverse, SPACE para atacar
     */
    public void manejarTeclado(KeyEvent evento) {
        if (jugador.isDead()) {
            return; // Ya está muerto, no puede moverse ni atacar
        }

        switch (evento.getCode()) {
            case W:
                jugador.moverArriba(mapa);
                break;
            case S:
                jugador.moverAbajo(mapa);
                break;
            case A:
                jugador.moverIzquierda(mapa);
                break;
            case D:
                jugador.moverDerecha(mapa);
                break;
            case SPACE:
                atacarEnemigo();
                break;
            default:
                break;
        }

        actualizarVista();
        if (jugador.getHealth() <= 0) {
            guardarEstadisticasJugador(jugador); // ← guardar antes de salir
            irAPantallaDerrota(); // ← cargar la pantalla de derrota
        }

        moverEnemigos();
        if (jugador.isDead()) {
            System.out.println("☠ El jugador ha muerto.");
            guardarEstadisticasYMostrarPantallaDerrota();
        }
    }

    private void guardarEstadisticasYMostrarPantallaDerrota() {
        // Guardar estadísticas
        String ruta = "src/main/resources/com/jorge_hugo_javier/Estadisticas/estadisticas.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ruta, true))) {
            writer.write("Jugador: " + jugador.getNombre() +
                    " | Vida: " + jugador.getHealth() +
                    " | Fuerza: " + jugador.getAttack() +
                    " | Defensa: " + jugador.getDefensa() +
                    " | Velocidad: " + jugador.getVelocidad());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("❌ Error al guardar estadísticas: " + e.getMessage());
        }

        // Cambiar de escena
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/jorge_hugo_javier/Vistas/Derrota.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) gridPane.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                System.err.println("❌ Error al cargar la pantalla de derrota: " + e.getMessage());
            }
        });
    }

    private void mostrarGameOver() {
        guardarEstadisticas();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jorge_hugo_javier/Vistas/Derrota.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) gridPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void guardarEstadisticas() {
        String ruta = "src/main/resources/com/jorge_hugo_javier/Estadisticas/estadisticas.txt";
        String datos = String.format(
                "Jugador: %s | Vida final: %d | Fuerza: %d | Defensa: %d | Velocidad: %d\n",
                jugador.getNombre(), jugador.getHealth(), jugador.getAttack(), jugador.getDefensa(),
                jugador.getVelocidad());

        try {
            java.nio.file.Files.createDirectories(
                    java.nio.file.Paths.get("src/main/resources/com/jorge_hugo_javier/Estadisticas"));
            java.nio.file.Files.write(
                    java.nio.file.Paths.get(ruta),
                    datos.getBytes(),
                    java.nio.file.StandardOpenOption.CREATE,
                    java.nio.file.StandardOpenOption.APPEND);
            System.out.println("[✔] Estadísticas guardadas en: " + ruta);
        } catch (IOException e) {
            System.err.println("Error al guardar estadísticas: " + e.getMessage());
        }
    }

    /**
     * Lógica de movimiento de enemigos
     */
    private void moverEnemigos() {
        for (Enemigo e : mapa.getEnemigos()) {
            if (e.isDead())
                continue;

            if (estanAdyacentes(e, jugador)) {
                jugador.receiveDamage(e.getAttack());
                System.out.println(e.getName() + " ataca al jugador: -" + e.getAttack() + " vida.");

                if (jugador.isDead()) {
                    mostrarFinPartida("Has sido derrotado por " + e.getName() + "...");
                    return;
                }
            } else {
                e.moverHacia(jugador.getX(), jugador.getY(), mapa);
            }
        }
    }

    private void mostrarFinPartida(String mensaje) {
        Platform.runLater(() -> {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Fin del Juego");
            alerta.setHeaderText(null);
            alerta.setContentText("⚔ " + mensaje + "\n\n¡La partida ha terminado!");
            alerta.showAndWait();

            // Salir o volver al menú (aquí puedes personalizar)
            Platform.exit();
        });
    }

    /**
     * Lógica de ataque del jugador al enemigo
     */
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

    /**
     * Verifica si dos personajes están en celdas adyacentes
     */
    private boolean estanAdyacentes(JuegoCharacter a, JuegoCharacter b) {
        int dx = Math.abs(a.getX() - b.getX());
        int dy = Math.abs(a.getY() - b.getY());
        return (dx + dy == 1); // celdas ortogonales
    }

    /**
     * Redibuja el mapa, el jugador y los enemigos
     */
    private void actualizarVista() {
        gridPane.getChildren().clear();
        char[][] celdas = mapa.getMapaChar();

        for (int fila = 0; fila < celdas.length; fila++) {
            for (int col = 0; col < celdas[fila].length; col++) {
                StackPane panel = new StackPane();
                panel.setPrefSize(40, 40);

                // Fondo según la celda
                String imagePath = (celdas[fila][col] == '#')
                        ? "/com/jorge_hugo_javier/Vistas/Pared.jpg"
                        : "/com/jorge_hugo_javier/Vistas/Suelo.png";

                ImageView fondo = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
                fondo.setFitWidth(40);
                fondo.setFitHeight(40);
                panel.getChildren().add(fondo);

                // Dibujar jugador
                if (jugador.getY() == fila && jugador.getX() == col) {
                    ImageView imgJugador = new ImageView(new Image(
                            getClass().getResourceAsStream("/com/jorge_hugo_javier/Vistas/Jugador.jpg")));
                    imgJugador.setFitWidth(35);
                    imgJugador.setFitHeight(35);
                    panel.getChildren().add(imgJugador);
                }

                // Dibujar enemigos
                for (Enemigo enemigo : mapa.getEnemigos()) {
                    if (!enemigo.isDead() && enemigo.getY() == fila && enemigo.getX() == col) {
                        ImageView imgEnemigo = new ImageView(new Image(
                                getClass().getResourceAsStream("/com/jorge_hugo_javier/Vistas/Enemigo.jpg")));
                        imgEnemigo.setFitWidth(35);
                        imgEnemigo.setFitHeight(35);
                        panel.getChildren().add(imgEnemigo);
                    }
                }

                gridPane.add(panel, col, fila);
            }
        }

        // Actualizar la vida del jugador en pantalla
        if (labelVida != null)
            labelVida.setText("Vida: " + jugador.getHealth());
        if (labelNombre != null)
            labelNombre.setText("Nombre: " + jugador.getNombre());
        if (labelFuerza != null)
            labelFuerza.setText("Fuerza: " + jugador.getFuerza());
        if (labelDefensa != null)
            labelDefensa.setText("Defensa: " + jugador.getDefensa());
        if (labelVelocidad != null)
            labelVelocidad.setText("Velocidad: " + jugador.getVelocidad());
    }

    private void irAPantallaDerrota() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/com/jorge_hugo_javier/Vistas/Derrota.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.scene.Scene scene = new javafx.scene.Scene(root);

            // Obtener el Stage desde cualquier nodo (por ejemplo el gridPane)
            javafx.stage.Stage stage = (javafx.stage.Stage) gridPane.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("❌ Error al cargar la pantalla de derrota: " + e.getMessage());
        }
    }

    private void guardarEstadisticasJugador(Jugador jugador) {
        try {
            String ruta = "src/main/resources/com/jorge_hugo_javier/Estadisticas/estadisticas.txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(ruta, true));

            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            writer.write("🕒 " + ahora.format(formato));
            writer.newLine();
            writer.write("→ Nombre: " + jugador.getNombre());
            writer.newLine();
            writer.write("→ Vida restante: " + jugador.getHealth());
            writer.newLine();
            writer.write("→ Fuerza: " + jugador.getAttack() + ", Defensa: " + jugador.getDefensa() + ", Velocidad: "
                    + jugador.getVelocidad());
            writer.newLine();
            writer.write("-----------------------------------------");
            writer.newLine();

            writer.close();
            System.out.println("[✔] Estadísticas guardadas correctamente.");
        } catch (IOException e) {
            System.err.println("❌ Error al guardar estadísticas: " + e.getMessage());
        }
    }
}