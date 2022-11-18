package muracle;


import muracle.domaine.Accessoire;
import muracle.domaine.Cote;
import muracle.domaine.CoteError;
import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.Pouce;
import org.junit.Test;

import java.util.ArrayList;

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
            cote.addSeparateur(new Pouce("30"));
            cote.addSeparateur(new Pouce("10"));
            cote.addSeparateur(new Pouce("29"));
            assertEquals(4, cote.getSeparateurs().size());
        }catch (Exception e){
            fail();
        }
    }
    @Test
    public void setSeparateurValid(){
        try{
            Cote cote = new Cote('N', new Pouce("30"), new Pouce("30"));
            cote.addSeparateur(new Pouce("12"));
            cote.addSeparateur(new Pouce("30"));
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
}
