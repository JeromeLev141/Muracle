package muracle.domaine;

import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.FractionError;
import muracle.utilitaire.Pouce;
import muracle.utilitaire.PouceError;

import java.util.ArrayList;
import java.util.Objects;

public class Cote implements java.io.Serializable{
    private char orientation;
    private Pouce largeur;
    private Pouce hauteur;
    private ArrayList<Pouce> separateurs;
    private ArrayList<Accessoire> accessoires;

    public Cote(){

    }
    /**
     * @brief constructeur
     * @param orientation:char
     * @param largeur:Pouce
     * @param hauteur:Pouce
     */
    public Cote(char orientation, Pouce largeur, Pouce hauteur){
        this.orientation = orientation;
        this.largeur = largeur;
        this.hauteur = hauteur;
        separateurs = new ArrayList<>();
        accessoires = new ArrayList<>();
    }
    /**
     * @brief vérifie si les accessoires du côté sont valides
     * @return boolean
     */
    public boolean isCoteAccessoireValid(){
        for(int i = 0; i< accessoires.size(); i++){
            if (!(getAccessoire(i).isValid())){
                return false;
            }
        }
        return true;
    }

    /**
     * @brief getter de l'orientation
     * @return char
     */
    public char getOrientation() {
        return orientation;
    }
    /**
     * @brief setter de l'orientation
     * @param orientation:char
     */
    public void setOrientation(char orientation) {
        this.orientation = orientation;
    }
    /**
     * @brief getter de la largeur
     * @return char
     */
    public Pouce getLargeur() {
        return largeur;
    }
    /**
     * @brief vérifie si la nouvelle largeur va supprimer un accessoire et modifie les séparateurs selon la nouvelle largeur
     * @param largeur:Pouce
     * @return boolean
     */
    public boolean veriSetLargeurAndResizeSeparateur(Pouce largeur) throws CoteError, FractionError {
        if (!doesLargeurFitWithAccessories(largeur)){
            throw new CoteError("On ne peut modifier la salle car l'opération va supprimer un accessoire.");
        }else if(largeur.toDouble() < 0){
            throw new CoteError("On ne peut modifier la salle car la nouvelle largeur est plus petite que la vieille.");
        }else{
            resizeSeparateur(largeur);
            checkValidityForEveryAccessoire();
            return true;
        }
    }
    /**
     * @brief modifie les séparateurs selon la nouvelle largeur
     * @param largeur:Pouce
     */
    private void resizeSeparateur(Pouce largeur) throws FractionError {
        // resize
        for (int i = 0; i < this.separateurs.size(); i++){
            Pouce newValue = (getSeparateur(i).mul(largeur)).div(this.largeur);
            newValue.round(64);
            this.separateurs.set(i, newValue);
        }
    }
    /**
     * @brief setter de l'orientation
     * @param largeur:Pouce
     */
    public void setLargeur(Pouce largeur) {
        this.largeur = largeur;
        this.updateRetourAir();
    }
    /**
     * @brief setter de l'orientation
     * @param largeur:Pouce
     * @return boolean
     */
    private boolean doesLargeurFitWithAccessories(Pouce largeur) {
        for(int i = 0; i < this.accessoires.size(); i++){
            Double oldLargeurDouble = this.largeur.toDouble();
            Double newLargeurDouble = largeur.toDouble();
            Double UpperRightPointX = getAccessoire(i).getPosition().getX().toDouble() + getAccessoire(i).getLargeur().toDouble();
            if(newLargeurDouble <= UpperRightPointX && UpperRightPointX <= oldLargeurDouble){
                return false;
            }
        }
        return true;
    }
    public boolean doesLargeurFitWithSeparateurs(Pouce largeur){
        for (int i = 0; i <this.separateurs.size();i++){
            Double separateurDouble = getSeparateur(i).toDouble();
            Double oldLargeurDouble = this.largeur.toDouble();
            Double newLargeurDouble = largeur.toDouble();
            if(newLargeurDouble <= separateurDouble && separateurDouble <= oldLargeurDouble){
                return false;
            }
        }
        return true;
    }
    /**
     * @brief getter de la hauteur
     * @return Pouce
     */
    public Pouce getHauteur() {
        return hauteur;
    }
    /**
     * @brief getter d'un accessoire
     * @param index:int
     * @return Accessoire
     */
    public Accessoire getAccessoire(int index){
        return accessoires.get(index);
    }
    /**
     * @brief getter des accessoires
     * @return ArrayList<Accessoire>
     */
    public ArrayList<Accessoire> getAccessoires() {
        return accessoires;
    }
    /**
     * @brief setter de la largeur
     * @param accessoires:ArrayList<Accessoire>
     */
    public void setAccessoires(ArrayList<Accessoire> accessoires){
        this.accessoires = accessoires;
    }
    /**
     * @brief ajouter un accessoire à la list d'accessoire et vérifie la validité pour tous les accessoires
     * @param accessoire:Accessoire
     * @throws CoteError: Si accessoire ne rentre pas dans le côté
     */
    public void addAccessoire(Accessoire accessoire) throws FractionError, PouceError, CoteError {

        if(doesAccessoireFitInCote(accessoire)){
            accessoires.add(accessoire);
            checkValidityForEveryAccessoire();
        } else{
            throw new CoteError("Accessoire ne rentre pas dans le côté");
        }
    }
    /**
     * @brief Bouge la position d'un accessoire et vérifie la validité pour tous les accessoires
     * @param accessoire:Accessoire
     * @param positionPost:CoordPouce
     * @throws CoteError: Si accessoire ne rentre pas dans le côté
     */
    public void moveAccessoire(Accessoire accessoire, CoordPouce positionPost) throws FractionError, PouceError, CoteError {
        Accessoire dummyAccessoire = new Accessoire(accessoire);
        dummyAccessoire.setPosition(positionPost);
        if(doesAccessoireFitInCote(dummyAccessoire)){
            accessoire.setPosition(positionPost);
            checkValidityForEveryAccessoire();
        }else{
            throw new CoteError("Accessoire ne rentre pas dans le côté");
        }
    }
    /**
     * @brief Change les paramètres d'un accessoire et vérifie la validité pour tous les accessoires
     * @param accessoire:Accessoire
     * @param largeur:Pouce
     * @param hauteur:Pouce
     * @param marge:Pouce
     * @throws CoteError: Si accessoire ne rentre pas dans le côté
     */
    public void setAccessoire(Accessoire accessoire, Pouce largeur, Pouce hauteur, Pouce marge) throws FractionError, PouceError, CoteError {
        Pouce posX = accessoire.getPosition().getX();
        Pouce posY = accessoire.getPosition().getY();
        CoordPouce positionCopie = new CoordPouce(new Pouce(posX.getEntier(), posX.getFraction().copy()), new Pouce(posY.getEntier(), posY.getFraction().copy()));
        Accessoire dummyAccessoire = new Accessoire(largeur, hauteur, positionCopie);
        if (Objects.equals(accessoire.getType(), "Fenêtre")){
            dummyAccessoire.setMarge(marge);
        }
        Pouce difLargeur = largeur.sub(accessoire.getLargeur());
        if (accessoire.getType().equals("Retour d'air")) {
            dummyAccessoire.getPosition().setX(dummyAccessoire.getPosition().getX().add(difLargeur.div(2)));
        }
        Pouce difHauteur = hauteur.sub(accessoire.getHauteur());
        if (accessoire.getType().equals("Porte")) {
            dummyAccessoire.getPosition().setY(dummyAccessoire.getPosition().getY().sub(difHauteur));
        }
        if(doesAccessoireFitInCote(dummyAccessoire)){
            accessoire.setLargeur(largeur);
            accessoire.setHauteur(hauteur);
            if(Objects.equals(accessoire.getType(), "Fenêtre")){
                accessoire.setMarge(marge);
            }
            if (accessoire.getType().equals("Retour d'air")) {
                accessoire.getPosition().setX(accessoire.getPosition().getX().add(difLargeur.div(2)));
            }
            if (accessoire.getType().equals("Porte")) {
                accessoire.getPosition().setY(accessoire.getPosition().getY().sub(difHauteur));
            }
            checkValidityForEveryAccessoire();
        }else{
            throw new CoteError("Accessoire ne rentre pas dans le côté");
        }
    }
    /**
     * @brief Enlève un accessoire et vérifie la validité pour tous les accessoires
     * @param accessoire:Accessoire
     */
    public void removeAccessoire(Accessoire accessoire){
        accessoires.remove(accessoire);
        checkValidityForEveryAccessoire();
    }
    /**
     * @brief Vérifie la validité pour tous les accessoires avec doesAccessoireFitWithSeparateur() et doesAccessoireFitWithOtherAccessoires()
     */
    private void checkValidityForEveryAccessoire() {
        for (int i = 0; i< accessoires.size(); i++){
            getAccessoire(i).setIsValid(doesAccessoireFitWithOtherAccessoires(getAccessoire(i)) && doesAccessoireFitWithSeparateur(getAccessoire(i)));
        }
    }

