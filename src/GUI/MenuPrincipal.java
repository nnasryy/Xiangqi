/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import Users.Usuario;
import almacenamiento.Sistema;
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
public class MenuPrincipal {

    private JFrame frame;

    public MenuPrincipal(Sistema sistema, Usuario actual) {
        frame = new JFrame("Menu Principal");
        frame.setSize(600, 530);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // =========================
        // FONDO 
        // =========================
        JLabel bg = new JLabel(new ImageIcon("src/images/MeunPrincipal.png"));
        bg.setLayout(null);
        bg.setBounds(0, 0, 600, 500);
        frame.add(bg);

        // =========================
        // JUGAR XIANGQI
        // =========================
        JButton jugar = new JButton();
        jugar.setBounds(10, 190, 180, 170);
        jugar.setBorderPainted(false);
        jugar.setContentAreaFilled(false);
        jugar.setFocusPainted(false);
        efectoHover(jugar, "src/images/jugarxiangqi.png", "src/images/hoverjugarxiangqi.png");
        jugar.addActionListener(e -> {
            frame.dispose();
            //  new Login();
        });
        bg.add(jugar);

        // =========================
        // BOTON MI CUENTA
        // =========================
        JButton miCuenta = new JButton();
        miCuenta.setBounds(210, 190, 180, 170);
        miCuenta.setBorderPainted(false);
        miCuenta.setContentAreaFilled(false);
        miCuenta.setFocusPainted(false);
        efectoHover(miCuenta, "src/images/micuenta.png", "src/images/hovermicuenta.png");
        miCuenta.addActionListener(e -> {
            frame.dispose();
            // new Login();
        });
        bg.add(miCuenta);
        //=========================
        // BOTON REPORTES
        // =========================
        JButton reportes = new JButton();
        reportes.setBounds(410, 190, 180, 170);
        reportes.setBorderPainted(false);
        reportes.setContentAreaFilled(false);
        reportes.setFocusPainted(false);
        efectoHover(reportes, "src/images/reportes.png", "src/images/hoverreportes.png");
        reportes.addActionListener(e -> {
            frame.dispose();
            //new Login();
        });
        bg.add(reportes);
        //=========================
        // BOTON SALIR
        // =========================
        JButton salir = new JButton();
        salir.setBounds(210, 370, 180, 100);
        salir.setBorderPainted(false);
        salir.setContentAreaFilled(false);
        salir.setFocusPainted(false);
        efectoHover(salir, "src/images/salirmenup.png", "src/images/salirmenup.png");
        salir.addActionListener(e -> {
            frame.dispose();
      new MenuScreens(sistema);
              });
        bg.add(salir);
frame.setVisible(true);
    }
    // =========================
    // HOVER EFFECT
    // =========================

    private void efectoHover(JButton btn, String normalPath, String hoverPath) {
        ImageIcon normal = new ImageIcon(normalPath);
        ImageIcon hover = new ImageIcon(hoverPath);
        btn.setIcon(normal);
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setIcon(hover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setIcon(normal);
            }
        });
    }
}
