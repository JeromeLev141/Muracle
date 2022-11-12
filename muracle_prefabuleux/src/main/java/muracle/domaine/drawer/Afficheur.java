package muracle.domaine.drawer;

import muracle.domaine.MuracleController;
import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.FractionError;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

public class Afficheur {

    protected final MuracleController controller;
    protected Dimension initialDimension;
    protected final Color lineColor;
    protected final Color fillColor;
    private final Color grilleColor;

    protected final Color selectColor;
    protected final Color errorColor;

    public Afficheur(MuracleController controller, Dimension initDim) {
        this.controller = controller;
        initialDimension = initDim;
        lineColor = Color.black;
        fillColor = Color.white;
        grilleColor = new Color(150, 173, 233);
        selectColor = new Color(97, 255, 89);
        errorColor = Color.red;
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

    protected void ajustement(Graphics2D g2d,double zoom,Dimension dim, CoordPouce posiCam, CoordPouce dimPlan){
        AffineTransform at = new AffineTransform();

        try{
            at.translate((-1*zoom * posiCam.getX().sub(dimPlan.getX().div(2)).toDouble()) + (-1*(zoom-1)*dim.getWidth()/2),
                    (-1*zoom * posiCam.getY().sub(dimPlan.getY().div(2)).toDouble()) + (-1*(zoom-1)*dim.getHeight()/2));
        }catch (FractionError ignored){}

        at.scale(zoom,zoom);
        g2d.transform(at);
    }

    protected void reset(Graphics2D g2d, double zoom,Dimension dim, CoordPouce posiCam, CoordPouce dimPlan){
        AffineTransform at = new AffineTransform();
        at.scale(1/zoom,1/zoom);
        try {
            at.translate((1 * zoom * posiCam.getX().sub(dimPlan.getX().div(2)).toDouble()) + (1 * (zoom - 1) * dim.getWidth() / 2),
                    (1 * zoom * posiCam.getY().sub(dimPlan.getY().div(2)).toDouble()) + (1 * (zoom - 1) * dim.getHeight() / 2));
        }catch (FractionError ignored){}
        g2d.transform(at);
    }

    private void drawGrille(Graphics2D g, double zoom) throws FractionError {
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
