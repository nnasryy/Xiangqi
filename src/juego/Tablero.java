/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package juego;

import piezas.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Panel visual del tablero de Xiangqi.
 * Dibuja la grilla, el río, los palacios y las piezas.
 * Maneja clics para seleccionar y mover piezas.
 * @author nasry
 */
public class Tablero extends JPanel {

    // ================================================================
    //  CONSTANTES DE DISEÑO
    // ================================================================
    private static final int FILAS      = 10;
    private static final int COLS       = 9;
    private static final int CELDA      = 65;   // px entre intersecciones
    private static final int MARGEN     = 45;   // margen exterior
    private static final int RADIO_PIEZA = 26;  // radio del círculo de pieza

    // ================================================================
    //  ESTADO DEL JUEGO
    // ================================================================
    private Pieza[][] tablero = new Pieza[FILAS][COLS];
    private int[]  seleccionada = null;  // {fila, col} de la pieza seleccionada
    private int[][] movimientosValidos = new int[0][];

    private String turno = "rojo";       // quién mueve ahora
    private boolean juegoActivo = true;

    // Listeners externos (para notificar captura de rey, cambio de turno)
    private Runnable onTurnoChange;
    private java.util.function.Consumer<String> onGanador;

    // ================================================================
    //  CONSTRUCTOR
    // ================================================================
    public Tablero() {
        int ancho = MARGEN * 2 + CELDA * (COLS - 1);
        int alto  = MARGEN * 2 + CELDA * (FILAS - 1);
        setPreferredSize(new Dimension(ancho, alto));
        setBackground(new Color(240, 200, 120)); // color madera

        inicializarPiezas();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                manejarClic(e.getX(), e.getY());
            }
        });
    }

    // ================================================================
    //  INICIALIZAR PIEZAS EN POSICIÓN INICIAL
    //  Tablero americano: rojo abajo (filas 5-9), negro arriba (filas 0-4)
    // ================================================================
    private void inicializarPiezas() {
        // ── NEGRO (arriba, filas 0-4) ──
        tablero[0][0] = new CarroDeGuerra("negro");
        tablero[0][1] = new Caballo("negro");
        tablero[0][2] = new Elefante("negro");
        tablero[0][3] = new Oficial("negro");
        tablero[0][4] = new General("negro");
        tablero[0][5] = new Oficial("negro");
        tablero[0][6] = new Elefante("negro");
        tablero[0][7] = new Caballo("negro");
        tablero[0][8] = new CarroDeGuerra("negro");

        tablero[2][1] = new Canon("negro");
        tablero[2][7] = new Canon("negro");

        tablero[3][0] = new Soldado("negro");
        tablero[3][2] = new Soldado("negro");
        tablero[3][4] = new Soldado("negro");
        tablero[3][6] = new Soldado("negro");
        tablero[3][8] = new Soldado("negro");

        // ── ROJO (abajo, filas 5-9) ──
        tablero[9][0] = new CarroDeGuerra("rojo");
        tablero[9][1] = new Caballo("rojo");
        tablero[9][2] = new Elefante("rojo");
        tablero[9][3] = new Oficial("rojo");
        tablero[9][4] = new General("rojo");
        tablero[9][5] = new Oficial("rojo");
        tablero[9][6] = new Elefante("rojo");
        tablero[9][7] = new Caballo("rojo");
        tablero[9][8] = new CarroDeGuerra("rojo");

        tablero[7][1] = new Canon("rojo");
        tablero[7][7] = new Canon("rojo");

        tablero[6][0] = new Soldado("rojo");
        tablero[6][2] = new Soldado("rojo");
        tablero[6][4] = new Soldado("rojo");
        tablero[6][6] = new Soldado("rojo");
        tablero[6][8] = new Soldado("rojo");
    }

    // ================================================================
    //  MANEJO DE CLICS
    // ================================================================
    private void manejarClic(int px, int py) {
        if (!juegoActivo) return;

        int col  = Math.round((float)(px - MARGEN) / CELDA);
        int fila = Math.round((float)(py - MARGEN) / CELDA);

        if (fila < 0 || fila >= FILAS || col < 0 || col >= COLS) return;

        // Si hay pieza seleccionada, intentar moverla
        if (seleccionada != null) {
            if (esMovimientoValido(fila, col)) {
                moverPieza(seleccionada[0], seleccionada[1], fila, col);
                deseleccionar();
                return;
            }
            // Si hizo clic en otra pieza propia, cambiar selección
            if (tablero[fila][col] != null &&
                tablero[fila][col].getColor().equals(turno)) {
                seleccionar(fila, col);
                return;
            }
            deseleccionar();
            return;
        }

        // Seleccionar pieza del turno actual
        if (tablero[fila][col] != null &&
            tablero[fila][col].getColor().equals(turno)) {
            seleccionar(fila, col);
        }
    }

    private void seleccionar(int fila, int col) {
        seleccionada = new int[]{fila, col};
        movimientosValidos = tablero[fila][col].movimientosValidos(fila, col, tablero);
        repaint();
    }

    private void deseleccionar() {
        seleccionada = null;
        movimientosValidos = new int[0][];
        repaint();
    }

    private boolean esMovimientoValido(int fila, int col) {
        for (int[] m : movimientosValidos) {
            if (m[0] == fila && m[1] == col) return true;
        }
        return false;
    }

    private void moverPieza(int fi, int ci, int fd, int cd) {
        Pieza capturada = tablero[fd][cd];

        tablero[fd][cd] = tablero[fi][ci];
        tablero[fi][ci] = null;

        // Verificar si se capturó el General
        if (capturada instanceof General) {
            juegoActivo = false;
            if (onGanador != null) onGanador.accept(turno);
        } else {
            // Cambiar turno
            turno = turno.equals("rojo") ? "negro" : "rojo";
            if (onTurnoChange != null) onTurnoChange.run();
        }

        repaint();
    }

    // ================================================================
    //  PINTADO DEL TABLERO
    // ================================================================
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        dibujarGrilla(g2);
        dibujarPalacios(g2);
        dibujarRio(g2);
        dibujarMovimientosValidos(g2);
        dibujarPiezas(g2);
    }

    private void dibujarGrilla(Graphics2D g2) {
        g2.setColor(new Color(80, 40, 10));
        g2.setStroke(new BasicStroke(1.2f));

        // Líneas horizontales
        for (int f = 0; f < FILAS; f++) {
            int y = MARGEN + f * CELDA;
            g2.drawLine(MARGEN, y, MARGEN + (COLS - 1) * CELDA, y);
        }

        // Líneas verticales (con hueco en el río — filas 4 a 5)
        for (int c = 0; c < COLS; c++) {
            int x = MARGEN + c * CELDA;
            // Arriba del río
            g2.drawLine(x, MARGEN, x, MARGEN + 4 * CELDA);
            // Abajo del río
            g2.drawLine(x, MARGEN + 5 * CELDA, x, MARGEN + 9 * CELDA);
        }
    }

    private void dibujarPalacios(Graphics2D g2) {
        g2.setColor(new Color(80, 40, 10));
        g2.setStroke(new BasicStroke(1.5f));

        // Palacio negro (arriba): cols 3-5, filas 0-2
        int px1 = MARGEN + 3 * CELDA;
        int py1 = MARGEN;
        int pw  = 2 * CELDA;
        int ph  = 2 * CELDA;
        g2.drawRect(px1, py1, pw, ph);
        g2.drawLine(px1, py1, px1 + pw, py1 + ph); // diagonal
        g2.drawLine(px1 + pw, py1, px1, py1 + ph); // diagonal

        // Palacio rojo (abajo): cols 3-5, filas 7-9
        int px2 = MARGEN + 3 * CELDA;
        int py2 = MARGEN + 7 * CELDA;
        g2.drawRect(px2, py2, pw, ph);
        g2.drawLine(px2, py2, px2 + pw, py2 + ph);
        g2.drawLine(px2 + pw, py2, px2, py2 + ph);
    }

    private void dibujarRio(Graphics2D g2) {
        int y1 = MARGEN + 4 * CELDA;
        int y2 = MARGEN + 5 * CELDA;
        int x1 = MARGEN;
        int x2 = MARGEN + (COLS - 1) * CELDA;

        // Fondo del río
        g2.setColor(new Color(100, 160, 220, 80));
        g2.fillRect(x1, y1, x2 - x1, y2 - y1);

        // Texto del río
        g2.setColor(new Color(30, 80, 160));
        g2.setFont(new Font("Serif", Font.BOLD, 18));
        FontMetrics fm = g2.getFontMetrics();

        String textoIzq = "楚河";
        String textoDer = "汉界";
        int cy = y1 + (y2 - y1) / 2 + fm.getAscent() / 2 - 2;
        int cx = (x1 + x2) / 2;

        g2.drawString(textoIzq, cx - fm.stringWidth(textoIzq) - 20, cy);
        g2.drawString(textoDer, cx + 20, cy);
    }

    private void dibujarMovimientosValidos(Graphics2D g2) {
        if (seleccionada == null) return;

        // Resaltar casilla seleccionada
        int sx = MARGEN + seleccionada[1] * CELDA;
        int sy = MARGEN + seleccionada[0] * CELDA;
        g2.setColor(new Color(255, 255, 0, 100));
        g2.fillOval(sx - RADIO_PIEZA, sy - RADIO_PIEZA,
                    RADIO_PIEZA * 2, RADIO_PIEZA * 2);

        // Resaltar movimientos posibles
        for (int[] m : movimientosValidos) {
            int mx = MARGEN + m[1] * CELDA;
            int my = MARGEN + m[0] * CELDA;

            if (tablero[m[0]][m[1]] != null) {
                // Captura posible → borde rojo
                g2.setColor(new Color(220, 50, 50, 160));
                g2.setStroke(new BasicStroke(3f));
                g2.drawOval(mx - RADIO_PIEZA, my - RADIO_PIEZA,
                            RADIO_PIEZA * 2, RADIO_PIEZA * 2);
            } else {
                // Movimiento libre → punto verde
                g2.setColor(new Color(50, 200, 50, 180));
                g2.fillOval(mx - 8, my - 8, 16, 16);
            }
        }
    }

    private void dibujarPiezas(Graphics2D g2) {
        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLS; c++) {
                Pieza p = tablero[f][c];
                if (p == null) continue;

                int x = MARGEN + c * CELDA;
                int y = MARGEN + f * CELDA;

                boolean esRojo = p.getColor().equals("rojo");

                // Círculo exterior (borde)
                g2.setColor(new Color(60, 30, 5));
                g2.setStroke(new BasicStroke(2f));
                g2.fillOval(x - RADIO_PIEZA, y - RADIO_PIEZA,
                            RADIO_PIEZA * 2, RADIO_PIEZA * 2);

                // Relleno del círculo
                Color fondoPieza = esRojo
                    ? new Color(210, 40, 40)
                    : new Color(30, 30, 30);
                g2.setColor(fondoPieza);
                g2.fillOval(x - RADIO_PIEZA + 2, y - RADIO_PIEZA + 2,
                            RADIO_PIEZA * 2 - 4, RADIO_PIEZA * 2 - 4);

                // Anillo interior decorativo
                g2.setColor(esRojo
                    ? new Color(240, 180, 180, 120)
                    : new Color(180, 180, 180, 80));
                g2.setStroke(new BasicStroke(1f));
                g2.drawOval(x - RADIO_PIEZA + 5, y - RADIO_PIEZA + 5,
                            RADIO_PIEZA * 2 - 10, RADIO_PIEZA * 2 - 10);

                // Nombre de la pieza
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Serif", Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                String nombre = abreviar(p.getNombre());
                g2.drawString(nombre,
                    x - fm.stringWidth(nombre) / 2,
                    y + fm.getAscent() / 2 - 2);
            }
        }
    }

    /** Abrevia el nombre para que quepa en el círculo. */
    private String abreviar(String nombre) {
        switch (nombre) {
            case "General":       return "将";
            case "Oficial":       return "士";
            case "Elefante":      return "象";
            case "Caballo":       return "马";
            case "Carro":         return "车";
            case "Canon":         return "炮";
            case "Soldado":       return "兵";
            default:              return nombre.substring(0, 1);
        }
    }

    // ================================================================
    //  API PÚBLICA
    // ================================================================
    public String getTurno()          { return turno; }
    public boolean isJuegoActivo()    { return juegoActivo; }
    public Pieza[][] getTablero()     { return tablero; }

    public void setOnTurnoChange(Runnable r)                              { onTurnoChange = r; }
    public void setOnGanador(java.util.function.Consumer<String> c)       { onGanador = c; }

    /** Fuerza retiro del jugador actual. */
    public void retirar() {
        if (!juegoActivo) return;
        juegoActivo = false;
        String ganador = turno.equals("rojo") ? "negro" : "rojo";
        if (onGanador != null) onGanador.accept(ganador + "|retiro|" + turno);
    }
}
