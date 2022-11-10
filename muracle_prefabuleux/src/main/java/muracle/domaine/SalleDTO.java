package muracle.domaine;

import muracle.utilitaire.Pouce;

import java.util.UUID;

public class SalleDTO {

    public Cote[] TableauCote;
    public Pouce Largeur;
    public Pouce Longueur;
    public Pouce Hauteur;
    public Pouce Profondeur;
    public Pouce HauteurRetourAir;
    public Pouce EpaisseurTrouRetourAir;
    public Pouce DistanceTrouRetourAir;
    public UUID id;

    SalleDTO(Salle salle){
        TableauCote = salle.getTableauCote();
        Largeur = salle.getLargeur();
        Longueur = salle.getLongueur();
        Hauteur = salle.getHauteur();
        Profondeur = salle.getProfondeur();
        HauteurRetourAir = salle.getHauteurRetourAir();
        EpaisseurTrouRetourAir = salle.getEpaisseurTrouRetourAir();
        DistanceTrouRetourAir = salle.getDistanceTrouRetourAir();
    }
}
