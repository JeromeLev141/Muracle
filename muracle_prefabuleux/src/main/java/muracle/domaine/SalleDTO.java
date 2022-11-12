package muracle.domaine;

import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.Fraction;
import muracle.utilitaire.FractionError;
import muracle.utilitaire.Pouce;

import java.util.UUID;

public class SalleDTO {

    public Cote[] tableauCote;
    public Pouce largeur;
    public Pouce longueur;
    public Pouce hauteur;
    public Pouce profondeur;
    public Pouce hauteurRetourAir;
    public Pouce epaisseurTrouRetourAir;
    public Pouce distanceTrouRetourAir;
    public UUID id;

    SalleDTO(Salle salle){
        tableauCote = salle.getTableauCote();
        largeur = salle.getLargeur();
        longueur = salle.getLongueur();
        hauteur = salle.getHauteur();
        profondeur = salle.getProfondeur();
        hauteurRetourAir = salle.getHauteurRetourAir();
        epaisseurTrouRetourAir = salle.getEpaisseurTrouRetourAir();
        distanceTrouRetourAir = salle.getDistanceTrouRetourAir();
    }

    public CoteDTO getCote(char orientation){
        for (Cote cote : this.tableauCote) {
            if (cote.getOrientation() == orientation) {
                return new CoteDTO(cote);
            }
        }
        return null;
    }

    public CoordPouce getDimension() throws FractionError {
        return new CoordPouce(this.largeur.add(this.profondeur.mul(new Fraction(2,1))),
                this.longueur.add(this.profondeur.mul(new Fraction(2,1))));
    }
}
