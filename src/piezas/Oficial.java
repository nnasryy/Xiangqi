/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package piezas;

import java.util.ArrayList;

/**
 * @author nasry
 */
public class Oficial extends Pieza {

    private static final int COL_MIN = 3;
    private static final int COL_MAX = 5;
    private final int filaMin;
    private final int filaMax;

    public Oficial(String color) {
        super(color, "Oficial");
        if (color.equals("rojo")) {
            filaMin = 7; filaMax = 9;
        } else {
            filaMin = 0; filaMax = 2;
        }
    }

    @Override
    public int[][] movimientosValidos(int fila, int col, Pieza[][] tablero) {
        ArrayList<int[]> movs = new ArrayList<>();

        int[][] diagonales = { {-1,-1},{-1,1},{1,-1},{1,1} };

        for (int[] d : diagonales) {
            int nf = fila + d[0];
            int nc = col  + d[1];

            if (nf < filaMin || nf > filaMax) continue;
            if (nc < COL_MIN || nc > COL_MAX) continue;
            if (!puedeMoverA(nf, nc, tablero))  continue;

            movs.add(new int[]{nf, nc});
        }

        return movs.toArray(new int[0][]);
    }
}
