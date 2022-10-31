package muracle;


import muracle.utilitaire.Fraction;
import muracle.utilitaire.FractionError;
import muracle.utilitaire.Pouce;
import muracle.utilitaire.PouceError;
import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.*;

public class PouceTest {

    @Test
    public void pouce(){
        try{
            Pouce pouce1 = new Pouce(0,1,4);
            assertThat(pouce1.getEntier()).isEqualTo(0);
            assertThat(pouce1.getFraction().toDouble()).isEqualTo(0.25);

            Pouce pouce2 = new Pouce(0,new Fraction(1,2));
            assertThat(pouce2.getEntier()).isEqualTo(0);
            assertThat(pouce2.getFraction().toDouble()).isEqualTo(0.5);

            Pouce pouce3 = new Pouce("1 3/4");
            assertThat(pouce3.getEntier()).isEqualTo(1);
            assertThat(pouce3.getFraction().toDouble()).isEqualTo(0.75);

            Pouce pouce4 = new Pouce(" 1 3/4 ");
            assertThat(pouce4.getEntier()).isEqualTo(1);
            assertThat(pouce4.getFraction().toDouble()).isEqualTo(0.75);

            Pouce pouce5 = new Pouce("532");
            assertThat(pouce5.getEntier()).isEqualTo(532);
            assertThat(pouce5.getFraction().toDouble()).isEqualTo(0);

            Pouce pouce6 = new Pouce("-3/4");
            assertThat(pouce6.getEntier()).isEqualTo(0);
            assertThat(pouce6.getFraction().toDouble()).isEqualTo(-0.75);

        }catch (FractionError | PouceError error) {
            fail();
        }
        try{
            new Pouce(0,1,0);
            fail();
        }catch (FractionError error) {
            assertTrue(true);
        }
        try{
            new Pouce(" as -15/asd");
            fail();
        }catch (FractionError | PouceError error) {
            assertTrue(true);
        }
        try{
            new Pouce("4a5");
            fail();
        }catch (FractionError | PouceError error) {
            assertTrue(true);
        }
        try{
            new Pouce("1234/asd");
            fail();
        }catch (FractionError | PouceError error) {
            assertTrue(true);
        }
    }

    @Test
    public void simplifier(){
        try{
            Pouce pouce1 = new Pouce(0,-10,3);
            pouce1.simplifier();
            //System.out.println(pouce1.getFraction().toString());
            assertThat(pouce1.getFraction().equals(new Fraction(1,3))).isTrue();
            assertThat(pouce1.getEntier()).isEqualTo(-3);

            Pouce pouce2 = new Pouce(-2,15,3);
            pouce2.simplifier();
            //System.out.println(pouce2.getFraction().toString());
            assertThat(pouce2.getFraction().equals(new Fraction(0,1))).isTrue();
            assertThat(pouce2.getEntier()).isEqualTo(-7);

            Pouce pouce3 = new Pouce(-1,-4,-6);
            pouce3.simplifier();
            //System.out.println(pouce3.getFraction().toString());
            assertThat(pouce3.getFraction().equals(new Fraction(2,3))).isTrue();
            assertThat(pouce3.getEntier()).isEqualTo(-1);

            Pouce pouce4 = new Pouce(-1,-8,6);
            pouce4.simplifier();
            //System.out.println(pouce4.getFraction().toString());
            assertThat(pouce4.getFraction().equals(new Fraction(1,3))).isTrue();
            assertThat(pouce4.getEntier()).isEqualTo(2);

            Pouce pouce5 = new Pouce(-1,8,6);
            pouce5.simplifier();
            //System.out.println(pouce5.getFraction().toString());
            assertThat(pouce5.getFraction().equals(new Fraction(1,3))).isTrue();
            assertThat(pouce5.getEntier()).isEqualTo(-2);

            Pouce pouce6 = new Pouce(0,8,6);
            pouce6.simplifier();
            //System.out.println(pouce6.getFraction().toString());
            assertThat(pouce6.getFraction().equals(new Fraction(1,3))).isTrue();
            assertThat(pouce6.getEntier()).isEqualTo(1);
        }catch (FractionError fractionError) {
            fail(fractionError.toString());
        }

    }

    @Test
    public void setEntier(){
        try {
            Pouce pouce1 = new Pouce(0, 1, 4);
            assertThat(pouce1.getEntier()).isEqualTo(0);
            pouce1.setEntier(2);
            assertThat(pouce1.getEntier()).isEqualTo(2);
        }catch (FractionError fractionError){
            fail();
        }
    }

    @Test
    public void setFractino(){
        try {
            Pouce pouce1 = new Pouce(3, 1, 4);
            assertThat(pouce1.getFraction().toDouble()).isEqualTo(0.25);
            pouce1.setFraction(new Fraction(1,2));
            assertThat(pouce1.getFraction().toDouble()).isEqualTo(0.5);
        }catch (FractionError fractionError){
            fail();
        }
    }

    @Test
    public void equals(){
        try {
            Pouce pouce1 = new Pouce(1, 1, 2);
            assertThat(pouce1.equals(new Pouce(0,3,2))).isTrue();
        }catch (FractionError fractionError){
            fail();
        }
    }

    @Test
    public void compare(){
        try {
            Pouce pouce1 = new Pouce(1, 1, 2);
            Pouce pouce2 = new Pouce(1, 3, 7);
            Pouce pouce3 = new Pouce(5, 1, 2);
            assertThat(pouce1.compare(pouce1)).isEqualTo(0);
            assertThat(pouce1.compare(pouce2)).isEqualTo(1);
            assertThat(pouce1.compare(pouce3)).isEqualTo(-1);
        }catch (FractionError fractionError){
            fail();
        }
    }

