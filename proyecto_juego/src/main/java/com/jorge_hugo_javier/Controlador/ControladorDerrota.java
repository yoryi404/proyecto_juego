package com.jorge_hugo_javier.Controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ControladorDerrota {

    @FXML
    private void volverACreacion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jorge_hugo_javier/Vistas/CreacionPersonaje.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) (loader.getRoot())).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}