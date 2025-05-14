/**
 * @author Jorge Alegre Maestre
 * @author Hugo Perez Muñoz
 * @author Javier Gil Garán
 */
package com.jorge_hugo_javier.Observer;
/**
 * Esta interfaz define el comportamiento para un sujeto en el patrón de diseño Observer.
 * Un sujeto es aquel que mantiene una lista de observadores y notifica a los observadores 
 * sobre cambios o eventos importantes en su estado.
 * Las clases que implementan esta interfaz permiten que los observadores se registren, 
 * se eliminen y se notifiquen cuando un evento relevante ocurre.
 */
public interface Subject {
    
    /**
     * Añade un observador a la lista de observadores del sujeto.
     * @param o El observador que se añadirá a la lista de observadores.
     */
    void addObserver(Observer o);

    /**
     * Elimina un observador de la lista de observadores del sujeto.
     * @param o El observador que se eliminará de la lista de observadores.
     */
    void removeObserver(Observer o);

    /**
     * Notifica a todos los observadores registrados sobre un evento específico.
     * @param event El tipo de evento que ha ocurrido y que los observadores deben conocer.
     */
    void notifyObservers(String event);
}
