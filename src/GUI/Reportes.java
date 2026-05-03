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
public class Reportes {

    private JFrame  frame;
    private Sistema sistema;
    private Usuario actual;

    public Reportes(Sistema sistema, Usuario actual) {
        this.sistema = sistema;
        this.actual  = actual;

        frame = new JFrame("Reportes");
        frame.setSize(600, 530);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // ── Fondo ──
        JPanel bg = new JPanel(null);
        bg.setBackground(new Color(218, 100, 0));
        bg.setBounds(0, 0, 600, 500);
        frame.add(bg);

        // ── Panel principal ──
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(255, 184, 50));
        panel.setBorder(new LineBorder(Color.BLACK, 3));
        panel.setBounds(30, 15, 540, 460);
        bg.add(panel);

        // ── Título ──
        JLabel titulo = new JLabel("REPORTES", SwingConstants.CENTER);
        titulo.setBounds(0, 15, 540, 45);
        titulo.setFont(new Font("Calisto MT", Font.BOLD, 36));
        titulo.setForeground(Color.BLACK);
        panel.add(titulo);

        // ── Tabs ──
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBounds(15, 68, 510, 340);
        tabs.setFont(new Font("Century", Font.BOLD, 14));
        tabs.setBackground(new Color(255, 200, 80));
        tabs.addTab(" Ranking Jugadores",    crearTabRanking());
        tabs.addTab("Mis Últimos Partidos", crearTabLogs());
        panel.add(tabs);

        // ── Botón VOLVER ──
        JButton btnVolver = new JButton("VOLVER");
        btnVolver.setBounds(190, 418, 160, 35);
        btnVolver.setBackground(new Color(80, 40, 0));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(new Font("Century", Font.BOLD, 14));
        btnVolver.setBorder(new LineBorder(Color.BLACK, 2));
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> {
            frame.dispose();
            new MenuPrincipal(sistema, actual);
        });
        panel.add(btnVolver);

        frame.setVisible(true);
    }

    // ================================================================
    //  TAB 1 — RANKING DE JUGADORES
    // ================================================================
    private JPanel crearTabRanking() {
        JPanel tab = new JPanel(null);
        tab.setBackground(new Color(255, 215, 114));

        // Encabezado
        String[] cols   = {"#", "USERNAME", "PUNTOS"};
        int[]    anchos = {40, 280, 130};
        int x = 10;
        for (int i = 0; i < cols.length; i++) {
            JLabel h = new JLabel(cols[i], SwingConstants.CENTER);
            h.setBounds(x, 8, anchos[i], 30);
            h.setFont(new Font("Century", Font.BOLD, 14));
            h.setOpaque(true);
            h.setBackground(new Color(180, 100, 0));
            h.setForeground(Color.WHITE);
            h.setBorder(new LineBorder(Color.BLACK, 1));
            tab.add(h);
            x += anchos[i];
        }

        // Filas
        ArrayList<Usuario> ranking = sistema.getRankingJugadores();

        JPanel filas = new JPanel(null);
        filas.setPreferredSize(new Dimension(460, Math.max(200, ranking.size() * 36)));
        filas.setBackground(new Color(255, 215, 114));

        for (int i = 0; i < ranking.size(); i++) {
            Usuario u    = ranking.get(i);
            int     fy   = i * 36;
            Color   fondo = (i % 2 == 0)
                ? new Color(255, 228, 161)
                : new Color(255, 215, 114);

            agregarCelda(filas, String.valueOf(i + 1),  fondo,  10, fy,  40, 34);
            agregarCelda(filas, u.getUsername(),         fondo,  50, fy, 280, 34);
            agregarCelda(filas, u.getPuntos() + " pts",  fondo, 330, fy, 130, 34);
        }

        if (ranking.isEmpty()) {
            JLabel vacio = new JLabel("Sin jugadores registrados.", SwingConstants.CENTER);
            vacio.setBounds(0, 50, 460, 30);
            vacio.setFont(new Font("Century", Font.ITALIC, 14));
            filas.add(vacio);
        }

        JScrollPane scroll = new JScrollPane(filas);
        scroll.setBounds(10, 45, 470, 240);
        scroll.setBorder(new LineBorder(Color.BLACK, 1));
        tab.add(scroll);

        return tab;
    }

    private void agregarCelda(JPanel panel, String texto, Color fondo,
                               int x, int y, int w, int h) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setBounds(x, y, w, h);
        lbl.setFont(new Font("Century", Font.PLAIN, 13));
        lbl.setOpaque(true);
        lbl.setBackground(fondo);
        lbl.setBorder(new LineBorder(new Color(200, 160, 80), 1));
        panel.add(lbl);
    }

    // ================================================================
    //  TAB 2 — MIS ÚLTIMOS PARTIDOS
    // ================================================================
    private JPanel crearTabLogs() {
        JPanel tab = new JPanel(null);
        tab.setBackground(new Color(255, 215, 114));

        JTextArea areaLogs = new JTextArea();
        areaLogs.setEditable(false);
        areaLogs.setFont(new Font("Century", Font.PLAIN, 13));
        areaLogs.setBackground(new Color(255, 228, 161));
        areaLogs.setForeground(Color.BLACK);
        areaLogs.setMargin(new Insets(8, 8, 8, 8));
        areaLogs.setLineWrap(true);
        areaLogs.setWrapStyleWord(true);

        ArrayList<String> logs = sistema.obtenerLogsUsuario(actual.getUsername());
        if (logs.isEmpty()) {
            areaLogs.setText("No tienes partidas registradas aún.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < logs.size(); i++) {
                sb.append(i + 1).append(". ").append(logs.get(i)).append("\n\n");
            }
            areaLogs.setText(sb.toString());
        }

        JScrollPane scroll = new JScrollPane(areaLogs);
        scroll.setBounds(10, 10, 470, 275);
        scroll.setBorder(new LineBorder(Color.BLACK, 2));
        tab.add(scroll);

        return tab;
    }
}