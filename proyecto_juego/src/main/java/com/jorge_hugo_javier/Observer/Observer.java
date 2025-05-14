/**
 * @author Jorge Alegre Maestre
 * @author Hugo Perez Muñoz
 * @author Javier Gil Garán
 */
package com.jorge_hugo_javier.Observer;
/**
 * Interfaz que representa un observador en el patrón Observer.
 * Se utiliza para recibir notificaciones de cambio por parte de objetos observables (subjects).
 */
public interface Observer {
    /**
     * Llamado cuando un Subject notifica un cambio.
     * @param subject el objeto que notifica
     * @param event    un String que describe el evento ("health", "death", "position")
     */
    void update(Object subject, String event);
}