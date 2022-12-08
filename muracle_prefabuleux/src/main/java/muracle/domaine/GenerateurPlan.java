package muracle.domaine;

import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.FractionError;
import muracle.utilitaire.Pouce;
import muracle.utilitaire.PouceError;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class GenerateurPlan implements java.io.Serializable {

    private Pouce margeEpaisseurMateriaux = new Pouce("0");
    private Pouce margeLargeurReplis = new Pouce("1");
    private double anglePlis = 45;
    private Pouce longueurPlis = new Pouce("1");

    public GenerateurPlan() throws FractionError, PouceError {
    }

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

    public void genererPlans(Salle salle, XMLStreamWriter writer) {
        try {
            writer.writeStartElement("svg");
            writer.writeAttribute("xmlns", "http://www.w3.org/2000/svg");

            //example du plan d'un mur

            writer.writeStartElement("text");
            writer.writeAttribute("x", "20");
            writer.writeAttribute("y", "20");
            writer.writeAttribute("fill", "black");
            writer.writeCharacters("N1");
            writer.writeEndElement();

            //nom

            //plan
            writer.writeEmptyElement("path");
            writer.writeAttribute("d", "M 400 100 L 400 300 L 500 300 L 500 100 z" +
                    "M 100 100 L 100 300 L 200 300 L 200 100 z");
            writer.writeAttribute("fill", "white");
            writer.writeAttribute("stroke", "black");
            writer.writeAttribute("stroke-width", "1");

            //plis
            writer.writeEmptyElement("path");
            writer.writeAttribute("d", "M 410 100 L 410 90 L 490 90 L 490 100 z" +
                    "M 410 300 L 410 310 L 490 310 L 490 300 z" +
                    "M 100 110 L 90 110 L 90 290 L 100 290 z" +
                    "M 200 110 L 210 110 L 210 290 L 200 290 z");
            writer.writeAttribute("fill", "white");
            writer.writeAttribute("stroke", "orange");
            writer.writeAttribute("stroke-width", "1");

            //replis
            writer.writeEmptyElement("path");
            writer.writeAttribute("d", "M 410 90 L 430 80 L 470 80 L 490 90 z" +
                    "M 410 310 L 430 320 L 470 320 L 490 310 z" +
                    "M 90 110 L 80 120 L 80 280 L 90 290 z" +
                    "M 210 110 L 220 120 L 220 280 L 210 290 z");
            writer.writeAttribute("fill", "white");
            writer.writeAttribute("stroke", "blue");
            writer.writeAttribute("stroke-width", "1");

            writer.writeEndDocument();

            writer.close();
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    public CoordPouce[][] genererCoordonees(Cote cote, int indexMur, Pouce epMurs) {
        return null;
    }
    public CoordPouce[][] genererCoordoneesAll() {
        return null;
    }

    public CoordPouce[][] genererCoordonees(Mur mur) {
        if (mur.isEstCoinDroit()){
            return null;
        }
        else if (mur.isEstCoinGauche())
        {
            return null;
        }

        return null;
    }
}
