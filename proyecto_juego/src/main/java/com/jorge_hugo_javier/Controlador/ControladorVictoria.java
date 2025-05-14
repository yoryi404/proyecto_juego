/**
 * @author Jorge Alegre Maestre
 * @author Hugo Perez Muñoz
 * @author Javier Gil Garán
 */
package com.jorge_hugo_javier.Controlador;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Controlador de la pantalla de victoria. Muestra estadísticas y permite
 * reiniciar.
 */
public class ControladorVictoria {

    @FXML
    private TextArea textAreaEstadisticas;

    @FXML
    private ImageView imagenVictoria;

    /**
     * Inicializa la vista cargando la imagen de victoria y las estadísticas del
     * jugador.
     */
    @FXML
    public void initialize() {
        // Cargar imagen de victoria
        try {
            imagenVictoria.setImage(new javafx.scene.image.Image(
                    getClass().getResourceAsStream("/com/jorge_hugo_javier/Imagenes/victoria.png")));
        } catch (Exception e) {
            System.err.println("No se pudo cargar la imagen de victoria: " + e.getMessage());
        }

        // Mostrar estadísticas
        String ruta = "src/main/resources/com/jorge_hugo_javier/Estadisticas/estadisticas.txt";
        try {
            List<String> lineas = Files.readAllLines(Paths.get(ruta));
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
            System.err.println("Error leyendo estadísticas: " + e.getMessage());
        }
    }

    /**
     * Cambia a la vista de creación de personaje desde la pantalla de victoria.
     */
    @FXML
    private void volverACrearPersonaje() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/jorge_hugo_javier/Vistas/CreacionPersonaje.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) textAreaEstadisticas.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al volver a creación del personaje: " + e.getMessage());
        }
    }
}