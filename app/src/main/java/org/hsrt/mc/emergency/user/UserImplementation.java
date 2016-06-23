package org.hsrt.mc.emergency.user;

import org.hsrt.mc.emergency.activities.MainActivity;
import org.hsrt.mc.emergency.persistence.UserDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by KA on 14.05.2016.
 */
public class UserImplementation implements User{

    private String firstName;
    private String lastName;
    private String bloodType;
    private Date dateOfBirth;
    private List<Medication> medication = new ArrayList<>();
    private List<String> diseases = new ArrayList<>();
    private List<String> specialNeeds = new ArrayList<>();
    private UserDAO userDAO;
    private List<Contact> contacts = new ArrayList<>();

    /**
     * Singleton constant
     */
    private static User userObject;


    public UserImplementation(){

    }

    public UserImplementation(UserDAO userDAO) {
        this.userDAO = userDAO;
        userObject = this;
    }

    @Override
    public String getMedication(){
        String allMedicationText = "";
        for( Medication m: medication )
        {
            allMedicationText += m.getMedicationText();
            allMedicationText += "\n";
        }
        return allMedicationText;
    }

    @Override
    public String getDiseases(){
        String allDiseases = "";
        for ( String d: diseases){
            allDiseases += d;
            allDiseases += "; ";
        }
        return allDiseases;
    }

    @Override
    public String getSpecialNeeds(){
        String allSpecialNeeds = "";
        for (String s: specialNeeds){
            allSpecialNeeds += s;
            allSpecialNeeds += "; ";
        }
        return allSpecialNeeds;
    }

    @Override
    public String getContacts() {
        //TODO
        return null;
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

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        userDAO.updateUserFirstName(firstName);
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
        userDAO.updateUserLastName(lastName);
    }

    @Override
    public String getBloodType() {
        return bloodType;
    }

    @Override
    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
        userDAO.updateUserBloodType(bloodType);
    }

    @Override
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        userDAO.updateUserDob(dateOfBirth);
    }

    @Override
    public void addContact(Contact contact) {
        if(!contacts.contains(contact)) {
            this.contacts.add(contact);
            userDAO.addContact(contact);
        }
    }

    @Override
    public void addMedication(Medication med) {
        if(!medication.contains(med)) {
            this.medication.add(med);
            userDAO.addMedication(med);
        }
    }

    @Override
    public void addSpecialNeed(String specialNeed) {
        if(!specialNeeds.contains(specialNeed)) {
            this.specialNeeds.add(specialNeed);
            userDAO.addSpecialNeed(specialNeed);
        }
    }

    @Override
    public void addDisease(String disease) {
        if(!diseases.contains(disease)) {
            this.diseases.add(disease);
            userDAO.addDiseases(disease);
        }
    }

    @Override
    public void removeContact(Contact contact) {
        if(!contacts.contains(contact)) {
            this.contacts.add(contact);
            userDAO.addContact(contact);
        }
    }

    @Override
    public void removeMedication(Medication med) {
        if(!medication.contains(med)) {
            this.medication.add(med);
            userDAO.addMedication(med);
        }
    }

    @Override
    public void removeSpecialNeed(String specialNeed) {
        if(!specialNeeds.contains(specialNeed)) {
            this.specialNeeds.add(specialNeed);
            userDAO.addSpecialNeed(specialNeed);
        }
    }

    @Override
    public void removeDisease(String disease) {
        if(!diseases.contains(disease)) {
            this.diseases.add(disease);
            userDAO.addDiseases(disease);
        }
    }

    public static User getUserObject() {
        return userObject;
    }


}
