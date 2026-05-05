package juego;

import piezas.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author nasry
 */
public class Tablero extends JPanel {

    private static final int FILAS      = 10;
    private static final int COLS       = 9;
    private static final int CELDA      = 60;   
    private static final int MARGEN     = 2;
    private static final int ICON_SIZE  = 52; 

    private Pieza[][]   tablero            = new Pieza[FILAS][COLS];
    private int[]       seleccionada       = null;
    private int[][]     movimientosValidos = new int[0][];

    private String  turno       = "rojo";
    private boolean juegoActivo = true;

    private Runnable                            onTurnoChange;
    private java.util.function.Consumer<String> onGanador;

    private java.util.HashMap<String, ImageIcon> imagenes = new java.util.HashMap<>();


    public Tablero() {
        int ancho = MARGEN * 2 + CELDA * COLS;
        int alto  = MARGEN * 2 + CELDA * FILAS;
        setPreferredSize(new Dimension(ancho, alto));
        setBackground(new Color(26, 24, 24));

        cargarImagenes();
        inicializarPiezas();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                manejarClic(e.getX(), e.getY());
            }
        });
    }

    private void cargarImagenes() {
        String[] nombres = {"general","oficial","elefante","caballo","carro","canon","soldado"};
        String[] colores = {"rojo","negro"};
        for (String color : colores) {
            for (String nombre : nombres) {
                String key  = color + "_" + nombre;
                String path = "src/images/" + key + ".png";
                ImageIcon icon = new ImageIcon(path);
                Image scaled = icon.getImage().getScaledInstance(
                    ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
                imagenes.put(key, new ImageIcon(scaled));
            }
        }
    }

    private String keyImagen(Pieza p) {
        String nombre;
        if      (p instanceof General)       nombre = "general";
        else if (p instanceof Oficial)       nombre = "oficial";
        else if (p instanceof Elefante)      nombre = "elefante";
        else if (p instanceof Caballo)       nombre = "caballo";
        else if (p instanceof CarroDeGuerra) nombre = "carro";
        else if (p instanceof Canon)         nombre = "canon";
        else                                 nombre = "soldado";
        return p.getColor() + "_" + nombre;
    }

    private void inicializarPiezas() {
   
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


    private void manejarClic(int px, int py) {
        if (!juegoActivo) return;

        int col  = (px - MARGEN) / CELDA;
        int fila = (py - MARGEN) / CELDA;

        if (fila < 0 || fila >= FILAS || col < 0 || col >= COLS) return;

        if (seleccionada != null) {
            if (esMovimientoValido(fila, col)) {
                moverPieza(seleccionada[0], seleccionada[1], fila, col);
                deseleccionar();
                return;
            }
            if (tablero[fila][col] != null &&
                tablero[fila][col].getColor().equals(turno)) {
                seleccionar(fila, col);
                return;
            }
            deseleccionar();
            return;
        }

        if (tablero[fila][col] != null &&
            tablero[fila][col].getColor().equals(turno)) {
            seleccionar(fila, col);
        }
    }

    private void seleccionar(int fila, int col) {
        seleccionada       = new int[]{fila, col};
        movimientosValidos = tablero[fila][col].movimientosValidos(fila, col, tablero);
        repaint();
    }

    private void deseleccionar() {
        seleccionada       = null;
        movimientosValidos = new int[0][];
        repaint();
    }

    private boolean esMovimientoValido(int fila, int col) {
        for (int[] m : movimientosValidos)
            if (m[0] == fila && m[1] == col) return true;
        return false;
    }

    private void moverPieza(int fi, int ci, int fd, int cd) {
        Pieza capturada = tablero[fd][cd];
        tablero[fd][cd] = tablero[fi][ci];
        tablero[fi][ci] = null;

        if (capturada instanceof General) {
            juegoActivo = false;
            if (onGanador != null) onGanador.accept(turno);
        } else {
            turno = turno.equals("rojo") ? "negro" : "rojo";
            if (onTurnoChange != null) onTurnoChange.run();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        dibujarCasillas(g2);
        dibujarPalacios(g2);
        dibujarRio(g2);
        dibujarMovimientosValidos(g2);
        dibujarPiezas(g2);
    }


    private void dibujarCasillas(Graphics2D g2) {
        Color claro  = new Color(240, 217, 181); // beige claro
        Color oscuro = new Color(204, 158, 59);  // verde oscuro

        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLS; c++) {
                int x = MARGEN + c * CELDA;
                int y = MARGEN + f * CELDA;
                g2.setColor((f + c) % 2 == 0 ? claro : oscuro);
                g2.fillRect(x, y, CELDA, CELDA);
            }
        }
    }

 
    private void dibujarPalacios(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3f));

        g2.drawRect(MARGEN + 3 * CELDA, MARGEN,
                    3 * CELDA, 3 * CELDA);


        g2.drawRect(MARGEN + 3 * CELDA, MARGEN + 7 * CELDA,
                    3 * CELDA, 3 * CELDA);
    }

   
    private void dibujarRio(Graphics2D g2) {
        int y  = MARGEN + 5 * CELDA;  // línea encima de fila 5
        int x1 = MARGEN;
        int x2 = MARGEN + COLS * CELDA;

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3f));
        g2.drawLine(x1, y, x2, y);
    }


    private void dibujarMovimientosValidos(Graphics2D g2) {
        if (seleccionada == null) return;

    
        int sx = MARGEN + seleccionada[1] * CELDA;
        int sy = MARGEN + seleccionada[0] * CELDA;
        g2.setColor(new Color(255, 255, 0, 140));
        g2.fillRect(sx, sy, CELDA, CELDA);

        for (int[] m : movimientosValidos) {
            int mx = MARGEN + m[1] * CELDA;
            int my = MARGEN + m[0] * CELDA;

            if (tablero[m[0]][m[1]] != null) {

                g2.setColor(new Color(220, 50, 50, 180));
                g2.setStroke(new BasicStroke(3f));
                g2.drawRect(mx + 2, my + 2, CELDA - 4, CELDA - 4);
            } else {
       
                g2.setColor(new Color(50, 200, 50, 250));
                int cx = mx + CELDA / 2;
                int cy = my + CELDA / 2;
                g2.fillOval(cx - 8, cy - 8, 16, 16);
            }
        }
    }


    private void dibujarPiezas(Graphics2D g2) {
        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLS; c++) {
                Pieza p = tablero[f][c];
                if (p == null) continue;

                int x = MARGEN + c * CELDA + (CELDA - ICON_SIZE) / 2;
                int y = MARGEN + f * CELDA + (CELDA - ICON_SIZE) / 2;

                ImageIcon icon = imagenes.get(keyImagen(p));
                if (icon != null) {
                    g2.drawImage(icon.getImage(), x, y, ICON_SIZE, ICON_SIZE, null);
                }
            }
        }
    }

 
    public String    getTurno()      { return turno;       }
    public boolean   isJuegoActivo() { return juegoActivo; }
    public Pieza[][] getTablero()    { return tablero;     }

    public void setOnTurnoChange(Runnable r)                               { onTurnoChange = r; }
    public void setOnGanador(java.util.function.Consumer<String> c)        { onGanador = c;     }

    public void retirar() {
        if (!juegoActivo) return;
        juegoActivo = false;
        String ganador = turno.equals("rojo") ? "negro" : "rojo";
        if (onGanador != null) onGanador.accept(ganador + "|retiro|" + turno);
    }
}