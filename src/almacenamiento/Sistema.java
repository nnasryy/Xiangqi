package almacenamiento;

import Users.Usuario;
import java.util.ArrayList;

/**
 * @author nasry
 */
public class Sistema implements Persistencia {

    private ArrayList<Usuario> usuarios = new ArrayList<>();

    // ================================================================
    //  VALIDACIONES DE MI PASSWORD O DE MI USER
    // ================================================================
    private boolean passwordValido(String password) {
        return password != null && password.length() == 5;
    }

    private boolean usernameValido(String username) {
        return username != null && !username.trim().isEmpty();
    }

    // ================================================================
    //  REGISTRAR
    // ================================================================
    @Override
    public boolean registrar(String username, String password) {
        if (!usernameValido(username)) return false;
        if (!passwordValido(password)) return false;
        if (usernameExiste(username))  return false;

        usuarios.add(new Usuario(username, password));
        return true;
    }

    // ================================================================
    //  LOGIN
    // ================================================================
    @Override
    public Usuario login(String username, String password) {
        for (Usuario u : usuarios) {
            if (u.getUsername().equals(username) &&
                u.getPassword().equals(password) &&
                u.isActivo()) {
                return u;
            }
        }
        return null;
    }

    // ================================================================
    //  ELIMINAR USUARIO
    // ================================================================
    @Override
    public boolean eliminarUsuario(Usuario u, String password) {
        if (u == null) return false;
        if (!u.getPassword().equals(password)) return false;

        usuarios.remove(u);
        return true;
    }

    // ================================================================
    //  CAMBIAR PASSWORD
    // ================================================================
    @Override
    public boolean cambiarPassword(Usuario u, String actual, String nuevo) {
        if (u == null)                       return false;
        if (!passwordValido(nuevo))          return false;
        if (!u.getPassword().equals(actual)) return false;

        u.setPassword(nuevo);
        return true;
    }

    // ================================================================
    //  LOGS DE PARTIDAS
    // ================================================================
    @Override
    public void guardarLogPartida(String usernameGanador, String usernamePerdedor, boolean porRetiro) {
        String mensaje;
        if (porRetiro) {
            mensaje = usernamePerdedor + " SE HA RETIRADO, FELICIDADES " +
                      usernameGanador  + ", HAS GANADO 3 PUNTOS";
        } else {
            mensaje = usernameGanador  + " VENCIO A " + usernamePerdedor +
                      ", FELICIDADES " + usernameGanador + " HAS GANADO 3 PUNTOS";
        }

        Usuario ganador  = buscarUsuario(usernameGanador);
        Usuario perdedor = buscarUsuario(usernamePerdedor);

        if (ganador  != null) { ganador.agregarPuntos(3); ganador.registrarLogPartida(mensaje);  }
        if (perdedor != null) { perdedor.registrarLogPartida(mensaje); }
    }

    // ================================================================
    //  OBTENER LOGS 
    // ================================================================
    @Override
    public ArrayList<String> obtenerLogsUsuario(String username) {
        Usuario u = buscarUsuario(username);
        if (u == null) return new ArrayList<>();

        ArrayList<String> resultado = new ArrayList<>();
        String[] logs = u.getLogsPartidas();
        for (int i = logs.length - 1; i >= 0; i--) {
            if (logs[i] != null) resultado.add(logs[i]);
        }
        return resultado;
    }

    // ================================================================
    //  RANKING 
    // ================================================================
    @Override
    public ArrayList<Usuario> getRankingJugadores() {
        ArrayList<Usuario> activos = getUsuariosActivos();
        ordenarPorPuntos(activos, activos.size());
        return activos;
    }

    private void ordenarPorPuntos(ArrayList<Usuario> lista, int n) {
        if (n <= 1) return;
        for (int i = 0; i < n - 1; i++) {
            if (lista.get(i).getPuntos() < lista.get(i + 1).getPuntos()) {
                Usuario tmp = lista.get(i);
                lista.set(i, lista.get(i + 1));
                lista.set(i + 1, tmp);
            }
        }
        ordenarPorPuntos(lista, n - 1);
    }

    // ================================================================
    //  PARA MIS WARNINGS
    // ================================================================
    @Override
    public boolean usernameExiste(String username) {
        for (Usuario u : usuarios) {
            if (u.getUsername().equalsIgnoreCase(username)) return true;
        }
        return false;
    }

    @Override
    public ArrayList<Usuario> getUsuariosActivos() {
        ArrayList<Usuario> activos = new ArrayList<>();
        for (Usuario u : usuarios) {
            if (u.isActivo()) activos.add(u);
        }
        return activos;
    }

    private Usuario buscarUsuario(String username) {
        for (Usuario u : usuarios) {
            if (u.getUsername().equals(username)) return u;
        }
        return null;
    }
}