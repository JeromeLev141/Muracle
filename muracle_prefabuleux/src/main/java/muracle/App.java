package muracle;


import com.formdev.flatlaf.intellijthemes.FlatNordIJTheme;
import muracle.utilitaire.FractionError;
import muracle.vue.MainWindow;

import javax.swing.*;


public class App {

    public static void main(String[] args) throws FractionError {

        MainWindow mainWindow = new MainWindow();
        try {
            UIManager.setLookAndFeel(new FlatNordIJTheme());
            SwingUtilities.updateComponentTreeUI(mainWindow);
        }catch (Exception e){}
        mainWindow.setVisible(true);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

