package com.jorge_hugo_javier.Controlador;

import com.jorge_hugo_javier.Model.Cell;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;


public class ControladorDeJuego {

    @FXML
    private GridPane gridPane;

    @FXML
    private Label labelVidaTexto;

    @FXML
    private Label labelVidaJugador;

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

    @FXML
    private Label turnLabel;        // Para mostrar quién actúa
    private Queue<JuegoCharacter> turnQueue;
    private boolean isPlayerTurn;   // Para saber si toca al jugador
    
    private void initTurnQueue() {
        List<JuegoCharacter> actores = new ArrayList<>();
        actores.add(jugador);
        actores.addAll(mapa.getEnemigos());
        // Orden descendente por velocidad
        actores.sort(Comparator.comparingInt(JuegoCharacter::getVelocidad).reversed());
        turnQueue = new LinkedList<>(actores);
    }

    private void nextTurn() {
        if (turnQueue.isEmpty()) return;
        JuegoCharacter actor = turnQueue.poll();
        turnLabel.setText("Turno de: " + actor.getNombre());
        if (actor instanceof Jugador) {
            isPlayerTurn = true;
        } else {
            isPlayerTurn = false;
            moverUnEnemigo((Enemigo) actor);
            endTurn(actor);
        }
    }

    private void endTurn(JuegoCharacter actor) {
        turnQueue.offer(actor);
        nextTurn();
    }

    public void setJugador(Jugador jugador) {
        System.out.println("[DEBUG] setJugador() en ControladorDeJuego ejecutado.");
        this.jugador = jugador;
    }

    public void setMapa(JuegoMap mapa) {
     System.out.println("[DEBUG] setMapa() en ControladorDeJuego ejecutado.");
     this.mapa = mapa;
     jugador.setLimites(mapa.getGrid()[0].length, mapa.getGrid().length);
     actualizarVista();
     initTurnQueue();
     nextTurn();
    }

    /**
     * Manejo de teclado: W A S D para moverse, SPACE para atacar
     */
    @FXML
    public void manejarTeclado(KeyEvent evento) {
        System.out.println("[DEBUG] tecla pulsada: " + evento.getCode());
        if (!isPlayerTurn) return;
        if (jugador.isDead()) {
            return; // No puede moverse si está muerto
        }

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
            Cell celdaDestino = mapa.getCell(newX, newY);
            JuegoCharacter objetivo = celdaDestino.getOccupant();

            if (objetivo instanceof Enemigo && objetivo.isAlive()) {
                Enemigo enemigo = (Enemigo) objetivo;
                int daño = jugador.getAttack();
                enemigo.receiveDamage(daño);
                System.out.println("Atacaste a " + enemigo.getName() + ", vida restante: " + enemigo.getHealth());
            } else if (celdaDestino.isWalkable()) {
                jugador.setX(newX);
                jugador.setY(newY);
            }
        }

        actualizarVista();
        isPlayerTurn = false;
        endTurn(jugador);

        if (jugador.getHealth() <= 0) {
            guardarEstadisticasJugador(jugador);
            irAPantallaDerrota();
            return;
        }

        moverEnemigos();

