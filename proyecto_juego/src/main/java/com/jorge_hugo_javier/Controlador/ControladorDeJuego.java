/**
 * @author Jorge Alegre Maestre
 * @author Hugo Perez Muñoz
 * @author Javier Gil Garán
 */
package com.jorge_hugo_javier.Controlador;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import com.jorge_hugo_javier.Model.Cell;
import com.jorge_hugo_javier.Model.Enemigo;
import com.jorge_hugo_javier.Model.JuegoCharacter;
import com.jorge_hugo_javier.Model.JuegoMap;
import com.jorge_hugo_javier.Model.Jugador;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.jorge_hugo_javier.Observer.Observer;
import com.jorge_hugo_javier.Model.Enemigo;

/**
 * Controlador principal del juego que gestiona la lógica de combate,
 * los turnos y la interacción entre el jugador, los enemigos y el mapa.
 * Utiliza JavaFX para enlazar con la vista, incluyendo etiquetas, barras de
 * vida,
 * y botones, para representar visualmente el estado del juego.
 */

public class ControladorDeJuego implements Observer {
    /** Barra de progreso que representa la vida del jugador. */
    @FXML
    private ProgressBar vidaJugadorBarra;
    /** Barra de progreso que representa la vida del enemigo. */
    @FXML
    private ProgressBar vidaEnemigoBarra;
    /** Etiqueta que muestra información de la vida del jugador. */
    @FXML
    private Label labelVidaTexto;
    /** Etiqueta con el nombre del enemigo actual. */
    @FXML
    private Label labelNombreEnemigo;
    /** Etiqueta con los puntos de vida actuales del enemigo. */
    @FXML
    private Label labelVidaEnemigo;

    /** Etiqueta que muestra la fuerza del enemigo. */
    @FXML
    private Label labelFuerzaEnemigo;

    /** Contenedor del mapa o área del juego. */
    @FXML
    private GridPane gridPane;

    /** Etiqueta con la vida del jugador. */
    @FXML
    private Label labelVidaJugador;

    /** Etiqueta con el nombre del jugador. */
    @FXML
    private Label labelNombre;

    /** Etiqueta que muestra la fuerza del jugador. */
    @FXML
    private Label labelFuerza;

    /** Etiqueta que muestra la defensa del jugador. */
    @FXML
    private Label labelDefensa;

    /** Etiqueta que muestra la velocidad del jugador. */
    @FXML
    private Label labelVelocidad;

    /** Referencia al jugador. */
    private Jugador jugador;

    /** Mapa actual del juego. */
    private JuegoMap mapa;

    /** Etiqueta que indica de quién es el turno actual. */
    @FXML
    private Label turnLabel;

    /** Cola de turnos de los personajes en juego. */
    private Queue<JuegoCharacter> turnQueue;

    /** Indica si es el turno del jugador. */
    private boolean isPlayerTurn;

    /** Botón para cargar el segundo nivel. */
    @FXML
    private Button btnNivel2;

    /**
     * 
     * @param event Evento de acción asociado al botón.
     */
    @FXML
    private void cargarNivel2(ActionEvent event) {
        // Ruta a tu fichero Nivel2.txt en resources
        String rutaNivel2 = "/com/jorge_hugo_javier/Mapa/Nivel2.txt";

        // Reutilizamos tu método para cargar un nuevo nivel:
        cargarNuevoNivel(rutaNivel2);

        /**
         * Inicializa la cola de turnos ordenando por velocidad de mayor a menor.
         */
        initTurnQueue();

        /**
         * Pasa al siguiente turno del juego. Determina si el actor actual es el jugador
         * o un enemigo.
         */
        nextTurn();
    }

    /** Bandera para evitar mostrar alertas múltiples. */
    private boolean alertaMostrada = false;

    private void initTurnQueue() {
        List<JuegoCharacter> actores = new ArrayList<>();
        actores.add(jugador);
        actores.addAll(mapa.getEnemigos());
        // Orden descendente por velocidad
        actores.sort(Comparator.comparingInt(JuegoCharacter::getVelocidad).reversed());
        turnQueue = new LinkedList<>(actores);
    }

    /** Nombre del nivel actual cargado. */
    private String nivelActual = "Nivel1.txt";

