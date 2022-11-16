package muracle.domaine.accessoire;

import muracle.domaine.Accessoire;
import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.Pouce;

public class Fenetre extends Accessoire {

    public Fenetre(){
        this.setType("Fenêtre");
        this.setInterieurOnly(Boolean.FALSE);
    }

    public Fenetre(Pouce largeur, Pouce hauteur, CoordPouce position){
        this.setType("Fenêtre");
        this.setInterieurOnly(Boolean.FALSE);
        this.setLargeur(largeur);
        this.setHauteur(hauteur);
        this.setPosition(position);
    }

}
