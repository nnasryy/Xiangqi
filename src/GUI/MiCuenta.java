package GUI;

import Users.Usuario;
import almacenamiento.Sistema;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * @author nasry
 */
public class MiCuenta {

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

        JPanel bg = new JPanel(null);
        bg.setBackground(new Color(218, 100, 0));
        bg.setBounds(0, 0, 600, 500);
        frame.add(bg);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(255, 184, 50));
        panel.setBorder(new LineBorder(Color.BLACK, 3));
        panel.setBounds(60, 15, 480, 465);
        bg.add(panel);

        // ── Título ──
        JLabel titulo = new JLabel("MI CUENTA", SwingConstants.CENTER);
        titulo.setBounds(0, 15, 480, 45);
        titulo.setFont(new Font("Calisto MT", Font.BOLD, 36));
        titulo.setForeground(Color.BLACK);
        panel.add(titulo);

        JSeparator sep = new JSeparator();
        sep.setBounds(25, 62, 430, 4);
        sep.setForeground(Color.BLACK);
        panel.add(sep);

        // ── Info ──
        int y = 72;
        panel.add(crearFila("USERNAME:",      actual.getUsername(),                      y)); y += 44;
        panel.add(crearFila("PUNTOS:",        String.valueOf(actual.getPuntos()),         y)); y += 44;
        panel.add(crearFila("MIEMBRO DESDE:", actual.getFechaIngreso().toString(),        y)); y += 44;
        panel.add(crearFila("ESTADO:",        actual.isActivo() ? "Activo" : "Inactivo", y));

        JSeparator sep2 = new JSeparator();
        sep2.setBounds(25, 255, 430, 4);
        sep2.setForeground(Color.BLACK);
        panel.add(sep2);

        // ── Botones — todos del mismo ancho y alto reducido ──
        int bx = 70, bw = 340, bh = 38, by = 265, gap = 46;

        JButton btnCambiarPass = new JButton("CAMBIAR PASSWORD");
        btnCambiarPass.setBounds(bx, by, bw, bh);
        estilo(btnCambiarPass, new Color(180, 100, 0));
        btnCambiarPass.addActionListener(e -> cambiarPassword());
        panel.add(btnCambiarPass);

        String lblDesact = actual.isActivo() ? "DESACTIVAR MI CUENTA" : "ACTIVAR MI CUENTA";
        JButton btnDesactivar = new JButton(lblDesact);
        btnDesactivar.setBounds(bx, by + gap, bw, bh);
        estilo(btnDesactivar, new Color(120, 60, 0));
        btnDesactivar.addActionListener(e -> toggleActivacion(btnDesactivar));
        panel.add(btnDesactivar);

        JButton btnEliminar = new JButton("ELIMINAR MI CUENTA");
        btnEliminar.setBounds(bx, by + gap * 2, bw, bh);
        estilo(btnEliminar, new Color(153, 0, 0));
        btnEliminar.addActionListener(e -> eliminarCuenta());
        panel.add(btnEliminar);

        JSeparator sep3 = new JSeparator();
        sep3.setBounds(25, by + gap * 3 + 5, 430, 3);
        sep3.setForeground(Color.BLACK);
        panel.add(sep3);

        JButton btnVolver = new JButton("VOLVER");
        btnVolver.setBounds(bx, by + gap * 3 + 15, bw, bh);
        estilo(btnVolver, new Color(80, 40, 0));
        btnVolver.addActionListener(e -> {
            frame.dispose();
            new MenuPrincipal(sistema, actual);
        });
        panel.add(btnVolver);

        frame.setVisible(true);
    }

    private void estilo(JButton btn, Color fondo) {
        btn.setBackground(fondo);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Century", Font.BOLD, 14));
        btn.setBorder(new LineBorder(Color.BLACK, 2));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JPanel crearFila(String etiqueta, String valor, int y) {
        JPanel fila = new JPanel(null);
        fila.setOpaque(false);
        fila.setBounds(25, y, 430, 36);

        JLabel lbl = new JLabel(etiqueta);
        lbl.setBounds(0, 0, 180, 36);
        lbl.setFont(new Font("Calisto MT", Font.BOLD, 17));
        fila.add(lbl);

        JLabel val = new JLabel(valor);
        val.setBounds(185, 0, 245, 36);
        val.setFont(new Font("Century", Font.PLAIN, 17));
        val.setForeground(new Color(60, 30, 0));
        fila.add(val);

        return fila;
    }

    // ================================================================
    //  CAMBIAR PASSWORD
    // ================================================================
    private void cambiarPassword() {
        String actualPass = Warning.pedirPassword(frame, "Ingresa tu password actual:");
        if (actualPass == null) return;

        if (!actual.getPassword().equals(actualPass)) {
            Warning.mensaje(frame, "Password incorrecto.");
            return;
        }

        String nuevo = Warning.pedirPassword(frame, "Ingresa tu nuevo password:");
        if (nuevo == null) return;

        String error = sistema.validarPasswordMensaje(nuevo);
        if (error != null) {
            Warning.mensaje(frame, error);
            return;
        }

        if (sistema.cambiarPassword(actual, actualPass, nuevo)) {
            Warning.mensaje(frame, "Password cambiado correctamente.");
        } else {
            Warning.mensaje(frame, "No se pudo cambiar el password.");
        }
    }

    // ================================================================
    //  DESACTIVAR / ACTIVAR
    // ================================================================
    private void toggleActivacion(JButton btn) {
        if (actual.isActivo()) {
            boolean seguro = Warning.confirmar(frame,
                "¿Seguro que deseas desactivar tu cuenta?\nPodrás reactivarla al hacer login.");
            if (!seguro) return;

            String pass = Warning.pedirPassword(frame, "Confirma tu password:");
            if (pass == null) return;

            if (sistema.desactivarUsuario(actual, pass)) {
                Warning.mensaje(frame, "Cuenta desactivada.");
                frame.dispose();
                new MenuScreens(sistema);
            } else {
                Warning.mensaje(frame, "Password incorrecto.");
            }
        } else {
            String pass = Warning.pedirPassword(frame, "Confirma tu password para activar:");
            if (pass == null) return;

            if (sistema.activarUsuario(actual, pass)) {
                Warning.mensaje(frame, "Cuenta activada.");
                btn.setText("DESACTIVAR MI CUENTA");
            } else {
                Warning.mensaje(frame, "Password incorrecto.");
            }
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

        if (sistema.eliminarUsuario(actual, pass)) {
            Warning.mensaje(frame, "Cuenta eliminada.\nHasta pronto, " + actual.getUsername() + ".");
            frame.dispose();
            new MenuScreens(sistema);
        } else {
            Warning.mensaje(frame, "Password incorrecto.");
        }
    }
}