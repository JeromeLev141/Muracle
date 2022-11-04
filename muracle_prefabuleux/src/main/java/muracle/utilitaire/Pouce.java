package muracle.utilitaire;

public class Pouce {

    private int entier;
    private Fraction fraction;

    public Pouce(int entier,int num, int denum) throws FractionError {
        this.entier = entier;
        this.fraction = new Fraction(num,denum);
        if(this.fraction.toDouble() < 0 && this.entier != 0){
            this.entier *= -1;
            this.fraction.mulRef(-1);
        }
    }

    public Pouce(int entier, Fraction fraction) throws FractionError {
        this.entier = entier;
        this.fraction = fraction.copy();
    }

    public Pouce(String mesure) throws PouceError,FractionError {
        String[] subString = mesure.trim().split(" ");

        if (subString.length == 2)
            try{
                this.entier = Integer.parseInt(subString[0]);
                String[] fraction = subString[1].split("/");
                this.fraction = new Fraction(Integer.parseInt(fraction[0]),Integer.parseInt(fraction[1]));
            }catch (NumberFormatException ex){
                throw new PouceError("la string d'entré est invalide");
            }

        else if (subString.length == 1)
            try{
                String[] fraction = subString[0].split("/");
                if (fraction.length == 1){
                    this.entier = Integer.parseInt(fraction[0]);
                    this.fraction = new Fraction(0,1);
                }
                else if(fraction.length == 2){
                    this.entier = 0;
                    this.fraction = new Fraction(Integer.parseInt(fraction[0]),Integer.parseInt(fraction[1]));
                }
                else
                    throw new PouceError("la string d'entré est invalide");

            }catch (NumberFormatException ex){
                throw new PouceError("la string d'entré est invalide");
            }
        else
            throw new PouceError("la string d'entré est invalide");



    }

    public int getEntier() {
        return entier;
    }

    public void setEntier(int entier) {
        this.entier = entier;
    }

    public Fraction getFraction() {
        return fraction;
    }

    public void setFraction(Fraction fraction) {
        this.fraction = fraction;
    }

    //opération d'addition par référence
    public Pouce addRef(Pouce pouce) throws FractionError {
        this.fraction = pouce2Fraction();
        entier = 0;
        fraction.addRef(pouce.pouce2Fraction());
        simplifier();
        return this;
    }

    //opération d'addition par référence copy
    public Pouce add(Pouce pouce) throws FractionError {
        return this.copy().addRef(pouce);
    }

    //Opération de soustraction par référence
    public Pouce subRef(Pouce pouce) throws FractionError {
        this.fraction = pouce2Fraction();
        entier = 0;
        fraction.subRef(pouce.pouce2Fraction());
        System.out.println(fraction.toString());
        simplifier();
        return this;
    }

    //Opération de soustraction par référence copy
    public Pouce sub(Pouce pouce) throws FractionError {
        return this.copy().subRef(pouce);
    }

    //Oprétation de multiplication par référence
    public Pouce mulRef(Pouce pouce) throws FractionError {
        this.fraction = pouce2Fraction();
        entier = 0;
        fraction.mulRef(pouce.pouce2Fraction());
        simplifier();
        return this;
    }

    //Opération de multiplication par référence
    public Pouce mulRef(Fraction fraction) throws FractionError {
        this.fraction = pouce2Fraction();
        entier = 0;
        fraction.mulRef(fraction);
        simplifier();
        return this;
    }

    //Opération de multiplication par référence copy
    public Pouce mul(Pouce pouce) throws FractionError {
        return this.copy().mulRef(pouce);
    }

    //Opération de multiplication par référence copy
    public Pouce mul(Fraction fraction) throws FractionError {
        return this.copy().mulRef(fraction);
    }

    //Opération de division par référence
    public Pouce divRef(Pouce pouce) throws FractionError {
        this.fraction = pouce2Fraction();
        entier = 0;
        fraction.divRef(pouce.pouce2Fraction());
        simplifier();
        return this;
    }

    //Opération de devision par référence copy
    public Pouce div(Pouce pouce) throws FractionError {
        return this.copy().divRef(pouce);
    }

    //Opération de ==
    public boolean equals(Pouce pouce){
        return (entier + fraction.toDouble() == pouce.getEntier() + pouce.getFraction().toDouble());
    }

    //Opération de comparaison -1<,0==,1>
    public int compare(Pouce pouce){
        return Double.compare(entier + fraction.toDouble(), pouce.getEntier() + pouce.getFraction().toDouble());
    }

    //Fonction de copy d'objet
    public Pouce copy() throws FractionError {
        return new Pouce(this.entier,this.fraction);
    }

    //Fonction pour simplifier l'entier avec fraction
    public void simplifier(){
        if (this.entier == 0 && this.fraction.toDouble() < 0){
            entier += fraction.getNum()/fraction.getDenum();
            fraction.setNum(fraction.getNum()%fraction.getDenum());
            if (entier != 0)
                fraction.mulRef(-1);
            this.fraction.simplifier();
        }
        else if (this.entier < 0){
            entier -= fraction.getNum()/fraction.getDenum();
            fraction.setNum(fraction.getNum()%fraction.getDenum());
            fraction.simplifier();

            if(this.fraction.toDouble() < 0){
                this.entier *= -1;
                this.fraction.mulRef(-1);
            }
        }
        else{
            entier += fraction.getNum()/fraction.getDenum();
            fraction.setNum(fraction.getNum()%fraction.getDenum());
            fraction.simplifier();

            if(this.fraction.toDouble() < 0){
                this.entier *= -1;
                this.fraction.mulRef(-1);
            }
        }
    }

    //Fonction qui formate pouce à Fraction
    private Fraction pouce2Fraction() throws FractionError {
        Fraction fract = this.fraction.copy();
        fract.setNum(fract.getNum() + entier*fract.getDenum());
        return fract;
    }

    public double toDouble(){
        return entier + fraction.toDouble();
    }

    @Override
    public String toString() {
        if (fraction.toDouble() == 0)
            return entier + "";
        else if(entier == 0)
            return  fraction.toString();
        else
            return entier + " " + fraction.toString();
    }
}
