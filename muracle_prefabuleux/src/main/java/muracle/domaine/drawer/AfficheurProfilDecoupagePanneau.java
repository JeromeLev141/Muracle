package muracle.domaine.drawer;

import muracle.domaine.MuracleController;
import muracle.utilitaire.CoordPouce;

import javax.imageio.ImageIO;
import java.awt.*;
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

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(ligneStroke);


        ajustement(g2d,zoom, dim, posiCam,dimPlan);
        if (controller.isGrilleShown())
            drawGrille(g2d, posX, posY, zoom, posiCam, dimPlan);
        drawPlan(g2d);
        reset(g2d,zoom, dim, posiCam, dimPlan);

        drawVue(g);
    }

    /**
     * @brief dessine le paneeau extérieur ou intérieur du mur sélectionné
     * @param g2d l'élément graphic du panel de dessin en Graphics2D
     */
    private void drawPlan(Graphics2D g2d) {}

    /**
     * @brief dessine l'indicateur de vue
     * @param g l'élément graphic du panel de dessin
     */
    private void drawVue(Graphics g) {
        try {
            Image image;
            String path = "/images/vues/vuePlan";
            image = ImageIO.read(Objects.requireNonNull(getClass().getResource(path)));
            image = image.getScaledInstance( 64, 64,  Image.SCALE_SMOOTH ) ;
            g.drawImage(image, initialDimension.width - 80, 16, null);
        } catch (Exception except) {
            except.printStackTrace();
        }
    }
}
