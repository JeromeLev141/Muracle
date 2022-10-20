package ca.ulaval.glo2004;


import ca.ulaval.glo2004.gui.MainWindow;
import com.formdev.flatlaf.intellijthemes.FlatNordIJTheme;

import javax.swing.*;


public class App {

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        try {
            UIManager.setLookAndFeel(new FlatNordIJTheme());
            SwingUtilities.updateComponentTreeUI(mainWindow);
        }catch (Exception e){}
        mainWindow.setVisible(true);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

