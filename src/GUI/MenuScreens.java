package GUI;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author nasry
 */
public class MenuScreens {

    private JFrame frame;

    public MenuScreens() {
        frame = new JFrame("Main Menu");
        frame.setSize(600, 530);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // =========================
        // FONDO 
        // =========================
        JLabel bg = new JLabel(new ImageIcon("src/images/XIANGQI.png"));
        bg.setLayout(null);
        bg.setBounds(0, 0, 600, 500);
        frame.add(bg);

        // =========================
        // BOTON SIGN UP
        // =========================
        JButton btnSignUp = new JButton();
        btnSignUp.setBounds(120, 242, 170, 160);
        btnSignUp.setBorderPainted(false);
        btnSignUp.setContentAreaFilled(false);
        btnSignUp.setFocusPainted(false);
        efectoHover(btnSignUp, "src/images/signup.png", "src/images/hoverloginbtn.png");
        btnSignUp.addActionListener(e -> System.out.println("Ir a SIGN UP"));
        bg.add(btnSignUp); 

        // =========================
        // BOTON LOGIN
        // =========================
        JButton btnLogin = new JButton();
        btnLogin.setBounds(310, 242, 170, 160);
        btnLogin.setBorderPainted(false);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setFocusPainted(false);
        efectoHover(btnLogin, "src/images/login.png", "src/images/hoversignupbtn.png");
        btnLogin.addActionListener(e -> System.out.println("Ir a LOGIN"));
        bg.add(btnLogin);

        // =========================
        // BOTON SALIR
        // =========================
        JButton btnSalir = new JButton();
        btnSalir.setBounds(50, 410, 140, 60);
        btnSalir.setBorderPainted(false);
        btnSalir.setContentAreaFilled(false);
        btnSalir.setFocusPainted(false);
        btnSalir.addActionListener(e -> System.exit(0));
        bg.add(btnSalir);

        frame.setVisible(true);
    }

    // =========================
    // HOVER EFFECT
    // =========================
    private void efectoHover(JButton btn, String normalPath, String hoverPath) {
        ImageIcon normal = new ImageIcon(normalPath);
        ImageIcon hover  = new ImageIcon(hoverPath);
        btn.setIcon(normal);
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setIcon(hover);  }
            @Override
            public void mouseExited(MouseEvent e)  { btn.setIcon(normal); }
        });
    }
}