package muracle.domaine.drawer;

import muracle.domaine.MurDTO;
import muracle.domaine.MuracleController;
import muracle.domaine.PlanPanneau;
import muracle.utilitaire.CoordPouce;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Objects;

public class AfficheurProfilDecoupagePanneau extends Afficheur {

    private double posX;
    private double posY;

    /**
     * @brief constructeur
     * @param controller controller du domaine
     * @param initDim dimension du panel de dessin
     */
    public AfficheurProfilDecoupagePanneau(MuracleController controller, Dimension initDim) {super(controller, initDim);}

    /**
     * @brief dessine les éléments à afficher
     * @param g l'élément graphic du panel de dessin
     * @param zoom facteur de zoom en double
     * @param dim dimension du panel de dessin
     * @param posiCam position de la caméra (point de vue)
     * @param dimPlan dimension de ce qui est dessiné
     */
    public void draw(Graphics g, double zoom, Dimension dim, CoordPouce posiCam, CoordPouce dimPlan) {
        super.draw(g,zoom,dim,posiCam,dimPlan);

        PlanPanneau plan = controller.genererPlanSelectedMur();
        posX = (initialDimension.width - plan.getLargeur().toDouble()) / 2;
        posY = (initialDimension.getHeight() - plan.getHauteur().toDouble()) / 2;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(ligneStroke);

        ajustement(g2d,zoom, dim, posiCam,dimPlan);
        if (controller.isGrilleShown())
            drawGrille(g2d, posX, posY, zoom, posiCam, dimPlan);
        drawPlan(g2d, plan);
        reset(g2d,zoom, dim, posiCam, dimPlan);

        drawVue(g);
    }

    /**
     * @brief dessine le panneau extérieur ou intérieur du mur sélectionné
     * @param g2d l'élément graphic du panel de dessin en Graphics2D
     */
    private void drawPlan(Graphics2D g2d, PlanPanneau plan) {

        g2d.setStroke(ligneStroke);
        g2d.setColor(lineColor);

        Path2D pathPlan = new Path2D.Double();
        CoordPouce firstPoint = plan.getPolygone().get(0);
        pathPlan.moveTo(posX + firstPoint.getX().toDouble(), posY + firstPoint.getY().toDouble());
        for(CoordPouce coord : plan.getPolygone()){
            pathPlan.lineTo(posX + coord.getX().toDouble(), posY + coord.getY().toDouble());
        }
        Area outlineArea = new Area(pathPlan);

        if(!plan.getPolygoneAccessoire().isEmpty()){
            removeAccessoiresFromArea(plan, outlineArea);
        }
        g2d.draw(outlineArea);
        g2d.setColor(fillColor);
        g2d.fill(outlineArea);

        drawLignePlie(g2d, plan);
    }

    private void drawLignePlie(Graphics2D g2d, PlanPanneau plan) {
        g2d.setStroke(new BasicStroke(0.5F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4}, 10));
        g2d.setColor(lineColor);


        for(CoordPouce[] coord : plan.getLignePlie()){
            //Path2D pathLignes = new Path2D.Double();
            //pathLignes.moveTo(posX + coord[0].getX().toDouble(), posY + coord[0].getY().toDouble());
            //pathLignes.lineTo(posX + coord[1].getX().toDouble(), posY + coord[1].getY().toDouble());
            //pathLignes.closePath();
            g2d.draw(new Line2D.Double(posX + coord[0].getX().toDouble(),
                                    posY + coord[0].getY().toDouble(),
                                    posX + coord[1].getX().toDouble(),
                                    posY + coord[1].getY().toDouble()));
            //g2d.draw(pathLignes);
            //g2d.fill(pathLignes);
        }

    }

    private void removeAccessoiresFromArea(PlanPanneau plan, Area outlineArea) {
        for (List<CoordPouce> coordsAccessoire : plan.getPolygoneAccessoire()) {
            Path2D pathAccessoire = new Path2D.Double();
            pathAccessoire.moveTo(posX + coordsAccessoire.get(0).getX().toDouble(),
                    posY + coordsAccessoire.get(0).getY().toDouble());

            for(int i = 1; i < coordsAccessoire.size(); i++){
                pathAccessoire.lineTo(posX + coordsAccessoire.get(i).getX().toDouble(),
                        posY + coordsAccessoire.get(i).getY().toDouble());
            }
            pathAccessoire.closePath();
            Area acessoireArea = new Area(pathAccessoire);
            outlineArea.subtract(acessoireArea);
            System.out.println(acessoireArea.toString());
        }
    }

    /**
     * @brief dessine l'indicateur de vue
     * @param g l'élément graphic du panel de dessin
     */
    private void drawVue(Graphics g) {
        try {
            Image image;
            String path = "/images/vues/vuePlan";
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
