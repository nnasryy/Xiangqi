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
        title.setBounds(130, 50, 200, 60);
        title.setFont(new Font("Calisto MT", Font.BOLD, 46));
        title.setForeground(Color.BLACK);
        panel.add(title);

        // USERNAME
        JLabel userLbl = new JLabel("USERNAME:");
        userLbl.setBounds(90, 130, 240, 40);
        userLbl.setFont(new Font("Calisto MT", Font.BOLD, 30));
        panel.add(userLbl);

        userField = new JTextField();
        userField.setBounds(90, 170, 270, 40);
        userField.setBackground(new Color(255, 215, 114));
        userField.setFont(new Font("Century", Font.PLAIN, 20));
        userField.setBorder(new LineBorder(Color.BLACK, 2));
        panel.add(userField);

        // PASSWORD
        JLabel passLbl = new JLabel("PASSWORD:");
        passLbl.setBounds(90, 220, 240, 40);
        passLbl.setFont(new Font("Calisto MT", Font.BOLD, 30));
        panel.add(passLbl);

        passField = new JPasswordField();
        passField.setBounds(90, 260, 270, 40);
        passField.setBackground(new Color(255, 215, 114));
        passField.setFont(new Font("Century", Font.PLAIN, 20));
        passField.setBorder(new LineBorder(Color.BLACK, 2));
        panel.add(passField);

        // BOTON SIGN UP
        JButton signupBtn = new JButton("SIGN UP");
        signupBtn.setBounds(130, 310, 160, 70);
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
            String pass = new String(passField.getPassword()).trim();

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Completa todos los campos.");
                return;
            }

            boolean ok = sistema.registrar(user, pass);

            if (ok) {
                JOptionPane.showMessageDialog(frame, "Cuenta creada. ¡Bienvenido " + user + "!");
                frame.dispose();
                new MenuPrincipal(sistema, sistema.login(user, pass));
            } else {
                JOptionPane.showMessageDialog(frame, "El usuario ya existe o datos inválidos.");
            }
        });

        exitBtn.addActionListener(e -> {
            frame.dispose();
            new MenuScreens(sistema);
        });

        frame.setVisible(true);
    }
}