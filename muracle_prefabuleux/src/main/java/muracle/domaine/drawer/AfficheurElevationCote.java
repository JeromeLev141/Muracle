package muracle.domaine.drawer;

import muracle.domaine.Cote;
import muracle.domaine.MuracleController;
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

    public void draw(Graphics g) throws FractionError {
        super.draw(g);

        Cote cote = controller.getSelectedCote();
        posX = (initialDimension.width - cote.getLargeur().toDouble()) / 2;
        posY = (initialDimension.getHeight() - cote.getHauteur().toDouble()) / 2;
        w = cote.getLargeur().toDouble();
        h = cote.getHauteur().toDouble();

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));
        drawCote(g2d);
        drawSeparateur(g2d);
        drawAccessoire(g2d);
        drawVue(g);
    }

    private void drawCote(Graphics2D g) {
        Rectangle2D.Double rect = new Rectangle2D.Double(posX, posY, w, h);
        g.setColor(fillColor);
        g.fill(new Area(rect));
        g.setColor(lineColor);
        g.draw(rect);
    }

    private void drawSeparateur(Graphics2D g) throws FractionError {
        Cote cote = controller.getSelectedCote();
        for (int i = 0; i < cote.getSeparateurs().size(); i++) {
            if (controller.isVueExterieur())
                g.draw(new Line2D.Double(cote.getSeparateur(i).toDouble(), posY,
                    cote.getSeparateur(i).toDouble(), posY + h));
            else g.draw(new Line2D.Double(w - cote.getSeparateur(i).toDouble(), posY,
                    w - cote.getSeparateur(i).toDouble(), posY + h));
        }
    }

    private void drawAccessoire(Graphics2D g) throws FractionError {
        /*Cote cote = controller.getSelectedCote();
        for (int i = 0; i < cote.getMurs().size(); i++) {
            posX = cote.getSeparateur(i).toDouble();
            Mur mur = cote.getMurs().get(i);
            for (int j = 0; j < mur.getAccessoires().length; j++) {
                Accessoire accessoire = mur.getAccessoire(j);
                g.draw(new Rectangle2D.Double(posX + Accessoire));
            }
        }*/
    }

    private void drawVue(Graphics g) {
        try {
            Image image;
            String path = "/images/vues/vue" + controller.getSelectedCote().getOrientation();
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
