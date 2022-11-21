package muracle.domaine.drawer;

import muracle.domaine.MuracleController;
import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.FractionError;
import muracle.utilitaire.PouceError;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class Afficheur {

    protected final MuracleController controller;
    protected Dimension initialDimension;
    protected final Color lineColor;
    protected final Color fillColor;
    private final Color grilleColor;
    protected final Color selectColor;
    protected final Color errorColor;
    protected final Color backErrorColor;

    public Afficheur(MuracleController controller, Dimension initDim) {
        this.controller = controller;
        initialDimension = initDim;
        lineColor = Color.black;
        fillColor = Color.white;
        grilleColor = new Color(150, 173, 233);
        selectColor = new Color(97, 255, 89);
        errorColor = new Color(233, 103, 104);
        backErrorColor = new Color(46, 52, 64);
    }

    public void draw(Graphics g, double zoom, Dimension dim, CoordPouce posiCam, CoordPouce dimPlan) throws FractionError {
        g.setColor(lineColor);
        if (controller.isGrilleShown()) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            ajustement(g2d, zoom, dim, posiCam, dimPlan);
            drawGrille(g2d, zoom);
            reset(g2d, zoom, dim, posiCam, dimPlan);
        }
    }


    private void drawGrille(Graphics2D g, double zoom) {
        g.setColor(grilleColor);
        double decalX = (int) (2 * initialDimension.width / controller.getDistLigneGrille().toDouble())
                * controller.getDistLigneGrille().toDouble();
        double decalY = (int) (2 * initialDimension.height / controller.getDistLigneGrille().toDouble())
                * controller.getDistLigneGrille().toDouble();
        double posX = initialDimension.width % controller.getDistLigneGrille().toDouble() / 2 - decalX;
        double posY = initialDimension.height % controller.getDistLigneGrille().toDouble() / 2 - decalY;
        while (posX < 3 * initialDimension.width) {
            g.draw(new Line2D.Double(posX, -2 * initialDimension.height, posX, 3 * initialDimension.height));
            posX += controller.getDistLigneGrille().toDouble();
        }
        while (posY < 3 * initialDimension.height) {
            g.draw(new Line2D.Double(-2 * initialDimension.width, posY, 3 * initialDimension.width, posY));
            posY += controller.getDistLigneGrille().toDouble();
        }
        g.setColor(lineColor);
    }

    protected void drawErrorMessage(Graphics2D g2d) {
        if (!controller.getErrorMessage().equals("")) {
            g2d.setFont(new Font("TimesRoman", Font.ITALIC, 18));
            int textWidth = g2d.getFontMetrics().stringWidth(controller.getErrorMessage());
            int xPos = (initialDimension.width / 2) - (textWidth / 2);
            int yPos = initialDimension.height - 40;
            Rectangle2D.Double rect = new Rectangle2D.Double(xPos - 4, yPos - 18, textWidth + 8, 22);
            g2d.setColor(backErrorColor);
            g2d.fill(rect);
            g2d.setColor(lineColor);
            g2d.draw(rect);
            g2d.setColor(errorColor);
            g2d.drawString(controller.getErrorMessage(), xPos, yPos);
            controller.ackErrorMessage();
        }
    }


    protected void ajustement(Graphics2D g2d,double zoom,Dimension dim, CoordPouce posiCam, CoordPouce dimPlan){
        AffineTransform at = new AffineTransform();

        try{
            at.translate((-1*zoom * posiCam.getX().sub(dimPlan.getX().div(2)).toDouble()) + (-1*(zoom-1)*dim.getWidth()/2),
                    (-1*zoom * posiCam.getY().sub(dimPlan.getY().div(2)).toDouble()) + (-1*(zoom-1)*dim.getHeight()/2));
        }catch (PouceError ignored){}

        at.scale(zoom,zoom);
        g2d.transform(at);
    }

    protected void reset(Graphics2D g2d, double zoom,Dimension dim, CoordPouce posiCam, CoordPouce dimPlan){
        AffineTransform at = new AffineTransform();
        at.scale(1/zoom,1/zoom);
        try {
            at.translate((1 * zoom * posiCam.getX().sub(dimPlan.getX().div(2)).toDouble()) + (1 * (zoom - 1) * dim.getWidth() / 2),
                    (1 * zoom * posiCam.getY().sub(dimPlan.getY().div(2)).toDouble()) + (1 * (zoom - 1) * dim.getHeight() / 2));
        }catch (PouceError ignored){}
        g2d.transform(at);
    }
}
