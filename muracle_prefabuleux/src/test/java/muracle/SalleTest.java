package muracle;

import muracle.domaine.Salle;


import muracle.utilitaire.FractionError;
import muracle.utilitaire.Pouce;
import muracle.utilitaire.PouceError;
import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.*;

public class SalleTest
{
    @Test
    public void setDistanceTrouRetourAir() {
        try{
            Salle salle = new Salle(new Pouce("10"), new Pouce("10"), new Pouce("12"), new Pouce("15"));
            salle.setDistanceTrouRetourAir(new Pouce("17"));
            assertTrue(salle.getDistanceTrouRetourAir().equals(new Pouce("17")));
        }catch (Exception e){
            fail();
        }
    }
    @Test
    public void setLargeur(){
        try{
            Salle salle = new Salle(new Pouce("10"),
                                    new Pouce("10"),
                                    new Pouce("12"),
                                    new Pouce("15"));
            salle.setLargeur(new Pouce("15"));
            assertTrue(salle.getLargeur().equals(new Pouce("15")));
            assertTrue(salle.getCote('N').getLargeur().equals(new Pouce("15")));
            assertTrue(salle.getCote('S').getLargeur().equals(new Pouce("15")));
        }catch (Exception e){
            fail();
        }
    }
    @Test
    public void setHauteur(){
        try{
            Salle salle = new Salle(new Pouce("10"),
                                    new Pouce("10"),
                                    new Pouce("12"),
                                    new Pouce("15"));
            salle.setHauteur(new Pouce("15"));
            assertTrue(salle.getHauteur().equals(new Pouce("15")));
            assertTrue(salle.getCote('N').getHauteur().equals(new Pouce("15")));
            assertTrue(salle.getCote('S').getHauteur().equals(new Pouce("15")));
            assertTrue(salle.getCote('E').getHauteur().equals(new Pouce("15")));
            assertTrue(salle.getCote('W').getHauteur().equals(new Pouce("15")));
        }catch (Exception e){
            fail();
        }
    }
    @Test
    public void setLongueur(){
        try{
            Salle salle = new Salle(new Pouce("10"),
                                    new Pouce("10"),
                                    new Pouce("12"),
                                    new Pouce("15"));
            salle.setLongueur(new Pouce("15"));
            assertTrue(salle.getLongueur().equals(new Pouce("15")));
            assertTrue(salle.getCote('E').getLargeur().equals(new Pouce("15")));
            assertTrue(salle.getCote('W').getLargeur().equals(new Pouce("15")));
        }catch (Exception e){
            fail();
        }
    }
}
