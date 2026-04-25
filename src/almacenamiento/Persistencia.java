/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package almacenamiento;

import Users.Usuario;

/**
 *
 * @author nasry
 */
public interface Persistencia {
     boolean registrar(String username, String password);

    Usuario login(String username, String password);

    boolean eliminarUsuario(Usuario u, String password);

    boolean cambiarPassword(Usuario u, String actual, String nuevo);
}
