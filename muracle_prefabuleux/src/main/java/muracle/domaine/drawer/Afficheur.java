package muracle.domaine.drawer;

import muracle.domaine.MuracleController;
import muracle.utilitaire.FractionError;

import java.awt.*;
import java.awt.geom.Line2D;

public class Afficheur {

    protected final MuracleController controller;
    protected Dimension initialDimension;
    protected final Color lineColor;
    protected final Color fillColor;
    private final Color grilleColor;

    //private final Color errorColor;

    public Afficheur(MuracleController controller, Dimension initDim) {
        this.controller = controller;
        initialDimension = initDim;
        lineColor = Color.black;
        fillColor = Color.white;
        grilleColor = new Color(150, 173, 233);
        //errorColor = Color.red;
    }

    public void draw(Graphics g) throws FractionError {
        g.setColor(lineColor);
        if (controller.isGrilleShown()) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            drawGrille(g2d);
        }
    }

    private void drawGrille(Graphics2D g) throws FractionError {
        g.setColor(grilleColor);
        double posX = initialDimension.width % controller.getDistLigneGrille().toDouble() / 2;
        double posY = initialDimension.height % controller.getDistLigneGrille().toDouble() / 2;
        while (posX < initialDimension.width) {
            g.draw(new Line2D.Double(posX, 0, posX, initialDimension.height));
            posX += controller.getDistLigneGrille().toDouble();
        }
        while (posY < initialDimension.height) {
            g.draw(new Line2D.Double(0, posY, initialDimension.width, posY));
            posY += controller.getDistLigneGrille().toDouble();
        }
        g.setColor(lineColor);
    }

    /*protected void drawErrorMessage(Graphics2D g) {
        g.setFont(new Font("TimesRoman", Font.ITALIC, 18));
        g.setStroke(new BasicStroke(5));
        FontMetrics fm = g.getFontMetrics();
        int wMessage = fm.charsWidth(controller.getErrorMessage().toCharArray(), 0, controller.getErrorMessage().length());
        System.out.println(wMessage);
        g.drawString(controller.getErrorMessage(), (initialDimension.width + wMessage) / 2, initialDimension.height - 32);
        g.setStroke(new BasicStroke(2));
        g.setColor(errorColor);
        g.drawString(controller.getErrorMessage(), (initialDimension.width + wMessage) / 2, initialDimension.height - 32);
        controller.ackErrorMessage();
    }*/
}
