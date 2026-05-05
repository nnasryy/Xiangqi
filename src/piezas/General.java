/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package piezas;

import java.util.ArrayList;

/**
 * @author nasry
 */
public final class General extends Pieza {

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

     
            if (!generalesEnfrentados(nf, nc, tablero)) {
                movs.add(new int[]{nf, nc});
            }
        }

        return movs.toArray(new int[0][]);
    }

    private boolean generalesEnfrentados(int nf, int nc, Pieza[][] tablero) {
        return buscarGeneralEnemigo(tablero, nf, nc,
                color.equals("rojo") ? -1 : 1, 0);
    }

    private boolean buscarGeneralEnemigo(Pieza[][] tablero, int fila, int col,
                                          int direccion, int piezasEncontradas) {
        int siguiente = fila + direccion;
        if (siguiente < 0 || siguiente >= 10) return false;

        Pieza p = tablero[siguiente][col];

        if (p != null) {
            if (p instanceof General && !p.getColor().equals(this.color)) {
                return piezasEncontradas == 0; 
            }
            return false; 
        }

        return buscarGeneralEnemigo(tablero, siguiente, col, direccion, piezasEncontradas);
    }
}
