package muracle;


import com.formdev.flatlaf.intellijthemes.FlatNordIJTheme;
import muracle.domaine.CoteError;
import muracle.utilitaire.FractionError;
import muracle.utilitaire.PouceError;
import muracle.vue.MainWindow;

import javax.swing.*;


public class App {

    public static void main(String[] args) throws FractionError, PouceError, CoteError {
        MainWindow mainWindow = new MainWindow();
        try {
            UIManager.setLookAndFeel(new FlatNordIJTheme());
            SwingUtilities.updateComponentTreeUI(mainWindow);
        }catch (Exception ignored){}
        mainWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainWindow.setVisible(true);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

