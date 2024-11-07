/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rocketgame;

import gamedetail.Panel;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

/**a s das sd 
 *
 * @author Bonus
 */
public class Main extends JFrame{
    Main(){
        digi();
        
    }
    void digi(){
        setTitle("Rocket Game");
        setSize(1000,600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        Panel panel = new Panel();
        add(panel);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
               panel.start();
            }
             
        } );
        
    }
    
    public static void main(String[] args) {
        Main main = new Main();
        main.setVisible(true);
    }
    
            
}