package org.hsrt.mc.emergency.user;

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

    public String getBloodType(){
        return this.bloodType;
    }

    public String getMedication(){
        String allMedicationText = "";
        for( Medication m: medication )
        {
            allMedicationText += m.getMedicationText();
        }
        return allMedicationText;
    }

    public void initTestData() {
        this.firstName = "Andy";
        this.lastName = "Müller";
        this.bloodType = BloodType.A_NEG;
        this.dateOfBirth = new Date(1980,5,10);

        Medication medication = new Medication();
        medication.setName("Vagisil");
        medication.setManufacturer("Vagisil Inc.");
        medication.setAmountPerDay(3);
        medication.setDosis("20g/Use");

        this.medication.add(medication);

        this.diseases.add("Vaginal-Herpes");
        this.specialNeeds.add("Pregnant");

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
