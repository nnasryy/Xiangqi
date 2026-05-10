package GUI;

import Users.Usuario;
import almacenamiento.Sistema;
import juego.Tablero;
import piezas.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * @author nasry
 */
public class JuegoXiangqi {

    private JFrame  frame;
    private Sistema sistema;
    private Usuario jugador1;  // rojo  — izquierda
    private Usuario jugador2;  // negro — derecha
    private Tablero tablero;
    private JLabel  lblTurno;

    private JPanel panelCapturasJ1;
    private JPanel panelCapturasJ2;

    public JuegoXiangqi(Sistema sistema, Usuario jugador1, Usuario jugador2) {
        this.sistema  = sistema;
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;

        frame = new JFrame("Xiangqi — " + jugador1.getUsername() +
                           " vs " + jugador2.getUsername());
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());

        // ── Norte: título + turno ──
        frame.add(crearPanelNorte(),    BorderLayout.NORTH);

        // ── Centro: tablero con margen marrón ──
        JPanel wrapTablero = new JPanel(new GridBagLayout());
        wrapTablero.setBackground(new Color(139, 90, 43));
        tablero = new Tablero();
        wrapTablero.add(tablero);
        frame.add(wrapTablero, BorderLayout.CENTER);

        // ── Izquierda: jugador 1 (rojo) ──
        frame.add(crearPanelJugador(jugador1, "rojo", true),  BorderLayout.WEST);

        // ── Derecha: jugador 2 (negro) ──
        frame.add(crearPanelJugador(jugador2, "negro", false), BorderLayout.EAST);

        // ── Sur: botón retirar ──
        frame.add(crearPanelSur(), BorderLayout.SOUTH);

