package com.jorge_hugo_javier;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jorge_hugo_javier/Vistas/CreacionPersonaje.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        scene.getStylesheets().add(getClass().getResource("/com/jorge_hugo_javier/Vistas/estilo.css").toExternalForm());

        primaryStage.setTitle("Juego de Mazmorras - Crear Personaje");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
