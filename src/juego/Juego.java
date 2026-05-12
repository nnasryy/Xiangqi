/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package juego;

import Users.Usuario;
import almacenamiento.Sistema;

/**
 * Clase padre — contiene la lógica común de cualquier juego de tablero.
 * JuegoXiangqi la extiende con la GUI específica.
 * @author nasry
 */
public class Juego {

    protected Sistema  sistema;
    protected Usuario  jugador1;  // rojo
    protected Usuario  jugador2;  // negro
    protected String   turnoActual;
    protected boolean  juegoTerminado;

    public Juego(Sistema sistema, Usuario jugador1, Usuario jugador2) {
        this.sistema        = sistema;
        this.jugador1       = jugador1;
        this.jugador2       = jugador2;
        this.turnoActual    = "rojo";
        this.juegoTerminado = false;
    }

    // ================================================================
    //  LÓGICA COMÚN
    // ================================================================

    /** Cambia el turno al siguiente jugador. */
    protected void cambiarTurno() {
        turnoActual = turnoActual.equals("rojo") ? "negro" : "rojo";
    }

    /** Retorna el usuario cuyo turno es actualmente. */
    protected Usuario getJugadorActual() {
        return turnoActual.equals("rojo") ? jugador1 : jugador2;
    }

    /** Retorna el usuario que NO tiene el turno. */
    protected Usuario getJugadorEspera() {
        return turnoActual.equals("rojo") ? jugador2 : jugador1;
    }

    /** Registra el resultado y suma puntos al ganador. */
    protected void registrarResultado(String usernameGanador,
                                      String usernamePerdedor,
                                      boolean porRetiro) {
        sistema.guardarLogPartida(usernameGanador, usernamePerdedor, porRetiro);
    }

    /** Construye el mensaje de fin según las instrucciones del proyecto. */
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

    // ================================================================
    //  GETTERS
    // ================================================================
    public String  getTurnoActual()    { return turnoActual;    }
    public boolean isJuegoTerminado()  { return juegoTerminado; }
    public Usuario getJugador1()       { return jugador1;       }
    public Usuario getJugador2()       { return jugador2;       }
}