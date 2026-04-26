/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package xiangqi;

import GUI.MenuScreens;
import almacenamiento.Sistema;

/**
 *
 * @author nasry
 */
public class Xiangqi {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Sistema sistema = new Sistema();
        new MenuScreens(sistema);
    }

}
