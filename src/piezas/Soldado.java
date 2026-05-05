/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package piezas;

import java.util.ArrayList;

/*
 * @author nasry
 */
public class Soldado extends Pieza {

    public Soldado(String color) {
        super(color, "Soldado");
    }

    @Override
    public int[][] movimientosValidos(int fila, int col, Pieza[][] tablero) {
        ArrayList<int[]> movs = new ArrayList<>();

        boolean esRojo    = color.equals("rojo");
        int     avance    = esRojo ? -1 : 1;     
        boolean cruzoRio  = esRojo ? fila <= 4 : fila >= 5;

      
        int nf = fila + avance;
        if (enTablero(nf, col) && puedeMoverA(nf, col, tablero)) {
            movs.add(new int[]{nf, col});
        }


        if (cruzoRio) {
            int[] laterales = {-1, 1};
            for (int dc : laterales) {
                int nc = col + dc;
                if (enTablero(fila, nc) && puedeMoverA(fila, nc, tablero)) {
                    movs.add(new int[]{fila, nc});
                }
            }
        }

        return movs.toArray(new int[0][]);
    }
}