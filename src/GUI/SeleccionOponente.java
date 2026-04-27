/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import Users.Usuario;
import almacenamiento.Sistema;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * @author nasry
 */
public class SeleccionOponente extends JFrame {

    private JFrame  frame;
    private Sistema sistema;
    private Usuario jugador1;

    public SeleccionOponente(Sistema sistema, Usuario jugador1) {
        this.sistema  = sistema;
        this.jugador1 = jugador1;

        frame = new JFrame("Seleccionar Oponente");
        frame.setSize(400, 500);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);

        // ── Fondo ──
        JPanel bg = new JPanel(null);
        bg.setBackground(new Color(218, 100, 0));
        bg.setBounds(0, 0, 400, 470);
        frame.add(bg);

        // ── Panel ──
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(255, 184, 50));
        panel.setBorder(new LineBorder(Color.BLACK, 3));
        panel.setBounds(30, 20, 340, 420);
        bg.add(panel);

        // ── Título ──
        JLabel titulo = new JLabel("SELECCIONA OPONENTE", SwingConstants.CENTER);
        titulo.setBounds(0, 20, 340, 35);
        titulo.setFont(new Font("Calisto MT", Font.BOLD, 20));
        panel.add(titulo);

        JLabel sub = new JLabel("Jugando como: " + jugador1.getUsername(), SwingConstants.CENTER);
        sub.setBounds(0, 55, 340, 25);
        sub.setFont(new Font("Arial", Font.ITALIC, 13));
        sub.setForeground(new Color(100, 0, 0));
        panel.add(sub);

        // ── Lista de jugadores ──
        ArrayList<Usuario> activos = sistema.getUsuariosActivos();
        activos.removeIf(u -> u.getUsername().equals(jugador1.getUsername()));

        if (activos.isEmpty()) {
            JLabel sinJugadores = new JLabel(
                "<html><center>No hay otros jugadores<br>registrados en el sistema.</center></html>",
                SwingConstants.CENTER);
            sinJugadores.setBounds(30, 120, 280, 60);
            sinJugadores.setFont(new Font("Century", Font.BOLD, 14));
            sinJugadores.setForeground(new Color(153, 0, 0));
            panel.add(sinJugadores);

            // Botón VOLVER únicamente
            JButton btnVolver = crearBoton("VOLVER", new Color(153, 0, 0));
            btnVolver.setBounds(90, 350, 160, 40);
            btnVolver.addActionListener(e -> {
                frame.dispose();
                new MenuPrincipal(sistema, jugador1);
            });
            panel.add(btnVolver);

        } else {
            JLabel lblLista = new JLabel("Jugadores disponibles:");
            lblLista.setBounds(30, 90, 280, 25);
            lblLista.setFont(new Font("Century", Font.BOLD, 13));
            panel.add(lblLista);

            DefaultListModel<String> modelo = new DefaultListModel<>();
            for (Usuario u : activos) {
                modelo.addElement(u.getUsername() + "  —  " + u.getPuntos() + " pts");
            }

            JList<String> lista = new JList<>(modelo);
            lista.setFont(new Font("Century", Font.PLAIN, 14));
            lista.setBackground(new Color(255, 215, 114));
            lista.setSelectionBackground(new Color(200, 120, 0));
            lista.setSelectionForeground(Color.WHITE);
            lista.setFixedCellHeight(35);

            JScrollPane scroll = new JScrollPane(lista);
            scroll.setBounds(30, 120, 280, 180);
            scroll.setBorder(new LineBorder(Color.BLACK, 2));
            panel.add(scroll);

            // ── Botón JUGAR ──
            JButton btnJugar = crearBoton("¡JUGAR!", new Color(0, 120, 0));
            btnJugar.setBounds(90, 315, 160, 45);
            btnJugar.addActionListener(e -> {
                int idx = lista.getSelectedIndex();
                if (idx < 0) {
                    Warning.mensaje(frame, "Selecciona un oponente\npara continuar.");
                    return;
                }
                Usuario jugador2 = activos.get(idx);
                frame.dispose();
                new JuegoXiangqi(sistema, jugador1, jugador2);
            });
            panel.add(btnJugar);

            // ── Botón VOLVER ──
            JButton btnVolver = crearBoton("VOLVER", new Color(153, 0, 0));
            btnVolver.setBounds(90, 368, 160, 38);
            btnVolver.addActionListener(e -> {
                frame.dispose();
                new MenuPrincipal(sistema, jugador1);
            });
            panel.add(btnVolver);
        }

        frame.setVisible(true);
    }

    private JButton crearBoton(String texto, Color fondo) {
        JButton btn = new JButton(texto);
        btn.setBackground(fondo);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Century", Font.BOLD, 15));
        btn.setBorder(new LineBorder(Color.BLACK, 2));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
