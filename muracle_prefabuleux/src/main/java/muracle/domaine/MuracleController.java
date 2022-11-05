package muracle.domaine;

import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.FractionError;
import muracle.utilitaire.Pouce;
import muracle.utilitaire.PouceError;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

public class MuracleController {

    private Salle salle;
    private char coteSelected;
    private int murSelected;
    private int accessoireSelected;
    private int separateurSelected;
    private boolean isVueExterieur;
    private Pouce distLigneGrille;
    private boolean isGrilleShown;
    private GenerateurPlan generateurPlan;
    private Stack<Object> undoPile;
    private Stack<Object> redoPile;

    public MuracleController() throws FractionError, PouceError {
        creerProjet();
        distLigneGrille = new Pouce("12");
        isGrilleShown = false;
        generateurPlan = new GenerateurPlan();
    }

    public void creerProjet() throws FractionError, PouceError {
        salle = new Salle(new Pouce("144"), new Pouce("144"),
                new Pouce("144"), new Pouce("12"));
        coteSelected = ' ';
        murSelected = -1;
        accessoireSelected = -1;
        separateurSelected = -1;
        isVueExterieur = true;
        undoPile = new Stack<>();
        redoPile = new Stack<>();
    }

    public void ouvrirProjet(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Ouverture de Projet");
        fileChooser.setFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
        int returnValue = fileChooser.showOpenDialog(parent);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File fichier = fileChooser.getSelectedFile();
            System.out.println("Ouverture du fichier : " + fichier.getAbsolutePath());
        }
    }

    public void sauvegarderProjet(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Sauvegarde de Projet");
        fileChooser.setFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
        int returnValue = fileChooser.showSaveDialog(parent);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File fichier = fileChooser.getSelectedFile();
            if(!fileChooser.getSelectedFile().getAbsolutePath().endsWith(".txt"))
                fichier = new File(fileChooser.getSelectedFile() + ".txt");
            try(FileWriter fw = new FileWriter(fichier)) {
                fw.write("test");
            } catch (Exception except) {
                except.printStackTrace();
            }
            System.out.println("Sauvegarde du fichier : " + fichier.getAbsolutePath());
        }
    }

    public void exporterPlan(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exporter les plans");
        fileChooser.setFileFilter(new FileNameExtensionFilter("*.svg", "SVG"));
        int returnValue = fileChooser.showSaveDialog(parent);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File fichier = fileChooser.getSelectedFile();
            if (!fileChooser.getSelectedFile().getAbsolutePath().endsWith(".svg"))
                fichier = new File(fileChooser.getSelectedFile() + ".svg");
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
    }

    public void fermerProjet() {}

    public void pushChange() {}

    public void undoChange() {}

    public void redoChange() {}

    public Salle getSalle() {
        return salle;
    }

    public void selectComponent(CoordPouce coordPouce) {}

    public void selectSalleComponent(CoordPouce coordPouce) {}

    public void selectCoteComponent(CoordPouce coordPouce) {}

    public void selectCote(char orientation) {
        coteSelected = orientation;
    }

    public Cote getSelectedCote() {
        if (coteSelected != ' ')
            return salle.getCote(coteSelected);
        return null;
    }

    public void setIsVueExterieur(boolean exterieur) {
        isVueExterieur = exterieur;
    }

    public boolean isVueExterieur() {
        return isVueExterieur;
    }

    public void selectMur(int index) {
        murSelected = index;
    }

    public Mur getSelectedMur() {
        if (murSelected != -1)
            return getSelectedCote().getMurs().get(murSelected);
        return null;
    }

    public void selectAccessoire(CoordPouce position) {}

    public Accessoire getSelectedAccessoire() {
        if (accessoireSelected != 1)
            return getSelectedMur().getAccessoire(accessoireSelected);
        return null;
    }

    public void setDistLigneGrille(String dist) {
        try {
            if (!dist.contains("-"))
                distLigneGrille = new Pouce(dist);
        } catch (PouceError | FractionError ignored) {
            System.out.println("valeur invalide");
        }
    }

    public Pouce getDistLigneGrille() {
        return distLigneGrille;
    }

    public boolean isGrilleShown() {
        return isGrilleShown;
    }

    public void reverseIsGrilleShown() {
        isGrilleShown = !isGrilleShown;
    }

    public void setDimensionSalle(String largeur, String longueur, String hauteur, String profondeur) {
        try {
            if (!largeur.contains("-"))
                salle.setLargeur(new Pouce(largeur));
            if (!longueur.contains("-"))
                salle.setLongueur(new Pouce(longueur));
            if (!hauteur.contains("-"))
                salle.setHauteur(new Pouce(hauteur));
            if (!profondeur.contains("-"))
                salle.setProfondeur(new Pouce(profondeur));
        } catch (PouceError | FractionError ignored) {
            System.out.println("valeur invalide");
        }
    }

    public void addAccessoire(int indexMur, String type, CoordPouce position) {}

    public void removeAccessoire(int indexMur, CoordPouce position) {}

    public void setDimensionAccessoire(String largeur, String hauteur, String marge) {
        /*try {
            if (!largeur.contains("-"))
            if (!hauteur.contains("-"))
            if (!marge.contains("-"))
        } catch (PouceError | FractionError ignored) {
            System.out.println("valeur invalide");
        }
         */
    }

    public void setPositionAccessoire(CoordPouce positionPost) {}

    private void selectSeparateur (int index) {
        separateurSelected = index;
    }

    public Pouce getSelectedSeparateur() {
        if (separateurSelected != -1)
            return getSelectedCote().getSeparateurs().get(separateurSelected);
        return null;
    }

    public void addSeparateur(CoordPouce coord) {}

    public void removeSeparateur(int index) {}

    public void moveSeparateur(Pouce position) {}

    public void setParametreRetourAir(String hauteur, String epaisseur, String distanceSol) {
        try {
            if (!hauteur.contains("-"))
                salle.setHauteurRetourAir(new Pouce(hauteur));
            if (!epaisseur.contains("-"))
                salle.setEpaisseurTrouRetourAir(new Pouce(epaisseur));
            if (!distanceSol.contains("-"))
                salle.setDistanceTrouRetourAir(new Pouce(distanceSol));
        } catch (PouceError | FractionError ignored) {
            System.out.println("valeur invalide");
        }
    }

    public String getParametrePlan(int indexParam) {
        String paramValue = "";
        switch (indexParam) {
            case 0 :
                paramValue = generateurPlan.getLongueurPlis().toString();
                break;
            case 1 :
                paramValue = generateurPlan.getMargeEpaisseurMateriaux().toString();
                break;
            case 2 :
                paramValue = generateurPlan.getMargeLargeurReplis().toString();
                break;
            case 3 :
                paramValue = String.valueOf(generateurPlan.getAnglePlis());
                break;
        }
        return  paramValue;
    }

    public void setParametrePlan(String margeEpaisseur, String margeLargeur, String anglePlis, String longueurPlis) {
        try {
            if (!margeEpaisseur.contains("-"))
                generateurPlan.setMargeEpaisseurMateriaux(new Pouce(margeEpaisseur));
            if (!margeLargeur.contains("-"))
                generateurPlan.setMargeLargeurReplis(new Pouce(margeLargeur));
            double angle = Double.parseDouble(anglePlis);
            if (0 <= angle && angle <= 90)
                generateurPlan.setAnglePlis(angle);
            else
                System.out.println("valeur invalide");
            if (!longueurPlis.contains("-"))
                generateurPlan.setLongueurPlis(new Pouce(longueurPlis));
        } catch (PouceError | FractionError ignored) {
            System.out.println("valeur invalide");
        }
    }
}
