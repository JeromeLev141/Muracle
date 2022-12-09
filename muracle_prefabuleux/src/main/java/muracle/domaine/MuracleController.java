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
import java.util.*;

public class MuracleController {

    private Salle salle;
    private char coteSelected;
    private int murSelected;
    private int accessoireSelected;
    private int separateurSelected;

    private boolean isVueDessus;
    private boolean isVueExterieur;

    private boolean isVuePlanDecoupage;
    private Pouce distLigneGrille;
    private boolean isGrilleShown;
    private String errorMessage;

    private String currentStateSave;
    private GenerateurPlan generateurPlan;
    private Stack<String> undoPile;
    private Stack<String> redoPile;

    private boolean isResizing;

    private CoordPouce dragRef;

    private Pouce accesPosRef;

    private static class Save implements java.io.Serializable{
        public Salle saveSalle;
        public GenerateurPlan saveGenerateurPlan;

        public char saveCoteSelected;

        public int saveMurSelected;

        public int saveAccessoireSelected;

        public int saveSeparateurSelected;

        public boolean saveIsVueDessus;

        public boolean saveIsVueExterieur;
        public Save(Salle saveSalle, GenerateurPlan saveGenerateurPlan, char saveCoteSelected,
                    int saveMurSelected, int saveAccessoireSelected, int saveSeparateurSelected, boolean saveIsVueDessus, boolean saveIsVueExterieur) {
            this.saveSalle = saveSalle;
            this.saveGenerateurPlan = saveGenerateurPlan;
            this.saveCoteSelected =saveCoteSelected;
            this.saveMurSelected = saveMurSelected;
            this.saveAccessoireSelected = saveAccessoireSelected;
            this.saveSeparateurSelected = saveSeparateurSelected;
            this.saveIsVueDessus = saveIsVueDessus;
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
                    new Pouce("144"), new Pouce("8"));
            for (Cote cote : salle.getTableauCote()) {
                cote.addSeparateur(new Pouce(36, 0, 1));
                cote.addSeparateur(new Pouce(72, 0, 1));
                cote.addSeparateur(new Pouce(108, 0, 1));
                if (cote.getLargeur().getEntier() == 288) {
                    cote.addSeparateur(new Pouce(144, 0, 1));
                    cote.addSeparateur(new Pouce(180, 0, 1));
                    cote.addSeparateur(new Pouce(216, 0, 1));
                    cote.addSeparateur(new Pouce(252, 0, 1));
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
        isVuePlanDecoupage = false;
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
                isVueDessus = true;
                isVueExterieur = true;
                isVuePlanDecoupage = false;
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
        fileChooser.setCurrentDirectory(new java.io.File("."));
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //
        // disable the "All files" option.
        //
        fileChooser.setAcceptAllFileFilterUsed(false);
        int returnValue = fileChooser.showSaveDialog(parent);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File dossierPlans = new File(fileChooser.getCurrentDirectory().getAbsolutePath() + "/" + fileChooser.getSelectedFile().getName() + "/Plans");
            int indexNumDossierMemeNom = 1;
            boolean dossiersValide = true;
            while (!dossierPlans.mkdir()) {
                if (indexNumDossierMemeNom == 10) { // pour eviter boucle infini en cas de bug
                    dossiersValide = false;
                    break;
                }
                dossierPlans = new File(fileChooser.getCurrentDirectory().getAbsolutePath() + "/" + fileChooser.getSelectedFile().getName() +
                        "/Plans (" + indexNumDossierMemeNom + ")");
                indexNumDossierMemeNom++;
            }

            File dossierN = new File(dossierPlans.getAbsolutePath() + "/North");
            dossiersValide = dossiersValide && dossierN.mkdir();
            File dossierS = new File(dossierPlans.getAbsolutePath() + "/South");
            dossiersValide = dossiersValide && dossierS.mkdir();
            File dossierE = new File(dossierPlans.getAbsolutePath() + "/East");
            dossiersValide = dossiersValide && dossierE.mkdir();
            File dossierW = new File(dossierPlans.getAbsolutePath() + "/West");
            dossiersValide = dossiersValide && dossierW.mkdir();

            if (dossiersValide && isSalleValid()) {
                File[] dossiers = {dossierN, dossierS, dossierE, dossierW};
                for (File dossierMur : dossiers) {
                    int indexMur = 0;
                    for (Mur mur : salle.getCote(dossierMur.getName().charAt(0)).getMurs(salle.getProfondeur(),
                            generateurPlan.getMargeEpaisseurMateriaux(), generateurPlan.getMargeLargeurReplis(),
                            generateurPlan.getLongueurPlis(), salle.getEpaisseurTrouRetourAir(), generateurPlan.getAnglePlis())) {
                        try {
                            File fichierExt = new File(dossierMur.getAbsolutePath() + "/" + dossierMur.getName().charAt(0) + indexMur + "Ext.svg");
                            fichierExt.createNewFile();
                            File fichierInt = new File(dossierMur.getAbsolutePath() + "/" + dossierMur.getName().charAt(0) + indexMur + "Int.svg");
                            fichierInt.createNewFile();

                            PlanPanneau[] plans = generateurPlan.genererCoordonees(salle.getCote(dossierMur.getName().charAt(0)).getAccessoires(), mur,
                                    salle.getProfondeur(),  salle.getEpaisseurTrouRetourAir());

                            XMLOutputFactory factory = XMLOutputFactory.newInstance();
                            XMLStreamWriter writer = factory.createXMLStreamWriter(Files.newOutputStream(fichierExt.toPath()));
                            writer.writeStartDocument("utf-8", "1.0");
                            generateurPlan.genererPlans(plans[0], writer);

                            writer = factory.createXMLStreamWriter(Files.newOutputStream(fichierInt.toPath()));
                            writer.writeStartDocument("utf-8", "1.0");
                            generateurPlan.genererPlans(plans[1], writer);
                        } catch (IOException | XMLStreamException e) {
                            throw new RuntimeException(e);
                        }
                        indexMur++;
                    }
                }
            }
            else
                setErrorMessage("Il y a eu un problème dans la création des dossiers de destination");
        }
    }

