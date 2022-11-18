package muracle.domaine;

import muracle.domaine.accessoire.Fenetre;
import muracle.domaine.accessoire.Porte;
import muracle.domaine.accessoire.PriseElec;
import muracle.domaine.accessoire.RetourAir;
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
import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Objects;
import java.util.Scanner;
import java.util.Stack;

public class MuracleController {

    private Salle salle;
    private char coteSelected;
    private int murSelected;
    private int accessoireSelected;
    private int separateurSelected;

    private boolean isVueDessus;
    private boolean isVueExterieur;
    private Pouce distLigneGrille;
    private boolean isGrilleShown;

    private String errorMessage;
    private GenerateurPlan generateurPlan;
    private Stack<String> undoPile;
    private Stack<String> redoPile;

    private static class Save implements java.io.Serializable{
        public Salle saveSalle;
        public GenerateurPlan saveGenerateurPlan;

        public char saveCoteSelected;

        public int saveMurSelected;

        public int saveAccessoireSelected;

        public int saveSeparateurSelected;

        public boolean isVueDessus;

        public boolean saveIsVueExterieur;
        public Save(Salle saveSalle, GenerateurPlan saveGenerateurPlan, char saveCoteSelected,
                    int saveMurSelected, int saveAccessoireSelected, int saveSeparateurSelected, boolean isVueDessus, boolean saveIsVueExterieur) {
            this.saveSalle = saveSalle;
            this.saveGenerateurPlan = saveGenerateurPlan;
            this.saveCoteSelected =saveCoteSelected;
            this.saveMurSelected = saveMurSelected;
            this.saveAccessoireSelected = saveAccessoireSelected;
            this.saveSeparateurSelected = saveSeparateurSelected;
            this.isVueDessus = isVueDessus;
            this.saveIsVueExterieur = saveIsVueExterieur;
        }
    }

    public MuracleController() throws FractionError, PouceError {
        creerProjet();
        distLigneGrille = new Pouce("12");
        isGrilleShown = false;
        errorMessage = "";
        generateurPlan = new GenerateurPlan();
    }

    public void creerProjet() {
        try {
            salle = new Salle(new Pouce("288"), new Pouce("108"),
                    new Pouce("144"), new Pouce("12"));
            for (Cote cote : salle.getTableauCote()) {
                cote.addSeparateur(new Pouce(48, 0, 1));
                cote.addSeparateur(new Pouce(96, 0, 1));
                if (cote.getLargeur().getEntier() == 288) {
                    cote.addSeparateur(new Pouce(144, 0, 1));
                    cote.addSeparateur(new Pouce(192, 0, 1));
                    cote.addSeparateur(new Pouce(240, 0, 1));
                }
            }
            generateurPlan = new GenerateurPlan();
        } catch (FractionError | PouceError | CoteError e) {
            throw new RuntimeException(e);
        }
        coteSelected = ' ';
        murSelected = -1;
        accessoireSelected = -1;
        separateurSelected = -1;
        isVueDessus = true;
        isVueExterieur = true;
        undoPile = new Stack<>();
        redoPile = new Stack<>();
    }

