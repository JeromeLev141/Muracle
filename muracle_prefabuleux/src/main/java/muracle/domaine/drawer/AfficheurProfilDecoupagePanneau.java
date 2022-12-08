package muracle.domaine.drawer;

import muracle.domaine.MuracleController;
import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.FractionError;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.Objects;

public class AfficheurProfilDecoupagePanneau extends Afficheur {

    private double posX;
    private double posY;

    public AfficheurProfilDecoupagePanneau(MuracleController controller, Dimension initDim) {super(controller, initDim);}

    public void draw(Graphics g, double zoom, Dimension dim, CoordPouce posiCam, CoordPouce dimPlan) throws FractionError {
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

    private void drawPlan(Graphics2D g) {}

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
