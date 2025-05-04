module com.jorge_hugo_javier {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.jorge_hugo_javier.Controlador to javafx.fxml;
    opens com.jorge_hugo_javier.Model to javafx.fxml;

    /*
     * En caso de añadir imagenes y css, necesitamos abrir el paquete de recursos
     * para que JavaFX pueda acceder a ellos. Esto es necesario para que JavaFX
     * pueda acceder a los recursos del paquete de recursos.
     * En este caso, el paquete de recursos es com.jorge_hugo_javier.Vistas y
     * com.jorge_hugo_javier.Mapa.
     * Esto es necesario para que JavaFX pueda acceder a los recursos del paquete de recursos.
     */
    opens com.jorge_hugo_javier.Vistas;
    opens com.jorge_hugo_javier.Mapa;


    exports com.jorge_hugo_javier.Controlador;
    exports com.jorge_hugo_javier.Model;
}