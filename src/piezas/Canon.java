/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package piezas;

import java.util.ArrayList;

/**
 * Sin captura: se mueve como el Carro.
 * Para capturar: necesita exactamente 1 pieza intermedia ("pantalla").
 * @author nasry
 */
public class Canon extends Pieza {

    public Canon(String color) {
        super(color, "Canon");
    }

    @Override
    public int[][] movimientosValidos(int fila, int col, Pieza[][] tablero) {
        ArrayList<int[]> movs = new ArrayList<>();

        int[][] direcciones = { {-1,0},{1,0},{0,-1},{0,1} };

        for (int[] d : direcciones) {
            int nf = fila + d[0];
            int nc = col  + d[1];
            boolean pantalla = false;

            while (enTablero(nf, nc)) {
                Pieza p = tablero[nf][nc];

                if (!pantalla) {
                    if (p == null) {
                        movs.add(new int[]{nf, nc}); // movimiento libre
                    } else {
                        pantalla = true; // encontró la pantalla, ahora busca captura
                    }
                } else {
                    if (p != null) {
                        // Solo captura si es enemigo y es la primera pieza tras la pantalla
                        if (!p.getColor().equals(this.color)) {
                            movs.add(new int[]{nf, nc});
                        }
                        break; // con o sin captura, se detiene
                    }
                }

                nf += d[0];
                nc += d[1];
            }
        }

        return movs.toArray(new int[0][]);
    }
}
