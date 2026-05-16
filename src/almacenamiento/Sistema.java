package almacenamiento;

import Users.Usuario;
import java.util.ArrayList;

/**
 * @author nasry
 */
public class Sistema implements Persistencia {

    private ArrayList<Usuario> usuarios = new ArrayList<>();

    
    private boolean passwordValido(String password) {
        if (password == null || password.length() != 5) return false;
        boolean tieneLetra  = false;
        boolean tieneNumero = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c))  tieneLetra  = true;
            if (Character.isDigit(c))   tieneNumero = true;
        }
        return tieneLetra && tieneNumero;
    }

    private boolean usernameValido(String username) {
        return username != null && !username.trim().isEmpty();
    }

   
    @Override
    public boolean registrar(String username, String password) {
        if (!usernameValido(username)) return false;
        if (!passwordValido(password)) return false;
        if (usernameExiste(username))  return false;

        usuarios.add(new Usuario(username, password));
        return true;
    }

   
    @Override
    public Usuario login(String username, String password) {
        for (Usuario u : usuarios) {
            if (u.getUsername().equals(username) &&
                u.getPassword().equals(password)) {
                return u; 
            }
        }
        return null;
    }

   
    public Usuario buscarPorUsername(String username) {
        for (Usuario u : usuarios) {
            if (u.getUsername().equals(username)) return u;
        }
        return null;
    }

    
    public boolean activarUsuario(Usuario u, String password) {
        if (u == null) return false;
        if (!u.getPassword().equals(password)) return false;
        u.activar();
        return true;
    }

    
    public boolean desactivarUsuario(Usuario u, String password) {
        if (u == null) return false;
        if (!u.getPassword().equals(password)) return false;
        u.desactivar();
        return true;
    }

    
    @Override
    public boolean eliminarUsuario(Usuario u, String password) {
        if (u == null) return false;
        if (!u.getPassword().equals(password)) return false;
        usuarios.remove(u);
        return true;
    }

    
    @Override
    public boolean cambiarPassword(Usuario u, String actual, String nuevo) {
        if (u == null)                       return false;
        if (!passwordValido(nuevo))          return false;
        if (!u.getPassword().equals(actual)) return false;
        u.setPassword(nuevo);
        return true;
    }

    
    @Override
    public void guardarLogPartida(String usernameGanador, String usernamePerdedor, boolean porRetiro) {
        String mensaje;
        if (porRetiro) {
            mensaje = usernamePerdedor + " SE RETIRO, " +
                      usernameGanador  + " GANO 3 PUNTOS";
        } else {
            mensaje = usernameGanador  + " VENCIO A " + usernamePerdedor +
                      ", " + usernameGanador + " GANO 3 PUNTOS";
        }

        Usuario ganador  = buscarPorUsername(usernameGanador);
        Usuario perdedor = buscarPorUsername(usernamePerdedor);

        if (ganador  != null) { ganador.agregarPuntos(3);  ganador.registrarLogPartida(mensaje);  }
        if (perdedor != null) {                             perdedor.registrarLogPartida(mensaje); }
    }

    @Override
    public ArrayList<String> obtenerLogsUsuario(String username) {
        Usuario u = buscarPorUsername(username);
        if (u == null) return new ArrayList<>();

        ArrayList<String> resultado = new ArrayList<>();
        String[] logs = u.getLogsPartidas();
        for (int i = logs.length - 1; i >= 0; i--) {
            if (logs[i] != null) resultado.add(logs[i]);
        }
        return resultado;
    }

    
    public String validarPasswordMensaje(String password) {
        if (password == null || password.isEmpty())
            return "El password no puede estar vacío.";
        if (password.length() != 5)
            return "El password debe tener exactamente 5 caracteres.";
        if (!tieneLetra(password, 0))
            return "El password debe contener al menos una letra.";
        if (!tieneNumero(password, 0))
            return "El password debe contener al menos un número.";
        return null; 
    }

    
    private boolean tieneLetra(String password, int index) {
        if (index >= password.length()) return false;
        if (Character.isLetter(password.charAt(index))) return true;
        return tieneLetra(password, index + 1);
    }


    private boolean tieneNumero(String password, int index) {
        if (index >= password.length()) return false;
        if (Character.isDigit(password.charAt(index))) return true;
        return tieneNumero(password, index + 1);
    }

 
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
}