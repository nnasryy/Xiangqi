/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package piezas;

import java.util.ArrayList;

/**
 * Se mueve como la torre del ajedrez: horizontal o vertical sin límite.
 * No puede saltar piezas.
 * @author nasry
 */
public class CarroDeGuerra extends Pieza {

    public CarroDeGuerra(String color) {
        super(color, "Carro");
    }

    @Override
    public int[][] movimientosValidos(int fila, int col, Pieza[][] tablero) {
        ArrayList<int[]> movs = new ArrayList<>();

        int[][] direcciones = { {-1,0},{1,0},{0,-1},{0,1} };

        for (int[] d : direcciones) {
            int nf = fila + d[0];
            int nc = col  + d[1];

            while (enTablero(nf, nc)) {
                if (tablero[nf][nc] == null) {
                    movs.add(new int[]{nf, nc});
                } else {
                    if (!tablero[nf][nc].getColor().equals(this.color)) {
                        movs.add(new int[]{nf, nc}); // captura enemigo
                    }
                    break; // bloqueado, no puede seguir
                }
                nf += d[0];
                nc += d[1];
            }
        }

        return movs.toArray(new int[0][]);
    }
}