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
import javafx.scene.control.Alert;
import javafx.application.Platform;

public class ControladorDeJuego {

    @FXML
    private GridPane gridPane;

    @FXML
    private Label labelVida; // Label para mostrar la vida

    @FXML private Label labelNombre;

    @FXML private Label labelFuerza;

    @FXML private Label labelDefensa;
    
    @FXML private Label labelVelocidad;

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
    // Mover enemigos después de que el jugador se mueva// Verificar si la vida del jugador llegó a 0
    if (jugador.getHealth() <= 0) {
        mostrarGameOver();
    }

    moverEnemigos();
    }

    private void mostrarGameOver() {
        javafx.scene.control.Alert alerta = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alerta.setTitle("Game Over");
        alerta.setHeaderText(null);
        alerta.setContentText("⚔ Se ha finalizado la partida. El jugador ha muerto.");
        alerta.setOnHidden(e -> System.exit(0)); // Cierra la aplicación tras cerrar el diálogo
        alerta.show();
    }

    /**
     * Lógica de movimiento de enemigos
     */
    private void moverEnemigos() {
    for (Enemigo e : mapa.getEnemigos()) {
        if (e.isDead()) continue;

        if (estanAdyacentes(e, jugador)) {
            jugador.receiveDamage(e.getAttack());
            System.out.println(e.getName() + " ataca al jugador: -" + e.getAttack() + " vida.");

            if (jugador.isDead()) {
                mostrarFinPartida("Has sido derrotado por " + e.getName() + "...");
                return;
            }
        } else {
            e.moverHacia(jugador.getX(), jugador.getY());
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
        if (labelVida != null) labelVida.setText("Vida: " + jugador.getHealth());
        if (labelNombre != null) labelNombre.setText("Nombre: " + jugador.getNombre());
        if (labelFuerza != null) labelFuerza.setText("Fuerza: " + jugador.getFuerza());
        if (labelDefensa != null) labelDefensa.setText("Defensa: " + jugador.getDefensa());
        if (labelVelocidad != null) labelVelocidad.setText("Velocidad: " + jugador.getVelocidad());
    }
}