package muracle.vue;

import muracle.domaine.drawer.Afficheur;
import muracle.domaine.drawer.AfficheurElevationCote;
import muracle.domaine.drawer.AfficheurPlanSalle;
import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.Fraction;
import muracle.utilitaire.FractionError;
import muracle.utilitaire.Pouce;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class DrawingPanel extends JPanel {
    private Dimension dimPanel;
    private MainWindow mainWindow;
    private Fraction zoomFactor;
    private CoordPouce posiCam;
    private Dimension dimPlan;
    private Fraction ratioWH;

    public DrawingPanel(){}

    public DrawingPanel(MainWindow mainWindow){
        this.mainWindow = mainWindow;
        dimPanel = null;
        dimPlan = null;
        try {
            zoomFactor = new Fraction(1, 1);
            ratioWH = new Fraction(1, 1);
        }catch (Exception ignored){}
        posiCam = null;
    }
    public void paintComponent(Graphics g){
        /*
        Fonction non terminé
         */
        if (mainWindow != null)
        {
            super.paintComponent(g);
            Afficheur drawer = new Afficheur(mainWindow.controller, getSize());
            if (mainWindow.controller.getSelectedCote() == null)
                drawer = new AfficheurPlanSalle(mainWindow.controller, getSize());
            else if (mainWindow.controller.getSelectedMur() == null)
                drawer = new AfficheurElevationCote(mainWindow.controller, getSize());
            try {
                drawer.draw(g);
            } catch (FractionError e) {
                e.printStackTrace();
            }
        }
    }

    public void updateParametre(Dimension dimPanel, Dimension dimPlan){
        resetZoomFactor();
        this.dimPlan = dimPlan;
        this.dimPanel = dimPanel;
        try{
            this.posiCam = new CoordPouce(new Pouce(dimPlan.width,0,1), new Pouce(dimPlan.height,0,1));
            this.ratioWH = new Fraction(dimPanel.width,dimPanel.height);
        }catch (Exception ex){
            throw new Error("Erreur dans le calcul de coord");
        }


    }

    public CoordPouce coordPixelToPouce(MouseEvent event){
        try {
            Pouce posiX = new Pouce(0, dimPlan.width, 2).addRef(posiCam.getX()).mulRef(zoomFactor);
            posiX.mulRef(new Fraction(2* event.getX(),dimPanel.width).subRef(new Fraction(-1,1)));

            Pouce posiY = new Pouce(0, dimPlan.height, 2).addRef(posiCam.getY()).mulRef(zoomFactor).mulRef(this.ratioWH);
            posiY.mulRef(new Fraction(2* event.getY(),dimPanel.height).subRef(new Fraction(-1,1)));

            return new CoordPouce(posiX,posiY);
        }catch (Exception ex){
            throw new Error("Erreur dans le calcul de coord");
        }
    }



    public Dimension getDimPanel() {
        return dimPanel;
    }

    public void setDimPanel(Dimension dimPanel) {
        this.dimPanel = dimPanel;
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public Fraction getZoomFactor() {
        return zoomFactor;
    }

    public void addZoomFactor() {
    }

    public void subZoomFactor() {
    }

    public void resetZoomFactor() {
        this.zoomFactor.setNum(3);
        this.zoomFactor.setDenum(2);
    }

    public void setZoomFactor(int zoomFactor){
        this.zoomFactor.setDenum(zoomFactor);
        this.zoomFactor.setNum(100);
        this.zoomFactor.simplifier();
    }


    public CoordPouce getPosiCam() {
        return posiCam;
    }

    public void setPosiCam(CoordPouce posiCam) {
        this.posiCam = posiCam;
    }

    public Dimension getDimPlan() {
        return dimPlan;
    }

    public void setDimPlan(Dimension dimPlan) {
        this.dimPlan = dimPlan;
    }

    public Fraction getRatioWH() {
        return ratioWH;
    }

    public void setRatioWH(int largeur, int longueur) {
        this.ratioWH.setNum(largeur);
        this.ratioWH.setDenum(longueur);
        this.ratioWH.simplifier();
    }
}
