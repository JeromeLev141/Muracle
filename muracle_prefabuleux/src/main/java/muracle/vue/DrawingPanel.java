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
    private Fraction zoomInc;
    private CoordPouce posiCam;
    private CoordPouce dimPlan;
    private Color backgroundColor;


    public DrawingPanel(MainWindow mainWindow){
        this.mainWindow = mainWindow;
        dimPlan = null;
        try {
            zoomFactor = new Fraction(1, 1);
            zoomInc = new Fraction(1,100);
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
            this.update();
            if (mainWindow.controller.isVueDessus()) {
                drawer = new AfficheurPlanSalle(mainWindow.controller, getSize());
            }
            else {
                drawer = new AfficheurElevationCote(mainWindow.controller, getSize());
            }

            try {
                drawer.draw(g,(double)zoomFactor.getDenum()/zoomFactor.getNum(),this.getSize(),posiCam,dimPlan);

            } catch (FractionError e) {
                e.printStackTrace();
            }
        }
    }

    public void update(){
        if (mainWindow.controller.isVueDessus())
            try {
                this.dimPlan = mainWindow.controller.getSalle().getDimension();
            }catch (FractionError ignored){}

        else if (mainWindow.controller.isVueCote())
            this.dimPlan = mainWindow.controller.getSelectedCote().getDimension();
    }

    public void updateParametre(){
        resetZoomFactor();
        if (mainWindow.controller.isVueDessus())
            try {
                this.dimPlan = mainWindow.controller.getSalle().getDimension();
            }catch (FractionError ignored){}

        else if (mainWindow.controller.isVueCote())
            this.dimPlan = mainWindow.controller.getSelectedCote().getDimension();

        try{
            this.posiCam = new CoordPouce(this.dimPlan.getX().div(2), this.dimPlan.getY().div(2));
        }catch (Exception ex){
            throw new Error("Erreur dans l'opdate des paramètre");
        }
    }

    private CoordPouce coordPixelToPouceAll(MouseEvent event){
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

    public CoordPouce coordPixelToPouce(MouseEvent event){
        try {
            //System.out.println(dimPlan + " " + zoomFactor + getSize() + posiCam);

            CoordPouce coord = coordPixelToPouceAll(event);
            double x =coord.getX().toDouble();
            double y = coord.getY().toDouble();
            if (x >= 0 && x <= dimPlan.getX().toDouble() && y >= 0 && y <= dimPlan.getY().toDouble())
                return coord;
            else
                return null;
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
        if (zoomFactor.toDouble() <= 0.1){
            this.zoomFactor.setNum(1);
            this.zoomFactor.setDenum(10);
        }else {
            try {
                this.zoomInc = new Fraction(5,100);
                this.setZoomFactor((int) ((double) this.zoomFactor.getDenum() / this.zoomFactor.getNum() * 100 + 5));
                //zoomInc = zoomInc.subRef(zoomFactor);
            }catch (FractionError ignored){}
        }

        System.out.println(zoomInc);
    }

    public void subZoomFactor() {

        if (zoomFactor.toDouble() >= 5) {
            this.zoomFactor.setNum(5);
            this.zoomFactor.setDenum(1);
        } else {
            this.setZoomFactor((int) ((double) this.zoomFactor.getDenum() / this.zoomFactor.getNum() * 100 - 5));
        }
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

    public void setZoomInc(int zoomFactor){
        this.zoomInc.setDenum(zoomFactor);
        this.zoomInc.setNum(100);
        this.zoomInc.simplifier();
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
        //CoordPouce c = coordPixelToPouceAll(e);
        //System.out.println(c.toString() + " <=> " + e.getX() + " - " + e.getY());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(e.getWheelRotation() < 0) {
            //Zoom
            addZoomFactor();
            try {
                if (zoomFactor.toDouble() >= 3) {
                    this.posiCam = new CoordPouce(this.dimPlan.getX().div(2), this.dimPlan.getY().div(2));
                }else if (zoomFactor.toDouble() > 0.1) {
                    //CoordPouce posi = coordPixelToPouce(e);
                    Pouce x = dimPlan.getX().div(2);

                    //Pouce x = new Pouce(0,getSize().width,2);
                    x.mulRef(zoomInc);
                    x.mulRef(new Fraction(2*e.getX(),getSize().width).subRef(1));
                    this.posiCam.getX().addRef(x);

                    Pouce y = dimPlan.getY().div(2);
                    //Pouce y = new Pouce(0,getSize().height,2);
                    y.mulRef(zoomInc);
                    y.mulRef(new Fraction(2*e.getY(),getSize().height).subRef(1));
                    this.posiCam.getY().addRef(y);
                }
                System.out.println(this.posiCam);
                System.out.println(dimPlan);
                System.out.println(getSize());
            }catch (FractionError ignored){}
            this.repaint();

        }
        else{
            //Dezoom
            subZoomFactor();
            if (zoomFactor.toDouble() >= 3)
                try {
                    this.posiCam = new CoordPouce(this.dimPlan.getX().div(2), this.dimPlan.getY().div(2));
                }catch (FractionError ignored){}
            this.repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //System.out.println("X : " + e.getX() + " | Y : " + e.getY());

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
