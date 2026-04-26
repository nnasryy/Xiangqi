/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package piezas;

import java.util.ArrayList;

/**
 * Se mueve exactamente 2 casillas en diagonal.
 * No puede cruzar el río (filas 0-4 negro, 5-9 rojo).
 * Bloqueado si hay pieza en la casilla intermedia ("ojo del elefante").
 * @author nasry
 */
public class Elefante extends Pieza {

    private final int filaMin;
    private final int filaMax;

    public Elefante(String color) {
        super(color, "Elefante");
        if (color.equals("rojo")) {
            filaMin = 5; filaMax = 9;
        } else {
            filaMin = 0; filaMax = 4;
        }
    }

    @Override
    public int[][] movimientosValidos(int fila, int col, Pieza[][] tablero) {
        ArrayList<int[]> movs = new ArrayList<>();

        int[][] diagonales = { {-2,-2},{-2,2},{2,-2},{2,2} };

        for (int[] d : diagonales) {
            int nf = fila + d[0];
            int nc = col  + d[1];

            if (!enTablero(nf, nc))            continue;
            if (nf < filaMin || nf > filaMax)  continue; // no cruza el río
            if (!puedeMoverA(nf, nc, tablero)) continue;

            // Verificar "ojo del elefante" — casilla intermedia
            int ojoFila = fila + d[0] / 2;
            int ojoCol  = col  + d[1] / 2;
            if (tablero[ojoFila][ojoCol] != null) continue; // bloqueado

            movs.add(new int[]{nf, nc});
        }

        return movs.toArray(new int[0][]);
    }
}
