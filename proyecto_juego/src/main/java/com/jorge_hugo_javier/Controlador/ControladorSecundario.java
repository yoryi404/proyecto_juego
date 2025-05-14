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
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;

/**
 * Controlador de la pantalla secundaria que permite cambiar a la creación de
 * personaje.
 */
public class ControladorSecundario {

    /**
     * Cambia a la vista de creación de personaje al recibir un evento de acción.
     * @param event Evento que dispara el cambio de escena.
     * @throws IOException Si ocurre un error al cargar el archivo FXML.
     */
    @FXML
    private void switchToCreacionPersonaje(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/jorge_hugo_javier/Vistas/CreacionPersonaje.fxml"));
        Parent root = loader.load();

        // Obtener la ventana actual desde el evento
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.setTitle("Crear personaje");
        stage.show();
    }
}