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
import java.awt.event.*;

public class DrawingPanel extends JPanel implements MouseMotionListener, MouseWheelListener, MouseListener {
    private MainWindow mainWindow;
    private Fraction zoomFactor;
    private CoordPouce posiCam;
    private CoordPouce dimPlan;
    private Color backgroundColor;


    public DrawingPanel(MainWindow mainWindow){
        this.mainWindow = mainWindow;
        dimPlan = null;
        try {
            zoomFactor = new Fraction(1, 1);
        }catch (Exception ignored){}
        posiCam = null;
        backgroundColor = new Color(89, 100, 124);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }

    public void paintComponent(Graphics g){
        if (mainWindow != null)
        {
            super.paintComponent(g);
            if (mainWindow.isDarkMode)
                setBackground(backgroundColor);
            else setBackground(Color.WHITE);
            Afficheur drawer = new Afficheur(mainWindow.controller, getSize());

            if (mainWindow.controller.isVueDessus()) {
                this.updateParametre();
                drawer = new AfficheurPlanSalle(mainWindow.controller, getSize());
            }
            else {
                this.updateParametre();
                drawer = new AfficheurElevationCote(mainWindow.controller, getSize());
            }

            try {
                drawer.draw(g,(double)zoomFactor.getDenum()/zoomFactor.getNum(),this.getSize(),posiCam,dimPlan);

            } catch (FractionError e) {
                e.printStackTrace();
            }
        }
    }

    public void updateParametre(){
        //resetZoomFactor();
        if (mainWindow.controller.getSelectedCote() == null)
            try {
                this.dimPlan = mainWindow.controller.getSalle().getDimension();
            }catch (FractionError ignored){}

        else if (mainWindow.controller.getSelectedMur() == null)
            this.dimPlan = mainWindow.controller.getSelectedCote().getDimension();

        try{
            this.posiCam = new CoordPouce(this.dimPlan.getX().div(2), this.dimPlan.getY().div(2));
        }catch (Exception ex){
            throw new Error("Erreur dans l'opdate des paramètre");
        }


    }


    public CoordPouce coordPixelToPouce(MouseEvent event){
        try {
            //System.out.println(dimPlan + " " + zoomFactor + getSize() + posiCam);

            Pouce posiX = new Pouce(0,this.getSize().width,2);
            posiX.mulRef(zoomFactor);
            posiX.mulRef(new Fraction(2* event.getX(),this.getSize().width).subRef(new Fraction(1,1)));
            posiX.addRef(posiCam.getX());

            Pouce posiY = new Pouce(0,this.getSize().height,2);
            posiY.mulRef(zoomFactor);
            posiY.mulRef(new Fraction(2* event.getY(),this.getSize().height).subRef(new Fraction(1,1)));
            posiY.addRef(posiCam.getY());
            return new CoordPouce(posiX,posiY);
        }catch (Exception ex){
            throw new Error("Erreur dans le calcul de coord");
        }
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
        this.setZoomFactor((int)((double)this.zoomFactor.getDenum()/this.zoomFactor.getNum()*100 + 10));
    }

    public void subZoomFactor() {
        this.setZoomFactor((int)((double)this.zoomFactor.getDenum()/this.zoomFactor.getNum()*100 - 10));
    }

    public void resetZoomFactor() {
        this.zoomFactor.setNum(1);
        this.zoomFactor.setDenum(1);
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

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        CoordPouce c = coordPixelToPouce(e);
        System.out.println(c.toString() + " <=> " + e.getX() + " - " + e.getY());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(e.getWheelRotation() < 0) {
            addZoomFactor();
            this.repaint();

        }
        else{
            subZoomFactor();
            this.repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
