package muracle.domaine.drawer;

import muracle.domaine.*;
import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.FractionError;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Objects;

public class AfficheurElevationCote extends Afficheur {

    private double posX;
    private double posY;
    private double w;
    private double h;

    private double ep;

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
        ep = controller.getSalleReadOnly().profondeur.toDouble();

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(ligneStroke);


        ajustement(g2d,zoom, dim, posiCam,dimPlan);
        if (controller.isGrilleShown())
            drawGrille(g2d, posX, posY, zoom, posiCam, dimPlan);
        drawCote(g2d);
        drawMurs(g2d);
        drawSeparateur(g2d);
        reset(g2d,zoom, dim, posiCam, dimPlan);

        drawVue(g);
        drawErrorMessage(g2d);
    }

    private void drawCote(Graphics2D g2d) {
        Rectangle2D.Double rect = new Rectangle2D.Double(posX, posY, w, h);
        if (controller.isVueExterieur()) {
            Rectangle2D.Double rectWithEp = new Rectangle2D.Double(rect.getX() - ep, rect.y, rect.width + 2 * ep, rect.height);
            Area coteAreaWithEp = new Area(rectWithEp);
            Area coteArea = new Area(rect);
            coteAreaWithEp.subtract(new Area(new Rectangle2D.Double(rect.x + 1, rect.y + 1, rect.width - 2, rect.height - 2)));
            g2d.setColor(fillColor);
            g2d.fill(coteAreaWithEp);
            g2d.setColor(lineColor);
            drawAccessoire(g2d, coteArea);
            g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4}, 0));
            g2d.draw(new Line2D.Double(rect.x, rect.y, rect.x, rect.y + rect.height));
            g2d.draw(new Line2D.Double(rect.x + rect.width, rect.y, rect.x + rect.width, rect.y + rect.height));
            g2d.setStroke(ligneStroke);
            g2d.draw(rectWithEp);
        }
        else {
            Area coteArea = new Area(rect);
            drawAccessoire(g2d, coteArea);
            g2d.draw(rect);
        }
    }

    private void drawSeparateur(Graphics2D g2d) {
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
                g2d.setColor(selectColor);
                g2d.setStroke(selectedStroke);
                g2d.draw(new Line2D.Double(ligne.x1, ligne.y1 + 1.5 * ligneStroke.getLineWidth(), ligne.x2, ligne.y2 - 1.5 * ligneStroke.getLineWidth()));
                g2d.setStroke(ligneStroke);
                g2d.setColor(lineColor);
            }
            g2d.draw(ligne);
        }
    }

    private void drawMurs(Graphics2D g2d) {
        CoteDTO cote = controller.getSelectedCoteReadOnly();
        if (controller.isMurSelected()) {
            MurDTO murSelected = controller.getSelectedMurReadOnly();
            double murPosX = 0;
            if (!murSelected.EstCoinGauche)
                murPosX = cote.separateurs.get(controller.getIndexOfSelectedMur() - 1).toDouble();
            if (!controller.isVueExterieur())
                murPosX = cote.largeur.toDouble() - (murPosX + murSelected.Largeur.toDouble());
            Rectangle2D.Double rect = new Rectangle2D.Double(posX + murPosX, posY, murSelected.Largeur.toDouble(), murSelected.Hauteur.toDouble());
            if (controller.isVueExterieur()) {
                if (murSelected.EstCoinGauche)
                    rect = new Rectangle2D.Double(rect.x - ep, rect.y, rect.width + ep, rect.height);
                if (murSelected.EstCoinDroit)
                    rect = new Rectangle2D.Double(rect.x, rect.y, rect.width + ep, rect.height);
            }
            g2d.setColor(selectColor);
            g2d.setStroke(selectedStroke);
            g2d.draw(rect);
            g2d.setStroke(ligneStroke);
            g2d.setColor(lineColor);
            g2d.draw(rect);
        }

        int indexMur = 0;
        for (Mur mur : controller.getSelectedCoteReadOnly().murs) {
            Rectangle2D.Double rect = null;
            if (!(mur.getPanneauExt().isPoidsValid() && mur.getPanneauInt().isPoidsValid())) {
                double murPosX;
                if (controller.isVueExterieur()) {
                    murPosX = 0;
                    if (!mur.GetEstCoinGauche())
                        murPosX = cote.separateurs.get(indexMur - 1).toDouble();
                } else {
                    murPosX = cote.largeur.toDouble() - mur.getLargeur().toDouble();
                    if (!mur.GetEstCoinGauche())
                        murPosX = cote.largeur.toDouble() - (cote.separateurs.get(indexMur - 1).toDouble() + mur.getLargeur().toDouble());
                }
                rect = new Rectangle2D.Double(posX + murPosX, posY, mur.getLargeur().toDouble(), mur.getHauteur().toDouble());
                if (controller.isVueExterieur()) {
                    if (mur.GetEstCoinGauche())
                        rect = new Rectangle2D.Double(rect.x - ep, rect.y, rect.width + ep, rect.height);
                    if (mur.GetEstCoinDroit())
                        rect = new Rectangle2D.Double(rect.x, rect.y, rect.width + ep, rect.height);
                }
                if (controller.getErrorMessage().equals(""))
                    controller.setErrorMessage("Les panneaux du mur " + (indexMur + 1) + " du côté selectionné excèdent le poids maximum");
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

    private void drawAccessoire(Graphics2D g2d, Area coteArea) {
        CoteDTO cote = controller.getSelectedCoteReadOnly();
        ArrayList<Rectangle2D.Double> rectangles = new ArrayList<>();
        ArrayList<Integer> indexOfInvalidAcces = new ArrayList<>();
        int indexAccesSelected = -1;
        g2d.setColor(fillColor.darker().darker());
        for (Accessoire acces : cote.accessoires) {
            if (!acces.isValid())
                if (controller.getErrorMessage().equals(""))
                    controller.setErrorMessage("Au moins un accessoire du côté selectionné est dans une position invalide");
            Rectangle2D.Double rect = null;
            double accesPosX = acces.getPosition().getX().toDouble();
            double accesPosY = acces.getPosition().getY().toDouble();
            if (controller.isVueExterieur() && !acces.isInterieurOnly())
                rect = new Rectangle2D.Double(posX + accesPosX, posY + accesPosY,
                        acces.getLargeur().toDouble(), acces.getHauteur().toDouble());
            else if (!controller.isVueExterieur())
                rect = new Rectangle2D.Double(posX + w - (accesPosX + acces.getLargeur().toDouble()), posY + accesPosY,
                        acces.getLargeur().toDouble(), acces.getHauteur().toDouble());
            if (rect != null && acces.getType().equals("Fenêtre")) {
                double marge = acces.getMarge().toDouble();
                rect = new Rectangle2D.Double(rect.x - marge, rect.y - marge, rect.width + 2 * marge, rect.height + 2 * marge);
            }
            if (rect != null) {
                coteArea.subtract(new Area(rect));
                if (acces.isInterieurOnly())
                    g2d.fill(new Area(rect));
                rectangles.add(rect);
                if (cote.accessoires.indexOf(acces) == controller.getIndexOfSelectedAccessoire())
                    indexAccesSelected = rectangles.size() - 1;
                if (!acces.isValid())
                    indexOfInvalidAcces.add(rectangles.size() -1);
            }
        }
        g2d.setColor(fillColor);
        g2d.fill(coteArea);
        g2d.setColor(lineColor);
        if (indexAccesSelected != -1) {
            g2d.setColor(selectColor);
            g2d.setStroke(selectedStroke);
            Rectangle2D.Double rect = rectangles.get(indexAccesSelected);
            g2d.draw(rect);
            g2d.setStroke(ligneStroke);
            g2d.setColor(lineColor);
        }
        for (int i = 0; i < rectangles.size(); i++) {
            if (indexOfInvalidAcces.contains(i)) {
                g2d.setColor(errorColor);
                Composite compoInit = g2d.getComposite();
                AlphaComposite alcom = AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, 0.5f);
                g2d.setComposite(alcom);
                g2d.fill(rectangles.get(i));
                g2d.setComposite(compoInit);
                g2d.setColor(backErrorColor);
                g2d.draw(rectangles.get(i));
                g2d.setColor(lineColor);
            }
            else
                g2d.draw(rectangles.get(i));

            if (i == indexAccesSelected) {
                Rectangle2D.Double rect = rectangles.get(i);
                Rectangle2D.Double resizeRect = new Rectangle2D.Double(rect.x + rect.width - 1, rect.y + rect.height - 1,
                        2, 2);
                if (cote.accessoires.get(indexAccesSelected).getType().equals("Porte"))
                    resizeRect = new Rectangle2D.Double(rect.x + rect.width - 1, rect.y - 1,
                            2, 2);
                g2d.fill(resizeRect);
                g2d.setColor(fillColor);
                g2d.fill(new Rectangle2D.Double(resizeRect.x + 0.5, resizeRect.y + 0.5, 1, 1));
                g2d.setColor(lineColor);
            }
        }
    }

    private void drawVue(Graphics g) {
        try {
            Image image;
            String path = "/images/vues/vue" + controller.getSelectedCoteReadOnly().orientation;
            if (controller.isVueExterieur())
                path += "Ext.png";
            else path += "Int.png";
            image = ImageIO.read(Objects.requireNonNull(getClass().getResource(path)));
            image = image.getScaledInstance( 64, 64,  Image.SCALE_SMOOTH ) ;
            g.drawImage(image, initialDimension.width - 80, 16, null);
        } catch (Exception except) {
            except.printStackTrace();
        }
    }
}
