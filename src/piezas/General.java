/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package piezas;

import java.util.ArrayList;

/**
 * CLASE FINAL — el General no puede ser extendido.
 * Se mueve 1 casilla ortogonal dentro del palacio 3x3.
 * Regla especial: dos generales no pueden verse en la misma columna sin piezas entre ellos.
 * @author nasry
 */
public final class General extends Pieza {

    // Palacio rojo:  filas 7-9, cols 3-5
    // Palacio negro: filas 0-2, cols 3-5
    private static final int COL_MIN = 3;
    private static final int COL_MAX = 5;
    private final int filaMin;
    private final int filaMax;

    public General(String color) {
        super(color, "General");
        if (color.equals("rojo")) {
            filaMin = 7; filaMax = 9;
        } else {
            filaMin = 0; filaMax = 2;
        }
    }

    @Override
    public final int[][] movimientosValidos(int fila, int col, Pieza[][] tablero) {
        ArrayList<int[]> movs = new ArrayList<>();

        int[][] direcciones = { {-1,0},{1,0},{0,-1},{0,1} };

        for (int[] d : direcciones) {
            int nf = fila + d[0];
            int nc = col  + d[1];

            if (nf < filaMin || nf > filaMax) continue;
            if (nc < COL_MIN || nc > COL_MAX) continue;
            if (!puedeMoverA(nf, nc, tablero))  continue;

            // Verificar regla de generales enfrentados
            if (!generalesEnfrentados(nf, nc, tablero)) {
                movs.add(new int[]{nf, nc});
            }
        }

        return movs.toArray(new int[0][]);
    }

    /**
     * Retorna true si al mover el General a (nf, nc) quedarían
     * ambos generales en la misma columna sin piezas entre ellos.
     * Función recursiva — recorre la columna buscando al general enemigo.
     */
    private boolean generalesEnfrentados(int nf, int nc, Pieza[][] tablero) {
        return buscarGeneralEnemigo(tablero, nf, nc,
                color.equals("rojo") ? -1 : 1, 0);
    }

    /** Función recursiva #2 — busca al general enemigo en la columna. */
    private boolean buscarGeneralEnemigo(Pieza[][] tablero, int fila, int col,
                                          int direccion, int piezasEncontradas) {
        int siguiente = fila + direccion;
        if (siguiente < 0 || siguiente >= 10) return false;

        Pieza p = tablero[siguiente][col];

        if (p != null) {
            if (p instanceof General && !p.getColor().equals(this.color)) {
                return piezasEncontradas == 0; // enfrentados solo si no hay nada entre ellos
            }
            return false; // hay una pieza que no es el general enemigo → no hay amenaza
        }

        return buscarGeneralEnemigo(tablero, siguiente, col, direccion, piezasEncontradas);
    }
}
