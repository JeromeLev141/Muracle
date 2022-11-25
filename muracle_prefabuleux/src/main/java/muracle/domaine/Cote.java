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
    public Cote(char orientation, Pouce largeur, Pouce hauteur){
        this.orientation = orientation;
        this.largeur = largeur;
        this.hauteur = hauteur;
        separateurs = new ArrayList<>();
        accessoires = new ArrayList<>();
    }

    public char getOrientation() {
        return orientation;
    }

    public void setOrientation(char orientation) {
        this.orientation = orientation;
    }

    public Pouce getLargeur() {
        return largeur;
    }

    public boolean setLargeur(Pouce largeur) throws CoteError {
        if(!doesLargeurFitWithSeparateurs(largeur)){
            throw new CoteError("On ne peut modifier la salle car l'opération va supprimer un séparateur.");
        } else if (!doesLargeurFitWithAccessories(largeur)) {
            throw new CoteError("On ne peut modifier la salle car l'opération va supprimer un accessoire.");
        }else{
            this.largeur = largeur;
            this.updateRetourAir();
            return true;
        }
    }
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

    public Pouce getHauteur() {
        return hauteur;
    }
    public Accessoire getAccessoire(int index){
        return accessoires.get(index);
    }
    public ArrayList<Accessoire> getAccessoires() {
        return accessoires;
    }
    public void setAccessoires(ArrayList<Accessoire> accessoires){
        this.accessoires = accessoires;
    }

    public void addAccessoire(Accessoire accessoire) throws FractionError, PouceError, CoteError {

        if(doesAccessoireFitInCote(accessoire)){
            accessoires.add(accessoire);
            CheckValidityForEveryAccessoire();
        } else{
            throw new CoteError("Accessoire ne rentre pas dans le côté");
        }
    }
    public void moveAccessoire(Accessoire accessoire, CoordPouce positionPost) throws FractionError, PouceError, CoteError {
        Accessoire dummyAccessoire = new Accessoire(accessoire);
        dummyAccessoire.setPosition(positionPost);
        if(doesAccessoireFitInCote(dummyAccessoire)){
            accessoire.setPosition(positionPost);
            CheckValidityForEveryAccessoire();
        }else{
            throw new CoteError("Accessoire ne rentre pas dans le côté");
        }
    }
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
            dummyAccessoire.getPosition().setX(dummyAccessoire.getPosition().getX().sub(difLargeur.div(2)));
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
                accessoire.getPosition().setX(accessoire.getPosition().getX().sub(difLargeur.div(2)));
            }
            if (accessoire.getType().equals("Porte")) {
                accessoire.getPosition().setY(accessoire.getPosition().getY().sub(difHauteur));
            }
            CheckValidityForEveryAccessoire();
        }else{
            throw new CoteError("Accessoire ne rentre pas dans le côté");
        }
    }
    public void removeAccessoire(Accessoire accessoire){
        accessoires.remove(accessoire);
        CheckValidityForEveryAccessoire();
    }
    private void CheckValidityForEveryAccessoire() {
        for (int i = 0; i< accessoires.size(); i++){
            getAccessoire(i).setIsValid(doesAccessoireFitWithOtherAccessoires(getAccessoire(i)) && doesAccessoireFitWithSeparateur(getAccessoire(i)));
        }
    }
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
    // Suit cet algorithme
    // https://silentmatt.com/rectangle-intersection/
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
    public boolean doesAccessoireFitInCote(Accessoire accessoire) throws FractionError, PouceError {
        CoordPouce cote1 = new CoordPouce(new Pouce("0"), new Pouce("0"));
        CoordPouce cote2 = new CoordPouce(largeur, hauteur);
        CoordPouce accessoireUpperLeftPoint;
        CoordPouce accessoireLowerRightPoint;
        if(Objects.equals(accessoire.getType(), "Fenêtre")){
            //FIX ICI BAS point peut être 0 si la marge est zero
            /*if(accessoire.getPosition().getX().compare(new Pouce("0")) == 0 || accessoire.getPosition().getY().compare(new Pouce("0")) == 0){
                return false;
            }*/
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

    public boolean setHauteur(Pouce hauteur) throws CoteError {
        if(!doesHauteurfitWithAccessoires(hauteur)){
            throw new CoteError("On ne peut modifier la salle car l'opération va supprimer un accessoire.");
        }else{
            this.hauteur = hauteur;
            return true;
        }
    }
    private boolean doesHauteurfitWithAccessoires(Pouce hauteur){
        for(int i = 0; i < this.accessoires.size(); i++){
            Double oldHauteurDouble = this.hauteur.toDouble();
            Double newHauteurDouble = hauteur.toDouble();
            Double LowerY = getAccessoire(i).getPosition().getY().toDouble() + getAccessoire(i).getHauteur().toDouble();
            if(newHauteurDouble <= LowerY && LowerY <= oldHauteurDouble){
                return false;
            }
        }
        return true;
    }

    public ArrayList<Mur> getMurs(Pouce epaisseur, Pouce margeLargeurReplis,Pouce longeurPlis,Pouce epTrouRetourAir,double angleReplis) {
        ArrayList<Mur> murs = new ArrayList<>();

        if(separateurs.size() == 0){
            Mur m = new Mur(this.getLargeur(),hauteur);
            m.setEstCoinGauche(true);
            m.setEstCoinDroit(true);
            murs.add(m);
        }
        else {
            for (int i = 0 ; i < separateurs.size(); i++) {
                if(i == 0){
                    Mur m = new Mur(separateurs.get(i),this.hauteur,epaisseur,margeLargeurReplis,longeurPlis,true,angleReplis);
                    m.setEstCoinGauche(true);
                    murs.add(m);
                }else if(i + 1 == separateurs.size()){
                    if(separateurs.size() != 1 ){
                        Mur m = new Mur(separateurs.get(i).sub(separateurs.get(i-1)),this.hauteur,epaisseur,margeLargeurReplis,longeurPlis,false,angleReplis);
                        murs.add(m);
                    }
                    Mur m2 = new Mur(largeur.sub(separateurs.get(i)),this.hauteur,epaisseur,margeLargeurReplis,longeurPlis,true,angleReplis);
                    m2.setEstCoinDroit(true);
                    murs.add(m2);
                }else {
                    Mur m = new Mur(separateurs.get(i).sub(separateurs.get(i-1)),this.hauteur,epaisseur,margeLargeurReplis,longeurPlis,false,angleReplis);
                    murs.add(m);
                }
            }
            for (Accessoire accessoire: accessoires) {
                CoordPouce coinHautGauche =  accessoire.getPosition();
                CoordPouce coinHautDroit = new CoordPouce(coinHautGauche.getX().add(accessoire.getLargeur()),coinHautGauche.getY());
                int i = 0;
                while (separateurs.get(i).compare(coinHautGauche.getX()) == -1){
                    i++;
                }
                Panneau panneauIntGauche = murs.get(i).getPanneauInt();
                Panneau panneauExtGauche = murs.get(i).getPanneauExt();

                if(separateurs.get(i).compare(coinHautDroit.getX()) == -1) {
                    Panneau panneauIntDroit = murs.get(i+1).getPanneauInt();
                    Panneau panneauExtDroit = murs.get(i+1).getPanneauExt();

                    Pouce largeurGauche = accessoire.getLargeur().sub(separateurs.get(i).sub(coinHautGauche.getX()));
                    Pouce largeurDroite=  accessoire.getLargeur().sub(coinHautDroit.getX().sub(separateurs.get(i)));
                    if(accessoire.isInterieurOnly()){
                        panneauIntGauche.soustrairePoidsAccessoire(accessoire.getHauteur(),largeurGauche,
                                accessoire.getMarge(),accessoire.getType(),epTrouRetourAir);

                        panneauIntDroit.soustrairePoidsAccessoire(accessoire.getHauteur(),largeurDroite,
                                accessoire.getMarge(),accessoire.getType(),epTrouRetourAir);
                    }else
                    {
                        panneauIntGauche.soustrairePoidsAccessoire(accessoire.getHauteur(),largeurGauche,
                                accessoire.getMarge(),accessoire.getType(),epTrouRetourAir);

                        panneauIntDroit.soustrairePoidsAccessoire(accessoire.getHauteur(),largeurDroite,
                                accessoire.getMarge(),accessoire.getType(),epTrouRetourAir);

                        panneauExtGauche.soustrairePoidsAccessoire(accessoire.getHauteur(),largeurGauche,
                                accessoire.getMarge(),accessoire.getType(),epTrouRetourAir);

                        panneauExtDroit.soustrairePoidsAccessoire(accessoire.getHauteur(),largeurDroite,
                                accessoire.getMarge(),accessoire.getType(),epTrouRetourAir);
                    }
                }
                else {
                   if(accessoire.isInterieurOnly()){
                       panneauIntGauche.soustrairePoidsAccessoire(accessoire.getHauteur(),accessoire.getLargeur(),
                               accessoire.getMarge(),accessoire.getType(),epTrouRetourAir);
                   }else
                   {
                    panneauIntGauche.soustrairePoidsAccessoire(accessoire.getHauteur(),accessoire.getLargeur(),
                            accessoire.getMarge(),accessoire.getType(),epTrouRetourAir);

                    panneauExtGauche.soustrairePoidsAccessoire(accessoire.getHauteur(),accessoire.getLargeur(),
                            accessoire.getMarge(),accessoire.getType(),epTrouRetourAir);
                   }
                }
            }
        }
        return murs;
    }

    public ArrayList<Pouce> getSeparateurs() {
        return separateurs;
    }
    public void addSeparateur(Pouce position) throws CoteError {
        if(position.compare(largeur) == -1 || !separateurs.contains(position)){
            separateurs.add(position);
            sortSeparateur();
            CheckValidityForEveryAccessoire();
            this.updateRetourAir();
        }else{
            throw new CoteError("Position en dehors du côté");
        }
    }
    private void sortSeparateur(){
        separateurs.sort(Pouce::compare);
    }
    public void deleteSeparateur(int index){
        this.separateurs.remove(index);
        this.updateRetourAir();
    }
    public Pouce getSeparateur(int index){
        return this.separateurs.get(index);
    }

    public void setSeparateur(int index, Pouce position) throws CoteError {
        if(!separateurs.contains(position) || (position.compare(largeur) == -1)){
            separateurs.remove(index);
            separateurs.add(position);
            sortSeparateur();
            this.updateRetourAir();
        }else{
            throw new CoteError("Le separateur ajouté chevauche un separateur. Il ne sera pas rajouté");
        }

    }


    private void centrerRetourAir(Pouce x1, Pouce x2, Accessoire access){
        try {
            Pouce posiX = x1.add(x2.sub(x1).divRef(2));
            Pouce largeurAccess = access.getLargeur().div(2);
            access.getPosition().setX(posiX.subRef(largeurAccess));
            access.setIsValid(doesAccessoireFitWithOtherAccessoires(access) && doesAccessoireFitWithSeparateur(access));
        } catch (PouceError ignored) {}
    }

    private void updateRetourAir(){
        for (Accessoire acces:accessoires) {
            if (!acces.getType().equals("Retour d'air"))
                continue;
            Pouce posiCentreX = acces.getPosition().getX();
            try{
                posiCentreX = posiCentreX.add(acces.getLargeur().div(2));
            } catch (PouceError ignored) {}
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
                if (posiCentreX.compare(separateurs.get(x)) >= 0 &&
                        posiCentreX.compare(separateurs.get(x+1)) == -1){
                    centrerRetourAir(separateurs.get(x),separateurs.get(x+1),acces);
                }

            }
        }
    }
}
