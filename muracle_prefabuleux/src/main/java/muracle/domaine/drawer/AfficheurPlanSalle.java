package muracle.domaine.drawer;

import muracle.domaine.Accessoire;
import muracle.domaine.CoteDTO;
import muracle.domaine.MuracleController;
import muracle.domaine.SalleDTO;
import muracle.utilitaire.CoordPouce;
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

    public void draw(Graphics g, double zoom, Dimension dim, CoordPouce posiCam, CoordPouce dimPlan) throws FractionError {
        super.draw(g,zoom,dim,posiCam,dimPlan);

        SalleDTO salle = controller.getSalleReadOnly();
        posX = (initialDimension.width - salle.largeur.toDouble()) / 2;
        posY = (initialDimension.getHeight() - salle.longueur.toDouble()) / 2;
        w = salle.largeur.toDouble();
        h = salle.longueur.toDouble();
        ep = salle.profondeur.toDouble();

        Graphics2D g2d = (Graphics2D) g;

        g2d.setStroke(new BasicStroke(2));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        ajustement(g2d,zoom, dim, posiCam, dimPlan);

        drawSalle(g2d);
        drawSeparateur(g2d);
        drawTrouRetourAir(g2d);

        reset(g2d,zoom, dim, posiCam, dimPlan);
        drawVue(g);
        drawErrorMessage(g2d);
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
        g.draw(new Line2D.Double(posX, posY, posX - ep + 1, posY - ep + 1));
        g.draw(new Line2D.Double(posX + w, posY, posX + w - 1 + ep , posY - ep + 1));
        g.draw(new Line2D.Double(posX, posY + h, posX - ep + 1, posY + h + ep - 1));
        g.draw(new Line2D.Double(posX + w, posY + h, posX + w + ep - 1, posY + h + ep - 1));
    }

    private void drawSeparateur(Graphics2D g) throws FractionError {
        CoteDTO cote;
        // south
        cote = controller.getSalleReadOnly().getCote('S');
        Line2D.Double ligne;
        for (int i = 0; i < cote.separateurs.size(); i++) {
            ligne = new Line2D.Double(posX + cote.separateurs.get(i).toDouble(), posY + h,
                    posX + cote.separateurs.get(i).toDouble(), posY + h + ep);
            if (controller.getSelectedSeparateur() == cote.separateurs.get(i)) {
                g.setColor(selectColor);
                g.setStroke(new BasicStroke(4));
                g.draw(new Line2D.Double(ligne.x1, ligne.y1 + 3, ligne.x2, ligne.y2 - 3));
                g.setStroke(new BasicStroke(2));
                g.setColor(lineColor);
            }
            g.draw(ligne);
        }

        // north
        cote = controller.getSalleReadOnly().getCote('N');
        for (int i = 0; i < cote.separateurs.size(); i++) {
            ligne = new Line2D.Double(posX + w -cote.separateurs.get(i).toDouble(), posY,
                    posX + w - cote.separateurs.get(i).toDouble(), posY - ep);
            if (controller.getSelectedSeparateur() == cote.separateurs.get(i)) {
                g.setColor(selectColor);
                g.setStroke(new BasicStroke(4));
                g.draw(new Line2D.Double(ligne.x1, ligne.y1 - 3, ligne.x2, ligne.y2 + 3));
                g.setStroke(new BasicStroke(2));
                g.setColor(lineColor);
            }
            g.draw(ligne);
        }

        // east
        cote = controller.getSalleReadOnly().getCote('E');
        for (int i = 0; i < cote.separateurs.size(); i++) {
            ligne = new Line2D.Double(posX + w, posY + h - cote.separateurs.get(i).toDouble(),
                    posX + w + ep, posY + h - cote.separateurs.get(i).toDouble());
            if (controller.getSelectedSeparateur() == cote.separateurs.get(i)) {
                g.setColor(selectColor);
                g.setStroke(new BasicStroke(4));
                g.draw(new Line2D.Double(ligne.x1 + 3, ligne.y1, ligne.x2 - 3, ligne.y2));
                g.setStroke(new BasicStroke(2));
                g.setColor(lineColor);
            }
            g.draw(ligne);
        }

        // west
        cote = controller.getSalleReadOnly().getCote('W');
        for (int i = 0; i < cote.separateurs.size(); i++) {
            ligne = new Line2D.Double(posX, posY + cote.separateurs.get(i).toDouble(),
                    posX - ep, posY + cote.separateurs.get(i).toDouble());
            if (controller.getSelectedSeparateur() == cote.separateurs.get(i)) {
                g.setColor(selectColor);
                g.setStroke(new BasicStroke(4));
                g.draw(new Line2D.Double(ligne.x1 - 3, ligne.y1, ligne.x2 + 3, ligne.y2));
                g.setStroke(new BasicStroke(2));
                g.setColor(lineColor);
            }
            g.draw(ligne);
        }
    }

    private void drawTrouRetourAir(Graphics2D g) throws FractionError {
        CoteDTO cote;

        // south
        cote = controller.getSalleReadOnly().getCote('S');
        for (Accessoire acces : cote.accessoires) {
            if (acces.getType().equals("Retour d'air")) {
                double retourPosX = acces.getPosition().getX().toDouble();
                double retourPosY = h + (ep - controller.getSalleReadOnly().epaisseurTrouRetourAir.toDouble()) / 2;
                Rectangle2D.Double rect = new Rectangle2D.Double(posX + retourPosX, posY + retourPosY,
                        acces.getLargeur().toDouble(), controller.getSalleReadOnly().epaisseurTrouRetourAir.toDouble());
                g.setColor(fillColor.darker().darker());
                g.fill(new Area(rect));
                g.setColor(lineColor);
                g.draw(rect);
            }
        }

        // north
        cote = controller.getSalleReadOnly().getCote('N');
        g.setColor(fillColor.darker().darker());
        for (Accessoire acces : cote.accessoires) {
            if (acces.getType().equals("Retour d'air")) {
                double retourPosX = w - acces.getPosition().getX().toDouble() - acces.getLargeur().toDouble();
                double retourPosY = (ep - controller.getSalleReadOnly().epaisseurTrouRetourAir.toDouble()) / 2;
                Rectangle2D.Double rect = new Rectangle2D.Double(posX + retourPosX, posY - ep + retourPosY,
                        acces.getLargeur().toDouble(), controller.getSalleReadOnly().epaisseurTrouRetourAir.toDouble());
                g.setColor(fillColor.darker().darker());
                g.fill(new Area(rect));
                g.setColor(lineColor);
                g.draw(rect);
            }
        }

        // east
        cote = controller.getSalleReadOnly().getCote('E');
        for (Accessoire acces : cote.accessoires) {
            if (acces.getType().equals("Retour d'air")) {
                double retourPosX = w + (ep - controller.getSalleReadOnly().epaisseurTrouRetourAir.toDouble()) / 2;
                double retourPosY = h - acces.getPosition().getX().toDouble() - acces.getLargeur().toDouble();
                Rectangle2D.Double rect = new Rectangle2D.Double(posX + retourPosX, posY + retourPosY,
                        controller.getSalleReadOnly().epaisseurTrouRetourAir.toDouble(), acces.getLargeur().toDouble());
                g.setColor(fillColor.darker().darker());
                g.fill(new Area(rect));
                g.setColor(lineColor);
                g.draw(rect);
            }
        }

        // west
        cote = controller.getSalleReadOnly().getCote('W');
        for (Accessoire acces : cote.accessoires) {
            if (acces.getType().equals("Retour d'air")) {
                double retourPosX = (ep - controller.getSalleReadOnly().epaisseurTrouRetourAir.toDouble()) / 2;
                double retourPosY = acces.getPosition().getX().toDouble();
                Rectangle2D.Double rect = new Rectangle2D.Double(posX - ep + retourPosX, posY + retourPosY,
                        controller.getSalleReadOnly().epaisseurTrouRetourAir.toDouble(), acces.getLargeur().toDouble());
                g.setColor(fillColor.darker().darker());
                g.fill(new Area(rect));
                g.setColor(lineColor);
                g.draw(rect);
            }
        }
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