    /**
     * Verifica si ya no quedan enemigos. Si no hay más, cambia de nivel o muestra
     * la pantalla de victoria.
     */
    private void onNoQuedenEnemigos() {
        if ("Nivel1.txt".equals(nivelActual)) {
            // Pasamos al nivel 2
            nivelActual = "Nivel2.txt";
            cargarNuevoNivel("/com/jorge_hugo_javier/Mapa/" + nivelActual);
        } else {
            // Victoria final: mostramos pantalla de victoria
            Platform.runLater(() -> {
                try {
                    Parent root = new FXMLLoader(
                            getClass().getResource("/com/jorge_hugo_javier/Vistas/Victoria.fxml")).load();
                    Stage stage = (Stage) gridPane.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    System.err.println("Error al cargar Victoria.fxml: " + e.getMessage());
                }
            });
        }
    }

    private void nextTurn() {
        if (turnQueue.isEmpty()) {
            onNoQuedenEnemigos();
            return;
        }
        JuegoCharacter actor = turnQueue.poll();
        if (actor.isDead()) {
            nextTurn();
            return;
        }
        turnLabel.setText("Turno de: " + actor.getNombre());

        if (actor instanceof Jugador) {
            isPlayerTurn = true;
        } else {
            isPlayerTurn = false;
            // Damos un pequeño delay para que JavaFX pinte el label
            PauseTransition pausa = new PauseTransition(Duration.millis(300));
            pausa.setOnFinished(evt -> {
                moverUnEnemigo((Enemigo) actor);
                endTurn(actor);
            });
            pausa.play();
        }
    }

    /**
     * 
     * @param actor Personaje cuyo turno acaba de terminar.
     */
    private void endTurn(JuegoCharacter actor) {
        if (!actor.isDead()) {
            turnQueue.offer(actor);
        }
        Platform.runLater(this::nextTurn);
    }

