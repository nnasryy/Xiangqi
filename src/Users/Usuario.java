package Users;

import java.time.LocalDate;

/**
 * @author nasry
 */
public class Usuario {

    private String    username;
    private String    password;
    private int       puntos;
    private LocalDate fechaIngreso;
    private boolean   activo;
    private String[]  logsPartidas;
    private int       logIndex;

    public Usuario(String username, String password) {
        this.username     = username;
        this.password     = password;
        this.puntos       = 0;
        this.fechaIngreso = LocalDate.now();
        this.activo       = true;
        this.logsPartidas = new String[10];
        this.logIndex     = 0;
    }

    public Usuario(String username, String password, int puntos,
                   LocalDate fechaIngreso, boolean activo) {
        this.username     = username;
        this.password     = password;
        this.puntos       = puntos;
        this.fechaIngreso = fechaIngreso;
        this.activo       = activo;
        this.logsPartidas = new String[10];
        this.logIndex     = 0;
    }

    
    public String    getUsername()     { return username;     }
    public String    getPassword()     { return password;     }
    public int       getPuntos()       { return puntos;       }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public boolean   isActivo()        { return activo;       }

   
    public void setPassword(String password) { this.password = password; }
    public void desactivar()                 { this.activo   = false;    }
    public void activar()                    { this.activo   = true;     }

  
    public void agregarPuntos(int pts) { puntos += pts; }

  
    public void registrarLogPartida(String log) {
        logsPartidas[logIndex] = log;
        logIndex = (logIndex + 1) % 10;
    }

    public String[] getLogsPartidas() {
        String[] resultado = new String[10];
        int k = 0;
        for (int i = logIndex; i < 10; i++)
            if (logsPartidas[i] != null) resultado[k++] = logsPartidas[i];
        for (int i = 0; i < logIndex; i++)
            if (logsPartidas[i] != null) resultado[k++] = logsPartidas[i];
        return resultado;
    }
}