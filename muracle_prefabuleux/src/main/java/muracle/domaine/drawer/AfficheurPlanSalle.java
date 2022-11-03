package muracle.domaine.drawer;

import muracle.domaine.MuracleController;

import java.awt.*;

public class AfficheurPlanSalle extends Afficheur{

    public AfficheurPlanSalle(MuracleController controller, Dimension initDim) {
        super(controller, initDim);
    }

    public void draw(Graphics g) {
        super.draw(g);
        drawSalle(g);
        drawSeparateur(g);
        drawTrouRetourAir(g);
    }

    private void drawSalle(Graphics g) {}

    private void drawSeparateur(Graphics g) {}

    private void drawTrouRetourAir(Graphics g) {}
}
