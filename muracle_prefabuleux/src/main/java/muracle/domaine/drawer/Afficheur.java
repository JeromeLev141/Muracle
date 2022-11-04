package muracle.domaine.drawer;

import muracle.domaine.MuracleController;
import muracle.utilitaire.FractionError;

import java.awt.*;
import java.awt.geom.Line2D;

public class Afficheur {

    protected final MuracleController controller;
    protected Dimension initialDimension;
    protected final Color mainColor;
    private final Color lnColor;

    public Afficheur(MuracleController controller, Dimension initDim) {
        this.controller = controller;
        initialDimension = initDim;
        mainColor = Color.BLACK;
        lnColor = new Color(150, 173, 233);
    }

    public void draw(Graphics g) throws FractionError {
        g.setColor(mainColor);
        Graphics2D g2d = (Graphics2D) g;
        drawGrille(g2d);
    }

    private void drawGrille(Graphics2D g) throws FractionError {
        g.setColor(lnColor);
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
        g.setColor(mainColor);
    }
}
