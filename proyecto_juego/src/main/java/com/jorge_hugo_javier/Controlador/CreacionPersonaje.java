/**
 * @author Jorge Alegre Maestre
 * @author Hugo Perez Muñoz
 * @author Javier Gil Garán
 */
package com.jorge_hugo_javier.Controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import com.jorge_hugo_javier.Model.Jugador;

import java.io.IOException;
/**
 * Controlador para la vista de creación de personaje.
 * Permite al usuario introducir los atributos de un nuevo jugador
 * y cargar la escena principal del juego si los datos son válidos.
 */
public class CreacionPersonaje {

    @FXML private TextField nombreField;
    @FXML private TextField saludField;
    @FXML private TextField fuerzaField;
    @FXML private TextField defensaField;
    @FXML private TextField velocidadField;

    /**
     * Crea un nuevo personaje a partir de los valores introducidos en los campos de texto.
     * Valida que los valores numéricos estén dentro del rango permitido (0-100),
     * y carga la vista principal del juego si los datos son correctos.
     */
    @FXML
    private void crearPersonaje() {
        try {
            String nombre = nombreField.getText();
            int salud = parseCampo(saludField.getText(), "Salud");
            int fuerza = parseCampo(fuerzaField.getText(), "Fuerza");
            int defensa = parseCampo(defensaField.getText(), "Defensa");
            int velocidad = parseCampo(velocidadField.getText(), "Velocidad");

            // Si alguno de los valores fue inválido, se habrá lanzado una excepción
            // y esta parte no se ejecutará

            Jugador jugador = new Jugador(nombre, salud, fuerza, defensa, velocidad);
            System.out.println("Jugador creado: " + jugador);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jorge_hugo_javier/Vistas/GameView.fxml"));
            Parent root = loader.load();

            ControladorPrincipal controlador = loader.getController();
            controlador.setJugador(jugador);

            Stage stage = (Stage) nombreField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Juego de Mazmorras");
            stage.show();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de entrada", "Introduce solo números válidos (0 a 100) en salud, fuerza, defensa y velocidad.");
        } catch (IOException e) {
            mostrarAlerta("Error al cargar la vista", "No se pudo cargar GameView.fxml:\n" + e.getMessage());
            e.printStackTrace();
        }
    }
/**
     * Parsea y valida un valor numérico introducido en un campo de texto.
     *
     * @param texto el texto a convertir en número
     * @param campo el nombre del campo (para mostrar en errores)
     * @return el valor numérico si es válido
     * @throws NumberFormatException si el valor no es un número válido o está fuera de rango
     */
    private int parseCampo(String texto, String campo) throws NumberFormatException {
        int valor = Integer.parseInt(texto);
        if (valor < 0) {
            throw new NumberFormatException(campo + " no puede ser negativo.");
        }
        if (valor > 100) {
            throw new NumberFormatException("Error: el valor de " + campo + " excede más de 100.");
        }
        return valor;
    }
/**
     * Muestra una alerta de error al usuario.
     *
     * @param titulo  el título de la ventana de alerta
     * @param mensaje el contenido del mensaje de error
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
