package muracle.domaine.accessoire;

import muracle.domaine.Accessoire;
import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.Pouce;

public class Porte extends Accessoire {

    public Porte(){
        this.setType("Porte");
        this.setInterieurOnly(Boolean.FALSE);
    }

    public Porte(Pouce largeur, Pouce hauteur, CoordPouce position){
        this.setType("Porte");
        this.setInterieurOnly(Boolean.FALSE);
        this.setLargeur(largeur);
        this.setHauteur(hauteur);
        this.setPosition(position);
    }

}
