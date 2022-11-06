package muracle.domaine.accessoire;

import muracle.domaine.Accessoire;
import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.Pouce;

public class RetourAir extends Accessoire {

    public RetourAir(){
        this.setType("RetourAir");
        this.setInterieurOnly(Boolean.TRUE);
    }

    public RetourAir(Pouce largeur, Pouce hauteur, CoordPouce position){
        this.setType("RetourAir");
        this.setInterieurOnly(Boolean.TRUE);
        this.setLargeur(largeur);
        this.setHauteur(hauteur);
        this.setPosition(position);
    }
}
