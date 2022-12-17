package muracle;


import muracle.domaine.Accessoire;
import muracle.domaine.Cote;
import muracle.domaine.CoteError;
import muracle.domaine.Mur;
import muracle.domaine.accessoire.Fenetre;
import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.Pouce;
import org.junit.Test;

import java.util.ArrayList;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class CoteTest {
    @Test
    public void addAccessoireValid(){
        try{
            Cote cote = new Cote('N', new Pouce("30"), new Pouce("30"));
            Accessoire accessoire = new Accessoire(new Pouce("10"), new Pouce("5"), new CoordPouce(new Pouce("0"), new Pouce("0")));
            cote.addAccessoire(accessoire);
            assertEquals(1, cote.getAccessoires().size());
        }catch (Exception e){
            fail();
        }
    }
    @Test
    public void addAccessoireFenetre(){
        try{
            Cote cote = new Cote('N', new Pouce("30"), new Pouce("30"));
            Accessoire accessoire = new Accessoire(new Pouce("10"), new Pouce("5"), new CoordPouce(new Pouce("1"), new Pouce("1")));
            accessoire.setType("Fenêtre");
            accessoire.setMarge(new Pouce("1"));
            cote.addAccessoire(accessoire);
            assertEquals(1, cote.getAccessoires().size());
        }catch (Exception e){
            fail();
        }
    }
    @Test
    public void addAccessoireNonValid(){
        try{
            Cote cote = new Cote('N', new Pouce("30"), new Pouce("30"));
            Accessoire accessoire = new Accessoire(new Pouce("10"), new Pouce("5"), new CoordPouce(new Pouce("-5"), new Pouce("0")));
            cote.addAccessoire(accessoire);
            fail();//Test doit lancer une exception
        }catch (CoteError e){
            assertTrue(true);
        } catch (Exception e){
            fail();
        }
    }

    @Test
    public void moveAccessoire(){}

    @Test
    public void removeAccessoire(){
        try{
            Cote cote = new Cote('N', new Pouce("12"), new Pouce("15"));
            Accessoire accessoire = new Accessoire(new Pouce("1"), new Pouce("1"), new CoordPouce(new Pouce("0"), new Pouce("0")));
            Accessoire accessoire2 = new Accessoire(new Pouce("1"), new Pouce("1"), new CoordPouce(new Pouce("5"), new Pouce("5")));
            Accessoire accessoire3 = new Accessoire(new Pouce("1"), new Pouce("1"), new CoordPouce(new Pouce("10"), new Pouce("10")));
            cote.addAccessoire(accessoire);
            cote.addAccessoire(accessoire2);
            cote.addAccessoire(accessoire3);
            cote.removeAccessoire(accessoire);
            assertEquals(2, cote.getAccessoires().size());
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void doesAccessoireFitInCote(){
        try{
            Cote cote = new Cote('N', new Pouce("12"), new Pouce("15"));
            Accessoire accessoire = new Accessoire(new Pouce("10"), new Pouce("5"), new CoordPouce(new Pouce("0"), new Pouce("0")));
            Accessoire accessoire2 = new Accessoire(new Pouce("10"), new Pouce("17"), new CoordPouce(new Pouce("0"), new Pouce("0")));
            Accessoire accessoire3 = new Accessoire(new Pouce("12"), new Pouce("15"), new CoordPouce(new Pouce("0"), new Pouce("0")));

            assertTrue(cote.doesAccessoireFitInCote(accessoire));
            assertFalse(cote.doesAccessoireFitInCote(accessoire2));
            assertTrue(cote.doesAccessoireFitInCote(accessoire3));
        }catch (Exception e){
            fail();
        }
    }
    @Test
    public void AccessoireFenetreBelow(){
        try{
            Cote cote = new Cote('N', new Pouce("30"), new Pouce("30"));
            Accessoire accessoire = new Accessoire(new Pouce("10"), new Pouce("10"), new CoordPouce(new Pouce("10"), new Pouce("10")));
            accessoire.setType("Fenêtre");
            accessoire.setMarge(new Pouce("5"));
            cote.addAccessoire(accessoire);
            Accessoire accessoire2 = new Accessoire(new Pouce("5"), new Pouce("5"), new CoordPouce(new Pouce("15"), new Pouce("25")));
            accessoire2.setType("Fenêtre");
            accessoire2.setMarge(new Pouce("0"));
            assertTrue(cote.doesAccessoireFitWithOtherAccessoires(accessoire2));
        }catch (Exception e){
            fail();
        }
    }
    @Test
    public void AccessoireFenetreRight(){
        try{
            Cote cote = new Cote('N', new Pouce("30"), new Pouce("30"));
            Accessoire accessoire = new Accessoire(new Pouce("10"), new Pouce("10"), new CoordPouce(new Pouce("10"), new Pouce("10")));
            accessoire.setType("Fenêtre");
            accessoire.setMarge(new Pouce("5"));
            cote.addAccessoire(accessoire);
            Accessoire accessoire2 = new Accessoire(new Pouce("5"), new Pouce("5"), new CoordPouce(new Pouce("25"), new Pouce("15")));
            accessoire2.setType("Fenêtre");
            accessoire2.setMarge(new Pouce("0"));
            assertTrue(cote.doesAccessoireFitWithOtherAccessoires(accessoire2));
        }catch (Exception e){
            fail();
        }
    }
    @Test
    public void AccessoireFenetreLeft(){
        try{
            Cote cote = new Cote('N', new Pouce("30"), new Pouce("30"));
            Accessoire accessoire = new Accessoire(new Pouce("10"), new Pouce("10"), new CoordPouce(new Pouce("10"), new Pouce("10")));
            accessoire.setType("Fenêtre");
            accessoire.setMarge(new Pouce("5"));
            cote.addAccessoire(accessoire);
            Accessoire accessoire2 = new Accessoire(new Pouce("5"), new Pouce("5"), new CoordPouce(new Pouce("0"), new Pouce("15")));
            accessoire2.setType("Fenêtre");
            accessoire2.setMarge(new Pouce("0"));
            assertTrue(cote.doesAccessoireFitWithOtherAccessoires(accessoire2));
        }catch (Exception e){
            fail();
        }
    }
    @Test
    public void AccessoireFenetreTop(){
        try{
            Cote cote = new Cote('N', new Pouce("30"), new Pouce("30"));
            Accessoire accessoire = new Accessoire(new Pouce("10"), new Pouce("10"), new CoordPouce(new Pouce("10"), new Pouce("10")));
            accessoire.setType("Fenêtre");
            accessoire.setMarge(new Pouce("5"));
            cote.addAccessoire(accessoire);
            Accessoire accessoire2 = new Accessoire(new Pouce("5"), new Pouce("5"), new CoordPouce(new Pouce("15"), new Pouce("0")));
            accessoire2.setType("Fenêtre");
            accessoire2.setMarge(new Pouce("0"));
            assertTrue(cote.doesAccessoireFitWithOtherAccessoires(accessoire2));
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void doesAccessoireFitWithOtherAccessoires(){
        try{
            Cote cote = new Cote('N', new Pouce("30"), new Pouce("30"));
            Accessoire accessoire = new Accessoire(new Pouce("10"), new Pouce("10"), new CoordPouce(new Pouce("10"), new Pouce("10")));
            ArrayList<Accessoire> listAccessoire = new ArrayList<>();
            listAccessoire.add(accessoire);
            cote.setAccessoires(listAccessoire);
            Accessoire accessoireGood = new Accessoire(new Pouce("5"), new Pouce("5"), new CoordPouce(new Pouce("20"), new Pouce("20")));
            Accessoire accessoireLeft = new Accessoire(new Pouce("10"), new Pouce("10"), new CoordPouce(new Pouce("5"), new Pouce("15")));
            Accessoire accessoireRight = new Accessoire(new Pouce("10"), new Pouce("10"), new CoordPouce(new Pouce("15"), new Pouce("15")));
            Accessoire accessoireTop = new Accessoire(new Pouce("10"), new Pouce("10"), new CoordPouce(new Pouce("15"), new Pouce("15")));
            Accessoire accessoireBot = new Accessoire(new Pouce("5"), new Pouce("5"), new CoordPouce(new Pouce("15"), new Pouce("15")));

            assertTrue(cote.doesAccessoireFitWithOtherAccessoires(accessoireGood));
            assertFalse(cote.doesAccessoireFitWithOtherAccessoires(accessoireLeft));
            assertFalse(cote.doesAccessoireFitWithOtherAccessoires(accessoireRight));
            assertFalse(cote.doesAccessoireFitWithOtherAccessoires(accessoireTop));
            assertFalse(cote.doesAccessoireFitWithOtherAccessoires(accessoireBot));
        }catch (Exception e){
            fail();
        }
    }
    @Test
    public void addSeparateurValid(){
        try{
            Cote cote = new Cote('N', new Pouce("30"), new Pouce("30"));
            cote.addSeparateur(new Pouce("12"));
            cote.addSeparateur(new Pouce("10"));
            cote.addSeparateur(new Pouce("29"));
            assertEquals(3, cote.getSeparateurs().size());
        }catch (Exception e){
            fail();
        }
    }
    @Test
    public void setSeparateurValid(){
        try{
            Cote cote = new Cote('N', new Pouce("30"), new Pouce("30"));
            cote.addSeparateur(new Pouce("12"));
            cote.addSeparateur(new Pouce("10"));
            cote.addSeparateur(new Pouce("29"));
            cote.setSeparateur(2, new Pouce("15"));
            assertEquals("15", cote.getSeparateurs().get(2).toString());
        }catch (Exception e){
            fail();
        }
    }
    @Test
    public void doesAccessoireFitWithSeparateur(){
        try{
            Cote cote = new Cote('N', new Pouce("30"), new Pouce("30"));
            cote.addSeparateur(new Pouce("10"));
            cote.addSeparateur(new Pouce("20"));
            cote.addSeparateur(new Pouce("25"));
            Accessoire accessoire = new Accessoire(new Pouce("10"), new Pouce("10"), new CoordPouce(new Pouce("5"), new Pouce("5")));
            cote.addAccessoire(accessoire);
            assertFalse(accessoire.isValid());
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void isPoidsMurTroisMurNoAccessorieIsValid(){
        try{
            Cote cote = new Cote('N', new Pouce("30"), new Pouce("30"));
            cote.addSeparateur(new Pouce("10"));
            cote.addSeparateur(new Pouce("20"));
            ArrayList<Mur> listeMur =  cote.getMurs(new Pouce("1"),new Pouce("1"),new Pouce("1"),new Pouce("1"),new Pouce("1"),45.0);

            Double poidsPanneauExtCoinDroit = listeMur.get(2).getPanneauExt().getPoids();
            Double poidsPanneauIntCoinDroit = listeMur.get(2).getPanneauInt().getPoids();
            assertThat(poidsPanneauIntCoinDroit).isEqualTo(16.099999999999998);
            assertThat(poidsPanneauExtCoinDroit).isEqualTo(22.80232322781408);


            Double poidsPanneauExtMurMilieu = listeMur.get(1).getPanneauExt().getPoids();
            Double poidsPanneauIntMurMilieu = listeMur.get(1).getPanneauInt().getPoids();
            assertThat(poidsPanneauExtMurMilieu).isEqualTo(20.3875);
            assertThat(poidsPanneauIntMurMilieu).isEqualTo(15.1375);

           Double poidsPanneauExtCoinGauche = listeMur.get(0).getPanneauExt().getPoids();
           Double poidsPanneauIntCoinGauche = listeMur.get(0).getPanneauInt().getPoids();
           assertThat(poidsPanneauExtCoinGauche).isEqualTo(22.80232322781408);
           assertThat(poidsPanneauIntCoinGauche).isEqualTo(16.099999999999998);

        }catch (Exception e){
            fail();
        }
    }
    @Test
    public void isPoidsMurDeuxMurNoAccessorieIsValid(){
        try{
            Cote cote = new Cote('N', new Pouce("30"), new Pouce("30"));
            cote.addSeparateur(new Pouce("15"));
            ArrayList<Mur> listeMur =  cote.getMurs(new Pouce("1"),new Pouce("1"),new Pouce("1"),new Pouce("1"),new Pouce("1"),45.0);

            Double poidsPanneauExtCoinGauche = listeMur.get(0).getPanneauExt().getPoids();
            Double poidsPanneauIntCoinGauche = listeMur.get(0).getPanneauInt().getPoids();
            assertThat(poidsPanneauExtCoinGauche).isEqualTo(29.36482322781408);
            assertThat(poidsPanneauIntCoinGauche).isEqualTo(24.412499999999998);

            Double poidsPanneauExtCoinDroit = listeMur.get(1).getPanneauExt().getPoids();
            Double poidsPanneauIntCoinDroit = listeMur.get(1).getPanneauInt().getPoids();
            assertThat(poidsPanneauIntCoinDroit).isEqualTo(24.412499999999998);
            assertThat(poidsPanneauExtCoinDroit).isEqualTo(29.36482322781408);

        }catch (Exception e){
            fail();
        }
    }
    @Test
    public void isPoidsMurOneMurNoAccessorieIsValid(){
        try{
            Cote cote = new Cote('N', new Pouce("30"), new Pouce("30"));
            ArrayList<Mur> listeMur =  cote.getMurs(new Pouce("1"),new Pouce("1"),new Pouce("1"),new Pouce("1"),new Pouce("1"),45.0);

            Double poidsPanneauExtCoinGauche = listeMur.get(0).getPanneauExt().getPoids();
            Double poidsPanneauIntCoinGauche = listeMur.get(0).getPanneauInt().getPoids();
            assertThat(poidsPanneauExtCoinGauche).isEqualTo(51.46714645562816);
            assertThat(poidsPanneauIntCoinGauche).isEqualTo(52.06249999999999);

        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void isPoidsMurOneMurWithFenetreIsValid(){
        try{
            Cote cote = new Cote('N', new Pouce("30"), new Pouce("30"));
            Accessoire accessoire = new Accessoire(new Pouce("9"), new Pouce("9"), new CoordPouce(new Pouce("5"), new Pouce("5")));
            accessoire.setType("Fenêtre");
            accessoire.setMarge(new Pouce("1"));
            accessoire.setInterieurOnly(false);
            cote.addAccessoire(accessoire);

            ArrayList<Mur> listeMur =  cote.getMurs(new Pouce("1"),new Pouce("1"),new Pouce("1"),new Pouce("1"),new Pouce("1"),45.0);

            Double poidsPanneauExtCoinGauche = listeMur.get(0).getPanneauExt().getPoids();
            Double poidsPanneauIntCoinGauche = listeMur.get(0).getPanneauInt().getPoids();
            assertThat(poidsPanneauExtCoinGauche).isEqualTo(46.173396455628165);
            assertThat(poidsPanneauIntCoinGauche).isEqualTo(46.76875);

        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void isPoidsMurOneMurWithPorteIsValid(){
        try{
            Cote cote = new Cote('N', new Pouce("30"), new Pouce("30"));
            Accessoire accessoire = new Accessoire(new Pouce("10"), new Pouce("10"), new CoordPouce(new Pouce("5"), new Pouce("5")));
            accessoire.setType("Porte");
            accessoire.setInterieurOnly(false);
            cote.addAccessoire(accessoire);

            ArrayList<Mur> listeMur =  cote.getMurs(new Pouce("1"),new Pouce("1"),new Pouce("1"),new Pouce("1"),new Pouce("1"),45.0);

            Double poidsPanneauExtCoinGauche = listeMur.get(0).getPanneauExt().getPoids();
            Double poidsPanneauIntCoinGauche = listeMur.get(0).getPanneauInt().getPoids();
            assertThat(poidsPanneauExtCoinGauche).isEqualTo( 47.09214645562816);
            assertThat(poidsPanneauIntCoinGauche).isEqualTo(47.68749999999999);

        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void isPoidsMurOneMurWithRetourAirIsValid(){
        try{
            Cote cote = new Cote('N', new Pouce("30"), new Pouce("30"));
            Accessoire accessoire = new Accessoire(new Pouce("3"), new Pouce("1/16"), new CoordPouce(new Pouce("5"), new Pouce("5")));
            accessoire.setType("Retour d'air");
            accessoire.setInterieurOnly(true);
            cote.addAccessoire(accessoire);

            ArrayList<Mur> listeMur =  cote.getMurs(new Pouce("1"),new Pouce("1"),new Pouce("1"),new Pouce("1"),new Pouce("1/16"),45.0);

            Double poidsPanneauExtCoinGauche = listeMur.get(0).getPanneauExt().getPoids();
            Double poidsPanneauIntCoinGauche = listeMur.get(0).getPanneauInt().getPoids();
            assertThat(poidsPanneauExtCoinGauche).isEqualTo(51.46714645562816);
            assertThat(poidsPanneauIntCoinGauche).isEqualTo(52.04609374999999);

        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void isPoidsMurOneMurWithPriseElecIsValid(){
        try{
            Cote cote = new Cote('N', new Pouce("30"), new Pouce("30"));
            Accessoire accessoire = new Accessoire(new Pouce("3"), new Pouce("3"), new CoordPouce(new Pouce("5"), new Pouce("5")));
            accessoire.setType("Prise électrique");
            accessoire.setInterieurOnly(true);
            cote.addAccessoire(accessoire);

            ArrayList<Mur> listeMur =  cote.getMurs(new Pouce("1"),new Pouce("1"),new Pouce("1"),new Pouce("1"),new Pouce("1/16"),45.0);

            Double poidsPanneauExtCoinGauche = listeMur.get(0).getPanneauExt().getPoids();
            Double poidsPanneauIntCoinGauche = listeMur.get(0).getPanneauInt().getPoids();
            assertThat(poidsPanneauExtCoinGauche).isEqualTo(51.46714645562816);
            assertThat(poidsPanneauIntCoinGauche).isEqualTo(51.668749999999996);

        }catch (Exception e){
            fail();
        }
    }
}
