package juego;

import Users.Usuario;
import almacenamiento.Sistema;

/**

 * @author nasry
 */
public class Juego {

    protected Sistema  sistema;
    protected Usuario  jugador1;  
    protected Usuario  jugador2;  
    protected String   turnoActual;
    protected boolean  juegoTerminado;

    public Juego(Sistema sistema, Usuario jugador1, Usuario jugador2) {
        this.sistema        = sistema;
        this.jugador1       = jugador1;
        this.jugador2       = jugador2;
        this.turnoActual    = "rojo";
        this.juegoTerminado = false;
    }

   
    protected void cambiarTurno() {
        turnoActual = turnoActual.equals("rojo") ? "negro" : "rojo";
    }

    protected Usuario getJugadorActual() {
        return turnoActual.equals("rojo") ? jugador1 : jugador2;
    }

    protected Usuario getJugadorEspera() {
        return turnoActual.equals("rojo") ? jugador2 : jugador1;
    }

    protected void registrarResultado(String usernameGanador,
                                      String usernamePerdedor,
                                      boolean porRetiro) {
        sistema.guardarLogPartida(usernameGanador, usernamePerdedor, porRetiro);
    }

    protected String construirMensajeFin(String usernameGanador,
                                          String usernamePerdedor,
                                          boolean porRetiro,
                                          String quienSeRetiro) {
        if (porRetiro) {
            return quienSeRetiro + " SE RETIRO\n" +
                   "FELICIDADES " + usernameGanador + ", HAS GANADO 3 PUNTOS";
        } else {
            return usernameGanador + " VENCIO A " + usernamePerdedor + "\n" +
                   "FELICIDADES " + usernameGanador + " HAS GANADO 3 PUNTOS";
        }
    }

    public String  getTurnoActual()    { return turnoActual;    }
    public boolean isJuegoTerminado()  { return juegoTerminado; }
    public Usuario getJugador1()       { return jugador1;       }
    public Usuario getJugador2()       { return jugador2;       }
}