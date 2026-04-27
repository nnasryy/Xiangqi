/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * 
 * @author nasry
 */
public class Warning extends JDialog {

    public enum Tipo { MENSAJE, CONFIRMAR, PASSWORD }

    private boolean confirmado = false;
    private String  password   = null;

    // ================================================================
    //  CONSTRUCTOR
    // ================================================================
    public Warning(Frame parent, String mensaje, Tipo tipo) {
        super(parent, true); 
        setUndecorated(true);
        setSize(590, 280);
        setLocationRelativeTo(parent);
        setLayout(null);

        // ── Fondo con imagen ──
        JLabel bg = new JLabel(new ImageIcon("src/images/Warning.png"));
        bg.setLayout(null);
    bg.setBounds(0, 0, 590, 280);
        add(bg);

        // ── Texto del mensaje ──
        JLabel lblMensaje = new JLabel(
            "<html><div style='text-align:center;'>" +
            mensaje.replace("\n", "<br>") +
            "</div></html>",
            SwingConstants.CENTER
        );
        lblMensaje.setFont(new Font("Century", Font.PLAIN, 18));
        lblMensaje.setForeground(Color.BLACK);

        switch (tipo) {
            case MENSAJE    -> construirMensaje(bg, lblMensaje);
            case CONFIRMAR  -> construirConfirmar(bg, lblMensaje);
            case PASSWORD   -> construirPassword(bg, lblMensaje, mensaje);
        }

        setVisible(true);
    }

    // ================================================================
    //  MODO MENSAJE — solo OK
    // ================================================================
    private void construirMensaje(JLabel bg, JLabel lblMensaje) {
        lblMensaje.setBounds(40, 90, 520, 100);
        bg.add(lblMensaje);

        JButton btnOk = crearBoton("OK", new Color(180, 100, 0));
        btnOk.setBounds(220, 190, 160, 45);
        btnOk.addActionListener(e -> dispose());
        bg.add(btnOk);
    }

    // ================================================================
    //  MODO CONFIRMAR — SÍ / NO
    // ================================================================
    private void construirConfirmar(JLabel bg, JLabel lblMensaje) {
        lblMensaje.setBounds(40, 60, 520, 100);
        bg.add(lblMensaje);

        JButton btnSi = crearBoton("SÍ", new Color(0, 120, 0));
        btnSi.setBounds(130, 190, 140, 45);
        btnSi.addActionListener(e -> { confirmado = true; dispose(); });
        bg.add(btnSi);

        JButton btnNo = crearBoton("NO", new Color(153, 0, 0));
        btnNo.setBounds(330, 190, 140, 45);
        btnNo.addActionListener(e -> { confirmado = false; dispose(); });
        bg.add(btnNo);
    }

    // ================================================================
    //  MODO PASSWORD — campo + CONFIRMAR / CANCELAR
    // ================================================================
    private void construirPassword(JLabel bg, JLabel lblMensaje, String mensaje) {
        lblMensaje.setBounds(40, 30, 520, 60);
        bg.add(lblMensaje);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(150, 105, 300, 40);
        passField.setBackground(new Color(255, 215, 114));
        passField.setFont(new Font("Century", Font.PLAIN, 18));
        passField.setBorder(new LineBorder(Color.BLACK, 2));
        passField.setHorizontalAlignment(JTextField.CENTER);
        bg.add(passField);

        // Hint debajo del campo
        JLabel hint = new JLabel("* exactamente 5 caracteres", SwingConstants.CENTER);
        hint.setBounds(150, 148, 300, 20);
        hint.setFont(new Font("Century", Font.ITALIC, 11));
        hint.setForeground(new Color(100, 0, 0));
        // Solo mostrar hint si el mensaje menciona password nuevo
        hint.setVisible(mensaje.toLowerCase().contains("nuevo"));
        bg.add(hint);

        JButton btnConfirmar = crearBoton("CONFIRMAR", new Color(0, 120, 0));
        btnConfirmar.setBounds(80, 195, 180, 45);
        btnConfirmar.addActionListener(e -> {
            password   = new String(passField.getPassword());
            confirmado = true;
            dispose();
        });
        bg.add(btnConfirmar);

        JButton btnCancelar = crearBoton("CANCELAR", new Color(153, 0, 0));
        btnCancelar.setBounds(340, 195, 180, 45);
        btnCancelar.addActionListener(e -> { confirmado = false; dispose(); });
        bg.add(btnCancelar);
    }

    // ================================================================
    //  FÁBRICA DE BOTONES
    // ================================================================
    private JButton crearBoton(String texto, Color fondo) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Century", Font.BOLD, 16));
        btn.setBackground(fondo);
        btn.setForeground(Color.WHITE);
        btn.setBorder(new LineBorder(Color.BLACK, 2));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ================================================================
    //  GETTERS DE RESULTADO
    // ================================================================
    public boolean getConfirmado() { return confirmado; }
    public String  getPassword()   { return password;   }

    // ================================================================
    //  MÉTODOS ESTÁTICOS DE CONVENIENCIA
    //  Uso: Warning.mensaje(frame, "texto");
    //       Warning.confirmar(frame, "¿Seguro?")  → true/false
    //       Warning.pedirPassword(frame, "msg")   → String o null
    // ================================================================

    /** Muestra un mensaje simple con OK. */
    public static void mensaje(Frame parent, String mensaje) {
        new Warning(parent, mensaje, Tipo.MENSAJE);
    }

    /** Muestra confirmación SÍ/NO. Retorna true si eligió SÍ. */
    public static boolean confirmar(Frame parent, String mensaje) {
        Warning w = new Warning(parent, mensaje, Tipo.CONFIRMAR);
        return w.getConfirmado();
    }

    /**
     * Muestra campo de password. 
     * Retorna el password ingresado, o null si canceló.
     */
    public static String pedirPassword(Frame parent, String mensaje) {
        Warning w = new Warning(parent, mensaje, Tipo.PASSWORD);
        return w.getConfirmado() ? w.getPassword() : null;
    }
}