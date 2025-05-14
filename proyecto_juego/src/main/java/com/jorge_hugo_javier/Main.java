/**
 * @author Jorge Alegre Maestre
 * @author Hugo Perez Muñoz
 * @author Javier Gil Garán
 */

package com.jorge_hugo_javier;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * La clase {@code Main} es el punto de entrada de la aplicación JavaFX.
 * Esta clase extiende {@link javafx.application.Application} y es responsable
 * de iniciar la interfaz gráfica del usuario (GUI), cargar la vista principal
 * (en este caso, la pantalla de creación de personaje) y mostrarla en la
 * ventana principal.
 */
public class Main extends Application {

    /**
     * Este método se ejecuta al iniciar la aplicación JavaFX. Carga la vista FXML
     * para la pantalla de creación de personaje, establece la escena y aplica
     * los estilos CSS antes de mostrar la ventana principal.
     *
     * @param primaryStage El escenario principal de la aplicación.
     *                     Es la ventana principal en la que se mostrará la interfaz
     *                     gráfica.
     * @throws Exception Si ocurre un error al cargar el archivo FXML o los
     *                   recursos.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Cargar el archivo FXML para la pantalla de creación de personaje
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/jorge_hugo_javier/Vistas/CreacionPersonaje.fxml"));
        Parent root = loader.load();

        // Crear la escena con el nodo raíz cargado desde el FXML
        Scene scene = new Scene(root);

        // Aplicar el archivo de estilo CSS para la apariencia de la interfaz
        scene.getStylesheets().add(getClass().getResource("/com/jorge_hugo_javier/Vistas/estilo.css").toExternalForm());

        // Configurar el título de la ventana principal
        primaryStage.setTitle("Juego de Mazmorras - Crear Personaje");

        // Establecer la escena en el escenario principal
        primaryStage.setScene(scene);

        // Mostrar la ventana principal
        primaryStage.show();
    }

    /**
     * El método {@code main} es el punto de entrada estándar para la aplicación
     * Java.
     * Este método lanza la aplicación JavaFX llamando a {@code launch}.
     *
     * @param args Argumentos de línea de comandos. No se utilizan en este caso.
     */
    public static void main(String[] args) {
        launch(args); // Llama al método start() para iniciar la aplicación JavaFX
    }
}
