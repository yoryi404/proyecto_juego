package com.jorge_hugo_javier.Controlador;

import java.io.IOException;
import javafx.fxml.FXML;

public class ControladorPrincipal {

    @FXML
    private void switchToSecondary() throws IOException {
        CreacionPersonaje.setRoot("secondary");
    }
}