    /**
     * @brief Vérifie si les accessoires ne rentre pas en collision avec les séparateurs
     * @param accessoire:Accessoire
     * @return boolean
     */
    public boolean doesAccessoireFitWithSeparateur(Accessoire accessoire){
       Double upperLeftPoint;
       Double upperRightPoint;
        if(Objects.equals(accessoire.getType(), "Fenêtre")){
            upperLeftPoint = accessoire.getPosition().getX().sub(accessoire.getMarge()).toDouble();
            upperRightPoint = accessoire.getPosition().getX().add(accessoire.getLargeur()).add(accessoire.getMarge()).toDouble();
        }else{
            upperLeftPoint = accessoire.getPosition().getX().toDouble();
            upperRightPoint = accessoire.getPosition().getX().add(accessoire.getLargeur()).toDouble();
        }
        for (int e = 0; e < separateurs.size(); e++) {
            double separateur = getSeparateur(e).toDouble();
            if (upperLeftPoint <= separateur && separateur <= upperRightPoint) {
                return false;
            }
        }
        return true;
    }
    /**
     * @brief Vérifie si les accessoires ne rentre pas en collision avec les autres accessoires
     * Suit cet algorithme : <a href="https://silentmatt.com/rectangle-intersection/">Rectangle intersection algorithm</a>
     * @param accessoire:Accessoire
     * @return boolean
     */
    public boolean doesAccessoireFitWithOtherAccessoires(Accessoire accessoire) {
        CoordPouce mainAccessoireUpperLeftPoint;
        CoordPouce mainAccessoireLowerRightPoint;
        if(Objects.equals(accessoire.getType(), "Fenêtre")){
            mainAccessoireUpperLeftPoint = new CoordPouce(accessoire.getPosition().getX().sub(accessoire.getMarge()),
                    accessoire.getPosition().getY().sub(accessoire.getMarge()));

            mainAccessoireLowerRightPoint = new CoordPouce((accessoire.getPosition().getX().add(accessoire.getLargeur()).add(accessoire.getMarge())),
                    (accessoire.getPosition().getY().add(accessoire.getHauteur())).add(accessoire.getMarge()));
        }else{
            mainAccessoireUpperLeftPoint = accessoire.getPosition();
            mainAccessoireLowerRightPoint = new CoordPouce(accessoire.getPosition().getX().add(accessoire.getLargeur()), accessoire.getPosition().getY().add(accessoire.getHauteur()));
        }
        if(!accessoires.isEmpty()){
            for(int i = 0; i < accessoires.size(); i++){
                if(accessoire != getAccessoire(i)){
                    CoordPouce secondAccessoireUpperLeftPoint;
                    CoordPouce secondAccessoireLowerRightPoint;
                    if(Objects.equals(getAccessoire(i).getType(), "Fenêtre")){
                        secondAccessoireUpperLeftPoint =  new CoordPouce(getAccessoire(i).getPosition().getX().sub(getAccessoire(i).getMarge()),
                                getAccessoire(i).getPosition().getY().sub(getAccessoire(i).getMarge()));

                        secondAccessoireLowerRightPoint =  new CoordPouce((getAccessoire(i).getPosition().getX().add(getAccessoire(i).getLargeur()).add(getAccessoire(i).getMarge())),
                                (getAccessoire(i).getPosition().getY().add(getAccessoire(i).getHauteur())).add(getAccessoire(i).getMarge()));
                    }else{
                        secondAccessoireUpperLeftPoint = getAccessoire(i).getPosition();
                        secondAccessoireLowerRightPoint = new CoordPouce(getAccessoire(i).getPosition().getX().add(getAccessoire(i).getLargeur()), getAccessoire(i).getPosition().getY().add(getAccessoire(i).getHauteur()));
                    }
                    if((mainAccessoireUpperLeftPoint.getX().compare(secondAccessoireLowerRightPoint.getX()) == -1) &&
                            mainAccessoireLowerRightPoint.getX().compare(secondAccessoireUpperLeftPoint.getX()) == 1 &&
                            mainAccessoireUpperLeftPoint.getY().compare(secondAccessoireLowerRightPoint.getY()) == -1 &&
                            mainAccessoireLowerRightPoint.getY().compare(secondAccessoireUpperLeftPoint.getY()) == 1 ){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    /**
     * @brief Vérifie si les accessoires ne rentre pas en collision avec le côté
     * @param accessoire:Accessoire
     * @return boolean
     */
    public boolean doesAccessoireFitInCote(Accessoire accessoire) throws FractionError, PouceError {
        CoordPouce cote1 = new CoordPouce(new Pouce("0"), new Pouce("0"));
        CoordPouce cote2 = new CoordPouce(largeur, hauteur);
        CoordPouce accessoireUpperLeftPoint;
        CoordPouce accessoireLowerRightPoint;
        if(Objects.equals(accessoire.getType(), "Fenêtre")){
            accessoireUpperLeftPoint = new CoordPouce(accessoire.getPosition().getX().sub(accessoire.getMarge()), accessoire.getPosition().getY().sub(accessoire.getMarge()));
            accessoireLowerRightPoint =new CoordPouce((accessoire.getPosition().getX().add(accessoire.getLargeur()).add(accessoire.getMarge())), (accessoire.getPosition().getY().add(accessoire.getHauteur())).add(accessoire.getMarge()));
        }else{
            accessoireUpperLeftPoint = accessoire.getPosition();
            accessoireLowerRightPoint =new CoordPouce(accessoire.getPosition().getX().add(accessoire.getLargeur()), accessoire.getPosition().getY().add(accessoire.getHauteur()));
        }

        boolean isLeftValid = (cote1.getX().compare(accessoireUpperLeftPoint.getX()) == -1) || (cote1.getX().compare(accessoireUpperLeftPoint.getX()) == 0);
        boolean isRightValid = (cote2.getX().compare(accessoireLowerRightPoint.getX()) == 1) ||  (cote2.getX().compare(accessoireLowerRightPoint.getX()) == 0);
        boolean isTopValid = (cote1.getY().compare(accessoireUpperLeftPoint.getY()) == -1) || (cote1.getY().compare(accessoireUpperLeftPoint.getY()) == 0);
        boolean isBotValid = (cote2.getY().compare(accessoireLowerRightPoint.getY()) == 1) || (cote2.getY().compare(accessoireLowerRightPoint.getY()) == 0);

        return isRightValid && isLeftValid && isBotValid && isTopValid;
    }
    /**
     * @brief Vérifie si la nouvelle hauteur ne rentre pas en collision avec les accessoires
     * @param hauteur:Pouce
     * @throws CoteError: Si la modification supprime un accessoire.
     * @return boolean
     */
    public boolean verifSetHauteur(Pouce hauteur) throws CoteError {
        if(!doesHauteurfitWithAccessoires(hauteur)){
            throw new CoteError("On ne peut modifier la salle car l'opération va supprimer un accessoire.");
        }else{
            return true;
        }
    }
    /**
     * @brief setteur de hauteur
     * @param hauteur:Pouce
     */
    public void setHauteur(Pouce hauteur) {
        reposYAcces(this.hauteur, hauteur);
        this.hauteur = hauteur;
    }
    /**
     * @brief Vérifie si la nouvelle hauteur ne rentre pas en collision avec les accessoires
     * @param hauteur:Pouce
     * @return boolean
     */
    private boolean doesHauteurfitWithAccessoires(Pouce hauteur){
        for(int i = 0; i < this.accessoires.size(); i++){
            if (!getAccessoire(i).getType().equals("Retour d'air")) {
                double oldHauteurDouble = this.hauteur.toDouble();
                double newHauteurDouble = hauteur.toDouble();
                double upperY = oldHauteurDouble - getAccessoire(i).getPosition().getY().toDouble();
                if (upperY >= newHauteurDouble) {
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * @brief getteur de la liste de murs
     * @param epaisseur:Pouce
     * @param margeEp:Pouce
     * @param margeLargeurReplis:Pouce
     * @param longeurPlis:Pouce
     * @param epTrouRetourAir:Pouce
     * @param angleReplis:double
     * @return ArrayList<Mur>
     */
    public ArrayList<Mur> getMurs(Pouce epaisseur, Pouce margeEp, Pouce margeLargeurReplis,Pouce longeurPlis,Pouce epTrouRetourAir,double angleReplis) {
        ArrayList<Mur> murs = new ArrayList<>();

        if(separateurs.size() == 0){
            try{
            Mur murSimpleCoin = new Mur(this.largeur,this.hauteur,epaisseur,margeEp,margeLargeurReplis,longeurPlis,true,angleReplis,null);
            Mur murDoubleCoin = new Mur(this.largeur,this.hauteur,epaisseur,margeEp,margeLargeurReplis,longeurPlis,true,angleReplis,new Pouce(0,0,1));
            Mur murNonCoin = new Mur(this.largeur,this.hauteur,epaisseur,margeEp,margeLargeurReplis,longeurPlis,false,angleReplis,null);
            double diffPoidsInt = murSimpleCoin.getPanneauInt().getPoids() - murNonCoin.getPanneauInt().getPoids();
            double diffPoidsExt = murSimpleCoin.getPanneauExt().getPoids() - murNonCoin.getPanneauExt().getPoids();

            diffPoidsInt += murSimpleCoin.getPanneauInt().getPoids();
            diffPoidsExt += murSimpleCoin.getPanneauExt().getPoids();

            murDoubleCoin.getPanneauInt().setPoids(diffPoidsInt);
            murDoubleCoin.getPanneauExt().setPoids(diffPoidsExt);

            murDoubleCoin.setEstCoinGauche(true);
            murDoubleCoin.setEstCoinDroit(true);
            murs.add(murDoubleCoin);
            }catch (FractionError ignored){}
        }
        else {
            for (int i = 0 ; i < separateurs.size(); i++) {
                if(i == 0){
                    try {
                        Mur m = new Mur(separateurs.get(i), this.hauteur, epaisseur, margeEp, margeLargeurReplis, longeurPlis, true, angleReplis, new Pouce(0,0,1));
                        m.setEstCoinGauche(true);
                        murs.add(m);
                    }catch (FractionError ignored){}
                }
                if(i + 1 == separateurs.size()){
                    if(separateurs.size() != 1 ){
                        Mur m = new Mur(separateurs.get(i).sub(separateurs.get(i-1)),this.hauteur,epaisseur,margeEp,margeLargeurReplis,longeurPlis,false,angleReplis,separateurs.get(i-1));
                        murs.add(m);
                    }
                    Mur m2 = new Mur(largeur.sub(separateurs.get(i)),this.hauteur,epaisseur,margeEp,margeLargeurReplis,longeurPlis,true,angleReplis,separateurs.get(i));
                    m2.setEstCoinDroit(true);
                    murs.add(m2);
                }else if (i != 0){
                    Mur m = new Mur(separateurs.get(i).sub(separateurs.get(i-1)),this.hauteur,epaisseur,margeEp,margeLargeurReplis,longeurPlis,false,angleReplis,separateurs.get(i-1));
                    murs.add(m);
                }
            }
        }
        for (Accessoire accessoire: accessoires) {
            CoordPouce coinHautGauche =  accessoire.getPosition();
            int x = 0;
            if(separateurs.size() != 0){
                for (Pouce separateur : separateurs) {
                    if (coinHautGauche.getX().compare(separateur) == 1)
                        x++;
                }
            }
            Panneau panneauIntGauche = murs.get(x).getPanneauInt();
            Panneau panneauExtGauche = murs.get(x).getPanneauExt();

            if(accessoire.isInterieurOnly()){
                panneauIntGauche.soustrairePoidsAccessoire(accessoire.getHauteur(),accessoire.getLargeur(),
                        accessoire.getMarge(),accessoire.getType(),epTrouRetourAir,true,epaisseur,margeEp,longeurPlis);
            }else
            {
                panneauIntGauche.soustrairePoidsAccessoire(accessoire.getHauteur(),accessoire.getLargeur(),
                        accessoire.getMarge(),accessoire.getType(),epTrouRetourAir,true,epaisseur,margeEp,longeurPlis);

                panneauExtGauche.soustrairePoidsAccessoire(accessoire.getHauteur(),accessoire.getLargeur(),
                        accessoire.getMarge(),accessoire.getType(),epTrouRetourAir,false,epaisseur,margeEp,longeurPlis);

            }
        }
        return murs;
    }
    /**
     * @brief getteur de la liste des séparateurs
     * @return ArrayList<Pouce>
     */
    public ArrayList<Pouce> getSeparateurs() {
        return separateurs;
    }
    /**
     * @brief ajoute séparateur
     * @param position:Pouce
     * @throws CoteError: Si la position est en dehors du côté
     */
    public void addSeparateur(Pouce position) throws CoteError {
        if(position.compare(largeur) == -1 && !separateurs.contains(position)){
            separateurs.add(position);
            sortSeparateur();
            checkValidityForEveryAccessoire();
            this.updateRetourAir();
        }else{
            throw new CoteError("Position en dehors du côté");
        }
    }
    /**
     * @brief tri la liste de séparateur
     */
    private void sortSeparateur(){
        separateurs.sort(Pouce::compare);
    }
    /**
     * @brief Enlève un séparateur et vérifie la validité pour tous les accessoires
     * @param index:int
     */
    public void deleteSeparateur(int index){
        this.separateurs.remove(index);
        checkValidityForEveryAccessoire();
        this.updateRetourAir();
    }
    /**
     * @brief getteur de séparateur
     * @param index:int
     */
    public Pouce getSeparateur(int index){
        return this.separateurs.get(index);
    }
    /**
     * @brief Modifie un séparateur et vérifie la validité pour tous les accessoires
     * @param index:int
     * @param position:Pouce
     * @throws CoteError: Si séparateur dépasse la largeur du côté
     * @throws CoteError: Si séparateur chevauche un séparateur
     */
    public void setSeparateur(int index, Pouce position) throws CoteError, FractionError {
        if(!separateurs.contains(position)){
            if (position.compare(largeur) == -1 && position.compare(new Pouce(0, 0, 1)) == 1){
                separateurs.remove(index);
                separateurs.add(position);
                sortSeparateur();
                checkValidityForEveryAccessoire();
                this.updateRetourAir();
            }
            else throw new CoteError("Le separateur ne doit pas dépasser la largeur du côté");
        }else throw new CoteError("Le separateur ajouté chevauche un separateur. Il ne sera pas rajouté");

    }
    /**
     * @brief Repositionne la valeur Y de la posisition de chaque accessoire
     * @param ancienneH:Pouce
     * @param nouvelleH:Pouce
     */
    private void reposYAcces(Pouce ancienneH, Pouce nouvelleH) {
        Pouce difH = nouvelleH.sub(ancienneH);
        for (Accessoire acces : accessoires) {
            acces.getPosition().setY(acces.getPosition().getY().add(difH));
        }
    }
    /**
     * @brief Centre le retour d'air
     * @param x1:Pouce
     * @param x2:Pouce
     * @param access:Accessoire
     */
    private void centrerRetourAir(Pouce x1, Pouce x2, Accessoire access){
        try {
            Pouce posiX = x1.add(x2.sub(x1).divRef(2));
            Pouce largeurAccess = access.getLargeur().div(2);
            access.getPosition().setX(posiX.subRef(largeurAccess));
            access.setIsValid(doesAccessoireFitWithOtherAccessoires(access) && doesAccessoireFitWithSeparateur(access));
        } catch (PouceError ignored) {}
    }
    /**
     * @brief Centre le retour d'air
     */
    private void updateRetourAir(){
        for (Accessoire acces:accessoires) {
            if (!acces.getType().equals("Retour d'air"))
                continue;
            Pouce posiCentreX = acces.getPosition().getX();
            try{
                posiCentreX = posiCentreX.add(acces.getLargeur().div(2));
            } catch (PouceError ignored) {}

            if (separateurs.size() == 0) {
                try {
                    centrerRetourAir(new Pouce(0, 0, 1), this.largeur, acces);
                } catch (FractionError ignored) {}
                continue;
            }
            else if (separateurs.size() == 1){
                if (posiCentreX.compare(separateurs.get(0)) == -1)
                    try {
                        centrerRetourAir(new Pouce(0, 0, 1), separateurs.get(0), acces);
                    }catch (FractionError ignored){}
                else
                    centrerRetourAir(separateurs.get(0),this.largeur,acces);
                continue;
            }

            for (int x = 0; x < separateurs.size();x++){
                if (x==0){
                    if(posiCentreX.compare(separateurs.get(x)) == -1)
                        try{
                            centrerRetourAir(new Pouce(0,0,1),separateurs.get(x),acces);
                        }catch (FractionError ignored){}

                    else if(posiCentreX.compare(separateurs.get(x)) >= 0 &&
                            posiCentreX.compare(separateurs.get(x+1)) == -1)
                        centrerRetourAir(separateurs.get(x),separateurs.get(x+1),acces);

                }
                else if(x == separateurs.size()-1){
                    if(posiCentreX.compare(separateurs.get(x)) >= 0)
                        centrerRetourAir(separateurs.get(x),this.largeur,acces);


                }
                else if (posiCentreX.compare(separateurs.get(x)) >= 0 &&
                        posiCentreX.compare(separateurs.get(x+1)) == -1){
                    centrerRetourAir(separateurs.get(x),separateurs.get(x+1),acces);
                }
            }
        }
    }
}
