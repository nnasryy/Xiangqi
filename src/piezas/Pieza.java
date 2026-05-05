/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package piezas;

/**
 * @author nasry
 */
public abstract class Pieza {

    protected String color;  
    protected String nombre;

    public Pieza(String color, String nombre) {
        this.color  = color;
        this.nombre = nombre;
    }

    public abstract int[][] movimientosValidos(int fila, int col, Pieza[][] tablero);

    public final boolean esMovimientoValido(int filaOrigen, int colOrigen,
                                             int filaDestino, int colDestino,
                                             Pieza[][] tablero) {
        int[][] movs = movimientosValidos(filaOrigen, colOrigen, tablero);
        for (int[] m : movs) {
            if (m[0] == filaDestino && m[1] == colDestino) return true;
        }
        return false;
    }

    public String getColor()  { return color;  }
    public String getNombre() { return nombre; }

    

    protected boolean enTablero(int fila, int col) {
        return fila >= 0 && fila < 10 && col >= 0 && col < 9;
    }

    protected boolean puedeMoverA(int fila, int col, Pieza[][] tablero) {
        if (!enTablero(fila, col)) return false;
        Pieza p = tablero[fila][col];
        return p == null || !p.getColor().equals(this.color);
    }


    protected boolean hayPiezaEntreFilas(int fila, int colMin, int colMax, Pieza[][] tablero) {
        for (int c = colMin + 1; c < colMax; c++) {
            if (tablero[fila][c] != null) return true;
        }
        return false;
    }


    protected boolean hayPiezaEntreCols(int colum, int filaMin, int filaMax, Pieza[][] tablero) {
        for (int f = filaMin + 1; f < filaMax; f++) {
            if (tablero[f][colum] != null) return true;
        }
        return false;
    }


    protected int contarPiezasEntreFilas(int fila, int colMin, int colMax, Pieza[][] tablero) {
        int count = 0;
        for (int c = colMin + 1; c < colMax; c++) {
            if (tablero[fila][c] != null) count++;
        }
        return count;
    }


    protected int contarPiezasEntreCols(int col, int filaMin, int filaMax, Pieza[][] tablero) {
        int count = 0;
        for (int f = filaMin + 1; f < filaMax; f++) {
            if (tablero[f][col] != null) count++;
        }
        return count;
    }

    @Override
    public String toString() {
        return nombre + "(" + color.charAt(0) + ")";
    }
}