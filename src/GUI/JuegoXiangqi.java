package GUI;

import Users.Usuario;
import almacenamiento.Sistema;
import juego.Tablero;
import java.awt.*;
import javax.swing.*;

/**
 * @author nasry
 */
public class JuegoXiangqi {

    private JFrame  frame;
    private Sistema sistema;
    private Usuario jugador1;
    private Usuario jugador2;
    private Tablero tablero;
    private JLabel  lblTurno;

    public JuegoXiangqi(Sistema sistema, Usuario jugador1, Usuario jugador2) {
        this.sistema  = sistema;
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;

        frame = new JFrame("Xiangqi — " + jugador1.getUsername() +
                           " vs " + jugador2.getUsername());
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());

        tablero = new Tablero();
        frame.add(tablero, BorderLayout.CENTER);
        frame.add(crearPanelLateral(), BorderLayout.EAST);

        tablero.setOnTurnoChange(() -> actualizarTurno());
        tablero.setOnGanador(info -> procesarFin(info));

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ================================================================
    //  PANEL LATERAL
    // ================================================================
    private JPanel crearPanelLateral() {
        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(220, 700));
        panel.setBackground(new Color(60, 30, 5));

        // ── Título ──
        JLabel titulo = new JLabel("XIANGQI", SwingConstants.CENTER);
        titulo.setBounds(0, 30, 220, 50);
        titulo.setFont(new Font("Serif", Font.BOLD, 28));
        titulo.setForeground(new Color(255, 210, 80));
        panel.add(titulo);

        separador(panel, 90);

        // ── Jugador 1 (ROJO) ──
        JLabel lblJ1titulo = new JLabel("JUGADOR ROJO", SwingConstants.CENTER);
        lblJ1titulo.setBounds(0, 105, 220, 25);
        lblJ1titulo.setFont(new Font("Century", Font.BOLD, 13));
        lblJ1titulo.setForeground(new Color(220, 80, 80));
        panel.add(lblJ1titulo);

        JLabel lblJ1 = new JLabel(jugador1.getUsername(), SwingConstants.CENTER);
        lblJ1.setBounds(0, 130, 220, 35);
        lblJ1.setFont(new Font("Century", Font.BOLD, 20));
        lblJ1.setForeground(new Color(220, 80, 80));
        panel.add(lblJ1);

        // ── VS ──
        JLabel vs = new JLabel("VS", SwingConstants.CENTER);
        vs.setBounds(0, 175, 220, 40);
        vs.setFont(new Font("Serif", Font.BOLD, 26));
        vs.setForeground(new Color(255, 210, 80));
        panel.add(vs);

        // ── Jugador 2 (NEGRO) ──
        JLabel lblJ2titulo = new JLabel("JUGADOR NEGRO", SwingConstants.CENTER);
        lblJ2titulo.setBounds(0, 220, 220, 25);
        lblJ2titulo.setFont(new Font("Century", Font.BOLD, 13));
        lblJ2titulo.setForeground(new Color(180, 180, 180));
        panel.add(lblJ2titulo);

        JLabel lblJ2 = new JLabel(jugador2.getUsername(), SwingConstants.CENTER);
        lblJ2.setBounds(0, 245, 220, 35);
        lblJ2.setFont(new Font("Century", Font.BOLD, 20));
        lblJ2.setForeground(new Color(180, 180, 180));
        panel.add(lblJ2);

        separador(panel, 295);

        // ── Turno ──
        JLabel lblTurnoTitle = new JLabel("TURNO ACTUAL", SwingConstants.CENTER);
        lblTurnoTitle.setBounds(0, 312, 220, 28);
        lblTurnoTitle.setFont(new Font("Century", Font.BOLD, 14));
        lblTurnoTitle.setForeground(Color.LIGHT_GRAY);
        panel.add(lblTurnoTitle);

        lblTurno = new JLabel(jugador1.getUsername(), SwingConstants.CENTER);
        lblTurno.setBounds(0, 340, 220, 45);
        lblTurno.setFont(new Font("Century", Font.BOLD, 20));
        lblTurno.setForeground(new Color(220, 80, 80));
        panel.add(lblTurno);

        separador(panel, 395);

        // ── Botón RETIRAR ──
        JButton btnRetirar = new JButton("RETIRAR");
        btnRetirar.setBounds(20, 415, 180, 55);
        btnRetirar.setBackground(new Color(153, 0, 0));
        btnRetirar.setForeground(Color.WHITE);
        btnRetirar.setFont(new Font("Century", Font.BOLD, 18));
        btnRetirar.setBorder(BorderFactory.createLineBorder(new Color(255, 100, 100), 2));
        btnRetirar.setFocusPainted(false);
        btnRetirar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRetirar.addActionListener(e -> confirmarRetiro());
        panel.add(btnRetirar);

        return panel;
    }

    private void separador(JPanel panel, int y) {
        JSeparator sep = new JSeparator();
        sep.setBounds(15, y, 190, 4);
        sep.setForeground(new Color(255, 210, 80));
        panel.add(sep);
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
}