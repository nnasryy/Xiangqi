/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import Users.Usuario;
import almacenamiento.Sistema;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * 
 * @author nasry
 */
public class MiCuenta extends JFrame {

    private JFrame  frame;
    private Sistema sistema;
    private Usuario actual;

    public MiCuenta(Sistema sistema, Usuario actual) {
        this.sistema = sistema;
        this.actual  = actual;

        frame = new JFrame("Mi Cuenta");
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
        panel.setBounds(60, 20, 480, 450);
        bg.add(panel);

        // ── Título ──
        JLabel titulo = new JLabel("MI CUENTA", SwingConstants.CENTER);
        titulo.setBounds(0, 20, 480, 50);
        titulo.setFont(new Font("Calisto MT", Font.BOLD, 38));
        titulo.setForeground(Color.BLACK);
        panel.add(titulo);

        // ── Separador ──
        JSeparator sep = new JSeparator();
        sep.setBounds(30, 72, 420, 5);
        sep.setForeground(Color.BLACK);
        panel.add(sep);

        // ── Info del usuario ──
        int y = 85;
        panel.add(crearFila("USERNAME:",     actual.getUsername(),                  y)); y += 48;
        panel.add(crearFila("PUNTOS:",       String.valueOf(actual.getPuntos()),     y)); y += 48;
        panel.add(crearFila("MIEMBRO DESDE:",actual.getFechaIngreso().toString(),   y)); y += 48;
        panel.add(crearFila("ESTADO:",       actual.isActivo() ? "Activo" : "Inactivo", y));

        // ── Separador ──
        JSeparator sep2 = new JSeparator();
        sep2.setBounds(30, 285, 420, 5);
        sep2.setForeground(Color.BLACK);
        panel.add(sep2);

        // ── Botón CAMBIAR PASSWORD ──
        JButton btnCambiarPass = new JButton("CAMBIAR PASSWORD");
        btnCambiarPass.setBounds(60, 300, 360, 50);
        btnCambiarPass.setBackground(new Color(180, 100, 0));
        btnCambiarPass.setForeground(Color.WHITE);
        btnCambiarPass.setFont(new Font("Century", Font.BOLD, 16));
        btnCambiarPass.setBorder(new LineBorder(Color.BLACK, 2));
        btnCambiarPass.setFocusPainted(false);
        btnCambiarPass.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCambiarPass.addActionListener(e -> cambiarPassword());
        panel.add(btnCambiarPass);

        // ── Botón ELIMINAR CUENTA ──
        JButton btnEliminar = new JButton("ELIMINAR MI CUENTA");
        btnEliminar.setBounds(60, 362, 360, 50);
        btnEliminar.setBackground(new Color(153, 0, 0));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFont(new Font("Century", Font.BOLD, 16));
        btnEliminar.setBorder(new LineBorder(Color.BLACK, 2));
        btnEliminar.setFocusPainted(false);
        btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEliminar.addActionListener(e -> eliminarCuenta());
        panel.add(btnEliminar);

        // ── Botón VOLVER ──
        JButton btnVolver = new JButton("VOLVER");
        btnVolver.setBounds(60, 415, 150, 22);
        btnVolver.setBackground(new Color(80, 40, 0));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(new Font("Century", Font.BOLD, 12));
        btnVolver.setBorder(new LineBorder(Color.BLACK, 1));
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
    //  FILA DE INFORMACIÓN
    // ================================================================
    private JPanel crearFila(String etiqueta, String valor, int y) {
        JPanel fila = new JPanel(null);
        fila.setOpaque(false);
        fila.setBounds(30, y, 420, 38);

        JLabel lbl = new JLabel(etiqueta);
        lbl.setBounds(0, 0, 180, 38);
        lbl.setFont(new Font("Calisto MT", Font.BOLD, 18));
        lbl.setForeground(Color.BLACK);
        fila.add(lbl);

        JLabel val = new JLabel(valor);
        val.setBounds(190, 0, 230, 38);
        val.setFont(new Font("Century", Font.PLAIN, 18));
        val.setForeground(new Color(60, 30, 0));
        fila.add(val);

        return fila;
    }

    // ================================================================
    //  CAMBIAR PASSWORD
    // ================================================================
    private void cambiarPassword() {
        String actual_pass = Warning.pedirPassword(frame, "Ingresa tu password actual:");
        if (actual_pass == null) return;

        if (!actual.getPassword().equals(actual_pass)) {
            Warning.mensaje(frame, "Password incorrecto.");
            return;
        }

        String nuevo = Warning.pedirPassword(frame, "Ingresa tu nuevo password:");
        if (nuevo == null) return;

        if (nuevo.length() != 5) {
            Warning.mensaje(frame, "El password debe tener exactamente 5 caracteres.");
            return;
        }

        boolean ok = sistema.cambiarPassword(actual, actual_pass, nuevo);
        if (ok) {
            Warning.mensaje(frame, "Password cambiado correctamente.");
        } else {
            Warning.mensaje(frame, "No se pudo cambiar el password.\nIntenta de nuevo.");
        }
    }

    // ================================================================
    //  ELIMINAR CUENTA
    // ================================================================
    private void eliminarCuenta() {
        boolean seguro = Warning.confirmar(frame,
            "¿Seguro que deseas eliminar tu cuenta?\nEsta acción no se puede deshacer.");
        if (!seguro) return;

        String pass = Warning.pedirPassword(frame, "Confirma tu password para eliminar:");
        if (pass == null) return;

        boolean ok = sistema.eliminarUsuario(actual, pass);
        if (ok) {
            Warning.mensaje(frame, "Cuenta eliminada.\nHasta pronto, " + actual.getUsername() + ".");
            frame.dispose();
            new MenuScreens(sistema);
        } else {
            Warning.mensaje(frame, "Password incorrecto.\nNo se eliminó la cuenta.");
        }
    }
}