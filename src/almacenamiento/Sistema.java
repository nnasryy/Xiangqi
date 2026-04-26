package almacenamiento;

import Users.Usuario;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * @author nasry
 *
 * Archivo players.xia  →  username|password|puntos|fechaIngreso|activo
 * Archivo logs/username_logs.xia  →  una línea por partida
 */
public class Sistema implements Persistencia {

    private static final String ARCHIVO_JUGADORES = "players.xia";
    private static final String CARPETA_LOGS      = "logs/";

    private ArrayList<Usuario> usuarios = new ArrayList<>();

    public Sistema() {
        new File(CARPETA_LOGS).mkdirs();
        cargarUsuarios();
    }

    // ================================================================
    //  VALIDACIONES INTERNAS
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
        guardarUsuarios();
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
    //  ELIMINAR USUARIO  (borra del ArrayList y su archivo de logs)
    // ================================================================
    @Override
    public boolean eliminarUsuario(Usuario u, String password) {
        if (u == null) return false;
        if (!u.getPassword().equals(password)) return false;

        usuarios.remove(u);
        new File(CARPETA_LOGS + u.getUsername() + "_logs.xia").delete();
        guardarUsuarios();
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
        guardarUsuarios();
        return true;
    }

    // ================================================================
    //  GUARDAR USUARIOS → players.xia
    // ================================================================

    public boolean guardarUsuarios() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_JUGADORES))) {
            for (Usuario u : usuarios) {
                pw.println(
                    u.getUsername()     + "|" +
                    u.getPassword()     + "|" +
                    u.getPuntos()       + "|" +
                    u.getFechaIngreso() + "|" +
                    u.isActivo()
                );
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error guardando jugadores: " + e.getMessage());
            return false;
        }
    }

    // ================================================================
    //  CARGAR USUARIOS ← players.xia
    // ================================================================
    public boolean cargarUsuarios() {
        File archivo = new File(ARCHIVO_JUGADORES);
        if (!archivo.exists()) return true;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            usuarios.clear();
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                String[] partes = linea.split("\\|");
                if (partes.length < 5) continue;

                String    username = partes[0];
                String    password = partes[1];
                int       puntos   = Integer.parseInt(partes[2]);
                LocalDate fecha    = LocalDate.parse(partes[3]);
                boolean   activo   = Boolean.parseBoolean(partes[4]);

                Usuario u = new Usuario(username, password, puntos, fecha, activo);
                cargarLogsUsuario(u);
                usuarios.add(u);
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error cargando jugadores: " + e.getMessage());
            return false;
        }
    }

    // ================================================================
    //  LOGS DE PARTIDAS
    //  Mensajes exactos según instrucciones del proyecto
    // ================================================================
    @Override
    public void guardarLogPartida(String usernameGanador, String usernamePerdedor, boolean porRetiro) {

        String msgGanador;
        String msgPerdedor;

        if (porRetiro) {
            msgGanador  = usernamePerdedor + " SE HA RETIRADO, FELICIDADES " +
                          usernameGanador  + ", HAS GANADO 3 PUNTOS";
            msgPerdedor = usernamePerdedor + " SE HA RETIRADO, FELICIDADES " +
                          usernameGanador  + ", HAS GANADO 3 PUNTOS";
        } else {
            msgGanador  = usernameGanador  + " VENCIO A " + usernamePerdedor +
                          ", FELICIDADES " + usernameGanador + " HAS GANADO 3 PUNTOS";
            msgPerdedor = usernameGanador  + " VENCIO A " + usernamePerdedor +
                          ", FELICIDADES " + usernameGanador + " HAS GANADO 3 PUNTOS";
        }

        Usuario ganador  = buscarUsuario(usernameGanador);
        Usuario perdedor = buscarUsuario(usernamePerdedor);

        if (ganador  != null) {
            ganador.agregarPuntos(3);
            ganador.registrarLogPartida(msgGanador);
            guardarLogsUsuario(ganador);
        }
        if (perdedor != null) {
            perdedor.registrarLogPartida(msgPerdedor);
            guardarLogsUsuario(perdedor);
        }

        guardarUsuarios(); // actualizar puntos en players.xia
    }

    // ================================================================
    //  OBTENER LOGS  (del más reciente al más viejo)
    // ================================================================
    @Override
    public ArrayList<String> obtenerLogsUsuario(String username) {
        Usuario u = buscarUsuario(username);
        if (u == null) return new ArrayList<>();

        ArrayList<String> resultado = new ArrayList<>();
        String[] logs = u.getLogsPartidas();
        // invertir para mostrar del más reciente al más viejo
        for (int i = logs.length - 1; i >= 0; i--) {
            if (logs[i] != null) resultado.add(logs[i]);
        }
        return resultado;
    }

    // ================================================================
    //  EXPORTAR LOGS A .TXT
    // ================================================================
  
    public boolean exportarLogsUsuario(String username, String rutaArchivo) {
        ArrayList<String> logs = obtenerLogsUsuario(username);
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaArchivo))) {
            pw.println("=== Logs de partidas de " + username + " ===");
            pw.println("Exportado: " + LocalDate.now());
            pw.println("-------------------------------------------");
            if (logs.isEmpty()) {
                pw.println("Sin partidas registradas.");
            } else {
                for (String log : logs) pw.println(log);
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error exportando logs: " + e.getMessage());
            return false;
        }
    }

    // ================================================================
    //  GUARDAR / CARGAR LOGS  →  logs/username_logs.xia
    // ================================================================
    private void guardarLogsUsuario(Usuario u) {
        try (PrintWriter pw = new PrintWriter(
                new FileWriter(CARPETA_LOGS + u.getUsername() + "_logs.xia"))) {
            for (String log : u.getLogsPartidas()) {
                if (log != null) pw.println(log);
            }
        } catch (IOException e) {
            System.err.println("Error guardando logs de " + u.getUsername() + ": " + e.getMessage());
        }
    }

    private void cargarLogsUsuario(Usuario u) {
        File archivo = new File(CARPETA_LOGS + u.getUsername() + "_logs.xia");
        if (!archivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) u.registrarLogPartida(linea.trim());
            }
        } catch (IOException e) {
            System.err.println("Error cargando logs de " + u.getUsername() + ": " + e.getMessage());
        }
    }

    // ================================================================
    //  RANKING  (bubble sort recursivo — función recursiva #1)
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
        ordenarPorPuntos(lista, n - 1); // recursivo
    }

    // ================================================================
    //  UTILIDADES
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