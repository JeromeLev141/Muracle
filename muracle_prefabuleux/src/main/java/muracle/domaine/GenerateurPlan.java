package muracle.domaine;

import muracle.domaine.accessoire.Porte;
import muracle.utilitaire.*;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenerateurPlan implements java.io.Serializable {

    private Pouce margeEpaisseurMateriaux = new Pouce("0 1/2");
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


    public PlanPanneau[] genererCoordonees(ArrayList<Accessoire> accessoires, Mur mur, Pouce epMurs, Pouce epRetourAir) {
        /*
        Ajouter ven de ventilation pour retour aire
         */
        PlanPanneau[] plan = new PlanPanneau[2];
        PlanPanneau interne = new PlanPanneau();
        PlanPanneau externe = new PlanPanneau();
        plan[0] = interne;
        plan[1] = externe;
        List<Accessoire> porte = new ArrayList<Accessoire>(){};
        Pouce ajoutY = this.longueurPlis.add(epMurs).add(this.margeLargeurReplis.mul(2));
        Pouce ajoutX = null;
        Pouce ajoutXInterne = null;
        Pouce hypothenus = null;
        try{
            hypothenus = epMurs.mul(epMurs).mulRef(2).sqrtRef();
        }catch (PouceError ignored){}
        if (mur.isEstCoinGauche()){
            try {
                ajoutX = this.longueurPlis.add(epMurs).addRef(hypothenus).addRef(this.margeLargeurReplis.mul(2));
                ajoutXInterne = new Pouce("0");
            }catch (PouceError | FractionError ignored){}
        }
        else{
            ajoutX = this.longueurPlis.add(epMurs).addRef(this.margeLargeurReplis.mul(2));
            try{
                ajoutXInterne = new Pouce("0");
            }catch (FractionError | PouceError ignored){}
        }
        if (mur.isEstCoinDroit()){
            ajoutXInterne = epMurs.sub(this.margeEpaisseurMateriaux);
        }

        for (Accessoire acce:accessoires) {
            if (mur.getPosition().toDouble() <= acce.getPosition().getX().toDouble() &&
                    acce.getPosition().getX().toDouble() <= mur.getPosition().add(mur.getLargeur()).toDouble()){
                if(acce.getType().equals("Porte")) {
                    porte.add(acce);
                    continue;
                }

                List<CoordPouce> polygoneAccesInterne = new ArrayList<>();
                /*
                 (largeurMur - (positionAcces +- marge - positionMur)) + ajout
                 */
                polygoneAccesInterne.add(new CoordPouce(mur.getLargeur().sub(acce.getPosition().getX().sub(acce.getMarge()).sub(mur.getPosition())).add(ajoutXInterne),
                                                 acce.getPosition().getY().sub(acce.getMarge()).add(ajoutY)));
                polygoneAccesInterne.add(new CoordPouce(mur.getLargeur().sub(acce.getLargeur().add(acce.getPosition().getX()).add(acce.getMarge()).subRef(mur.getPosition())).add(ajoutXInterne),
                                                 acce.getPosition().getY().sub(acce.getMarge()).add(ajoutY)));
                polygoneAccesInterne.add(new CoordPouce(mur.getLargeur().sub(acce.getLargeur().add(acce.getPosition().getX()).add(acce.getMarge()).subRef(mur.getPosition())).add(ajoutXInterne),
                                                 acce.getHauteur().add(acce.getPosition().getY()).add(acce.getMarge()).add(ajoutY)));
                polygoneAccesInterne.add(new CoordPouce(mur.getLargeur().sub(acce.getPosition().getX().sub(acce.getMarge()).sub(mur.getPosition())).add(ajoutXInterne),
                                                 acce.getHauteur().add(acce.getPosition().getY()).add(acce.getMarge()).add(ajoutY)));

                //System.out.println(polygoneAccesInterne);
                interne.ajoutAccessoire(polygoneAccesInterne);
                if(acce.getType().equals("Fenêtre")){

                    List<CoordPouce> polygoneAccesExterne = new ArrayList<>();
                    polygoneAccesExterne.add(new CoordPouce(acce.getPosition().getX().sub(acce.getMarge().add(ajoutX)),
                            acce.getPosition().getY().sub(acce.getMarge())));
                    polygoneAccesExterne.add(new CoordPouce(acce.getLargeur().add(acce.getPosition().getX()).add(acce.getMarge()),
                            acce.getPosition().getY().sub(acce.getMarge())));
                    polygoneAccesExterne.add(new CoordPouce(acce.getLargeur().add(acce.getPosition().getX()).add(acce.getMarge()),
                            acce.getHauteur().add(acce.getPosition().getY()).add(acce.getMarge())));
                    polygoneAccesExterne.add(new CoordPouce(acce.getPosition().getX().sub(acce.getMarge()),
                            acce.getHauteur().add(acce.getPosition().getY()).add(acce.getMarge())));

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
                        coord.getX().addRef(acce.getLargeur());
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
            System.out.println(interne);
            dessinPanneauInterne(mur, interne, porte, epMurs, startInterne);
            Collections.reverse(porte);
            dessinPanneauExterne(mur, externe, porte, epMurs, startExterne,hypothenus);
        }catch (FractionError | PouceError ignored){}

        return plan;
    }

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

        coordActuel.getX().addRef(this.margeEpaisseurMateriaux);
        plan.ajoutPointPolygone(coordActuel.copy());//2
        coordActuel.getY().subRef(this.margeLargeurReplis.div(2));
        plie1[0] = coordActuel.copy();
        plie1[1] = new CoordPouce(mur.getLargeur().sub(this.margeEpaisseurMateriaux), plie1[0].getY().copy());
        plan.ajoutlignePlie(plie1);

        System.out.println(mur.isEstCoinGauche());
        System.out.println(mur.isEstCoinDroit());


        if (mur.isEstCoinDroit()){
            coordActuel.getY().subRef(this.margeLargeurReplis.div(2));
            plan.ajoutPointPolygone(coordActuel.copy());//2.5

            coordActuel.getX().subRef(epaisseurMur);
            coordActuel.getY().subRef(epaisseurMur);
            plan.ajoutPointPolygone(coordActuel.copy());//3

            coordActuel.getY().subRef(this.margeLargeurReplis);
            plan.ajoutPointPolygone(coordActuel.copy());//3.5
            plie2[0] = coordActuel.copy();
            plie2[0].getY().addRef(this.margeLargeurReplis.div(2));

        }
        else {
            coordActuel.getY().subRef(epaisseurMur).subRef(this.margeLargeurReplis);
            plie2[0] = coordActuel.copy();
            //plie2[1] = new CoordPouce(plie1[1].getX().copy(), plie2[0].getY().copy());

            coordActuel.getY().subRef(this.margeLargeurReplis.div(2));
            System.out.println("ok");
            plan.ajoutPointPolygone(coordActuel.copy());//3
        }

        plan.ajoutlignePlie(plie2);

        coordActuel.getY().subRef(this.longueurPlis);
        coordActuel.getX().addRef(ajoutPlie);
        plan.ajoutPointPolygone(coordActuel.copy());//4

        //coordActuel.setX(mur.getLargeur().sub(this.margeEpaisseurMateriaux).subRef(ajoutPlie));
        //plan.ajoutPointPolygone(coordActuel.copy());//5

        if (mur.isEstCoinGauche()){
            coordActuel.setX(mur.getLargeur().sub(this.margeEpaisseurMateriaux).subRef(ajoutPlie).addRef(epaisseurMur));
            plan.ajoutPointPolygone(coordActuel.copy());//5

            coordActuel.getY().addRef(this.longueurPlis);
            coordActuel.getX().addRef(ajoutPlie);
            plan.ajoutPointPolygone(coordActuel.copy());//5.5

            coordActuel.getY().addRef(this.margeLargeurReplis.div(2));
            plie2[1] = coordActuel.copy();
            coordActuel.getY().addRef(this.margeLargeurReplis.div(2));
            plan.ajoutPointPolygone(coordActuel.copy());//6

            coordActuel.getY().addRef(epaisseurMur);
            coordActuel.getX().subRef(epaisseurMur);
            plan.ajoutPointPolygone(coordActuel.copy());//6.5

            coordActuel.getY().addRef(this.margeLargeurReplis);
            plan.ajoutPointPolygone(coordActuel.copy());//7
        }
        else {
            coordActuel.setX(start.getX().copy().add(mur.getLargeur().sub(this.margeEpaisseurMateriaux).subRef(ajoutPlie)));
            plan.ajoutPointPolygone(coordActuel.copy());//5

            coordActuel.getY().addRef(this.longueurPlis);
            coordActuel.getX().addRef(ajoutPlie);
            plan.ajoutPointPolygone(coordActuel.copy());//6
            plie2[1] = coordActuel.copy();
            plie2[1].getY().addRef(this.margeLargeurReplis.div(2));

            coordActuel.getY().addRef(epaisseurMur.add(this.margeLargeurReplis.mul(2)));
            plan.ajoutPointPolygone(coordActuel.copy());//7
        }
        plan.ajoutlignePlie(plie2);

        coordActuel = start.copy();
        coordActuel.getX().addRef(mur.getLargeur());
        plan.ajoutPointPolygone(coordActuel.copy());//8

        coordActuel.getY().addRef(mur.getHauteur());
        plan.ajoutPointPolygone(coordActuel.copy());//9

        coordActuel.getX().subRef(this.margeEpaisseurMateriaux);
        plan.ajoutPointPolygone(coordActuel.copy());//10


        CoordPouce posiPlie1_1 = null;
        CoordPouce posiPlie2_1 = null;
        CoordPouce posiPlie1_2 = null;
        CoordPouce posiPlie2_2 = null;


        if (mur.isEstCoinGauche()){
            coordActuel.getY().addRef(this.margeLargeurReplis);
            plan.ajoutPointPolygone(coordActuel.copy());//10.5

            posiPlie1_1 = coordActuel.copy();
            posiPlie1_1.getX().subRef(this.margeLargeurReplis.div(2));

            coordActuel.getX().addRef(epaisseurMur);
            coordActuel.getY().addRef(epaisseurMur);
            plan.ajoutPointPolygone(coordActuel.copy());//11

            coordActuel.getY().addRef(this.margeLargeurReplis);
            plan.ajoutPointPolygone(coordActuel.copy());//11.5

            posiPlie2_1 = coordActuel.copy();
            posiPlie2_1.getY().subRef(this.margeLargeurReplis.div(2));
        }
        else{
            posiPlie1_1 = coordActuel.copy();
            posiPlie1_1.getY().addRef(this.margeLargeurReplis.div(2));
            posiPlie2_1 = posiPlie1_1.copy();
            posiPlie2_1.getY().addRef(epaisseurMur).addRef(this.margeLargeurReplis);

            coordActuel.getY().addRef(epaisseurMur.add(this.margeLargeurReplis.mul(2)));
            plan.ajoutPointPolygone(coordActuel.copy());//11
        }

        posiPlie1_2 = posiPlie1_1.copy();
        posiPlie2_2 = posiPlie2_1.copy();

        coordActuel.getX().subRef(ajoutPlie);
        coordActuel.getY().addRef(this.longueurPlis);
        plan.ajoutPointPolygone(coordActuel.copy());//12

        if (porte.size() != 0)
            for (Accessoire access:porte) {
                Pouce x = mur.getLargeur().sub(access.getPosition().getX());

                coordActuel.getX().subRef(x);
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
            coordActuel.setX(start.getX().add(this.margeEpaisseurMateriaux).addRef(ajoutPlie).subRef(epaisseurMur));
            plan.ajoutPointPolygone(coordActuel.copy());//13

            coordActuel.getX().subRef(ajoutPlie);
            coordActuel.getY().subRef(this.longueurPlis);
            plan.ajoutPointPolygone(coordActuel.copy());//13.5

            coordActuel.getY().subRef(this.margeLargeurReplis);
            plan.ajoutPointPolygone(coordActuel.copy());//14
            posiPlie2_2.setX(coordActuel.getX().add(this.margeLargeurReplis.div(2)));

            coordActuel.getY().subRef(epaisseurMur);
            coordActuel.getX().addRef(epaisseurMur);
            plan.ajoutPointPolygone(coordActuel.copy());//14.5

            posiPlie1_2.setX(coordActuel.getX().sub(this.margeLargeurReplis.div(2)));

            coordActuel.getY().subRef(this.margeLargeurReplis);
            plan.ajoutPointPolygone(coordActuel.copy());//15

        }
        else{
            coordActuel.setX(start.getX().add(this.margeEpaisseurMateriaux).addRef(ajoutPlie));
            plan.ajoutPointPolygone(coordActuel.copy());//13

            coordActuel.getX().subRef(ajoutPlie);
            coordActuel.getY().subRef(this.longueurPlis);
            plan.ajoutPointPolygone(coordActuel.copy());//14

            posiPlie1_2.setX(coordActuel.getX().copy());
            posiPlie2_2.setX(coordActuel.getX().copy());

            System.out.println(epaisseurMur);
            System.out.println(epaisseurMur.add(this.margeLargeurReplis).add(this.margeLargeurReplis));
            coordActuel.getY().subRef(this.margeLargeurReplis.mul(2).add(epaisseurMur));
            plan.ajoutPointPolygone(coordActuel.copy());//15
        }

        plan.ajoutlignePlie(new CoordPouce[]{posiPlie1_1.copy(),posiPlie1_2.copy()});
        plan.ajoutlignePlie(new CoordPouce[]{posiPlie2_1.copy(),posiPlie2_2.copy()});

        coordActuel.getX().subRef(this.margeEpaisseurMateriaux);
        plan.ajoutPointPolygone(coordActuel.copy());//16
    }

    private void dessinPanneauExterne(Mur mur, PlanPanneau plan, List<Accessoire> porte, Pouce epaisseurMur,CoordPouce start, Pouce hypo) throws PouceError, FractionError {
        CoordPouce coordActuel = start.copy();
        plan.ajoutPointPolygone(coordActuel.copy());//1
        //System.out.println(hypo);

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

            coordActuel.getY().addRef(this.margeEpaisseurMateriaux);
            plan.ajoutPointPolygone(coordActuel.copy());//3

            CoordPouce plie1_1 = coordActuel.copy();
            plie1_1.getX().addRef(this.margeLargeurReplis.div(2));
            CoordPouce plie2_1 = plie1_1.copy();
            plie2_1.getX().addRef(hypo).addRef(this.margeLargeurReplis);

            coordActuel.getX().addRef(hypo).addRef(this.margeLargeurReplis.mul(2));
            plan.ajoutPointPolygone(coordActuel.copy());//4

            coordActuel.getX().addRef(longueurPlis);
            coordActuel.getY().addRef(ajoutPlie);
            plan.ajoutPointPolygone(coordActuel.copy());//5

            coordActuel.setY(mur.getHauteur().copy());
            coordActuel.getY().subRef(this.margeEpaisseurMateriaux).subRef(ajoutPlie);
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
            coordActuel.getY().addRef(mur.getHauteur()).subRef(this.margeEpaisseurMateriaux);
            plan.ajoutPointPolygone(coordActuel.copy());//8

            coordActuel.getY().addRef(this.margeEpaisseurMateriaux);
            plan.ajoutPointPolygone(coordActuel.copy());//9

            coordActuel.getX().subRef(epaisseurMur);
        }
        else{
            coordActuel.getX().addRef(mur.getLargeur());
            plan.ajoutPointPolygone(coordActuel.copy());//2

            coordActuel.getY().addRef(this.margeEpaisseurMateriaux);
            plan.ajoutPointPolygone(coordActuel.copy());//3

            CoordPouce plie1_1 = coordActuel.copy();
            plie1_1.getX().addRef(this.margeLargeurReplis.div(2));
            CoordPouce plie2_1 = plie1_1.copy();
            plie2_1.getX().addRef(epaisseurMur).addRef(this.margeLargeurReplis);

            coordActuel.getX().addRef(epaisseurMur).addRef(this.margeLargeurReplis.mul(2));
            plan.ajoutPointPolygone(coordActuel.copy());//4

            coordActuel.getX().addRef(longueurPlis);
            coordActuel.getY().addRef(ajoutPlie);
            plan.ajoutPointPolygone(coordActuel.copy());//5

            coordActuel.setY(mur.getHauteur().copy());
            coordActuel.getY().subRef(this.margeEpaisseurMateriaux).subRef(ajoutPlie);
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
            coordActuel.getY().addRef(mur.getHauteur()).subRef(this.margeEpaisseurMateriaux);
            plan.ajoutPointPolygone(coordActuel.copy());//8

            coordActuel.getY().addRef(this.margeEpaisseurMateriaux);
            plan.ajoutPointPolygone(coordActuel.copy());//9
        }

        for (Accessoire access:porte) {
            coordActuel.getX().subRef(access.getPosition().getX()).addRef(access.getLargeur());
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

            coordActuel.getY().subRef(margeEpaisseurMateriaux);
            plan.ajoutPointPolygone(coordActuel.copy());//11

            CoordPouce plie1_1 = coordActuel.copy();
            plie1_1.getX().subRef(this.margeLargeurReplis.div(2));
            CoordPouce plie2_1 = plie1_1.copy();
            plie2_1.getX().addRef(hypo).addRef(this.margeLargeurReplis);

            coordActuel.getX().subRef(hypo).subRef(this.margeLargeurReplis.mul(2));
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

            coordActuel.getX().addRef(hypo).addRef(this.margeLargeurReplis.mul(2));
            plan.ajoutPointPolygone(coordActuel.copy());//16
        }
        else{
            plan.ajoutPointPolygone(coordActuel.copy());//10

            coordActuel.getY().subRef(margeEpaisseurMateriaux);
            plan.ajoutPointPolygone(coordActuel.copy());//11

            CoordPouce plie1_1 = coordActuel.copy();
            plie1_1.getX().subRef(this.margeLargeurReplis.div(2));
            CoordPouce plie2_1 = plie1_1.copy();
            plie2_1.getX().addRef(epaisseurMur).addRef(this.margeLargeurReplis);

            coordActuel.getX().subRef(epaisseurMur).subRef(this.margeLargeurReplis.mul(2));
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

            coordActuel.getX().addRef(epaisseurMur).addRef(this.margeLargeurReplis.mul(2));
            plan.ajoutPointPolygone(coordActuel.copy());//16
        }
    }
}
