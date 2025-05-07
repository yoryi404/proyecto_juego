package com.jorge_hugo_javier.Controlador;
import com.jorge_hugo_javier.Model.Jugador;
import com.jorge_hugo_javier.Model.Enemigo;
import com.jorge_hugo_javier.Model.JuegoMap;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
public class ControladorDeJuego {
 @FXML
 private GridPane gridPane;
 private Jugador jugador;
 private JuegoMap mapa;
 public void setJugador(Jugador jugador) {
 this.jugador = jugador;
 }
 public void setMapa(JuegoMap mapa) {
 this.mapa = mapa;
 actualizarVista();
 }
 public void manejarTeclado(KeyEvent evento) {
 switch (evento.getCode()) {
 case W -> jugador.moverArriba();
 case S -> jugador.moverAbajo();
 case A -> jugador.moverIzquierda();
 case D -> jugador.moverDerecha();
 }
 actualizarVista();
 moverEnemigos();
 }
 private void actualizarVista() {
 System.out.println("Jugador en: " + jugador.getPosX() + "," + jugador.getPosY());
 }
 private void moverEnemigos() {
 for (Enemigo e : mapa.getEnemigos()) {xx
 e.moverHacia(jugador.getPosX(), jugador.getPosY());
 }
 }
}