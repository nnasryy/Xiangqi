/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package piezas;

import java.util.ArrayList;

/**
 * Se mueve 1 paso ortogonal + 1 diagonal hacia afuera.
 * Bloqueado si la primera casilla ortogonal está ocupada.
 * @author nasry
 */
public class Caballo extends Pieza {

    public Caballo(String color) {
        super(color, "Caballo");
    }

    @Override
    public int[][] movimientosValidos(int fila, int col, Pieza[][] tablero) {
        ArrayList<int[]> movs = new ArrayList<>();

        // Cada entrada: {paso ortogonal fila, paso ortogonal col, destino fila, destino col}
        int[][] saltos = {
            {-1, 0, -2, -1}, {-1, 0, -2,  1},  // arriba  → diagonal
            { 1, 0,  2, -1}, { 1, 0,  2,  1},  // abajo   → diagonal
            { 0,-1, -1, -2}, { 0,-1,  1, -2},  // izq     → diagonal
            { 0, 1, -1,  2}, { 0, 1,  1,  2}   // derecha → diagonal
        };

        for (int[] s : saltos) {
            int bloqFila = fila + s[0];
            int bloqCol  = col  + s[1];
            int destFila = fila + s[2];
            int destCol  = col  + s[3];

            if (!enTablero(bloqFila, bloqCol)) continue;
            if (tablero[bloqFila][bloqCol] != null) continue; // bloqueado
            if (!puedeMoverA(destFila, destCol, tablero)) continue;

            movs.add(new int[]{destFila, destCol});
        }

        return movs.toArray(new int[0][]);
    }
}