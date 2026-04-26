/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package almacenamiento;

import Users.Usuario;
import java.util.ArrayList;

/**
 * @author nasry
 */
public interface Persistencia {

    // ===== USUARIOS =====
    boolean registrar(String username, String password);
    Usuario login(String username, String password);
    boolean eliminarUsuario(Usuario u, String password);
    boolean cambiarPassword(Usuario u, String actual, String nuevo);

    // ===== LOGS DE PARTIDAS =====
    void guardarLogPartida(String usernameGanador, String usernamePerdedor, boolean porRetiro);
    ArrayList<String> obtenerLogsUsuario(String username);

    // ===== RANKING =====
    ArrayList<Usuario> getRankingJugadores();

    // ===== UTILIDADES =====
    boolean usernameExiste(String username);
    ArrayList<Usuario> getUsuariosActivos();
}