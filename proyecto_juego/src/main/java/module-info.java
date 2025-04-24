module com.jorge_hugo_javier {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.jorge_hugo_javier to javafx.fxml;
    exports com.jorge_hugo_javier;
}
