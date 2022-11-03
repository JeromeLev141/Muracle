package muracle.domaine.drawer;

import muracle.domaine.MuracleController;

import java.awt.*;

public class Afficheur {

    private final MuracleController controller;
    private Dimension initialDimension;

    public Afficheur(MuracleController controller, Dimension initDim) {
        this.controller = controller;
        initialDimension = initDim;
    }

    public void draw(Graphics g) {
        drawGrille(g);
    }

    private static void drawGrille(Graphics g) {
        //do something
    }
}
