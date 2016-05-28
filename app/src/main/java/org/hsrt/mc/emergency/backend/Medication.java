package org.hsrt.mc.emergency.backend;

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
}
