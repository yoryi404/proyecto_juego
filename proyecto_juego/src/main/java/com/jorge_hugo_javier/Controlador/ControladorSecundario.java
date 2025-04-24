package com.jorge_hugo_javier.Controlador;

import java.io.IOException;
import javafx.fxml.FXML;

public class ControladorSecundario {

    @FXML
    private void switchToPrimary() throws IOException {
        CreacionPersonaje.setRoot("primary");
    }
}