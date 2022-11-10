package muracle.domaine;

import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.FractionError;
import muracle.utilitaire.Pouce;
import muracle.utilitaire.PouceError;

import javax.xml.stream.XMLStreamWriter;

public class GenerateurPlan implements java.io.Serializable {

    private Pouce margeEpaisseurMateriaux = new Pouce("0");
    private Pouce margeLargeurReplis = new Pouce("1");
    private double anglePlis = 45;
    private Pouce longueurPlis = new Pouce("1");

    public GenerateurPlan() throws FractionError, PouceError {
    }

    public GenerateurPlan(Pouce margeEpMat, Pouce margeLargRep, double angle, Pouce longPlis) throws FractionError, PouceError {}

    public Pouce getMargeEpaisseurMateriaux() {
        return margeEpaisseurMateriaux;
    }

    public void setMargeEpaisseurMateriaux(Pouce margeEpMat) {
        margeEpaisseurMateriaux = margeEpMat;
    }

    public Pouce getMargeLargeurReplis() {
        return margeLargeurReplis;
    }

    public void setMargeLargeurReplis(Pouce margeLargRep) {
        margeLargeurReplis = margeLargRep;
    }

    public double getAnglePlis() {
        return anglePlis;
    }

    public void setAnglePlis(double angle) {
        anglePlis = angle;
    }

    public Pouce getLongueurPlis() {
        return longueurPlis;
    }

    public void setLongueurPlis(Pouce longPlis) {
        longueurPlis = longPlis;
    }

    public void genererPlans(Salle salle, XMLStreamWriter writer) {}

    public CoordPouce[][] genererCoordonees() {
        return null;
    }
}