        if (jugador.isDead()) {
            System.out.println("☠ El jugador ha muerto.");
            guardarEstadisticasYMostrarPantallaDerrota();
        }
    }

    private void guardarEstadisticasYMostrarPantallaDerrota() {
        // Guardar estadisticas
        String ruta = "src/main/resources/com/jorge_hugo_javier/Estadisticas/estadisticas.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ruta, true))) {
            writer.write("Jugador: " + jugador.getNombre() +
                    " | Vida: " + jugador.getHealth() +
                    " | Fuerza: " + jugador.getAttack() +
                    " | Defensa: " + jugador.getDefensa() +
                    " | Velocidad: " + jugador.getVelocidad());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error al guardar estadísticas: " + e.getMessage());
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
            if (e.isDead()) continue;

            if (estanAdyacentes(e, jugador)) {
                jugador.receiveDamage(e.getAttack());
                if (jugador.isDead()) {
                    mostrarFinPartida("Has sido derrotado por " + e.getName() + "...");
                    return;
                }
            } else {
                // 🆕 Si el jugador está dentro del rango 3x3, el enemigo lo persigue
                if (estaEnRango3x3(e)) {
                    e.moverHacia(jugador.getX(), jugador.getY(), mapa);
                } else {
                    moverAleatorio(e); // 🆕 Movimiento aleatorio si está lejos
                }
            }
        }

        if (todosLosEnemigosDerrotados()) {
            mostrarPantallaVictoria();
        }
    }

    private void moverUnEnemigo(Enemigo e) {
        // Copia tu lógica de un enemigo, p.ej.:
        if (estanAdyacentes(e, jugador)) {
            jugador.receiveDamage(e.getAttack());
        } else if (estaEnRango3x3(e)) {
            e.moverHacia(jugador.getX(), jugador.getY(), mapa);
        } else {
            moverAleatorio(e);
        }
        actualizarVista();
    }

    // 🆕 Verifica si el jugador está en un área de 3x3 alrededor del enemigo
    private boolean estaEnRango3x3(Enemigo enemigo) {
        int dx = Math.abs(enemigo.getX() - jugador.getX());
        int dy = Math.abs(enemigo.getY() - jugador.getY());
        return dx <= 1 && dy <= 1;
    }

    // 🆕 Mueve al enemigo a una celda adyacente aleatoria válida
    private void moverAleatorio(Enemigo enemigo) {
        Random rand = new Random();
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
        List<int[]> opciones = new ArrayList<>();

        for (int[] dir : dirs) {
            int nuevoX = enemigo.getX() + dir[0];
            int nuevoY = enemigo.getY() + dir[1];
            if (mapa.isInsideBounds(nuevoX, nuevoY)
                    && mapa.getCell(nuevoX, nuevoY).isWalkable()
                    && mapa.getCell(nuevoX, nuevoY).getOccupant() == null) {
                opciones.add(dir);
            }
        }

        if (!opciones.isEmpty()) {
            int[] mov = opciones.get(rand.nextInt(opciones.size()));
            int nuevoX = enemigo.getX() + mov[0];
            int nuevoY = enemigo.getY() + mov[1];
            mapa.getCell(enemigo.getX(), enemigo.getY()).setOccupant(null);
            enemigo.setX(nuevoX);
            enemigo.setY(nuevoY);
            mapa.getCell(nuevoX, nuevoY).setOccupant(enemigo);
        }
    

        boolean todosMuertos = mapa.getEnemigos().stream().allMatch(Enemigo::isDead);
        if (todosMuertos) {
            mostrarPantallaVictoria();
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
                    verificarVictoria();
                }
            }
        }
        // Después de derrotar enemigos, comprobar si ganaste
        if (todosLosEnemigosDerrotados()) {
            System.out.println("🎉 Todos los enemigos han sido derrotados.");
            mostrarPantallaVictoria();
        }
    }

    private boolean todosLosEnemigosDerrotados() {
        return mapa.getEnemigos().stream().allMatch(Enemigo::isDead);
    }

    private void mostrarPantallaVictoria() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jorge_hugo_javier/Vistas/Victoria.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) gridPane.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        System.out.println("✅ Se ha mostrado la pantalla de victoria.");
    } catch (IOException e) {
        System.err.println("❌ Error al cargar la pantalla de victoria: " + e.getMessage());
    }
    }



    /**
     * 
     * @param a
     * @param b
     * @return
     */
    private boolean estanAdyacentes(JuegoCharacter a, JuegoCharacter b) {
        int dx = Math.abs(a.getX() - b.getX());
        int dy = Math.abs(a.getY() - b.getY());
        return (dx + dy == 1);
    }

    private void cargarNuevoNivel(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream(rutaArchivo)))) {
            List<String> lineas = new ArrayList<>();
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }

            mapa = new JuegoMap(lineas);
            mapa.addEnemigo(new Enemigo("Orco Jefe", 20, 5, 5, 5));

            jugador.setPosicion(0, 0);
            jugador.setLimites(mapa.getGrid()[0].length, mapa.getGrid().length);

            actualizarVista();

        } catch (IOException e) {
            System.err.println("Error al cargar el nuevo nivel");
            e.printStackTrace();
        }
    }

    private void verificarVictoria() {
        boolean todosMuertos = mapa.getEnemigos().stream().allMatch(e -> !e.isAlive());

        if (todosMuertos) {
            System.out.println("🎉 ¡Has derrotado a todos los enemigos!");

            mostrarPantallaVictoria();

            cargarNuevoNivel("/com/jorge_hugo_javier/Mapa/Nivel2.txt");
        }
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

                if (todosLosEnemigosDerrotados()) {
                    guardarEstadisticasVictoria();
                    mostrarPantallaVictoria();
                }

                gridPane.add(panel, col, fila);
            }
        }

        // Actualizar la vida del jugador en pantalla
        if (labelVidaTexto!= null)
            labelVidaTexto.setText("Vida: " + jugador.getHealth());
        if (labelVidaJugador != null) 
            labelVidaJugador.setText("Salud: " + jugador.getHealth());
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

    private void guardarEstadisticasVictoria() {
    String ruta = "src/main/resources/com/jorge_hugo_javier/Estadisticas/estadisticas.txt";

    String linea = jugador.getNombre() + " | Salud final: " + jugador.getHealth() +
                   " | Fuerza: " + jugador.getAttack() +
                   " | Defensa: " + jugador.getDefensa() +
                   " | Velocidad: " + jugador.getVelocidad() +
                   " | Resultado: VICTORIA";

    try {
        java.nio.file.Files.write(java.nio.file.Paths.get(ruta),
                (linea + System.lineSeparator()).getBytes(), java.nio.file.StandardOpenOption.CREATE,
                java.nio.file.StandardOpenOption.APPEND);
        System.out.println("✅ Estadísticas de victoria guardadas.");
    } catch (IOException e) {
        System.err.println("❌ Error al guardar estadísticas de victoria: " + e.getMessage());
    }
}
}
