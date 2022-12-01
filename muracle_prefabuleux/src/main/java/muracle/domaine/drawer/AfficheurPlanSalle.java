package muracle.domaine.drawer;

import muracle.domaine.*;
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

        g2d.setStroke(ligneStroke);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        ajustement(g2d,zoom, dim, posiCam, dimPlan);
        if (controller.isGrilleShown())
            drawGrille(g2d, posX - ep, posY- ep, zoom, posiCam, dimPlan);
        drawSalle(g2d);
        drawTrouRetourAir(g2d);
        drawMursCote(g2d);
        drawSeparateur(g2d);
        reset(g2d,zoom, dim, posiCam, dimPlan);

        drawVue(g);
        drawErrorMessage(g2d);
    }

    private void drawSalle(Graphics2D g2d) {
        Rectangle2D.Double rectInt = new Rectangle2D.Double(posX, posY, w, h);
        Rectangle2D.Double rectExt = new Rectangle2D.Double(posX - ep, posY - ep, w + 2 * ep, h + 2 * ep);
        Area exterieur = new Area(rectExt);
        Area interieur = new Area(rectInt);
        exterieur.subtract(interieur);
        g2d.setColor(fillColor);
        g2d.fill(exterieur);
        g2d.setColor(lineColor);
        g2d.draw(rectInt);
        g2d.draw(rectExt);
        g2d.draw(new Line2D.Double(posX, posY, posX - ep + 0.5 * ligneStroke.getLineWidth(), posY - ep + 0.5 * ligneStroke.getLineWidth()));
        g2d.draw(new Line2D.Double(posX + w, posY, posX + w - 0.5 * ligneStroke.getLineWidth() + ep , posY - ep + 0.5 * ligneStroke.getLineWidth()));
        g2d.draw(new Line2D.Double(posX, posY + h, posX - ep + 0.5 * ligneStroke.getLineWidth(), posY + h + ep - 0.5 * ligneStroke.getLineWidth()));
        g2d.draw(new Line2D.Double(posX + w, posY + h, posX + w + ep - 0.5 * ligneStroke.getLineWidth(), posY + h + ep - 0.5 * ligneStroke.getLineWidth()));
    }

    private void drawSeparateur(Graphics2D g) {
        CoteDTO cote;
        // south
        cote = controller.getSalleReadOnly().getCote('S');
        Line2D.Double ligne;
        for (int i = 0; i < cote.separateurs.size(); i++) {
            ligne = new Line2D.Double(posX + cote.separateurs.get(i).toDouble(), posY + h,
                    posX + cote.separateurs.get(i).toDouble(), posY + h + ep);
            if (controller.getSelectedSeparateur() == cote.separateurs.get(i)) {
                g.setColor(selectColor);
                g.setStroke(selectedStroke);
                g.draw(new Line2D.Double(ligne.x1, ligne.y1 + 1.5 * ligneStroke.getLineWidth(), ligne.x2, ligne.y2 - 1.5 * ligneStroke.getLineWidth()));
                g.setStroke(ligneStroke);
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
                g.setStroke(selectedStroke);
                g.draw(new Line2D.Double(ligne.x1, ligne.y1 - 1.5 * ligneStroke.getLineWidth(), ligne.x2, ligne.y2 + 1.5 * ligneStroke.getLineWidth()));
                g.setStroke(ligneStroke);
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
                g.setStroke(selectedStroke);
                g.draw(new Line2D.Double(ligne.x1 + 1.5 * ligneStroke.getLineWidth(), ligne.y1, ligne.x2 - 1.5 * ligneStroke.getLineWidth(), ligne.y2));
                g.setStroke(ligneStroke);
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
                g.setStroke(selectedStroke);
                g.draw(new Line2D.Double(ligne.x1 - 1.5 * ligneStroke.getLineWidth(), ligne.y1, ligne.x2 + 1.5 * ligneStroke.getLineWidth(), ligne.y2));
                g.setStroke(ligneStroke);
                g.setColor(lineColor);
            }
            g.draw(ligne);
        }
    }


    private void drawMursCote(Graphics2D g2d) {
        CoteDTO cote;
        // south
        cote = controller.getCoteReadOnly('S');
        int indexMur = 0;
        for (Mur mur : cote.murs) {
            Rectangle2D.Double rect = null;
            if (!(mur.getPanneauExt().isPoidsValid() && mur.getPanneauInt().isPoidsValid())) {
                double murPosX;
                murPosX = 0;
                if (!mur.GetEstCoinGauche())
                    murPosX = cote.separateurs.get(indexMur - 1).toDouble();
                rect = new Rectangle2D.Double(posX + murPosX, posY + h, mur.getLargeur().toDouble(), ep);
                if (controller.getErrorMessage().equals(""))
                    controller.setErrorMessage("Les panneaux du mur " + (indexMur + 1) + " du côté " + cote.orientation + " excèdent le poids maximum");
            }
            if (rect != null) {
                g2d.setColor(errorColor);
                Composite compoInit = g2d.getComposite();
                AlphaComposite alcom = AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, 0.5f);
                g2d.setComposite(alcom);
                g2d.fill(rect);
                g2d.setComposite(compoInit);
                g2d.setColor(backErrorColor);
                g2d.draw(rect);
                g2d.setColor(lineColor);
            }
            indexMur++;
        }

        // north
        cote = controller.getCoteReadOnly('N');
        indexMur = 0;
        for (Mur mur : cote.murs) {
            Rectangle2D.Double rect = null;
            if (!(mur.getPanneauExt().isPoidsValid() && mur.getPanneauInt().isPoidsValid())) {
                double murPosX;
                murPosX = cote.largeur.toDouble() - mur.getLargeur().toDouble();
                if (!mur.GetEstCoinGauche())
                    murPosX = cote.largeur.toDouble() - cote.separateurs.get(indexMur - 1).toDouble() - mur.getLargeur().toDouble();
                rect = new Rectangle2D.Double(posX + murPosX, posY - ep, mur.getLargeur().toDouble(), ep);
                if (controller.getErrorMessage().equals(""))
                    controller.setErrorMessage("Les panneaux du mur " + (indexMur + 1) + " du côté " + cote.orientation + " excèdent le poids maximum");
            }
            if (rect != null) {
                g2d.setColor(errorColor);
                Composite compoInit = g2d.getComposite();
                AlphaComposite alcom = AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, 0.5f);
                g2d.setComposite(alcom);
                g2d.fill(rect);
                g2d.setComposite(compoInit);
                g2d.setColor(backErrorColor);
                g2d.draw(rect);
                g2d.setColor(lineColor);
            }
            indexMur++;
        }

        // south
        cote = controller.getCoteReadOnly('E');
        indexMur = 0;
        for (Mur mur : cote.murs) {
            Rectangle2D.Double rect = null;
            if (!(mur.getPanneauExt().isPoidsValid() && mur.getPanneauInt().isPoidsValid())) {
                double murPosY;
                murPosY = cote.largeur.toDouble() - mur.getLargeur().toDouble();
                if (!mur.GetEstCoinGauche())
                    murPosY = cote.largeur.toDouble() - cote.separateurs.get(indexMur - 1).toDouble() - mur.getLargeur().toDouble();
                rect = new Rectangle2D.Double(posX + w, posY + murPosY, ep, mur.getLargeur().toDouble());
                if (controller.getErrorMessage().equals(""))
                    controller.setErrorMessage("Les panneaux du mur " + (indexMur + 1) + " du côté " + cote.orientation + " excèdent le poids maximum");
            }
            if (rect != null) {
                g2d.setColor(errorColor);
                Composite compoInit = g2d.getComposite();
                AlphaComposite alcom = AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, 0.5f);
                g2d.setComposite(alcom);
                g2d.fill(rect);
                g2d.setComposite(compoInit);
                g2d.setColor(backErrorColor);
                g2d.draw(rect);
                g2d.setColor(lineColor);
            }
            indexMur++;
        }

        // east
        cote = controller.getCoteReadOnly('W');
        indexMur = 0;
        for (Mur mur : cote.murs) {
            Rectangle2D.Double rect = null;
            if (!(mur.getPanneauExt().isPoidsValid() && mur.getPanneauInt().isPoidsValid())) {
                double murPosY;
                murPosY = 0;
                if (!mur.GetEstCoinGauche())
                    murPosY = cote.separateurs.get(indexMur - 1).toDouble();
                rect = new Rectangle2D.Double(posX - ep, posY + murPosY, ep, mur.getLargeur().toDouble());
                if (controller.getErrorMessage().equals(""))
                    controller.setErrorMessage("Les panneaux du mur " + (indexMur + 1) + " du côté " + cote.orientation + " excèdent le poids maximum");
            }
            if (rect != null) {
                g2d.setColor(errorColor);
                Composite compoInit = g2d.getComposite();
                AlphaComposite alcom = AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, 0.5f);
                g2d.setComposite(alcom);
                g2d.fill(rect);
                g2d.setComposite(compoInit);
                g2d.setColor(backErrorColor);
                g2d.draw(rect);
                g2d.setColor(lineColor);
            }
            indexMur++;
        }
    }

    private void drawTrouRetourAir(Graphics2D g2d) {
        CoteDTO cote;

        // south
        cote = controller.getSalleReadOnly().getCote('S');
        for (Accessoire acces : cote.accessoires) {
            if (!acces.isValid() && controller.getErrorMessage().equals(""))
                controller.setErrorMessage("Au moins un accessoire du côté " + cote.orientation + " est dans une position invalide");
            if (acces.getType().equals("Retour d'air")) {
                double retourPosX = acces.getPosition().getX().toDouble();
                double retourPosY = h + (ep - controller.getSalleReadOnly().epaisseurTrouRetourAir.toDouble()) / 2;
                Rectangle2D.Double rect = new Rectangle2D.Double(posX + retourPosX, posY + retourPosY,
                        acces.getLargeur().toDouble(), controller.getSalleReadOnly().epaisseurTrouRetourAir.toDouble());
                g2d.setColor(fillColor.darker().darker());
                g2d.fill(new Area(rect));
                g2d.setColor(lineColor);
                g2d.draw(rect);
            }
        }

        // north
        cote = controller.getSalleReadOnly().getCote('N');
        g2d.setColor(fillColor.darker().darker());
        for (Accessoire acces : cote.accessoires) {
            if (!acces.isValid() && controller.getErrorMessage().equals(""))
                controller.setErrorMessage("Au moins un accessoire du côté " + cote.orientation + " est dans une position invalide");
            if (acces.getType().equals("Retour d'air")) {
                double retourPosX = w - acces.getPosition().getX().toDouble() - acces.getLargeur().toDouble();
                double retourPosY = (ep - controller.getSalleReadOnly().epaisseurTrouRetourAir.toDouble()) / 2;
                Rectangle2D.Double rect = new Rectangle2D.Double(posX + retourPosX, posY - ep + retourPosY,
                        acces.getLargeur().toDouble(), controller.getSalleReadOnly().epaisseurTrouRetourAir.toDouble());
                g2d.setColor(fillColor.darker().darker());
                g2d.fill(new Area(rect));
                g2d.setColor(lineColor);
                g2d.draw(rect);
            }
        }

        // east
        cote = controller.getSalleReadOnly().getCote('E');
        for (Accessoire acces : cote.accessoires) {
            if (!acces.isValid() && controller.getErrorMessage().equals(""))
                controller.setErrorMessage("Au moins un accessoire du côté " + cote.orientation + " est dans une position invalide");
            if (acces.getType().equals("Retour d'air")) {
                double retourPosX = w + (ep - controller.getSalleReadOnly().epaisseurTrouRetourAir.toDouble()) / 2;
                double retourPosY = h - acces.getPosition().getX().toDouble() - acces.getLargeur().toDouble();
                Rectangle2D.Double rect = new Rectangle2D.Double(posX + retourPosX, posY + retourPosY,
                        controller.getSalleReadOnly().epaisseurTrouRetourAir.toDouble(), acces.getLargeur().toDouble());
                g2d.setColor(fillColor.darker().darker());
                g2d.fill(new Area(rect));
                g2d.setColor(lineColor);
                g2d.draw(rect);
            }
        }

        // west
        cote = controller.getSalleReadOnly().getCote('W');
        for (Accessoire acces : cote.accessoires) {
            if (!acces.isValid() && controller.getErrorMessage().equals(""))
                controller.setErrorMessage("Au moins un accessoire du côté " + cote.orientation + " est dans une position invalide");
            if (acces.getType().equals("Retour d'air")) {
                double retourPosX = (ep - controller.getSalleReadOnly().epaisseurTrouRetourAir.toDouble()) / 2;
                double retourPosY = acces.getPosition().getX().toDouble();
                Rectangle2D.Double rect = new Rectangle2D.Double(posX - ep + retourPosX, posY + retourPosY,
                        controller.getSalleReadOnly().epaisseurTrouRetourAir.toDouble(), acces.getLargeur().toDouble());
                g2d.setColor(fillColor.darker().darker());
                g2d.fill(new Area(rect));
                g2d.setColor(lineColor);
                g2d.draw(rect);
            }
        }

        g2d.setColor(lineColor);
    }

    private void drawVue(Graphics g) {
        try {
            Image image = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/vues/vueDessus.png")));
            image = image.getScaledInstance( 64, 64,  Image.SCALE_SMOOTH ) ;
            g.drawImage(image, initialDimension.width - 80, 16, null);
        } catch (Exception except) {
            except.printStackTrace();
        }
    }
}
