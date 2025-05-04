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

            Jugador jugador = new Jugador(nombre, salud, fuerza, defensa, velocidad);
            System.out.println("Jugador creado: " + jugador);

            // TODO: Cargar Game.fxml y pasar el jugador

        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Datos inválidos");
            alert.setContentText("Introduce valores numéricos correctos.");
            alert.showAndWait();
        }
    }
}