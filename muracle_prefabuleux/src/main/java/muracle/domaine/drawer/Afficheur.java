package muracle.domaine.drawer;

import muracle.domaine.MuracleController;
import muracle.utilitaire.FractionError;

import java.awt.*;
import java.awt.geom.Line2D;

public class Afficheur {

    protected final MuracleController controller;
    protected Dimension initialDimension;

    public Afficheur(MuracleController controller, Dimension initDim) {
        this.controller = controller;
        initialDimension = initDim;
    }

    public void draw(Graphics g) throws FractionError {
        Graphics2D g2d = (Graphics2D) g;
        drawGrille(g2d);
    }

    private void drawGrille(Graphics2D g) throws FractionError {
        double posX, posY;
        posX = posY = controller.getDistLigneGrille().toDouble();
        while (posX < initialDimension.width) {
            g.draw(new Line2D.Double(posX, 0, posX, initialDimension.height));
            posX += controller.getDistLigneGrille().toDouble();
        }
        while (posY < initialDimension.height) {
            g.draw(new Line2D.Double(0, posY, initialDimension.width, posY));
            posY += controller.getDistLigneGrille().toDouble();
        }
    }
}
