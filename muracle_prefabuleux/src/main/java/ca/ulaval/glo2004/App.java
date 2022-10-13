package ca.ulaval.glo2004;


import ca.ulaval.glo2004.gui.MainWindow;

import javax.swing.*;


public class App {

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(mainWindow);
        }catch (Exception e){}
        mainWindow.setVisible(true);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

