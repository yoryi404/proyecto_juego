package com.jorge_hugo_javier.Controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import com.jorge_hugo_javier.Model.Jugador;

import java.io.IOException;

public class CreacionPersonaje {

    @FXML private TextField nombreField;
    @FXML private TextField saludField;
    @FXML private TextField fuerzaField;
    @FXML private TextField defensaField;
    @FXML private TextField velocidadField;

    @FXML
    private void crearPersonaje() {
        try {
            String nombre = nombreField.getText();
            int salud = Integer.parseInt(saludField.getText());
            int fuerza = Integer.parseInt(fuerzaField.getText());
            int defensa = Integer.parseInt(defensaField.getText());
            int velocidad = Integer.parseInt(velocidadField.getText());

            Jugador jugador = new Jugador(nombre, salud, fuerza, defensa, velocidad);
            System.out.println("Jugador creado: " + jugador);

            // Cargar GameView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jorge_hugo_javier/Vistas/GameView.fxml"));
            Parent root = loader.load();

            // Pasar el jugador al controlador de GameView
            ControladorPrincipal controlador = loader.getController();
            controlador.setJugador(jugador);

            // Cambiar de escena
            Stage stage = (Stage) nombreField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Juego de Mazmorras");
            stage.show();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de entrada", "Introduce solo números válidos para salud, fuerza, defensa y velocidad.");
        } catch (IOException e) {
            mostrarAlerta("Error al cargar la vista", "No se pudo cargar GameView.fxml:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}