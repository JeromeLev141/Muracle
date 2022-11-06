package muracle.domaine.accessoire;

import muracle.domaine.Accessoire;
import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.Pouce;

public class Fenetre extends Accessoire {

    private Pouce marge;

    public Fenetre(){
        this.setType("Fenetre");
        this.setInterieurOnly(Boolean.FALSE);
    }

    public Fenetre(Pouce largeur, Pouce hauteur, CoordPouce position){
        this.setType("Fenetre");
        this.setInterieurOnly(Boolean.FALSE);
        this.setLargeur(largeur);
        this.setHauteur(hauteur);
        this.setPosition(position);
    }

    public void setMarge(Pouce marge) {
        this.marge = marge;
    }
    public Pouce getMarge() {
        return marge;
    }
}
