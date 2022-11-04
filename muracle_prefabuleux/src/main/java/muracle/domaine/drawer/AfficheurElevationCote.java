package muracle.domaine.drawer;

import muracle.domaine.Accessoire;
import muracle.domaine.Cote;
import muracle.domaine.Mur;
import muracle.domaine.MuracleController;
import muracle.utilitaire.FractionError;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class AfficheurElevationCote extends Afficheur {

    private double posX;
    private double posY;
    private double w;
    private double h;

    public AfficheurElevationCote (MuracleController controller, Dimension initDim) {
        super(controller, initDim);
    }

    public void draw(Graphics g) throws FractionError {
        super.draw(g);

        Cote cote = controller.getSelectedCote();
        posX = (initialDimension.width - cote.getLargeur().toDouble()) / 2;
        posY = (initialDimension.getHeight() - cote.getHauteur().toDouble()) / 2;
        w = cote.getLargeur().toDouble();
        h = cote.getHauteur().toDouble();

        Graphics2D g2d = (Graphics2D) g;
        drawCote(g2d);
        drawSeparateur(g2d);
        drawAccessoire(g2d);
    }

    private void drawCote(Graphics2D g) {
        g.draw(new Rectangle2D.Double(posX, posY, w, h));
    }

    private void drawSeparateur(Graphics2D g) throws FractionError {
        Cote cote = controller.getSelectedCote();
        for (int i = 0; i < cote.getSeparateurs().size(); i++) {
            g.draw(new Line2D.Double(cote.getSeparateur(i).toDouble(), posY,
                    cote.getSeparateur(i).toDouble(), posY + h));
        }
    }

    private void drawAccessoire(Graphics2D g) throws FractionError {
        /*Cote cote = controller.getSelectedCote();
        for (int i = 0; i < cote.getMurs().size(); i++) {
            posX = cote.getSeparateur(i).toDouble();
            Mur mur = cote.getMurs().get(i);
            for (int j = 0; j < mur.getAccessoires().length; j++) {
                Accessoire accessoire = mur.getAccessoire(j);
                g.draw(new Rectangle2D.Double(posX + Accessoire));
            }
        }*/
    }
}
