/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package piezas;

import java.util.ArrayList;

/**
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
            if (nf < filaMin || nf > filaMax)  continue; 
            if (!puedeMoverA(nf, nc, tablero)) continue;

        
            int ojoFila = fila + d[0] / 2;
            int ojoCol  = col  + d[1] / 2;
            if (tablero[ojoFila][ojoCol] != null) continue; 

            movs.add(new int[]{nf, nc});
        }

        return movs.toArray(new int[0][]);
    }
}
