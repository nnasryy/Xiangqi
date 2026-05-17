package GUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * @author nasry
 */
public class Warning extends JDialog {

    public enum Tipo { MENSAJE, CONFIRMAR, PASSWORD }

    private boolean confirmado = false;
    private String  password   = null;

    public Warning(Frame parent, String mensaje, Tipo tipo) {
        super(parent, true);
        setUndecorated(true);
        setSize(590, 280);
        setLocationRelativeTo(parent);
        setLayout(null);

        JLabel bg = new JLabel(new ImageIcon("src/images/Warning.png"));
        bg.setLayout(null);
        bg.setBounds(0, 0, 590, 280);
        add(bg);

        JLabel lblMensaje = new JLabel(
            "<html><div style='text-align:center;'>" +
            mensaje.replace("\n", "<br>") +
            "</div></html>",
            SwingConstants.CENTER
        );
        lblMensaje.setFont(new Font("Century", Font.PLAIN, 18));
        lblMensaje.setForeground(Color.BLACK);

        switch (tipo) {
            case MENSAJE   -> construirMensaje(bg, lblMensaje);
            case CONFIRMAR -> construirConfirmar(bg, lblMensaje);
            case PASSWORD  -> construirPassword(bg, lblMensaje, mensaje);
        }

        setVisible(true);
    }

   
    private void construirMensaje(JLabel bg, JLabel lblMensaje) {
        lblMensaje.setBounds(40, 80, 520, 100);
        bg.add(lblMensaje);

        JButton btnOk = crearBoton("OK", new Color(180, 100, 0));
        btnOk.setBounds(215, 205, 160, 40);
        btnOk.addActionListener(e -> dispose());
        bg.add(btnOk);
    }

    
    private void construirConfirmar(JLabel bg, JLabel lblMensaje) {
        lblMensaje.setBounds(40, 80, 520, 110);
        bg.add(lblMensaje);

        JButton btnSi = crearBotonPequeno("SÍ", new Color(0, 120, 0));
        btnSi.setBounds(140, 205, 120, 38);
        btnSi.addActionListener(e -> { confirmado = true; dispose(); });
        bg.add(btnSi);

        JButton btnNo = crearBotonPequeno("NO", new Color(153, 0, 0));
        btnNo.setBounds(340, 205, 120, 38);
        btnNo.addActionListener(e -> { confirmado = false; dispose(); });
        bg.add(btnNo);
    }

    
    private void construirPassword(JLabel bg, JLabel lblMensaje, String mensaje) {
     
        lblMensaje.setBounds(40, 70, 520, 55);
        bg.add(lblMensaje);

    
        JPasswordField passField = new JPasswordField();
        passField.setBounds(110, 125, 310, 40);
        passField.setBackground(new Color(255, 215, 114));
        passField.setFont(new Font("Century", Font.PLAIN, 18));
        passField.setBorder(new LineBorder(Color.BLACK, 2));
        passField.setHorizontalAlignment(JTextField.CENTER);
        passField.setEchoChar('●');
        bg.add(passField);

        
        JButton btnOjo = new JButton("👁");
        btnOjo.setBounds(425, 125, 42, 40);
        btnOjo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
        btnOjo.setBackground(new Color(255, 215, 114));
        btnOjo.setBorder(new LineBorder(Color.BLACK, 2));
        btnOjo.setFocusPainted(false);
        btnOjo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnOjo.addActionListener(e -> {
            if (passField.getEchoChar() == '●') {
                passField.setEchoChar((char) 0);
                btnOjo.setText("🙈");
            } else {
                passField.setEchoChar('●');
                btnOjo.setText("👁");
            }
        });
        bg.add(btnOjo);

      
        JLabel hint = new JLabel("* 5 caracteres: letras y números", SwingConstants.CENTER);
        hint.setBounds(110, 168, 357, 18);
        hint.setFont(new Font("Century", Font.ITALIC, 11));
        hint.setForeground(new Color(100, 0, 0));
        hint.setVisible(mensaje.toLowerCase().contains("nuevo"));
        bg.add(hint);


        JButton btnConfirmar = crearBotonPequeno("CONFIRMAR", new Color(0, 120, 0));
        btnConfirmar.setBounds(95, 205, 160, 38);
        btnConfirmar.addActionListener(e -> {
            password   = new String(passField.getPassword());
            confirmado = true;
            dispose();
        });
        bg.add(btnConfirmar);

        JButton btnCancelar = crearBotonPequeno("CANCELAR", new Color(153, 0, 0));
        btnCancelar.setBounds(345, 205, 160, 38);
        btnCancelar.addActionListener(e -> { confirmado = false; dispose(); });
        bg.add(btnCancelar);
    }

    
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

    private JButton crearBotonPequeno(String texto, Color fondo) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Century", Font.BOLD, 13));
        btn.setBackground(fondo);
        btn.setForeground(Color.WHITE);
        btn.setBorder(new LineBorder(Color.BLACK, 2));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public boolean getConfirmado() { return confirmado; }
    public String  getPassword()   { return password;   }

    public static void mensaje(Frame parent, String mensaje) {
        new Warning(parent, mensaje, Tipo.MENSAJE);
    }

    public static boolean confirmar(Frame parent, String mensaje) {
        Warning w = new Warning(parent, mensaje, Tipo.CONFIRMAR);
        return w.getConfirmado();
    }

    public static String pedirPassword(Frame parent, String mensaje) {
        Warning w = new Warning(parent, mensaje, Tipo.PASSWORD);
        return w.getConfirmado() ? w.getPassword() : null;
    }
}