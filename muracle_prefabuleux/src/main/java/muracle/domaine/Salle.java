package muracle.domaine;

import muracle.utilitaire.FractionError;
import muracle.utilitaire.Pouce;
import muracle.utilitaire.PouceError;

import java.util.Objects;

public class Salle implements java.io.Serializable{
    private Cote[] tableauCote;
    private Pouce largeur;
    private Pouce longueur;
    private Pouce hauteur;
    private Pouce profondeur;
    private Pouce hauteurRetourAir;
    private Pouce epaisseurTrouRetourAir;
    private Pouce distanceTrouRetourAir;
    /**
     * @brief constructeur vide
     */
    public Salle(){}

    /**
     * @brief constructeur de Salle
     * @param largeur:Pouce
     * @param hauteur:Pouce
     * @param longueur:Pouce
     * @param profondeur:Pouce
     */
    public Salle(Pouce largeur,Pouce hauteur, Pouce longueur, Pouce profondeur) throws FractionError, PouceError {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.longueur = longueur;
        this.profondeur = profondeur;
        this.distanceTrouRetourAir = new Pouce("10");
        this.epaisseurTrouRetourAir = new Pouce("4");
        this.hauteurRetourAir = new Pouce("12");

        tableauCote = new Cote[]{new Cote('S', largeur, hauteur), new Cote('E', longueur, hauteur),
        new Cote('N', largeur, hauteur), new Cote('W', longueur, hauteur)};
    }
    /**
     * @brief getter de tableauCote
     * @return Cote[]
     */
    public Cote[] getTableauCote() {
        return tableauCote;
    }
    /**
     * @brief getter d'un côté individuelle avec l'orientation
     * @param orientation:char
     * @return Cote
     */
    public Cote getCote(char orientation){
        for (Cote cote : this.tableauCote) {
            if (cote.getOrientation() == orientation) {
                return cote;
            }
        }
        return null;
    }
    /**
     * @brief getter de distanceTrouRetourAir
     * @return Pouce
     */
    public Pouce getDistanceTrouRetourAir() {
        return distanceTrouRetourAir;
    }
    /**
     * @brief setter de distanceTrouRetourAir
     * @param distanceTrouRetourAir: Pouce
     * @throws SalleError: Si la distance du sol des retours d'air additionné à la hauteur des retours d'air est plus grande que la hauteur.
     */
    public void setDistanceTrouRetourAir(Pouce distanceTrouRetourAir) throws SalleError {
        if(hauteurRetourAir.add(distanceTrouRetourAir).compare(hauteur) == -1){
            for (Cote cote : tableauCote) {
                for (int e = 0; e < cote.getAccessoires().size(); e++) {
                    if (Objects.equals(cote.getAccessoire(e).getType(), "Retour d'air")) {
                        Pouce accesH = cote.getAccessoire(e).getHauteur();
                        Pouce posY = cote.getHauteur().sub(distanceTrouRetourAir.add(accesH));
                        cote.getAccessoire(e).getPosition().setY(posY);
                    }
                }
            }
            this.distanceTrouRetourAir = distanceTrouRetourAir;
        }else{
            throw new SalleError("La distance du sol des retours d'air additionné à la hauteur des retours d'air ne peut pas être plus grande que la hauteur");
        }
    }
    /**
     * @brief getter de largeur
     * @return Pouce
     */
    public Pouce getLargeur(){
        return largeur;
    }
    /**
     * @brief setter de largeur
     * @param largeur: Pouce
     */
    public void setLargeur(Pouce largeur) throws CoteError, FractionError {
        if (getCote('N').veriSetLargeurAndResizeSeparateur(largeur) && getCote('S').veriSetLargeurAndResizeSeparateur(largeur)){
            getCote('N').setLargeur(largeur);
            getCote('S').setLargeur(largeur);
            this.largeur = largeur;
        }
    }
    /**
     * @brief getter de longueur
     * @return Pouce
     */
    public Pouce getLongueur() {
        return longueur;
    }
    /**
     * @brief setter de largeur
     * @param longueur: Pouce
     */
    public void setLongueur(Pouce longueur) throws CoteError, FractionError {
        if (getCote('E').veriSetLargeurAndResizeSeparateur(longueur) && getCote('W').veriSetLargeurAndResizeSeparateur(longueur)){
            getCote('E').setLargeur(longueur);
            getCote('W').setLargeur(longueur);
            this.longueur = longueur;
        }
    }
    /**
     * @brief getter d'epaisseurTrouRetourAir
     * @return Pouce
     */
    public Pouce getEpaisseurTrouRetourAir() {
        return epaisseurTrouRetourAir;
    }
    /**
     * @brief setter d'epaisseurTrouRetourAir
     * @param epaisseurTrouRetourAir: Pouce
     * @throws SalleError: Si l'épaisseur trou retour d'air est plus grande ou égale à l'épaisseur de la salle.
     */
    public void setEpaisseurTrouRetourAir(Pouce epaisseurTrouRetourAir) throws SalleError {
        if (epaisseurTrouRetourAir.compare(profondeur) == 1 || epaisseurTrouRetourAir.compare(profondeur) == 0){
            throw new SalleError("L'épaisseur trou retour d'air ne peut pas être plus grande ou égale à l'épaisseur de la salle: " + profondeur);
        }else{
            this.epaisseurTrouRetourAir = epaisseurTrouRetourAir;
        }
    }
    /**
     * @brief getter de hauteur
     * @return Pouce
     */
    public Pouce getHauteur() {
        return hauteur;
    }
    /**
     * @brief setter de hauteur
     * @param hauteur: Pouce
     * @throws SalleError: Si la distance du sol des retours d'air additionné à la hauteur des retours d'air est plus grande que la hauteur.
     */
    public void setHauteur(Pouce hauteur) throws SalleError, CoteError {
        if(hauteurRetourAir.add(distanceTrouRetourAir).compare(hauteur) == -1){
            if(getCote('E').verifSetHauteur(hauteur) && getCote('W').verifSetHauteur(hauteur) &&
                    getCote('N').verifSetHauteur(hauteur) && getCote('S').verifSetHauteur(hauteur)){
                getCote('E').setHauteur(hauteur);
                getCote('W').setHauteur(hauteur);
                getCote('N').setHauteur(hauteur);
                getCote('S').setHauteur(hauteur);
                this.hauteur = hauteur;
            }
        }else{
            throw new SalleError("La distance du sol des retours d'air additionné à la hauteur des retours d'air ne peut pas être plus grande que la hauteur");
        }
    }
    /**
     * @brief getter de hauteurRetourAir
     * @return Pouce
     */
    public Pouce getHauteurRetourAir() {
        return hauteurRetourAir;
    }
    /**
     * @brief setter de hauteurRetourAir
     * @param hauteurRetourAir: Pouce
     * @throws SalleError: Si la distance du sol des retours d'air additionné à la hauteur des retours d'air est plus grande que la hauteur.
     */
    public void setHauteurRetourAir(Pouce hauteurRetourAir) throws SalleError {
        if (hauteurRetourAir.add(distanceTrouRetourAir).compare(hauteur) == -1){
            for (Cote cote : tableauCote) {
                for (int e = 0; e < cote.getAccessoires().size(); e++) {
                    if (Objects.equals(cote.getAccessoire(e).getType(), "Retour d'air")) {
                        Pouce ancienneH = cote.getAccessoire(e).getHauteur();
                        cote.getAccessoire(e).setHauteur(hauteurRetourAir);
                        Pouce posY = cote.getAccessoire(e).getPosition().getY();
                        Pouce decalemetY = ancienneH.sub(hauteurRetourAir);
                        cote.getAccessoire(e).getPosition().setY(posY.add(decalemetY));
                    }
                }
            }
            this.hauteurRetourAir = hauteurRetourAir;
        }else {
            throw new SalleError("La distance du sol des retours d'air additionné à la hauteur des retours d'air ne peut pas être plus grande que la hauteur");
        }
    }
    /**
     * @brief getter de profondeur
     * @return Pouce
     */
    public Pouce getProfondeur() {
        return profondeur;
    }
    /**
     * @brief setter de largeur
     * @param profondeur: Pouce
     * @throws SalleError: Si l'épaisseur trou retour d'air est plus grande ou égale à l'épaisseur de la salle.
     */
    public void setProfondeur(Pouce profondeur) throws SalleError {
        if (epaisseurTrouRetourAir.compare(profondeur) == 1 || epaisseurTrouRetourAir.compare(profondeur) == 0){
            throw new SalleError("L'épaisseur trou retour d'air ne peut pas être plus grande ou égale à l'épaisseur de la salle: " + profondeur);
        }else{
            this.profondeur = profondeur;
        }
    }
}
