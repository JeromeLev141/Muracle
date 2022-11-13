package muracle.domaine.drawer;

import muracle.domaine.Accessoire;
import muracle.domaine.CoteDTO;
import muracle.domaine.MuracleController;
import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.FractionError;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

public class AfficheurElevationCote extends Afficheur {

    private double posX;
    private double posY;
    private double w;
    private double h;

    public AfficheurElevationCote (MuracleController controller, Dimension initDim) {
        super(controller, initDim);
    }

    public void draw(Graphics g, double zoom,Dimension dim, CoordPouce posiCam, CoordPouce dimPlan) throws FractionError {
        super.draw(g,zoom,dim,posiCam,dimPlan);

        CoteDTO cote = controller.getSelectedCoteReadOnly();
        posX = (initialDimension.width - cote.largeur.toDouble()) / 2;
        posY = (initialDimension.getHeight() - cote.hauteur.toDouble()) / 2;
        w = cote.largeur.toDouble();
        h = cote.hauteur.toDouble();

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));


        ajustement(g2d,zoom, dim, posiCam,dimPlan);
        drawCote(g2d);
        drawSeparateur(g2d);
        reset(g2d,zoom, dim, posiCam, dimPlan);

        drawVue(g);
    }

    private void drawCote(Graphics2D g) throws FractionError {
        Rectangle2D.Double rect = new Rectangle2D.Double(posX, posY, w, h);
        Area coteArea = new Area(rect);
        drawAccessoire(g, coteArea);
        g.draw(rect);
    }

    private void drawSeparateur(Graphics2D g) throws FractionError {
        CoteDTO cote = controller.getSelectedCoteReadOnly();
        for (int i = 0; i < cote.separateurs.size(); i++) {
            Line2D.Double ligne;
            if (controller.isVueExterieur()) {
                ligne = new Line2D.Double(posX + cote.separateurs.get(i).toDouble(), posY,
                        posX + cote.separateurs.get(i).toDouble(), posY + h);
            }
            else {
                ligne = new Line2D.Double(posX + w - cote.separateurs.get(i).toDouble(), posY,
                        posX + w - cote.separateurs.get(i).toDouble(), posY + h);
            }
            if (controller.getSelectedSeparateur() == cote.separateurs.get(i)) {
                g.setColor(selectColor);
                g.setStroke(new BasicStroke(4));
                g.draw(new Line2D.Double(ligne.x1, ligne.y1 + 3, ligne.x2, ligne.y2 - 3));
                g.setStroke(new BasicStroke(2));
                g.setColor(lineColor);
            }
            g.draw(ligne);
        }
    }

    private void drawAccessoire(Graphics2D g, Area coteArea) throws FractionError {
        CoteDTO cote = controller.getSelectedCoteReadOnly();
        Rectangle2D.Double[] rectangles = new Rectangle2D.Double[cote.accessoires.size()];
        int i = 0;
        g.setColor(fillColor.darker());
        for (Accessoire acces : cote.accessoires) {
            Rectangle2D.Double rect = null;
            double accesPosX = acces.getPosition().getX().toDouble();
            double accesPosY = acces.getPosition().getY().toDouble();
            if (controller.isVueExterieur() && !acces.isInterieurOnly())
                rect = new Rectangle2D.Double(posX + accesPosX, posY + accesPosY,
                        acces.getLargeur().toDouble(), acces.getHauteur().toDouble());
            else if (!controller.isVueExterieur())
                rect = new Rectangle2D.Double(posX + w - (accesPosX + acces.getLargeur().toDouble()), posY + accesPosY,
                        acces.getLargeur().toDouble(), acces.getHauteur().toDouble());
            if (rect != null) {
                coteArea.subtract(new Area(rect));
                if (acces.isInterieurOnly())
                    g.fill(new Area(rect));
                rectangles[i] = rect;
            }
            i++;
        }
        g.setColor(fillColor);
        g.fill(coteArea);
        g.setColor(lineColor);
        for (Rectangle2D.Double rect : rectangles)
            g.draw(rect);
    }

    private void drawVue(Graphics g) {
        try {
            Image image;
            String path = "/images/vues/vue" + controller.getSelectedCoteReadOnly().orientation;
            if (controller.isVueExterieur())
                path += "Ext.png";
            else path += "Int.png";
            image = ImageIO.read(Objects.requireNonNull(getClass().getResource(path)));
            image = image.getScaledInstance( 48, 48,  Image.SCALE_SMOOTH ) ;
            g.drawImage(image, initialDimension.width - 64, 16, null);
        } catch (Exception except) {
            except.printStackTrace();
        }
    }
}
