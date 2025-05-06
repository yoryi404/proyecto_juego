package com.jorge_hugo_javier.Controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;

public class ControladorSecundario {

    /**
     * Este método cambia la escena actual y carga CreacionPersonaje.fxml
     * Vamos desde el boon Ir a creacion de personaje"
     */
    @FXML
    private void switchToCreacionPersonaje(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jorge_hugo_javier/Vistas/CreacionPersonaje.fxml"));
        Parent root = loader.load();

        // Obtener la ventana actual desde el evento
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.setTitle("Crear personaje");
        stage.show();
    }
}