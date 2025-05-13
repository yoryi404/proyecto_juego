package com.jorge_hugo_javier.Controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class ControladorVictoria {

    @FXML
    private ImageView victoriaImage;

    @FXML
    private Button btnReiniciar;

    @FXML
    private Button btnSalir;

    @FXML
    private void reiniciarJuego(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jorge_hugo_javier/Vistas/CreacionPersonaje.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnReiniciar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Crear Personaje");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void salirJuego(ActionEvent event) {
        System.exit(0);
    }
}

