package muracle.vue;

import muracle.domaine.drawer.Afficheur;
import muracle.domaine.drawer.AfficheurElevationCote;
import muracle.domaine.drawer.AfficheurPlanSalle;
import muracle.domaine.drawer.AfficheurProfilDecoupagePanneau;
import muracle.utilitaire.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class DrawingPanel extends JPanel implements MouseWheelListener {
    private MainWindow mainWindow;
    private Fraction zoomFactor;
    private Fraction zoomInc;
    private CoordPouce posiCam;
    private CoordPouce posiCamTempo;
    private CoordPouce dimPlan;
    private final Color backgroundColor;
    private Point mouse_pt;
    private boolean clip;

    /**
     * @brief constructeur de la classe DrawingPanel
     * @param mainWindow:MainWindow
     */
    public DrawingPanel(MainWindow mainWindow){
        this.mainWindow = mainWindow;
        dimPlan = null;
        try {
            zoomFactor = new Fraction(1, 2);
            zoomInc = new Fraction(0,1);
        }catch (Exception ignored){}
        posiCam = null;
        posiCamTempo = null;
        backgroundColor = new Color(89, 100, 124);
        addMouseWheelListener(this);
        mouse_pt = null;
        clip = false;
    }

    /**
     * @brief paint les components dans le jpanel
     * @param g: Graphics
     */
    public void paintComponent(Graphics g){
        if (mainWindow != null)
        {
            super.paintComponent(g);
            if (mainWindow.isDarkMode)
                setBackground(backgroundColor);
            else setBackground(new Color(223, 237, 255));
            Afficheur drawer;//= new Afficheur(mainWindow.controller, getSize());
            this.update();
            if (mainWindow.controller.isVuePlanDecoupage()) {
                drawer = new AfficheurProfilDecoupagePanneau(mainWindow.controller, getSize(), mainWindow.isDarkMode);
            }
            else if (mainWindow.controller.isVueDessus()) {
                drawer = new AfficheurPlanSalle(mainWindow.controller, getSize(), mainWindow.isDarkMode);
            }
            else {
                drawer = new AfficheurElevationCote(mainWindow.controller, getSize(), mainWindow.isDarkMode);
            }

            drawer.draw(g,(double)zoomFactor.getDenum()/zoomFactor.getNum(),this.getSize(),posiCam,dimPlan);
        }
    }


    /**
     * @brief update dimPlan
     */
    private void update(){
        if (mainWindow.controller.isVueDessus())
            try {
                this.dimPlan = mainWindow.controller.getSalleReadOnly().getDimension();
            }catch (FractionError ignored){}

        else if (mainWindow.controller.isVueCote())
            this.dimPlan = mainWindow.controller.getSelectedCoteReadOnly().getDimension();

    }

    /**
     * @brief update zoomFactor, dimPlan et posiCam
     */
    public void updateParametre(boolean doResetZoom){
        if (doResetZoom)
            resetZoomFactor();
        if (mainWindow.controller.isVueDessus())
            try {
                this.dimPlan = mainWindow.controller.getSalleReadOnly().getDimension();
            }catch (FractionError ignored){}

        else if (mainWindow.controller.isVueCote())
            this.dimPlan = mainWindow.controller.getSelectedCoteReadOnly().getDimension();

        try{
            this.posiCam = new CoordPouce(this.dimPlan.getX().div(2), this.dimPlan.getY().div(2));
        }catch (Exception ex){
            throw new Error("Erreur dans l'opdate des paramètre");
        }
    }

    /**
     * @brief inverse le x de la position de la cam
     */
    public void miroir(){
        posiCam.setX(dimPlan.getX().sub(posiCam.getX()));
    }

    /**
     * @brief calcule avec l'emplacement de la sourie la position dans le plan en Pouce
     * @param event :MouseEvent
     * @return CoordPouce de l'emplacement de la sourie
     */
    private CoordPouce coordPixelToPouceAll(MouseEvent event){
        try {

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

    /**
     * @brief calcule la position de la souris dans le plan. Si click a l'extérieur du plan return null
     * @param event :MouseEvent
     * @return CoordPouce valide
     */
    public CoordPouce coordPixelToPouce(MouseEvent event){
        try {
            CoordPouce coord = coordPixelToPouceAll(event);
            coord.round(64);
            double x = coord.getX().toDouble();
            double y = coord.getY().toDouble();
            if (x >= 0 && x <= dimPlan.getX().toDouble() && y >= 0 && y <= dimPlan.getY().toDouble())
                return coord;
            else
                return null;
        }catch (Exception ex){
            throw new Error("Erreur dans le calcul de coord");
        }
    }

    /**
     * @brief getteur de l'attribut mainWindow()
     * @return this.mainWindow
     */
    public MainWindow getMainWindow() {
        return mainWindow;
    }

    /**
     * @brief setteur du mainWindow
     * @param mainWindow: MainWindow
     */
    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    /**
     * @brief getteur du facteurZoom
     * @return this.zoomFactor
     */
    public Fraction getZoomFactor() {
        return zoomFactor;
    }

    /**
     * @brief diminue le zoomFacteur par zoomInc
     */
    private void addZoomFactor() {
        if(!zoomFactor.equals(zoomInc))
            zoomFactor.subRef(zoomInc);

    }

    /**
     * @brief augmente le zoomFacteur par zoomInc
     */
    private void subZoomFactor(){
        if (zoomFactor.toDouble() < 20000)
            zoomFactor.addRef(zoomInc);
        else
        {
            this.zoomFactor.setNum(20000);
            this.zoomFactor.setDenum(1);
        }
    }

    /**
     * @brief remet le zoomFactor a 2 == 50%
     */
    public void resetZoomFactor() {
        this.zoomFactor.setNum(1);
        this.zoomFactor.setDenum(2);
    }

    /**
     * @brief set le zoomFactor par un poucentage
     * @param zoomFactor:int pourcentage de zoom
     */
    public void setZoomFactor(int zoomFactor){
        this.zoomFactor.setDenum(zoomFactor);
        this.zoomFactor.setNum(100);
        this.zoomFactor.simplifier();
    }

    /**
     * @brief calcule le zoomInc par rapport au zoom
     */
    private void calculZoomInc(){
        try {
            zoomInc = zoomFactor.mul(new Fraction(1,10)).round(1048576);
            if(zoomInc.toDouble() == 0)
                zoomInc = new Fraction(1,2097152);

        }catch (FractionError ignored){}
    }

    /**
     * @brief mouse drag fonction
     * @param e:MouseMotionEvent
     */
    public void moved(MouseEvent e){
        if (clip) {
            Fraction dx = zoomFactor.mul(mouse_pt.x-e.getX());
            Fraction dy = zoomFactor.mul(mouse_pt.y-e.getY());
            posiCam.setX(posiCamTempo.getX().add(dx));
            posiCam.setY(posiCamTempo.getY().add(dy));
            this.repaint();
        }
    }

    /**
     * @brief déplacement en CoordPouce depuis le dernier clip
     * @param e (MouseEvent): l'évenement de la sourie
     * @return CoordPouce
     */
    public CoordPouce movedItem(MouseEvent e) {
        Fraction dx = zoomFactor.mul(mouse_pt.x-e.getX());
        Fraction dy = zoomFactor.mul(mouse_pt.y-e.getY());
        return new CoordPouce(new Pouce(0, dx), new Pouce(0, dy));
    }

    /**
     * @brief clip pour le déplacement de l'image avec souris
     * @param e (MouseEvent): L'évènement de la souris
     */
    public void press(MouseEvent e){
        clip = true;
        mouse_pt = e.getPoint();
        posiCamTempo = new CoordPouce(posiCam.getX().copy(), posiCam.getY().copy());
    }

    /**
     * @brief clip pour le déplacement d'un objet
     * @param e (MouseEvent): L'évènement de la souris
     */
    public void pressItem(MouseEvent e){
        clip = true;
        mouse_pt = e.getPoint();
    }

    /**
     * @brief unclip pour le déplacement de l'image avec souris
     * @param e (MouseEvent): L'évènement de la souris
     */
    public void release(MouseEvent e){
        moved(e);
        clip = false;
        mouse_pt = null;
        posiCamTempo = null;

    }

    /**
     * @brief unclip pour le déplacement d'un objet
     */
    public void releaseItem(){
        clip = false;
        mouse_pt = null;
    }

    /**
     * @brief event du mouseWheel
     * @param e:MouseWheelEvent
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(e.getWheelRotation() < 0) {
            //zoomIn
            calculZoomInc();
            addZoomFactor();

            try {
                if (zoomFactor.toDouble() == 5) {
                    this.posiCam = new CoordPouce(this.dimPlan.getX().div(2), this.dimPlan.getY().div(2));
                }else if (zoomFactor.toDouble() > 0.002) {

                    Pouce x = new Pouce(0,this.getWidth(),2);
                    x.mulRef(zoomInc);
                    x.mulRef(new Fraction(2*e.getX(),this.getWidth()).subRef(1));
                    this.posiCam.getX().addRef(x);

                    Pouce y = new Pouce(0,this.getHeight(),2);
                    y.mulRef(zoomInc);
                    y.mulRef(new Fraction(2*e.getY(),this.getHeight()).subRef(1));
                    this.posiCam.getY().addRef(y);
                }
            }catch (FractionError | PouceError ignored){}
            this.repaint();

        }
        else{
            //zoomOut
            calculZoomInc();
            subZoomFactor();
            this.repaint();
        }
    }

}
