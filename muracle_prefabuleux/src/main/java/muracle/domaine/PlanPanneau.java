package muracle.domaine;

import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.Pouce;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlanPanneau {
    private List<CoordPouce> polygone;
    private List<CoordPouce[]> lignePlis;
    private List<List<CoordPouce>> polygoneAccessoire;
    private boolean poidsValide;


    /**
     * @brief constructeur de la class PlanPanneau
     */
    PlanPanneau(){
        this.polygone = new ArrayList<>();
        this.lignePlis = new ArrayList<>();
        this.polygoneAccessoire = new ArrayList<>();
        this.poidsValide = false;
    }

    /**
     * @brief ajout un points dans la liste polygone
     * @param coordPouce: coordonnée du point
     */
    void ajoutPointPolygone(CoordPouce coordPouce){
        polygone.add(coordPouce);
    }

    /**
     * @brief ajout un tableau de 2 coordPouce qui représente une ligne de plie
     * @param coordPouces paire de 2 coordPouce pour une ligne de plie
     */
    void ajoutlignePlie(CoordPouce[] coordPouces){
        lignePlis.add(coordPouces);
    }

    /**
     * @brief ajout une list de CoordPouce qui représente un accessoir
     * @param accessoire: list<CoordPouce>
     */
    void ajoutAccessoire(List<CoordPouce> accessoire){
        polygoneAccessoire.add(accessoire);
    }

    /**
     * @brief getteur de l'attribut polygone
     * @return polygone: List<CoordPouce>
     */
    public List<CoordPouce> getPolygone(){
        return polygone;
    }

    /**
     * @brief getteur de l'attribut lignePlie
     * @return polygone: List<CoordPouce[]>
     */
    public List<CoordPouce[]> getLignePlis(){
        return lignePlis;
    }

    /**
     * @brief getteur de l'attribut polygoneAccessoire
     * @return polygone: List<List<CoordPouce>>
     */
    public List<List<CoordPouce>> getPolygoneAccessoire(){
        return polygoneAccessoire;
    }

    /**
     * @brief retourne la largeur du plan
     * @return Pouce
     */
    public Pouce getLargeur(){
        return polygone.stream().max(Comparator.comparing(i -> i.getX().toDouble())).get().getX();
    }

    /**
     * @brief retourne la hauteur du plan
     * @return Pouce
     */
    public Pouce getHauteur(){
        return polygone.stream().max(Comparator.comparing(i -> i.getY().toDouble())).get().getY();
    }

    /**
     * @brief retourne le boolean de la constante poidsValide
     * @return Bool
     */
    public Boolean isPoidsValide(){
        return this.poidsValide;
    }

    /**
     * @brief set le bool de la variable poidsValide
     * @param valide (bool): validité du panneau
     */
    void setPoidsValide(boolean valide){
        this.poidsValide = valide;
    }



    @Override
    public String toString(){
        return polygone.toString();
    }
}
