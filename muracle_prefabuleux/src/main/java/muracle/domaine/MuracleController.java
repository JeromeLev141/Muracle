package muracle.domaine;

import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.FractionError;
import muracle.utilitaire.Pouce;
import muracle.utilitaire.PouceError;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Stack;

public class MuracleController {

    private char coteSelected = ' ';
    private int murSelected = -1;
    private int AccessoireSelected = -1;
    private int separateurSelected = -1;
    private boolean isVueExterieur = true;
    private Pouce distLigneGrille = new Pouce(1, 0, 1);
    private Stack undoPile;
    private Stack redoPile;

    public MuracleController() throws FractionError {
    }

    public void creerProjet() {}

    public void ouvrirProjet() {}

    public void SauvegarderProjet() {}

    public void exporterPlan(File fichier) {
        //a faire
        try {
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            XMLStreamWriter writer = factory.createXMLStreamWriter(new FileOutputStream(fichier));
            writer.writeStartDocument();

            writer.writeStartElement("svg");
            writer.writeAttribute("xmlns", "http://www.w3.org/2000/svg");
            writer.writeAttribute("width", "600");
            writer.writeAttribute("height", "400");

            //example du plan d'un mur

            //plan
            writer.writeEmptyElement("path");
            writer.writeAttribute("d", "M 400 100 L 400 300 L 500 300 L 500 100 z" +
                    "M 100 100 L 100 300 L 200 300 L 200 100 z");
            writer.writeAttribute("fill", "white");
            writer.writeAttribute("stroke", "black");
            writer.writeAttribute("stroke-width", "1");

            //plis
            writer.writeEmptyElement("path");
            writer.writeAttribute("d", "M 410 100 L 410 90 L 490 90 L 490 100 z" +
                    "M 410 300 L 410 310 L 490 310 L 490 300 z" +
                    "M 100 110 L 90 110 L 90 290 L 100 290 z" +
                    "M 200 110 L 210 110 L 210 290 L 200 290 z");
            writer.writeAttribute("fill", "white");
            writer.writeAttribute("stroke", "orange");
            writer.writeAttribute("stroke-width", "1");

            //replis
            writer.writeEmptyElement("path");
            writer.writeAttribute("d", "M 410 90 L 430 80 L 470 80 L 490 90 z" +
                    "M 410 310 L 430 320 L 470 320 L 490 310 z" +
                    "M 90 110 L 80 120 L 80 280 L 90 290 z" +
                    "M 210 110 L 220 120 L 220 280 L 210 290 z");
            writer.writeAttribute("fill", "white");
            writer.writeAttribute("stroke", "blue");
            writer.writeAttribute("stroke-width", "1");

            writer.writeEndDocument();

            writer.close();

        } catch (IOException | XMLStreamException ex) {
                throw new RuntimeException(ex);
        }

        System.out.println("Exportation des plans au fichier : " + fichier.getAbsolutePath());
    }

    public void fermerProjet() {}

    public void pushChange() {}

    public void undoChange() {}

    public void redoChange() {}

    public void getSalle() {}

    public void selectComponent(CoordPouce coordPouce) {}

    public void selectSalleComponent(CoordPouce coordPouce) {}

    public void selectCoteComponent(CoordPouce coordPouce) {}

    private void selectCote(char orientation) {
        coteSelected = orientation;
    }

    public void getSelectedCote() {}

    public void setIsVueExterieur(boolean exterieur) {
        isVueExterieur = exterieur;
    }

    public boolean isVueExterieur() {
        return isVueExterieur;
    }

    public void selectMur(int index) {
        murSelected = index;
    }

    public void getSelectedMur() {}

    public void selectAccessoire(CoordPouce position) {}

    public void getSelectedAccessoire() {}

    public void setDistLigneGrille(String dist) throws FractionError, PouceError {
        distLigneGrille = new Pouce(dist);
    }

    public Pouce getDistLigneGrille() {
        return distLigneGrille;
    }

    public void setDimensionSalle(String largeur, String longueur, String hauteur, String epaisseur) {}

    public void addAccessoire(int indexMur, String type, CoordPouce position) {}

    public void removeAccessoire(int indexMur, CoordPouce position) {}

    public void setDimensionAccessoire(String largeur, String hauteur, String marge) {}

    public void setPositionAccessoire(CoordPouce positionPost) {}

    private void selectSeparateur (int index) {
        separateurSelected = index;
    }

    public void getSelectedSeparateur() {}

    public void addSeparateur(CoordPouce coord) {}

    public void removeSeparateur(int index) {}

    public void moveSeparateur(Pouce position) {}

    public void setParametreRetourAir(String hauteur, String epaisseur, String distanceSol) {}

    public void setParametrePlan(String margeEpaisseur, String margeLargeur, String anglePlis, String longueurPlis) {}
}
