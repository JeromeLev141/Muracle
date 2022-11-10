package muracle.domaine.drawer;

import muracle.domaine.MuracleController;
import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.Fraction;
import muracle.utilitaire.FractionError;
import muracle.utilitaire.Pouce;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

public class Afficheur {

    protected final MuracleController controller;
    protected Dimension initialDimension;
    protected final Color lineColor;
    protected final Color fillColor;
    private final Color grilleColor;

    public Afficheur(MuracleController controller, Dimension initDim) {
        this.controller = controller;
        initialDimension = initDim;
        lineColor = Color.black;
        fillColor = Color.white;
        grilleColor = new Color(150, 173, 233);
    }

    public void draw(Graphics g, double zoom, Dimension dim, CoordPouce posiCam, CoordPouce dimPlan) throws FractionError {
        g.setColor(lineColor);
        if (controller.isGrilleShown()) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            drawGrille(g2d);
        }
    }

    protected void ajustement(Graphics2D g2d,double zoom,Dimension dim, CoordPouce posiCam, CoordPouce dimPlan){
        AffineTransform at = new AffineTransform();

        try{
            at.translate((-1*zoom * posiCam.getX().sub(dimPlan.getX().div(2)).toDouble()) + (-1*(zoom-1)*dim.getWidth()/2),
                    (-1*zoom * posiCam.getY().sub(dimPlan.getY().div(2)).toDouble()) + (-1*(zoom-1)*dim.getHeight()/2));
            //System.out.println(zoom*posiCam.getX().sub(dimPlan.getX().div(2)).toDouble());
        }catch (FractionError ignored){}

        at.scale(zoom,zoom);
        g2d.transform(at);
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
}
