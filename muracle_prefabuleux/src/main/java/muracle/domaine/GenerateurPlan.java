package muracle.domaine;

import muracle.utilitaire.*;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenerateurPlan implements java.io.Serializable {

    private Pouce margeEpaisseurMateriaux = new Pouce("1/4");
    private Pouce margeLargeurReplis = new Pouce("1/2");
    private double anglePlis = 45;
    private Pouce longueurPlis = new Pouce("2");

    /**
     * @brief constructeur de la classe GenerateurPlan
     * @throws FractionError n'occure jamais
     * @throws PouceError n'occure jamais
     */
    public GenerateurPlan() throws FractionError, PouceError {
    }

    /**
     * @brief getteur de la la constante MargeEpaisseurMateriaux
     * @return Pouce
     */
    public Pouce getMargeEpaisseurMateriaux() {
        return margeEpaisseurMateriaux;
    }

    /**
     * @brief setteur de la constante MargeEpaisseurMateriaux
     * @param margeEpMat (Pouce)
     */
    public void setMargeEpaisseurMateriaux(Pouce margeEpMat) {
        margeEpaisseurMateriaux = margeEpMat;
    }

    /**
     * @brief getteur de la constante margeLargeurReplis
     * @return (Pouce)
     */
    public Pouce getMargeLargeurReplis() {
        return margeLargeurReplis;
    }

    /**
     * @brief setteur de la constante MargeLargeurReplis
     * @param margeLargRep (Pouce)
     */
    public void setMargeLargeurReplis(Pouce margeLargRep) {
        margeLargeurReplis = margeLargRep;
    }

    /**
     * @brief getteur de la constante anglePlis
     * @return (double)
     */
    public double getAnglePlis() {
        return anglePlis;
    }

    /**
     * @brief setteur de la constante anglePlis
     * @param angle (double)
     */
    public void setAnglePlis(double angle) {
        anglePlis = angle;
    }

    /**
     * @brief getteur de la constante longueurPlis
     * @return (Pouce)
     */
    public Pouce getLongueurPlis() {
        return longueurPlis;
    }

    /**
     * @brief setteur de la constante longueurPlis
     * @param longPlis (Pouce): longueur des plis
     */
    public void setLongueurPlis(Pouce longPlis) {
        longueurPlis = longPlis;
    }

    /**
     * @brief Export le plan fourni en svg
     * @param plan (planPanneau): le panneau a export
     * @param writer (XMLStreamWriter): xmls writer
     */
    public void genererPlans(PlanPanneau plan, XMLStreamWriter writer) {
        try {
            writer.writeStartElement("svg");
            writer.writeAttribute("xmlns", "http://www.w3.org/2000/svg");
            double decal = 10;
            double multiple = 5;//Pouce 96

            //plan
            StringBuilder contourPanneau = new StringBuilder("M");
            for (CoordPouce coord : plan.getPolygone()) {
                contourPanneau.append(" ").append(decal + coord.getX().toDouble()*multiple).append(" ").append(decal + coord.getY().toDouble()*multiple).append(" L");
            }
            contourPanneau.setCharAt(contourPanneau.length() - 1, 'z');
            writer.writeEmptyElement("path");
            writer.writeAttribute("d", contourPanneau.toString());
            writer.writeAttribute("fill", "white");
            writer.writeAttribute("stroke", "black");
            writer.writeAttribute("stroke-width", "1");

            // lignes de plis
            for (CoordPouce[] coords : plan.getLignePlie()) {
                writer.writeEmptyElement("line");
                writer.writeAttribute("x1", String.valueOf(decal + coords[0].getX().toDouble()*multiple));
                writer.writeAttribute("y1", String.valueOf(decal + coords[0].getY().toDouble()*multiple));
                writer.writeAttribute("x2", String.valueOf(decal + coords[1].getX().toDouble()*multiple));
                writer.writeAttribute("y2", String.valueOf(decal + coords[1].getY().toDouble()*multiple));
                writer.writeAttribute("stroke", "black");
                writer.writeAttribute("stroke-width", "1");
                writer.writeAttribute("stroke-dasharray", "4");
            }

            //accessoires
            for (List<CoordPouce> coordsAccessoire : plan.getPolygoneAccessoire()) {
                StringBuilder contourAccessoire = new StringBuilder("M");
                for (CoordPouce coord : coordsAccessoire) {
                    contourAccessoire.append(" ").append(decal + coord.getX().toDouble()*multiple).append(" ").append(decal + coord.getY().toDouble()*multiple).append(" L");
                }
                contourAccessoire.setCharAt(contourAccessoire.length() - 1, 'z');
                writer.writeEmptyElement("path");
                writer.writeAttribute("d", contourAccessoire.toString());
                writer.writeAttribute("fill", "white");
                writer.writeAttribute("stroke", "black");
                writer.writeAttribute("stroke-width", "1");
            }

            writer.writeEndDocument();
            writer.close();
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @brief création des objet PlanPanneau qui sont les dessin des panneaus intérieur et extérieur d'un mur
     * @param accessoires (List<Accessoir>) : liste d'accessoire du coté du mur
     * @param mur (Mur) : mur à dessiner
     * @param epMurs (Pouce) : epaisseur des mur
     * @param epRetourAir (Pouce) : epaisseir des retours d'aire
     * @return PlanPanneau : [0] panneau externe
     *                       [1] panneau interne
     */
    public PlanPanneau[] genererCoordonees(ArrayList<Accessoire> accessoires, Mur mur, Pouce epMurs, Pouce epRetourAir) {
        PlanPanneau[] plan = new PlanPanneau[2];
        PlanPanneau interne = new PlanPanneau();
        PlanPanneau externe = new PlanPanneau();
        plan[0] = externe;
        plan[1] = interne;
        List<Accessoire> porte = new ArrayList<Accessoire>(){};
        Pouce ajoutY = this.longueurPlis.add(epMurs).add(this.margeEpaisseurMateriaux.mul(2));
        Pouce ajoutX = null;
        Pouce ajoutXInterne = null;
        Pouce hypothenus = null;
        try{
            hypothenus = epMurs.mul(epMurs).mulRef(2).sqrtRef();
        }catch (PouceError ignored){}
        if (mur.isEstCoinGauche()){
            try {
                ajoutX = this.longueurPlis.add(epMurs).addRef(hypothenus).addRef(this.margeEpaisseurMateriaux.mul(2));
                ajoutXInterne = new Pouce("0");
            }catch (PouceError | FractionError ignored){}
        }
        else{
            ajoutX = this.longueurPlis.add(epMurs).addRef(this.margeEpaisseurMateriaux.mul(2));
            try{
                ajoutXInterne = new Pouce("0");
            }catch (FractionError | PouceError ignored){}
        }
        if (mur.isEstCoinDroit()){
            ajoutXInterne = epMurs.sub(this.margeLargeurReplis);
        }

        for (Accessoire acce:accessoires) {
            if (mur.getPosition().toDouble() <= acce.getPosition().getX().toDouble() &&
                    acce.getPosition().getX().toDouble() <= mur.getPosition().add(mur.getLargeur()).toDouble()){
                if(acce.getType().equals("Porte")) {
                    porte.add(acce);
                    continue;
                }

                List<CoordPouce> polygoneAccesInterne = new ArrayList<>();

                polygoneAccesInterne.add(new CoordPouce(mur.getLargeur().sub(acce.getPosition().getX().sub(acce.getMarge()).sub(mur.getPosition())).add(ajoutXInterne),
                                                 acce.getPosition().getY().sub(acce.getMarge()).add(ajoutY)));
                polygoneAccesInterne.add(new CoordPouce(mur.getLargeur().sub(acce.getLargeur().add(acce.getPosition().getX()).add(acce.getMarge()).subRef(mur.getPosition())).add(ajoutXInterne),
                                                 acce.getPosition().getY().sub(acce.getMarge()).add(ajoutY)));
                polygoneAccesInterne.add(new CoordPouce(mur.getLargeur().sub(acce.getLargeur().add(acce.getPosition().getX()).add(acce.getMarge()).subRef(mur.getPosition())).add(ajoutXInterne),
                                                 acce.getHauteur().add(acce.getPosition().getY()).add(acce.getMarge()).add(ajoutY)));
                polygoneAccesInterne.add(new CoordPouce(mur.getLargeur().sub(acce.getPosition().getX().sub(acce.getMarge()).sub(mur.getPosition())).add(ajoutXInterne),
                                                 acce.getHauteur().add(acce.getPosition().getY()).add(acce.getMarge()).add(ajoutY)));

                interne.ajoutAccessoire(polygoneAccesInterne);
                if(acce.getType().equals("Fenêtre")){

                    List<CoordPouce> polygoneAccesExterne = new ArrayList<>();
                    CoordPouce coord = new CoordPouce(acce.getPosition().getX().sub(acce.getMarge()).sub(mur.getPosition()).add(ajoutX),
                            acce.getPosition().getY().sub(acce.getMarge()));
                    polygoneAccesExterne.add(coord.copy());
                    coord.getX().addRef(acce.getLargeur()).addRef(acce.getMarge().mul(2));
                    polygoneAccesExterne.add(coord.copy());
                    coord.getY().addRef(acce.getHauteur()).addRef(acce.getMarge().mul(2));
                    polygoneAccesExterne.add(coord.copy());
                    coord.getX().subRef(acce.getLargeur()).subRef(acce.getMarge().mul(2));
                    polygoneAccesExterne.add(coord.copy());

                    externe.ajoutAccessoire(polygoneAccesExterne);
                }

                if(acce.getType().equals("Retour d'air")){
                    List<CoordPouce> polygoneAccesInterne2 = new ArrayList<>();
                    try {
                        CoordPouce coord = new CoordPouce(acce.getPosition().getX().sub(acce.getMarge().add(ajoutXInterne)),
                                ajoutY.sub(epRetourAir).sub(epMurs.sub(epRetourAir).div(2)));
                        polygoneAccesInterne2.add(coord.copy());
                        coord.getX().addRef(acce.getLargeur());
                        polygoneAccesInterne2.add(coord.copy());
                        coord.getY().addRef(epRetourAir);
                        polygoneAccesInterne2.add(coord.copy());
                        coord.getX().subRef(acce.getLargeur());
                        polygoneAccesInterne2.add(coord.copy());
                        interne.ajoutAccessoire(polygoneAccesInterne2);

                    }catch (PouceError ignored){}
                }
            }
        }
        try {
            porte.sort((porte1,porte2)->(porte1.getPosition().getX().compare(porte2.getPosition().getX())));
            CoordPouce startInterne = new CoordPouce(ajoutXInterne, ajoutY);
            CoordPouce startExterne = new CoordPouce(ajoutX, new Pouce("0"));
            dessinPanneauInterne(mur, interne, porte, epMurs, startInterne);
            Collections.reverse(porte);
            dessinPanneauExterne(mur, externe, porte, epMurs, startExterne,hypothenus);
        }catch (FractionError | PouceError ignored){}

        return plan;
    }

    /**
     * @brief dessine dans la variable plan le polygone version intérieur
     * @param mur (Mur) : mur à dessiner
     * @param plan (PlanPanneau) : plan qui est modifier, ajout dans la liste polygone
     * @param porte (List<Accessoire>) : que les portes dans le mur
     * @param epaisseurMur (Pouce) : epaisseur des murs de la salle
     * @param start (CoordPouce) : possition du premier point
     * @throws PouceError si une erreur avec les Pouces
     * @throws FractionError si une erreur avec les Fractions
     */
    private void dessinPanneauInterne(Mur mur, PlanPanneau plan, List<Accessoire> porte, Pouce epaisseurMur,CoordPouce start) throws PouceError, FractionError {
        CoordPouce[] plie1 = new CoordPouce[2];
        CoordPouce[] plie2 = new CoordPouce[2];
        CoordPouce coordActuel = start.copy();

        Pouce ajoutPlie = this.longueurPlis.copy();
        ajoutPlie.formatToFraction();
        Fraction tempo = ajoutPlie.getFraction();
        tempo.mulRef(100000000);
        tempo.divRef((int)(Math.tan(Math.toRadians(this.anglePlis))*100000000));
        ajoutPlie.setFraction(tempo);
        ajoutPlie.round(128);
        ajoutPlie.simplifier();

        plan.ajoutPointPolygone(coordActuel.copy());//1

        coordActuel.getX().addRef(this.margeLargeurReplis);
        plan.ajoutPointPolygone(coordActuel.copy());//2
        coordActuel.getY().subRef(this.margeEpaisseurMateriaux.div(2));
        plie1[0] = coordActuel.copy();
        plie1[1] = new CoordPouce(mur.getLargeur().sub(this.margeLargeurReplis), plie1[0].getY().copy());
        plan.ajoutlignePlie(plie1);

        if (mur.isEstCoinDroit()){
            coordActuel.getY().subRef(this.margeEpaisseurMateriaux.div(2));
            plan.ajoutPointPolygone(coordActuel.copy());//2.5

            coordActuel.getX().subRef(epaisseurMur);
            coordActuel.getY().subRef(epaisseurMur);
            plan.ajoutPointPolygone(coordActuel.copy());//3

            coordActuel.getY().subRef(this.margeEpaisseurMateriaux);
            plan.ajoutPointPolygone(coordActuel.copy());//3.5
            plie2[0] = coordActuel.copy();
            plie2[0].getY().addRef(this.margeEpaisseurMateriaux.div(2));

        }
        else {
            coordActuel.getY().subRef(epaisseurMur).subRef(this.margeEpaisseurMateriaux);
            plie2[0] = coordActuel.copy();

            coordActuel.getY().subRef(this.margeEpaisseurMateriaux.div(2));
            plan.ajoutPointPolygone(coordActuel.copy());//3
        }

        plan.ajoutlignePlie(plie2);

        coordActuel.getY().subRef(this.longueurPlis);
        coordActuel.getX().addRef(ajoutPlie);
        plan.ajoutPointPolygone(coordActuel.copy());//4

        if (mur.isEstCoinGauche()){
            coordActuel.setX(mur.getLargeur().sub(this.margeLargeurReplis).subRef(ajoutPlie).addRef(epaisseurMur));
            plan.ajoutPointPolygone(coordActuel.copy());//5

            coordActuel.getY().addRef(this.longueurPlis);
            coordActuel.getX().addRef(ajoutPlie);
            plan.ajoutPointPolygone(coordActuel.copy());//5.5

            coordActuel.getY().addRef(this.margeEpaisseurMateriaux.div(2));
            plie2[1] = coordActuel.copy();
            coordActuel.getY().addRef(this.margeEpaisseurMateriaux.div(2));
            plan.ajoutPointPolygone(coordActuel.copy());//6

            coordActuel.getY().addRef(epaisseurMur);
            coordActuel.getX().subRef(epaisseurMur);
            plan.ajoutPointPolygone(coordActuel.copy());//6.5

            coordActuel.getY().addRef(this.margeEpaisseurMateriaux);
            plan.ajoutPointPolygone(coordActuel.copy());//7
        }
        else {
            coordActuel.setX(start.getX().copy().add(mur.getLargeur().sub(this.margeLargeurReplis).subRef(ajoutPlie)));
            plan.ajoutPointPolygone(coordActuel.copy());//5

            coordActuel.getY().addRef(this.longueurPlis);
            coordActuel.getX().addRef(ajoutPlie);
            plan.ajoutPointPolygone(coordActuel.copy());//6
            plie2[1] = coordActuel.copy();
            plie2[1].getY().addRef(this.margeEpaisseurMateriaux.div(2));

            coordActuel.getY().addRef(epaisseurMur.add(this.margeEpaisseurMateriaux.mul(2)));
            plan.ajoutPointPolygone(coordActuel.copy());//7
        }
        plan.ajoutlignePlie(plie2);

        coordActuel = start.copy();
        coordActuel.getX().addRef(mur.getLargeur());
        plan.ajoutPointPolygone(coordActuel.copy());//8

        coordActuel.getY().addRef(mur.getHauteur());
        plan.ajoutPointPolygone(coordActuel.copy());//9

        coordActuel.getX().subRef(this.margeLargeurReplis);
        plan.ajoutPointPolygone(coordActuel.copy());//10


        CoordPouce posiPlie1_1;
        CoordPouce posiPlie2_1;
        CoordPouce posiPlie1_2;
        CoordPouce posiPlie2_2;


        if (mur.isEstCoinGauche()){
            coordActuel.getY().addRef(this.margeEpaisseurMateriaux);
            plan.ajoutPointPolygone(coordActuel.copy());//10.5

            posiPlie1_1 = coordActuel.copy();
            posiPlie1_1.getX().subRef(this.margeEpaisseurMateriaux.div(2));

            coordActuel.getX().addRef(epaisseurMur);
            coordActuel.getY().addRef(epaisseurMur);
            plan.ajoutPointPolygone(coordActuel.copy());//11

            coordActuel.getY().addRef(this.margeEpaisseurMateriaux);
            plan.ajoutPointPolygone(coordActuel.copy());//11.5

            posiPlie2_1 = coordActuel.copy();
            posiPlie2_1.getY().subRef(this.margeEpaisseurMateriaux.div(2));
        }
        else{
            posiPlie1_1 = coordActuel.copy();
            posiPlie1_1.getY().addRef(this.margeEpaisseurMateriaux.div(2));
            posiPlie2_1 = posiPlie1_1.copy();
            posiPlie2_1.getY().addRef(epaisseurMur).addRef(this.margeEpaisseurMateriaux);

            coordActuel.getY().addRef(epaisseurMur.add(this.margeEpaisseurMateriaux.mul(2)));
            plan.ajoutPointPolygone(coordActuel.copy());//11
        }

        posiPlie1_2 = posiPlie1_1.copy();
        posiPlie2_2 = posiPlie2_1.copy();

        coordActuel.getX().subRef(ajoutPlie);
        coordActuel.getY().addRef(this.longueurPlis);
        plan.ajoutPointPolygone(coordActuel.copy());//12

        if (porte.size() != 0)
            for (Accessoire access:porte) {
                coordActuel.setX(start.getX().add(mur.getLargeur().sub(access.getPosition().getX().sub(mur.getPosition()))));

                plan.ajoutPointPolygone(coordActuel.copy());//en-bas a droite

                coordActuel.getY().subRef(access.getHauteur());
                plan.ajoutPointPolygone(coordActuel.copy());//en-haut a droite

                posiPlie1_2.setX(coordActuel.getX().copy());
                posiPlie2_2.setX(coordActuel.getX().copy());

                coordActuel.getX().subRef(access.getLargeur());
                plan.ajoutPointPolygone(coordActuel.copy());//en-haut a gauche

                coordActuel.getY().addRef(access.getHauteur());
                plan.ajoutPointPolygone(coordActuel.copy());//en-bas a gauche

                plan.ajoutlignePlie(new CoordPouce[]{posiPlie1_1.copy(),posiPlie1_2.copy()});
                plan.ajoutlignePlie(new CoordPouce[]{posiPlie2_1.copy(),posiPlie2_2.copy()});

                posiPlie1_1.setX(coordActuel.getX().copy());
                posiPlie2_1.setX(coordActuel.getX().copy());
            }


        if (mur.isEstCoinDroit()){
            coordActuel.setX(start.getX().add(this.margeLargeurReplis).addRef(ajoutPlie).subRef(epaisseurMur));
            plan.ajoutPointPolygone(coordActuel.copy());//13

            coordActuel.getX().subRef(ajoutPlie);
            coordActuel.getY().subRef(this.longueurPlis);
            plan.ajoutPointPolygone(coordActuel.copy());//13.5

            coordActuel.getY().subRef(this.margeEpaisseurMateriaux);
            plan.ajoutPointPolygone(coordActuel.copy());//14
            posiPlie2_2.setX(coordActuel.getX().add(this.margeEpaisseurMateriaux.div(2)));

            coordActuel.getY().subRef(epaisseurMur);
            coordActuel.getX().addRef(epaisseurMur);
            plan.ajoutPointPolygone(coordActuel.copy());//14.5

            posiPlie1_2.setX(coordActuel.getX().sub(this.margeEpaisseurMateriaux.div(2)));

            coordActuel.getY().subRef(this.margeEpaisseurMateriaux);
            plan.ajoutPointPolygone(coordActuel.copy());//15

        }
        else{
            coordActuel.setX(start.getX().add(this.margeLargeurReplis).addRef(ajoutPlie));
            plan.ajoutPointPolygone(coordActuel.copy());//13

            coordActuel.getX().subRef(ajoutPlie);
            coordActuel.getY().subRef(this.longueurPlis);
            plan.ajoutPointPolygone(coordActuel.copy());//14

            posiPlie1_2.setX(coordActuel.getX().copy());
            posiPlie2_2.setX(coordActuel.getX().copy());

            coordActuel.getY().subRef(this.margeEpaisseurMateriaux.mul(2).add(epaisseurMur));
            plan.ajoutPointPolygone(coordActuel.copy());//15
        }

        plan.ajoutlignePlie(new CoordPouce[]{posiPlie1_1.copy(),posiPlie1_2.copy()});
        plan.ajoutlignePlie(new CoordPouce[]{posiPlie2_1.copy(),posiPlie2_2.copy()});

        coordActuel.getX().subRef(this.margeLargeurReplis);
        plan.ajoutPointPolygone(coordActuel.copy());//16
    }

    /**
     * @brief dessine dans la variable plan le polygone version extérieur
     * @param mur (Mur) : mur à dessiner
     * @param plan (PlanPanneau) : plan qui est modifier, ajout dans la liste polygone
     * @param porte (List<Accessoire>) : que les portes dans le mur
     * @param epaisseurMur (Pouce) : epaisseur des murs de la salle
     * @param start (CoordPouce) : possition du premier point
     * @param hypo (Pouce) : hypothénus du coin
     * @throws PouceError si une erreur avec les Pouces
     * @throws FractionError si une erreur avec les Fractions
     */
    private void dessinPanneauExterne(Mur mur, PlanPanneau plan, List<Accessoire> porte, Pouce epaisseurMur,CoordPouce start, Pouce hypo) throws PouceError, FractionError {
        CoordPouce coordActuel = start.copy();
        plan.ajoutPointPolygone(coordActuel.copy());//1

        Pouce ajoutPlie = this.longueurPlis.copy();
        ajoutPlie.formatToFraction();
        Fraction tempo = ajoutPlie.getFraction();
        tempo.mulRef(100000000);
        tempo.divRef((int)(Math.tan(Math.toRadians(this.anglePlis))*100000000));
        ajoutPlie.setFraction(tempo);
        ajoutPlie.round(128);
        ajoutPlie.simplifier();

        if (mur.isEstCoinDroit()){
            coordActuel.getX().addRef(mur.getLargeur()).addRef(epaisseurMur);
            plan.ajoutPointPolygone(coordActuel.copy());//2

            coordActuel.getY().addRef(this.margeLargeurReplis);
            plan.ajoutPointPolygone(coordActuel.copy());//3

            CoordPouce plie1_1 = coordActuel.copy();
            plie1_1.getX().addRef(this.margeEpaisseurMateriaux.div(2));
            CoordPouce plie2_1 = plie1_1.copy();
            plie2_1.getX().addRef(hypo).addRef(this.margeEpaisseurMateriaux);

            coordActuel.getX().addRef(hypo).addRef(this.margeEpaisseurMateriaux.mul(2));
            plan.ajoutPointPolygone(coordActuel.copy());//4

            coordActuel.getX().addRef(longueurPlis);
            coordActuel.getY().addRef(ajoutPlie);
            plan.ajoutPointPolygone(coordActuel.copy());//5

            coordActuel.setY(mur.getHauteur().copy());
            coordActuel.getY().subRef(this.margeLargeurReplis).subRef(ajoutPlie);
            plan.ajoutPointPolygone(coordActuel.copy());//6

            coordActuel.getX().subRef(this.longueurPlis);
            coordActuel.getY().addRef(ajoutPlie);
            plan.ajoutPointPolygone(coordActuel.copy());//7

            CoordPouce plie1_2 = plie1_1.copy();
            plie1_2.setY(coordActuel.getY().copy());
            CoordPouce plie2_2 = plie2_1.copy();
            plie2_2.setY(coordActuel.getY().copy());
            plan.ajoutlignePlie(new CoordPouce[]{plie1_1,plie1_2});
            plan.ajoutlignePlie(new CoordPouce[]{plie2_1,plie2_2});

            coordActuel = start.copy();
            coordActuel.getX().addRef(mur.getLargeur()).addRef(epaisseurMur);
            coordActuel.getY().addRef(mur.getHauteur()).subRef(this.margeLargeurReplis);
            plan.ajoutPointPolygone(coordActuel.copy());//8

            coordActuel.getY().addRef(this.margeLargeurReplis);
            plan.ajoutPointPolygone(coordActuel.copy());//9

            coordActuel.getX().subRef(epaisseurMur);
        }
        else{
            coordActuel.getX().addRef(mur.getLargeur());
            plan.ajoutPointPolygone(coordActuel.copy());//2

            coordActuel.getY().addRef(this.margeLargeurReplis);
            plan.ajoutPointPolygone(coordActuel.copy());//3

            CoordPouce plie1_1 = coordActuel.copy();
            plie1_1.getX().addRef(this.margeEpaisseurMateriaux.div(2));
            CoordPouce plie2_1 = plie1_1.copy();
            plie2_1.getX().addRef(epaisseurMur).addRef(this.margeEpaisseurMateriaux);

            coordActuel.getX().addRef(epaisseurMur).addRef(this.margeEpaisseurMateriaux.mul(2));
            plan.ajoutPointPolygone(coordActuel.copy());//4

            coordActuel.getX().addRef(longueurPlis);
            coordActuel.getY().addRef(ajoutPlie);
            plan.ajoutPointPolygone(coordActuel.copy());//5

            coordActuel.setY(mur.getHauteur().copy());
            coordActuel.getY().subRef(this.margeLargeurReplis).subRef(ajoutPlie);
            plan.ajoutPointPolygone(coordActuel.copy());//6

            coordActuel.getX().subRef(this.longueurPlis);
            coordActuel.getY().addRef(ajoutPlie);
            plan.ajoutPointPolygone(coordActuel.copy());//7

            CoordPouce plie1_2 = plie1_1.copy();
            plie1_2.setY(coordActuel.getY().copy());
            CoordPouce plie2_2 = plie2_1.copy();
            plie2_2.setY(coordActuel.getY().copy());
            plan.ajoutlignePlie(new CoordPouce[]{plie1_1,plie1_2});
            plan.ajoutlignePlie(new CoordPouce[]{plie2_1,plie2_2});

            coordActuel = start.copy();
            coordActuel.getX().addRef(mur.getLargeur());
            coordActuel.getY().addRef(mur.getHauteur()).subRef(this.margeLargeurReplis);
            plan.ajoutPointPolygone(coordActuel.copy());//8

            coordActuel.getY().addRef(this.margeLargeurReplis);
            plan.ajoutPointPolygone(coordActuel.copy());//9
        }

        for (Accessoire access:porte) {
            coordActuel.setX(start.getX().add(access.getPosition().getX().sub(mur.getPosition()).addRef(access.getLargeur())));
            plan.ajoutPointPolygone(coordActuel.copy());//en-bas à droite

            coordActuel.getY().subRef(access.getHauteur());
            plan.ajoutPointPolygone(coordActuel.copy());//en-hau à droite

            coordActuel.getX().subRef(access.getLargeur());
            plan.ajoutPointPolygone(coordActuel.copy());//en-haut à gauche

            coordActuel.getY().addRef(access.getHauteur());
            plan.ajoutPointPolygone(coordActuel.copy());//en-bas à gauche
        }
        coordActuel = start.copy();
        coordActuel.getY().addRef(mur.getHauteur());

        if(mur.isEstCoinGauche()){
            coordActuel.getX().subRef(epaisseurMur);
            plan.getPolygone().get(0).getX().subRef(epaisseurMur);
            plan.ajoutPointPolygone(coordActuel.copy());//10

            coordActuel.getY().subRef(margeLargeurReplis);
            plan.ajoutPointPolygone(coordActuel.copy());//11

            CoordPouce plie1_1 = coordActuel.copy();
            plie1_1.getX().subRef(this.margeEpaisseurMateriaux.div(2));
            CoordPouce plie2_1 = plie1_1.copy();
            plie2_1.getX().subRef(hypo).subRef(this.margeEpaisseurMateriaux);

            coordActuel.getX().subRef(hypo).subRef(this.margeEpaisseurMateriaux.mul(2));
            plan.ajoutPointPolygone(coordActuel.copy());//12

            coordActuel.getX().subRef(this.longueurPlis);
            coordActuel.getY().subRef(ajoutPlie);
            plan.ajoutPointPolygone(coordActuel.copy());//13

            coordActuel.setY(mur.getHauteur().sub(coordActuel.getY()));
            plan.ajoutPointPolygone(coordActuel.copy());//14

            CoordPouce plie1_2 = plie1_1.copy();
            plie1_2.setY(coordActuel.getY().copy());
            CoordPouce plie2_2 = plie2_1.copy();
            plie2_2.setY(coordActuel.getY().copy());
            plan.ajoutlignePlie(new CoordPouce[]{plie1_1,plie1_2});
            plan.ajoutlignePlie(new CoordPouce[]{plie2_1,plie2_2});

            coordActuel.getX().addRef(this.longueurPlis);
            coordActuel.getY().subRef(ajoutPlie);
            plan.ajoutPointPolygone(coordActuel.copy());//15

            coordActuel.getX().addRef(hypo).addRef(this.margeEpaisseurMateriaux.mul(2));
            plan.ajoutPointPolygone(coordActuel.copy());//16
        }
        else{
            plan.ajoutPointPolygone(coordActuel.copy());//10

            coordActuel.getY().subRef(margeLargeurReplis);
            plan.ajoutPointPolygone(coordActuel.copy());//11

            CoordPouce plie1_1 = coordActuel.copy();
            plie1_1.getX().subRef(this.margeEpaisseurMateriaux.div(2));
            CoordPouce plie2_1 = plie1_1.copy();
            plie2_1.getX().subRef(epaisseurMur).subRef(this.margeEpaisseurMateriaux);

            coordActuel.getX().subRef(epaisseurMur).subRef(this.margeEpaisseurMateriaux.mul(2));
            plan.ajoutPointPolygone(coordActuel.copy());//12

            coordActuel.getX().subRef(this.longueurPlis);
            coordActuel.getY().subRef(ajoutPlie);
            plan.ajoutPointPolygone(coordActuel.copy());//13

            coordActuel.setY(mur.getHauteur().sub(coordActuel.getY()));
            plan.ajoutPointPolygone(coordActuel.copy());//14

            CoordPouce plie1_2 = plie1_1.copy();
            plie1_2.setY(coordActuel.getY().copy());
            CoordPouce plie2_2 = plie2_1.copy();
            plie2_2.setY(coordActuel.getY().copy());
            plan.ajoutlignePlie(new CoordPouce[]{plie1_1,plie1_2});
            plan.ajoutlignePlie(new CoordPouce[]{plie2_1,plie2_2});

            coordActuel.getX().addRef(this.longueurPlis);
            coordActuel.getY().subRef(ajoutPlie);
            plan.ajoutPointPolygone(coordActuel.copy());//15

            coordActuel.getX().addRef(epaisseurMur).addRef(this.margeEpaisseurMateriaux.mul(2));
            plan.ajoutPointPolygone(coordActuel.copy());//16
        }
    }
}
