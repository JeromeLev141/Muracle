package muracle.domaine;

import muracle.utilitaire.Pouce;

import java.util.UUID;

public class PanneauDTO {

    private Pouce Largeur;
    private Pouce Hauteur;
    private double  Poids;
    public UUID id;

    public PanneauDTO(Panneau panneau){
        Largeur = panneau.getLargeur();
        Hauteur = panneau.getHauteur();
        Poids = panneau.getPoids();
    }

}
