package piezas;

/**
 * Clase base para todas las piezas del Xiangqi.
 * Herencia simple — cada pieza extiende solo esta clase.
 * @author nasry
 */
public abstract class Pieza {

    protected String color;
    protected String nombre;

    public Pieza(String color, String nombre) {
        this.color  = color;
        this.nombre = nombre;
    }

    // ================================================================
    //  MÉTODO ABSTRACTO — cada subclase DEBE implementar sus movimientos
    // ================================================================
    public abstract int[][] movimientosValidos(int fila, int col, Pieza[][] tablero);

    // ================================================================
    //  MÉTODO FINAL — nadie puede sobreescribirlo
    // ================================================================
    public final boolean esMovimientoValido(int filaOrigen, int colOrigen,
                                             int filaDestino, int colDestino,
                                             Pieza[][] tablero) {
        int[][] movs = movimientosValidos(filaOrigen, colOrigen, tablero);
        for (int[] m : movs) {
            if (m[0] == filaDestino && m[1] == colDestino) return true;
        }
        return false;
    }

    // ================================================================
    //  GETTERS
    // ================================================================
    public String getColor()  { return color;  }
    public String getNombre() { return nombre; }

    // ================================================================
    //  UTILIDADES PROTEGIDAS
    // ================================================================
    protected boolean enTablero(int fila, int col) {
        return fila >= 0 && fila < 10 && col >= 0 && col < 9;
    }

    protected boolean puedeMoverA(int fila, int col, Pieza[][] tablero) {
        if (!enTablero(fila, col)) return false;
        Pieza p = tablero[fila][col];
        return p == null || !p.getColor().equals(this.color);
    }

    @Override
    public String toString() {
        return nombre + "(" + color.charAt(0) + ")";
    }
}