    /**
     * Establece el jugador del juego.
     * 
     * @param jugador Objeto de tipo Jugador.
     */
    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
        jugador.addObserver(this);
        System.out.println("[DEBUG] setJugador() en ControladorDeJuego ejecutado.");
        this.jugador = jugador;
    }

    /**
     * Establece el mapa y actualiza vista, límites y enemigos observados.
     * 
     * @param mapa Mapa del juego.
     */
    public void setMapa(JuegoMap mapa) {
        this.mapa = mapa;
        System.out.println("[DEBUG] setMapa() en ControladorDeJuego ejecutado.");
        this.mapa = mapa;
        jugador.setLimites(mapa.getGrid()[0].length, mapa.getGrid().length);
        actualizarVista();
        initTurnQueue();
        nextTurn();
        for (Enemigo e : mapa.getEnemigos()) {
            e.addObserver(this);
        }
        initTurnQueue();
        nextTurn();
    }

    /**
     * Maneja el evento de teclado para mover al jugador o atacar.
     * W/A/S/D: direcciones.
     * 
     * @param evento Evento de tecla presionada.
     */
    @FXML
    public void manejarTeclado(KeyEvent evento) {
        System.out.println("[DEBUG] tecla pulsada: " + evento.getCode());
        if (!isPlayerTurn)
            return;
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

        Cell celdaDestino = mapa.getCell(newX, newY);
        JuegoCharacter objetivo = celdaDestino.getOccupant();

        if (objetivo instanceof Enemigo && objetivo.isAlive()) {
            // Si hay un enemigo vivo, atacamos
            Enemigo enemigo = (Enemigo) objetivo;
            int daño = jugador.getAttack();
            enemigo.receiveDamage(daño);
            System.out.println("Atacaste a " + enemigo.getName() +
                    ", vida restante: " + enemigo.getHealth());
        }
        // Solo si es una celda válida (suelo, dentro de límites y sin ocupante)
        else if (mapa.esPosicionValida(newX, newY)) {
            // Desocupa la celda actual del jugador
            mapa.getCell(jugador.getX(), jugador.getY()).setOccupant(null);
            // Mueve al jugador
            jugador.setX(newX);
            jugador.setY(newY);
            // Ocupa la nueva celda
            mapa.getCell(newX, newY).setOccupant(jugador);
        }

        actualizarVista();
        isPlayerTurn = false;
        endTurn(jugador);

        if (jugador.getHealth() <= 0) {
            mostrarDerrota("Has sido derrotado por " /* nombre enemigo */);
            return;
        }

        moverEnemigos();

        if (jugador.isDead()) {
            System.out.println("El jugador ha muerto.");
            guardarEstadisticasYMostrarPantallaDerrota();
        }
    }

    /**
     * Guarda estadísticas y muestra la pantalla de derrota.
     */
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
                System.err.println("Error al cargar la pantalla de derrota: " + e.getMessage());
            }
        });
    }

    /**
     * Muestra directamente la pantalla de derrota y guarda estadísticas.
     */
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

    /**
     * Guarda las estadísticas del jugador en un archivo.
     */
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
            System.out.println("Estadísticas guardadas en: " + ruta);
        } catch (IOException e) {
            System.err.println("Error al guardar estadísticas: " + e.getMessage());
        }
    }

    /**
     * Ejecuta la lógica de movimiento y ataque para todos los enemigos vivos.
     */
    private void moverEnemigos() {
        for (Enemigo e : mapa.getEnemigos()) {
            if (e.isDead())
                continue;

            if (estanAdyacentes(e, jugador)) {
                jugador.receiveDamage(e.getAttack());
                actualizarBarrasYDatos();
                if (jugador.isDead()) {
                    mostrarDerrota("Has sido derrotado por " + e.getName() + "...");
                    return;
                }
            } else {
                if (estaEnRango3x3(e)) {
                    e.moverHacia(jugador.getX(), jugador.getY(), mapa);
                } else {
                    moverAleatorio(e);
                }
            }
        }

        if (todosLosEnemigosDerrotados()) {
            mostrarPantallaVictoria();
        }
    }

    /**
     * Mueve un solo enemigo según su proximidad al jugador.
     */
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

    // Verifica si el jugador está en un área de 3x3 alrededor del enemigo
    private boolean estaEnRango3x3(Enemigo enemigo) {
        int dx = Math.abs(enemigo.getX() - jugador.getX());
        int dy = Math.abs(enemigo.getY() - jugador.getY());
        return dx <= 1 && dy <= 1;
    }

    /**
     * Mueve al enemigo a una celda adyacente aleatoria válida.
     */
    private void moverAleatorio(Enemigo enemigo) {
        Random rand = new Random();
        int[][] dirs = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        List<int[]> opciones = new ArrayList<>();

        for (int[] dir : dirs) {
            int nuevoX = enemigo.getX() + dir[0];
            int nuevoY = enemigo.getY() + dir[1];
            if (mapa.isInsideBounds(nuevoX, nuevoY)
                    && mapa.getCell(nuevoX, nuevoY).isWalkable()
                    && mapa.getCell(nuevoX, nuevoY).getOccupant() == null) {
                opciones.add(dir);
            }
            int nx = enemigo.getX() + dir[0];
            int ny = enemigo.getY() + dir[1];
            // Usamos esPosicionValida para validar límites, suelo y ocupante
            if (mapa.esPosicionValida(nx, ny)) {
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

    /**
     * Muestra la pantalla de derrota (Derrota.fxml), igual que en nivel 1.
     * 
     * @param mensaje Mensaje que quieras pasar (puedes ignorarlo o usarlo para el
     *                Alert previo).
     */
    private void mostrarDerrota(final String mensaje) {
        Platform.runLater(() -> {
            try {
                // Opcional: mostrar primero un Alert
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("¡Has sido derrotado!");
                alerta.setHeaderText(null);
                alerta.setContentText("⚔ " + mensaje + "\n\nLa partida ha terminado.");
                alerta.showAndWait();

                // Cargar la pantalla de Derrota.fxml
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/jorge_hugo_javier/Vistas/Derrota.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) gridPane.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                System.err.println("Error al cargar Derrota.fxml: " + e.getMessage());
            }
        });
    }

    /**
     * Lógica de ataque del jugador a enemigos adyacentes.
     */
    private void atacarEnemigo() {
        for (Enemigo enemigo : mapa.getEnemigos()) {
            if (!enemigo.isDead() && estanAdyacentes(jugador, enemigo)) {
                enemigo.receiveDamage(jugador.getAttack());
                actualizarBarrasYDatos();
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
            System.out.println("Todos los enemigos han sido derrotados.");
            mostrarPantallaVictoria();
        }
    }

    /**
     * Verifica si todos los enemigos han sido derrotados.
     */
    private boolean todosLosEnemigosDerrotados() {
        return mapa.getEnemigos().stream().allMatch(Enemigo::isDead);
    }

    /**
     * Muestra la pantalla de victoria al jugador.
     */
    private void mostrarPantallaVictoria() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jorge_hugo_javier/Vistas/Victoria.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) gridPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            System.out.println("Se ha mostrado la pantalla de victoria.");
        } catch (IOException e) {
            System.err.println("Error al cargar la pantalla de victoria: " + e.getMessage());
        }
    }

    /**
     * Verifica si dos personajes están en celdas adyacentes (horizontal o
     * verticalmente).
     * 
     * @param a Primer personaje.
     * @param b Segundo personaje.
     * @return true si están adyacentes, false en caso contrario.
     */
    private boolean estanAdyacentes(JuegoCharacter a, JuegoCharacter b) {
        int dx = Math.abs(a.getX() - b.getX());
        int dy = Math.abs(a.getY() - b.getY());
        return (dx + dy == 1);
    }

    /**
     * Carga un nuevo nivel desde un archivo, reinicia el mapa y la lógica del
     * juego.
     * 
     * @param rutaArchivo Ruta al archivo de nivel (por ejemplo, "Nivel1.txt").
     */
    private void cargarNuevoNivel(String rutaArchivo) {
        nivelActual = Paths.get(rutaArchivo).getFileName().toString();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream(rutaArchivo)))) {

            // 1) Leer y crear mapa
            List<String> lineas = new ArrayList<>();
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
            mapa = new JuegoMap(lineas);

            // 2) Limpiar ocupantes antiguos (jugador lo re-colocamos luego)
            for (int y = 0; y < mapa.getGrid().length; y++) {
                for (int x = 0; x < mapa.getGrid()[0].length; x++) {
                    Cell c = mapa.getCell(x, y);
                    if (c.getOccupant() instanceof Enemigo) {
                        c.setOccupant(null);
                    }
                }
            }
            mapa.getEnemigos().clear();

            // 3) Añadir orcos según el nivel
            if (rutaArchivo.endsWith("Nivel1.txt")) {
                // Un único orco en (5,5) —ajústalo a tu mapa
                Enemigo orco1 = new Enemigo("Orco", 50, 5, 5, 5);
                mapa.addEnemigo(orco1);
                mapa.getCell(5, 5).setOccupant(orco1);
            } else {
                // Dos orcos en Nivel2, en (5,5) y (7,3) por ejemplo
                Enemigo orco1 = new Enemigo("Orco A", 40, 5, 5, 5);
                Enemigo orco2 = new Enemigo("Orco B", 40, 10, 7, 3);
                mapa.addEnemigo(orco1);
                mapa.addEnemigo(orco2);
                mapa.getCell(5, 5).setOccupant(orco1);
                mapa.getCell(7, 3).setOccupant(orco2);
            }

            // 4) Colocar al jugador en el primer suelo libre
            colocarJugadorEnSuelo();
            jugador.setLimites(mapa.getGrid()[0].length, mapa.getGrid().length);

            // 5) Refrescar UI y reiniciar turnos
            actualizarVista();
            initTurnQueue();
            nextTurn();

            // 6) Re-ligar teclado a la escena
            Platform.runLater(() -> {
                Scene scene = gridPane.getScene();
                scene.setOnKeyPressed(evt -> manejarTeclado(evt));
                scene.getRoot().requestFocus();
            });

        } catch (IOException e) {
            System.err.println("Error al cargar el nuevo nivel: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Posiciona al jugador en la primera celda de tipo FLOOR que esté libre.
     * Ajusta internamente las coordenadas del jugador y marca la celda como
     * ocupada.
     */
    private void colocarJugadorEnSuelo() {
        Cell[][] grid = mapa.getGrid();
        for (int fila = 0; fila < grid.length; fila++) {
            for (int col = 0; col < grid[fila].length; col++) {
                Cell celda = grid[fila][col];
                if (celda.getType() == Cell.Type.FLOOR && celda.getOccupant() == null) {
                    // Ajusta coordenadas
                    jugador.setX(col);
                    jugador.setY(fila);
                    // Marca al jugador en esa celda
                    celda.setOccupant(jugador);
                    return;
                }
            }
        }
    }

    /**
     * Verifica si todos los enemigos han sido derrotados.
     * Si es así, muestra la pantalla de victoria y carga el siguiente nivel.
     */
    private void verificarVictoria() {
        boolean todosMuertos = mapa.getEnemigos().stream().allMatch(e -> !e.isAlive());

        if (todosMuertos) {
            System.out.println("¡Has derrotado a todos los enemigos!");

            mostrarPantallaVictoria();

            cargarNuevoNivel("/com/jorge_hugo_javier/Mapa/Nivel2.txt");
        }
    }

    /**
     * Redibuja el mapa del juego en la interfaz gráfica.
     * Coloca imágenes de suelo, paredes, el jugador y enemigos en el GridPane.
     * También actualiza la información del jugador en las etiquetas de UI.
     */
    private void actualizarVista() {
        gridPane.getChildren().clear();
        char[][] celdas = mapa.getMapaChar();

        for (int y = 0; y < celdas.length; y++) {
            for (int x = 0; x < celdas[y].length; x++) {
                StackPane panel = new StackPane();
                panel.setPrefSize(40, 40);
                String imgPath = (celdas[y][x] == '#') ? "/com/jorge_hugo_javier/Vistas/Pared.jpg"
                        : "/com/jorge_hugo_javier/Vistas/Suelo.png";

                ImageView fondo = new ImageView(new Image(getClass().getResourceAsStream(imgPath)));
                fondo.setFitWidth(40);
                fondo.setFitHeight(40);
                panel.getChildren().add(fondo);

                if (jugador.getY() == y && jugador.getX() == x) {
                    ImageView jugadorImg = new ImageView(
                            new Image(getClass().getResourceAsStream("/com/jorge_hugo_javier/Vistas/Jugador.jpg")));
                    jugadorImg.setFitWidth(35);
                    jugadorImg.setFitHeight(35);
                    panel.getChildren().add(jugadorImg);
                }

                for (Enemigo enemigo : mapa.getEnemigos()) {
                    if (!enemigo.isDead() && enemigo.getX() == x && enemigo.getY() == y) {
                        ImageView enemigoImg = new ImageView(
                                new Image(getClass().getResourceAsStream("/com/jorge_hugo_javier/Vistas/Enemigo.jpg")));
                        enemigoImg.setFitWidth(35);
                        enemigoImg.setFitHeight(35);
                        panel.getChildren().add(enemigoImg);
                    }
                }

                gridPane.add(panel, x, y);
            }
        }

        // Actualizar la vida del jugador en pantalla
        if (labelVidaTexto != null)
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
        actualizarBarrasYDatos();
    }

    /**
     * Actualiza las barras de vida y las estadísticas mostradas en pantalla
     * tanto para el jugador como para el primer enemigo vivo.
     * Si no hay enemigos vivos, se ocultan sus datos.
     */
    private void actualizarBarrasYDatos() {
        if (vidaJugadorBarra != null) {
            double progresoJugador = Math.max(0.0,
                    Math.min(1.0, (double) jugador.getHealth() / jugador.getMaxHealth()));
            vidaJugadorBarra.setProgress(progresoJugador);
        }

        if (labelVidaTexto != null)
            labelVidaTexto.setText("Salud: " + jugador.getHealth());

        Enemigo enemigo = mapa.getEnemigos().stream().filter(e -> !e.isDead()).findFirst().orElse(null);

        if (enemigo != null && vidaEnemigoBarra != null) {
            double progresoEnemigo = Math.max(0.0,
                    Math.min(1.0, (double) enemigo.getHealth() / enemigo.getMaxHealth()));
            vidaEnemigoBarra.setProgress(progresoEnemigo);

            if (labelNombreEnemigo != null)
                labelNombreEnemigo.setText("Nombre Enemigo: " + enemigo.getName());
            if (labelVidaEnemigo != null)
                labelVidaEnemigo.setText("Vida Enemigo: " + enemigo.getHealth());
            if (labelFuerzaEnemigo != null)
                labelFuerzaEnemigo.setText("Fuerza Enemigo: " + enemigo.getAttack());
        } else {
            if (vidaEnemigoBarra != null)
                vidaEnemigoBarra.setProgress(0);
            if (labelNombreEnemigo != null)
                labelNombreEnemigo.setText("Nombre Enemigo: --");
            if (labelVidaEnemigo != null)
                labelVidaEnemigo.setText("Vida Enemigo: --");
            if (labelFuerzaEnemigo != null)
                labelFuerzaEnemigo.setText("Fuerza Enemigo: --");
        }

        // Estadísticas del jugador
        if (labelVidaTexto != null)
            labelVidaTexto.setText("Vida: " + jugador.getHealth());
        if (labelNombre != null)
            labelNombre.setText("Nombre: " + jugador.getNombre());
        if (labelFuerza != null)
            labelFuerza.setText("Fuerza: " + jugador.getFuerza());
        if (labelDefensa != null)
            labelDefensa.setText("Defensa: " + jugador.getDefensa());
        if (labelVelocidad != null)
            labelVelocidad.setText("Velocidad: " + jugador.getVelocidad());
    }

    /**
     * Cambia a la pantalla de derrota al perder el jugador.
     * Carga el archivo FXML correspondiente y actualiza la escena.
     */
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
            System.err.println("Error al cargar la pantalla de derrota: " + e.getMessage());
        }
    }

    /**
     * Guarda las estadísticas del jugador en un archivo al finalizar la partida.
     * Incluye nombre, vida restante, fuerza, defensa y velocidad, con fecha y hora.
     * 
     * @param jugador El jugador del que se registran las estadísticas.
     */
    private void guardarEstadisticasJugador(Jugador jugador) {
        try {
            String ruta = "src/main/resources/com/jorge_hugo_javier/Estadisticas/estadisticas.txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(ruta, true));

            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            writer.write("Tiempo: " + ahora.format(formato));
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
            System.out.println("Estadísticas guardadas correctamente.");
        } catch (IOException e) {
            System.err.println("Error al guardar estadísticas: " + e.getMessage());
        }
    }

    /**
     * Guarda en un archivo las estadísticas del jugador al obtener una victoria.
     * Incluye atributos clave y marca el resultado como "VICTORIA".
     */
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
            System.out.println("Estadísticas de victoria guardadas.");
        } catch (IOException e) {
            System.err.println("Error al guardar estadísticas de victoria: " + e.getMessage());
        }
    }

    /**
     * Recibe actualizaciones desde los observables (Jugador o Enemigo).
     * Dependiendo del evento, actualiza la interfaz o maneja la muerte del
     * personaje.
     * 
     * @param subject Objeto que genera la notificación (Jugador o Enemigo).
     * @param event   Tipo de evento recibido: "health", "death", o "position".
     */
    @Override
    public void update(Object subject, String event) {
        // 1) Jugador cambia salud
        if (subject == jugador && "health".equals(event)) {
            Platform.runLater(this::actualizarBarrasYDatos);
        }
        // 2) Jugador muere
        else if (subject == jugador && "death".equals(event)) {
            if (!alertaMostrada) {
                alertaMostrada = true;
                // opcional: te das de baja para no recibir más eventos
                jugador.removeObserver(this);
                Platform.runLater(() -> mostrarDerrota("Has sido derrotado..."));
            }
        }
        // 3) Enemigo cambia salud
        else if (subject instanceof Enemigo && "health".equals(event)) {
            Platform.runLater(this::actualizarBarrasYDatos);
        }
        // 4) Enemigo muere
        else if (subject instanceof Enemigo && "death".equals(event)) {
            // si ya no quedan enemigos, avanzamos/mostramos victoria
            if (mapa.getEnemigos().stream().allMatch(Enemigo::isDead)) {
                Platform.runLater(this::mostrarPantallaVictoria);
            }
        }
    }
}