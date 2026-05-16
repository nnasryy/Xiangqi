package almacenamiento;

import Users.Usuario;
import java.util.ArrayList;

/**
 * @author nasry
 */
public interface Persistencia {

    boolean registrar(String username, String password);

    Usuario login(String username, String password);

    boolean eliminarUsuario(Usuario u, String password);

    boolean cambiarPassword(Usuario u, String actual, String nuevo);

    void guardarLogPartida(String usernameGanador, String usernamePerdedor, boolean porRetiro);

    ArrayList<String> obtenerLogsUsuario(String username);

    ArrayList<Usuario> getRankingJugadores();

    boolean usernameExiste(String username);

    ArrayList<Usuario> getUsuariosActivos();
}
