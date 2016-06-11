package org.hsrt.mc.emergency.user;

/**
 * Created by KA on 14.05.2016.
 */
public class Medication {

    private String name;

    /**
     * The dosis per taking, e.g. mg, ml, 3 pills
     */
    private String dosis;

    private String manufacturer;

    /**
     * Descibes if medication needs to be taken once, twice or trice a day
     * TODO Irregularity must be covered (such as Insulin: only when high on blood sugar)
     */
    private int amountPerDay;


    public Medication(){

    }


    public String getMedicationText(){
        String medicationText = "";
        medicationText += "Ich benoetige das Medikament" + this.name + ", davon " + this.dosis + " und das " + this.amountPerDay + "taeglich";
        return medicationText;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getAmountPerDay() {
        return amountPerDay;
    }

    public void setAmountPerDay(int amountPerDay) {
        this.amountPerDay = amountPerDay;
    }
}
