package com.jorge_hugo_javier.Controlador;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import com.jorge_hugo_javier.Model.Jugador;

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

            // Crear objeto Jugador
            Jugador jugador = new Jugador(nombre, salud, fuerza, defensa, velocidad);

            // Aquí se puede pasar el jugador a la siguiente vista o guardarlo en el modelo
            System.out.println("Personaje creado: " + jugador);

            // TODO: Cargar Game.fxml y pasar el objeto jugador

        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Datos inválidos");
            alert.setContentText("Por favor introduce solo números en salud, fuerza, defensa y velocidad.");
            alert.showAndWait();
        }
    }
}