    public void ouvrirProjet(Component parent) {
        fermerProjet(parent);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Ouverture de Projet");
        fileChooser.setFileFilter(new FileNameExtensionFilter("*.mrc", "mrc"));
        int returnValue = fileChooser.showOpenDialog(parent);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File fichier = fileChooser.getSelectedFile();
            try {
                Scanner scan = new Scanner(fichier);
                StringBuilder sb = new StringBuilder();
                while(scan.hasNext()) {
                    sb.append(scan.next());
                }
                scan.close();
                readChange(sb.toString());
                undoPile.clear();
                redoPile.clear();
                coteSelected = ' ';
                murSelected = -1;
                accessoireSelected = -1;
                separateurSelected = -1;
                isVueExterieur = true;
            } catch (ClassNotFoundException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sauvegarderProjet(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Sauvegarde de Projet");
        fileChooser.setFileFilter(new FileNameExtensionFilter("*.mrc", "mrc"));
        int returnValue = fileChooser.showSaveDialog(parent);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File fichier = fileChooser.getSelectedFile();
            if(!fileChooser.getSelectedFile().getAbsolutePath().endsWith(".mrc"))
                fichier = new File(fileChooser.getSelectedFile() + ".mrc");
            try(FileWriter fw = new FileWriter(fichier)) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(new Save(salle, generateurPlan, coteSelected, murSelected, accessoireSelected,
                        separateurSelected, isVueDessus, isVueExterieur));
                oos.close();
                fw.write(Base64.getEncoder().encodeToString(baos.toByteArray()));
            } catch (Exception except) {
                except.printStackTrace();
            }
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
                XMLStreamWriter writer = factory.createXMLStreamWriter(Files.newOutputStream(fichier.toPath()));
                writer.writeStartDocument();
                generateurPlan.genererPlans(salle, writer);

                /*writer.writeStartElement("svg");
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
                writer.writeAttribute("stroke-width", "1");*/

                writer.writeEndDocument();

                writer.close();

            } catch (IOException | XMLStreamException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void fermerProjet(Component parent) {
        if (!undoPile.isEmpty()) {
            int result = JOptionPane.showConfirmDialog(parent,"Voulez-vous sauvergarder votre travail?\n" +
                            "Toutes modifications non-sauvegardées seront perdues.", "Attention",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if(result == JOptionPane.YES_OPTION)
                sauvegarderProjet(parent);
        }
    }

    public String makeSaveString() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(new Save(salle, generateurPlan, coteSelected, murSelected, accessoireSelected,
                separateurSelected, isVueDessus, isVueExterieur));
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    private void pushChange(Stack<String> pile) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(new Save(salle, generateurPlan, coteSelected, murSelected, accessoireSelected,
                separateurSelected, isVueDessus, isVueExterieur));
        oos.close();
        pile.push(Base64.getEncoder().encodeToString(baos.toByteArray()));
    }

    private void pushNewChange(String saveString) throws IOException {
        undoPile.push(saveString);
        redoPile.clear();
    }

    private void readChange(String salleEnString) throws IOException, ClassNotFoundException {
        byte [] bytes = Base64.getDecoder().decode(salleEnString);
        ObjectInputStream ois = new ObjectInputStream( new ByteArrayInputStream(bytes) );
        Save save = (Save) ois .readObject();
        salle = save.saveSalle;
        generateurPlan = save.saveGenerateurPlan;
        coteSelected = save.saveCoteSelected;
        murSelected = save.saveMurSelected;
        accessoireSelected = save.saveAccessoireSelected;
        separateurSelected = save.saveSeparateurSelected;
        isVueDessus = save.isVueDessus;
        isVueExterieur = save.saveIsVueExterieur;
        ois.close();
    }

    public void undoChange() throws IOException, ClassNotFoundException {
        if (undoPile.size() != 0) {
            pushChange(redoPile);
            readChange(undoPile.pop());
        }
    }

    public void redoChange() throws IOException, ClassNotFoundException {
        if (redoPile.size() != 0) {
            pushChange(undoPile);
            readChange(redoPile.pop());
        }
    }

    public  SalleDTO getSalleReadOnly() { return new SalleDTO(salle); }
    private Salle getSalle() {
        return salle;
    }

    public void interactComponent(CoordPouce coordPouce, boolean addSepMode, boolean addAccesMode, String type) {
        // manque les deux autres vues
        separateurSelected = -1;
        accessoireSelected = -1;
        if (coordPouce != null) {
            if (isVueDessus) {
                try {
                    interactSalleComponent(coordPouce, addSepMode);
                } catch (FractionError | PouceError e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    interactCoteComponent(coordPouce, addSepMode, addAccesMode, type);
                } catch (FractionError | PouceError e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void interactSalleComponent(CoordPouce coordPouce, boolean addSepMode) throws FractionError, PouceError {
        coteSelected = ' ';
        Pouce posX = coordPouce.getX();
        Pouce posY = coordPouce.getY();
        Pouce ep = salle.getProfondeur(); // epaisseur mur
        Pouce coinX = salle.getProfondeur().add(salle.getLargeur()); // coin interieur haut droit
        Pouce coinY = salle.getProfondeur().add(salle.getLongueur()); // coin interieur bas gauche
        Pouce posXVueCote = new Pouce(1, 0, 1); //temporaire
        if (posX.compare(ep) == 1 && posX.compare(coinX) == -1) {
            if (posY.compare(ep) == -1) {
                selectCote('N');
                posXVueCote = coinX.sub(posX);
            }
            else if (posY.compare(coinY) == 1) {
                selectCote('S');
                posXVueCote = posX.sub(ep);
            }
        }
        else if (posY.compare(ep) == 1 && posY.compare(coinY) == -1) {
            if (posX.compare(ep) == -1) {
                selectCote('W');
                posXVueCote = posY.sub(ep);
            }
            else if (posX.compare(coinX) == 1) {
                selectCote('E');
                posXVueCote = coinY.sub(posY);
            }
        }
        if (coteSelected != ' ') {
            boolean contientSep = false;
            for (Pouce sep : getSelectedCote().getSeparateurs()) {
                Pouce jeu = new Pouce(1, 0, 1); // la largeur des lignes est de deux pouces (pixels) en zoom x1
                if (posXVueCote.compare(sep.sub(jeu)) == 1 &&
                        posXVueCote.compare(sep.add(jeu)) == -1) {
                    selectSeparateur(getSelectedCote().getSeparateurs().indexOf(sep));
                    contientSep = true;
                }
            }
            if (!contientSep && addSepMode) {
                addSeparateur(posXVueCote);
                selectSeparateur(getSelectedCote().getSeparateurs().indexOf(posXVueCote));
            }
        }
        if (coteSelected != ' ' && separateurSelected == -1) {
            setIsVueDessus(false);
        }
    }

    private void interactCoteComponent(CoordPouce coordPouce, boolean addSepMode, boolean addAccesMode, String type) throws FractionError, PouceError {
        // add interraction avec murs
        Pouce posX = coordPouce.getX();
        Pouce posY = coordPouce.getY();
        if (!isVueExterieur) {
            posX = getSelectedCote().getLargeur().sub(posX);
            coordPouce.setX(posX);
        }

        boolean contientAcces = false;
        for (Accessoire acces : getSelectedCote().getAccessoires()) {
            if (posX.compare(acces.getPosition().getX()) == 1 && posX.compare(acces.getPosition().getX().add(acces.getLargeur())) == -1) {
                if (posY.compare(acces.getPosition().getY()) == 1 && posY.compare(acces.getPosition().getY().add(acces.getHauteur())) == -1) {
                    selectAccessoire(getSelectedCote().getAccessoires().indexOf(acces));
                    contientAcces = true;
                }
            }
        }
        if (!contientAcces && addAccesMode) {
            addAccessoire(type, coordPouce);
        }

        if (!addAccesMode) {
            boolean contientSep = false;
            for (Pouce sep : getSelectedCote().getSeparateurs()) {
                Pouce jeu = new Pouce(1, 0, 1); // la largeur des lignes est de deux pouces (pixels) en zoom x1
                if (posX.compare(sep.sub(jeu)) == 1 && posX.compare(sep.add(jeu)) == -1) {
                    accessoireSelected = -1;
                    selectSeparateur(getSelectedCote().getSeparateurs().indexOf(sep));
                    contientSep = true;
                }
            }
            if (!contientSep && addSepMode) {
                addSeparateur(posX);
                selectSeparateur(getSelectedCote().getSeparateurs().indexOf(posX));
            }
        }
    }

    public void selectCote(char orientation) {
        coteSelected = orientation;
    }

    public CoteDTO getSelectedCoteReadOnly() { return new CoteDTO(Objects.requireNonNull(getSelectedCote())); }
    private Cote getSelectedCote() {
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

    public void setIsVueDessus(boolean dessus) {
        if (dessus) {
            coteSelected = ' ';
            separateurSelected = -1;
            accessoireSelected = -1;
            murSelected = -1;
        }
        isVueDessus = dessus;
    }
    public boolean isVueDessus() {return isVueDessus; }

    public boolean isVueCote() { return !isVueDessus();}

    public boolean isMurSelected() { return murSelected != -1; }

    public boolean isAccessoireSelected() { return accessoireSelected != -1; }

    public boolean isSeparateurSelected() { return separateurSelected != -1; }

    public void selectMur(int index) {
        murSelected = index;
    }

    public MurDTO getSelectedMurReadOnly() { return new MurDTO(Objects.requireNonNull(getSelectedMur())); }
    private Mur getSelectedMur() {
        if (murSelected != -1)
            return getSelectedCote().getMurs().get(murSelected);
        return null;
    }

    public void selectAccessoire(int index) {
        accessoireSelected = index;
    }

    public AccessoireDTO getSelectedAccessoireReadOnly() {
        return new AccessoireDTO(getSelectedAccessoire());
    }
    private Accessoire getSelectedAccessoire() {
        if (accessoireSelected != -1)
            return getSelectedCote().getAccessoire(accessoireSelected);
        return null;
    }

    public int getIndexOfSelectedAccessoire() {
        return accessoireSelected;
    }

    public void setDistLigneGrille(String dist) {
        try {
            if (!dist.contains("-") && !dist.equals(distLigneGrille.toString()))
                distLigneGrille = new Pouce(dist);
        } catch (PouceError | FractionError e) {
            setErrorMessage(e.getMessage());
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

    public String getDimensionSalle(int indexConfig) {
        String configValue = "";
        switch (indexConfig) {
            case 0 :
                configValue = salle.getLargeur().toString();
                break;
            case 1 :
                configValue = salle.getLongueur().toString();
                break;
            case 2 :
                configValue = salle.getHauteur().toString();
                break;
            case 3 :
                configValue = salle.getProfondeur().toString();
                break;
        }
        return  configValue;
    }
    public void setDimensionSalle(String largeur, String longueur, String hauteur, String profondeur) {
        try {
            String save = makeSaveString();
            if (!largeur.contains("-") && !largeur.equals(salle.getLargeur().toString())) {
                salle.setLargeur(new Pouce(largeur));
                pushNewChange(save);
            }
            if (!longueur.contains("-") && !longueur.equals(salle.getLongueur().toString())) {
                salle.setLongueur(new Pouce(longueur));
                pushNewChange(save);
            }
            if (!hauteur.contains("-") && !hauteur.equals(salle.getHauteur().toString())) {
                salle.setHauteur(new Pouce(hauteur));
                pushNewChange(save);
            }
            if (!profondeur.contains("-") && !profondeur.equals(salle.getProfondeur().toString())) {
                salle.setProfondeur(new Pouce(profondeur));
                pushNewChange(save);
            }
        } catch (PouceError | FractionError e) {
            setErrorMessage(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addAccessoire(String type, CoordPouce position) {
        try {
            String save = makeSaveString();
            Accessoire acces;
            switch (type) {
                case "Fenêtre":
                    Fenetre fenetre = new Fenetre(new Pouce(18, 0, 1), new Pouce(24, 0, 1), position);
                    fenetre.setMarge(new Pouce(0, 1, 8));
                    acces = fenetre;
                    //recentrage du clic
                    acces.getPosition().setX(acces.getPosition().getX().sub(acces.getLargeur().div(2)));
                    acces.getPosition().setY(acces.getPosition().getY().sub(acces.getHauteur().div(2)));
                    break;
                case "Porte":
                    Pouce hauteurStandart = new Pouce(88, 0, 1);
                    position.setY(salle.getHauteur().sub(hauteurStandart));
                    acces = new Porte(new Pouce(38, 0, 1), hauteurStandart, position);
                    //recentrage du clic seulement en x
                    acces.getPosition().setX(acces.getPosition().getX().sub(acces.getLargeur().div(2)));
                    break;
                case "Prise électrique":
                    acces = new PriseElec(new Pouce(2, 0, 1), new Pouce(4, 0, 1), position);
                    //recentrage du clic
                    acces.getPosition().setX(acces.getPosition().getX().sub(acces.getLargeur().div(2)));
                    acces.getPosition().setY(acces.getPosition().getY().sub(acces.getHauteur().div(2)));
                    break;
                default:
                    Pouce debutMur = new Pouce(0, 0, 1);
                    int indexSepSuivant = 0;
                    if (getSelectedCote().getSeparateurs().size() != 0) {
                        for (Pouce sep : getSelectedCote().getSeparateurs()) {
                            if (position.getX().compare(sep) == 1) {
                                debutMur = sep;
                                indexSepSuivant++;
                            }
                        }
                    }
                    Pouce finMur;
                    if (indexSepSuivant == getSelectedCote().getSeparateurs().size())
                        finMur = getSelectedCote().getLargeur();
                    else
                        finMur = getSelectedCote().getSeparateur(indexSepSuivant);

                    Pouce largeurStandart = new Pouce(32, 0, 1);
                    Pouce largeurMur = finMur.sub(debutMur);
                    position.setX(debutMur.add(largeurMur.sub(largeurStandart).div(2)));
                    position.setY(salle.getHauteur().sub(salle.getDistanceTrouRetourAir().add(salle.getHauteurRetourAir())));
                    acces = new RetourAir(largeurStandart, salle.getHauteurRetourAir(), position);
                    break;
            }

            getSelectedCote().addAccessoire(acces);
            selectAccessoire(getSelectedCote().getAccessoires().size() - 1);
            pushNewChange(save);
        } catch (FractionError | PouceError | CoteError e) {
            setErrorMessage(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeAccessoire() {
        try {
            String save = makeSaveString();
            getSelectedCote().removeAccessoire(getSelectedAccessoire());
            accessoireSelected = -1;
            pushNewChange(save);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void moveAccessoire(String posX, String posY) {
        try {
            String save = makeSaveString();
            if (!posX.contains("-") && !posY.contains("-")) {
                Pouce pouceX = new Pouce(posX);
                Pouce pouceY = new Pouce(posY);
                if (!pouceX.equals(getSelectedAccessoire().getPosition().getX()) ||
                        !pouceY.equals(getSelectedAccessoire().getPosition().getY())) {
                    getSelectedCote().moveAccessoire(getSelectedAccessoire(), new CoordPouce(pouceX, pouceY));
                    //getSelectedAccessoire().setPosition(new CoordPouce(pouceX, pouceY));
                    pushNewChange(save);
                }
        }
        } catch(PouceError | FractionError | CoteError e){
            setErrorMessage(e.getMessage());
        } catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    public void setDimensionAccessoire(String largeur, String hauteur, String marge) {
        try {
            String save = makeSaveString();
            if (!largeur.contains("-") && !largeur.equals(getSelectedAccessoire().getLargeur().toString())) {
                Pouce difLargeur = new Pouce(largeur).sub(getSelectedAccessoire().getLargeur());
                getSelectedAccessoire().setLargeur(new Pouce(largeur));
                if (getSelectedAccessoire().getType().equals("Retour d'air"))
                    getSelectedAccessoire().getPosition().setX(getSelectedAccessoire().getPosition().getX().sub(difLargeur.div(2)));
                pushNewChange(save);
            }
            if (!hauteur.contains("-") && !hauteur.equals(getSelectedAccessoire().getHauteur().toString())) {
                getSelectedAccessoire().setHauteur(new Pouce(hauteur));
                pushNewChange(save);
            }
            if (!marge.contains("-") && getSelectedAccessoire().getType().equals("Fenêtre")) {
                getSelectedAccessoire().setMarge(new Pouce(marge));
                pushNewChange(save);
            }
        } catch (PouceError | FractionError e) {
            setErrorMessage(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void selectSeparateur (int index) {
        separateurSelected = index;
    }

    public Pouce getSelectedSeparateur() {
        if (separateurSelected != -1)
            return getSelectedCote().getSeparateurs().get(separateurSelected);
        return null;
    }

    public void addSeparateur(Pouce pos) {
        try {
            String save = makeSaveString();
            getSelectedCote().addSeparateur(pos);
            pushNewChange(save);
        } catch (CoteError e) {
            setErrorMessage(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeSeparateur() {
        try {
            String save = makeSaveString();
            getSelectedCote().deleteSeparateur(getSelectedCote().getSeparateurs().indexOf(getSelectedSeparateur()));
            separateurSelected = -1;
            pushNewChange(save);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void moveSeparateur(String position) {
        if (!position.contains("-")) {
            try {
                String save = makeSaveString();
                Pouce newSep = new Pouce(position);
                if (!getSelectedCote().getSeparateur(separateurSelected).equals(newSep)) {
                    getSelectedCote().setSeparateur(separateurSelected, newSep);
                    selectSeparateur(getSelectedCote().getSeparateurs().indexOf(newSep));
                    pushNewChange(save);
                }
            } catch (PouceError | FractionError | CoteError e) {
                setErrorMessage(e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getParametreRetourAir(int indexParam) {
        String paramValue = "";
        switch (indexParam) {
            case 0 :
                paramValue = salle.getHauteurRetourAir().toString();
                break;
            case 1 :
                paramValue = salle.getEpaisseurTrouRetourAir().toString();
                break;
            case 2 :
                paramValue = salle.getDistanceTrouRetourAir().toString();
                break;
        }
        return  paramValue;
    }

    public void setParametreRetourAir(String hauteur, String epaisseur, String distanceSol) {
        try {
            String save = makeSaveString();
            if (!hauteur.contains("-") && !hauteur.equals(salle.getHauteurRetourAir().toString())) {
                salle.setHauteurRetourAir(new Pouce(hauteur));
                pushNewChange(save);
            }
            if (!epaisseur.contains("-") && !epaisseur.equals(salle.getEpaisseurTrouRetourAir().toString())) {
                salle.setEpaisseurTrouRetourAir(new Pouce(epaisseur));
                pushNewChange(save);
            }
            if (!distanceSol.contains("-") && !distanceSol.equals(salle.getDistanceTrouRetourAir().toString())) {
                salle.setDistanceTrouRetourAir(new Pouce(distanceSol));
                pushNewChange(save);
            }
        } catch (PouceError | FractionError e) {
            setErrorMessage(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            String save = makeSaveString();
            if (!margeEpaisseur.contains("-") && !margeEpaisseur.equals(generateurPlan.getMargeEpaisseurMateriaux().toString())) {
                generateurPlan.setMargeEpaisseurMateriaux(new Pouce(margeEpaisseur));
                pushNewChange(save);
            }
            if (!margeLargeur.contains("-") && !margeLargeur.equals(generateurPlan.getMargeLargeurReplis().toString())) {
                generateurPlan.setMargeLargeurReplis(new Pouce(margeLargeur));
                pushNewChange(save);
            }
            try {
                double angle = Double.parseDouble(anglePlis);
                if (0 <= angle && angle <= 90) {
                    if (angle != generateurPlan.getAnglePlis()) {
                        generateurPlan.setAnglePlis(angle);
                        pushNewChange(save);
                    }
                } else
                    setErrorMessage("L'angle doit être entre 0 et 90 degrée");
            } catch (NumberFormatException e) {
                setErrorMessage("Caractères alphabétiques détectés");
            }
            if (!longueurPlis.contains("-") && !longueurPlis.equals(generateurPlan.getLongueurPlis().toString())) {
                generateurPlan.setLongueurPlis(new Pouce(longueurPlis));
                pushNewChange(save);
            }
        } catch (PouceError | FractionError e) {
            setErrorMessage(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
    public void ackErrorMessage() {
        errorMessage = "";
    }
}
