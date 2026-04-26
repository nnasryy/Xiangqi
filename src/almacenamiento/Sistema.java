package almacenamiento;

import Users.Usuario;
import java.util.ArrayList;

/**
 * @author nasry
 */
public class Sistema implements Persistencia {

    private ArrayList<Usuario> usuarios = new ArrayList<>();

    // =========================
    // REGISTRAR
    // =========================
    @Override
    public boolean registrar(String username, String password) {
        // Verificar que no esté vacío
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            return false;
        }
        // Verificar que el username no exista ya
        for (Usuario u : usuarios) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return false; // ya existe
            }
        }
        usuarios.add(new Usuario(username, password));
        return true;
    }

    // =========================
    // LOGIN
    // =========================
    @Override
    public Usuario login(String username, String password) {
        for (Usuario u : usuarios) {
            if (u.getUsername().equals(username) &&
                u.getPassword().equals(password) &&
                u.isActivo()) {
                u.registrarLog("Login: " + java.time.LocalDate.now());
                return u;
            }
        }
        return null; // no encontrado o inactivo
    }

    // =========================
    // ELIMINAR USUARIO
    // =========================
    @Override
    public boolean eliminarUsuario(Usuario u, String password) {
        if (u.getPassword().equals(password)) {
            u.desactivar();
            return true;
        }
        return false;
    }

    // =========================
    // CAMBIAR PASSWORD
    // =========================
    @Override
    public boolean cambiarPassword(Usuario u, String actual, String nuevo) {
        if (nuevo == null || nuevo.trim().isEmpty()) return false;
        if (u.getPassword().equals(actual)) {
            u.setPassword(nuevo);
            u.registrarLog("Password cambiado: " + java.time.LocalDate.now());
            return true;
        }
        return false;
    }
}
