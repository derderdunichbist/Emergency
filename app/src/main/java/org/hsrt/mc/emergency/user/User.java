package org.hsrt.mc.emergency.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by KA on 14.05.2016.
 */
public class User {

    private String firstName;
    private String lastName;
    private String bloodType;
    private Date dateOfBirth;
    private List<Medication> medication;
    private List<String> diseases;
    private List<String> specialNeeds;
    private List<Contact> contacts;


    public User(){

    }

    public String getFirstName(){
        return this.firstName;
    }

    public String getLastName(){
        return this.lastName;
    }

    //return the bloodType of the user
    public String getBloodType(){
        return this.bloodType;
    }

    //return all medication of the user
    public String getMedication(){
        String allMedicationText = "";
        for( Medication m: medication )
        {
            allMedicationText += m.getMedicationText();
            allMedicationText += "\n";
        }
        return allMedicationText;
    }

    //return all allDiseases of the user
    public String getDiseases(){
        String allDiseases = "";
        for ( String d: diseases){
            allDiseases += d;
            allDiseases += "; ";
        }
        return allDiseases;
    }

    //return all allSpecialNeeds of the user
    public String getSpecialNeeds(){
        String allSpecialNeeds = "";
        for (String s: specialNeeds){
            allSpecialNeeds += s;
            allSpecialNeeds += "; ";
        }
        return allSpecialNeeds;
    }


    public void initTestData() {
        this.firstName = "Andy";
        this.lastName = "Müller";
        this.bloodType = BloodType.A_NEG;
        this.dateOfBirth = new Date(1980,5,10);

        this.medication = new ArrayList<Medication>();


        Medication medication1 = new Medication();
        medication1.setName("Vagisil");
        medication1.setManufacturer("Vagisil Inc.");
        medication1.setAmountPerDay(3);
        medication1.setDosis("20g/Use");
        this.medication.add(medication1);

        Medication medication2 = new Medication();
        medication2.setName("Asperin");
        medication2.setManufacturer("DIEFIRMA Inc.");
        medication2.setAmountPerDay(5);
        medication2.setDosis("20g/Use");
        this.medication.add(medication2);

        this.diseases = new ArrayList<String>();
        this.diseases.add("Mamaschnuckler");
        this.diseases.add("Krankheit2");

        this.specialNeeds = new ArrayList<String>();
        this.specialNeeds.add("Schokoladenesser");
        this.specialNeeds.add("Viel Schokoladenesser");
    }

    public void addContact(Contact contact) {
        if(!contacts.contains(contact)) {
            this.addContact(contact);
        }
    }

    public void setNameFromCompleteName(String name) {
        String[] names = name.split(" ");
        this.firstName = names[0];

        if(names.length > 1) {
            this.lastName = names[1];
        }
     }

}