    public void fermerProjet(Component parent) {
        if (!undoPile.isEmpty()) {
            int result = JOptionPane.showConfirmDialog(parent,"Voulez-vous sauvegarder votre travail?\n" +
                            "Toutes modifications non-sauvegardées seront perdues.", "Attention",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if(result == JOptionPane.YES_OPTION)
                sauvegarderProjet(parent);
        }
    }

    private String makeSaveString() throws IOException {
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

    private void readChange(String saveString) throws IOException, ClassNotFoundException {
        byte [] bytes = Base64.getDecoder().decode(saveString);
        ObjectInputStream ois = new ObjectInputStream( new ByteArrayInputStream(bytes) );
        Save save = (Save) ois .readObject();
        salle = save.saveSalle;
        generateurPlan = save.saveGenerateurPlan;
        coteSelected = save.saveCoteSelected;
        murSelected = save.saveMurSelected;
        accessoireSelected = save.saveAccessoireSelected;
        separateurSelected = save.saveSeparateurSelected;
        isVueDessus = save.saveIsVueDessus;
        isVueExterieur = save.saveIsVueExterieur;
        isVuePlanDecoupage = isVuePlanDecoupage;
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

    public  SalleDTO getSalleReadOnly() { return new SalleDTO(getSalle()); }
    
    private Salle getSalle() {
        return salle;
    }

    public void interactComponent(CoordPouce coordPouce, boolean addSepMode, boolean addAccesMode, String type) {
        // manque les deux autres vues
        try {
            currentStateSave = makeSaveString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        murSelected = -1;
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
        Pouce posXVueCote = null;
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
        Cote cote = getSelectedCote();
        if (cote != null && posXVueCote != null) {
            boolean contientSep = false;
            Pouce jeu = new Pouce(1, 1, 2); // la largeur des lignes est de deux pouces (pixels) en zoom x1 + jeu de 1 sur 2
            for (Pouce sep : cote.getSeparateurs()) {
                if (posXVueCote.compare(sep.sub(jeu)) == 1 &&
                        posXVueCote.compare(sep.add(jeu)) == -1) {
                    selectSeparateur(cote.getSeparateurs().indexOf(sep));
                    contientSep = true;
                }
            }
            if (!contientSep && addSepMode) {
                addSeparateur(posXVueCote);
                selectSeparateur(cote.getSeparateurs().indexOf(posXVueCote));
            }
        }
        if (coteSelected != ' ' && separateurSelected == -1) {
            setIsVueDessus(false);
        }

        // reize salle
        if (posX.compare(coinX.add(ep).sub(4)) == 1 && posX.compare(coinX.add(ep).sub(4)) == 1)
            isResizing = true;
    }

    private void interactCoteComponent(CoordPouce coordPouce, boolean addSepMode, boolean addAccesMode, String type) throws FractionError, PouceError {
        Pouce jeu = new Pouce(1, 1, 2); // la largeur des lignes est de deux pouces (pixels) en zoom x1 + jeu de 1 sur 2
        // add interraction avec murs
        Cote cote = getSelectedCote();
        if (cote == null) // la methode ne devrait pas etre appelle si on n'est pas ne vue de cote (un cote est selectionne)
            return;
        Pouce posX = coordPouce.getX();
        Pouce posY = coordPouce.getY();
        if (!isVueExterieur) {
            posX = cote.getLargeur().sub(posX);
            coordPouce.setX(posX);
        }

        if (!addAccesMode && !addSepMode) {
            int indexMur = 0;
            for (Pouce sep : cote.getSeparateurs()) {
                if (posX.compare(sep.add(jeu)) == 1)
                    indexMur++;
            }
            selectMur(indexMur);
        }

        boolean contientAcces = false;
        for (Accessoire acces : cote.getAccessoires()) {
            Pouce jeuAcces = jeu.add(acces.getMarge());
            if (posX.compare(acces.getPosition().getX().sub(jeuAcces)) == 1 && posX.compare(acces.getPosition().getX().add(acces.getLargeur()).add(jeuAcces)) == -1) {
                if (posY.compare(acces.getPosition().getY().sub(jeuAcces)) == 1 && posY.compare(acces.getPosition().getY().add(acces.getHauteur()).add(jeuAcces)) == -1) {
                    if (!isVueExterieur || !acces.isInterieurOnly()) {
                        murSelected = -1;
                        selectAccessoire(cote.getAccessoires().indexOf(acces));
                        contientAcces = true;
                        // resize point
                        if ((!acces.getType().equals("Porte") && posY.compare(acces.getPosition().getY().add(acces.getHauteur()).sub(jeuAcces)) == 1) ||
                                (acces.getType().equals("Porte") && posY.compare(acces.getPosition().getY().add(jeuAcces)) == -1)) {
                            if (isVueExterieur && posX.compare(acces.getPosition().getX().add(acces.getLargeur()).add(acces.getMarge().mul(2)).sub(jeuAcces)) == 1) {
                                isResizing = true;
                            } else if (!isVueExterieur && posX.compare(acces.getPosition().getX().sub(acces.getMarge().mul(2)).add(jeuAcces)) == -1) {
                                isResizing = true;
                            }
                        }
                    }
                }
            }
        }
        if (!contientAcces && addAccesMode) {
            murSelected = -1;
            addAccessoire(type, coordPouce);
        }

        if (!addAccesMode) {
            boolean contientSep = false;
            for (Pouce sep : cote.getSeparateurs()) {
                if (posX.compare(sep.sub(jeu)) == 1 && posX.compare(sep.add(jeu)) == -1) {
                    murSelected = -1;
                    accessoireSelected = -1;
                    selectSeparateur(getSelectedCote().getSeparateurs().indexOf(sep));
                    contientSep = true;
                }
            }
            if (!contientSep && addSepMode) {
                murSelected = -1;
                accessoireSelected = -1;
                addSeparateur(posX);
                selectSeparateur(cote.getSeparateurs().indexOf(posX));
            }
        }
    }

    public void startDragging() {
        if (isSeparateurSelected()) {
            try {
                if (isVueExterieur)
                    dragRef = new CoordPouce(getSelectedSeparateur().copy(), new Pouce(0, 0 ,1));
                else
                    dragRef = new CoordPouce(Objects.requireNonNull(getSelectedCote()).getLargeur().sub(getSelectedSeparateur()), new Pouce(0, 0, 1));
            } catch (FractionError e) {
                throw new RuntimeException(e);
            }
        }
        else if (isAccessoireSelected()) {
            Cote cote = Objects.requireNonNull(getSelectedCote());
            Accessoire access = Objects.requireNonNull(getSelectedAccessoire());
            if (isResizing) {
                dragRef = new CoordPouce(access.getPosition().getX().add(access.getLargeur()), access.getPosition().getY().add(access.getHauteur()));
                if (!isVueExterieur) {
                    dragRef.setX(cote.getLargeur().sub(access.getPosition().getX()));
                    accesPosRef = cote.getLargeur().sub(access.getPosition().getX().add(access.getLargeur()));
                }
                if (access.getType().equals("Porte"))
                    dragRef.setY(access.getPosition().getY());
            }
            else {
                if (isVueExterieur)
                    dragRef = new CoordPouce(access.getPosition().getX().copy(), access.getPosition().getY().copy());
                else
                    dragRef = new CoordPouce(Objects.requireNonNull(getSelectedCote()).getLargeur().sub(access.getPosition().getX()), access.getPosition().getY().copy());
            }
        }
        else {
            // resize salle
            if (isResizing)
                dragRef = new CoordPouce(salle.getLargeur().copy(), salle.getLongueur().copy());
        }
    }

    public void dragging (CoordPouce decalCoord) {
        //salle resize
        if (isVueDessus && isResizing) {
            try {
                decalCoord.getX().round(64);
                decalCoord.getY().round(64);
            } catch (FractionError e) {
                throw new RuntimeException(e);
            }
            CoordPouce coord = new CoordPouce(dragRef.getX().sub(decalCoord.getX()), dragRef.getY().sub(decalCoord.getY()));
            try {
                Pouce min = new Pouce(48, 0, 1);
                if (coord.getX().compare(min) == -1)
                    coord.setX(min.copy());
                if (coord.getY().compare(min) == -1)
                    coord.setY(min.copy());
                salle.setLargeur(coord.getX());
                salle.setLongueur(coord.getY());
            } catch (CoteError | FractionError e) {
                setErrorMessage(e.getMessage());
            }
        }
        else {
            Cote cote = Objects.requireNonNull(getSelectedCote());
            try {
                decalCoord.getX().round(64);
                decalCoord.getY().round(64);
            } catch (FractionError e) {
                throw new RuntimeException(e);
            }
            if (isSeparateurSelected()) {
                Pouce pos;
                if (isVueDessus) {
                    if (coteSelected == 'N')
                        pos = dragRef.getX().add(decalCoord.getX());
                    else if (coteSelected == 'S')
                        pos = dragRef.getX().sub(decalCoord.getX());
                    else if (coteSelected == 'E')
                        pos = dragRef.getX().add(decalCoord.getY());
                    else
                        pos = dragRef.getX().sub(decalCoord.getY());
                } else
                    pos = dragRef.getX().sub(decalCoord.getX());

                //stopper pour manip plus fluide
                try {
                    Pouce coinGauche = new Pouce(1, 0, 1);
                    if (pos.compare(coinGauche) == -1) {
                        pos = coinGauche;
                    } else if (pos.compare(cote.getLargeur().sub(1)) == 1) {
                        pos = cote.getLargeur().sub(1);
                    }
                } catch (FractionError e) {
                    throw new RuntimeException(e);
                }
                dragSeparateur(pos);
            } else if (isAccessoireSelected()) {
                CoordPouce coord = new CoordPouce(dragRef.getX().sub(decalCoord.getX()), dragRef.getY().sub(decalCoord.getY()));

                Accessoire access = Objects.requireNonNull(getSelectedAccessoire());

                //stopper pour manip plus fluide
                try {
                    Pouce coinHGauche = new Pouce(1, 0, 1);
                    if (isResizing) {
                        if (!isVueExterieur) {
                            coord.setX(coord.getX().sub(accesPosRef));
                            if (coord.getX().compare(coinHGauche) == -1)
                                coord.setX(coinHGauche);
                            else if (coord.getX().add(accesPosRef).add(access.getMarge()).compare(cote.getLargeur().sub(1)) == 1)
                                coord.setX(cote.getLargeur().sub(1).sub(accesPosRef).sub(access.getMarge()));
                        } else {
                            coord.setX(coord.getX().sub(access.getPosition().getX()));
                            if (coord.getX().compare(coinHGauche) == -1)
                                coord.setX(coinHGauche);
                            else if (coord.getX().add(access.getPosition().getX()).add(access.getMarge()).compare(cote.getLargeur().sub(1)) == 1)
                                coord.setX(cote.getLargeur().sub(access.getMarge()).sub(1).sub(access.getPosition().getX()));
                        }
                        if (access.getType().equals("Porte")) {
                            coord.setY(cote.getHauteur().sub(coord.getY()));
                            if (coord.getY().compare(coinHGauche) == -1)
                                coord.setY(coinHGauche);
                            else if (coord.getY().compare(cote.getHauteur().sub(1)) == 1)
                                coord.setY(cote.getHauteur().sub(1));
                        } else {
                            coord.setY(coord.getY().sub(access.getPosition().getY()));
                            if (coord.getY().compare(coinHGauche) == -1)
                                coord.setY(coinHGauche);
                            else if (coord.getY().add(access.getMarge()).add(access.getPosition().getY()).compare(cote.getHauteur().sub(1)) == 1)
                                coord.setY(cote.getHauteur().sub(1).sub(access.getPosition().getY()).sub(access.getMarge()));
                        }
                    } else {
                        if (isVueExterieur) {
                            if (coord.getX().sub(access.getMarge()).compare(coinHGauche) == -1)
                                coord.setX(coinHGauche.add(access.getMarge()));
                            else if (coord.getX().add(access.getLargeur().add(access.getMarge())).compare(cote.getLargeur().sub(1)) == 1) {
                                coord.setX(cote.getLargeur().sub(access.getLargeur().add(access.getMarge()).add(1)));
                            }
                        } else {
                            if (coord.getX().compare(coinHGauche.add(access.getLargeur()).add(access.getMarge())) == -1)
                                coord.setX(coinHGauche.add(access.getLargeur()).add(access.getMarge()));
                            else if (coord.getX().sub(access.getMarge()).compare(cote.getLargeur().sub(1)) == 1)
                                coord.setX(cote.getLargeur().sub(access.getMarge().add(1)));
                        }
                        if (coord.getY().sub(access.getMarge()).compare(coinHGauche) == -1)
                            coord.setY(coinHGauche.add(access.getMarge()));
                        else if (coord.getY().add(access.getHauteur()).add(access.getMarge()).compare(cote.getHauteur().sub(1)) == 1)
                            coord.setY(cote.getHauteur().sub(access.getHauteur().add(access.getMarge()).add(1)));
                    }
                } catch (FractionError e) {
                    throw new RuntimeException(e);
                }
                dragAccessoire(coord);
            }
        }
    }

    public void endDraggging (CoordPouce decalCoord) {
        try {
            isResizing = false;
            decalCoord.getX().round(64);
            decalCoord.getY().round(64);
            CoordPouce coord = new CoordPouce(dragRef.getX().sub(decalCoord.getX()), dragRef.getY().sub(decalCoord.getY()));
            if (!coord.getX().equals(dragRef.getX()) || !coord.getY().equals(dragRef.getY()))
                pushNewChange(currentStateSave);
        } catch (IOException | FractionError e) {
            throw new RuntimeException(e);
        }
    }

    private void selectCote(char orientation) {
        coteSelected = orientation;
    }

    public CoteDTO getSelectedCoteReadOnly() { return new CoteDTO(Objects.requireNonNull(getSelectedCote()),
            salle.getProfondeur(), generateurPlan.getMargeEpaisseurMateriaux(), generateurPlan.getMargeLargeurReplis(),
            generateurPlan.getLongueurPlis(), salle.getEpaisseurTrouRetourAir(), generateurPlan.getAnglePlis()); }

    public CoteDTO getCoteReadOnly(char orientation) { return new CoteDTO(salle.getCote(orientation),
            salle.getProfondeur(), generateurPlan.getMargeEpaisseurMateriaux(), generateurPlan.getMargeLargeurReplis(),
            generateurPlan.getLongueurPlis(), salle.getEpaisseurTrouRetourAir(), generateurPlan.getAnglePlis()); }
    private Cote getSelectedCote() {
        if (coteSelected != ' ')
            return salle.getCote(coteSelected);
        return null;
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

    public void setIsVueExterieur(boolean exterieur) {
        isVueExterieur = exterieur;
        if (isAccessoireSelected())
            if (Objects.requireNonNull(getSelectedAccessoire()).isInterieurOnly())
                accessoireSelected = -1;
    }

    public boolean isVueExterieur() {return isVueExterieur;}

    public void setIsVuePlanDecoupage(boolean vuePlanDecoupage) {
        isVuePlanDecoupage = vuePlanDecoupage;
    }

    public boolean isVuePlanDecoupage() {return isVuePlanDecoupage;}

    public boolean isVueCote() { return !isVueDessus();}

    public boolean isMurSelected() { return murSelected != -1; }

    public boolean isAccessoireSelected() { return accessoireSelected != -1; }

    public boolean isSeparateurSelected() { return separateurSelected != -1; }

    private void selectMur(int index) {murSelected = index;}

    public MurDTO getSelectedMurReadOnly() { return new MurDTO(Objects.requireNonNull(getSelectedMur())); }

    private Mur getSelectedMur() {
        if (murSelected != -1)
            return Objects.requireNonNull(getSelectedCote()).getMurs(salle.getProfondeur(), generateurPlan.getMargeEpaisseurMateriaux(), generateurPlan.getMargeLargeurReplis(),
                    generateurPlan.getLongueurPlis(), salle.getEpaisseurTrouRetourAir(), generateurPlan.getAnglePlis()).get(murSelected);
        return null;
    }

    public int getIndexOfSelectedMur() {
        return murSelected;
    }

    private void selectAccessoire(int index) {
        accessoireSelected = index;
    }

    public AccessoireDTO getSelectedAccessoireReadOnly() {
        return new AccessoireDTO(Objects.requireNonNull(getSelectedAccessoire()));
    }

    private Accessoire getSelectedAccessoire() {
        if (accessoireSelected != -1)
            return Objects.requireNonNull(getSelectedCote()).getAccessoire(accessoireSelected);
        return null;
    }

    public int getIndexOfSelectedAccessoire() {
        return accessoireSelected;
    }

    public void setDistLigneGrille(String dist) {
        try {
            if (!dist.contains("-"))
                if (!dist.equals(distLigneGrille.toString()))
                    distLigneGrille = new Pouce(dist);
            else setErrorMessage("La valeur entrée ne doit pas être négative");
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
            if (!largeur.contains("-") && !longueur.contains("-") && !hauteur.contains("-") && !profondeur.contains("-")) {
                if (!largeur.equals(salle.getLargeur().toString())) {
                    salle.setLargeur(new Pouce(largeur));
                    pushNewChange(save);
                }
                if (!longueur.equals(salle.getLongueur().toString())) {
                    salle.setLongueur(new Pouce(longueur));
                    pushNewChange(save);
                }
                if (!hauteur.equals(salle.getHauteur().toString())) {
                    salle.setHauteur(new Pouce(hauteur));
                    pushNewChange(save);
                }
                if (!profondeur.equals(salle.getProfondeur().toString())) {
                    salle.setProfondeur(new Pouce(profondeur));
                    pushNewChange(save);
                }
            } else setErrorMessage("La valeur entrée ne doit pas être négative");
        } catch (CoteError | SalleError | PouceError | FractionError e) {
            setErrorMessage(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addAccessoire(String type, CoordPouce position) {
        try {
            Accessoire acces;
            Cote cote = Objects.requireNonNull(getSelectedCote());
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
                    if (cote.getSeparateurs().size() != 0) {
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

            cote.addAccessoire(acces);
            selectAccessoire(getSelectedCote().getAccessoires().size() - 1);
            pushNewChange(currentStateSave);
        } catch (FractionError | PouceError | CoteError e) {
            setErrorMessage(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeAccessoire() {
        try {
            String save = makeSaveString();
            Objects.requireNonNull(getSelectedCote()).removeAccessoire(getSelectedAccessoire());
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
                Cote cote = Objects.requireNonNull(getSelectedCote());
                Accessoire acces = Objects.requireNonNull(getSelectedAccessoire());
                if (!isVueExterieur) {
                    pouceX = cote.getLargeur().sub(pouceX.add(acces.getLargeur()));
                }
                if (!pouceX.equals(acces.getPosition().getX()) ||
                        !pouceY.equals(acces.getPosition().getY())) {
                    cote.moveAccessoire(acces, new CoordPouce(pouceX, pouceY));
                    pushNewChange(save);
                }
        } else setErrorMessage("La valeur entrée ne doit pas être négative");
        } catch(PouceError | FractionError | CoteError e){
            setErrorMessage(e.getMessage());
        } catch(IOException e){
            throw new RuntimeException(e);
        }
    }


    private void dragAccessoire(CoordPouce coord) {
        try {
            Accessoire acces = Objects.requireNonNull(getSelectedAccessoire());
            Cote cote = Objects.requireNonNull(getSelectedCote());
            if (isResizing) {
                Pouce largeur = coord.getX();
                Pouce hauteur = coord.getY();
                if (!isVueExterieur) {
                    Pouce decal = largeur.sub(acces.getLargeur());
                    acces.getPosition().setX(acces.getPosition().getX().sub(decal));
                }
                if (acces.getType().equals("Retour d'air")) {
                    cote.setAccessoire(acces, largeur, acces.getHauteur(), acces.getMarge());
                }
                else {
                    cote.setAccessoire(acces, largeur, hauteur, acces.getMarge());
                }
            }
            else {
                Pouce pouceX = coord.getX();
                Pouce pouceY = coord.getY();
                if (!acces.getType().equals("Retour d'air")) {
                    if (!isVueExterieur) {
                        pouceX = cote.getLargeur().sub(pouceX);
                    }
                    coord = new CoordPouce(pouceX, pouceY);
                    if (acces.getType().equals("Porte"))
                        coord.setY(acces.getPosition().getY());
                    cote.moveAccessoire(acces, coord);
                }
            }
        } catch (FractionError | PouceError | CoteError e) {
            setErrorMessage(e.getMessage());
        }
    }

    public void setDimensionAccessoire(String largeur, String hauteur, String marge) {
        Accessoire acces = Objects.requireNonNull(getSelectedAccessoire());
        Pouce posXAcces = acces.getPosition().getX();
        try {
            String save = makeSaveString();
            Cote cote = Objects.requireNonNull(getSelectedCote());
            if (!largeur.contains("-") && !hauteur.contains("-") && !marge.contains("-")) {
                if (!largeur.equals(acces.getLargeur().toString())) {
                    Pouce largAcces = new Pouce(largeur);
                    if (!isVueExterieur) {
                        Pouce decal = largAcces.sub(acces.getLargeur());
                        acces.getPosition().setX(posXAcces.sub(decal));
                    }
                    cote.setAccessoire(acces, new Pouce(largeur), new Pouce(hauteur), new Pouce(marge));
                    pushNewChange(save);
                }
                if (!hauteur.equals(acces.getHauteur().toString())) {
                    cote.setAccessoire(acces, new Pouce(largeur), new Pouce(hauteur), new Pouce(marge));
                    pushNewChange(save);
                }
                if (acces.getType().equals("Fenêtre")) {
                    cote.setAccessoire(acces, new Pouce(largeur), new Pouce(hauteur), new Pouce(marge));
                    pushNewChange(save);
                }
            } else setErrorMessage("La valeur entrée ne doit pas être négative");
        } catch (PouceError | FractionError | CoteError e) {
            setErrorMessage(e.getMessage());
            acces.getPosition().setX(posXAcces);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void selectSeparateur (int index) {
        separateurSelected = index;
    }

    public Pouce getSelectedSeparateur() {
        if (separateurSelected != -1)
            return Objects.requireNonNull(getSelectedCote()).getSeparateurs().get(separateurSelected);
        return null;
    }

    private void addSeparateur(Pouce pos) {
        try {
            Objects.requireNonNull(getSelectedCote()).addSeparateur(pos);
            pushNewChange(currentStateSave);
        } catch (CoteError e) {
            setErrorMessage(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeSeparateur() {
        try {
            String save = makeSaveString();
            Objects.requireNonNull(getSelectedCote()).deleteSeparateur(getSelectedCote().getSeparateurs().indexOf(getSelectedSeparateur()));
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
                Cote cote = Objects.requireNonNull(getSelectedCote());
                if (!isVueExterieur)
                    newSep = cote.getLargeur().sub(newSep);
                if (!cote.getSeparateur(separateurSelected).equals(newSep)) {
                    cote.setSeparateur(separateurSelected, newSep);
                    selectSeparateur(cote.getSeparateurs().indexOf(newSep));
                    pushNewChange(save);
                }
            } catch (PouceError | FractionError | CoteError e) {
                setErrorMessage(e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else setErrorMessage("La valeur entrée ne doit pas être négative");
    }

    private void dragSeparateur(Pouce pos) {
        try {
            Cote cote = Objects.requireNonNull(getSelectedCote());
            if (!isVueExterieur)
                pos = cote.getLargeur().sub(pos);
            cote.setSeparateur(separateurSelected, pos);
            selectSeparateur(cote.getSeparateurs().indexOf(pos));
        } catch (FractionError | CoteError e) {
            setErrorMessage(e.getMessage());
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
            if (!hauteur.contains("-") && !epaisseur.contains("-") && !distanceSol.contains("-")) {
                if (!hauteur.equals(salle.getHauteurRetourAir().toString())) {
                    salle.setHauteurRetourAir(new Pouce(hauteur));
                    pushNewChange(save);
                }
                if (!epaisseur.equals(salle.getEpaisseurTrouRetourAir().toString())) {
                    salle.setEpaisseurTrouRetourAir(new Pouce(epaisseur));
                    pushNewChange(save);
                }
                if (!distanceSol.equals(salle.getDistanceTrouRetourAir().toString())) {
                    salle.setDistanceTrouRetourAir(new Pouce(distanceSol));
                    pushNewChange(save);
                }
            } else setErrorMessage("La valeur entrée ne doit pas être négative");
        } catch (SalleError | PouceError | FractionError e) {
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
            if (!margeEpaisseur.contains("-") && !margeLargeur.contains("-") && !longueurPlis.contains("-")) {
                if (!margeEpaisseur.equals(generateurPlan.getMargeEpaisseurMateriaux().toString())) {
                    generateurPlan.setMargeEpaisseurMateriaux(new Pouce(margeEpaisseur));
                    pushNewChange(save);
                }
                if (!margeLargeur.equals(generateurPlan.getMargeLargeurReplis().toString())) {
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
                if (!longueurPlis.equals(generateurPlan.getLongueurPlis().toString())) {
                    generateurPlan.setLongueurPlis(new Pouce(longueurPlis));
                    pushNewChange(save);
                }
            } else setErrorMessage("La valeur entrée ne doit pas être négative");
        } catch (PouceError | FractionError e) {
            setErrorMessage(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Pouce getSelectedAccesPosXInverse() {
        Accessoire acces = Objects.requireNonNull(getSelectedAccessoire());
        return Objects.requireNonNull(getSelectedCote()).getLargeur().sub(acces.getPosition().getX().add(acces.getLargeur()));
    }

    public Pouce getSelectedSepInverse() {
        return Objects.requireNonNull(getSelectedCote()).getLargeur().sub(getSelectedSeparateur());
    }

    public boolean isResizing() {
        return isResizing;
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

    public boolean isSalleValid(){
        for (int i = 0; i < salle.getTableauCote().length; i ++){
            ArrayList<Mur> murs = salle.getTableauCote()[i].getMurs(salle.getProfondeur(), generateurPlan.getMargeEpaisseurMateriaux(), generateurPlan.getMargeLargeurReplis(),
                    generateurPlan.getLongueurPlis(), salle.getEpaisseurTrouRetourAir(), generateurPlan.getAnglePlis());
            for (Mur mur : murs) {
                if (!(mur.getPanneauExt().isPoidsValid()) || !(mur.getPanneauInt().isPoidsValid())) {
                    return false;
                }
            }
            if (!(salle.getTableauCote()[i].isCoteAccessoireValid())){
                return false;
            }
        }
        return true;
    }
}