    @Test
    public void setFraction(){
        try {
            Pouce pouce1 = new Pouce(0, 1, 4);
            assertThat(pouce1.getFraction().toDouble()).isEqualTo(0.25);
            pouce1.setFraction(new Fraction(1,10));
            assertThat(pouce1.getFraction().toDouble()).isEqualTo(0.1);

        }catch (FractionError fractionError){
            fail();
        }
    }

    @Test
    public void addRef(){
        try {
            Pouce pouce1 = new Pouce(1,1,4);
            Pouce pouce2 = new Pouce(2,1,5);
            Pouce pouce3 = new Pouce(0,-1,2);
            pouce1.addRef(pouce2);
            assertThat(pouce1.equals(new Pouce(3,9,20))).isTrue();
            pouce1.addRef(pouce3);
            assertThat(pouce1.equals(new Pouce(2,19,20))).isTrue();
        }catch (FractionError fractionError){
            fail();
        }
    }

    @Test
    public void add(){
        try {
            Pouce pouce1 = new Pouce(1,1,4);
            Pouce pouce2 = new Pouce(2,1,5);
            Pouce pouce3 = new Pouce(0,-1,2);
            Pouce pouce4 = pouce1.add(pouce2);
            assertThat(pouce1.equals(new Pouce(1,1,4))).isTrue();
            assertThat(pouce4.equals(new Pouce(3,9,20))).isTrue();
            Pouce pouce5 = pouce3.add(pouce4);
            assertThat(pouce3.equals(new Pouce(0,-1,2))).isTrue();
            assertThat(pouce5.equals(new Pouce(2,19,20))).isTrue();
        }catch (FractionError fractionError){
            fail();
        }
    }

    @Test
    public void subRef(){
        try {
            Pouce pouce1 = new Pouce(1,1,4);
            Pouce pouce2 = new Pouce(2,1,5);
            Pouce pouce3 = new Pouce(0,-1,2);
            pouce1.subRef(pouce2);
            assertThat(pouce1.equals(new Pouce(0,-19,20))).isTrue();
            pouce1.subRef(pouce3);
            assertThat(pouce1.equals(new Pouce(0,-9,20))).isTrue();
        }catch (FractionError fractionError){
            fail();
        }
    }

    @Test
    public void sub(){
        try {
            Pouce pouce1 = new Pouce(1,1,4);
            Pouce pouce2 = new Pouce(2,1,5);
            Pouce pouce3 = new Pouce(0,-1,2);
            Pouce pouce4 = pouce1.sub(pouce2);
            assertThat(pouce1.equals(new Pouce(1,1,4))).isTrue();
            assertThat(pouce4.equals(new Pouce(0,-19,20))).isTrue();
            Pouce pouce5 = pouce4.sub(pouce3);
            assertThat(pouce4.equals(new Pouce(0,-19,20))).isTrue();
            assertThat(pouce5.equals(new Pouce(0,-9,20))).isTrue();
        }catch (FractionError fractionError){
            fail();
        }
    }

    @Test
    public void mulRef(){
        try {
            Pouce pouce1 = new Pouce(1,1,4);
            Pouce pouce2 = new Pouce(2,1,5);
            Pouce pouce3 = new Pouce(0,-1,2);
            pouce1.mulRef(pouce2);
            assertThat(pouce1.equals(new Pouce(2,3,4))).isTrue();
            pouce1.mulRef(pouce3);
            assertThat(pouce1.equals(new Pouce(-1,3,8))).isTrue();
        }catch (FractionError fractionError){
            fail();
        }
    }

    @Test
    public void mul(){
        try {
            Pouce pouce1 = new Pouce(1,1,4);
            Pouce pouce2 = new Pouce(2,1,5);
            Pouce pouce3 = new Pouce(0,-1,2);
            Pouce pouce4 = pouce1.mul(pouce2);
            assertThat(pouce1.equals(new Pouce(1,1,4))).isTrue();
            assertThat(pouce4.equals(new Pouce(2,3,4))).isTrue();
            Pouce pouce5 = pouce4.mul(pouce3);
            assertThat(pouce4.equals(new Pouce(2,3,4))).isTrue();
            assertThat(pouce5.equals(new Pouce(-1,3,8))).isTrue();
        }catch (FractionError fractionError){
            fail();
        }
    }

    @Test
    public void divRef(){
        try {
            Pouce pouce1 = new Pouce(1,1,4);
            Pouce pouce2 = new Pouce(2,1,5);
            Pouce pouce3 = new Pouce(0,-1,2);
            pouce1.divRef(pouce2);
            assertThat(pouce1.equals(new Pouce(0,25,44))).isTrue();
            pouce1.divRef(pouce3);
            assertThat(pouce1.equals(new Pouce(-1,3,22))).isTrue();
        }catch (FractionError fractionError){
            fail();
        }
    }

    @Test
    public void div(){
        try {
            Pouce pouce1 = new Pouce(1,1,4);
            Pouce pouce2 = new Pouce(2,1,5);
            Pouce pouce3 = new Pouce(0,-1,2);
            Pouce pouce4 = pouce1.div(pouce2);
            assertThat(pouce1.equals(new Pouce(1,1,4))).isTrue();
            assertThat(pouce4.equals(new Pouce(0,25,44))).isTrue();
            Pouce pouce5 = pouce4.div(pouce3);
            assertThat(pouce4.equals(new Pouce(0,25,44))).isTrue();
            assertThat(pouce5.equals(new Pouce(-1,3,22))).isTrue();
        }catch (FractionError fractionError){
            fail();
        }
    }


}
