package GUI;

import almacenamiento.Sistema;
import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * @author nasry
 */
public class Signup {

    JFrame frame;
    JPasswordField passField;
    JTextField userField;
    private Sistema sistema;

    public Signup(Sistema sistema) {
        this.sistema = sistema;
        frame = new JFrame("Signup");
        frame.setSize(600, 530);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // FONDO
        JPanel bg = new JPanel();
        bg.setLayout(null);
        bg.setBackground(new Color(218, 100, 0));
        bg.setBounds(0, 0, 600, 500);
        frame.add(bg);

        // PANEL PRINCIPAL
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(255, 184, 50));
        panel.setBorder(new LineBorder(Color.BLACK, 3));
        panel.setBounds(80, 30, 440, 440);
        bg.add(panel);

        // TITULO
        JLabel title = new JLabel("SIGN UP");
        title.setBounds(120, 20, 260, 60);
        title.setFont(new Font("Calisto MT", Font.BOLD, 46));
        title.setForeground(Color.BLACK);
        panel.add(title);

        // HINT password
        JLabel hint = new JLabel("* El password debe tener exactamente 5 caracteres");
        hint.setBounds(80, 75, 370, 20);
        hint.setFont(new Font("Calisto MT", Font.ITALIC, 15));
        hint.setForeground(new Color(100, 0, 0));
        panel.add(hint);

        // USERNAME
        JLabel userLbl = new JLabel("USERNAME:");
        userLbl.setBounds(90, 110, 240, 40);
        userLbl.setFont(new Font("Calisto MT", Font.BOLD, 30));
        panel.add(userLbl);

        userField = new JTextField();
        userField.setBounds(90, 150, 270, 40);
        userField.setBackground(new Color(255, 215, 114));
        userField.setFont(new Font("Century", Font.PLAIN, 20));
        userField.setBorder(new LineBorder(Color.BLACK, 2));
        panel.add(userField);

        // PASSWORD
        JLabel passLbl = new JLabel("PASSWORD:");
        passLbl.setBounds(90, 200, 240, 40);
        passLbl.setFont(new Font("Calisto MT", Font.BOLD, 30));
        panel.add(passLbl);

        passField = new JPasswordField();
        passField.setBounds(90, 240, 270, 40);
        passField.setBackground(new Color(255, 215, 114));
        passField.setFont(new Font("Century", Font.PLAIN, 20));
        passField.setBorder(new LineBorder(Color.BLACK, 2));
        panel.add(passField);

        // Contador de caracteres
        JLabel contador = new JLabel("0/5");
        contador.setBounds(370, 240, 50, 40);
        contador.setFont(new Font("Arial", Font.BOLD, 14));
        contador.setForeground(Color.RED);
        panel.add(contador);

        passField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void actualizar() {
                int len = passField.getPassword().length;
                contador.setText(len + "/5");
                contador.setForeground(len == 5 ? new Color(0, 120, 0) : Color.RED);
            }
            public void insertUpdate (javax.swing.event.DocumentEvent e) { actualizar(); }
            public void removeUpdate (javax.swing.event.DocumentEvent e) { actualizar(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizar(); }
        });

        // BOTON SIGN UP
        JButton signupBtn = new JButton("SIGN UP");
        signupBtn.setBounds(130, 300, 160, 70);
        signupBtn.setBackground(new Color(255, 228, 161));
        signupBtn.setFont(new Font("Century", Font.BOLD, 24));
        signupBtn.setBorder(new LineBorder(Color.BLACK, 3));
        panel.add(signupBtn);

        // BOTON SALIR
        JButton exitBtn = new JButton("SALIR");
        exitBtn.setBounds(20, 390, 90, 40);
        exitBtn.setBackground(new Color(153, 0, 0));
        exitBtn.setFont(new Font("Century", Font.PLAIN, 18));
        exitBtn.setBorder(new LineBorder(Color.BLACK, 3));
        panel.add(exitBtn);

        // =========================
        // ACCIONES
        // =========================
        signupBtn.addActionListener(e -> {
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword());

            if (user.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "El username no puede estar vacío.");
                return;
            }
            if (pass.length() != 5) {
                JOptionPane.showMessageDialog(frame, "El password debe tener exactamente 5 caracteres.");
                return;
            }
            if (sistema.usernameExiste(user)) {
                JOptionPane.showMessageDialog(frame, "Ese username ya existe, elige otro.");
                return;
            }

            boolean ok = sistema.registrar(user, pass);
            if (ok) {
                JOptionPane.showMessageDialog(frame, "¡Cuenta creada! Bienvenido " + user);
                frame.dispose();
                new MenuPrincipal(sistema, sistema.login(user, pass));
            } else {
                JOptionPane.showMessageDialog(frame, "Error al crear la cuenta. Intenta de nuevo.");
            }
        });

        exitBtn.addActionListener(e -> {
            frame.dispose();
            new MenuScreens(sistema);
        });

        frame.setVisible(true);
    }
}