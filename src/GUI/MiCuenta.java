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
        panel.setBounds(60, 20, 480, 460);
        bg.add(panel);

        JLabel titulo = new JLabel("MI CUENTA", SwingConstants.CENTER);
        titulo.setBounds(0, 20, 480, 50);
        titulo.setFont(new Font("Calisto MT", Font.BOLD, 38));
        titulo.setForeground(Color.BLACK);
        panel.add(titulo);

        JSeparator sep = new JSeparator();
        sep.setBounds(30, 72, 420, 5);
        sep.setForeground(Color.BLACK);
        panel.add(sep);

        int y = 85;
        panel.add(crearFila("USERNAME:",      actual.getUsername(),                    y)); y += 48;
        panel.add(crearFila("PUNTOS:",        String.valueOf(actual.getPuntos()),       y)); y += 48;
        panel.add(crearFila("MIEMBRO DESDE:", actual.getFechaIngreso().toString(),      y)); y += 48;
        panel.add(crearFila("ESTADO:",        actual.isActivo() ? "Activo" : "Inactivo", y));

        JSeparator sep2 = new JSeparator();
        sep2.setBounds(30, 277, 420, 5);
        sep2.setForeground(Color.BLACK);
        panel.add(sep2);

        // CAMBIAR PASSWORD
        JButton btnCambiarPass = new JButton("CAMBIAR PASSWORD");
        btnCambiarPass.setBounds(60, 290, 360, 44);
        estilo(btnCambiarPass, new Color(180, 100, 0));
        btnCambiarPass.addActionListener(e -> cambiarPassword());
        panel.add(btnCambiarPass);

        // DESACTIVAR / ACTIVAR CUENTA
        String lblDesact = actual.isActivo() ? "DESACTIVAR MI CUENTA" : "ACTIVAR MI CUENTA";
        JButton btnDesactivar = new JButton(lblDesact);
        btnDesactivar.setBounds(60, 344, 360, 44);
        estilo(btnDesactivar, new Color(120, 60, 0));
        btnDesactivar.addActionListener(e -> toggleActivacion(btnDesactivar));
        panel.add(btnDesactivar);

        // ELIMINAR CUENTA
        JButton btnEliminar = new JButton("ELIMINAR MI CUENTA");
        btnEliminar.setBounds(60, 398, 360, 44);
        estilo(btnEliminar, new Color(153, 0, 0));
        btnEliminar.addActionListener(e -> eliminarCuenta());
        panel.add(btnEliminar);

        // VOLVER
        JButton btnVolver = new JButton("VOLVER");
        btnVolver.setBounds(60, 428, 150, 22);
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
        btn.setFont(new Font("Century", Font.BOLD, 15));
        btn.setBorder(new LineBorder(Color.BLACK, 2));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JPanel crearFila(String etiqueta, String valor, int y) {
        JPanel fila = new JPanel(null);
        fila.setOpaque(false);
        fila.setBounds(30, y, 420, 38);

        JLabel lbl = new JLabel(etiqueta);
        lbl.setBounds(0, 0, 180, 38);
        lbl.setFont(new Font("Calisto MT", Font.BOLD, 18));
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
                btn.setText("ACTIVAR MI CUENTA");
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
            Warning.mensaje(frame, "Password incorrecto.\nNo se eliminó la cuenta.");
        }
    }
}