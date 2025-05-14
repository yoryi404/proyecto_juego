/**
 * @author Jorge Alegre Maestre
 * @author Hugo Perez Muñoz
 * @author Javier Gil Garán
 */
package com.jorge_hugo_javier.Controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Controlador de la pantalla de derrota. Muestra estadísticas y permite volver
 * a crear personaje.
 */
public class ControladorDerrota {

    @FXML
    private TextArea textAreaEstadisticas;

    /**
     * Inicializa la pantalla cargando las estadísticas del jugador desde un
     * archivo.
     */
    @FXML
    public void initialize() {
        String ruta = "src/main/resources/com/jorge_hugo_javier/Estadisticas/estadisticas.txt";

        try {
            List<String> lineas = Files.readAllLines(Paths.get(ruta), StandardCharsets.UTF_8);

            if (lineas.isEmpty()) {
                textAreaEstadisticas.setText("No hay estadísticas guardadas aún.");
            } else {
                StringBuilder contenido = new StringBuilder("Estadísticas del jugador:\n\n");
                for (String linea : lineas) {
                    contenido.append("• ").append(linea).append("\n");
                }
                textAreaEstadisticas.setText(contenido.toString());
            }

        } catch (IOException e) {
            textAreaEstadisticas.setText("Error al leer el archivo de estadísticas.");
            System.err.println("rror leyendo estadísticas: " + e.getMessage());
        }
    }

    /**
     * Carga la vista de creación de personaje al cerrar la pantalla de derrota.
     */
    @FXML
    private void volverACrearPersonaje() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/jorge_hugo_javier/Vistas/CreacionPersonaje.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) textAreaEstadisticas.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen(); // Centra la nueva ventana
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al volver a la creación del personaje: " + e.getMessage());
            e.printStackTrace();
        }
    }
}