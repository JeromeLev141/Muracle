package muracle.domaine.drawer;

import muracle.domaine.Cote;
import muracle.domaine.MuracleController;
import muracle.domaine.Salle;
import muracle.utilitaire.FractionError;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

public class AfficheurPlanSalle extends Afficheur{

    private double posX;
    private double posY;
    private double w;
    private double h;
    private double ep;

    public AfficheurPlanSalle(MuracleController controller, Dimension initDim) {
        super(controller, initDim);
    }

    public void draw(Graphics g) throws FractionError {
        super.draw(g);

        Salle salle = controller.getSalle();
        posX = (initialDimension.width - salle.getLargeur().toDouble()) / 2;
        posY = (initialDimension.getHeight() - salle.getLongueur().toDouble()) / 2;
        w = salle.getLargeur().toDouble();
        h = salle.getLongueur().toDouble();
        ep = salle.getProfondeur().toDouble();

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawSalle(g2d);
        drawSeparateur(g2d);
        drawTrouRetourAir(g2d);
        drawVue(g);
    }

    private void drawSalle(Graphics2D g) {
        Rectangle2D.Double rectInt = new Rectangle2D.Double(posX, posY, w, h);
        Rectangle2D.Double rectExt = new Rectangle2D.Double(posX - ep, posY - ep, w + 2 * ep, h + 2 * ep);
        Area exterieur = new Area(rectExt);
        Area interieur = new Area(rectInt);
        exterieur.subtract(interieur);
        g.setColor(fillColor);
        g.fill(exterieur);
        g.setColor(lineColor);
        g.draw(rectInt);
        g.draw(rectExt);
        g.draw(new Line2D.Double(posX, posY, posX - ep, posY - ep));
        g.draw(new Line2D.Double(posX + w, posY, posX + w + ep , posY - ep));
        g.draw(new Line2D.Double(posX, posY + h, posX - ep, posY + h + ep));
        g.draw(new Line2D.Double(posX + w, posY + h, posX + w + ep, posY + h + ep));
    }

    private void drawSeparateur(Graphics2D g) throws FractionError {
        Cote cote;
        // south
        cote = controller.getSalle().getCote('S');
        for (int i = 0; i < cote.getSeparateurs().size(); i++) {
            g.draw(new Line2D.Double(posX + cote.getSeparateur(i).toDouble(), posY + h,
                    posX + cote.getSeparateur(i).toDouble(), posY + h + ep));
        }

        // north
        cote = controller.getSalle().getCote('N');
        for (int i = 0; i < cote.getSeparateurs().size(); i++) {
            g.draw(new Line2D.Double(posX + w -cote.getSeparateur(i).toDouble(), posY,
                    posX + w - cote.getSeparateur(i).toDouble(), posY - ep));
        }

        // east
        cote = controller.getSalle().getCote('E');
        for (int i = 0; i < cote.getSeparateurs().size(); i++) {
            g.draw(new Line2D.Double(posX + w, posY + h - cote.getSeparateur(i).toDouble(),
                    posX + w + ep, posY + h - cote.getSeparateur(i).toDouble()));
        }

        // west
        cote = controller.getSalle().getCote('E');
        for (int i = 0; i < cote.getSeparateurs().size(); i++) {
            g.draw(new Line2D.Double(posX, posY + cote.getSeparateur(i).toDouble(),
                    posX - ep, posY + cote.getSeparateur(i).toDouble()));
        }
    }

    private void drawTrouRetourAir(Graphics2D g) throws FractionError {
        /*Cote cote;
        // south
        cote = controller.getSalle().getCote('S');
        for (int i = 0; i < cote.getMurs().size(); i++) {
            Mur mur = cote.getMurs().get(i);
            for (int j = 0; j < mur.getAccessoires().length; j++) {
                if (mur.getAccessoire(i).)
                    g.draw(new Line2D.Double());
            }
        }

        // north
        cote = controller.getSalle().getCote('N');
        for (int i = 0; i < cote.getSeparateurs().size(); i++) {
            g.draw(new Line2D.Double(posX + w -cote.getSeparateur(i).toDouble(), posY,
                    posX + w - cote.getSeparateur(i).toDouble(), posY - ep));
        }

        // east
        cote = controller.getSalle().getCote('E');
        for (int i = 0; i < cote.getSeparateurs().size(); i++) {
            g.draw(new Line2D.Double(posX + w, posY + h - cote.getSeparateur(i).toDouble(),
                    posX + w + ep, posY + h - cote.getSeparateur(i).toDouble()));
        }

        // west
        cote = controller.getSalle().getCote('E');
        for (int i = 0; i < cote.getSeparateurs().size(); i++) {
            g.draw(new Line2D.Double(posX, posY + cote.getSeparateur(i).toDouble(),
                    posX - ep, posY + cote.getSeparateur(i).toDouble()));
        }*/
    }

    private void drawVue(Graphics g) {
        try {
            Image image = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/vues/vueDessus.png")));
            image = image.getScaledInstance( 48, 48,  Image.SCALE_SMOOTH ) ;
            g.drawImage(image, initialDimension.width - 64, 16, null);
        } catch (Exception except) {
            except.printStackTrace();
        }
    }
}