        tablero.setOnTurnoChange(() -> actualizarTurno());
        tablero.setOnGanador(info -> procesarFin(info));
        tablero.setOnCaptura((pieza, colorGanador) -> registrarCaptura(pieza, colorGanador));

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ================================================================
    //  PANEL NORTE — título + turno centrados
    // ================================================================
    private JPanel crearPanelNorte() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(800, 90));
        panel.setBackground(new Color(60, 30, 5));

        JPanel inner = new JPanel(null);
        inner.setOpaque(false);
        inner.setPreferredSize(new Dimension(500, 80));

        JLabel titulo = new JLabel("XIANGQI", SwingConstants.CENTER);
        titulo.setBounds(0, 5, 500, 40);
        titulo.setFont(new Font("Serif", Font.BOLD, 34));
        titulo.setForeground(new Color(255, 210, 80));
        inner.add(titulo);

        JPanel turnoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        turnoPanel.setOpaque(false);
        turnoPanel.setBounds(0, 48, 500, 30);

        JLabel lblTurnoLabel = new JLabel("TURNO:");
        lblTurnoLabel.setFont(new Font("Century", Font.BOLD, 16));
        lblTurnoLabel.setForeground(Color.LIGHT_GRAY);
        turnoPanel.add(lblTurnoLabel);

        lblTurno = new JLabel(jugador1.getUsername());
        lblTurno.setFont(new Font("Century", Font.BOLD, 22));
        lblTurno.setForeground(new Color(220, 80, 80));
        turnoPanel.add(lblTurno);

        inner.add(turnoPanel);
        panel.add(inner);

        return panel;
    }

    // ================================================================
    //  PANEL SUR — botón retirar
    // ================================================================
    private JPanel crearPanelSur() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        panel.setBackground(new Color(60, 30, 5));

        JButton btnRetirar = new JButton("RETIRAR");
        btnRetirar.setPreferredSize(new Dimension(160, 45));
        btnRetirar.setBackground(new Color(153, 0, 0));
        btnRetirar.setForeground(Color.WHITE);
        btnRetirar.setFont(new Font("Century", Font.BOLD, 16));
        btnRetirar.setBorder(BorderFactory.createLineBorder(new Color(255, 100, 100), 2));
        btnRetirar.setFocusPainted(false);
        btnRetirar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRetirar.addActionListener(e -> confirmarRetiro());
        panel.add(btnRetirar);

        return panel;
    }

    // ================================================================
    //  PANEL JUGADOR (izquierda o derecha)
    // ================================================================
    private JPanel crearPanelJugador(Usuario jugador, String color, boolean esRojo) {
        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(170, 600));
        panel.setBackground(new Color(60, 30, 5));

        Color colorTexto = esRojo
            ? new Color(220, 80, 80)
            : new Color(180, 180, 180);

        // ── Etiqueta color ──
        JLabel lblColor = new JLabel(esRojo ? "JUGADOR ROJO" : "JUGADOR NEGRO",
                                     SwingConstants.CENTER);
        lblColor.setBounds(0, 20, 170, 22);
        lblColor.setFont(new Font("Century", Font.BOLD, 12));
        lblColor.setForeground(colorTexto);
        panel.add(lblColor);

        // ── Username ──
        JLabel lblNombre = new JLabel(jugador.getUsername(), SwingConstants.CENTER);
        lblNombre.setBounds(0, 44, 170, 32);
        lblNombre.setFont(new Font("Century", Font.BOLD, 18));
        lblNombre.setForeground(colorTexto);
        panel.add(lblNombre);

        // ── Separador ──
        JSeparator sep = new JSeparator();
        sep.setBounds(10, 82, 150, 3);
        sep.setForeground(new Color(255, 210, 80));
        panel.add(sep);

        // ── Label capturas ──
        JLabel lblCap = new JLabel("CAPTURADAS", SwingConstants.CENTER);
        lblCap.setBounds(0, 90, 170, 20);
        lblCap.setFont(new Font("Century", Font.BOLD, 11));
        lblCap.setForeground(new Color(200, 170, 80));
        panel.add(lblCap);

        // ── Panel de iconos capturados ──
        JPanel panelCapturas = new JPanel(new WrapLayout(FlowLayout.CENTER, 3, 3));
        panelCapturas.setBounds(5, 112, 160, 420);
        panelCapturas.setBackground(new Color(80, 45, 10));
        panelCapturas.setBorder(BorderFactory.createLineBorder(
            new Color(180, 130, 40), 1));

        if (esRojo) panelCapturasJ1 = panelCapturas;
        else        panelCapturasJ2 = panelCapturas;

        panel.add(panelCapturas);

        return panel;
    }

    // ================================================================
    //  REGISTRAR CAPTURA
    // ================================================================
    private void registrarCaptura(Pieza pieza, String colorGanador) {
        String key  = pieza.getColor() + "_" + keyNombre(pieza);
        ImageIcon icon = cargarIcono(key, 44);

        JLabel lbl = new JLabel(icon != null ? icon : new ImageIcon());
        lbl.setToolTipText(pieza.getNombre());

        if (colorGanador.equals("rojo")) {
            panelCapturasJ1.add(lbl);
            panelCapturasJ1.revalidate();
            panelCapturasJ1.repaint();
        } else {
            panelCapturasJ2.add(lbl);
            panelCapturasJ2.revalidate();
            panelCapturasJ2.repaint();
        }
    }

    private String keyNombre(Pieza p) {
        if (p instanceof General)       return "general";
        if (p instanceof Oficial)       return "oficial";
        if (p instanceof Elefante)      return "elefante";
        if (p instanceof Caballo)       return "caballo";
        if (p instanceof CarroDeGuerra) return "carro";
        if (p instanceof Canon)         return "canon";
        return "soldado";
    }

    private ImageIcon cargarIcono(String key, int size) {
        try {
            ImageIcon icon = new ImageIcon("src/images/" + key + ".png");
            Image scaled = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            return null;
        }
    }

    // ================================================================
    //  ACTUALIZAR TURNO
    // ================================================================
    private void actualizarTurno() {
        if (tablero.getTurno().equals("rojo")) {
            lblTurno.setText(jugador1.getUsername());
            lblTurno.setForeground(new Color(220, 80, 80));
        } else {
            lblTurno.setText(jugador2.getUsername());
            lblTurno.setForeground(new Color(180, 180, 180));
        }
    }

    // ================================================================
    //  CONFIRMAR RETIRO
    // ================================================================
    private void confirmarRetiro() {
        String quienRetira = tablero.getTurno().equals("rojo")
            ? jugador1.getUsername()
            : jugador2.getUsername();

        boolean ok = Warning.confirmar(frame,
            "¿Seguro que " + quienRetira + " desea retirarse?");

        if (ok) tablero.retirar();
    }

    // ================================================================
    //  PROCESAR FIN DE JUEGO
    // ================================================================
    private void procesarFin(String info) {
        String  ganadorColor;
        boolean porRetiro;
        String  quienSeRetiro = null;

        if (info.contains("|retiro|")) {
            String[] partes = info.split("\\|");
            ganadorColor  = partes[0];
            porRetiro     = true;
            quienSeRetiro = partes[2].equals("rojo")
                ? jugador1.getUsername()
                : jugador2.getUsername();
        } else {
            ganadorColor = info;
            porRetiro    = false;
        }

        String usernameGanador  = ganadorColor.equals("rojo")
            ? jugador1.getUsername() : jugador2.getUsername();
        String usernamePerdedor = ganadorColor.equals("rojo")
            ? jugador2.getUsername() : jugador1.getUsername();

        sistema.guardarLogPartida(usernameGanador, usernamePerdedor, porRetiro);

        String mensaje;
        if (porRetiro) {
            mensaje = quienSeRetiro + " SE HA RETIRADO\n" +
                      "FELICIDADES " + usernameGanador + ", HAS GANADO 3 PUNTOS";
        } else {
            mensaje = usernameGanador + " VENCIO A " + usernamePerdedor + "\n" +
                      "FELICIDADES " + usernameGanador + " HAS GANADO 3 PUNTOS";
        }

        Warning.mensaje(frame, mensaje);

        frame.dispose();
        new MenuPrincipal(sistema,
            ganadorColor.equals("rojo") ? jugador1 : jugador2);
    }

    // ================================================================
    //  WRAPLAYOUT — permite que los iconos hagan wrap automático
    // ================================================================
    static class WrapLayout extends FlowLayout {
        public WrapLayout(int align, int hgap, int vgap) {
            super(align, hgap, vgap);
        }
        @Override
        public Dimension preferredLayoutSize(Container target) {
            return layoutSize(target, true);
        }
        @Override
        public Dimension minimumLayoutSize(Container target) {
            return layoutSize(target, false);
        }
        private Dimension layoutSize(Container target, boolean preferred) {
            synchronized (target.getTreeLock()) {
                int targetWidth = target.getSize().width;
                if (targetWidth == 0) targetWidth = Integer.MAX_VALUE;
                int hgap = getHgap(), vgap = getVgap();
                Insets insets = target.getInsets();
                int maxWidth = targetWidth - (insets.left + insets.right + hgap * 2);
                int width = 0, height = 0, rowWidth = 0, rowHeight = 0;
                int nmembers = target.getComponentCount();
                for (int i = 0; i < nmembers; i++) {
                    Component m = target.getComponent(i);
                    if (m.isVisible()) {
                        Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();
                        if (rowWidth + d.width > maxWidth) {
                            width = Math.max(width, rowWidth);
                            height += rowHeight + vgap;
                            rowWidth = 0; rowHeight = 0;
                        }
                        rowWidth += d.width + hgap;
                        rowHeight = Math.max(rowHeight, d.height);
                    }
                }
                width = Math.max(width, rowWidth);
                height += rowHeight + insets.top + insets.bottom + vgap * 2;
                return new Dimension(width, height);
            }
        }
    }
}