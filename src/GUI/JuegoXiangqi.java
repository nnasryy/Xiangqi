package GUI;

import Users.Usuario;
import almacenamiento.Sistema;
import juego.Tablero;
import java.awt.*;
import javax.swing.*;

/**
 * @author nasry
 */
public class JuegoXiangqi extends JFrame {

    private JFrame   frame;
    private Sistema  sistema;
    private Usuario  jugador1;
    private Usuario  jugador2;
    private Tablero  tablero;

    private JLabel   lblTurno;
    private JLabel   lblJ1Puntos;
    private JLabel   lblJ2Puntos;

    public JuegoXiangqi(Sistema sistema, Usuario jugador1, Usuario jugador2) {
        this.sistema  = sistema;
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;

        frame = new JFrame("Xiangqi — " + jugador1.getUsername() +
                           " vs " + jugador2.getUsername());
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());

        // ── Tablero (centro) ──
        tablero = new Tablero();
        frame.add(tablero, BorderLayout.CENTER);

        // ── Panel lateral (derecha) ──
        frame.add(crearPanelLateral(), BorderLayout.EAST);

        // ── Callbacks del tablero ──
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
        panel.setPreferredSize(new Dimension(200, 700));
        panel.setBackground(new Color(60, 30, 5));

        JLabel titulo = new JLabel("XIANGQI", SwingConstants.CENTER);
        titulo.setBounds(0, 20, 200, 40);
        titulo.setFont(new Font("Serif", Font.BOLD, 22));
        titulo.setForeground(new Color(255, 210, 80));
        panel.add(titulo);

        JSeparator sep1 = new JSeparator();
        sep1.setBounds(15, 65, 170, 5);
        sep1.setForeground(new Color(255, 210, 80));
        panel.add(sep1);

        // Jugador 1 (ROJO)
        JLabel lblJ1 = new JLabel("- " + jugador1.getUsername(), SwingConstants.CENTER);
        lblJ1.setBounds(0, 80, 200, 30);
        lblJ1.setFont(new Font("Arial", Font.BOLD, 14));
        lblJ1.setForeground(new Color(220, 80, 80));
        panel.add(lblJ1);

        lblJ1Puntos = new JLabel("Puntos: " + jugador1.getPuntos(), SwingConstants.CENTER);
        lblJ1Puntos.setBounds(0, 108, 200, 20);
        lblJ1Puntos.setFont(new Font("Arial", Font.PLAIN, 12));
        lblJ1Puntos.setForeground(Color.LIGHT_GRAY);
        panel.add(lblJ1Puntos);

        JLabel vs = new JLabel("VS", SwingConstants.CENTER);
        vs.setBounds(0, 135, 200, 30);
        vs.setFont(new Font("Serif", Font.BOLD, 18));
        vs.setForeground(new Color(255, 210, 80));
        panel.add(vs);

        // Jugador 2 (NEGRO)
        JLabel lblJ2 = new JLabel("- " + jugador2.getUsername(), SwingConstants.CENTER);
        lblJ2.setBounds(0, 170, 200, 30);
        lblJ2.setFont(new Font("Arial", Font.BOLD, 14));
        lblJ2.setForeground(new Color(180, 180, 180));
        panel.add(lblJ2);

        lblJ2Puntos = new JLabel("Puntos: " + jugador2.getPuntos(), SwingConstants.CENTER);
        lblJ2Puntos.setBounds(0, 198, 200, 20);
        lblJ2Puntos.setFont(new Font("Arial", Font.PLAIN, 12));
        lblJ2Puntos.setForeground(Color.LIGHT_GRAY);
        panel.add(lblJ2Puntos);

        JSeparator sep2 = new JSeparator();
        sep2.setBounds(15, 228, 170, 5);
        sep2.setForeground(new Color(255, 210, 80));
        panel.add(sep2);

        // Turno
        JLabel lblTurnoTitle = new JLabel("TURNO:", SwingConstants.CENTER);
        lblTurnoTitle.setBounds(0, 242, 200, 25);
        lblTurnoTitle.setFont(new Font("Arial", Font.BOLD, 13));
        lblTurnoTitle.setForeground(Color.LIGHT_GRAY);
        panel.add(lblTurnoTitle);

        lblTurno = new JLabel(jugador1.getUsername(), SwingConstants.CENTER);
        lblTurno.setBounds(0, 265, 200, 35);
        lblTurno.setFont(new Font("Arial", Font.BOLD, 16));
        lblTurno.setForeground(new Color(220, 80, 80));
        panel.add(lblTurno);

        JSeparator sep3 = new JSeparator();
        sep3.setBounds(15, 308, 170, 5);
        sep3.setForeground(new Color(255, 210, 80));
        panel.add(sep3);

        // Botón RETIRAR
        JButton btnRetirar = new JButton("RETIRAR");
        btnRetirar.setBounds(25, 325, 150, 45);
        btnRetirar.setBackground(new Color(153, 0, 0));
        btnRetirar.setForeground(Color.WHITE);
        btnRetirar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRetirar.setBorder(BorderFactory.createLineBorder(new Color(255, 100, 100), 2));
        btnRetirar.setFocusPainted(false);
        btnRetirar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRetirar.addActionListener(e -> confirmarRetiro());
        panel.add(btnRetirar);

        return panel;
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

        lblJ1Puntos.setText("Puntos: " + jugador1.getPuntos());
        lblJ2Puntos.setText("Puntos: " + jugador2.getPuntos());

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